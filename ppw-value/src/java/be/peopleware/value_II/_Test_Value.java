/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_Object;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_Value extends _Test_Object {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/


  public static void main(final String[] args) {
    Test.main(new String[] {"be.peopleware.value_II._Test_Value"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_Value__();
  }

  protected void testInstanceMethods() {
    test_equals_Object_();
    test_hashCode__();
    test_toString__();
  }


  protected class Stub extends Value {

    public int hashCode() {
      return 0;
    }

    public String toString() {
      return "0"; //$NON-NLS-1$
    }

  }


  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected Value create_Value() {
    return new Stub();
  }


  public Set getCases() {
    java.util.Set result = new java.util.HashSet();
    result.add(create_Value());
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Value subject) {
    /* (forall MutableValue mv1, mv2; ;
          mv1.equals(mv2) ==> mv1.hashCode() == mv2.hashCode());
       see equals
     */
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected final void test_Value__() {
    try {
      Value subject = new Stub();
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/


  /*<section name="instance methods">*/
  //-------------------------------------------------------------------

  protected final void test_equals_Object_() {
    Set casesSubject = getCases();
    Set casesOther = new HashSet();
    casesOther.addAll(casesSubject);
    casesOther.add(null);
    casesOther.add(new Object());
    Iterator iterSubject = casesSubject.iterator();
    while (iterSubject.hasNext()) {
      Value subject = (Value)iterSubject.next();
      Iterator iterOther = casesOther.iterator();
      while (iterOther.hasNext()) {
        Object other = iterOther.next();
        test_equals_Object_(subject, other);
      }
    }
  }

  protected void test_equals_Object_(final Value subject,
                                     final Object other) {
    try {
      boolean result = subject.equals(other);
      post_equals_Object_(subject, other, result);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected void post_equals_Object_(final Value subject,
                                     final Object other,
                                     final boolean result) {
    validate(result ? (other != null)
                      && (other.getClass() == subject.getClass())
                    : true);
    if (result) {
      validate(subject.hashCode() == other.hashCode());
    }
  }

  protected final void test_hashCode__() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      Value subject = (Value)iterSubject.next();
      test_hashCode__(subject);
    }
  }

  protected void test_hashCode__(final Value subject) {
    try {
      subject.hashCode();
      // nothing to validate, not interested in result
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_toString__() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      Value subject = (Value)iterSubject.next();
      test_toString__(subject);
    }
  }

  protected void test_toString__(final Value subject) {
    try {
      subject.toString();
      // nothing to validate, not interested in result
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/

}
