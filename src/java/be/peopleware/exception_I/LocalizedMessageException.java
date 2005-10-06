package be.peopleware.exception_I;


import be.peopleware.i18n_I.DefaultResourceBundleLoadStrategy;
import be.peopleware.i18n_I.Properties;
import be.peopleware.i18n_I.ResourceBundleLoadStrategy;


/**
 * <p>Support class for localized messages in Exception.</p>
 * <p>Since JDK 1.4, throwables have a method that returns a
 *   localized message. There is however no implemented support for this.
 *   This class adds such support.</p>
 * <p>Localized messages are returned from a resource bundle with
 *   basename {@link #getLocalizedMessageResourceBundleBasename()}.
 *   The resource bundle with this base name is recovered using the
 *   {@link #getLocalizedMessageResourceBundleLoadStrategy() actual resource
 *   bundle load strategy} (the default is
 *   {@link DefaultResourceBundleLoadStrategy})
 *   The localized message that is returned by {@link #getLocalizedMessage()}
 *   is looked up in this resource bundle using keys
 *   {@link #getLocalizedMessageKeys()}.
 *   The keys are tried in order. The first one that gives a result,
 *   is used.
 *   If this fails, we try to load the resource bundle with base name
 *   <code>getClass().getName()</code> with the same load strategy,
 *   and look up the keys there.
 *   If this fails, the {@link #getMessage() non-localized message}
 *   is returned.
 * <p>Subclasses provide an actual base name and key.</p>
 *
 * @invar     getLocalizedMessageKeys() != null
 *                ==> (forall int i; i >= 0
 *                     && i < getLocalizedMessageKeys().length;
 *                        getLocalizedMessageKeys()[i] != null
 *                        && !getLocalizedMessageKeys()[i].equals(""));
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @idea (jand): Add functionality to use properties of the exception
 *               as arguments in the localized message, e.g.,
 *               <samp>The {origin} had validation errors.</samp>
 */
public abstract class LocalizedMessageException extends Exception {

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
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   *
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   */
  public LocalizedMessageException(final String message,
                                   final Throwable cause) {
    super(message, cause);
  }

  /*</construction>*/



  /*<property name="localizedMessageResourceBundleLoadStrategy">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final ResourceBundleLoadStrategy
      getLocalizedMessageResourceBundleLoadStrategy() {
    return $localizedMessageResourceBundleLoadStrategy;
  }

  /**
   * @param     strategy
   *            The new resource bundle load strategy.
   * @post    new.getLocalizedMessageResourceBundleLoadStrategy() == strategy;
   */
  public final void setLocalizedMessageResourceBundleLoadStrategy(
      final ResourceBundleLoadStrategy strategy) {
    $localizedMessageResourceBundleLoadStrategy = strategy;
  }

  ResourceBundleLoadStrategy $localizedMessageResourceBundleLoadStrategy
      = new DefaultResourceBundleLoadStrategy();

  /*</property>*/



  /*<property name="localizedMessageResourceBundleBasename">*/
  //------------------------------------------------------------------

  /**
   * This implementation returns <code>null</code>. As a result, the key is
   * looked up in the bundle with base name <code>getClass().getName()</code>.
   *
   * @basic
   */
  public String getLocalizedMessageResourceBundleBasename() {
    return null;
  }

  /*</property>*/



  /*<property name="localizedMessageKeys">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public abstract String[] getLocalizedMessageKeys();

  /*</property>*/



  /**
   * Return the a label from the
   * {@link #getLocalizedMessageResourceBundleBasename()} resource
   * bundle with keys {@link #getLocalizedMessageKeys()}, using the
   * resoure bundle load strategy
   * {@link #getLocalizedMessageResourceBundleLoadStrategy()}.
   * The keys are tried in order. The first one that gives a result,
   * is used.
   * If this fails, we try to load a resource with name
   * <code>getClass().getName()</code>, with the same resource
   * bundle load strategy and look up the same keys.
   * If there is no load strategy, or the bundles could not be found,
   * or there is no entry in the bundles with the given keys, the
   * non-localized message is returned.
   */
  public final String getLocalizedMessage() {
    ResourceBundleLoadStrategy strategy
        = getLocalizedMessageResourceBundleLoadStrategy();
    String[] keys = getLocalizedMessageKeys();
    String result;
    if ((strategy == null) || (keys == null) || keys.length <= 0) {
      result = getMessage();
    }
    else {
      result = Properties.findKeyWithBasename(getLocalizedMessageResourceBundleBasename(),
                                              keys,
                                              getLocalizedMessageResourceBundleLoadStrategy());
      if (result == null) {
        result = Properties.findKeyWithBasename(getClass().getName(),
                                                keys,
                                                getLocalizedMessageResourceBundleLoadStrategy());
      }
      if (result == null) {
        result = super.getMessage();
      }
    }
    return result;
  }

}