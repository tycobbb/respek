package dev.wizrad.respek.runners

import dev.wizrad.respek.graph.Respek
import dev.wizrad.respek.graph.ExampleGroup
import dev.wizrad.respek.graph.Example
import dev.wizrad.respek.graph.throwables.ContextFailure
import dev.wizrad.respek.graph.throwables.HookFailure
import dev.wizrad.respek.graph.throwables.StatusFailure
import dev.wizrad.respek.graph.throwables.TestFailure
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import java.io.Serializable

abstract class ChildRunner {
  protected val id: Id = Id.next()

  abstract val description: Description
  abstract fun action(notifier: RunNotifier)

  //
  // execution
  fun run(notifier: RunNotifier) {
    if(description.isTest) {
      notifier.fireTestStarted(description)
    }

    try {
      action(notifier)
    } catch(failure: HookFailure) {
      notifier.fireTestFailure(Failure(description, failure))
    } catch(failure: ContextFailure) {
      notifier.fireTestFailure(Failure(description, failure))
    } catch(failure: TestFailure) {
      notifier.fireTestFailure(Failure(description, failure))
    } catch(failure: StatusFailure) {
      notifier.fireTestIgnored(description)
    } finally {
      if(description.isTest) {
        notifier.fireTestFinished(description)
      }
    }
  }

  //
  // Factory
  companion object {
    fun <T: Respek> group(testClass: Class<T>, group: ExampleGroup): ExampleGroupRunner<T> {
      return ExampleGroupRunner(testClass, group)
    }

    fun <T: Respek> example(testClass: Class<T>, example: Example): ExampleRunner<T> {
      return ExampleRunner(testClass, example)
    }
  }

  //
  // Id
  protected data class Id private constructor(
    val value: Int) : Serializable {

    companion object {
      private var id = 0
      fun next() = Id(id++)
    }
  }
}
