/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.bean_V;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A partial implementation of the interface {@link RousseauBean}.
 *
 * @author    nsmeets
 * @author    PeopleWare n.v.
 */
public abstract class AbstractRousseauBean implements RousseauBean {

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


  private static final Log LOG = LogFactory.getLog(AbstractRousseauBean.class);

  /**
   * @see  RousseauBean
   *
   * @protected
   * <p><em>The implementation in this class returns an emtpy
   *   CompoundPropertyException
   *   (<code>result.getElementExceptions().isEmpty();</code>).</em>
   *   Subclasses should override this method.</p>
   */
  public CompoundPropertyException getWildExceptions() {
    return new CompoundPropertyException(this, null, null, null);
  }

  /**
   * @see  RousseauBean
   */
  public final boolean isCivilized() {
    return getWildExceptions().isEmpty();
  }

  /**
   * This method does nothing, but will throw the wild exceptions
   * if this bean is not civilized.
   *
   * @see  RousseauBean
   */
  public final void checkCivility() throws CompoundPropertyException {
    CompoundPropertyException cpe = getWildExceptions();
    if (!cpe.isEmpty()) {
      if (LOG.isDebugEnabled()) { // this if does only loggin
        LOG.debug("the wild exceptions were not empty; we will throw the " +
            "CompoundPropertyException"); //$NON-NLS-1$
        LOG.debug("current RousseauBean is " + toString()); //$NON-NLS-1$
        LOG.debug(cpe.getElementExceptions());
        LOG.debug("ElementExceptions details:"); //$NON-NLS-1$
        Iterator iter =  cpe.getElementExceptions().entrySet().iterator();
        while (iter.hasNext()) {
          Map.Entry entry = (Map.Entry)iter.next();
          if (LOG.isDebugEnabled()) {
            Object key = entry.getKey();
            if (key == null) {
              LOG.debug("null as key:");
            }
            else {
              LOG.debug(key + ":");
            }
          }
          Set propertyExceptions = (Set)entry.getValue();
          Iterator exceptions = propertyExceptions.iterator();
          while (exceptions.hasNext()) {
            PropertyException pexc = (PropertyException)exceptions.next();
            LOG.debug(pexc, pexc);
          }
        }
      }
      cpe.close();
      throw cpe;
    }
  }

  /**
   * @see  RousseauBean
   *
   * @protected
   *  The implementation of this method in this class does NOP,
   *  as a convenience to most subtypes.
   */
  public void normalize() {
    // NOP
  }

  /**
   * This instance has the same values as the instance <code>rb</code>.
   * This comparison does not take into account to-many associations,
   * to avoid infinite loops. Comparison is independent of normalization,
   * i.e., it should return the same result when the participants in the
   * comparison are normalized or not. To accomplish that, this method
   * may call {@link #normalize()} itself.
   *
   * @see  RousseauBean
   */
  public boolean hasSameValues(final RousseauBean rb) {
    return (rb != null) && (getClass() == rb.getClass());
  }

  /**
   * @see  RousseauBean
   */
  public final boolean equals(final Object other) {
    return super.equals(other);
  }

  /**
   * @see  RousseauBean
   */
  public final int hashCode() {
    return super.hashCode();
  }

  /**
   * Convenience method for generating the toString
   * of to-many associations.
   *
   * @pre       c != null;
   */
  protected final String collectionString(final Collection c) {
    assert c != null;
    String result = "{"; //$NON-NLS-1$
    for (Iterator i = c.iterator(); i.hasNext();) {
        Object item = i.next();
        result = result + item.getClass().getName()
                     + "@" + item.hashCode(); //$NON-NLS-1$
        if (i.hasNext()) {
          result = result + ", "; //$NON-NLS-1$
        }
    }
    result = result + "}"; //$NON-NLS-1$
    return result;
  }

  public static boolean hasSameValuesWithNull(RousseauBean one, RousseauBean other) {
    return (one == null)
            ? (other == null)
            : one.hasSameValues(other);
  }

}
