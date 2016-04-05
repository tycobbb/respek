package dev.wizrad.respek.graph

interface DslNode<T> {
  val status: Status

  fun message(): String
  fun action(receiver: T)

  //
  // Factories
  companion object {
    fun on(message: String, expression: ExampleGroup.() -> Unit) : DslNode<ExampleGroup> {
      return prefixed("on", message, expression)
    }

    fun given(message: String, expression: ExampleGroup.() -> Unit) : DslNode<ExampleGroup> {
      return prefixed("given", message, expression)
    }

    fun prefixed(prefix: String, message: String, expression: ExampleGroup.() -> Unit) : DslNode<ExampleGroup> {
      return object: DslNode<ExampleGroup> {
        override fun message() = prefix + " " + message
        override fun action(receiver: ExampleGroup) = receiver.expression()
        override val status: Status get() = Status.Normal
      }
    }

    fun test(message: String, expression: Example.() -> Unit, status: Status = Status.Normal) : DslNode<Example> {
      return object: DslNode<Example> {
        override fun message() = "it " + message
        override fun action(receiver: Example) = receiver.expression()
        override val status: Status get() = status
      }
    }
  }
}
