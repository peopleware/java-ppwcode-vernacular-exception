package be.peopleware.jsp_I.tag;


import java.util.Locale;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport;

import be.peopleware.bean_I.Beans;
import be.peopleware.i18n_I.Properties;
import be.peopleware.jsp_I.JSTLResourceBundleLoadStrategy;


/**
 * General JSP functions.
 *
 * @author      Jan Dockx
 * @author      David Van Keer
 * @author      PeopleWare n.v.
 */
 public class Functions {

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



  // default constructor

  /**
   * Returns the constant(public final static) with the given fully qualified
   * name.
   *
   * @param     fqClassName
   *            The fully qualified class name of the type to look in
   *            for the constant.
   * @param     constantName
   *            The name of the constant whose value to return.
   * @return    Object
   *            The value of the field named <code>constantName</code>
   *            in class <code>fqClassName</code>.
   * @throws    LinkageError
   *            Error retrieving value.
   * @throws    ClassNotFoundException
   *            Could not find class <code>fqClassName</code>.
   * @throws    NoSuchFieldException
   *            Could not find a field named <code>constantName</code>
   *            in class <code>fqClassName</code>.
   * @throws    NullPointerException
   *            Error retrieving value.
   * @throws    SecurityException
   *            Not allowed to read the value of the field named
   *            <code>constantName</code>
   *            in class <code>fqClassName</code>.
   * @throws    IllegalAccessException
   *            The field named
   *            <code>constantName</code>
   *            in class <code>fqClassName</code> is not public.
   * @throws    IllegalArgumentException
   *            Error retrieving value.
   */
  public static Object constant(final String fqClassName,
                                final String constantName)
      throws LinkageError,
             ClassNotFoundException,
             NoSuchFieldException,
             NullPointerException,
             SecurityException,
             IllegalAccessException,
             IllegalArgumentException {
    return Beans.constant(fqClassName, constantName);
  }



  /**
   * @pre pc != null;
   */
  public static Locale locale(final PageContext pc) {
    assert pc != null;
    Locale pref = getLocale(pc, Config.FMT_LOCALE);
                      // Try to find locale set with fmt:setLocale
    if (pref == null) { // not set with fmt:setLocale
      pref = pc.getRequest().getLocale();
              // look in browser request; if not found, look at server
    }
    return pref;
  }


  public static String i18nlabel(final LocalizationContext bundle,
                                 final String key) {
    assert bundle != null : "Seems that we do not have a bundle set."; //$NON-NLS-1$
    assert key != null : "Fetching with a empty key is not usefull."; //$NON-NLS-1$
    ResourceBundle resourceBundle = bundle.getResourceBundle();
    String result = resourceBundle.getString(key);
    return result;
  }

  /**
   * COPIED FROM JSTL:
   *    org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport,
   * WHERE THE METHOD IS PACKAGE ACCESSIBLE
   *
   * Returns the locale specified by the named scoped attribute or context
   * configuration parameter.
   *
   * <p> The named scoped attribute is searched in the page, request,
   * session (if valid), and application scope(s) (in this order). If no such
   * attribute exists in any of the scopes, the locale is taken from the
   * named context configuration parameter.
   *
   * @param pageContext the page in which to search for the named scoped
   * attribute or context configuration parameter
   * @param name the name of the scoped attribute or context configuration
   * parameter
   *
   * @return the locale specified by the named scoped attribute or context
   * configuration parameter, or <tt>null</tt> if no scoped attribute or
   * configuration parameter with the given name exists
   */
  private static Locale getLocale(final PageContext pageContext,
                                  final String name) {
    Locale loc = null;
    Object obj = Config.find(pageContext, name);
    if (obj != null) {
      if (obj instanceof Locale) {
        loc = (Locale) obj;
      }
      else {
        loc = SetLocaleSupport.parseLocale((String) obj);
      }
    }
    return loc;
  }

  /**
   * <p>Return a label for a property with name <code>property</code>
   *   of a type <code>type</code>. The
   *   {@link JSTLResourceBundleLoadStrategy} is used to lookup
   *   the correct resource bundle, dependent on the JSTL
   *   <code>fmt:</code> locale.</p>
   *
   * @param     property
   *            The name of the property to return an i18n'ed label for.
   * @param     type
   *            The fully qualified name of the type of bean that owns
   *            the property. This is the start of our lookup.
   * @param     pageContext
   *            The pageContext where we look for the locale information.
   * @throws    IllegalArgumentException
   *            (property == null)
   *            || (property.length() <= 0)
   *            || (* no class with name type *);
   */
  public static String i18nPropertyLabel(final String property,
                                         final String type,
                                         final PageContext pageContext) {
    JSTLResourceBundleLoadStrategy strategy =
        new JSTLResourceBundleLoadStrategy();
    strategy.setPageContext(pageContext);
    Class typeClass;
    try {
      typeClass = Class.forName(type);
    }
    catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Class " //$NON-NLS-1$
                                         + type
                                         + " not found"); //$NON-NLS-1$
    }
    return Properties.i18nPropertyLabel(property, typeClass, false, strategy);
  }

  /**
   * <p>Return a <em>short</em> label for a property with name
   *   <code>property</code>
   *   of a type <code>type</code>. The
   *   {@link JSTLResourceBundleLoadStrategy} is used to lookup
   *   the correct resource bundle, dependent on the JSTL
   *   <code>fmt:</code> locale.</p>
   *
   * @param     property
   *            The name of the property to return an i18n'ed label for.
   * @param     type
   *            The fully qualified name of the type of bean that owns
   *            the property. This is the start of our lookup.
   * @param     pageContext
   *            The pageContext where we look for the locale information.
   * @throws    IllegalArgumentException
   *            (property == null)
   *            || (property.length() <= 0)
   *            || (* no class with name type *);
   */
  public static String i18nShortPropertyLabel(final String property,
                                              final String type,
                                              final PageContext pageContext) {
    JSTLResourceBundleLoadStrategy strategy =
        new JSTLResourceBundleLoadStrategy();
    strategy.setPageContext(pageContext);
    Class typeClass;
    try {
      typeClass = Class.forName(type);
    }
    catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Class " //$NON-NLS-1$
                                         + type
                                         + " not found"); //$NON-NLS-1$
    }
    return Properties.i18nPropertyLabel(property, typeClass, true, strategy);
  }

  /**
   * <p>Return a label for a type <code>type</code>. The
   *   {@link JSTLResourceBundleLoadStrategy} is used to lookup
   *   the correct resource bundle, dependent on the JSTL
   *   <code>fmt:</code> locale.</p>
   *
   * @param     type
   *            The fully qualified name of the type of bean that owns
   *            the property. This is the start of our lookup.
   * @param     pageContext
   *            The pageContext where we look for the locale information.
   * @throws    IllegalArgumentException
   *            || (* no class with name type *);
   */
  public static String i18nTypeLabel(final String type,
                                     final PageContext pageContext) {
    JSTLResourceBundleLoadStrategy strategy =
        new JSTLResourceBundleLoadStrategy();
    strategy.setPageContext(pageContext);
    Class typeClass;
    try {
      typeClass = Class.forName(type);
    }
    catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Class " //$NON-NLS-1$
                                         + type
                                         + " not found"); //$NON-NLS-1$
    }
    return Properties.i18nTypeLabel(typeClass, false, strategy);
  }


  /**
   * <p>Return a label for a type <code>type</code>, in plural form.
   *   The {@link JSTLResourceBundleLoadStrategy} is used to lookup
   *   the correct resource bundle, dependent on the JSTL
   *   <code>fmt:</code> locale.</p>
   *
   * @param     type
   *            The fully qualified name of the type of bean that owns
   *            the property. This is the start of our lookup.
   * @param     pageContext
   *            The pageContext where we look for the locale information.
   * @throws    IllegalArgumentException
   *            || (* no class with name type *);
   */
  public static String i18nPluralTypeLabel(final String type,
                                           final PageContext pageContext) {
    JSTLResourceBundleLoadStrategy strategy =
        new JSTLResourceBundleLoadStrategy();
    strategy.setPageContext(pageContext);
    Class typeClass;
    try {
      typeClass = Class.forName(type);
    }
    catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Class " //$NON-NLS-1$
                                         + type
                                         + " not found"); //$NON-NLS-1$
    }
    return Properties.i18nTypeLabel(typeClass, true, strategy);
  }

  private static final String HTML_BLANK_RULE = "<br/>"; //$NON-NLS-1$

  /**
   * Converts all line seperators in <param>string</param> into blank rule html
   * entities.
   *
   * @param     string
   *            The string to convert.
   * @return    Converted string
   */
  public static String nl2br(final String string) {
    // Replace all carriage return line feeds with <br/>
    String result = string.replaceAll(System.getProperty("line.separator"), //$NON-NLS-1$
                                      HTML_BLANK_RULE);
    return result;
  }

  /**
   * Converts all blank rule html entities in <param>string</param>
   * into a line seperators.
   *
   * @param     string
   *            The string to convert.
   * @return    Converted string
   */
  public static String br2nl(final String string) {
    // Replace all <br/> with carriage return line feeds
    String result = string.replaceAll(HTML_BLANK_RULE,
                                      System.getProperty("line.separator")); //$NON-NLS-1$
    return result;
  }

  /**
   * Perform a JNDI lookup with a specified lookupString.
   *
   * @param     name
   *            The name identifying the lookup to perform.
   * @throws    NamingException
   *            Exception signalling a lookup failure.
   * @return    String
   *            The value looked up, or null if not found.
   */
  public static String jndiLookup(final String name)
      throws NamingException {
    Context initCtx = new InitialContext();
    Context envCtx = (Context)initCtx.lookup("java:comp/env"); //$NON-NLS-1$
    return (String)envCtx.lookup(name);
  }

}
