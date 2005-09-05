/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_II.ramstorage;


import be.peopleware.persistence_II.dao.Dao;


/**
 * Support methods for RamStorage <acronym title="Data Access Object">DAO<acronym>'s.
 * These implementations need a {@link RamStorage}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public class AbstractRamStorageDao implements Dao {

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


  /**
   * @basic
   */
  public final RamStorage getRamStorage() {
    return $rs;
  }

  /**
   * @post getRamStorage() == rs;
   */
  public final void setRamStorage(RamStorage rs) {
    $rs = rs;
  }
  
  /**
   * @invar $rs != null;
   */
  private RamStorage $rs;

}
