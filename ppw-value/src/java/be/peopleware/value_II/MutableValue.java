package be.peopleware.value_II;


/**
 * <p>
 * This class defines general modi operandi for mutable value types.
 * When a mutable value type is used for a bean property, the following
 * idiom is often used:
 * </p>
 * <pre>
 *     /*&lt;property name=&quot;<var>propertyName</var>&quot;&gt;&#x2A;/
 *     //------------------------------------------------------------------
 *
 *     /&#x2A;*
 *      * &#x40;basic
 *      &#x2A;/
 *     public <var>MutableValueType</var> get<var>PropertyName</var>() {
 *       return (<var>MutableValueType</var>)$<var>propertyName</var>.clone();
 *     }
 *
 *     /&#x2A;*
 *      * &#x40;param <var>propertyName</var>
 *      *        The new value for <var>propertyName</var>
 *      * &#x40;post  (<var>propertyName</var> == null)
 *      *          ? new.get<var>PropertyName</var>() == null
 *      *          : <var>propertyName</var>.equals(new.get<var>PropertyName</var>());
 *      &#x2A;/
 *     public void set<var>PropertyName</var>(final <var>MutableValueType</var> <var>propertyName</var>) {
 *       $<var>propertyName</var> = (<var>MutableValueType</var>)<var>propertyName</var>.clone()
 *     }
 *
 *     private <var>MutableValueType</var> $<var>propertyName</var>;
 *
 *     /&#x2A;&lt;/property&gt;&#x2A;/
 * </pre>
 * <p>
 * Potentially with the addition of validation and type invariants.
 * </p>
 * <p>
 * Note the use of {@link #clone()} to emulate value semantics.
 * For this reason, mutable value types should implement {@link Cloneable}.
 * Since the properties of the value are mutable, it is enough to have a
 * default constructor that sets default values internally. Since we
 * demand of subtypes that they implement {@link java.io.Serializable},
 * a default constructor is mandatory.
 * </p>
 * <p>
 * Like with any value type, {@link #equals(Object)} and {@link #hashCode()}
 * should be overridden appropriately.
 * </p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class MutableValue extends Value implements Cloneable {

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
   * Mutable value types should have a public default constructor.
   */
  public MutableValue() {
    // NOP
  }

  /**
   * This method should be overwritten with deep copy statements
   * if needed locally. The following idiom can be used:
   * <pre>
   *     public Object clone() {
   *       <var>MutableValueType</var> clone = super.clone();
   *       clone.$<var>propertyName1</var> = (<var>PropertyType</var>)$<var>propertyName1</var>.clone();
   *       &hellip;
   *       clone.$<var>propertyNamen</var> = (<var>PropertyType</var>)$<var>propertyNamen</var>.clone();
   *       return clone;
   *     }
   * </pre>
   *
   * @result    result != this;
   * @result    result.equals(this);
   */
  public Object clone() {
    Object result = null;
    try {
      result = super.clone();
    }
    catch (CloneNotSupportedException cnsExc) {
      assert false : "impossible, since we do implement Cloneable"; //$NON-NLS-1$
    }
    return result;
  }

  /*</construction>*/

}