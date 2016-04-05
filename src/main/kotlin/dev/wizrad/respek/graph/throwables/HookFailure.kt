package dev.wizrad.respek.graph.throwables

import dev.wizrad.respek.graph.ExampleGroup
import dev.wizrad.respek.graph.Hooks

internal class HookFailure(
  val hook:  Hooks.Type,
  context: ExampleGroup,
  throwable: Throwable) : ContextFailure(context, throwable) {

}