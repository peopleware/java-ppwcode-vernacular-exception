package be.peopleware.i18n;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_Object;
import be.peopleware.test_I.java.lang._Test_String;
import be.peopleware.test_I.java.util._Test_Locale;


/**
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public class _Test_DefaultResourceBundleLoadStrategy extends _Test_Object {

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
        {"be.peopleware.i18n._Test_DefaultResourceBundleLoadStrategy"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_DefaultResourceBundleLoadStrategy();
  }

  protected void testInstanceMethods() {
    test_setLocale_Locale_();
    test_loadResourceBundle_String_();
  }



  /* <section name="test cases"> */
  //-------------------------------------------------------------------

  protected DefaultResourceBundleLoadStrategy
      create_DefaultResourceBundleLoadStrategy() {
    return new DefaultResourceBundleLoadStrategy();
  }

  public Set getCases() {
    Set result = new HashSet();
    DefaultResourceBundleLoadStrategy drbls;
    Iterator iterLocale = new _Test_Locale().getCasesWithNull().iterator();
    while (iterLocale.hasNext()) {
      Locale locale = (Locale)iterLocale.next();
      try {
        drbls = create_DefaultResourceBundleLoadStrategy();
        drbls.setLocale(locale);
        result.add(drbls);
      }
      catch (Throwable t) {
        assert true : "If create throws an exception we are not intrested."; //$NON-NLS-1$
      }
    }
    return result;
  }

  /* </section> */



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(
      final DefaultResourceBundleLoadStrategy subject) {
    // NOP: no type invariants
  }

  /*</section>*/



  /* <section name="class methods"> */
  //-------------------------------------------------------------------

  protected final void test_DefaultResourceBundleLoadStrategy() {
    try {
      DefaultResourceBundleLoadStrategy subject
        = new DefaultResourceBundleLoadStrategy();
      validate(subject.getLocale() == null);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /* </section> */


  /* <section name="instance methods"> */
  //-------------------------------------------------------------------
  protected final void test_setLocale_Locale_() {
    Iterator iterDRBLS = getCases().iterator();
    while (iterDRBLS.hasNext()) {
      DefaultResourceBundleLoadStrategy subject
        = (DefaultResourceBundleLoadStrategy)iterDRBLS.next();
      Iterator iterLocale = new _Test_Locale().getCasesWithNull().iterator();
      while (iterLocale.hasNext()) {
        Locale locale = (Locale)iterLocale.next();
        test_setLocale_Locale_(subject, locale);
      }
    }
  }

  protected void test_setLocale_Locale_(
      final DefaultResourceBundleLoadStrategy subject,
      final Locale locale) {
    try {
      subject.setLocale(locale);
      validate(subject.getLocale() ==  locale);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected static final String TEST_RESOURCE_BASENAME
      = "be.peopleware.i18n._Test_DefaultResourceBundleLoadStrategy"; //$NON-NLS-1$

  protected static final String TEST_RESOURCE_KEY = "locale"; //$NON-NLS-1$

  protected final void test_loadResourceBundle_String_() {
    Set stringCases = new _Test_String().getCasesWithNull();
    stringCases.add(TEST_RESOURCE_BASENAME); //$NON-NLS-1$
    Iterator iterSLRBLS = getCases().iterator();
    while (iterSLRBLS.hasNext()) {
      DefaultResourceBundleLoadStrategy subject
          = (DefaultResourceBundleLoadStrategy)iterSLRBLS.next();
      Iterator iterString = stringCases.iterator();
      while (iterString.hasNext()) {
        String basename = (String)iterString.next();
        test_loadResourceBundle_String_(subject, basename);
      }
    }
  }

  protected void test_loadResourceBundle_String_(
      final DefaultResourceBundleLoadStrategy subject,
      final String basename) {
    try {
      Locale.setDefault(Locale.ENGLISH);
      ResourceBundle result = subject.loadResourceBundle(basename);
      validate(((basename == null)
               || (basename.equals(""))) ? result == null : true); //$NON-NLS-1$
       /* we expect a valid result if the basename is
        * TEST_RESOURCE_BASENAME; these files contain an entry "locale",
        * which contains the name of the locale that the file is for if
        * basename is not TEST_RESOURCE_BASENAME, String are so
        * nonsensical that we expect the resource bundle not to be found
        */
      if (TEST_RESOURCE_BASENAME.equals(basename)) {
        validate(result != null);
        String resource = null;
        try {
          resource = result.getString(TEST_RESOURCE_KEY);
        }
        catch (NullPointerException npe) {
          assert false : "TEST_RESOURCE_KEY != null"; //$NON-NLS-1$
        }
        catch (ClassCastException ccExc) {
          assert false : "not a string in a properties file (impossible)"; //$NON-NLS-1$
        }
        catch (MissingResourceException mrExc) {
          validate(false);
        }
        /* the following tests depend on intimate knowledge of the cases for the
         * locale and the Test_DefaultResourceBundleLoadStrategy*.properties
         * file in this directory.
         */
        if (subject.getLocale() == null) {
          validate(resource.equals("DEFAULT") //$NON-NLS-1$
                   || resource.startsWith(Locale.getDefault().getLanguage()));
        }
        else if (subject.getLocale().toString().equals("nl_BE")) { //$NON-NLS-1$
          validate(resource != null
                   && resource.equals(subject.getLocale().toString()));
        }
        else if (subject.getLocale().getLanguage().equals("nl") //$NON-NLS-1$
                 || subject.getLocale().toString().equals("qq")) { //$NON-NLS-1$
          validate(resource != null
                   && resource.equals(subject.getLocale().getLanguage()));
        }
        else {
          validate(resource != null
                   && resource.equals("DEFAULT")); //$NON-NLS-1$
        }
      }
      else {
        validate(result == null);
      }
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /* </section> */

}