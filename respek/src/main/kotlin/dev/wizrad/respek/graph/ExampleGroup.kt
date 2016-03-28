package dev.wizrad.respek.graph

import dev.wizrad.respek.dsl.Context
import dev.wizrad.respek.dsl.Test
import dev.wizrad.respek.graph.throwables.ContextFailure
import dev.wizrad.respek.graph.throwables.HookFailure
import dev.wizrad.respek.graph.interfaces.DebugPrintable
import dev.wizrad.respek.graph.interfaces.Parent
import dev.wizrad.respek.graph.interfaces.Mappable
import java.util.ArrayList

class ExampleGroup(
  private val node:   DslNode<ExampleGroup>,
  private val parent: Parent? = null) : Context, Parent, Mappable, DebugPrintable {

  private val hooks = Hooks()
  private val children: MutableList<ExampleGroup> = ArrayList()
  private val examples: MutableList<Example>      = ArrayList()

  init {
    try {
      node.action(this)
    } catch(exception: Exception) {
      throw ContextFailure(this, exception)
    }
  }

  fun willRun() {
    this.runOwnHooks(Hooks.Type.Before)
  }

  fun didRun() {
    this.runOwnHooks(Hooks.Type.After)
  }

  //
  // Parent
  override fun runHooks(type: Hooks.Type) {
    parent?.runHooks(type)
    this.runOwnHooks(type)
  }

  fun runOwnHooks(type: Hooks.Type) {
    try {
      hooks.run(type)
    } catch(exception: Exception) {
      throw HookFailure(type, this, exception)
    }
  }

  //
  // Nesting
  override fun given(message: String, expression: Context.() -> Unit) {
    this.appendContext(DslNode.given(message, expression))
  }

  override fun on(message: String, expression: Context.() -> Unit) {
    this.appendContext(DslNode.on(message, expression))
  }

  private fun appendContext(description: DslNode<ExampleGroup>) {
    children.add(ExampleGroup(description, this))
  }

  //
  // Tests
  override fun it(message: String, expression: Test.() -> Unit) {
    this.appendTest(DslNode.test(message, expression))
  }

  override fun xit(message: String, expression: Test.() -> Unit) {
    this.appendTest(DslNode.test(message, expression, Status.Skipped))
  }

  private fun appendTest(node: DslNode<Example>) : Example {
    val test = Example(node, this)
    examples.add(test)
    return test
  }

  //
  // Hookable
  override fun before(expression: () -> Unit) = hooks.before(expression)
  override fun beforeEach(expression: () -> Unit) = hooks.beforeEach(expression)
  override fun after(expression: () -> Unit)  = hooks.after(expression)
  override fun afterEach(expression: () -> Unit) = hooks.afterEach(expression)

  //
  // Mappable
  override fun <T> map(groupTransform: ((ExampleGroup) -> T)?, exampleTransform: ((Example) -> T)?): MutableList<T> {
    val result = ArrayList<T>()

    if(exampleTransform != null) {
      for(example in examples) {
        result.add(exampleTransform(example))
      }
    }

    if(groupTransform != null) {
      for(group in children) {
        result.add(groupTransform(group))
      }
    }

    return result
  }

  //
  // Describable
  override val description: String get() {
    return node.message()
  }

  //
  // DebugPrintable
  override fun debugString(depth: Int): String {
    var result = "${this.paddedString(depth)}\n"

    for(test in examples) {
      result += "${test.debugString(depth + 1)}\n"
    }

    for(context in children) {
      result += "${context.debugString(depth + 1)}"
    }

    return result
  }

  override fun toString(): String {
    return node.message()
  }
}
