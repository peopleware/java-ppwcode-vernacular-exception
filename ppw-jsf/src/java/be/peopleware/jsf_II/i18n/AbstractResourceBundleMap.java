/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.i18n;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIViewRoot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.util.AbstractUnmodifiableMap;


/**
 * <p>Common code for maps that offer access to entries in a properties file.
 *   These maps are unmodifiable.</p>
 * <p>Subclasses have to provide a keySet of supported keys at construction
 *   time, and implement {@link #getLabel(String)}</p>
 * <p>Requests for an actual entry will use the Locale that
 *   is in the {@link UIViewRoot} then. If there are entries in the properties
 *   file for 1 {@link Locale} that are not in the properties file for another
 *   {@link Locale}, strange behavior is to be expected.</p>
 * <p>Due to the contract of a map, when an entry is requested for a
 *   non-existent key, <code>null</code> is returned. This is hard to debug.
 *   Therefor, a warning is written to the log in this case.</p>
 *
 * @invar  keySet() != null;
 * @invar  cC:instanceOf(keySet(), String);
 *
 * @author David Van Keer
 * @author Jan Dockx
 * @author Nele Smeets
 * @author PeopleWare n.v.
 */
public abstract class AbstractResourceBundleMap extends AbstractUnmodifiableMap {

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
   * Create a new abstract resource bundle map with the given key set.
   * The key set is initialised using the given set.
   *
   * @param  keySet
   * @pre    keySet != null;
   * @toryt(cC, org.toryt.contract.Collections);
   * @pre    cC:noNull(keySet);
   * @pre    cC:instanceOf(keySet, String);
   * @throws FatalFacesException
   *         getCurrentResourceBundle();
   */
  protected AbstractResourceBundleMap(final Set keySet) throws FatalFacesException {
    $keySet = keySet;
    initEntrySet(); // $keySet must have been set
  }

  /*</construction>*/



  /*<section name="keys">*/
  //------------------------------------------------------------------

  /**
   * Returns a set view of the keys contained in this map.
   *
   * @basic
   */
  public final Set keySet() {
    return $keySet;
  }

  /**
   * @invar $keySet != null;
   * @invar  cC:instanceOf(keySet(), String);
   */
  private Set $keySet;

  /*</section>*/



  /*<section name="values">*/
  //------------------------------------------------------------------

  /**
   * Returns the value to which this map maps the specified key.
   *
   * If the given key is not effective or not a String, <code>null</code> is
   * returned.
   * Otherwise, the corresponding value is searched using the
   * {@link #getLabel(String)} method.
   * If there is not corresponding value, we return
   * "??? " + key + " ???".
   *
   * @result  (key == null || !(key instanceof String))
   *            ==> result == null;
   * @result  (key != null && key instanceof String && getLabel(key) != null)
   *            ==> result == getLabel(key);
   * @result  (key != null && key instanceof String && getLabel(key) == null)
   *            ==> result == "??? " + keyString + " ???";
   * @throws  FatalFacesException
   */
  public final Object get(final Object key) throws FatalFacesException {
    LOG.debug("getting entry for " + key);
    if (key == null) {
      LOG.warn("key \"" + key + "\" is null; returning null");
      return null;
    }
    if (!(key instanceof String)) {
      LOG.warn("key \"" + key + "\" is not a String; returning null");
      return null;
    }
    String keyString = (String)key;
    String label = getLabel(keyString);
    if (label == null) {
      return "??? " + keyString + " ???";
    }
    else {
      return label;
    }
  }

  /**
   * Return the value corresponding to the given key.
   * If no value for the given key is found, return <code>null</code>.
   *
   * @pre key != null;
   * @pre key instanceof String;
   * @pre containsKey(key);
   */
  protected abstract String getLabel(final String key);

  /**
   * Returns a collection view of the values contained in this map.
   *
   * The values are found by invoking {@link #getLabel(String)} on the keys in
   * {@link #keySet()}.
   *
   * @result  result != null;
   * @result  (forAll Object key; keySet().contains(key); result.contains(getLabel(key)));
   * @result  (forAll Object obj; result.contains(obj);
   *             (forSome Object key; keySet().contains(key), obj = getLabel(key)));
   */
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
   * Returns a set view of the mappings contained in this map.
   *
   * @basic
   */
  public final Set entrySet() {
    return $entrySet;
  }

  /**
   * Initialise the entry set using the keys in {@link #keySet()}.
   * For each key, a corresponding {@link AbstractUnmodifiableMap.DefaultSetEntry}
   * is created.
   *
   * @pre   keySet() != null;
   * @post  new.entrySet() != null;
   * @post  cC:instanceOf(new.entrySet(), DefaultSetEntry);
   * @post  (forAll Object key; keySet().contains(key);
   *           (forSome Object entry; new.entrySet().contains(entry); entry.getKey() == key));
   * @post  (forAll Object entry; new.entrySet().contains(entry);
   *           (forSome Object key; keySet().contains(key); entry.getKey() == key));
   */
  private void initEntrySet() {
    Set result = new TreeSet();
    Iterator keys = keySet().iterator();
    while (keys.hasNext()) {
      String key = (String)keys.next();
      result.add(new DefaultSetEntry(key));
    }
    $entrySet = Collections.unmodifiableSet(result);
  }

  private Set $entrySet;

  /*</property>*/

}
