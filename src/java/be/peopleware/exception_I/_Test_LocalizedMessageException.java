package be.peopleware.exception_I;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.peopleware.i18n_I.DefaultResourceBundleLoadStrategy;
import be.peopleware.i18n_I.ResourceBundleLoadStrategy;
import be.peopleware.i18n_I._Test_DefaultResourceBundleLoadStrategy;
import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_Object;
import be.peopleware.test_I.java.lang._Test_String;
import be.peopleware.test_I.java.lang._Test_Throwable;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_LocalizedMessageException extends _Test_Object {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /* </section> */


  public static void main(final String[] args) {
    Test.main(new String[]
        {"be.peopleware.exception_I._Test_LocalizedMessageException"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_LocalizedMessageException_String_Throwable_();
  }

  protected void testInstanceMethods() {
    test_setLocalizedMessageResourceBundleLoadStrategy_ResourceBundleLoadStrategy_();
    test_getLocalizedMessage__();
  }

  protected static final String TEST_KEY = "key";  //$NON-NLS-1$

  protected class Stub_LocalizedMessageException
      extends LocalizedMessageException {
    public Stub_LocalizedMessageException(final String message,
                                          final Throwable cause) {
      super(message, cause);
    }

    public String[] getLocalizedMessageKeys() {
      return new String[] {TEST_KEY};
    }

  }


  /* <section name="test cases"> */
  //-------------------------------------------------------------------

  protected LocalizedMessageException
      create_LocalizedMessageException(final String message,
                                       final Throwable t) {
    return new Stub_LocalizedMessageException(message, t);
  }

  public Set getCases() {
    Set result = new HashSet();
    Iterator iterString = new _Test_String().getCasesWithNull().iterator();
    while (iterString.hasNext()) {
      String message = (String)iterString.next();
      Iterator iterThrowable
          = new _Test_Throwable().getCasesWithNull().iterator();
      while (iterThrowable.hasNext()) {
        Throwable cause = (Throwable)iterThrowable.next();
        try {
          result.add(create_LocalizedMessageException(message, cause));
        }
        catch (Throwable t) {
          assert true : "If create throws an exception we are not intrested."; //$NON-NLS-1$
        }
      }
    }
    return result;
  }

  /* </section> */



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(
      final LocalizedMessageException subject) {
    if (subject.getLocalizedMessageKeys() != null) {
      for (int i = 0; i < subject.getLocalizedMessageKeys().length; i++) {
        validate((subject.getLocalizedMessageKeys()[i] != null)
                 && (!subject.getLocalizedMessageKeys()[i].equals(""))); //$NON-NLS-1$
      }
    }
  }

  /*</section>*/



  /* <section name="class methods"> */
  //-------------------------------------------------------------------

  protected final void test_LocalizedMessageException_String_Throwable_() {
    Iterator iterString = new _Test_String().getCasesWithNull().iterator();
    while (iterString.hasNext()) {
      String message = (String)iterString.next();
      Iterator iterThrowable
          = new _Test_Throwable().getCasesWithNull().iterator();
      while (iterThrowable.hasNext()) {
        Throwable t = (Throwable)iterThrowable.next();
        test_LocalizedMessageException_String_Throwable_(message, t);
      }
    }
  }

  protected void test_LocalizedMessageException_String_Throwable_(
      final String message,
      final Throwable cause) {
    try {
      LocalizedMessageException subject =
          new Stub_LocalizedMessageException(message, cause);
      validate((message != null)
               ? subject.getMessage().equals(message)
               : subject.getMessage() == null);
      validate(subject.getCause() == cause);
      validate(
          subject.getLocalizedMessageResourceBundleLoadStrategy().getClass()
          == DefaultResourceBundleLoadStrategy.class);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /* </section> */


  /* <section name="instance methods"> */
  //-------------------------------------------------------------------

  protected final void test_setLocalizedMessageResourceBundleLoadStrategy_ResourceBundleLoadStrategy_() {
    Iterator iterLME = getCases().iterator();
    while (iterLME.hasNext()) {
      LocalizedMessageException lme
        = (LocalizedMessageException)iterLME.next();
      Iterator iterRBLS = new _Test_DefaultResourceBundleLoadStrategy()
          .getCasesWithNull().iterator();
      while (iterRBLS.hasNext()) {
        ResourceBundleLoadStrategy rbls
            = (ResourceBundleLoadStrategy)iterRBLS.next();
        test_setLocalizedMessageResourceBundleLoadStrategy_ResourceBundleLoadStrategy_(lme, rbls);
      }
    }
  }

  protected void test_setLocalizedMessageResourceBundleLoadStrategy_ResourceBundleLoadStrategy_(
      final LocalizedMessageException subject,
      final ResourceBundleLoadStrategy rbls) {
    try {
      subject.setLocalizedMessageResourceBundleLoadStrategy(rbls);
      validate(subject.getLocalizedMessageResourceBundleLoadStrategy()
               ==  rbls);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected static final String TEST_MESSAGE = "TEST"; //$NON-NLS-1$

  protected final void test_getLocalizedMessage__() {
    Iterator iterLME = getCases().iterator();
    while (iterLME.hasNext()) {
      LocalizedMessageException lme = (LocalizedMessageException)iterLME.next();
      test_getLocalizedMessage__(lme);
    }
    test_getLocalizedMessage__(create_LocalizedMessageException(TEST_MESSAGE,
                                                                null));
  }

  protected static final String TEST_ENTRY = "test entry"; //$NON-NLS-1$

  protected void test_getLocalizedMessage__(
      final LocalizedMessageException subject) {
    try {
      String result = subject.getLocalizedMessage();
      /* there needs to be a properties file with the name of the exception
       * class in that directory for the test to work.
       */
      validate((result == null)
               || result.equals(TEST_ENTRY)
               || result.equals(subject.getMessage()));
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /* </section> */

}