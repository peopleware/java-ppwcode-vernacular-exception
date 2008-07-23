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


public class TestExternalError extends TestCase {

  public final static String TEST_MESSAGE = "This is a test message";

  public final static String EMPTY = "";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(ExternalError subject) {
    assertNotNull(subject.getMessage());
    assertNotSame("", subject.getMessage());
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
    String expectedMessage = (message == null) || (EMPTY.equals(message)) ?
                               ((t == null) ? ExternalError.UNSPECIFIED_EXTERNAL_ERROR_MESSAGE : ExternalError.EXCEPTION_WITH_EXTERNAL_CAUSE_MESSAGE) :
                               message;
    assertEquals(expectedMessage, subject.getMessage());
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
    String expectedMessage = (t == null) ? ExternalError.UNSPECIFIED_EXTERNAL_ERROR_MESSAGE : ExternalError.EXCEPTION_WITH_EXTERNAL_CAUSE_MESSAGE;
    assertEquals(expectedMessage, subject.getMessage());
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
    String expectedMessage = (message == null) || (EMPTY.equals(message)) ?
                               ExternalError.UNSPECIFIED_EXTERNAL_ERROR_MESSAGE :
                               message;
    assertEquals(expectedMessage, subject.getMessage());
    assertNull(subject.getCause());
  }

  /*</method>*/

}

