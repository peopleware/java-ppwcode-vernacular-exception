package be.peopleware.servlet_I.hibernate;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import be.peopleware.exception_I.TechnicalException;


/**
 * This listener is called on startup and shut down of a web application.
 * This listener is to be registered with the web application in the
 * <kbd>web.xml</kbd> file.
 * The listener creates a Hibernate {@link SessionFactory} from a
 * configuration file on startup and stores it in the application
 * scope of the web application. The listener closes the Hibernate
 * {@link SessionFactory} when the web application shuts down.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class SessionFactoryController
    implements ServletContextListener {

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

  private static final Log LOG
      = LogFactory.getLog(SessionFactoryController.class);

  private static SessionFactory _sessionFactory;

  /**
   * <p>
   * The name under which the Hibernate session factory is stored as an
   * application scope attribute.
   * </p>
   * <p>
   * <strong><code>= SessionFactoryController.class.getName()
            + ".SESSION_FACTORY"</code></strong>
   * </p>
   */
  public static final String SESSION_FACTORY_ATTRIBUTE_NAME
      = SessionFactoryController.class.getName()
            + ".SESSION_FACTORY"; //$NON-NLS-1$

  /**
   * Create a Hibernate {@link SessionFactory}from a configuration file, and
   * store it in the web application scope as attribute with name
   * {@link #SESSION_FACTORY_ATTRIBUTE_NAME}. This object is cleaned up by
   * {@link #contextDestroyed(ServletContextEvent)}.
   */
  public void contextInitialized(final ServletContextEvent event) {
    Configuration config = new Configuration();
    try {
      if (LOG.isDebugEnabled()) {
        LOG.debug("creating Hibernate SessionFactory ..."); //$NON-NLS-1$
      }
      config.configure(); // @idea (jand): use config param to know which file
      _sessionFactory = config.buildSessionFactory();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Hibernate SessionFactory created"); //$NON-NLS-1$
      }
      event.getServletContext().setAttribute(SESSION_FACTORY_ATTRIBUTE_NAME,
                                             _sessionFactory);
    }
    catch (HibernateException hExc) {
      LOG.fatal("failed to initialize Hibernate SessionFactory", //$NON-NLS-1$
                 hExc);
    }
  }

  /**
   * Cleanup the Hibernate {@link SessionFactory}that is stored in the web
   * application scope as attribute with name
   * {@link #SESSION_FACTORY_ATTRIBUTE_NAME}. It was created by
   * {@link #contextInitialized(ServletContextEvent)}.
   */
  public void contextDestroyed(final ServletContextEvent event) {
    try {
      if (LOG.isDebugEnabled()) {
        LOG.debug("closing Hibernate SessionFactory ..."); //$NON-NLS-1$
      }
      _sessionFactory.close();
      _sessionFactory = null;
      if (LOG.isDebugEnabled()) {
        LOG.debug("Hibernate SessionFactory closed"); //$NON-NLS-1$
      }
    }
    catch (HibernateException hExc) {
      LOG.error("failed to deinitialize Hibernate SessionFactory", hExc); //$NON-NLS-1$
    }
    event.getServletContext().removeAttribute(SESSION_FACTORY_ATTRIBUTE_NAME);
  }

  /**
   * This will return null if there is no session factory
   * in the application scope.
   *
   * @param     servletContext
   *            The application scope into which to look for the session
   *            factory.
   * @pre       servletContext != null;
   * @return    servletContext.getAttribute(SESSION_FACTORY_ATTRIBUTE_NAME);
   * @throws    TechnicalException
   *            servletContext.getAttribute(SESSION_FACTORY_ATTRIBUTE_NAME)
   *                == null;
   */
  public static SessionFactory getSessionFactory(final ServletContext servletContext)
      throws TechnicalException {
    assert servletContext != null;
    SessionFactory factory
        = (SessionFactory)servletContext.getAttribute(SESSION_FACTORY_ATTRIBUTE_NAME);
    if (factory == null) {
      throw new TechnicalException("Hibernate session factory not found in servlet context", //$NON-NLS-1$
                                   null);
    }
    return factory;
  }

  /**
   * This will return null if there is no SessionFactory available.
   */
  public static SessionFactory getSessionFactory() {
   return _sessionFactory;
  }

}
