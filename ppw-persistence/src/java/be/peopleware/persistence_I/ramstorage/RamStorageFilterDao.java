/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_I.ramstorage;


import java.util.List;
import java.util.Set;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_I.dao.FilterDao;


/**
 * A {@link FilterDao} for ram storage.
 *
 * @author nsmeets
 * @author ashoudou
 * @author Peopleware n.v.
 */
public final class RamStorageFilterDao extends RamStorageAsyncCrudDao implements FilterDao {

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


//  private static final Log LOG = LogFactory.getLog(RamStorageFilterDao.class);

  /* <construction> */
  //------------------------------------------------------------------

  /**
   *
   */
  public RamStorageFilterDao() {
    // NOP
  }

  /* </construction> */

  /**
   * @see    FilterDao
   * @mudo
   */
  public final Set retrievePersistentBeans(final Class type, final List criteriaList)
      throws TechnicalException {
    assert false : "not implemented yet";
		return null;
	}
}
