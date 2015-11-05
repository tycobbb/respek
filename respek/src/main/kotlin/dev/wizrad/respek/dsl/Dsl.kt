package dev.wizrad.respek.dsl

//
// Elements
//

interface Root : Definable {

}

interface Context : Definable, Hookable {
  fun on(message: String, expression: Context.() -> Unit)
  fun it(message: String, expression: Test.() -> Unit)
  fun xit(message: String, expression: Test.() -> Unit)
}

interface Test {

}

//
// Aspects
//

interface Definable {
  fun given(message: String, expression: Context.() -> Unit)
}

interface Hookable {
  fun before(expression: () -> Unit)
  fun after(expression: () -> Unit)
  fun beforeEach(expression: () -> Unit)
  fun afterEach(expression: () -> Unit)
}
