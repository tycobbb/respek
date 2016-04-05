package dev.wizrad.respek.graph.throwables

import dev.wizrad.respek.graph.Example

internal class TestFailure(
  val example: Example,
  val throwable: Throwable) : RuntimeException(throwable) {

}
