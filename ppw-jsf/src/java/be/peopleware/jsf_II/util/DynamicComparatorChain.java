/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II.util;


import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;


/**
 * <p>{@link Comparator} for sorting collections of beans interactively, while
 *   remembering previous sorted state. Typically, this is the code behind
 *   a UI where the end user clicks on a column heading to sort a table on
 *   that property primarily. A second click on the same column heading reverses
 *   the order.</p>
 * <p>An instance of this type wraps several comparators that are identified by a
 *   property name. These wrapped comparators do not operate on the objects to be
 *   compared, but on the values of the properties of those
 *   objects with as name the key for which the comparator is added to instances
 *   of this class. The wrapped comparators are kept in a sequence. Comparison
 *   calls each wrapped {@link Comparator} in sequence until either
 * <ol>
 *  <li>any single {@link Comparator} returns a non-zero result
 *   (and that result is then returned), or</li>
 *  <li>the DynamicComparatorChain is exhausted (and zero is returned).</li>
 * </ol>
 * <p>A call to {@link #bringToFront(String)} brings the comparator registered
 *   with the given name to the front of the sequence. A second call with the
 *   same propertyName reverses the order of that comparator.</p>
 * <p>Because {@link org.apache.commons.beanutils.PropertyUtils} is used to
 *   get the values of the properties, the property names can be complex
 *   (i.e., <strong>simple</strong>, <strong>nested</strong>, <strong>indexed</strong>,
 *   <strong>mapped</strong> or <strong>combined</strong>; e.g.,
 *   <code>address.street</code> is a nested name). For information about the format
 *   of the property names, see
 *   <a href="http://jakarta.apache.org/commons/beanutils/api/index.html" target="extern">the
 *   Apache Jakarta Commons beanutil Javadoc</a>.</p>
 * <p>This class also offers a Map interface, so that it can be configured, e.g.,
 *   as a managed map in JSF.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public final class DynamicComparatorChain implements Comparator, Map {

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

  // Default constructor is needed

  /*</construction */


  /**
   * Add the comparator at the front of the chain. If <code>comp</code>
   * is <code>null</code>, we will try to use natural ordering. This will
   * work if the value to which the <code>propertyName</code> evaluates
   * turns out to be {@link Comparable}. If not, a {@link ClassCastException}
   * will be thrown.
   */
  public void addComparator(String propertyName, Comparator comp, boolean ascending) {
    Entry cce = new Entry(propertyName, comp, ascending);
    $chain.add(cce);
  }

  /**
   * Brings the comparator that was added with <code>propertyName</code>
   * to the front of the chain. If it was already at the front of the chain,
   * its comparison order is reversed. If no comparator is registered
   * under that name, nothing happens.
   */
  public void bringToFront(String propertyName) {
    ListIterator iter = $chain.listIterator();
    while (iter.hasNext()) {
      Entry cce = (Entry)iter.next();
      if (cce.getPropertyName().equals(propertyName)) {
        if (iter.previousIndex() == 0) {
          cce.toggleAscending();
        }
        else {
          iter.remove();
          $chain.add(0, cce);
        }
        return;
      }
    }
  }

  public final String[] getSortOrder() {
    String[] result = new String[$chain.size()];
    Iterator iter = $chain.iterator();
    int index = 0;
    while (iter.hasNext()) {
      Entry entry = (Entry)iter.next();
      result[0] = entry.getPropertyName();
      index++;
    }
    return result;
  }

  public final void setSortOrder(String[] sortOrder) {
    for(int i = sortOrder.length - 1; i >= 0; i--) {
      bringToFront(sortOrder[i]);
    }
  }

  /**
   * @invar $chain != null;
   * @invar ! $chain.contains(null);
   * @invar (forall Object o; $chain.contains(o); o instanceof Entry);
   */
  private List $chain = new LinkedList();

  /**
   * If comp == null, the property <code>propertyName</code>
   * of the compared objects must be of a {@link Comparable} type.
   * If the propertyName is <code>null</code>, the objects themselves are compared.
   */
  private class Entry implements Comparator {

    public Entry(String propertyName, Comparator comp, boolean ascending) {
      if (propertyName == null) {
        throw new NullPointerException();
      }
      $propertyName = propertyName;
      $comparator = comp;
      $ascending = ascending;
    }

    public final String getPropertyName() {
      return $propertyName;
    }

    private String $propertyName;


    public final Comparator getComparator() {
      return $comparator;
    }

    public final void setComparator(Comparator comp) {
      $comparator = comp;
    }

    private Comparator $comparator;

    public final boolean isAscending() {
      return $ascending;
    }

    public void toggleAscending() {
      $ascending = ! $ascending;
    }

    private boolean $ascending;

    public int compare(Object o1, Object o2) throws ClassCastException {
      if (o1 == o2) {
        return 0;
      }
      else if (o1 == null) {
        return $ascending ? -1 : 1;
      }
      else if (o2 == null) {
        assert o1 != null;
        return $ascending ? 1 : -1;
      }
      else {
        assert o1 != o2;
        assert o1 != null;
        assert o2 != null;
        if ($ascending) {
          return compareProperty(o1, o2);
        }
        else {
          return compareProperty(o2, o1);
        }
      }
    }

    private Object getNavigatedProperyValueOrNull(Object bean, String propertyName) {
      Object result = null;
      try {
        result = PropertyUtils.getProperty(bean, propertyName);
      }
      catch (NullPointerException e) {
        /* there is a null somewhere in the path of the $propertyName;
         * we will compare as if the property is null */
       return null;
      }
      catch (NoSuchMethodException e) {
        assert false : "NoSuchMethodExceptionshould not happen: " + e;
      }
      catch (IllegalArgumentException e) {
        assert false : "IllegalArgumentException not happen: " + e;
      }
      catch (IllegalAccessException e) {
        assert false : "IllegalAccessExceptionshould not happen: " + e;
      }
      catch (InvocationTargetException e) {
        assert false : "InvocationTargetExceptionshould not happen: " + e;
      }
      return result;
    }

    private int compareProperty(Object o1, Object o2) throws ClassCastException {
      assert o1 != null;
      assert o2 != null;
      Object v1 = null;
      Object v2 = null;
      if ($propertyName != null) {
        v1 = getNavigatedProperyValueOrNull(o1, $propertyName);
        v2 = getNavigatedProperyValueOrNull(o2, $propertyName);
      }
      else {
        v1 = o1;
        v2 = o2;
      }
      if ($comparator != null) {
        return $comparator.compare(v1, v2);
      }
      else { // v1 and v2 must be comparable
        Comparable c1 = (Comparable)v1; // ClassCastException
        Comparable c2 = (Comparable)v2;
        if (c1 == c2) {
          return 0;
        }
        else if (c1 == null) {
          return -1;
        }
        else if (c2 == null) {
          assert c1 != null;
          return 1;
        }
        else {
          assert c1 != c2;
          assert c1 != null;
          assert c2 != null;
          return c1.compareTo(c2);
        }
      }
    }
  }

  public final int compare(Object o1, Object o2) {
    Iterator iter = $chain.iterator();
    int result = 0;
    while ((result == 0) && iter.hasNext()) {
      Entry cce = (Entry)iter.next();
      result = cce.compare(o1, o2);
    }
    return result;
  }

  // Map methods

  public final int size() {
    return $chain.size();
  }

  public final boolean isEmpty() {
    return $chain.isEmpty();
  }

  public boolean containsKey(Object key) {
    Iterator iter = $chain.iterator();
    while (iter.hasNext()) {
      Entry entry = (Entry)iter.next();
      if (entry.getPropertyName().equals(key)) {
        return true;
      }
    }
    return false;
  }

  public boolean containsValue(Object value) {
    Iterator iter = $chain.iterator();
    while (iter.hasNext()) {
      Entry entry = (Entry)iter.next();
      if (entry.getComparator().equals(value)) {
        return true;
      }
    }
    return false;
  }

  public Object get(Object key) {
    Iterator iter = $chain.iterator();
    while (iter.hasNext()) {
      Entry entry = (Entry)iter.next();
      if (entry.getPropertyName().equals(key)) {
        return entry.getComparator();
      }
    }
    return null;
  }

  /**
   * @post addComparator(propertyName, comp, true);
   * @throws NullPointerException
   *         propertyName == null;
   * @throws ClassCastException
   *         ! propertyName instanceof String;
   * @throws ClassCastException
   *         ! comp instanceof Comparator;
   */
  public Object put(Object propertyName, Object comp) {
    Object previousValue = remove(propertyName);
    addComparator((String)propertyName, (Comparator)comp, true);
    return previousValue;
  }

  public Object remove(Object key) {
    ListIterator iter = $chain.listIterator();
    while (iter.hasNext()) {
      Entry entry = (Entry)iter.next();
      if (entry.getPropertyName().equals(key)) {
        iter.remove();
        return entry.getComparator();
      }
    }
    return null;
  }

  public void putAll(Map t) {
    Iterator iter = t.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry tEntry = (Map.Entry)iter.next();
      put(tEntry.getKey(), tEntry.getValue());
    }
  }

  public void clear() {
    $chain.clear();
  }

  public Set keySet() {
    Set result = new HashSet();
    Iterator iter = $chain.iterator();
    while (iter.hasNext()) {
      Entry entry = (Entry)iter.next();
      result.add(entry.getPropertyName());
    }
    return result;
  }

  public Collection values() {
    List result = new LinkedList();
    Iterator iter = $chain.iterator();
    while (iter.hasNext()) {
      Entry entry = (Entry)iter.next();
      result.add(entry.getComparator());
    }
    return result;
  }

  public Set entrySet() {
    Set result = new HashSet();
    Iterator iter = $chain.iterator();
    while (iter.hasNext()) {
      Entry entry = (Entry)iter.next();
      Map.Entry mEntry = new MapEntry(entry);
      result.add(mEntry);
    }
    return result;
  }

  private class MapEntry implements Map.Entry {

    public MapEntry(Entry entry) {
      $entry = entry;
    }

    public Object getKey() {
      return $entry.getPropertyName();
    }

    public Object getValue() {
      return $entry.getComparator();
    }

    public Object setValue(Object value) {
      Comparator previousValue = $entry.getComparator();
      $entry.setComparator((Comparator)value);
      return previousValue;
    }

    private Entry $entry;

  }

}
