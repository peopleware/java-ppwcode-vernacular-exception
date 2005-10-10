/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import java.util.Map;


/**
 * <p>This class defines general modi operandi for type-safe enumeration
 *  value types. Values of an enumeration type are immutable. Values
 *  of an enumeration type implemented according to this pattern are
 *  not intended to be calculated with. We are interested merely in
 *  their existence. This is not an algebra.</p>
 * <p>There are different possibilities to emulate type-safe enumeration
 *  types.</p>
 * <p>One popular way is to apply an n-ary singleton-like pattern,
 *  where the constructor is kept private, and the acceptable values
 *  are created by the class loader and stored in class variables.
 *  When this pattern is used, it is not necessary to store any value
 *  in instance variables, since the mere object identity suffices to
 *  identify the value. Object equality (<code>==</code>) can be used
 *  to compare values. This approach however fails in applications
 *  with more than 1 class loader.</p>
 * <p>It is easier to allow many different object to represent the same
 *  value. The value is then identified by the contents of an instance
 *  variable. This instance variable is typically an <code>int</code>
 *  or a <code>String</code>. The former is more memory friendly,
 *  but the latter is much easier if a String representation is
 *  necessary anyway.</p>
 * <p>Therefor, this pattern chooses the latter approach. All enumeration
 *  type objects carry with them a String that identifies the value
 *  within its type hierarchy. The String should be the String
 *  that is to be used as programmatic representation by an assorted
 *  property editor. This is not the label for the value that will
 *  be used to present the value to end users, since this should be
 *  i18ned.</p>
 * <p>Most interesting reuse happens in
 *  {@link AbstractEnumerationValueEditor}.
 *  That class depends on the fact that the instances of subtypes of
 *  this type return a discriminating value through
 *  {@link #toString()}, and that there is a properties file with
 *  the same name as the enumeration type that holds i18n labels
 *  for all values of the type. Also, to make implementation of specific
 *  EnumerationValueEditors easy to implement, it is a good idea
 *  for subtypes of this class to implement a class variable
 *  that holds a {@link Map} of tag / instance associations. This
 *  makes it also sensible to define the actual enumeration type
 *  class <code>final</code>. This class contains type invariants
 *  that subclasses must adhere to for a class constant {@link Map}
 *  called <code>VALUES</code>.</p>
 * <p>Below is an idiom for implementing subclasses:</p>
 * <pre>
 * public final class <var>EnumerationValueName</var>
 *     extends EnumerationValue {
 *
 *   /&#x2A;*
 *    * The string that is used internally to identify
 *    * <var>value 1</var>.
 *    *
 *    * <strong>= {&#x40;value}</strong>
 *    &#x2A;/
 *   public static final String <var>VALUE_1</var>_STRING
 *       = &quot;<var>DISCRIMINATING_STRING_1</var>&quot;
 *
 *   &hellip;
 *
 *   /&#x2A;*
 *    * The string that is used internally to identify
 *    * <var>default value</var>.
 *    *
 *    * <strong>= {&#x40;value}</strong>
 *    &#x2A;/
 *   public static final String <var>VALUE_DEFAULT</var>_STRING
 *       = &quot;<var>DISCRIMINATING_STRING_DEFAULT</var>&quot;;
 *
 *   /&#x2A;*
 *    * The instance representing <var>value 1</var>.
 *    &#x2A;/
 *   public static final <var>EnumerationValueName</var> <var>VALUE_1</var>
 *       = new <var>EnumerationValueName</var>(<var>VALUE_1</var>_STRING);
 *
 *    &hellip;
 *
 *   /&#x2A;*
 *    * The instance representing <var>default value</var>.
 *    &#x2A;/
 *   public static final <var>EnumerationValueName</var> <var>VALUE_DEFAULT</var>
 *       = new <var>EnumerationValueName</var>(<var>VALUE_DEFAULT</var>_STRING);
 *
 *   public static final java.util.Map VALUES = generateValues();
 *
 *   private static java.util.Map generateValues() {
 *     java.util.Map result = new java.util.HashMap();
 *     result.put(<var>VALUE_1</var>.toString(), <var>VALUE_1</var>);
 *     result.put(<var>VALUE_DEFAULT</var>.toString(), <var>VALUE_DEFAULT</var>);
 *     return java.util.Collections.unmodifiableMap(result);
 *   }
 *
 *   /&#x2A;*
 *    * &#x40;param     discriminator
 *    *            The string that discriminates this value.
 *    * &#x40;pre       discriminator != null;
 *    * &#x40;pre       discriminator.length > 0;
 *    * &#x40;post      new.toString().equals(discriminator);
 *    &#x2A;/
 *   private <var>EnumerationValueName</var>(final String discriminator) {
 *     super(discriminator);
 *   }
 *
 *   /&#x2A;*
 *    * Enumeration types require a default constructor for
 *    * serializability. It is ill-advised to use this default
 *    * constructor yourself. Use the constants instead.
 *    *
 *    * &#x40;post new.equals(<var>VALUE_DEFAULT</var>);
 *    &#x2A;/
 *   public <var>EnumerationValueName</var>() {
 *     this(<var>VALUE_DEFAULT</var>_STRING);
 *   }
 *
 * }
 * </pre>
 * <p>Since uniqueness of instances is not guaranteed,
 *  <strong>comparison should always be done through
 *  {@link #equals(Object)}</strong>. Users
 *   should however still avoid creating new objects, and use
 *   the class constants, or the entries in the <code>VALUES</code>
 *   map instead.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @invar     VALUES != null;
 * @invar     VALUES.size() > 0;
 * @invar     ! VALUES.keySet().contains(null);
 * @invar     ! VALUES.values().contains(null);
 * @invar     (forall Object o; VALUES.keySet().contains(o);
 *                t instanceof String);
 * @invar     (forall Object o; VALUES.values().contains(o);
 *                o.getClass() == Gender.class);
 * @invar     VALUES.values().contains(this);
 * @invar     this.equals(VALUES.get(toString()));
 */
public abstract class EnumerationValue extends ImmutableValue {

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
   * @param     discriminator
   *            The string that discriminates this value.
   * @pre       discriminator != null;
   * @pre       discriminator.length() > 0;
   * @post      new.toString().equals(discriminator);
   */
  protected EnumerationValue(final String discriminator) {
    assert discriminator != null;
    assert discriminator.length() > 0;
    $discriminator = discriminator;
  }

  /*</construction>*/



  /**
   * @invar     $discriminator != null;
   */
  private String $discriminator;

  /**
   * @basic
   */
  public final String toString() {
    return $discriminator;
  }

  /**
   * @result toString().equals(other.toString());
   */
  public final boolean equals(final Object o) {
    return super.equals(o)
            && ($discriminator.equals(((EnumerationValue)o).$discriminator));
  }

  /**
   * @return toString().hashCode();
   */
  public final int hashCode() {
    return $discriminator.hashCode();
  }

  /**
   * @basic
   */
  public final String getValue() {
    return $discriminator;
  }

}
