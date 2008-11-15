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

package org.ppwcode.vernacular.exception_III;


import org.ppwcode.vernacular.exception_III.ApplicationException;

import junit.framework.TestCase;


public class TestApplicationException extends TestCase {

  public final static String TEST_MESSAGE = "TEST_MESSAGE_IDENTIFIER";

  public final static String EMPTY = "";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(ApplicationException subject) {
    assertNotNull(subject.getMessage());
    assertTrue(ApplicationException.validMessageKey(subject.getMessage()));
  }



  /*<method signature="ApplicationException(String, Throwable)">*/
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
    ApplicationException subject = new ApplicationException(messageIdentifier, t);
    testInvariants(subject);
    assertEquals(((messageIdentifier == null) || (EMPTY.equals(messageIdentifier))) ?
                     ApplicationException.DEFAULT_MESSAGE_KEY :
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
                       ApplicationException.matchesMessageKeyPattern(messageIdentifier);
    boolean result = ApplicationException.validMessageKey(messageIdentifier);
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
    boolean result = ApplicationException.matchesMessageKeyPattern(messageIdentifier);
    assertEquals(expected, result);
  }

  /*</method>*/

}

