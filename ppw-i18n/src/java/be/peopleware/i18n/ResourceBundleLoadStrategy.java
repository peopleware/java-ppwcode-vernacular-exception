package be.peopleware.i18n;


import java.util.ResourceBundle;


/**
 * <p>Strategy pattern to load i18n resource bundles.</p>
 * <p>The stratey to find a resource bundle in an i18n context is different in
 *   different deployments environments. E.g., the strategy is different for
 *   general Swing applications, JSTL <code>fmt</code> tags, and Struts.</p>
 * <p> Classes that need to access resources based on a
 *   {@link java.util.Locale locale}, can use an instance of this strategy at
 *   runtime to have it load a resource bundle for them.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public interface ResourceBundleLoadStrategy {


  /* <section name="Meta Information"> */
  //------------------------------------------------------------------
  /** {@value} */
  String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /* </section> */


  /**
   * Try to load the resource bundle with name <code>basename</code>,
   * according to the strategy implemented in this type. If no matching
   * resource bundle can be found with this strategy, <code>null</code>
   * is returned.
   *
   * @param     basename
   *            The basename of the resource bundle that should be loaded.
   * @result    (basename == null) ==> (result == null);
   * @result    (basename.equals("")) ==> (result == null);
   * @result    ; if there is no matching resource bundle,
   *            <code>result == null</code>
   */
  ResourceBundle loadResourceBundle(final String basename);

}
