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

  /*</section>*/



  /*<section name="values">*/
  //------------------------------------------------------------------

  /**
   * If the <code>key</code> is
   * @todo description, contract
   *
   * If the key is not in the set, we try the key as a nested property
   * name nevertheless. If nothing found, we return
   * "???".
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
   * Return the entry for key. If no entry for key is found, return <code>null</code>.
   * @pre key != null;
   * @pre key instanceof String;
   * @pre containsKey(key);
   */
  protected abstract String getLabel(String key);

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
      result.add(new DefaultSetEntry(key));
    }
    $entrySet = Collections.unmodifiableSet(result);
  }

  private Set $entrySet;

  /*</property>*/

}
