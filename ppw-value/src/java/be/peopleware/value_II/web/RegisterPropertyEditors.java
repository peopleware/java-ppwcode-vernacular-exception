/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II.web;


import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.value_II.Values;


/**
 * This listener is called on startup and shut down of a web application, to register
 * this library in the {@link PropertyEditorManager} lookup path.
 * This listener is to be registered with the web application in the
 * <kbd>web.xml</kbd> file.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class RegisterPropertyEditors implements ServletContextListener {

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

  private static final Log LOG =
      LogFactory.getLog(RegisterPropertyEditors.class);


  /**
   * Register this library in the {@link PropertyEditorManager}
   * {@link PropertyEditor} lookup path.
   */
  public void contextInitialized(final ServletContextEvent event) {
    LOG.debug("registering ppw-value library in PropertyEditorManager "
              + "PropertyEditor lookup path");
    Values.registerPropertyEditors();
    if (LOG.isDebugEnabled()) {
      LOG.debug("PropertyEditorManager PropertyEditor lookup path: "
                + PropertyEditorManager.getEditorSearchPath());
    }
  }

  /**
   * Notification that the servlet context is about to be shut down.
   * No operation.
   *
   * @post  true;
   */
  public void contextDestroyed(final ServletContextEvent event) {
    // NOP
  }


}
