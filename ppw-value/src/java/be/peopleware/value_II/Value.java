/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;

import java.io.Serializable;


/**
 * <p>
 * A super type for all classes that emulate <em>algebras</em>, i.e.,
 * value types.
 * </p>
 * <p>
 * Value types should override {@link #equals(Object)} and
 * {@link #hashCode()}, so that different objects that represent
 * the same value are considered equal. Value types should override
 * {@link #toString()} to return a textual representation of
 * the value they represent, that does not mention object identity.
 * </p>
 * <p>
 * Value types should be serializable, and thus always should
 * have a public default constructor.
 * </p>
 * <p>
 * Value types should come with a {@link java.beans.PropertyEditor}
 * that supports at least a textual interface.
 * </p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar     (forall MutableValue mv1, mv2; ;
 *              mv1.equals(mv2) ==> mv1.hashCode() == mv2.hashCode());
 *
 * @todo (jand): Check serializability for entire hierarchy
 */
public abstract class Value implements Serializable {

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

  /**
   * The empty string.
   *
   * <strong>= &quot;&quot;</strong>
   */
  public static final String EMPTY = ""; //$NON-NLS-1$

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Value types should have a public default constructor.
   */
  public Value() {
    // NOP
  }

  /*</construction>*/



  /**
   * This method should be overwritten with extra conditions that
   * actually do compare internal values. The following idiom
   * can be used:
   * <pre>
   *     /&#x2A;*
   *      * &#x40;result <var>local conditions</var>;
   *      &#x2A;/
   *     public boolean equals(Object other) {
   *       return super.equals(other)
   *              &amp;&amp; <var>local conditions</var>;
   *     }
   * </pre>
   * Remember that the {@link #hashCode()} must be consistent with
   * <code>equals</code>.
   *
   * @result    result ==> (other != null) && (other.getClass() == getClass());
   */
  public boolean equals(final Object other) {
    return (other != null) && (other.getClass() == getClass());
  }

  /**
   * This method is made abstract to enforce subtypes to override
   * this method.
   */
  public abstract int hashCode();

  /**
   * This method is made abstract to enforce subtypes to override
   * this method.
   */
  public abstract String toString();

}
