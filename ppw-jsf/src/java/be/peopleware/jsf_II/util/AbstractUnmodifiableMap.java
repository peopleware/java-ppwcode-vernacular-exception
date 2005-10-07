/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.util;


import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Common code for maps that are unmodifiable. Methods that modify the map throw an
 * {@link java.lang.UnsupportedOperationException}. Some other methods have
 * a default implementation, but can be overridden by subclasses.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public abstract class AbstractUnmodifiableMap implements Map, Serializable {

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



  /*<section name="keys">*/
  //------------------------------------------------------------------

  /**
   * @return keySet().size();
   */
  public int size() {
    return keySet().size();
  }

  /**
   * @return size() == 0;
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * @return keySet().contains(key);
   */
  public boolean containsKey(final Object key) {
    return keySet().contains(key);
  }

  /*</section>*/



  /*<section name="values">*/
  //------------------------------------------------------------------

  /**
   * Default implementation, based on {@link #keySet()}
   * and {@link #get(Object)}.
   */
  public Collection values() {
    Set result = new HashSet();
    Iterator iter = keySet().iterator();
    while (iter.hasNext()) {
      Object key = iter.next();
      result.add(get(key));
    }
    return Collections.unmodifiableCollection(result);
  }

  /**
   * @return values().contains(value);
   */
  public boolean containsValue(final Object value) {
    return values().contains(value);
  }

  /*</property>*/



  /*<property name="entrySet">*/
  //------------------------------------------------------------------

  /**
   * Default implementation, based on {@link #keySet()}
   * and {@link #get(Object)}.
   */
  public Set entrySet() {
    Set result = new HashSet();
    Iterator iter = keySet().iterator();
    while (iter.hasNext()) {
      Object key = iter.next();
      Map.Entry entry = new DefaultSetEntry(key);
      result.add(entry);
    }
    return Collections.unmodifiableSet(result);
  }

  /**
   * A class of map entries. The method {@link #setValue(Object)}
   * is not supported.
   */
  protected abstract class EntrySetEntry implements Map.Entry, Comparable, Serializable {

    /**
     * Replaces the value corresponding to this entry with the specified
     * value; not supported.
     *
     * @post    false;
     * @throws  UnsupportedOperationException
     *          true;
     */
    public final Object setValue(final Object value) throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

    /**
     * Returns -1 when the given object is not effective; compares the keys
     * of the objects otherwise.
     *
     * @return  o == null
     *            ? result == -1
     *            : result == getKey().compareTo(((EntrySetEntry)o).getKey())
     */
    public final int compareTo(final Object o) {
      if (o == null) {
        return -1;
      }
      else {
        return ((String)getKey()).compareTo(((EntrySetEntry)o).getKey());
      }
    }

  }

  /**
   * A class of map entries containing a key. The method {@link #getValue()}
   * returns the value in the surrounding map corresponding to that key.
   */
  protected class DefaultSetEntry extends EntrySetEntry {

    /**
     * Create a new {@link DefaultSetEntry} with the given key.
     *
     * @param key
     * @post  new.getKey() == key;
     */
    public DefaultSetEntry(final Object key) {
      $key = key;
    }

    private Object $key;

    /**
     * Returns the key.
     *
     * @basic
     */
    public final Object getKey() {
      return $key;
    }

    /**
     * Returns the value corresponding to the key of this entry.
     *
     * @return  get(getKey());
     */
    public Object getValue() {
      return get(getKey());
    }

  }

  /*</property>*/


  /**
   * Associates the specified value with the specified key in this map; not supported.
   *
   * @post    false;
   * @throws  UnsupportedOperationException
   *          true;
   */
  public final Object put(final Object key, final Object value)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Removes the mapping for this key from this map if it is present; not supported.
   *
   * @post    false;
   * @throws  UnsupportedOperationException
   *          true;
   */
  public final Object remove(final Object key) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Copies all of the mappings from the specified map to this map; not supported.
   *
   * @post    false;
   * @throws  UnsupportedOperationException
   *          true;
   */
  public final void putAll(final Map t) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Removes all mappings from this map; not supported.
   *
   * @post    false;
   * @throws  UnsupportedOperationException
   *          true;
   */
  public final void clear() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

}
