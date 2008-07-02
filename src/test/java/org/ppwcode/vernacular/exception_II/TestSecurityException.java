/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.exception_II;


import junit.framework.TestCase;


public class TestSecurityException extends TestCase {

  public final static String TEST_MESSAGE = "TEST_MESSAGE_IDENTIFIER";

  public final static String EMPTY = "";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(SecurityException subject) {
    assertNotNull(subject.getMessage());
    assertTrue(InternalException.validMessageIdentifier(subject.getMessage()));
  }



  /*<method signature="SecurityException(String, Throwable)">*/
  //-----------------------------------------------------------------------

  public void testSecurityExceptionStringThrowable1() {
    testSecurityExceptionStringThrowable(TEST_MESSAGE, TEST_THROWABLE);
  }

  public void testSecurityExceptionStringThrowable2() {
    testSecurityExceptionStringThrowable(TEST_MESSAGE, null);
  }

  public void testSecurityExceptionStringThrowable3() {
    testSecurityExceptionStringThrowable(EMPTY, TEST_THROWABLE);
  }

  public void testSecurityExceptionStringThrowable4() {
    testSecurityExceptionStringThrowable(EMPTY, null);
  }

  public void testSecurityExceptionStringThrowable5() {
    testSecurityExceptionStringThrowable(null, TEST_THROWABLE);
  }

  public void testSecurityExceptionStringThrowable6() {
    testSecurityExceptionStringThrowable(null, null);
  }

  private void testSecurityExceptionStringThrowable(String messageIdentifier, Throwable t) {
    SecurityException subject = new SecurityException(messageIdentifier, t);
    testInvariants(subject);
    assertEquals(((messageIdentifier == null) || (EMPTY.equals(messageIdentifier))) ?
                     InternalException.DEFAULT_MESSAGE_IDENTIFIER :
                     messageIdentifier, subject.getMessage());
    assertEquals(t, subject.getCause());
  }

  /*</method>*/

}

