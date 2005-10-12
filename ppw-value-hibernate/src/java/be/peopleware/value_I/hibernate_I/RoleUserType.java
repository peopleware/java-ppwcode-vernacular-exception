/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_I.hibernate_I;


import be.peopleware.value_I.Role;
import be.peopleware.value_I.RoleEditor;


/**
 * Hibernate mapping for {@link Role}. A single letter code is stored in
 * a VARCHAR.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Peopleware n.v.
 * 
 * @deprecated This package is completely deprecated. It is absorbed by
 *             library ppw-value I, from version 1.2.0/2.0 on.
 */
public final class RoleUserType extends AbstractEnumerationUserType {

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

  public RoleUserType() {
    super(new RoleEditor());
  }

}