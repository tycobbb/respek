package dev.wizrad.respek.runners

import dev.wizrad.respek.graph.Respek
import dev.wizrad.respek.graph.Example
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier

class ExampleRunner<T: Respek>(
  val testClass: Class<T>,
  val example:   Example) : ChildRunner() {

  override fun action(notifier: RunNotifier) {
    example.run()
  }

  override val description: Description by lazy {
    Description.createTestDescription(testClass.name, example.description, id)
  }
}
