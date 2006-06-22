/*<license>
  Copyright 2004-2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.servlet.navigation;


import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.faces.FacesException;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.persistence.PersistentBeanHandler;
import be.peopleware.servlet.sessionMopup.Skimmable;


/**
 * <p>Stack of user navigation in a user session. The most important goal of this
 *   class is to provide goBack-functionality. An instance of this type should
 *   be declared as a managed bean in session scope:</p>
 * <pre>
 *   &lt;managed-bean&gt;
 *     &lt;managed-bean-name&gt;navigationStack&lt;/managed-bean-name&gt;
 *     &lt;managed-bean-class&gt;be.peopleware.servlet.navigation.NavigationStack&lt;/managed-bean-class&gt;
 *     &lt;managed-bean-scope&gt;session&lt;/managed-bean-scope&gt;
 *   &lt;/managed-bean&gt;
 * </pre>
 * <p>Suppose the user navigate from a <dfn>page A</dfn> to a <dfn>page B</dfn>.
 *   At this time, the top of the stack will be a {@ NavigationInstance} that
 *   holds all necssary information to bring the user back to his current state.
 *   The second entry on the stack will be a {@link NavigationInstance} that
 *   holds all necessary information to bring the user back to the state
 *   when his browser showed <dfn>page A</dfn>.</p>
 * <p>The term <dfn>page</dfn> above should be taken losely. E.g., in an
 *   application that shows semantic data in different modes (display, edit,
 *   deleted, ?, see {@link PersistentBeanHandler}), the architect might decide
 *   that all operations on 1 whole of semantic data are considered 1
 *   {@link NavigationInstance}. This would mean that returning to the recorded
 *   state results in showing the same semantic data, in a default mode
 *   (e.g., display). The mode state is forgotten. Also, if e.g., a persistent
 *   object was shown in a previous state, and it is deleted from persistent
 *   storage in the mean time, that returning to a previous state is not possible.
 *   The application could then retrace the user on step further, e.g., and
 *   display a message to this effect.</p>
 * <p>Instances of this type do not consider themselves with the exact definition
 *   of a navigation instance, and how to return to a previous state, or what to
 *   do in case of problems. {@link NavigationInstance} is a thin interface, and
 *   different implementations can be used for different strategies. This package
 *   offers a number of concrete implementations, for use cases that are
 *   supported elsewhere in this library, but application developers can develop
 *   more.</p>
 *
 * <h3>Recording Navigation</h3>
 * <p>The main problem that poses itself is how to record navigation state. For this,
 *   we have to consider the possible ways of navigation, and the different resources
 *   that can be requested from a server.</p>
 * <p>First, there is regular JSF navigation. This means, when navigating from
 *   page A to page B, that an action method or action listener method in a
 *   handler for page A is called, which returns an outcome that resolves to page
 *   B, or navigates to page B itself.</p>
 * <p>Second, there is navigation by an uncontrolled HTTP request. This happens, e.g.,
 *   for images in a page, downloads, or when the user just types in a URL in his
 *   browser, when he activates a bookmark, or when the JSF developer used, e.g., an
 *   <code>outputLink</code>. In this case, most often, images, downloads, or other
 *   such resources, are not considered navigation, and thus no action should be
 *   taken. Uncontrolled navigation to a page is relevant. When uncontrolled navigation
 *   to a page B happens, we are essentially in the same situation as above, where
 *   we at JSF navigation from a page A to page B.</p>
 * <p>To exploit this commonality, navigation recording should be independent of
 *   how we get to a JSF page. In other words, it should happen during or after
 *   the <dfn>render response</dfn> phase. This is also necessary in the first case,
 *   because, to be able to render a JSF page again in a previous state, also the
 *   handlers need to be restored to their previous state. Only the target page B
 *   author can know, in general, which handlers are used, and how to restore them,
 *   and before the <dfn>render response</dfn>, are not in the context of the target
 *   page B. Only in very special circumstances would it be possible to record state
 *   earlier, but even then here we need be cautious: it is possible that an error
 *   occurs during rendering of the response, and in that case the stack would
 *   represent a wrong state. On the other hands, if an error occurs during the
 *   render response phase, you probably would end the user session anyway.
 *   A phase listener could possibly be used, but in general a custom component
 *   in the page seems the best way to record navigation.</p>
 * <p>It is also possible that there are regular JSP pages that are part of your
 *   web application, or even plain HTML pages. For regular JSP pages, also a
 *   custom tag, or a scriptlet, could be used to record navigation. For regular
 *   HTML pages, we would need to use, e.g., a {@link javax.servlet.Filter}.</p>
 * <p>In the subpackage <code>be.pepleware.jsf_II.navigation.persistence</code>, we
 *   offer support for the default navigation offered in
 *   {@link be.peopleware.jsf_II.persistence}.</p>
 *
 * @idea This opens possibilities for detecting that a user makes cycles, or
 *   has returned to a page much earlier.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar getSize() >= 0;
 * @invar isEmpty() ? getTop() == null;
 */
