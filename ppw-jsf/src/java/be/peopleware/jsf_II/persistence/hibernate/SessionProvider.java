/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.persistence.hibernate;


import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.servlet_I.hibernate.SessionInView;


/**
 * Utility class whose instances can locate {@link Session Hibernate Sessions}
 * in different application contexts (web, rich client, &hellip;).
 * This is merely a bean version of
 * {@link SessionInView#getSession(javax.servlet.ServletRequest)} for JSF,
 * for use in <kbd>faces-config.xml</kbd>. Since this class is stateless,
 * we can declare it in application scope.
 * <pre>
 * <listener>
 *   <listener-class>be.peopleware.servlet_I.hibernate.SessionFactoryController</listener-class>
 *   <listener-class>be.peopleware.servlet_I.hibernate.SessionInView</listener-class>
 * </listener>
 * </pre>
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public class SessionProvider {
  
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

  
  private static final Log LOG = LogFactory.getLog(SessionProvider.class);

  
  /**
   * Get the Hibernate {@link Session} from {@link SessionInView}.
   * 
   * @result result != null;
   * @throws TechnicalException
   *         ; cannot provide Session
   */
  public final Session getRequestSession() throws TechnicalException, FatalFacesException {
    LOG.debug("Looking for Hibernate Session in request");
    Session result = SessionInView.getSession(RobustCurrent.httpServletRequest());
    LOG.debug("Found Hibernate Session in request: " + result);
    return result;
  }

}
