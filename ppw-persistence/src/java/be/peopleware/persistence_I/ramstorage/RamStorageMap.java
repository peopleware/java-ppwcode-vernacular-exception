/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_I.ramstorage;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.peopleware.persistence_I.PersistentBean;


/**
 * How the {@link RamStorage} stores its objects of type
 * {@link #getType()}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 * @toryt-cC toryt.contract.Collections;
 * @invar getType() != null;
 * @invar (forall long l; find(l) != null; getType().isInstance(find(l)));
 */
public final class RamStorageMap {

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
   * @pre type != null;
   * @post getType() == type;
   * @post getAll().isEmpty();
   */
  RamStorageMap(final Class type) {
    assert type != null : "Type cannot be null";
    $type = type;
  }

  /*<property name="type">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public Class getType() {
    return $type;
  }

  /**
   * @invar $type != null;
   */
  private Class $type;

  /*</property>*/

  /**
   * All stored {@link PersistentBean PersistentBeans}.
   *
   * @result (forall long l; ; result.contains(find(l)));
   * @result (forall PersistentBean pb; result.contains(pb);
   *              (exists long l; ; find(l) == pb));
   * @result cC:noNull(result);
   * @resutl cC:instanceOf(result, getType());
   */
  public synchronized Set all() {
    return new HashSet($pbMap.values());
  }

  /**
   * Return the object in the {@link #all()} set
   * with id <code>id</code>. We return <code>null</code> if no such object
   * is found.
   *
   * @pre id != null;
   * @basic
   */
  public synchronized PersistentBean find(final Long id) {
    assert id != null;
    return (PersistentBean)$pbMap.get(id);
  }

  /**
   * Return the object in the {@link #all()} set
   * with id <code>id</code>. We return <code>null</code> if no such object
   * is found.
   *
   * @return find(new Long(id));
   */
  public synchronized PersistentBean find(final long id) {
    return find(new Long(id));
  }

  /**
   * @pre pb'getId() == null;
   * @pre getType().isInstance(pb);
   * @post pb.getId() != null; the new key is unique
   * @post find(pb.getId()) == pb;
   */
  public synchronized void add(final PersistentBean pb) {
    assert pb.getId() == null;
    assert getType().isInstance(pb);
    Long key = newKey();
    pb.setId(key);
    $pbMap.put(key, pb);
  }

  /**
   * @pre pb'getId() != null;
   * @post find(pb'getId()) == null;
   */
  public synchronized void remove(final PersistentBean pb) {
    assert pb.getId() != null;
    $pbMap.remove(pb.getId());
  }

  /**
   * @invar $pbMap != null;
   * @invar cC:noNull($pbMap);
   * @invar cC:instanceOf($pbMap, Long.class, getType());
   */
  private Map $pbMap = new HashMap();

  /*<property name="new key">*/
  //------------------------------------------------------------------

  /**
   * @init newKey().longValue() == 0;
   * @return result.equals(new Long('newKey().longValue() + 1));
   */
  public synchronized Long newKey() {
    Long result = $nextKey;
    $nextKey = new Long($nextKey.longValue() + 1);
    return result;
  }

  /**
   * @invar $nextKey != null;
   */
  private Long $nextKey =  new Long(0);

  /*</property>*/
}
