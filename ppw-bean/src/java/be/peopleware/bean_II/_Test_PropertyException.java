package be.peopleware.bean_II;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.peopleware.exception_I.LocalizedMessageException;
import be.peopleware.exception_I._Test_LocalizedMessageException;
import be.peopleware.i18n_I.DefaultResourceBundleLoadStrategy;
import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_String;
import be.peopleware.test_I.java.lang._Test_Throwable;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_PropertyException extends _Test_LocalizedMessageException {

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
        {"be.peopleware.bean_II._Test_PropertyException"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_PropertyException_Object_String_String_Throwable_();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
    test_getLocalizedMessageResourceBundleBasename__();
    test_getLocalizedMessageKeys__();
    test_hasProperties_Object_String_String_Throwable_();
    test_hasSameValues_PropertyException_();
  }

  /* <section name="test cases"> */
  //-------------------------------------------------------------------

   protected final LocalizedMessageException
      create_LocalizedMessageException(final String message,
                                       final Throwable t) {
    return create_PropertyException(new Object(),
                                    "testPropertyName", //$NON-NLS-1$
                                    message,
                                    t);
  }

  protected PropertyException
      create_PropertyException(final Object origin,
                               final String propertyName,
                               final String message,
                               final Throwable cause) {
    return new PropertyException(origin, propertyName, message, cause);
  }

  protected boolean pre_PropertyException(final Object origin,
                                          final String propertyName,
                                          final String message,
                                          final Throwable cause) {
    return (origin != null)
            && ((propertyName != null) ? (!propertyName.equals("")) : true) //$NON-NLS-1$
            && ((message != null) ? (!message.equals("")) : true); //$NON-NLS-1$
  }

  public Set getCases() {
    Set result = new HashSet();
    Iterator iterString1 = new _Test_String().getCasesWithNull().iterator();
    while (iterString1.hasNext()) {
      String propertyName = (String)iterString1.next();
      Iterator iterString2 = new _Test_String().getCasesWithNull().iterator();
      while (iterString2.hasNext()) {
        String message = (String)iterString2.next();
        Throwable cause = new Throwable();
        try {
          Object origin = new Object();
          if (pre_PropertyException(origin, propertyName, message, cause)) {
            result.add(create_PropertyException(origin,
                                                propertyName,
                                                message,
                                                cause));
          }
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

  protected void validateTypeInvariants(final LocalizedMessageException subject) {
    super.validateTypeInvariants(subject);
    PropertyException pe = (PropertyException)subject;
    validate(pe.getOrigin() != null);
    validate(pe.getPropertyName() == null
             || !pe.getPropertyName().equals("")); //$NON-NLS-1$
    validate(pe.getMessage() == null
             || !pe.getMessage().equals("")); //$NON-NLS-1$
  }

  /*</section>*/



  /* <section name="class methods"> */
  //-------------------------------------------------------------------

  protected final void test_PropertyException_Object_String_String_Throwable_() {
    Iterator iterString1 = new _Test_String().getCasesWithNull().iterator();
    while (iterString1.hasNext()) {
      String propertyName = (String)iterString1.next();
      Iterator iterString2 = new _Test_String().getCasesWithNull().iterator();
      while (iterString2.hasNext()) {
        String message = (String)iterString2.next();
        Iterator iterThrowable
            = new _Test_Throwable().getCasesWithNull().iterator();
        while (iterThrowable.hasNext()) {
          Throwable cause = (Throwable)iterThrowable.next();
          test_PropertyException_Object_String_String_Throwable_(new Object(),
                                                                 propertyName,
                                                                 message,
                                                                 cause);
        }
      }
    }
  }

  protected void test_PropertyException_Object_String_String_Throwable_(
      final Object origin,
      final String propertyName,
      final String message,
      final Throwable cause) {
    if (pre_PropertyException_Object_String_String_Throwable_(origin,
                                                              propertyName,
                                                              message,
                                                              cause)) {
      try {
        PropertyException subject = new PropertyException(origin,
                                                          propertyName,
                                                          message,
                                                          cause);
        validate(subject.getOrigin() == origin);
        validate((propertyName != null)
                 ? subject.getPropertyName().equals(propertyName)
                 : subject.getPropertyName() == null);
        validate((message != null)
                 ? subject.getMessage().equals(message)
                 : subject.getMessage() == null);
        validate(subject.getCause() == cause);
        validate(subject.getLocalizedMessageResourceBundleLoadStrategy().getClass()
                     == DefaultResourceBundleLoadStrategy.class);
        validateTypeInvariants(subject);
      }
      catch (Throwable t) {
        unexpectedThrowable(t);
      }
    }
  }

  protected boolean pre_PropertyException_Object_String_String_Throwable_(
      final Object origin,
      final String propertyName,
      final String message,
      final Throwable cause) {
    return (origin != null)
            && ((propertyName != null) ? (!propertyName.equals("")) : true) //$NON-NLS-1$
            && ((message != null) ? (!message.equals("")) : true); //$NON-NLS-1$
  }

  /* </section> */



  /* <section name="instance methods"> */
  //-------------------------------------------------------------------

  protected final void test_getLocalizedMessageResourceBundleBasename__() {
    Iterator iterPE = getCases().iterator();
    while (iterPE.hasNext()) {
      PropertyException pe = (PropertyException)iterPE.next();
      test_getLocalizedMessageResourceBundleBasename__(pe);
    }
  }

  protected void test_getLocalizedMessageResourceBundleBasename__(
      final PropertyException subject) {
    try {
      String result = subject.getLocalizedMessageResourceBundleBasename();
      validate(result.equals(subject.getOrigin().getClass().getName()));
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_getLocalizedMessageKeys__() {
    Iterator iterPE = getCases().iterator();
    while (iterPE.hasNext()) {
      PropertyException pe = (PropertyException)iterPE.next();
      test_getLocalizedMessageResourceBundleBasename__(pe);
    }
  }

  protected void
      test_getLocalizedMessageKeys__(final PropertyException subject) {
    try {
      String[] result = subject.getLocalizedMessageKeys();
      validate(result != null);
      validate(result.length == ((subject.getMessage() != null) ? 2 : 1));
      validate(result[0] != null);
      validate((subject.getPropertyName() != null)
               && (subject.getMessage() != null)
                      ? result[0].equals(getClass().getName()
                                        + "." + subject.getPropertyName() //$NON-NLS-1$
                                        + "." + subject.getMessage()) //$NON-NLS-1$
                      : true);
      validate((subject.getPropertyName() == null)
               && (subject.getMessage() != null)
                      ? result[0].equals(getClass().getName()
                                         + "." + subject.getMessage()) //$NON-NLS-1$
                      : true);
      validate((subject.getPropertyName() != null)
               && (subject.getMessage() == null)
                      ? result[0].equals(getClass().getName()
                                         + "." + subject.getPropertyName()) //$NON-NLS-1$
                      : true);
      validate((subject.getPropertyName() == null)
               && (subject.getMessage() == null)
                      ? result[0].equals(getClass().getName())
                      : true);
      validate((subject.getMessage() != null)
                   ? (result[1] != null)
                     && result[1].equals("message." + subject.getMessage()) //$NON-NLS-1$
                   : true);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_hasSameValues_PropertyException_() {
    Set cases = getCases();
    Iterator iterSubject = cases.iterator();
    while (iterSubject.hasNext()) {
      PropertyException subject =  (PropertyException)iterSubject.next();
      Set others = getCasesWithNull();
      others.addAll(cases);
      Iterator iterOther = others.iterator();
      while (iterOther.hasNext()) {
        PropertyException other =  (PropertyException)iterOther.next();
        test_hasSameValues_PropertyException_(subject, other);
      }
    }
  }

  protected void test_hasSameValues_PropertyException_(
      final PropertyException subject,
      final PropertyException other) {
    try {
      boolean result = subject.hasSameValues(other);
      validate(result
               == ((other != null)
                   && subject.hasProperties(other.getOrigin(),
                                    other.getPropertyName(),
                                    other.getMessage(),
                                    other.getCause())));
      validateTypeInvariants(subject);
      if (other != null) {
        validateTypeInvariants(other);
      }
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_hasProperties_Object_String_String_Throwable_() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      PropertyException subject =  (PropertyException)iterSubject.next();
      HashSet objects = new HashSet();
      objects.add(new Object());
      objects.add(null);
      Iterator iterOrigin = objects.iterator();
      while (iterOrigin.hasNext()) {
        Object origin = iterOrigin.next();
        Iterator iterPropertyName
            = new _Test_String().getCasesWithNull().iterator();
        while (iterPropertyName.hasNext()) {
          String propertyName = (String)iterPropertyName.next();
          Iterator iterMessage
              = new _Test_String().getCasesWithNull().iterator();
          while (iterMessage.hasNext()) {
            String message = (String)iterMessage.next();
            HashSet causes = new HashSet();
            causes.add(null);
            causes.add(new Throwable());
            causes.add(subject.getCause());
            Iterator iterCause = causes.iterator();
            while (iterCause.hasNext()) {
              Throwable cause = (Throwable)iterCause.next();
              test_hasProperties_Object_String_String_Throwable_(subject,
                                                                 origin,
                                                                 propertyName,
                                                                 message,
                                                                 cause);
            }
          }
        }
      }
    }
  }

  protected void test_hasProperties_Object_String_String_Throwable_(
      final PropertyException subject,
      final Object origin,
      final String propertyName,
      final String message,
      final Throwable cause) {
    try {
      boolean result = subject.hasProperties(origin,
                                             propertyName,
                                             message,
                                             cause);
      validate(result
               == ((subject.getOrigin() == origin)
                     && ((subject.getPropertyName() == null)
                          ? propertyName == null
                          : subject.getPropertyName().equals(propertyName))
                     && ((subject.getCause() == null)
                          ? cause == null
                          : subject.getCause().equals(cause))
                     && ((subject.getMessage() == null)
                          ? message == null
                          : subject.getMessage().equals(message))));
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /* </section> */

}