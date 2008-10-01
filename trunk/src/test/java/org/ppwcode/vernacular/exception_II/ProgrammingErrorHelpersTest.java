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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;


public class ProgrammingErrorHelpersTest {

  public final static String TEST_MESSAGE_1 = "TEST MESSAGE 1";

  public final static String TEST_MESSAGE_2 = "TEST MESSAGE 2";

  public final static Throwable TEST_THROWABLE = new Throwable();

  public final static Object TEST_OBJECT = new Object();

  @Test
  public void testDeadBranch() {
    try {
      ProgrammingErrorHelpers.deadBranch();
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.DEAD_BRANCH_MESSAGE, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testDeadBranchString() {
    try {
      ProgrammingErrorHelpers.deadBranch(TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.DEAD_BRANCH_MESSAGE + ": " + TEST_MESSAGE_1, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testUnexpectedExceptionThrowable() {
    try {
      ProgrammingErrorHelpers.unexpectedException(TEST_THROWABLE);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.UNEXPECTED_EXCEPTION_MESSAGE, aErr.getMessage());
      assertEquals(TEST_THROWABLE, aErr.getCause());
    }
  }

  @Test
  public void testUnexpectedExceptionThrowableString() {
    try {
      ProgrammingErrorHelpers.unexpectedException(TEST_THROWABLE, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.UNEXPECTED_EXCEPTION_MESSAGE + ": " + TEST_MESSAGE_1, aErr.getMessage());
      assertEquals(TEST_THROWABLE, aErr.getCause());
    }
  }

  @Test
  public void testPreBoolean1() {
    try {
      ProgrammingErrorHelpers.pre(false);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.PRECONDITION_VIOLATION_MESSAGE, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreBoolean2() {
    boolean result = ProgrammingErrorHelpers.pre(true);
    assertTrue(result);
  }

  @Test
  public void testPreBooleanString1() {
    try {
      ProgrammingErrorHelpers.pre(false, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.PRECONDITION_VIOLATION_MESSAGE + ": " + TEST_MESSAGE_1, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreBooleanString2() {
    boolean result = ProgrammingErrorHelpers.pre(true, TEST_MESSAGE_1);
    assertTrue(result);
  }

  @Test
  public void testPreArgumentNotNullObject1() {
    try {
      ProgrammingErrorHelpers.preArgumentNotNull(null);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.PRECONDITION_VIOLATION_MESSAGE + ProgrammingErrorHelpers.COLON + ProgrammingErrorHelpers.ARGUMENT_NOT_NULL_MESSAGE, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreArgumentNotNullObject2() {
    boolean result = ProgrammingErrorHelpers.preArgumentNotNull(TEST_OBJECT);
    assertTrue(result);
  }

  @Test
  public void testPreArgumentNotNullObjectString1() {
    try {
      ProgrammingErrorHelpers.preArgumentNotNull(null, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.PRECONDITION_VIOLATION_MESSAGE + ProgrammingErrorHelpers.COLON + ProgrammingErrorHelpers.ARGUMENT_NOT_NULL_MESSAGE  + " (" + TEST_MESSAGE_1 + ")", aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreArgumentNotNullObjectString2() {
    boolean result = ProgrammingErrorHelpers.preArgumentNotNull(TEST_OBJECT, TEST_MESSAGE_1);
    assertTrue(result);
  }






  @Test
  public void testPreArgumentNotEmptyString1() {
    try {
      ProgrammingErrorHelpers.preArgumentNotEmpty(null);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.PRECONDITION_VIOLATION_MESSAGE + ProgrammingErrorHelpers.COLON + ProgrammingErrorHelpers.STRING_ARGUMENT_NOT_EMPTY_MESSAGE, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreArgumentNotEmptyString2() {
    try {
      ProgrammingErrorHelpers.preArgumentNotEmpty(ProgrammingErrorHelpers.EMPTY);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.PRECONDITION_VIOLATION_MESSAGE + ProgrammingErrorHelpers.COLON + ProgrammingErrorHelpers.STRING_ARGUMENT_NOT_EMPTY_MESSAGE, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreArgumentNotEmptyString3() {
    boolean result = ProgrammingErrorHelpers.preArgumentNotEmpty(TEST_MESSAGE_2);
    assertTrue(result);
  }

  @Test
  public void testPreArgumentNotEmp1yStringString1() {
    try {
      ProgrammingErrorHelpers.preArgumentNotEmpty(null, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.PRECONDITION_VIOLATION_MESSAGE + ProgrammingErrorHelpers.COLON + ProgrammingErrorHelpers.STRING_ARGUMENT_NOT_EMPTY_MESSAGE  + " (" + TEST_MESSAGE_1 + ")", aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreArgumentNotEmp1yStringString2() {
    try {
      ProgrammingErrorHelpers.preArgumentNotEmpty(ProgrammingErrorHelpers.EMPTY, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.PRECONDITION_VIOLATION_MESSAGE + ProgrammingErrorHelpers.COLON + ProgrammingErrorHelpers.STRING_ARGUMENT_NOT_EMPTY_MESSAGE  + " (" + TEST_MESSAGE_1 + ")", aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreArgumentNotEmptyStringString3() {
    boolean result = ProgrammingErrorHelpers.preArgumentNotEmpty(TEST_MESSAGE_2, TEST_MESSAGE_1);
    assertTrue(result);
  }

  @Test
  public void testDependencyObjectString1() {
    try {
      ProgrammingErrorHelpers.dependency(null, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + TEST_MESSAGE_1 + ProgrammingErrorHelpers.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testDependencyObjectString2() {
    boolean result = ProgrammingErrorHelpers.dependency(TEST_OBJECT, TEST_MESSAGE_1);
    assertTrue(result);
  }

  @Test
  public void testDependencyBooleanString1() {
    try {
      ProgrammingErrorHelpers.dependency(false, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + TEST_MESSAGE_1 + ProgrammingErrorHelpers.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testDependencyBooleanString2() {
    boolean result = ProgrammingErrorHelpers.dependency(true, TEST_MESSAGE_1);
    assertTrue(result);
  }

  @Test
  public void testDependencyBooleanStringString1() {
    try {
      ProgrammingErrorHelpers.dependency(false, TEST_MESSAGE_1, TEST_MESSAGE_2);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrorHelpers.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + TEST_MESSAGE_1 + ProgrammingErrorHelpers.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 + " " + TEST_MESSAGE_2, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testDependencyBooleanStringString2() {
    boolean result = ProgrammingErrorHelpers.dependency(true, TEST_MESSAGE_1, TEST_MESSAGE_2);
    assertTrue(result);
  }

}

