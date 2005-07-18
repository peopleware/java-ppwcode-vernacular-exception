/*<license>
Copyright 2004-2005, PeopleWare n.v.
NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.i18n;


import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
public class I18nPropertyLabelMap implements Map {
  
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
    assert type != null;
    $type = type;
    $shortLabel = shortLabel;
    initKeySet();
    initEntrySet();
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


  
  /*<section name="keys">*/
  //------------------------------------------------------------------

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
   */
  public final Set keySet() {
    return $keySet;
  }    
  
  private void initKeySet() {
    LOG.debug("type is " + getType());
    PropertyDescriptor[] properties
      = PropertyUtils.getPropertyDescriptors(getType());
    LOG.debug("properties are " + properties);
    Set result = new HashSet(properties.length);
    for (int i = 0; i < properties.length; i++) {
      result.add(properties[i].getName());
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
   */
  public final Object get(Object key) {
    LOG.debug("getting "
              + (isShortLabel() ? "short" : "nominal")
              + " label for " + key);
    if ((! (key instanceof String)) || (! containsKey(key))) {
      LOG.debug(key + " is not a property name of " + getType() + "; returning null");
      return null;
    }
    else {
      return getLabel((String)key);
    }
  }

  /**
   * @pre containsKey(propertyName);
   */
  private String getLabel(String propertyName) {
    assert containsKey(propertyName);
    String result = Properties.i18nPropertyLabel(propertyName, getType(),
                                                 isShortLabel(),
                                                 RobustCurrent.JSF_RESOURCE_BUNDLE_LOAD_STRATEGY);
    LOG.debug("label for " + propertyName + ": " + result);
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
    
    public EntrySetEntry(String propertyName) {
      $propertyName = propertyName;
    }
    
    private String $propertyName;
    
    public final Object getKey() {
      return $propertyName;
    }
    
    public final Object getValue() {
      return getLabel($propertyName);
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
