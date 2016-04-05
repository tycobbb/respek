package dev.wizrad.respek.runners

import dev.wizrad.respek.graph.Respek
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.junit.runners.ParentRunner

class SpecRunner<T: Respek>(
  val testClass: Class<T>) : ParentRunner<ChildRunner>(testClass) {

  lateinit var spec: Respek

  init {
    try {
      spec = testClass.newInstance()
    } catch(exception: Exception) {
      print(exception)
    }
  }

  override fun getChildren(): MutableList<ChildRunner>? {
    return spec.map({ ExampleGroupRunner(testClass, it) })
  }

  override fun describeChild(child: ChildRunner?): Description? {
    return child?.description
  }

  override fun runChild(child: ChildRunner?, notifier: RunNotifier?) {
    child?.run(notifier!!)
  }
}
