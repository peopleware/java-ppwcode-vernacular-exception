package be.peopleware.value_I.hibernate;


import be.peopleware.value_I.Gender;
import be.peopleware.value_I.GenderEditor;


/**
 * Hibernate mapping for {@link Gender}. A single letter
 * code is stored in a VARCHAR.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public final class GenderUserType extends AbstractEnumerationUserType {

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

  public GenderUserType() {
    super(new GenderEditor());
  }

}