/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


/**
 * <p>
 * This class defines general modi operandi for immutable value types.
 * When a immutable value type is used for a bean property, the following
 * idiom is often used:
 * </p>
 * <pre>
 *     /&#x2A;*
 *      * &#x40;param <var>propertyName</var>
 *      *          <var>property description</var>
 *      * &#x40;post (<var>propertyName</var> == null)
 *      *         ? new.get<var>PropertyName</var>() == null
 *      *         : new.get<var>PropertyName</var>().equals(<var>propertyName</var>);
 *      &#x2A;/
 *     public <var>BeanType</var>(&hellip , <var>ImmutableValueType</var> <var>propertyName</var>, &hellip;) {
 *       &hellip;
 *       $<var>propertyName</var> = <var>propertyName</var>;
 *       &hellip;
 *     }
 *
 *     &hellip;
 *
 *     /*&lt;property name=&quot;<var>propertyName</var>&quot;&gt;&#x2A;/
 *     //------------------------------------------------------------------
 *
 *     /&#x2A;*
 *      * &#x40;basic
 *      &#x2A;/
 *     public <var>ImmutableValueType</var> get<var>PropertyName</var>() {
 *       return $<var>propertyName</var>;
 *     }
 *
 *     private <var>ImmutableValueType</var> $<var>propertyName</var>;
 *
 *     /&#x2A;&lt;/property&gt;&#x2A;/
 * </pre>
 * <p>
 * Potentially with the addition of validation and type invariants.
 * Note that values are not cloned or copied when they are set
 * or returned.
 * </p>
 * <p>
 * The actual value of an immutable value can only be set in the
 * constructor. A default constructor is necessary for serializability.
 * It should create a default value.
 * Since there is no need to make copies of immutable values, there
 * is no need for cloning functionality. A copy constructor can be
 * handy.
 * The {@link #equals(Object)} and {@link #hashCode()} method should
 * be overridden.
 * Actual immutable value types should be declared final. This is
 * the only way immutability can be enforced. As a result, if there
 * is a copy constructor, the cloning functionality will also not
 * be missed.
 * </p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @invar     !Cloneable.class.isAssignableFrom(getClass());
 */
public abstract class ImmutableValue extends Value {

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
   * Value types should have a public default constructor.
   */
  public ImmutableValue() {
    // NOP
  }

  /*</construction>*/

}
