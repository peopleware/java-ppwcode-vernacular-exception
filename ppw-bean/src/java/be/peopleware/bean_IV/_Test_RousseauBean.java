package be.peopleware.bean_IV;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_Object;


/**
 * @author      Jan Dockx
 * @author      PeopleWare n.v.
 */
public class _Test_RousseauBean extends _Test_Object {

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

//  private static final Log LOG
//      = LogFactory.getLog(_Test_RousseauBean.class); //$NON-NLS-1$



  private static class Stub extends RousseauBean {
    // NOP
  }



  public static void main(final String[] args) {
    Test.main(new String[]
        {"be.peopleware.bean_II._Test_RousseauBean"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_RousseauBean__();
  }

  protected void testInstanceMethods() {
    test_getWildExceptions__();
    test_isCivilized__();
    test_checkCivility__();
    test_normalize__();
    test_hasSameValues_RousseauBean_();
    test_equals_Object_();
    test_hashCode__();
  }

  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected RousseauBean create_RousseauBean() {
    return new Stub();
  }

  protected Set getCases() {
    Set result = new HashSet();
    RousseauBean rb = new Stub();
    result.add(rb);
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final RousseauBean subject) {
    // No Operations
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  private void test_RousseauBean__() {
    try {
      RousseauBean rb = new Stub();
      validateTypeInvariants(rb);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/



  /*<section name="instance methods">*/
  //-------------------------------------------------------------------

  protected final void test_getWildExceptions__() {
    Iterator iter = getCases().iterator();
    while (iter.hasNext()) {
      RousseauBean subject = (RousseauBean)iter.next();
      test_getWildExceptions__(subject);
    }
  }

  protected final void test_getWildExceptions__(final RousseauBean subject) {
    try {
      CompoundPropertyException result = subject.getWildExceptions();
      post_getWildExceptions__(subject, result);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected void post_getWildExceptions__(
      final RousseauBean subject,
      final CompoundPropertyException result) {
    validate(result != null);
    validate(!result.isClosed());
    validate(result.getOrigin() == subject);
    validate(result.getPropertyName() == null);
    validate(result.getMessage() == null);
    validate(result.getCause() == null);
  }


  protected final void test_isCivilized__() {
    Iterator iter = getCases().iterator();
    while (iter.hasNext()) {
      RousseauBean subject = (RousseauBean)iter.next();
      test_isCivilized__(subject);
    }
  }

  protected final void test_isCivilized__(final RousseauBean subject) {
    try {
      boolean result = subject.isCivilized();
      validate(subject.getWildExceptions().getElementExceptions().isEmpty() == result);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_checkCivility__() {
    Iterator iter = getCases().iterator();
    while (iter.hasNext()) {
      RousseauBean subject = (RousseauBean)iter.next();
      test_checkCivility__(subject);
    }
  }

  protected final void test_checkCivility__(final RousseauBean subject) {
    try {
      subject.checkCivility();
      validate(subject.isCivilized()); // if we get here, this must be true
      validateTypeInvariants(subject);
    }
    catch (CompoundPropertyException cpe) {
      validate(! subject.isCivilized());
      validate(cpe.hasSameValues(subject.getWildExceptions()));
      validate(cpe.isClosed());
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_normalize__() {
    Iterator iter = getCases().iterator();
    while (iter.hasNext()) {
      RousseauBean subject = (RousseauBean)iter.next();
      test_normalize__(subject);
    }
  }

  protected final void test_normalize__(final RousseauBean subject) {
    try {
      subject.normalize();
      post_normalize__(subject);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected void post_normalize__(final RousseauBean subject) {
    // nothing to validate here
  }
  
  protected final void test_hasSameValues_RousseauBean_() {
    Iterator iterOther = getCases().iterator();
    RousseauBean other1 = null;
    if (iterOther.hasNext()) {
      other1 = (RousseauBean)iterOther.next();
    }
    else {
      assert false;
    }
    Iterator iter1 = getCases().iterator();
    while (iter1.hasNext()) {
      RousseauBean subject = (RousseauBean)iter1.next();
      test_hasSameValues_RousseauBean_(subject, other1);
      test_hasSameValues_RousseauBean_(subject, subject);
      test_hasSameValues_RousseauBean_(subject, null);
      if (other1 != null) {
        test_hasSameValues_RousseauBean_(other1, subject);
      }
    }
  }

  protected final void test_hasSameValues_RousseauBean_(
      final RousseauBean subject,
      final RousseauBean other) {
    try {
//      System.out.println("testing hasSameValues(RousseauBean)"); //$NON-NLS-1$
//      System.out.println("subject: " + subject.toString()); //$NON-NLS-1$
//      System.out.println("other: " + ((other == null) ? "null" : other.toString())); //$NON-NLS-1$ //$NON-NLS-2$
// Logging doesn't work
//      if (LOG.isDebugEnabled()) {
//        LOG.debug("testing hasSameValues(RousseauBean)"); //$NON-NLS-1$
//        LOG.debug("subject: " + subject.toString()); //$NON-NLS-1$
//        LOG.debug("other: " + ((other == null) ? "null" : other.toString())); //$NON-NLS-1$
//      }
      boolean expectedResult
        = expected_hasSameValues_RousseauBean_(subject, other);
//      System.out.println("expectedResult: " +  (expectedResult ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//      LOG.debug("expectedResult: " +  (expectedResult ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      boolean result = subject.hasSameValues(other);
//      System.out.println("result: " + (result ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//      LOG.debug("result: " + (result ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      validate(result == expectedResult);
      if (result != expectedResult) {
        System.out.println("!!!!!!!!!!!!! result != expected"); //$NON-NLS-1$
      }
      validateTypeInvariants(subject);
      if (other != null) {
        validateTypeInvariants(other);
      }
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected boolean expected_hasSameValues_RousseauBean_(
      final RousseauBean subject,
      final RousseauBean other) {
    return (other != null)
           && (subject.getClass() == other.getClass());
  }

  protected final void test_equals_Object_() {
    Set cases = getCases();
    Iterator iterSubject = cases.iterator();
    while (iterSubject.hasNext()) {
      RousseauBean subject = (RousseauBean)iterSubject.next();
      test_equals_Object_(subject, subject);
      test_equals_Object_(subject, null);
      test_equals_Object_(subject, new Object());
      test_equals_Object_(subject, new Stub());
    }
  }

  protected final void test_equals_Object_(final RousseauBean subject,
                                           final Object other) {
    try {
      boolean result = subject.equals(other);
      boolean expected = (subject == other);
      validate(result == expected);
      validateTypeInvariants(subject);
      if (other instanceof RousseauBean) {
        validateTypeInvariants((RousseauBean)other);
      }
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_hashCode__() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      RousseauBean subject = (RousseauBean)iterSubject.next();
      test_hashCode__(subject);
    }
  }

  protected final void test_hashCode__(final RousseauBean subject) {
    try {
      subject.hashCode(); // nothing to test the result for
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/

}
