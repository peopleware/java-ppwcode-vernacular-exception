package be.peopleware.value_I;


import java.io.Serializable;


/**
 * A class representing a address.
 *
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 *
 * @invar     getStreetAddress() != null;
 * @invar     getStreetAddress().length() <= STREET_ADDRESS_MAX_LENGTH;
 * @invar     getPostalCode() != null;
 * @invar     getPostalCode().length() <= POSTAL_CODE_MAX_LENGTH;
 * @invar     getCity() != null;
 * @invar     getCity().length() <= CITY_MAX_LENGTH;
 * @invar     getState() != null;
 * @invar     getState().length() <= STATE_MAX_LENGTH;
 */
public class Address extends MutableValue implements Serializable {

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



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Value types should have a public default constructor.
   *
   * @post      getPostalCode().equals("");
   * @post      getStreetAddress().equals("");
   * @post      getCity().equals("");
   * @post      getState().equals("");
   * @post      getCountry() == null;
   */
  public Address() {
    // NOP
  }

  /*</construction>*/



  /*<property name="postalCode">*/
  //------------------------------------------------------------------

  /**
   * <p>The maximum length of the postal code property String.</p>
   * <strong>= {@value}</strong>
   */
  public static final int POSTAL_CODE_MAX_LENGTH = 20;

  /**
   * @basic
   */
  public final String getPostalCode() {
    return $postalCode.getString();
  }

  /**
   * @param     postalCode
   *            The postal code to set for this Address.
   * @throws    TooLongException
   *            postalCode.length > POSTAL_CODE_MAX_LENGTH;
   * @post      (postalCode == null) ? new.getPostalCode().equals("")
   *                                 : new.getPostalCode().equals(postalCode);
   */
  public final void setPostalCode(final String postalCode)
      throws TooLongException {
    $postalCode.setString(postalCode);
  }

  private ConstrainedString $postalCode
      = new ConstrainedString(this,
                              "postalCode", //$NON-NLS-1$
                              POSTAL_CODE_MAX_LENGTH);

  /*</property>*/



  /*<property name="streetAddress">*/
  //------------------------------------------------------------------

  /**
   * <p>The maximum length of the street address property String.</p>
   * <strong>= {@value}</strong>
   */
  public static final int STREET_ADDRESS_MAX_LENGTH = 255;

  /**
   * @basic
   */
  public final String getStreetAddress() {
    return $streetAddress.getString();
  }

  /**
   * @param     streetAddress
   *            The street address to set for this Address.
   * @throws    TooLongException
   *            streetAddress.length > STREET_ADDRESS_MAX_LENGTH;
   * @post      (streetAddress == null)
   *                ? new.getStreetAddress().equals("")
   *                : new.getStreetAddress().equals(streetAddress);
   */
  public final void setStreetAddress(final String streetAddress)
      throws TooLongException {
    $streetAddress.setString(streetAddress);
  }

  private ConstrainedString $streetAddress
      = new ConstrainedString(this,
                              "streetAddress", //$NON-NLS-1$
                              STREET_ADDRESS_MAX_LENGTH);

  /*</property>*/



  /*<property name="city">*/
  //------------------------------------------------------------------

  /**
   * <p>The maximum length of the city property String.</p>
   * <strong>= {@value}</strong>
   */
  public static final int CITY_MAX_LENGTH = 255;

  /**
   * @basic
   */
  public final String getCity() {
    return $city.getString();
  }

  /**
   * @param     city
   *            The city to set for this Address.
   * @throws    TooLongException
   *            city.length > CITY_MAX_LENGTH;
   * @post      (city == null) ? new.getCity().equals("")
   *                           : new.getCity().equals(city);
   */
  public final void setCity(final String city) throws TooLongException {
    $city.setString(city);
  }

  private ConstrainedString $city
    = new ConstrainedString(this,
                            "city", //$NON-NLS-1$
                            CITY_MAX_LENGTH);

  /*</property>*/



  /*<property name="state">*/
  //------------------------------------------------------------------

  /**
   * <p>The maximum length of the state property String.</p>
   * <strong>= {@value}</strong>
   */
  public static final int STATE_MAX_LENGTH = 255;

  /**
   * @basic
   */
  public final String getState() {
    return $state.getString();
  }

  /**
   * @param     state
   *            The state to set for this Address.
   * @throws    TooLongException
   *            state.length > CITY_MAX_LENGTH;
   * @post      (state == null) ? new.getState().equals("")
   *                            : new.getState().equals(state);
   */
  public final void setState(final String state) throws TooLongException {
    $state.setString(state);
  }

  private ConstrainedString $state
    = new ConstrainedString(this,
                            "state", //$NON-NLS-1$
                            STATE_MAX_LENGTH);

  /*</property>*/



  /*<property name="country">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Country getCountry() {
    return $country;
  }

  /**
   * @param     country
   *            The country to set for this Address.
   * @post      (country == null) ? new.getCountry() == null
   *                              : new.getCountry().getValue().equals(
   *                                    country.getValue());
   */
  public final void setCountry(final Country country) {
    $country = country;
  }

  private Country $country;

  /*</property>*/
  
  
  public final boolean isEmpty() {
    return (((getStreetAddress() == null) || getStreetAddress().equals(EMPTY))
            && ((getPostalCode() == null) || getPostalCode().equals(EMPTY))
            && ((getCity() == null) || getCity().equals(EMPTY))
            && ((getState() == null) || getState().equals(EMPTY))
            && ((getCountry() == null) || getCountry().toString().equals(EMPTY)
                || (getCountry().toString() == " ")));  //$NON-NLS-1$
  }
  
  public final int hashCode() {
    return $streetAddress.hashCode()
           + $postalCode.hashCode()
           + $city.hashCode()
           + $state.hashCode()
           + (($country != null) ? $country.hashCode() : 0);
  }

  /**
   * @see       java.lang.Object#equals(Object)
   */
  public final boolean equals(final Object object) {
    boolean result = false;
    if (object instanceof Address) {
      Address comparer = (Address)object;
      result = (comparer.getStreetAddress().equals(getStreetAddress()))
               && (comparer.getPostalCode().equals(getPostalCode()))
               && (comparer.getCity().equals(getCity()))
               && (comparer.getState().equals(getState()))
               && ((comparer.getCountry() != null)
                     ? (comparer.getCountry().equals(getCountry()))
                     : (getCountry() == null));
    }
    return result;
  }

  public String toString() {
    String countryToString = "";  //$NON-NLS-1$
    if ($country != null) {
      countryToString = $country.toString();
    }
    return getClass().getName()
           + "@" + hashCode() //$NON-NLS-1$
           + "[" //$NON-NLS-1$
             + "street address: " + $streetAddress //$NON-NLS-1$
             + ", postal code: " + $postalCode //$NON-NLS-1$
             + ", city: " + $city //$NON-NLS-1$
             + ", state: " + $state //$NON-NLS-1$
             + ", country: " + countryToString //$NON-NLS-1$
           + "]"; //$NON-NLS-1$
  }
  
  /**
   * @see       java.lang.Object#clone()
   */
  public final Object clone() {
    Object result = super.clone();
    try {
      ((Address)result).setStreetAddress(getStreetAddress());
      ((Address)result).setPostalCode(getPostalCode());
      ((Address)result).setCity(getCity());
      ((Address)result).setState(getState());
      ((Address)result).setCountry(getCountry());
    }
    catch (TooLongException tlExc) {
      assert false : "TooLongException on clone cannot happen."; //$NON-NLS-1$
    }
    return result;
  }

}
