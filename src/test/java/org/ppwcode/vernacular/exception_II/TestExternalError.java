/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.exception_II;


import junit.framework.TestCase;


public class TestExternalError extends TestCase {

  public final static String TEST_MESSAGE = "This is a test message";

  public final static String EMPTY = "";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(ExternalError subject) {
    assertNotNull(subject.getMessage());
  }



  /*<method signature="ExternalError(String, Throwable)">*/
  //-----------------------------------------------------------------------

  public void testExternalErrorStringThrowable1() {
    testExternalErrorStringThrowable(TEST_MESSAGE, TEST_THROWABLE);
  }

  public void testExternalErrorStringThrowable2() {
    testExternalErrorStringThrowable(TEST_MESSAGE, null);
  }

  public void testExternalErrorStringThrowable3() {
    testExternalErrorStringThrowable(EMPTY, TEST_THROWABLE);
  }

  public void testExternalErrorStringThrowable4() {
    testExternalErrorStringThrowable(EMPTY, null);
  }

  public void testExternalErrorStringThrowable5() {
    testExternalErrorStringThrowable(null, TEST_THROWABLE);
  }

  public void testExternalErrorStringThrowable6() {
    testExternalErrorStringThrowable(null, null);
  }

  public void testExternalErrorStringThrowable(String message, Throwable t) {
    ExternalError subject = new ExternalError(message, t);
    testInvariants(subject);
    assertEquals(ExternalError.defaultMessage(message, t), subject.getMessage());
    assertEquals(t, subject.getCause());
  }

  /*</method>*/



  /*<method signature="ExternalError(Throwable)">*/
  //-----------------------------------------------------------------------

  public void testExternalErrorThrowable1() {
    testExternalErrorThrowable(TEST_THROWABLE);
  }

  public void testExternalErrorThrowable2() {
    testExternalErrorThrowable(null);
  }

  private void testExternalErrorThrowable(Throwable t) {
    ExternalError subject = new ExternalError(t);
    testInvariants(subject);
    assertEquals(ExternalError.defaultMessage(null, t), subject.getMessage());
    assertEquals(t, subject.getCause());
  }

  /*</method>*/



  /*<method signature="ExternalError(String)">*/
  //-----------------------------------------------------------------------

  public void testExternalErrorString1() {
    testExternalErrorString(TEST_MESSAGE);
  }

  public void testExternalErrorString2() {
    testExternalErrorString(EMPTY);
  }

  public void testExternalErrorString3() {
    testExternalErrorString(null);
  }

  private void testExternalErrorString(String message) {
    ExternalError subject = new ExternalError(message);
    testInvariants(subject);
    assertEquals(ExternalError.defaultMessage(message, null), subject.getMessage());
    assertEquals(null, subject.getCause());
  }

  /*</method>*/



  /*<method signature="ExternalError()">*/
  //-----------------------------------------------------------------------

  public void testExternalError() {
    ExternalError subject = new ExternalError();
    testInvariants(subject);
    assertEquals(ExternalError.defaultMessage(null, null), subject.getMessage());
    assertEquals(null, subject.getCause());
  }

  /*</method>*/



  /*<method signature="defaultMessage(String, Throwable)">*/
  //-----------------------------------------------------------------------

  public void testDefaultMessage1() {
    testDefaultMessage(null, null);
  }

  public void testDefaultMessage2() {
    testDefaultMessage(TEST_MESSAGE, null);
  }

  public void testDefaultMessage3() {
    testDefaultMessage(null, TEST_THROWABLE);
  }

  public void testDefaultMessage4() {
    testDefaultMessage(TEST_MESSAGE, TEST_THROWABLE);
  }

  private void testDefaultMessage(String message, Throwable t) {
    String expected = (message != null) ?
                        message :
                        ((t != null) ?
                           ExternalError.UNEXPECTED_MESSAGE :
                           ExternalError.COULD_NOT_CONTINUE_MESSAGE);
    String result = ExternalError.defaultMessage(message, t);
    assertEquals(expected, result);
  }

  /*</method>*/

}

