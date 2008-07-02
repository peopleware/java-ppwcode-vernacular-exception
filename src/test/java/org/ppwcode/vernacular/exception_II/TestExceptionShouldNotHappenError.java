/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
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

