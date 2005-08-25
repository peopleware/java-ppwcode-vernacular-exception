/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II.servlet;


/**
 * <p>Objects of this type can signal that they want to be removed from
 *   a collection. They are queried at some times, and if {@link #isToBeRemoved()}
 *   return <code>true</code>, they will be removed from that collection.</p>
 * <p>The {@link MopupListener} applies this to all variables in
 *   session scope of a web application at the end of a HTTP request.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public interface Removable {

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
   * Return <code>true</code> if <code>this</this</code> wants to be removed
   * from the collection at this time.
   */
  boolean isToBeRemoved();

}



