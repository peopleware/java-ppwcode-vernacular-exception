package be.peopleware.i18n;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * <p>Support methods to work with properties files.
 *   There are general methods here, and some methods specific to a
 *   properties file strategy for i18n of bean type labels and bean property
 *   labels, and general bean related text.</p>
 * <p>In general, we look for the i18n strings in a properties file that has
 *   the same fully qualified name as the bean class whose labels we are
 *   interested in. If a match is not found, we search for the labels
 *   through the supertypes of the bean class.</p>
 * 
 * @todo test code
 */
public abstract class Properties {

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

  /**
   * Cannot instantiate this class. Only use static methods.
   */
  private Properties() {
    // NOP
  }

  /*</construction>*/



  /**
   * @param     basename
   *            The base name of the resource bundle to look for.
   * @param     keys
   *            The keys to look for in the resource bundle.
   * @param     strategy
   *            The strategy to use to look for a
   *            resource bundle properties file.
   * @pre       key != null && ! key.equals("");
   * @pre       strategy != null
   * @return    ; <code>null</code> if something goes wrong
   */
  public static String findKeyWithBasename(final String basename,
                                            final String[] keys,
                                            final ResourceBundleLoadStrategy strategy) {
    assert strategy != null
        : "strategy != null"; //$NON-NLS-1$
    assert keys != null && keys.length > 0
        : "keys != null && keys.length > 0"; //$NON-NLS-1$
    String result = null;
    if (basename != null && !basename.equals("")) { //$NON-NLS-1$
      ResourceBundle bundle = strategy.loadResourceBundle(basename);
      if (bundle != null) {
        int i = 0;
        while (result == null && i < keys.length) {
          result = findKeyInResourceBundle(bundle, keys[i]);
          i++;
        }
      }
    }
    return result;
  }

  /**
   * @param     rb
   *            The resource bundle to look in.
   * @param     key
   *            The key to look for.
   * @pre       rb != null;
   * @pre       key != null && key.length() > 0;
   * @return    ; <code>null</code> if the resource could not be retrieved
   */
  public static String findKeyInResourceBundle(final ResourceBundle rb,
                                               final String key) {
    assert rb != null : "rb != null;"; //$NON-NLS-1$
    assert (key != null && key.length() > 0)
        : "key != null && key.length() > 0"; //$NON-NLS-1$
    String result = null;
    try {
      result = rb.getString(key);
          /* throws MissingResourceException, ClassCastException */
    }
    catch (ClassCastException ccExc) {
      // match is not a String, return null
    }
    catch (MissingResourceException mrExc) {
      // key not found, return null
    }
    return result;
  }

  /**
   * <p>Token used in return values to signal properties that
   *  are not found.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String NOT_FOUND_TOKEN = "???"; //$NON-NLS-1$

  /**
   * <p>Token used in return values to separate type names
   *  from property names.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String PROPERTY_SEPARATOR_TOKEN = "#"; //$NON-NLS-1$

  /**
   * <p>Return a label for a property with name <code>property</code>
   *   of a type <code>type</code>.</p>
   * <p>We look for a property with key
   *   <code>propertyName.<var>property</var></code> or
   *   <code>propertyName.short.<var>property</var></code>
   *   in a properties file with the name of <code>type</code>,
   *   and its supertypes. We search bottom up, and return the
   *   first match. If no match is found, we return the String
   *   <code>&quot;???&quot;<var>property</var>&quot;???&quot;</code>.
   *   If the short label does not exist in a given properties file,
   *   we look for the normal label, and vice versa, before
   *   we try the next type.</p>
   *
   * @param property
   *        The name of the property to return an i18n'ed label for.
   * @param type
   *        The type of bean that owns the property. This is the
   *        start of our lookup.
   * @param shortLabel
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *         (property == null)
   *          || (property.length() <= 0)
   *          || (type == null);
   */
  public static String i18nPropertyLabel(
      final String property,
      final Class type,
      final boolean shortLabel,
      final ResourceBundleLoadStrategy strategy) {
    if ((property == null)
          || (property.length() <= 0)
          || (type == null)) {
      throw new IllegalArgumentException("parameters must be effective"); //$NON-NLS-1$
    }
    String result = findKeyInTypeProperties(type,
                                            i18nPropertyLabel_keys(property, shortLabel),
                                            strategy);
    return (result != null)
            ? result
            : keyNotFound(property + PROPERTY_SEPARATOR_TOKEN + type.getName());
  }

  /**
   * <p>Return a label for a property with name <code>property</code>
   *   of a type <code>instance.getClass()</code>.</p>
   *
   * @see #i18nPropertyLabel(String, Class, boolean, ResourceBundleLoadStrategy)
   *
   * @param property
   *        The name of the property to return an i18n'ed label for.
   * @param instance
   *        The object that owns the property. The class of this object
   *        is the start of our lookup.
   * @param shortLabel
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *         (property == null)
   *          || (property.length() <= 0)
   *          || (type == null);
   */
  public static String i18nInstancePropertyLabel(
      final String property,
      final Object instance,
      final boolean shortLabel,
      final ResourceBundleLoadStrategy strategy) {
    return i18nPropertyLabel(property,
                             instance.getClass(),
                             shortLabel,
                             strategy);
  }

