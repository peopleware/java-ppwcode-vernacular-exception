/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.persistence.hibernate;


import java.io.Serializable;
import java.sql.Connection;

import javax.servlet.ServletRequest;

import net.sf.hibernate.HibernateException;
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
public class SessionProvider implements Serializable {

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
   * Return the Hibernate {@link Session} from {@link SessionInView}.
   *
   * @return    SessionInView.getSession(RobustCurrent.httpServletRequest());
   * @throws    FatalFacesException
   *            RobustCurrent.httpServletRequest();
   * @throws    FatalFacesException
   *            SessionInView.getSession(RobustCurrent.httpServletRequest()) / TechnicalException;
   */
  public Session getSession() throws FatalFacesException {
    Session session = null;
    try {
      ServletRequest sr = RobustCurrent.httpServletRequest();
      session = SessionInView.getSession(sr);
      LOG.debug("acquired Hibernate Session from SessionInView");
    }
    catch (TechnicalException tExc) {
      RobustCurrent.fatalProblem("Could not retrieve Hibernate Session", tExc, LOG);
    }
    return session;
  }

  public Connection getConnection() throws FatalFacesException {
    Connection result = null;
    try {
      result = getSession().connection();
    }
    catch (HibernateException e) {
      RobustCurrent.fatalProblem("could not get connection from Hibernate session", e, LOG);
    }
    return result;
  }

}
