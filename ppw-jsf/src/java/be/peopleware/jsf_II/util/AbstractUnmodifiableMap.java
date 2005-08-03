/*<license>
Copyright 2004-2005, PeopleWare n.v.
NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.util;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Common code for maps that are unmodifiable. Methods that modify the map throw an
 * {@link java.lang.UnsupportedOperationException}. Some other methods have
 * a default implementation, but can be overridden by subclasses.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public abstract class AbstractUnmodifiableMap implements Map {

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
  public boolean containsKey(Object key) {
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
  public boolean containsValue(Object value) {
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

  protected abstract class EntrySetEntry implements Map.Entry, Comparable {

    public final Object setValue(Object value) throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

    public final int compareTo(Object o) {
      if (o == null) {
        return -1;
      }
      else {
        return ((String)getKey()).compareTo(((EntrySetEntry)o).getKey());
      }
    }

  }

  protected class DefaultSetEntry extends EntrySetEntry {

    public DefaultSetEntry(Object key) {
      $key = key;
    }

    private Object $key;

    public final Object getKey() {
      return $key;
    }

    public Object getValue() {
      return get($key);
    }

  }

  /*</property>*/



  public final Object put(Object key, Object value) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public final Object remove(Object key) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public final void putAll(Map t) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public final void clear() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

}
