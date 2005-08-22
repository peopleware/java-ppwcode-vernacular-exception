/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II.navigation.persistence;


import java.util.Date;

import javax.faces.FacesException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.persistence.InstanceHandler;
import be.peopleware.persistence_I.PersistentBean;


/**
 * <p>Keep the information about a step in the navigation through a
 * JSF web application. This means, the {@link #getFromViewId() view id} of the
 * <dfn>from</dfn> page, i.e., the path to the <kbd>jspx</kbd> page that
 * started the navigation,
 * the {@link #getFromHandler() handler} that was used for a request, and
 * the {@link #getTime() time that that page was left for another page}.
 * The handler must be a {@link InstanceHandler}, and we also
 * store the {@link #getFromPersistentBeanId() id} of the {@link PersistentBean}
 * that the handler was handling at {@link #getTime() the time} (the handler
 * should now the <dfn>type</dfn> of the beans it is handling.
 * Also, the outcome to which this navigation directs is stored.</p>
 *
 * <p>The method {@link #go()} is intented as an action method to return to
 * the <dfn>from</dfn>-page, with the handler in the status it was at
 * {@link #getTime() the time}.</p>
 *
 * <p>Instances of this type should be
 * {@link #InstanceHandlerNavigationInstance(InstanceHandler, Long, String) created}
 * during the handling of the request in the {@link #getFromHandler() from handler}
 * that will result in a navigation to {@link #getOutcome()}. This will
 * automatically result in the correct setting of {@link #getTime()} and
 * {@link #getFromViewId()}.</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar getTime() != null;
 * @invar ! getTime().after(NOW);
 * @invar getFromViewId() != null;
 * @invar getFromHandler() != null;
 * @invar getFromPersistentBeanId() != null;
 * @invar getOutcome() != null; in-page navigation is not interesting
 */
public class InstanceHandlerNavigationInstance {

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


  private static final Log LOG = LogFactory.getLog(InstanceHandlerNavigationInstance.class);


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @pre fromHandler != null;
   * @pre fromPersistentBeanId != null;
   * @pre outcome != null;
   * @post new.getTime().equals(NOW);
   * @post new.getFromViewId().equals(RobustCurrent.uiViewRoot().getViewId());
   * @post new.getFromHandler() == fromHandler;
   * @post new.getPeristentBeanId().equals(fromPersistentBeanId);
   * @post new.getOutcome().equals(outcome);
   * @throws FacesException
   *         RobustCurrent.uiViewRoot();
   */
  public InstanceHandlerNavigationInstance(InstanceHandler fromHandler,
                            Long fromPersistentBeanId,
                            String outcome) throws FacesException {
    assert fromHandler != null;
    assert fromPersistentBeanId != null;
    assert outcome != null;
    $fromHandler = fromHandler;
    $fromPersistentBeanId = fromPersistentBeanId;
    $outcome = outcome;
    $fromViewId = RobustCurrent.uiViewRoot().getViewId();
  }

  /*</construction>*/



  /*<property name="time">*/
  //------------------------------------------------------------------

  /**
   * @basic
   * @init NOW;
   */
  public final Date getTime() {
    return $endTime;
  }

  /**
   * @invar $endTime != null;
   */
  private Date $endTime = new Date();

  /*</property>*/



  /*<property name="fromViewId">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final String getFromViewId() {
    return $fromViewId;
  }

  /**
   * The URL of the <dfn>from</dfn> page, with faces mapping.
   * Calling this URL in a browser will call the <dfn>from</dfn>-page.
   *
   * @return RobustCurrent.viewHandler()
   *              .getActionURL(RobustCurrent.facesContext(), $fromViewId);
   * @throws FacesException
   *         RobustCurrent.viewHandler();
   * @throws FacesException
   *         RobustCurrent.facesContext();
   */
  String getFromLink() throws FacesException {
    return RobustCurrent.viewHandler().getActionURL(RobustCurrent.facesContext(), $fromViewId);
  }

  private String $fromViewId;

  /*</property>*/



  /*<property name="fromHandler">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final InstanceHandler getFromHandler() {
    return $fromHandler;
  }

  private InstanceHandler $fromHandler;

  /*</property>*/



  /*<property name="fromPersistentBeanId">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Long getFromPersistentBeanId() {
    return $fromPersistentBeanId;
  }

  /**
   * @invar $fromPersistentBeanId != null;
   */
  private Long $fromPersistentBeanId;

  /*</property>*/



  /*<property name="outcome">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final String getOutcome() {
    return $outcome;
  }

//  public final String getOutcomeViewId() {
//    return RobustCurrent.application().
//  }

  private String $outcome;

  /*</property>*/



  /**
   * <p>Set the {@link InstanceHandler#setId(Long) id} of
   * {@link #getFromHandler()} to {@link #getFromPersistentBeanId()}, tell it to
   * {@link InstanceHandler#reset() reset}, and <strong>HTTP redirect</strong> to
   * {@link #getFromLink()}.</p>
   *
   * <p>This should should result in the browser requesting the
   * {@link #getFromViewId() from JSF page} from the web app. This page should use
   * {@link #getFromHandler()} as its handler, which was just prepared
   * for such a request.</p>
   *
   * <p>This method should be used as an <dfn>action method</dfn> like</p>
   * <pre>
   * &lt;h:commandButton id=&quot;<var>backButtonId</var>&quot;
   *        value=&quot;#{<var>buttonBundle</var>.<var>backButtonLabel</var>}&quot;
   *        action=&quot;#{<var>navigationStack</var>.pop.go}&quot;
   *        immediate=&quot;true&quot;
   *        disabled=&quot;#{not <var>currentHandler</var>.displayView}&quot; /&gt;
   * </pre>
   *
   * @pre getOutcomeViewId.equals(CUrrentVIewID); <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
   * KAN WEL; ZELFDE PAGINA VOOR ANDERE ID
   * @post getFromHandler().getPersistentBeanId().equals(getFromPersistentBeanId());
   * @post getFromHandler().reset();
   * @throws FacesException
   *         RobustCurrent.facesContext();
   * @throws FacesException
   *         RobustCurrent.redirect();
   */
  public final void go() throws FacesException {
    if (LOG.isDebugEnabled()) {
      String currentViewId = RobustCurrent.uiViewRoot().getViewId();
      LOG.debug("go called from " + currentViewId);
    }
    getFromHandler().setId(getFromPersistentBeanId());
    // TODO getFromPersistentBeanId() cannot be null
    LOG.debug("handler id set and handler reset: " + getFromHandler()
              + " (id: " + getFromPersistentBeanId() + ")");
    String fromLink = getFromLink();
    RobustCurrent.redirect(fromLink);
    RobustCurrent.facesContext().responseComplete();
    LOG.debug("redirected to from link: " + fromLink);
  }

  private void cannotGoBack() {
    // TODO (jand) message and stay here?? we cannot go back to our parent!
    /* try grandparent, i.e., next in stack! */
  }

}