public class NavigationStack implements Skimmable, Serializable {

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


  private static final Log LOG = LogFactory.getLog(NavigationStack.class);


  /**
   * The {@link NavigationInstance} describing the current page the user
   * is seeing in his browser.
   *
   * @basic
   */
  public final NavigationInstance getTop() {
    try {
      return (NavigationInstance)$stack.getLast();
    }
    catch (NoSuchElementException nseExc) {
      return null;
    }
  }

  /**
   * @basic
   * @init 0;
   */
  public final int getSize() {
    return $stack.size();
  }

  /**
   * @return getSize() < 2;
   */
  public final boolean isEmpty() {
    return $stack.size() < 2;
  }


  /**
   * Add a {@link NavigationInstance} to the top of the stack.
   * First, we ask the old top of the stack to
   * {@link NavigationInstance#absorb(NavigationInstance) absorb}
   * the new {@link NavigationInstance} <code>ni</code>. If this succeeds,
   * and {@link NavigationInstance#absorb(NavigationInstance)} has a result
   * that is not <code>null</code>, the current top is replaced by that
   * result. If this does return <code>null</code>, <code>ni</code>
   * is added to the stack and becomes the new {@link #getTop()}.
   * Pushing <code>null</code> results in an exception.
   *
   * @post (isEmpty() || (getTop().absorb(ni) == null)) ?
   *            new.getSize() == getSize() + 1 :
   *            new.getSize() == getSize();
   * @post (isEmpty() || (getTop().absorb(ni) == null)) ?
   *            new.getTop() == ni :
   *            new.getTop() == getTop().absorb(ni);
   * @throws FatalFacesException
   *         ni == null;
   */
  public final void push(final NavigationInstance ni) throws FatalFacesException {
    LOG.debug("pushing on stack: " + ni);
    if (ni == null) {
      RobustCurrent.fatalProblem("tried to add null to navigation stack", LOG);
    }
    NavigationInstance toPush = ni;
    if (! isEmpty()) {
      NavigationInstance joined = getTop().absorb(ni); // NullPointerException cannot happen
      if (joined != null) {
        pop(); // exception cannot happen
        toPush = joined;
      }
    }
    $stack.add(toPush);
  }

  /**
   * Pop the top of the stack.
   *
   * @post new.getSize() == getSize() - 1;
   * @throws FatalFacesException
   *         isEmpty();
   */
  public final void pop() throws FatalFacesException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("popping navigation stack (top is " + getTop() + ")");
    }
    try {
      $stack.removeLast();
    }
    catch (NoSuchElementException nseExc) {
      RobustCurrent.fatalProblem("stack was empty; cannot pop", LOG);
    }
  }

  /**
   * {@link NavigationInstance}s are {@link Serializable}.
   *
   * @toryt-cC org.toryt.contract.Collections;
   * @invar $stack != null;
   * @invar cC:noNull($stack);
   * @invar cC:instanceOf($stack, NavigationInstance.class);
   */
  private final LinkedList $stack = new LinkedList();

  /**
   * Navigate back to the previous user state. The old {@link #getTop()}
   * is the page the user is viewing currently. The {@link NavigationInstance}
   * immediately below the top describes the state the user wants to return
   * to. This will become the new current state. However, by re-rendering
   * this state, a new entry for this state will be pushed to this stack.
   * This means that we need to remove the 2 entries at the top, and navigate
   * to the second one.
   *
   * @post new.getSize() = getSize() - 2;
   * @post pop(); getTop().goBack();
   * @throws FacesException
   *         getSize() < 2;
   * @throws FacesException
   *         pop(); getTop().goBack();
   */
  public final void goBack() throws FacesException {
    if (getSize() < 2) {
      RobustCurrent.fatalProblem("cannot go back: no previous state in NavigationStack", LOG);
    }
    pop();
    NavigationInstance previous = getTop();
    pop();
    LOG.debug("navigating back to previous state (" + previous + ")");
    previous.navigateHere();
  }

  /**
   * Action listener method that calls {@link #goBack()}.
   *
   * @post goBack();
   * @except goBack();
   */
  public final void goBack(final ActionEvent event) throws FacesException {
    goBack();
  }

  /*<section name="skimmable">*/
  //------------------------------------------------------------------

  /**
   * Skim all registered {@link NavigationInstance NavigationInstances}.
   * Instances are never removed!
   */
  public void skim() {
    Iterator iter = $stack.iterator();
    while (iter.hasNext()) {
      NavigationInstance ni = (NavigationInstance)iter.next();
      if (ni instanceof Skimmable) {
        Skimmable skimNi = (Skimmable)ni;
        skimNi.skim();
      }
    }
  }

  /*</section>*/

}


