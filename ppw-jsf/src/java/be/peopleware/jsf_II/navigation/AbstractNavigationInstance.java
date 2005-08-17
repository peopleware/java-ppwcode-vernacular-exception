/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II.navigation;


import java.util.Date;


/**
 * <p>Common code for {@link NavigationInstance NavigationInstances}.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public abstract class AbstractNavigationInstance implements NavigationInstance {

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



  /*<property name="time">*/
  //------------------------------------------------------------------

  public final Date getTime() {
    return $endTime;
  }

  /**
   * @post new.getTime().equals(NOW);
   */
  protected void resetTime() {
    $endTime = new Date();
  }

  /**
   * @invar $endTime != null;
   */
  private Date $endTime = new Date();

  /*</property>*/

}



