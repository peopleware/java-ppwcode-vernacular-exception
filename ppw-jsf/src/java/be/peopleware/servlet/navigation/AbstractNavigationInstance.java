/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.servlet.navigation;


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



  /*<property name="LastRenderedTime">*/
  //------------------------------------------------------------------

  /**
   * Returns the last time the corresponding page is rendered.
   *
   * @basic
   */
  public final Date getLastRenderedTime() {
    return $lastRenderedTime;
  }

  /**
   * @post new.getTime().equals(NOW);
   */
  protected void resetLastRenderedTime() {
    $lastRenderedTime = new Date();
  }

  /**
   * @invar $lastRenderedTime != null;
   */
  private Date $lastRenderedTime = new Date();

  /*</property>*/

}



