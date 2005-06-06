package be.peopleware.value_I;


import java.util.HashSet;
import java.util.Set;

import be.peopleware.test_I.Test;



/**
 * @author    David Van Keer
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_Gender extends _Test_EnumerationValue {

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
    Test.main(new String[] {"be.peopleware.value_I._Test_Gender"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_Gender__();
    test_VALUES();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
  }


  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected final EnumerationValue create_EnumerationValue(final String d) {
    return create_Gender();
  }

  protected Gender create_Gender() {
    return new Gender();
  }

  public Set getCases() {
    Set result = new HashSet();
    result.add(create_Gender());
    result.addAll(Gender.VALUES.values());
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Value subject) {
    super.validateTypeInvariants(subject);
    validateVALUETypeInvariants(subject, Gender.VALUES);
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected void test_Gender__() {
    try {
      Gender subject = new Gender();
      validate(subject.equals(Gender.MALE));
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected void test_VALUES() {
   test_VALUES(Gender.VALUES, Gender.class);
  }

  /*</section>*/

}

