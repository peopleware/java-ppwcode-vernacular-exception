/*<license>
Copyright 2004-2005, PeopleWare n.v.
NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.i18n;


import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

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
public class I18nButtonLabelMap extends AbstractResourceBundleMap {

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
    super(createKeySet(baseName));
    $resourceBundleBaseName = baseName;
  }

  /**
   * @pre $resourceBundleBaseName must be set
   * @throws FatalFacesException
   *         getCurrentResourceBundle();
   */
  private static final Set createKeySet(String baseName) throws FatalFacesException {
    Set result = new HashSet();
    Enumeration enumeration = currentResourceBundle(baseName).getKeys();
    while (enumeration.hasMoreElements()) {
      String bundleKey = (String)enumeration.nextElement();
      result.add(bundleKey);
    }
    Set keySet = Collections.unmodifiableSet(result);
    LOG.debug("keySet init complete: " + keySet);
    return keySet;
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



  /*<property name="resourceBundle">*/
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
    return currentResourceBundle(getResourceBundleBaseName());
  }

  /**
   * The resource bundle with base name <code>baseName</code>,
   * loaded with the locale of the current {@link UIViewRoot} with
   * {@link JsfResourceBundleLoadStrategy}. If no such bundle can be found,
   * an exception is thrown.
   *
   * @throws FatalFacesException
   *         RobustCurrent.resourceBundle(getResourceBundleBaseName());
   * @throws FatalFacesException
   *         RobustCurrent.resourceBundle(getResourceBundleBaseName()) == null;
   */
  private final static ResourceBundle currentResourceBundle(String baseName) throws FatalFacesException {
    ResourceBundle result = RobustCurrent.resourceBundle(baseName);
    if (result == null) {
      RobustCurrent.fatalProblem("could not load resource bundle with basename \"" +
                                 baseName + "\" (locale: " +
                                 RobustCurrent.locale() + ")", LOG);
    }
    return result;
  }

  /*</property>*/




  /**
   * If there is no key <code>key</code>, return <code>null</code>.
   *
   * @throws FatalFacesException
   *         getCurrentResourceBundle();
   */
  protected final String getLabel(final String buttonName) throws FatalFacesException {
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

}
