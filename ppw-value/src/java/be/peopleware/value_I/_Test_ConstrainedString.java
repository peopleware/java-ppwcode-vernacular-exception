package be.peopleware.value_I;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import be.peopleware.bean_V.Delegate;
import be.peopleware.bean_V._Test_Delegate;
import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_Integer;
import be.peopleware.test_I.java.lang._Test_String;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_ConstrainedString extends _Test_Delegate {

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
    Test.main(new String[]
        {"be.peopleware.sql.constrainedstring._Test_ConstrainedString"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_ConstrainedString_Object_String_int_();
    test_ConstrainedString_Object_String_int_String_();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
    test_setString_String_();
  }


  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected final Delegate create_Delegate(final Object delegatingBean,
                                           final String propertyName) {
    return create_ConstrainedString(delegatingBean, propertyName, 10);
  }

  protected ConstrainedString create_ConstrainedString(
      final Object delegatingBean,
      final String propertyName,
      final int maxLength) {
    return new ConstrainedString(delegatingBean, propertyName, maxLength);
  }


  public Set getCases() {
    Set result = new HashSet();
    ConstrainedString cs;
    Iterator iterString = new _Test_String().getCases().iterator();
    while (iterString.hasNext()) {
      String propertyName = (String)iterString.next();
      String str = propertyName;    
      Iterator iterInteger = new _Test_Integer().getCases().iterator();
      while (iterInteger.hasNext()) {
        int maxLength = ((Integer)iterInteger.next()).intValue();
        try {
          Object delegatingBean = new Object();
          if (pre_ConstrainedString_Object_String_int_(delegatingBean,
                                                       propertyName,
                                                       maxLength)) {
            cs = create_ConstrainedString(delegatingBean,
                                          propertyName,
                                          maxLength);
            cs.setString(str);
            result.add(cs);
          }
        }
        catch (Throwable t) {
          assert true : "If create throws an exception we are not intrested."; //$NON-NLS-1$
        }
      }
    }
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Delegate subject) {
    ConstrainedString cs = (ConstrainedString)subject;
    validate(cs.getMaxLength() >=  0);
    validate(cs.getString() != null);
    validate(cs.getString().length() <= cs.getMaxLength());
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected final void test_ConstrainedString_Object_String_int_() {
    Iterator iterString1 = new _Test_String().getCases().iterator();
    while (iterString1.hasNext()) {
      String propertyName = (String)iterString1.next();
      Iterator iterInteger = new _Test_Integer().getCases().iterator();
      while (iterInteger.hasNext()) {
        int maxLength = ((Integer)iterInteger.next()).intValue();
        Object delegatingBean = new Object();
        if (pre_ConstrainedString_Object_String_int_(delegatingBean,
                                                     propertyName,
                                                     maxLength)) {
          test_ConstrainedString_Object_String_int_(delegatingBean,
                                                    propertyName,
                                                    maxLength);
        }
      }
    }
  }

  protected boolean pre_ConstrainedString_Object_String_int_(
      final Object delegatingBean,
      final String propertyName,
      final int maxLength) {
    return (delegatingBean != null)
           && (propertyName != null) && (propertyName.length() > 0)
           && (maxLength > 0);
  }

  protected final void test_ConstrainedString_Object_String_int_(
      final Object delegatingBean,
      final String propertyName,
      final int maxLength) {
    try {
      ConstrainedString subject = new ConstrainedString(delegatingBean,
                                                        propertyName,
                                                        maxLength);
      validate(subject.getDelegatingBean() == delegatingBean);
      validate(subject.getPropertyName().equals(propertyName));
      validate(subject.getString().equals("")); //$NON-NLS-1$
      validate(subject.getMaxLength() == maxLength);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_ConstrainedString_Object_String_int_String_() {
    Iterator iterString1 = new _Test_String().getCases().iterator();
    while (iterString1.hasNext()) {
      String propertyName = (String)iterString1.next();
      Iterator iterInteger = new _Test_Integer().getCases().iterator();
      while (iterInteger.hasNext()) {
        int maxLength = ((Integer)iterInteger.next()).intValue();
        Object delegatingBean = new Object();
        Iterator iterString2 = new _Test_String().getCases().iterator();
        while (iterString2.hasNext()) {
          String value = (String)iterString2.next();
          if (pre_ConstrainedString_Object_String_int_String_(delegatingBean,
                                                              propertyName,
                                                              maxLength,
                                                              value)) {
            test_ConstrainedString_Object_String_int_String_(delegatingBean,
                                                             propertyName,
                                                             maxLength,
                                                             value);
          }
        }
      }
    }
  }

  protected boolean pre_ConstrainedString_Object_String_int_String_(
      final Object delegatingBean,
      final String propertyName,
      final int maxLength,
      final String value) {
    return pre_ConstrainedString_Object_String_int_(delegatingBean,
                                                    propertyName,
                                                    maxLength)
              && (value != null) && (value.length() <= maxLength);
  }

  protected final void test_ConstrainedString_Object_String_int_String_(
      final Object delegatingBean,
      final String propertyName,
      final int maxLength,
      final String value) {
    try {
      ConstrainedString subject = new ConstrainedString(delegatingBean,
                                                        propertyName,
                                                        maxLength,
                                                        value);
      validate(subject.getDelegatingBean() == delegatingBean);
      validate(subject.getPropertyName().equals(propertyName));
      validate(subject.getString().equals(value)); //$NON-NLS-1$
      validate(subject.getMaxLength() == maxLength);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/


  /*<section name="instance methods">*/
  //-------------------------------------------------------------------

  protected final void test_setString_String_() {
    Iterator iterCS = getCases().iterator();
    while (iterCS.hasNext()) {
      ConstrainedString subject = (ConstrainedString)iterCS.next();
      Iterator iterString = new _Test_String().getCasesWithNull().iterator();
      while (iterString.hasNext()) {
        String str = (String)iterString.next();
        test_setString_String_(subject, str);
      }
    }
  }

  protected void test_setString_String_(final ConstrainedString subject,
                                        final String str) {
    try {
      subject.setString(str);
      validate(str == null ? subject.getString().equals("") //$NON-NLS-1$
                           : subject.getString().equals(str));
      validateTypeInvariants(subject);
    }
    catch (TooLongException csExc) {
      validate(str.length() > subject.getMaxLength());
      validate(csExc.getOrigin() == subject.getDelegatingBean());
      validate(csExc.getPropertyName().equals(subject.getPropertyName()));
      validate(csExc.getString() == null
               ? str == null
               : csExc.getString().equals(str));
      validate(csExc.getMaxLength() == subject.getMaxLength());
      validate(csExc.getMessage().endsWith("TOO_LONG")); //$NON-NLS-1$
      validate(csExc.getCause() == null);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/

}
