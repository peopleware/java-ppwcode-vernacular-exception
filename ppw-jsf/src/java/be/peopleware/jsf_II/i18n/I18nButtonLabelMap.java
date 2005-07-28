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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import javax.faces.component.UIViewRoot;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.i18n_I.Properties;
import be.peopleware.i18n_I.ResourceBundleLoadStrategy;
import be.peopleware.jsf_II.RobustCurrent;

// TODO: DOCUMENTATION......

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
 * @author David Van Keer
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
   * Create a new initialized Button Label Map, all button for use in the application are
   * available via this Map.
   */
  public I18nButtonLabelMap() {
    initKeySet();
    initEntrySet();
  }
  
  /*</construction>*/
  
  ResourceBundle $bundle = ResourceBundle.getBundle(getClass().getPackage().getName() + ".ButtonLabels",
                                                    RobustCurrent.facesContext().getViewRoot().getLocale());
  
  
  /*<section name="keys">*/
  //------------------------------------------------------------------

  /**
   * TODO (dvankeer): Write description
   */
  public final Set keySet() {
    return $keySet;
  }    
  
  private void initKeySet() {
    Set result = new HashSet();
    Enumeration enumeration = $bundle.getKeys();
    while (enumeration.hasMoreElements()) {
      result.add((String)enumeration.nextElement());
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
    LOG.debug("getting button label for " + key);
    if ((! (key instanceof String)) || (! containsKey(key))) {
      LOG.debug("Button label for " + key + " does not exist; returning null");
      return null;
    }
    else {
      return getLabel((String)key);
    }
  }

  /**
   * @pre containsKey(propertyName);
   */
  private String getLabel(final String buttonName) {
    assert containsKey(buttonName);
    String result = $bundle.getString(buttonName);
    LOG.debug("Button label for " + buttonName + ": " + result);
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
