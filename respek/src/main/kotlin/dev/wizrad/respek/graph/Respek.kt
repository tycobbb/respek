package dev.wizrad.respek.graph

import dev.wizrad.respek.dsl.Context
import dev.wizrad.respek.dsl.Root
import dev.wizrad.respek.graph.interfaces.Mappable
import dev.wizrad.respek.graph.throwables.SpecException
import dev.wizrad.respek.runners.SpecRunner
import org.junit.runner.RunWith

@RunWith(SpecRunner::class)
abstract class Respek() : Root, Mappable {

  private var group: ExampleGroup? = null
  private val root:  ExampleGroup get() {
    if(group == null) {
      throw SpecException("Spec ${javaClass.name} did not contain a `given` block")
    }

    return group!!
  }

  //
  // Definable
  override fun given(message: String, expression: Context.() -> Unit) {
    if(group != null) {
      throw SpecException("Spec ${javaClass.name} can only contain one `given` block at the root level")
    } else {
      // construct the tree of nested contexts / tests starting this given context
      group = ExampleGroup(DslNode.given(message, expression))
    }
  }

  //
  // Mappable
  override fun <T> map(groupTransform: ((ExampleGroup) -> T)?, exampleTransform: ((Example) -> T)?): MutableList<T> {
    return arrayListOf(groupTransform!!(root))
  }

  //
  // Debugging
  override fun toString(): String {
    var result = this.javaClass.name

    if(group != null) {
      result = "$result\n${root.debugString(0)}"
    }

    return result
  }
}
