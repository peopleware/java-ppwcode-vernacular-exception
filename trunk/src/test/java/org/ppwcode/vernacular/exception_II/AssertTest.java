/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.ppwcode.vernacular.exception_II;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class demonstrates what the effects are when we use assert with
 * static methods.
 *
 * @note This class was not run yet without -ea.
 */
public class AssertTest {

  /**
   * A static method that returns false.
   */
  private static boolean staticMethodContradiction() {
    return false;
  }

  /**
   * A static method that returns true.
   * @return
   */
  private static boolean staticMethodTautology() {
    return true;
  }

  /**
   * A static method that returns the argument.
   */
  private static boolean staticMethodEcho(boolean arg) {
    return arg;
  }

  /**
   * A static method that throws a {@link AssertionError} when
   * {@code arg} is {@code false}, and otherwise returns {@code true}.
   */
  private static boolean staticMethodAssertionErrorOnFalse(boolean arg) {
    if (! arg) {
      throw new AssertionError("tuut");
    }
    return arg;
  }

  private static boolean $called;

  /**
   * A static method that remembers it has been called in {@link #clone()}.
   */
  private static boolean staticMethodAmICalled() {
    $called = true;
    return true;
  }

  /**
   * Demonstrates a failed assertion ({@link AssertionError} is thrown if assertions are enabled).
   */
  @Test
  public void testAssertFalse() {
    try {
      assert false : "test";
      assertFalse(getClass().desiredAssertionStatus());
    }
    catch (AssertionError a) {
      assertTrue(getClass().desiredAssertionStatus());
    }
  }

  /**
   * Demonstrates a passed assertion (ends nominally).
   */
  @Test
  public void testAssertTrue() {
    assert true : "test";
  }

  /**
   * Demonstrates a failed assertion ({@link AssertionError} is thrown if assertions are enabled)
   * where the {@code false} that triggers the failure comes from a
   * static method.
   */
  @Test
  public void testAssertFalseStaticMethod() {
    try {
      assert staticMethodContradiction() : "test";
      assertFalse(getClass().desiredAssertionStatus());
    }
    catch (AssertionError a) {
      assertTrue(getClass().desiredAssertionStatus());
    }
  }

  /**
   * Demonstrates a passed assertion (ends nominally)
   * where the {@code true} that passes the assertion comes from a
   * static method.
   */
  @Test
  public void testAssertTrueStaticMethod() {
    assert staticMethodTautology() : "test";
  }

  /**
   * Demonstrates a failed assertion ({@link AssertionError} is thrown if assertions are enabled)
   * where the {@code false} that triggers the failure comes from a
   * static method with an argument.
   */
  public void testAssertFalseStaticMethodWithArgument() {
    try {
      assert staticMethodEcho(false) : "test";
      assertFalse(getClass().desiredAssertionStatus());
    }
    catch (AssertionError a) {
      assertTrue(getClass().desiredAssertionStatus());
    }
  }

  /**
   * Demonstrates a passed assertion (ends nominally)
   * where the {@code true} that passes the assertion comes from a
   * static method with an argument.
   */
  @Test
  public void testAssertTrueStaticMethodWithArgument() {
    assert staticMethodEcho(true) : "test";
  }

  /**
   * An assert that calls a static method that throws a {@link AssertionError}
   * if its argument is {@code false}, but it is {@code true}. The method should
   * end nominally.
   */
  @Test
  public void testAssertNoAssertionError() {
    assert staticMethodAssertionErrorOnFalse(true) : "test";
  }

  /**
   * An assert that calls a static method that throws a {@link AssertionError}
   * if its argument is {@code false}, where the argument is another
   * static method that returns {@code true}.
   * With assertions enabled, we expect that the methods (we test it for the
   * nested method) is called. With assertions disabled we expect that the methods
   * are not called. In either case, the method should end nominally.
   *
   * @note this was not yet run with assertions disabled
   */
  @Test
  public void testAssertMethodsNotCalledIfAssertionsDisabled() {
    $called = false;
//    System.out.println(getClass().desiredAssertionStatus());
    assert staticMethodAssertionErrorOnFalse(staticMethodAmICalled()) : "test";
    assertEquals(getClass().desiredAssertionStatus(), $called);
  }

  /**
   * Finally, in this method, we have an assert which is decided by a static method
   * that throws a {@link AssertionError} if its argument is false (and it is),
   * and returns {@code true} otherwise. In other words, the method will throw
   * a {@link AssertionError} before the assert clause can throw its own
   * {@link AssertionError}. Does this work as expected.
   *
   * With assertions enabled, we expect all methods to be called during the evaluation
   * of the assert, and we expect a {@link AssertionError} (and not an {@link AssertionError}).
   * With assertions disabled, we expect a nominal end of the method, and none of the
   * evaluation methods called.
   */
  public void testAssert999() {
    $called = false;
    try {
      assert staticMethodAssertionErrorOnFalse(staticMethodAmICalled() && false) : "test";
      assertFalse(getClass().desiredAssertionStatus());
      assertFalse($called);
    }
    catch (AssertionError pe) {
      assertTrue(getClass().desiredAssertionStatus());
      assertTrue($called);
    }
  }

  // attempt to run without assertions -- failed
  public static void main(String[]  args) {
//    System.out.println(AssertTest.class.desiredAssertionStatus());
    assert staticMethodAssertionErrorOnFalse(staticMethodAmICalled() && false) : "test";
  }

}

