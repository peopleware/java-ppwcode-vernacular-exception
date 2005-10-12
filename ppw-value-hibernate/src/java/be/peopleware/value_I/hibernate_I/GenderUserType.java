/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_I.hibernate_I;


import be.peopleware.value_I.Gender;
import be.peopleware.value_I.GenderEditor;


/**
 * Hibernate mapping for {@link Gender}. A single letter
 * code is stored in a VARCHAR.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 * 
 * @deprecated This package is completely deprecated. It is absorbed by
 *             library ppw-value I, from version 1.2.0/2.0 on.
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