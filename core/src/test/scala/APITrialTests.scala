import io.SourceLocation.SourceLocation
import io.threadcso._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random

abstract class APITests(implicit loc: SourceLocation) {
  val shutDown: Thread = new Thread() {
    override def run(): Unit = { Console.println("Shut down") }
  }

  def job(args: Array[String]): String

  def apply(args: Array[String]): String = {
    var s = ""
    run(proc("Runtime System") {} || proc("job") { s = job(args) })
    s
  }

  java.lang.Runtime.getRuntime.addShutdownHook(shutDown)

  println(s"Runnable $loc")
  println(debugger)
}

object PARTest extends APITests {
  def job(args: Array[String]): String = {
    var s = ""
    val procs =
      for (arg <- args) yield proc(arg) {
        s += arg + ", "
        sleep(1000)
      }
    val p = ||(procs)
    p()
    s += "-"
    p()
    s += "-"
    par(List(p, p))
    s += "-"
    run(p || p)
    s += "-"
    s
  }
}

class APITrialTests extends AnyFlatSpec {

  private val N = 100

  private val alpha =
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

  protected def randStr(M: Int) = {
    val n = Random.nextInt(M) + 2
    (1 to n).map(_ => alpha(Random.nextInt(alpha.length))).mkString
  }

  it should "run PAR properly" in {
    val args = Array.fill(N)(randStr(10))
    val s = PARTest(args)
    // assert (s == args.mkString(", ") + ", ----")
  }

  it should "run virtual threads" in {
    import java.lang.Thread
    Thread.startVirtualThread(
      () => {
        System.out.println("Hello World");
      }
    );
  }
}
