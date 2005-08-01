/*<license>
Copyright 2004-2005, PeopleWare n.v.
NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.i18n;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIViewRoot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;


/**
 * <p>Common code for maps that offer access to entries in a properties file.
 *   These maps are unmodifiable.</p>
 * <p>Subclasses have to provide a keySet of supported keys at construction
 *   time, and implement {@link #getLabel(String)}</p>
 * <p>Request for an actual entry will use the Locale that
 *   is in the {@link UIViewRoot} then. If there are entries in the properties
 *   file for 1 {@link Locale} that are not in the properties file for another
 *   {@link Locale}, strange behavior is to be expected.</p>
 * <p>Due to the contract of a map, when an entry is requested for a
 *   non-existent key, <code>null</code> is returned. This is hard to debug.
 *   Therefor, a warning is written to the log in this case.</p>
 *
 * @author David Van Keer
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public abstract class AbstractResourceBundleMap implements Map {

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



  private static final Log LOG = LogFactory.getLog(AbstractResourceBundleMap.class);



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Create a new initialized properties map for the resource bundle
   * with basename <code>baseName</code>.
   * The {@link Locale} in the current {@link UIViewRoot} is used
   * to initialize the {@link #keySet()} and {@link #entrySet()}.
   *
   * @pre keySet != null;
   * @toryt(cC, org.toryt.contract.Collections);
   * @pre cC:noNull(keySet);
   * @pre cC:instanceOf(keySet, String);
   * @throws FatalFacesException
   *         getCurrentResourceBundle();
   */
  protected AbstractResourceBundleMap(Set keySet) throws FatalFacesException {
    $keySet = keySet;
    initEntrySet(); // $keySet must have been set
  }

  /*</construction>*/



  /*<section name="keys">*/
  //------------------------------------------------------------------

  /**
   * TODO (dvankeer): Write description
   */
  public final Set keySet() {
    return $keySet;
  }

  /**
   * @invar $keySet != null;
   */
  private Set $keySet;

  /**
   * @return PropertyUtils.getPropertyDescriptors(getType());
   */
  public final int size() {
    return keySet().size();
  }

  /**
   * @return size() == 0;
   */
  public final boolean isEmpty() {
    return size() == 0;
  }

  /**
   * @return keySet().contains(key);
   */
  public final boolean containsKey(Object key) {
    return keySet().contains(key);
  }

  /*</section>*/



  /*<section name="values">*/
  //------------------------------------------------------------------

  /**
   * If the <code>key</code> is
   * @todo description, contract
   *
   * @throws FatalFacesException
   */
  public final Object get(Object key) throws FatalFacesException {
    LOG.debug("getting entry for " + key);
    if (key == null) {
      LOG.warn("key \"" + key + "\" is null; returning null");
      return null;
    }
    if (! (key instanceof String)) {
      LOG.warn("key \"" + key + "\" is not a String; returning null");
      return null;
    }
    if (! containsKey(key)) {
      LOG.warn("key \"" + key + "\" is not in the keySet (" +
               keySet() + "); returning null");
      return null;
    }
    return getLabel((String)key);
  }

  /**
   * Return the entry for key. If no entry for key is found, return <code>null</code>.
   * @pre key != null;
   * @pre key instanceof String;
   * @pre containsKey(key);
   */
  protected abstract String getLabel(String key);

  /**
   * @note This is a costly method.
   *
   * @return values().contains(value);
   */
  public final boolean containsValue(Object value) {
    return values().contains(value);
  }

  public final Collection values() {
    Set result = new TreeSet();
    Iterator keys = keySet().iterator();
    while (keys.hasNext()) {
      String key = (String)keys.next();
      result.add(getLabel(key));
    }
    return Collections.unmodifiableSet(result);
  }

  /*</property>*/



  /*<property name="entrySet">*/
  //------------------------------------------------------------------

  /**
   * This set contains label-value pairs, for all
   */
  public final Set entrySet() {
    return $entrySet;
  }

  /**
   * @pre $keySet must have been set
   */
  private void initEntrySet() {
    Set result = new TreeSet();
    Iterator keys = keySet().iterator();
    while (keys.hasNext()) {
      String key = (String)keys.next();
      result.add(new EntrySetEntry(key));
    }
    $entrySet = Collections.unmodifiableSet(result);
  }

  private class EntrySetEntry implements Map.Entry, Comparable {

    public EntrySetEntry(String key) {
      $key = key;
    }

    private String $key;

    public final Object getKey() {
      return $key;
    }

    public final Object getValue() {
      return getLabel($key);
    }

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

  private Set $entrySet;

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
