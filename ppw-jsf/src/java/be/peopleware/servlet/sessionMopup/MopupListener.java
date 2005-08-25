package be.peopleware.servlet.sessionMopup;


import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Mopup information in session scope on the destruction of a HTTP request. For
 *   all variables in session scope, first we check if they are
 *   {@link Removable#isToBeRemoved() to be removed}.
 *   If so, the variable is removed from session scope. If not, we check if they
 *   are {@link Skimmable}. If so, they are {@link Skimmable#skim() skimmed}.</p>
 * <p>It makes no sense to do this for request scope, since the entire request
 *   scope will be discarded at the end of the HTTP request. It makes no sense
 *   to do this for application scope, since objects in application scope
 *   should not depend on the actions of a specific user.</p>
 * <p>This listener is called on initialize and destroy of a request. This listener
 *   is to be registered with the web application in the <kbd>web.xml</kbd> file.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class MopupListener implements ServletRequestListener, Serializable {

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

  private static final Log LOG = LogFactory.getLog(MopupListener.class);

  // Serializability ok: no instance state

  /**
   * Do nothing
   */
  public void requestInitialized(final ServletRequestEvent event) {
    // NOP
  }

  /**
   * Check all session scope variables. If the value of the variable
   * implements {@link Removable}, remove the variable from session scope if
   * {@link Removable#isToBeRemoved()} returns <code>true</code>. If the
   * variable is not removed, check if the value of the variable implements
   * {@link Skimmable}. If so, {@link Skimmable#skim()} that object.
   */
  public void requestDestroyed(final ServletRequestEvent event) {
    LOG.debug("starting after-HTTP-request mopup");
    try {
      HttpServletRequest request = (HttpServletRequest)event.getServletRequest();
          // ClassCastException
      assert request != null : "request cannot be null";
      HttpSession session = request.getSession(false);
      if (session != null) {
        List attributeNames = new LinkedList();
        { /* put all the names in a list; copy is needed to avoid
             concurrent modification problems if things are removed
             while we are iterating over the set */
          Enumeration enumeration = session.getAttributeNames();
              // IllegalStateException if session is invalidated
          while (enumeration.hasMoreElements()) {
            attributeNames.add(enumeration.nextElement());
              // certainly a String
          }
        }
        { /* iterate over the names, get the value; if the value
             is instance of Removable, ask it if it wants to be removed;
             if so, remove. If it is not Removable or doesn't want
             to be removed, check if it is Skimmable; if so,
             skim it */
          Iterator iter = attributeNames.iterator();
          while (iter.hasNext()) {
            String attrName = (String)iter.next();
            Object value = session.getAttribute(attrName);
              // IllegalStateException if session is invalidated
            if (value != null) {
              if ((value instanceof Removable) && ((Removable)value).isToBeRemoved()) {
                session.removeAttribute(attrName);
                  // IllegalStateException if session is invalidated
              }
              else if (value instanceof Skimmable) {
                ((Skimmable)value).skim();
              }
            }
          }
        }
      }
      else {
        LOG.debug("no session found; nothing to do");
      }
    }
    catch (ClassCastException ccExc) {
      LOG.fatal("Servlet request was not an HttpServletRequest. This listener " +
                "only makes sense in the HTTP context, since it works on " +
                "session scope.");
    }
    catch (IllegalStateException isExc) {
      // NOP; session was invalidated and will be removed anyway
    }
  }

}
