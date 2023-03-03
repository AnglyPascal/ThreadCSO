# Some CSO examples

## Dining Philosophers

Except for `Phils.scala`, the `.scala` programs are his solutions
to a practical set often by Bernard Sufrin at Oxford (2007-20017).
`Phils.scala` is a deadlocking non-solution that illustrates the
problem. It is very easy to change the number of Philosphers
(in the source text).


### The Complete Practical Briefing

It is recommended that you read the material from the section of
the course notes on the Dining Philosophers before you attempt this
practical. The code from the lectures is on the course website. The
aim of the practical is to investigate some variants of the Dining
Philosophers, which aim to avoid deadlocks. You should implement
the first three variants below, and as many of the optional variants
you have the inclination to work on.

**Variant 1: a right-handed philosopher**

In the standard version of the dining philosophers, all the
philosophers are left-handed: they pick up their left fork first.
Implement a variant where one of the philosophers is right-handed,
i.e.she picks up her right fork first.

**Variant 2: using a butler**

Now consider a variant using an extra process, which represents a
butler. The butler makes sure that no more than four philosophers
are ever simultaneously seated.

**Variant 3: asking the forks using a protocol**

Now consider a variant where, if a philosopher is unable to obtain
her second fork, she puts down her first fork, and re-tries later.
This can be solved straighforwardly by implementing a bidirectional
protocol between forks and philosophers: a philosopher should be
able ask a fork to commit itself to her, and be answered positively
only if the fork has actually committed to her; negatively if the
fork has already committed to its other potential user. \[Hint: the
protocol will need two channels; it will simplify your program
structure to package them in a class.\]

**Optional, but desirable**

For each of the variants you implement include a way of counting
the number of times each philosopher has eaten, and of presenting
all this information periodically in addition to, or instead of,
the detailed reports of the actions of forks and philosophers. Think
of this as insurance against starvation! The same code should suffice
for all variants.

**Variant (optional: for fun): pile the forks in the middle of the
table**

In this variant, forks that are not being used sit in a pile in the
middle of the table, and philosophers can use any two of them to
eat.

**Variant (optional: timeouts)**

Now consider a variant where, if a philosopher is unable to obtain
her second fork, she puts down her first fork, and re-tries later.
Hint: This can be solved straighforwardly by using the deadlined
'writeBefore' method in the channel from a philosopher to one of
her forks. It may be worth doing some simple experiments in advance
to see how deadlined writes/reads interact with alternations. My
own solution used a deadline that was roughly twice the eating time.


**Reporting**

Your report should be in the form of a well-commented program, together
with a brief discussion of any design considerations and of your
results.

## Particles

Simple example of a hybrid concurrent implementation using 
[ThreadCSO](https://github.com/sufrin/ThreadCSO)
barriers, semaphores, and channels.


A demonstration of pseudo-gravitational particle calculations done by several workers
in lock-step, coordinated by barriers, with (many) parameters of the simulation
settable from the GUI.  
  
  1. The particles move in a closed container with energy absorbent
  walls. 

  1. There is an upper bound on the speed to which they can accelerate

  1. Clicking on a particle increases its density by an order of
  magnitude (and changes its hue "redward". The mass of a particle
  is calculated in the usual way from its radius, viz:
  density×(4/3)πR&#x00B3;
  
  1. Control-clicking on a particle  decreases its density by an order of magnitude.
  
  1. One or more particles can be selected (or deselected) by Command-cllicking. Selected
  particles can be nudged, when the simulation is not running, by using the direction keys
  on the keyboard. 
  
  1. Coefficients of restitution of walls and particles can be set
  interactively or as the application begins.

  1. δt is the simulated elapsed time between computed frames

  1. FPS is the (target) number of frames to be shown per elapsed
  second of real time This is really here to test the efficiency
  of our scala code, and under some circumstances it may not be
  reached; but this doesn't damage the simulation.

  1. The number of particles is the product of P (the number of
  worker processes), and 2×S (the number of particles managed per
  worker process).

Make the jar file from the shell with `make` (make sure you have
set up the `make.properties` file properly with the class path of
the threadcso library, etc.)

Usage (from sbt) is

`package examples; runMain Particles` [*args*], where each *arg* is one of

   S «S: int» (default 1) each worker manages 2xS particles

   P «P: int» (default 4) number of worker processes

   t «∂t: double» (default 3.0) time quantum for simulation

   s «scale: int» (default -11) order of magnitude of 'gravitational' constant: G = 6.79E<scale>

   W «Wall: double» (default 0.9) When it hits a wall a particle's momentum *= -Wall.

   B «Ball: double» (default 1.0) force multiplier for touching particles [negative => repulsion]

   FPS «FPS: int» (600) frames to be shown per second (target value)

   w=«int» (800) width of the arena (units)

   h=«int» (700) height of the arena (units)

   C «CF: double» (16.0) particle speed is limited to `|`(width/CF∂t, height/CF∂t)`|`.

   -d enable the debugger (false)

   `--` Remaining parameters are particle specs of the form `<radius>@<x>,<y>`. If none are given, then 2×P×S are made at random.

For example:

        runMain Particles s -9 P 40                # 80 particles, G = 6.79E-9
        runMain Particles S 4  P 40  w=1800 h=1000 # 320 particles, G = 6.79E-11 

Things can get a bit hectic when gravitation is high and there are lots of
particles; but you can moderate behaviour from the control panel of the
viewer. When there are too many particles on a small screen they can become
indistinguishable -- the screen just looks like a greenish blob. Some action can
be generated by changing the densities of one or more particles (just click on
the green a few times at random).
 


  
Bernard Sufrin, Oxford, 2011 and 2017

