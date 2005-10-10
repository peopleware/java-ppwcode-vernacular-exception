/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II.hibernate;


import be.peopleware.value_II.Gender;
import be.peopleware.value_II.GenderEditor;


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

  /**
   * Create a new {@link GenderUserType}.
   *
   * @post new.getEnumerationValueEditor() instanceof GenderEditor;
   */
  public GenderUserType() {
    super(new GenderEditor());
  }

}
