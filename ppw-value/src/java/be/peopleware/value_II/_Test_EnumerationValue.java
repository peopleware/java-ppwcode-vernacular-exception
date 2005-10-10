/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_String;



/**
 * @author    David Van Keer
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_EnumerationValue extends _Test_ImmutableValue {

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
    Test.main(new String[] {"be.peopleware.value_II._Test_EnumerationValue"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_EnumerationValue_String_();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
  }


  protected class Stub extends EnumerationValue {
    public Stub(final String d) {
      super(d);
    }
  }

  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected final ImmutableValue create_ImmutableValue() {
    return create_EnumerationValue("DEFAULT"); //$NON-NLS-1$
  }

  protected EnumerationValue create_EnumerationValue(
      final String discriminator) {
    return new Stub(discriminator);
  }

  public Set getCases() {
    Set result = new HashSet();
    result.add(create_EnumerationValue("DISCRIMINATOR")); //$NON-NLS-1$
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  // NOP

  /**
   * For subclasses.
   */
  protected void validateVALUETypeInvariants(final Value subject,
                                             final Map map) {
    EnumerationValue ev = (EnumerationValue)subject;
    validate(map.values().contains(ev));
    validate(ev.equals(map.get(ev.toString())));
  }

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected void test_EnumerationValue_String_() {
    Iterator iterString = new _Test_String().getCases().iterator();
    while (iterString.hasNext()) {
      String d = (String)iterString.next();
      test_EnumerationValue_String_(d);
    }
  }

  protected void test_EnumerationValue_String_(final String d) {
    if ((d != null) && (d.length() > 0)) {
      try {
        EnumerationValue subject = new Stub(d);
        validate(subject.toString().equals(d));
        validateTypeInvariants(subject);
      }
      catch (Throwable t) {
        unexpectedThrowable(t);
      }
    }
  }

  /**
   * for the benefit of the subclasses
   */
  protected void test_VALUES(final Map values, final Class clazz) {
    try {
      validate(values != null);
      validate(values.size() > 0);
      validate(!values.keySet().contains(null));
      validate(!values.values().contains(null));
      Iterator iter = values.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        validate(entry.getValue().getClass() == clazz);
        validate(entry.getKey() instanceof String);
      }
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/


  /*<section name="instance methods">*/
  //-------------------------------------------------------------------



  /*</section>*/


  protected void post_equals_Object_(final Value subject,
                                     final Object other,
                                     final boolean result) {
    super.post_equals_Object_(subject, other, result);
    if (result) {
      validate(subject.toString().equals(other.toString()));
    }
  }

}

