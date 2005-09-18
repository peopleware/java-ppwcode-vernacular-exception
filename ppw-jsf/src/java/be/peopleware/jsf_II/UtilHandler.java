/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II;


import java.security.Principal;
import java.util.Date;

import javax.faces.FacesException;


/**
 * Handler to be used in application scope, to offer all kinds of general
 * things. Mostly, this makes stuff from {@link RobustCurrent}
 * available through a managed bean.

 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public class UtilHandler {

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


//  private static final Log LOG = LogFactory.getLog(UtilHandler.class);


  /**
   * @return new Date();
   */
  public Date getNow() {
    return new Date();
  }

  /**
   * @return RobustCurrent.principal();
   * @except RobustCurrent.principal();
   */
  public Principal getPrincipal() throws FacesException {
    return RobustCurrent.principal();
  }

}



