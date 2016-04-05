package dev.wizrad.respek.runners

import dev.wizrad.respek.graph.Respek
import dev.wizrad.respek.graph.ExampleGroup
import dev.wizrad.respek.graph.Hooks
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier

class ExampleGroupRunner<T: Respek>(
  private val testClass: Class<T>,
  private val group:     ExampleGroup) : ChildRunner() {

  val children = lazy {
    group.map(
      { ChildRunner.group(testClass, it) },
      { ChildRunner.example(testClass, it) }
    )
  }

  //
  // Child
  override fun action(notifier: RunNotifier) {
    group.willRun()

    for(child in children.value) {
      child.run(notifier)
    }

    group.didRun()
  }

  override val description: Description by lazy {
    Description
      .createSuiteDescription(group.description, id)
      .append(children.value.map { it.description })
  }

  //
  // Helpers
  private fun Description.append(children: List<Description>) : Description {
    for(child in children) {
      this.addChild(child)
    }

    return this
  }
}
