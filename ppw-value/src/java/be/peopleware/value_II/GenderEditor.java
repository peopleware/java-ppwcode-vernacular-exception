package be.peopleware.value_II;


import java.util.Map;


/**
 * A property editor for properties of type {@link Gender}.
 * This editor is choosen automatically when needed, because it is in the
 * same package as the type it is for, with the expected name.
 *
 * <p>Only get and set as text is supported for now. It is all
 *   that is needed for beans used in web application.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public final class GenderEditor extends ResourceBundleBasedEnumerationValueEditor {

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



  /*<property name="valueMap">*/
  //------------------------------------------------------------------

  /**
   *
   */
  public Map getValuesMap() {
    return Gender.VALUES;
  }

}
