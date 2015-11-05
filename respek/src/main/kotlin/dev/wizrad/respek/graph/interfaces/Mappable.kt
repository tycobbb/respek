package dev.wizrad.respek.graph.interfaces

import dev.wizrad.respek.graph.ExampleGroup
import dev.wizrad.respek.graph.Example

interface Mappable {
  fun <T> map(groupTransform: ((ExampleGroup) -> T)? = null, exampleTransform: ((Example) -> T)? = null) : MutableList<T>
}