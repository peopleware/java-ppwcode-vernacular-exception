/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.exception_II;


import junit.framework.TestCase;


public class TestSemanticException extends TestCase {

  public final static String TEST_MESSAGE = "TEST_MESSAGE_IDENTIFIER";

  public final static String EMPTY = "";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(SemanticException subject) {
    assertNotNull(subject.getMessage());
    assertTrue(InternalException.validMessageIdentifier(subject.getMessage()));
  }



  /*<method signature="SemanticException(String, Throwable)">*/
  //-----------------------------------------------------------------------

  public void testSemanticExceptionStringThrowable1() {
    testSemanticExceptionStringThrowable(TEST_MESSAGE, TEST_THROWABLE);
  }

  public void testSemanticExceptionStringThrowable2() {
    testSemanticExceptionStringThrowable(TEST_MESSAGE, null);
  }

  public void testSemanticExceptionStringThrowable3() {
    testSemanticExceptionStringThrowable(EMPTY, TEST_THROWABLE);
  }

  public void testSemanticExceptionStringThrowable4() {
    testSemanticExceptionStringThrowable(EMPTY, null);
  }

  public void testSemanticExceptionStringThrowable5() {
    testSemanticExceptionStringThrowable(null, TEST_THROWABLE);
  }

  public void testSemanticExceptionStringThrowable6() {
    testSemanticExceptionStringThrowable(null, null);
  }

  private void testSemanticExceptionStringThrowable(String messageIdentifier, Throwable t) {
    SemanticException subject = new SemanticException(messageIdentifier, t);
    testInvariants(subject);
    assertEquals(((messageIdentifier == null) || (EMPTY.equals(messageIdentifier))) ?
                     InternalException.DEFAULT_MESSAGE_IDENTIFIER :
                     messageIdentifier, subject.getMessage());
    assertEquals(t, subject.getCause());
  }

  /*</method>*/

}