  /**
   * <p>Return a string from a properties file that has a basename that
   *   matches <code>type</code>, or one of its supertypes.</p>
   * <p>We look for <code>keys</code> in a properties file with the same
   *   name as <code>type.getName()</code>, in order. If no match is
   *   found, or no such properties file exists, we try again with
   *   the super types, in the order they are presented by the reflection
   *   functionality. We look in interfaces first, the superclass if
   *   no match is found in the super interfaces. We search breath-first</p>.
   * <p>If this entire process does not return a valid result, we return
   *   <code>null</code></p>
   *
   * @param type
   *        The type to use as starting point for the lookup in associated
   *        properties files.
   * @param keys
   *        The keys to lookup in order.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *         (property == null)
   *          || (property.length() <= 0)
   *          || (type == null);
   */
  public static String findKeyInTypeProperties(
      final Class type,
      final String[] keys,
      final ResourceBundleLoadStrategy strategy) {
    LinkedList agenda = new LinkedList();
    agenda.add(type);
    String result = null;
    while ((result == null) && (!agenda.isEmpty())) {
      Class current = (Class)agenda.removeFirst();
      result = findKeyWithBasename(current.getName(), keys, strategy);
      if (result == null) {
        agenda.addAll(Arrays.asList(current.getInterfaces()));
        Class superClass = current.getSuperclass();
        if (superClass != null) {
          agenda.add(superClass);
        }
      }
    }
    return result; // null if not found
  }


  /**
   * <p>Prefix used in property files to discriminate property
   *  labels.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_PROPERTY_LABEL_KEY_PREFIX
      = "propertyName."; //$NON-NLS-1$

  /**
   * <p>Prefix used in property files to discriminate short property
   *  labels.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_SHORT_PROPERTY_LABEL_KEY_PREFIX
      = I18N_PROPERTY_LABEL_KEY_PREFIX + "short."; //$NON-NLS-1$

  /**
   * @pre property != null;
   * @pre property.length() > 0;
   */
  private static String[] i18nPropertyLabel_keys(final String property,
                                                 final boolean shortLabel) {
    assert property != null;
    assert property.length() > 0;
    String key = I18N_PROPERTY_LABEL_KEY_PREFIX + property;
    String shortKey = I18N_SHORT_PROPERTY_LABEL_KEY_PREFIX + property;
    return shortLabel
            ? new String[] {shortKey, key}
            : new String[] {key, shortKey};
  }

  /**
   * @pre key != null;
   * @pre key.length() > 0;
   * @pre type != null;
   */
  private static String keyNotFound(final String key) {
    assert key != null;
    assert key.length() > 0;
    return NOT_FOUND_TOKEN
            + key
            + NOT_FOUND_TOKEN;
  }

  /**
   * <p>Key used in property files to discriminate the type
   *  label.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_TYPE_LABEL_KEY
      = "type"; //$NON-NLS-1$

  /**
   * <p>Key used in property files to discriminate the
   *  plural type label.</p>
   *
   * <p><strong>= {@value}</strong></p>
   */
  public static final String I18N_PLURAL_TYPE_LABEL_KEY
      = I18N_TYPE_LABEL_KEY + ".plural"; //$NON-NLS-1$

  private static String[] I18N_TYPE_LABEL_KEYS
      = new String[] {I18N_TYPE_LABEL_KEY};

  private static String[] I18N_PLURAL_TYPE_LABEL_KEYS
      = new String[] {I18N_PLURAL_TYPE_LABEL_KEY};

  /**
   * <p>Return a label for a type <code>type</code>.</p>
   * <p>We look for a property with key
   *   <code>type</code> or <code>type.plural</code>
   *   in a properties file with the name of <code>type</code>,
   *   and its supertypes. We search bottom up, and return the
   *   first match. If no match is found, we return the String
   *   <code>&quot;???&quot;<var>className</var>&quot;???&quot;</code>.</p>
   *
   * @param type
   *        The type of bean that owns the property. This is the
   *        start of our lookup.
   * @param plural
   *        Should we return a short or a normal label. Defaults to
   *        <code>false</code>.
   * @param strategy
   *        The strategy to use to look for a
   *        resource bundle properties file.
   * @throws IllegalArgumentException
   *          type == null;
   */
  public static String i18nTypeLabel(final Class type,
                                     final boolean plural,
                                     final ResourceBundleLoadStrategy strategy) {
    if (type == null) {
      throw new IllegalArgumentException("type must be effective"); //$NON-NLS-1$
    }
    String result = findKeyInTypeProperties(type,
                                            (plural
                                              ? I18N_PLURAL_TYPE_LABEL_KEYS
                                              : I18N_TYPE_LABEL_KEYS),
                                            strategy);
    return (result != null) ? result : keyNotFound(type.getName());
  }


}