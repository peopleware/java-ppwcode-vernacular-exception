/*<license>
Copyright 2004-2005, PeopleWare n.v.
NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.i18n;


import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.faces.component.UIViewRoot;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.i18n_I.Properties;
import be.peopleware.i18n_I.ResourceBundleLoadStrategy;
import be.peopleware.jsf_II.RobustCurrent;


/**
 * <p>A map that offers the i18n labels for properties of beans, to be found
 *   in properties files, as described in
 *   {@link Properties#i18nPropertyLabel(java.lang.String, java.lang.Class, boolean, ResourceBundleLoadStrategy)}.</p>
 *   This is based on the functionality of <code>&lt;f:loadBundle&gt;</code>.</p>
 * <p>The keys in this map are the programmatic property names of {@link #getType()}.
 *   If no match can be found in the property file with the name
 *   <code>getType() + &quot;.properties&quot;</code> with the expected key,
 *   we look in the properties file for all supertypes.</p>
 * <p>The locale used is retrieved from the {@link UIViewRoot#getLocale()}, and
 *   the resource bundles are loaded using the JSF strategy. The returned
 *   label is the nominal label if {@link #isShortLabel()} is <code>false</code>,
 *   and the short label if it is <code>true</code>.</p>
 * <p>A value is returned for each property of {@link #getType()}. If no label
 *   can be found for a property, <code>&quot;???&quot; + <var>propertyName</var>
 *   + &quot;???&quot;</code>.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public class I18nPropertyLabelMap extends AbstractResourceBundleMap {

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



  private static final Log LOG = LogFactory.getLog(I18nPropertyLabelMap.class);



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @pre type != null;
   * @post new.getType() == type;
   * @post new.isShortLabel() == shortLabel;
   */
  public I18nPropertyLabelMap(Class type, boolean shortLabel) {
    super(createKeySet(type));
    $type = type;
    $shortLabel = shortLabel;
  }

  /**
   * @pre type != null;
   */
  private static Set createKeySet(Class type) {
    assert type != null;
    LOG.debug("type is " + type);
    PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(type);
    LOG.debug("properties are " + properties);
    Set result = new HashSet(properties.length);
    for (int i = 0; i < properties.length; i++) {
      result.add(properties[i].getName());
    }
    Set keySet = Collections.unmodifiableSet(result);
    LOG.debug("keySet init complete: " + keySet);
    return keySet;
  }

  /*</construction>*/



  /*<property name="type">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Class getType() {
    return $type;
  }

  /**
   * @invar $type != null;
   */
  private Class $type;

  /*</property>*/



  /*<property name="short">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final boolean isShortLabel() {
    return $shortLabel;
  }

  private boolean $shortLabel;

  /*</property>*/



  /**
   * The set of the names of all properties of {@link #getType()}.
   *
   * @toryt(cC, org.toryt.contract.Collections);
   * @result cC:noNull(result);
   * @result cC:instanceOf(result, String);
   * @result result.size() == PropertyUtils.getPropertyDescriptors(getType());
   * @result (forall PropertyDescriptor pd;
   *            PropertyUtils.getPropertyDescriptors(getType()).contains(pd);
   *            result.contains(pd.getName()));
   *
  public final Set keySet();
   */



  protected final String getLabel(String propertyName) {
    String result = Properties.i18nPropertyLabel(propertyName, getType(),
                                                 isShortLabel(),
                                                 RobustCurrent.JSF_RESOURCE_BUNDLE_LOAD_STRATEGY);
    LOG.debug("label for " + propertyName + ": " + result);
    return result;
  }

}
