package dev.wizrad.respek.graph.throwables

import dev.wizrad.respek.graph.ExampleGroup

open internal class ContextFailure(
  val context: ExampleGroup,
  val throwable: Throwable) : RuntimeException(throwable) {

}