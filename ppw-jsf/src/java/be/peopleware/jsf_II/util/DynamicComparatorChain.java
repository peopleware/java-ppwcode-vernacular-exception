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

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
 *   Apache Jakarta Commons beanutil Javadoc</a>. Furthmore, if
 *   the last element in a nested expression is <code>size</code> or
 *   <code>length</code>, and it is called on a {@link Collection} or
 *   {@link Map}, resp. an array, this returns the size of that instance.</p>
 * <p>This class also offers a Map interface, so that it can be configured, e.g.,
 *   as a managed map in JSF, as follows:</p>
 * <pre>
 *    &lt;managed-bean&gt;
 *      &lt;managed-bean-name&gt;<samp>my$semantic$class$Fqcn_collection_comparator&lt;/managed-bean-name&gt;
 *      &lt;managed-bean-class&gt;be.peopleware.jsf_II.util.DynamicComparatorChain&lt;/managed-bean-class&gt;
 *      &lt;managed-bean-scope&gt;session&lt;/managed-bean-scope&gt;
 *      &lt;map-entries&gt;
 *        &lt;map-entry&gt;
 *          &lt;key&gt;<samp>lastName</samp>&lt;/key&gt;
 *          &lt;null-value /&gt;
 *        &lt;/map-entry&gt;
 *        ...
 *        &lt;map-entry&gt;
 *          &lt;key&gt;<samp>roles</samp>.size&lt;/key&gt;
 *          &lt;null-value /&gt;
 *        &lt;/map-entry&gt;
 *        ...
 *        &lt;map-entry&gt;
 *          &lt;key&gt;<samp>address.street</samp>&lt;/key&gt;
 *          &lt;null-value /&gt;
 *        &lt;/map-entry&gt;
 *      &lt;/map-entries&gt;
 *    &lt;/managed-bean&gt;
 * </pre>
 * <p>Note that the comparator sorts on the top entry first.</p>
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


  static private final Log LOG = LogFactory.getLog(DynamicComparatorChain.class);



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
    LOG.debug("call to bringToFront; propertyName = " + propertyName);
    ListIterator iter = $chain.listIterator();
    LOG.debug("looking for entry with propertyName = " + propertyName);
    while (iter.hasNext()) {
      Entry cce = (Entry)iter.next();
      LOG.debug("entry: propertyName = " + cce.getPropertyName() +
                ", comparator = " + cce.getComparator() +
                ", ascending = " + cce.isAscending());
      if (cce.getPropertyName().equals(propertyName)) {
        LOG.debug("match found");
        if (iter.previousIndex() == 0) {
          LOG.debug("is in front, toggleAscending");
          cce.toggleAscending();
        }
        else {
          LOG.debug("not in front; bringing to front");
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

    private final static String COLLECTION_SIZE = "size";

    private final static String ARRAY_LENGTH = "length";

    private Object getNavigatedProperyValueOrNull(Object bean, String propertyName) {
      try {
        int sizeOfNameEnd = propertyName.lastIndexOf(COLLECTION_SIZE);
        if (sizeOfNameEnd < 0) {
          sizeOfNameEnd = propertyName.lastIndexOf(ARRAY_LENGTH);
        }
        if (sizeOfNameEnd >= 0) {
          Object sizeOf = null;
          if (sizeOfNameEnd == 0) { // size of bean
            sizeOf = bean;
          }
          else if (propertyName.charAt(sizeOfNameEnd - 1) == '.') {
            // it is a call
            String sizeOfName = propertyName.substring(0, sizeOfNameEnd - 1);
            // -1 to get rid of the '.'
            sizeOf = PropertyUtils.getProperty(bean, sizeOfName);
          }
          /* else no call, sizeOf stays null: normal handling;
             probably size is by accident the final part of a longer property name */
          if (sizeOf != null) {
            if (sizeOf instanceof Collection) {
              return new Integer(((Collection)sizeOf).size());
            }
            else if (sizeOf instanceof Map) {
              return new Integer(((Map)sizeOf).size());
            }
            else if (sizeOf.getClass().isArray()) {
              return new Integer(((Object[])sizeOf).length);
            }
            // else, normal handling
          }
        }
        // else, normal handling; result is null
        return PropertyUtils.getProperty(bean, propertyName);
      }
      catch (NestedNullException nnExc) {
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
      return null; // make compiler happy
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
    LOG.debug("compare called: o1 = " + o1 + ", o2 = " + o2);
    Iterator iter = $chain.iterator();
    int result = 0;
    while ((result == 0) && iter.hasNext()) {
      Entry cce = (Entry)iter.next();
      result = cce.compare(o1, o2);
    }
    LOG.debug("comparison result = " + result);
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
