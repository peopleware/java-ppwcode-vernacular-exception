/*<license>
Copyright 2004-2005, PeopleWare n.v.
NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.i18n;


import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIViewRoot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;


/**
 * <p>A map that offers access to entries in a properties file.</p>
 * <p>Note that the {@link #keySet()} and {@link #entrySet()} are generated
 *   during construction, based on the properties file that is found
 *   with the {@link JsfResourceBundleLoadStrategy}
 *   with the {@link Locale} that is in the {@link UIViewRoot}
 *   at that time. Later request for an actual entry will use the Locale that
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
public class I18nButtonLabelMap implements Map {

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



  private static final Log LOG = LogFactory.getLog(I18nButtonLabelMap.class);



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Create a new initialized properties map for the resource bundle
   * with basename <code>baseName</code>.
   * The {@link Locale} in the current {@link UIViewRoot} is used
   * to initialize the {@link #keySet()} and {@link #entrySet()}.
   *
   * @throws FatalFacesException
   *         getCurrentResourceBundle();
   */
  public I18nButtonLabelMap(String baseName) throws FatalFacesException {
    $resourceBundleBaseName = baseName;
    initKeySet(); // $resourceBundleBaseName must be set
    initEntrySet(); // initKeySet() must have run already
  }

  /*</construction>*/



  /*<property name="resourceBundleBaseName">*/
  //------------------------------------------------------------------

  public final String getResourceBundleBaseName() {
    return $resourceBundleBaseName;
  }

  /**
   * @invar $resourceBundleBaseName != null;
   */
  private String $resourceBundleBaseName;

  /*</property>*/



  /*<property name="resourceBundleBaseName">*/
  //------------------------------------------------------------------

  /**
   * The resource bundle with base name {@link #getResourceBundleBaseName()},
   * loaded with the locale of the current {@link UIViewRoot} with
   * {@link JsfResourceBundleLoadStrategy}. If no such bundle can be found,
   * an exception is thrown.
   *
   * @throws FatalFacesException
   *         RobustCurrent.resourceBundle(getResourceBundleBaseName());
   * @throws FatalFacesException
   *         RobustCurrent.resourceBundle(getResourceBundleBaseName()) == null;
   */
  public final ResourceBundle getCurrentResourceBundle() throws FatalFacesException {
    ResourceBundle result = RobustCurrent.resourceBundle(getResourceBundleBaseName());
    if (result == null) {
      RobustCurrent.fatalProblem("could not load resource bundle with basename \"" +
                                 getResourceBundleBaseName() + "\" (locale: " +
                                 RobustCurrent.locale() + ")", LOG);
    }
    return result;
  }

  /*</property>*/



  /*<section name="keys">*/
  //------------------------------------------------------------------

  /**
   * TODO (dvankeer): Write description
   */
  public final Set keySet() {
    return $keySet;
  }

  /**
   * @pre $resourceBundleBaseName must be set
   * @throws FatalFacesException
   *         getCurrentResourceBundle();
   */
  private void initKeySet() throws FatalFacesException {
    Set result = new HashSet();
    Enumeration enumeration = getCurrentResourceBundle().getKeys();
    while (enumeration.hasMoreElements()) {
      String bundleKey = (String)enumeration.nextElement();
      result.add(bundleKey);
    }
    $keySet = Collections.unmodifiableSet(result);
    LOG.debug("$keySet init complete: " + $keySet);
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
   *         getCurrentResourceBundle();
   */
  public final Object get(Object key) throws FatalFacesException {
    LOG.debug("getting entry for " + key);
    if (! (key instanceof String)) {
      LOG.warn("key \"" + key + "\" is not a String; returning null");
      return null;
    }
    else {
      return getLabel((String)key);
    }
  }

  /**
   * If there is no key <code>key</code>, return <code>null</code>.
   *
   * @throws FatalFacesException
   *         getCurrentResourceBundle();
   */
  private String getLabel(final String buttonName) throws FatalFacesException {
    if (! containsKey(buttonName)) {
      LOG.warn("key \"" + buttonName + "\" not initialized for resource bundle with basename \"" +
               getResourceBundleBaseName() + "\"; returning null");
      return null;
    }
    String result = null;
    try {
      result = getCurrentResourceBundle().getString(buttonName);
    }
    catch (MissingResourceException mrExc) {
      LOG.warn("no entry for key \"" + buttonName + "\" found in resource bundle with basename \"" +
               getResourceBundleBaseName() + "\" (locale: " + RobustCurrent.locale() +
               "); returning null",
               mrExc);
    }
    catch (ClassCastException ccExc) {
      LOG.warn("entry for key \"" + buttonName + "\" in resource bundle with basename \"" +
               getResourceBundleBaseName() + "\" (locale: " + RobustCurrent.locale() +
               ") is no a String; returning null", ccExc);
    }
    LOG.debug("entry for " + buttonName + ": " + result);
    return result;
  }

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
   * @pre initKeySet() must have run already
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

    public EntrySetEntry(String buttonLabel) {
      $buttonLabel = buttonLabel;
    }

    private String $buttonLabel;

    public final Object getKey() {
      return $buttonLabel;
    }

    public final Object getValue() {
      return getLabel($buttonLabel);
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
