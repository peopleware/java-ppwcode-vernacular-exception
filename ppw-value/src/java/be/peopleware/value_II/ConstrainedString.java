package be.peopleware.value_II;


import java.io.Serializable;

import be.peopleware.bean_V.Delegate;


/**
 * A Class to represent VARCHAR() objects with constrained checking.
 *
 * @author    Jan Dockx
 * @author    Dimitri Smits
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 *
 * @invar     getMaxLength() >= 0
 * @invar     getConstrainedString().length() <= getMaxLength()
 * @invar     getConstrainedString() <> null
 */
public class ConstrainedString extends Delegate implements Serializable {

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



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     delegatingBean
   *            The bean that delegates to this.
   * @param     propertyName
   *            The name of the property for which <code>delegatingBean</code>
   *            uses this.
   * @param     maxLength
   *            The maximum length of the string
   * @pre       delegatingBean != null;
   * @pre       (propertyName != null) && ! propertyName.equals("");
   * @pre       maxLength > 0;
   * @post      new.getDelegatingBean() == delegatingBean;
   * @post      new.getpropertyName().equals(propertyName);
   * @post      new.getString() = "";
   * @post      new.getMaxLength() = maxLength;
   */
  public ConstrainedString(final Object delegatingBean,
                           final String propertyName,
                           final int maxLength) {
    super(delegatingBean, propertyName);
    assert maxLength > 0 : "maxLength > 0";  //$NON-NLS-1$
    $maxLength = maxLength;
  }

  /**
   * @param     delegatingBean
   *            The bean that delegates to this.
   * @param     propertyName
   *            The name of the property for which <code>delegatingBean</code>
   *            uses this.
   * @param     maxLength
   *            The maximum length of the string
   * @param     stringValue
   *            The initial string for this object.
   * @pre       delegatingBean != null;
   * @pre       (propertyName != null) && ! propertyName.equals("");
   * @pre       maxLength > 0;
   * @pre       stringValue != null;
   * @pre       stringValue.length() <= maxLength;
   * @post      new.getDelegatingBean() == delegatingBean;
   * @post      new.getpropertyName().equals(propertyName);
   * @post      new.getString().equals(stringValue);
   * @post      new.getMaxLength() = maxLength;
   */
  public ConstrainedString(final Object delegatingBean,
                           final String propertyName,
                           final int maxLength,
                           final String stringValue) {
    super(delegatingBean, propertyName);
    assert maxLength > 0 : "maxLength > 0";  //$NON-NLS-1$
    assert stringValue != null;
    assert stringValue.length() <= maxLength;
    $maxLength = maxLength;
    $string = stringValue;
  }

  /*</construction>*/



  /*<property name="string">*/
  //------------------------------------------------------------------

  /**
   * @param     stringValue
   *            the new value
   * @post      stringValue == null ? new.getString().equals("")
   *                                : new.getString().equals(stringValue);
   * @throws    TooLongException
   *            stringValue.length > getMaxLength();
   */
  public final void setString(final String stringValue)
      throws TooLongException {
    String result = ""; //$NON-NLS-1$
    if (stringValue != null) {
      result = stringValue;
    }
    if (result.length() > getMaxLength()) {
      throw new TooLongException(getDelegatingBean(),
                                 getPropertyName(),
                                 stringValue,
                                 getMaxLength(),
                                 "TOO_LONG", //$NON-NLS-1$
                                 null);
    }
    $string = result;
  }

  /**
   * @basic
   */
  public final String getString() {
    return $string;
  }

  /**
   * @invar     $string != null;
   * @invar     $string.length < getMaxLength();
   */
  private String $string = ""; //$NON-NLS-1$

  /*</property>*/



  /*<property name="maxLength">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final int getMaxLength() {
    return $maxLength;
  }

  /**
   * @invar     $maxLength >= 0;
   */
  private int $maxLength;

  /*</property>*/



  /**
   * @param     other
   *            Object to compare with
   * @return    (other != null) && (other instanceof ConstrainedString)
   *            && (getString().equals(((ConstrainedString)other).getString()))
   *            && (getMaxLength()
   *                == ((ConstrainedString)other).getMaxLength());
   * @post      result ? hashCode() == other.hashCode() : true;
   */
  public boolean hasSameValue(final Object other) {
    return super.hasSameValues(other)
           && (getString().equals(((ConstrainedString)other).getString()))
           && (getMaxLength() == ((ConstrainedString)other).getMaxLength());
  }

  public String toString() {
    return getString();
  }

}
