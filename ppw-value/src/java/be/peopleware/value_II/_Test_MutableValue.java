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


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_MutableValue extends _Test_Value {

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
    Test.main(new String[] {"be.peopleware.value_I._Test_MutableValue"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_MutableValue__();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
    test_clone__();
  }


  protected class Stub extends MutableValue {

    public int hashCode() {
      return 0;
    }

    public String toString() {
      return "0"; //$NON-NLS-1$
    }

  }


  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected final Value create_Value() {
    return create_MutableValue();
  }

  protected Value create_MutableValue() {
    return new Stub();
  }

  public Set getCases() {
    java.util.Set result = new HashSet();
    result.add(create_MutableValue());
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Value subject) {
    super.validateTypeInvariants(subject);
    // no extra invariants
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected final void test_MutableValue__() {
    try {
      MutableValue subject = new Stub();
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/


  /*<section name="instance methods">*/
  //-------------------------------------------------------------------

  protected final void test_clone__() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      MutableValue subject = (MutableValue)iterSubject.next();
      test_clone__(subject);
    }
  }

  protected void test_clone__(final MutableValue subject) {
    try {
      Object result = subject.clone();
      validate(result != subject);
      validate(subject.equals(result));
      validateTypeInvariants((Value)result);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/

}
