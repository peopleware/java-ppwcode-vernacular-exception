package be.peopleware.servlet_I.hibernate;


import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import net.sf.hibernate.FlushMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;


/**
 * This listener is called on initialize and destroy of a request. This listener
 * is to be registered with the web application in the <kbd>web.xml</kbd> file.
 * The listener creates a Hibernate {@link Session} from the
 * {@link SessionFactory} created in {@link SessionFactoryController} on request
 * initialization and stores it in the request scope. The listener closes the
 * Hibernate {@link Session} when the request is destroyed.
 *
 * This listeren depends on the existence of a {@link SessionFactory} in
 * a attribute in application scope with name
 * {@link SessionFactoryController#SESSION_FACTORY_ATTRIBUTE_NAME}.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class SessionInView implements ServletRequestListener, Serializable {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /* </section> */

  private static final Log LOG = LogFactory.getLog(SessionInView.class);

  /**
   * <p>
   * The name under which the Hibernate session is stored as a
   * request scope attribute.
   * </p>
   * <p>
   * <strong>= SessionInView.class.getName() + ".SESSION";</strong>
   * </p>
   */
  public static final String SESSION_ATTRIBUTE_NAME =
      SessionInView.class.getName()
         + ".SESSION"; //$NON-NLS-1$

  /**
   * Create a Hibernate session, and store in the request scope
   * as attribute with name {@link #SESSION_ATTRIBUTE_NAME}.
   * This is a {@link LazyAttribute}.
   */
  public void requestInitialized(final ServletRequestEvent event) {
    event.getServletRequest().setAttribute(SESSION_ATTRIBUTE_NAME,
                                           new LazyAttribute() {
        protected Object initialValue() {
          Session session = null;
          try {
            if (LOG.isDebugEnabled()) {
              LOG.debug("creating Hibernate Session ..."); //$NON-NLS-1$
            }
            SessionFactory factory = SessionFactoryController
                .getSessionFactory(event.getServletContext());
            try {
              session = factory.openSession();
              if (LOG.isDebugEnabled()) {
                LOG.debug("Hibernate Session created"); //$NON-NLS-1$
              }
              // Setting auto flush on commit. This fixes the flush on exception
              // error we encountered when trying to insert a duplicate value.
              // Possible values: AUTO, COMMIT and NEVER.
              session.setFlushMode(FlushMode.COMMIT);
            }
            catch (HibernateException hExc) {
              LOG.fatal("failed to initialize Hibernate Session", //$NON-NLS-1$
                         hExc);
            }
          }
          catch (TechnicalException tExc) {
            LOG.fatal("no Hibernate SessionFactory found; " //$NON-NLS-1$
                           + "could not initialize session", //$NON-NLS-1$
                       tExc);
          }
          return session;
        }
    });
  }

  /**
   * Remove a Hibernate session from the request scope
   * attribute with name {@link #SESSION_ATTRIBUTE_NAME}.
   */
  public void requestDestroyed(final ServletRequestEvent event) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("closing Hibernate Session ..."); //$NON-NLS-1$
    }
    Session session;
    try {
      LazyAttribute la = getLA(event.getServletRequest());
          // TechnicalException
      if (la.isInitialized()) {
        session = getSession(event.getServletRequest());
            // TechnicalException
        event.getServletContext().removeAttribute(SESSION_ATTRIBUTE_NAME);
        if (session.isOpen()) {
          try  {
            session.close();
            if (LOG.isDebugEnabled()) {
              LOG.debug("Hibernate Session flushed and closed"); //$NON-NLS-1$
            }
          }
          catch (HibernateException hExc) {
            LOG.error("failed to deinitialize Hibernate Session", //$NON-NLS-1$
                       hExc);
          }
        }
        else {
         LOG.error("Hibernate Session already closed"); //$NON-NLS-1$
        }
      }
      else {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Hibernate Session was never initialized; ok"); //$NON-NLS-1$
        }
      }
    }
    catch (TechnicalException tExc) {
      LOG.error("NO Hibernate Session to close", tExc); //$NON-NLS-1$
    }
  }

  /**
   * This will return null if there is no session in the request scope.
   *
   * @param     request
   *            The request to look for the session in.
   * @pre       request != null;
   * @result    request.getAttribute(SESSION_ATTRIBUTE_NAME).getValue();
   * @result    result != null;
   * @throws    TechnicalException
   *            request.getAttribute(SESSION_ATTRIBUTE_NAME) == null;
   * @throws    TechnicalException
   *            !request.getAttribute(SESSION_ATTRIBUTE_NAME)
   *                instanceof LazyAttribute;
   * @throws    TechnicalException
   *            !request.getAttribute(SESSION_ATTRIBUTE_NAME).getValue()
   *                instanceof Session;
   */
  public static Session getSession(final ServletRequest request)
      throws TechnicalException {
    assert request != null;
    Session session = null;
    try {
      session = (Session)getLA(request).getValue(); // does init when needed
    }
    catch (ClassCastException ccExc) {
      throw new TechnicalException("value of attribute " //$NON-NLS-1$
                                       + SESSION_ATTRIBUTE_NAME
                                       + " is not a Hibernate Session", //$NON-NLS-1$
                                   ccExc);
    }
    assert session != null;
    return session;
  }

  /**
   * @result    result.getValue() instanceof Session;
   * @result    result != null;
   * @throws    TechnicalException
   *            request.getAttribute(SESSION_ATTRIBUTE_NAME) == null;
   * @throws    TechnicalException
   *            !request.getAttribute(SESSION_ATTRIBUTE_NAME)
   *                instanceof LazyAttribute;
   */
  private static LazyAttribute getLA(final ServletRequest request)
      throws TechnicalException {
    assert request != null;
    LazyAttribute la = null;
    try {
      la = (LazyAttribute)request.getAttribute(SESSION_ATTRIBUTE_NAME);
    }
    catch (ClassCastException ccExc) {
      throw new TechnicalException("attribute " //$NON-NLS-1$
                                       + SESSION_ATTRIBUTE_NAME
                                       + " is not a LazyAttribute", //$NON-NLS-1$
                                   ccExc);
    }
    if (la == null) {
      throw new TechnicalException("no lazy attribute for the Hibernate " //$NON-NLS-1$
                                       + "session found in the request scope", //$NON-NLS-1$
                                   null);
    }
    assert la != null;
    return la;
  }

}
