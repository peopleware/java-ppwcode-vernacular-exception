package be.peopleware.value_II;


import java.util.HashSet;
import java.util.Set;

import be.peopleware.test_I.Test;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_ImmutableValue extends _Test_Value {

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
    Test.main(new String[] {"be.peopleware.value_I._Test_ImmutableValue"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_ImmutableValue__();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
  }


  protected class Stub extends ImmutableValue {

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
    return create_ImmutableValue();
  }

  protected ImmutableValue create_ImmutableValue() {
    return new Stub();
  }

  public Set getCases() {
    java.util.Set result = new HashSet();
    result.add(create_ImmutableValue());
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Value subject) {
    super.validateTypeInvariants(subject);
    validate(!Cloneable.class.isAssignableFrom(subject.getClass()));
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected final void test_ImmutableValue__() {
    try {
      ImmutableValue subject = new Stub();
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/


  /*<section name="instance methods">*/
  //-------------------------------------------------------------------


  /*</section>*/

}
