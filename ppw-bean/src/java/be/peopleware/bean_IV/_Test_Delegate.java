package be.peopleware.bean_IV;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_Object;
import be.peopleware.test_I.java.lang._Test_String;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_Delegate extends _Test_Object {

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
    Test.main(new String[] {"be.peopleware.bean_II._Test_Delegate"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_Delegate_Object_String_();
  }

  protected void testInstanceMethods() {
    test_hasSameValues_Object_();
  }


  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected Delegate create_Delegate(final Object delegatingBean,
                                     final String propertyName) {
    return new Delegate(delegatingBean, propertyName);
  }


  public Set getCases() {
    Set result = new HashSet();
    Delegate cs;
    Iterator iterString = new _Test_String().getCases().iterator();
    while (iterString.hasNext()) {
      String propertyName = (String)iterString.next();
      try {
        Object delegatingBean = new Object();
        if (pre_Delegate_Object_String_(delegatingBean, propertyName)) {
          cs = create_Delegate(delegatingBean, propertyName);
          result.add(cs);
        }
      }
      catch (Throwable t) {
        assert true : "If create throws an exception we are not intrested."; //$NON-NLS-1$
      }
    }
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Delegate subject) {
    validate(subject.getDelegatingBean() != null);
    validate((subject.getPropertyName() != null)
             && subject.getPropertyName().length() > 0);
    validate(!Cloneable.class.isAssignableFrom(subject.getClass()));
    validate(!Serializable.class.isAssignableFrom(subject.getClass()));
    /* (forall Object o; ; equals(o) == (this == o));
            do not override the <code>equals</code> method */
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected final void test_Delegate_Object_String_() {
    Iterator iterString = new _Test_String().getCases().iterator();
    while (iterString.hasNext()) {
      String propertyName = (String)iterString.next();
      Object delegatingBean = new Object();
      if (pre_Delegate_Object_String_(delegatingBean, propertyName)) {
        test_Delegate_Object_String_(delegatingBean, propertyName);
      }
    }
  }

  protected boolean pre_Delegate_Object_String_(final Object delegatingBean,
                                                final String propertyName) {
    return (delegatingBean != null)
           && (propertyName != null)
           && (propertyName.length() > 0);
  }

  protected final void test_Delegate_Object_String_(final Object delegatingBean,
                                                    final String propertyName) {
    try {
      Delegate subject = new Delegate(delegatingBean, propertyName);
      validate(subject.getDelegatingBean() == delegatingBean);
      validate(subject.getPropertyName().equals(propertyName));
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/



  /*<section name="instance methods">*/
  //-------------------------------------------------------------------

  protected final void test_hasSameValues_Object_() {
    Iterator iterDelegate1 = getCases().iterator();
    while (iterDelegate1.hasNext()) {
      Delegate subject = (Delegate)iterDelegate1.next();
      Iterator iterDelegate2 = getCasesWithNull().iterator();
      while (iterDelegate2.hasNext()) {
        Object other = iterDelegate2.next();
        test_hasSameValues_Object_(subject, other);
        test_hasSameValues_Object_(subject, new Object());
      }
    }
  }

  protected void test_hasSameValues_Object_(final Delegate subject,
                                            final Object other) {
    try {
      boolean result = subject.hasSameValues(other);
      post_hasSameValues_Object_(subject, other, result);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected void post_hasSameValues_Object_(final Delegate subject,
                                            final Object other,
                                            final boolean result) {
    validate(result ? ((other != null)
                          && (subject.getClass() == other.getClass()))
                    : true);
  }

  /*</section>*/

}
