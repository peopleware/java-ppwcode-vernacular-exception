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
import be.peopleware.test_I.java.lang._Test_String;


/**
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class _Test_Address extends _Test_MutableValue {

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
    Test.main(new String[] {"be.peopleware.value_I._Test_Address"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_Address__();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
    test_setStreetAddress_String_();
    test_setPostalCode_String_();
    test_setCity_String_();
    test_setState_String_();
    test_setCountry_Country_();
  }


  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected final MutableValue create_ImmutableValue() {
    return create_Address();
  }

  protected MutableValue create_Address() {
    return new Address();
  }

  public Set getCases() {
    java.util.Set result = new HashSet();
    result.add(create_Address());
    String postalCode = "2660"; //$NON-NLS-1$
    String streetAddress = "Cdt. Weynsstraat 85"; //$NON-NLS-1$
    String city = "Hoboken"; //$NON-NLS-1$
    String state = "Antwerp"; //$NON-NLS-1$
    Country country = new Country();
    try {
      Address address = (Address)create_Address();
      address.setPostalCode(postalCode);
      address.setStreetAddress(streetAddress);
      address.setCity(city);
      address.setState(state);
      address.setCountry(country);
      result.add(address);
    }
    catch (TooLongException tlExc) {
      assert true : "if it fails, it is not a case"; //$NON-NLS-1$
    }
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  protected void validateTypeInvariants(final Value subject) {
    super.validateTypeInvariants(subject);
    Address address = (Address)subject;
    validate(address.getStreetAddress() != null);
    validate(address.getStreetAddress().length()
                 <= Address.STREET_ADDRESS_MAX_LENGTH);
    validate(address.getPostalCode() != null);
    validate(address.getPostalCode().length()
                 <= Address.POSTAL_CODE_MAX_LENGTH);
    validate(address.getCity() != null);
    validate(address.getCity().length() <= Address.CITY_MAX_LENGTH);
    validate(address.getState() != null);
    validate(address.getState().length() <= Address.STATE_MAX_LENGTH);
  }

  /*</section>*/


  private static final String EMPTY = ""; //$NON-NLS-1$

  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  protected final void test_Address__() {
    try {
      Address address = new Address();
      validate(address.getPostalCode().equals(EMPTY));
      validate(address.getStreetAddress().equals(EMPTY));
      validate(address.getCity().equals(EMPTY));
      validate(address.getState().equals(EMPTY));
      validate(address.getCountry() == null);
      validateTypeInvariants(address);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/


  /*<section name="instance methods">*/
  //-------------------------------------------------------------------

  protected final void test_setStreetAddress_String_() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      Address subject = (Address)iterSubject.next();
      Iterator iterString = new _Test_String().getCasesWithNull().iterator();
      while (iterString.hasNext()) {
        String streetAddress = (String)iterString.next();
        test_setStreetAddress_String_(subject, streetAddress);
      }
    }
  }

  protected void test_setStreetAddress_String_(final Address subject,
                                               final String streetAddress) {
    try {
      subject.setStreetAddress(streetAddress);
      validate(streetAddress != null
                   ? subject.getStreetAddress().equals(streetAddress)
                   : subject.getStreetAddress().equals(EMPTY));
      validateTypeInvariants(subject);
    }
    catch (TooLongException csExc) {
      validate(streetAddress.length() > Address.STREET_ADDRESS_MAX_LENGTH);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_setPostalCode_String_() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      Address subject = (Address)iterSubject.next();
      Iterator iterString = new _Test_String().getCasesWithNull().iterator();
      while (iterString.hasNext()) {
        String postalCode = (String)iterString.next();
        test_setPostalCode_String_(subject, postalCode);
      }
    }
  }

  protected void test_setPostalCode_String_(final Address subject,
                                            final String postalCode) {
    try {
      subject.setPostalCode(postalCode);
      validate(postalCode != null
                   ? subject.getPostalCode().equals(postalCode)
                   : subject.getPostalCode().equals(EMPTY));
      validateTypeInvariants(subject);
    }
    catch (TooLongException csExc) {
      validate(postalCode.length() > Address.POSTAL_CODE_MAX_LENGTH);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_setCity_String_() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      Address subject = (Address)iterSubject.next();
      Iterator iterString = new _Test_String().getCasesWithNull().iterator();
      while (iterString.hasNext()) {
        String city = (String)iterString.next();
        test_setCity_String_(subject, city);
      }
    }
  }

  protected void test_setCity_String_(final Address subject,
                                      final String city) {
    try {
      subject.setCity(city);
      validate(city != null
                   ? subject.getCity().equals(city)
                   : subject.getCity().equals(EMPTY));
      validateTypeInvariants(subject);
    }
    catch (TooLongException csExc) {
      validate(city.length() > Address.CITY_MAX_LENGTH);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_setState_String_() {
    Iterator iterSubject = getCases().iterator();
    while (iterSubject.hasNext()) {
      Address subject = (Address)iterSubject.next();
      Iterator iterString = new _Test_String().getCasesWithNull().iterator();
      while (iterString.hasNext()) {
        String state = (String)iterString.next();
        test_setState_String_(subject, state);
      }
    }
  }

  protected void test_setState_String_(final Address subject,
                                      final String state) {
    try {
      subject.setState(state);
      validate(state != null
                   ? subject.getState().equals(state)
                   : subject.getState().equals(EMPTY));
      validateTypeInvariants(subject);
    }
    catch (TooLongException csExc) {
      validate(state.length() > Address.STATE_MAX_LENGTH);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_setCountry_Country_() {
    Iterator iterSubject = getSomeCases().iterator();
    while (iterSubject.hasNext()) {
      Address subject = (Address)iterSubject.next();
      Iterator iterCountry
          = new _Test_Country().getCasesWithNull().iterator();
      while (iterCountry.hasNext()) {
        Country country = (Country)iterCountry.next();
        test_setCountry_Country_(subject, country);
      }
    }
  }

  protected void test_setCountry_Country_(final Address subject,
                                          final Country country) {
    try {
      subject.setCountry(country);
      validate(country != null ? subject.getCountry().equals(country)
                               : subject.getCountry() == null);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/

}
