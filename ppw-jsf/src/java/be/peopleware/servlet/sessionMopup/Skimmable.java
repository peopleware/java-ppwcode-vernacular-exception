/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.servlet.sessionMopup;


import java.io.Serializable;


/**
 * <p>Objects of this type have data that should be cleared at certain times
 *   or under certain conditions (the fat should be skimmed of them). At
 *   certain times, {@link #skim()} will be called on objects of this type.
 *   The implementation of this method then decides if skimming is appropriate
 *   at this time, given the state of <code>this</code>instance, what should
 *   be skimmed, and what <def>skimming</def> entails in these circumstances.
 *   Potentially, the implementation can cascade the skimming request to related
 *   objects.</p>
 * <p>The {@link MopupListener} applies this to all variables in
 *   session scope of a web application at the end of a HTTP request.</p>
 * <p>If an instance of this type is also {@link Serializable} it is good
 *   practice to surely skim transient fields.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public interface Skimmable {

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
   * Skim unneeded data from this instance, if applicable at this time.
   */
  void skim();

}



