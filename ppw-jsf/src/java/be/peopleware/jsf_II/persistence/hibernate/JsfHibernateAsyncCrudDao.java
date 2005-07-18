package be.peopleware.jsf_II.persistence.hibernate;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.hibernate.HibernateAsyncCrudDao;
import be.peopleware.servlet_I.hibernate.SessionInView;


/**
 * A Hibernate Asynchronous CRUD class wich depends on Java Server Faces for retrieval
 * of a Hibernate Session.
 *
 * @author     David Van Keer
 * @author     Peopleware n.v.
 */
public class JsfHibernateAsyncCrudDao extends HibernateAsyncCrudDao {

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


  private static final Log LOG = LogFactory.getLog(JsfHibernateAsyncCrudDao.class);

  /**
   * @basic
   */
  public Session getSession() {
    if ($session == null || !$session.isOpen()) {
      try {
        // @mudo (nsmeets) Hier stond vroeger:
        // ServletRequest sr = Util.getServletRequest();
        ServletRequest sr = RobustCurrent.httpServletRequest();
        $session = SessionInView.getSession(sr);
        // @mudo (nsmeets) Nodig om te zetten? Di is toegevoegd omdat in de code
        // van FamilyDoctorsListHandler.select tomcat klaagde dat na startTransaction
        // er geen sessie was. De test is toegevoegd omdat setSession een exceptie
        // gooit als je een sessie wil zetten als een transactie bezig is.
        LOG.debug("acquired Hibernate Session from SessionInView");
      }
      catch (TechnicalException tExc) {
        LOG.fatal("could not retrieve Hibernate Session", tExc);
        forceLogOut();
        // null Session will be detected later on, and TechnicalException will be thrown then
      }
    }
    return $session;
  }


  /**
   * A helper method to force a user logout. Is simply invalides the current HTTP session
   * of the current user.
   */
  private static void forceLogOut() {
    LOG.fatal("invalidating session");
    // @mudo hier stond vroeger
    // HttpSession session = Util.getHttpSession();
    HttpSession session = RobustCurrent.httpSession();
    if (session != null) {
      session.invalidate();
    }
  }

}
