package dev.wizrad.respek.graph.throwables

import dev.wizrad.respek.graph.Status
import dev.wizrad.respek.graph.Example

class StatusFailure(
  val example: Example,
  val status: Status) : RuntimeException() {

}
