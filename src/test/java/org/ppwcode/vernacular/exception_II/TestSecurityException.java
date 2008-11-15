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


import junit.framework.TestCase;


public class TestSecurityException extends TestCase {

  public final static String TEST_MESSAGE = "TEST_MESSAGE_IDENTIFIER";

  public final static String EMPTY = "";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(SecurityException subject) {
    assertNotNull(subject.getMessage());
    assertTrue(ApplicationException.validMessageKey(subject.getMessage()));
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
                     ApplicationException.DEFAULT_MESSAGE_KEY :
                     messageIdentifier, subject.getMessage());
    assertEquals(t, subject.getCause());
  }

  /*</method>*/

}

