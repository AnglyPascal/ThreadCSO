package io.threadcso.semaphore

import java.util.concurrent.atomic._
import java.util.concurrent.locks.LockSupport
import io.threadcso.basis.{Identity, NameGenerator}

/** A synchronization object that supports a ''single'' thread waiting until
  * some other thread enables it to proceed. Functionally equivalent to (but
  * considerably more efficient than) a `BooleanSemaphore(false)` that can be
  * `release`d once.
  */

class Flag(var name: String = null) extends Semaphore {
  name = Flag.genName(name)
  private[this] val _available = new AtomicBoolean(false)
  private[this] val _waiting = new AtomicReference[Thread]
  @volatile
  private[this] var _interrupted = false

  override def toString: String =
    s"""FLAG $name: ${if (_available.get) "available" else "unavailable"} ${if (
        _interrupted
      ) "[cancelled]"
      else ""} [waiter: ${_waiting.get.identity}]"""

  /** Interrupt a stalled acquiring thread
    */
  override def cancel(): Unit = {
    _interrupted = true
    val stalled = _waiting.getAndSet(null)
    if (stalled != null) { stalled.interrupt(); LockSupport.unpark(stalled) }
    _available.set(true)
  }

  /** Causes the current thread to wait until the flag is available. It is an
    * error to invoke `acquire` or `tryAcquire` more than once on an unavailable
    * flag.
    */
  def acquire(): Unit = {
    if (_available.get) return
    val current = Thread.currentThread
    if (!_waiting.compareAndSet(null, current))
      throw new IllegalStateException(
        s"$current cannot await already awaited: ${this} "
      )
    while (!_available.get) LockSupport.park(this)
    ()
  }

  override def cancelled(): Boolean = _interrupted

  /** Causes the current thread to wait until the flag is `release`d (returning
    * true) or until the specified timeout has elapsed (returning false). It is
    * an error to invoke `acquire` or `tryAcquire` more than once on an
    * unavaliable flag.
    */
  override def tryAcquire(timeoutNS: Long): Boolean = {
    if (_available.get) return true

    val current = Thread.currentThread
    val deadline = timeoutNS + System.nanoTime
    var waiting, outcome = true
    if (!_waiting.compareAndSet(null, current))
      throw new IllegalStateException(
        s"$current cannot await already awaited: ${this} "
      )

    while (waiting) {
      if (_available.get) { waiting = false }
      else {
        val left = deadline - System.nanoTime
        if (left <= 0L) { outcome = false; waiting = false }
        else {
          LockSupport.parkNanos(this, left)
          // ASSERT: deadline expired || unparked
          if (deadline < System.nanoTime) { outcome = false; waiting = false }
          if (Thread.interrupted()) { waiting = false; _interrupted = true }
        }
      }
    }
    return outcome
  }

  @inline private[semaphore] def _getWaiting: Thread = _waiting.get

  /** Raise the flag; permitting an `acquire`ing process to proceed. It is an
    * error for a `Flag` that has already been `release`d to be `release`d
    * again.
    */
  def release(): Unit = {
    if (_available.compareAndSet(false, true)) // previous value was false
      LockSupport.unpark(_waiting.get)
    else
      throw new IllegalStateException("Flag already released: " + toString)
  }

}

object Flag extends NameGenerator("Flag") {
  def apply(name: String = null): Flag = new Flag(name)
}
