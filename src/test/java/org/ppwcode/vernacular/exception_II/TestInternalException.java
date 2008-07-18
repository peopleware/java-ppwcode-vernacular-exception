/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.ppwcode.vernacular.exception_II;


import junit.framework.TestCase;


public class TestInternalException extends TestCase {

  public final static String TEST_MESSAGE = "TEST_MESSAGE_IDENTIFIER";

  public final static String EMPTY = "";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(InternalException subject) {
    assertNotNull(subject.getMessage());
    assertTrue(InternalException.validMessageKey(subject.getMessage()));
  }



  /*<method signature="InternalException(String, Throwable)">*/
  //-----------------------------------------------------------------------

  public void testInternalExceptionStringThrowable1() {
    testInternalExceptionStringThrowable(TEST_MESSAGE, TEST_THROWABLE);
  }

  public void testInternalExceptionStringThrowable2() {
    testInternalExceptionStringThrowable(TEST_MESSAGE, null);
  }

  public void testInternalExceptionStringThrowable3() {
    testInternalExceptionStringThrowable(EMPTY, TEST_THROWABLE);
  }

  public void testInternalExceptionStringThrowable4() {
    testInternalExceptionStringThrowable(EMPTY, null);
  }

  public void testInternalExceptionStringThrowable5() {
    testInternalExceptionStringThrowable(null, TEST_THROWABLE);
  }

  public void testInternalExceptionStringThrowable6() {
    testInternalExceptionStringThrowable(null, null);
  }

  private void testInternalExceptionStringThrowable(String messageIdentifier, Throwable t) {
    InternalException subject = new InternalException(messageIdentifier, t);
    testInvariants(subject);
    assertEquals(((messageIdentifier == null) || (EMPTY.equals(messageIdentifier))) ?
                     InternalException.DEFAULT_MESSAGE_KEY :
                     messageIdentifier, subject.getMessage());
    assertEquals(t, subject.getCause());
  }

  /*</method>*/



  /*<method signature="validMessageIdentifier(String)">*/
  //-----------------------------------------------------------------------

  public void testValidMessageIdentifierString0() {
    testValidMessageIdentifierString(null);
  }

  public void testValidMessageIdentifierString1() {
    testValidMessageIdentifierString("d");
  }

  public void testValidMessageIdentifierString2() {
    testValidMessageIdentifierString("D");
  }

  public void testValidMessageIdentifierString3() {
    testValidMessageIdentifierString(EMPTY);
  }

  public void testValidMessageIdentifierString4() {
    testValidMessageIdentifierString("THIS IS");
  }

  public void testValidMessageIdentifierString5() {
    testValidMessageIdentifierString("TEST_STUFF");
  }

  public void testValidMessageIdentifierString6() {
    testValidMessageIdentifierString("TEST_stUFF");
  }

  public void testValidMessageIdentifierString7() {
    testValidMessageIdentifierString("DD");
  }

  private void testValidMessageIdentifierString(String messageIdentifier) {
    boolean expected = (messageIdentifier != null) &&
                       InternalException.matchesMessageKeyPattern(messageIdentifier);
    boolean result = InternalException.validMessageKey(messageIdentifier);
    assertEquals(expected, result);
  }

  /*</method>*/



  /*<method signature="matchesMessageIdentifierPattern(String)">*/
  //-----------------------------------------------------------------------

  public void testMatchesMessageIdentifierPatternString1() {
    testMatchesMessageIdentifierPatternString("d");
  }

  public void testMatchesMessageIdentifierPatternString2() {
    testMatchesMessageIdentifierPatternString("D");
  }

  public void testMatchesMessageIdentifierPatternString3() {
    testMatchesMessageIdentifierPatternString(EMPTY);
  }

  public void testMatchesMessageIdentifierPatternString4() {
    testMatchesMessageIdentifierPatternString("THIS IS");
  }

  public void testMatchesMessageIdentifierPatternString5() {
    testMatchesMessageIdentifierPatternString("TEST_STUFF");
  }

  public void testMatchesMessageIdentifierPatternString6() {
    testMatchesMessageIdentifierPatternString("TEST_stUFF");
  }

  public void testMatchesMessageIdentifierPatternString7() {
    testMatchesMessageIdentifierPatternString("DD");
  }

  private void testMatchesMessageIdentifierPatternString(String messageIdentifier) {
    boolean expected = (messageIdentifier.length() > 1);
    if (expected) {
      char first = messageIdentifier.charAt(0);
      expected &= Character.isUpperCase(first);
      char last = messageIdentifier.charAt(messageIdentifier.length() - 1);
      expected &= Character.isUpperCase(last);
      for (int i = 1; i < messageIdentifier.length() - 1; i++) {
        char ch = messageIdentifier.charAt(i);
        expected &= ((ch == '_') || (Character.isUpperCase(ch)));
      }
    }
    boolean result = InternalException.matchesMessageKeyPattern(messageIdentifier);
    assertEquals(expected, result);
  }

  /*</method>*/

}

