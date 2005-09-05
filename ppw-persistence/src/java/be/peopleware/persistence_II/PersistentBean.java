package be.peopleware.persistence_II;


import java.io.Serializable;
import be.peopleware.bean_V.RousseauBean;


/**
 * <p>Persistent classes need a simple primary key. Persistent objects
 *   always represent real-world objects, and therefor should be
 *   implemented as {@link RousseauBean}s. This class
 *   enforces the correct behavior and provides supporting code.</p>
 * <p>Users should be aware that this means that there can be more
 *   than 1 object that represents the same instance in the persistent storage.
 *   To check whether 2 persistent objects represent the same persistent
 *   instance, use {@link #hasSameId(PersistentBean)}. To check whether 2
 *   persistent objects have the same values for all of their properties,
 *   independent of their id, use {@link #hasSameValues(RousseauBean)}.</p>
 * <p>Persistent beans are not {@link Cloneable} however. Implementing
 *   clone for a semantic inheritance tree is a large investment, and
 *   should not be enforced. Furthermore, it still is a bad idea to make
 *   any semantic object {@link Cloneable}. From experience we know that
 *   it is very difficult to decide in general how deep a clone should go.
 *   Persistent beans are {@link java.io.Serializable} though, because
 *   they are often used also as Data Transfer Objects in multi-tier
 *   applications.</p>
 * <p>To make clear that more than 1 persistent bean instance representing
 *   the same persistent instance can exist, this class implements
 *   {@link Cloneable}: it makes no sense to make it impossible to
 *   have more than 1 instance with the same id.</p>
 * <p>Subclasses should take care to override the following methods diligently:
 *   <ul>
 *     <li>{@link #hasSameValues(RousseauBean)}, to take into account
 *         the values of properties added in the subclass;</li>
 *     <li>{@link #getWildExceptions()}, to add validation concerning
 *         properties and type invariants added in the subclass.</li>
 *   </ul>
 * </p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public interface PersistentBean extends RousseauBean, Serializable {

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
   * @param     id
   *            The new value
   * @post      (id == null) ? new.getId() == null : new.getId().equals(id);
   *
   * @idea (jand) This should only be changed by Hibernate. So public is too broad.
   *              Can something be done about this?
   * @note (dvankeer): Make hibernate set this field directly instead of using
   *                   the get- & setter. <id .... access="field" ... />
   * @note (dvankeer): If we remove this method we break a lot in the
   *                   application. Especially tests
   */
  void setId(final Long id);

  /**
   * @basic
   */
  Long getId();

  /*</property>*/


  // IDEA (jand) move the String stuff to a ppw-util, or at least ppw-bean
  // IDEA (jand) automatic implementation of hasSameValues there too

  /**
   * Short representation of the bean.
   *
   * @return getClass().getName() + "@" + hashCode()
   *           + "[id: " + $id + "]";
   */
  String toString();

  /**
   * Long representation of this bean.
   */
  String toStringLong();

  /**
   * Append a long representation of this to <code>acc</code>.
   */
  void appendLongRepresentation(StringBuffer acc);

  /**
   * This instance has the same id as the instance <code>other</code>.
   *
   * @param     other
   *            The persisten object to compare to.
   * @return    (other != null)
   *                && ((getId() == null)
   *                    ? other.getId() == null
   *                    : getId().equals(other.getId())));
   */
  boolean hasSameId(final PersistentBean other);

}
