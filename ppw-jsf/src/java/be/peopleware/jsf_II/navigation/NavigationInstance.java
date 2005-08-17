/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II.navigation;


import java.util.Date;

import javax.faces.FacesException;


/**
 * <p>Keep the information about a step in the navigation through a
 *   web application. Instances of this type are used in a
 *   {@link NavigationStack}.</p>
 * <p>This interface does not concern itself with the exact definition
 *   of a navigation instance, or how to return to a previous state, or what to
 *   do in case of problems. This is a thin interface, and
 *   different implementations can be used for different strategies. This package
 *   offers a number of concrete implementations, for use cases that are
 *   supported elsewhere in this library, but application developers can develop
 *   more.</p>
 * <p>Possbible implementations are not limited to JSF navigation, although
 *   the concrete implementations in this package do depend on JSF.</p>
 * <p>Absorption is inspired by {@link javax.swing.undo.UndoableEdit}.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public interface NavigationInstance {

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


  /*<property name="time">*/
  //------------------------------------------------------------------

  /**
   * @basic
   * @init NOW;
   */
  Date getTime();

  /*</property>*/


  /**
   * Join the state of this <code>NavigationInstance</code> and <code>ni</code>
   * into <code>result</code>. This is typically called on an instance
   * on top of the {@link NavigationStack}, with a next <code>NavigationInstance</code>.
   * If this succeeds, the current top of the stack is replaced with the result
   * of this operation, and <code>ni</code> is not placed on the stack. This
   * method may return <code>this</code>, possibly
   * with changed state to take into account the <code>ni</code> navigation.
   * If absorption is not possible, return <code>null</code>. When <code>ni</code> is
   * <code>null</code>, return <code>null</code>.
   *
   * @result (ni == null) ? (result == null);
   * @post getTime().equals(NOW);
   */
  NavigationInstance absorb(NavigationInstance ni);

  /**
   * Navigate back to this navigation instance.
   */
  void goBack() throws FacesException;


//  /**
//   * <p>Set the {@link InstanceHandler#setId(Long) id} of
//   * {@link #getFromHandler()} to {@link #getFromPersistentBeanId()}, tell it to
//   * {@link InstanceHandler#reset() reset}, and <strong>HTTP redirect</strong> to
//   * {@link #getFromLink()}.</p>
//   *
//   * <p>This should should result in the browser requesting the
//   * {@link #getFromViewId() from JSF page} from the web app. This page should use
//   * {@link #getFromHandler()} as its handler, which was just prepared
//   * for such a request.</p>
//   *
//   * <p>This method should be used as an <dfn>action method</dfn> like</p>
//   * <pre>
//   * &lt;h:commandButton id=&quot;<var>backButtonId</var>&quot;
//   *        value=&quot;#{<var>buttonBundle</var>.<var>backButtonLabel</var>}&quot;
//   *        action=&quot;#{<var>navigationStack</var>.pop.go}&quot;
//   *        immediate=&quot;true&quot;
//   *        disabled=&quot;#{not <var>currentHandler</var>.displayView}&quot; /&gt;
//   * </pre>
//   *
//   * @pre getOutcomeViewId.equals(CUrrentVIewID); <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//   * KAN WEL; ZELFDE PAGINA VOOR ANDERE ID
//   * @post getFromHandler().getPersistentBeanId().equals(getFromPersistentBeanId());
//   * @post getFromHandler().reset();
//   * @throws FacesException
//   *         RobustCurrent.facesContext();
//   * @throws FacesException
//   *         RobustCurrent.redirect();
//   */
//  public final void go() throws FacesException {
//    if (LOG.isDebugEnabled()) {
//      String currentViewId = RobustCurrent.uiViewRoot().getViewId();
//      LOG.debug("go called from " + currentViewId);
//    }
//    getFromHandler().setId(getFromPersistentBeanId());
//    // TODO getFromPersistentBeanId() cannot be null
//    LOG.debug("handler id set and handler reset: " + getFromHandler()
//              + " (id: " + getFromPersistentBeanId() + ")");
//    String fromLink = getFromLink();
//    RobustCurrent.redirect(fromLink);
//    RobustCurrent.facesContext().responseComplete();
//    LOG.debug("redirected to from link: " + fromLink);
//  }

}



