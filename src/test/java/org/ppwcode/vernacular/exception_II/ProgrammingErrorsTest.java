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


public class ProgrammingErrorsTest {

  public final static String TEST_MESSAGE_1 = "TEST MESSAGE 1";

  public final static String TEST_MESSAGE_2 = "TEST MESSAGE 2";

  public final static Throwable TEST_THROWABLE = new Throwable();

  public final static Object TEST_OBJECT = new Object();

  @Test
  public void testDeadBranch() {
    try {
      ProgrammingErrors.deadBranch();
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.DEAD_BRANCH_MESSAGE, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testDeadBranchString() {
    try {
      ProgrammingErrors.deadBranch(TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.DEAD_BRANCH_MESSAGE + ": " + TEST_MESSAGE_1, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testUnexpectedExceptionThrowable() {
    try {
      ProgrammingErrors.unexpectedException(TEST_THROWABLE);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.UNEXPECTED_EXCEPTION_MESSAGE, aErr.getMessage());
      assertEquals(TEST_THROWABLE, aErr.getCause());
    }
  }

  @Test
  public void testUnexpectedExceptionThrowableString() {
    try {
      ProgrammingErrors.unexpectedException(TEST_THROWABLE, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.UNEXPECTED_EXCEPTION_MESSAGE + ": " + TEST_MESSAGE_1, aErr.getMessage());
      assertEquals(TEST_THROWABLE, aErr.getCause());
    }
  }

  @Test
  public void testPreBoolean1() {
    try {
      ProgrammingErrors.pre(false);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.PRECONDITION_VIOLATION_MESSAGE, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreBoolean2() {
    boolean result = ProgrammingErrors.pre(true);
    assertTrue(result);
  }

  @Test
  public void testPreBooleanString1() {
    try {
      ProgrammingErrors.pre(false, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.PRECONDITION_VIOLATION_MESSAGE + ": " + TEST_MESSAGE_1, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testPreBooleanString2() {
    boolean result = ProgrammingErrors.pre(true, TEST_MESSAGE_1);
    assertTrue(result);
  }

  @Test
  public void testDependencyObjectString1() {
    try {
      ProgrammingErrors.dependency(null, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + TEST_MESSAGE_1 + ProgrammingErrors.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testDependencyObjectString2() {
    boolean result = ProgrammingErrors.dependency(TEST_OBJECT, TEST_MESSAGE_1);
    assertTrue(result);
  }

  @Test
  public void testDependencyBooleanString1() {
    try {
      ProgrammingErrors.dependency(false, TEST_MESSAGE_1);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + TEST_MESSAGE_1 + ProgrammingErrors.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testDependencyBooleanString2() {
    boolean result = ProgrammingErrors.dependency(true, TEST_MESSAGE_1);
    assertTrue(result);
  }

  @Test
  public void testDependencyBooleanStringString1() {
    try {
      ProgrammingErrors.dependency(false, TEST_MESSAGE_1, TEST_MESSAGE_2);
      fail();
    }
    catch (AssertionError aErr) {
      assertEquals(ProgrammingErrors.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + TEST_MESSAGE_1 + ProgrammingErrors.DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 + " " + TEST_MESSAGE_2, aErr.getMessage());
      assertNull(aErr.getCause());
    }
  }

  @Test
  public void testDependencyBooleanStringString2() {
    boolean result = ProgrammingErrors.dependency(true, TEST_MESSAGE_1, TEST_MESSAGE_2);
    assertTrue(result);
  }

}

