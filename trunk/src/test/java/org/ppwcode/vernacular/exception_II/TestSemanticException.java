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


public class TestSemanticException extends TestCase {

  public final static String TEST_MESSAGE = "TEST_MESSAGE_IDENTIFIER";

  public final static String EMPTY = "";

  public final static Throwable TEST_THROWABLE = new NullPointerException();



  private void testInvariants(SemanticException subject) {
    assertNotNull(subject.getMessage());
    assertTrue(InternalException.validMessageKey(subject.getMessage()));
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
                     InternalException.DEFAULT_MESSAGE_KEY :
                     messageIdentifier, subject.getMessage());
    assertEquals(t, subject.getCause());
  }

  /*</method>*/

}

