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


import java.text.MessageFormat;

import junit.framework.TestCase;


public class TestExceptionShouldNotHappenError extends TestCase {

  public final static String TEST_MESSAGE = "This is a test message";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(ProgrammingError subject) {
    assertNotNull(subject.getMessage());
    assertNotNull(subject.getCause());
  }


  /*<method signature="ExceptionShouldNotHappenError(String, Throwable)">*/
  //-----------------------------------------------------------------------

  public void testExceptionShouldNotHappenErrorStringThrowable() {
    ExceptionShouldNotHappenError subject = new ExceptionShouldNotHappenError(TEST_MESSAGE, TEST_THROWABLE);
    testInvariants(subject);
    assertEquals(ExceptionShouldNotHappenError.prependMessage(TEST_MESSAGE, TEST_THROWABLE),
                 subject.getMessage());
    assertEquals(TEST_THROWABLE, subject.getCause());
  }

  /*</method>*/



  /*<method signature="prependMessage(String, Throwable)">*/
  //-----------------------------------------------------------------------

  public void testPrependMessage1() {
    testPrependMessage(null, TEST_THROWABLE);
  }

  public void testPrependMessage2() {
    testPrependMessage(TEST_MESSAGE, TEST_THROWABLE);
  }

  private void testPrependMessage(String message, Throwable t) {
    String expected = MessageFormat.format(ExceptionShouldNotHappenError.MESSAGE_PATTERN,
                                           t.getClass().getName(), message);
    String result = ExceptionShouldNotHappenError.prependMessage(message, t);
    assertEquals(expected, result);
  }

  /*</method>*/

}

