package be.peopleware.persistence_II;


import java.beans.PropertyDescriptor;
import java.io.Serializable;

import org.apache.commons.beanutils.PropertyUtils;
import be.peopleware.bean_V.AbstractRousseauBean;


/**
 * A partial implementation of the interface {@link PersistentBean}.
 *
 *
 * @author    nsmeets
 * @author    PeopleWare n.v.
 */
public abstract class AbstractPersistentBean
    extends AbstractRousseauBean
    implements PersistentBean, Serializable {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";

  /*</section>*/



  /*<property name="id">*/
  //------------------------------------------------------------------

  /**
   * @see   PersistentBean
   */
  public final void setId(final Long id) {
    $id = id;
  }

  /**
   * @basic
   */
  public final Long getId() {
    return $id;
  }

  private Long $id;

  /*</property>*/

  // IDEA (jand) move the String stuff to a ppw-util, or at least ppw-bean
  // IDEA (jand) automatic implementation of hasSameValues there too

  /**
   * Short representation of the bean.
   *
   * @see   PersistentBean
   */
  public final String toString() {
    return getClass().getName() + "@" + hashCode()
               + "[id: " + $id + "]";
  }

  /**
   * Long representation of this bean.
   *
   * @see   PersistentBean
   */
  public final String toStringLong() {
    StringBuffer result = new StringBuffer(1024);
    appendLongRepresentation(result);
    return result.toString();
  }

  /**
   * Append a long representation of this to <code>acc</code>.
   *
   * @see   PersistentBean
   */
  public void appendLongRepresentation(StringBuffer acc) {
    acc.append(getClass().getName());
    acc.append("@");
    acc.append(hashCode());
    acc.append("[");
    appendProperties(acc);
    acc.append("]");
  }

  private final static String SEPARATOR = ", ";

  private void appendProperties(StringBuffer acc) {
    PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(this);
    for (int i = 0; i < pds.length; i++) {
      appendPropertyString(pds[i].getName(), acc);
      if (i < pds.length - 1) {
        acc.append(SEPARATOR);
      }
    }
  }

  private void appendPropertyString(String propertyName, StringBuffer acc) {
    acc.append(propertyName);
    acc.append("=");
    try {
      Object value = PropertyUtils.getProperty(this, propertyName);
      acc.append(value);
    }
    catch (Throwable exc) {
      // if anything goes wrong, mention it, but eat it
      acc.append("!!EXCEPTION!! (");
      acc.append(exc);
      acc.append(")");
    }
  }

  /**
   * This instance has the same id as the instance <code>other</code>.
   *
   * @see   PersistentBean
   */
  public final boolean hasSameId(final PersistentBean other) {
    return (other != null)
             && ((getId() == null)
                   ? other.getId() == null
                   : getId().equals(other.getId()));
  }

}
