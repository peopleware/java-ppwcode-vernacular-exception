package be.peopleware.value_II.hibernate;


import be.peopleware.value_II.Country;
import be.peopleware.value_II.CountryEditor;


/**
 * Hibernate mapping for {@link Country}. Country ISO codes
 * are stored in a VARCHAR.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */

public final class CountryUserType extends AbstractEnumerationUserType {

  /*<section name="Meta Information">*/
  //  ------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/


  public CountryUserType() {
    super(new CountryEditor());
  }

}