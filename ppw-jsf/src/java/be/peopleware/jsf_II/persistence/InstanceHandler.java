/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.persistence;


import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.servlet.ServletRequestListener;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.util.AbstractUnmodifiableMap;
import be.peopleware.persistence_II.IdNotFoundException;
import be.peopleware.persistence_II.PersistentBean;
import be.peopleware.persistence_II.dao.AsyncCrudDao;
import be.peopleware.servlet.navigation.NavigationInstance;
import be.peopleware.servlet.sessionMopup.Removable;
import be.peopleware.servlet.sessionMopup.Skimmable;


/**
 * <p>Handler for {@link PersistentBean} detail CRUD pages.</p>
 * <p>This handler can be used in a number of circumstances. The main use
 *   is as the backing bean for a detail CRUD page of an instance of semantics.
 *   For this to work, the handler needs a {@link #getAsyncCrudDao()} the
 *   {@link #getPersistentBeanType()} filled out,
 *   needs to know the previous {@link #getViewMode()},
 *   and it needs an {@link #getInstance() instance}.</p>
 * <p>The instance can be set explicitly with {@link #setInstance(PersistentBean)}.
 *   This immediately also sets the {@link #getId()}. If the
 *   {@link #getInstance() instance} is <code>null</code> when it is requested,
 *   we will retrieve the instance with id {@link #getId()} and type
 *   {@link #getPersistentBeanType()} from persistent storage, and cache it. If at this time
 *   {@link #getId()} is <code>null</code>, a new instance of {@link #getPersistentBeanType()}
 *   will be created.</p>
 * <p>In conclusion this means that, before an instance of this class can be used,
 *   you need to set the<p>
 * <ul>
 *   <li>the {@link #setPersistentBeanType(Class) type} and</li>
 *   <li>the {@link #setViewMode(String) previous view mode}, and</li>
 *   <li>either
 *     <ul>
 *       <li>an {@link #setInstance(PersistentBean) instance},</li>
 *       <li>or
 *         <ul>
 *           <li>a {@link #setDaoVariableName(String) dao variable name}, and</li>
 *           <li>an {@link #setId(Long) id}.</li>
 *         </ul>
 *       </li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <h2>States &amp; Transitions</h2>
 * <p>An <code>InstanceHandler</code> has 4 states and
 *  a number of state transitions. The states are actually <dfn>view modes</dfn>
 *  for the JSF page.</p>
 * <img src="doc-files/persistence.gif" style="width: 100%;" />
 *
 * <h3>Retrieving the {@link be.peopleware.persistence_II.PersistentBean Instance}</h3>
 * <p>With each HTTP request (except the request for a screen to fill out the
 *  values for a new instance, the request to create a new instance, the request
 *  to show a list of persistent beans and a request to go back to the
 *  previous page) we expect the
 *  {@link be.peopleware.persistence_II.PersistentBean#getId() primary key of a
 *  persistent bean} as request parameter. This id is filled in in the handler's
 *  {@link #getId()} property. Before
 *  the Update Model Values phase is reached, we need to load the
 *  {@link be.peopleware.persistence_II.PersistentBean} with
 *  {@link #getId() this id} and type {@link #getPersistentBeanType()} from
 *  persistent storage. This instance will be stored in the
 *  {@link #getInstance() instance handler property}.</p>
 * <p>It is always possible that no instance with {@link
 *  #getId() this id} and  {@link #getPersistentBeanType() type} can be found
 *  in persistent storage, because it never existed, or because such instance
 *  was removed in-between user requests from persistent storage. Whenever this
 *  occurs, the user will be directed back to the page he originally came from,
 *  before he accessed this page.</p>
 * <p>Again it is possible that the previous instance does not exist in persistent
 *  storage anymore, so this process is recursive, until the first page after
 *  login is reached.</p>
 * <p>If the user itself is removed from the system while logged in, the session
 *  will be stopped as early as possible, and the user will return to the login
 *  page.</p>
 * <p>HOW IS THIS ACHIEVED?</p>
 * <h3>Entry</h3>
 * <p>The state machine is entered by a navigation request (<code>navigate(id)
 *  [found(id)]</code>), or a request to edit a new instance
 *  (<code>editNew()</code>).</p>
 * <p>Entry is requested always from the handler of a previous page. When entry
 *  succeeds, the previous page is recorded in the  TODO NavigationStack, so we
 *  can go back to it later.</p>
 * <h3>Display</h3>
 * <p>In view mode <code>display</code>, the data of the {@link
 *  be.peopleware.persistence_II.PersistentBean} is shown, in a non-editable
 *  way.</p>
 * <h4>delete</h4>
 * <p>From this state, we can try to delete the {@link
 *  be.peopleware.persistence_II.PersistentBean}. When the bean is not found in
 *  persistent storage (<code>delete(id) [! found(id)]</code>), the user is
 *  brought back to the previous page, and a message is shown. If the bean is
 *  found in persistent storage, deletion might fail for semantic reasons
 *  (<code>delete(id) [found(id) &amp;&amp; exception]</code>). In this case, we
 *  stay in <code>display</code> state, and show a message to the user about the
 *  exception. When deletion succeeds (<code>delete(id) [found(id) &amp;&amp;
 *  nominal] / DELETE</code>), the instance is deleted from persistent storage,
 *  and the handler goes to the <code>deleted</code> state.</p>
 * <p>This is implemented in the action method {@link #delete()}. The user can
 *  request the deletion by clicking a button defined as:</p>
 * <pre>
 *   &lt;h:commandButton action=&quot;#{<var>myHandler</var>.delete}&quot;
 *                    value=&quot;#{<var>myHandler</var>.buttonLabels['delete']}&quot;
 *                    rendered=&quot;#{<var>myHandler</var>.viewMode eq 'display'}&quot;
 *                    immediate=&quot;true&quot;/&gt;
 * </pre>
 * <h4>edit</h4>
 * <p>In <code>display</code> mode, the user can also ask for <code>edit</code> mode. When the
 *  bean is not found in persistent storage (<code>edit(id) [! found(id)]</code>),
 *  the user is brought back to the previous page, and a message is shown. If the
 *  bean is found in persistent storage (<code>edit(id) [found(id)]</code>), the
 *  handler goes to the <code>edit</code> state.</p>
 * <p>This is implemented in action method {@link #edit()}. The user can request
 *  view mode <code>edit</code> by clicking a button defined as:</p>
 * <pre>
 *   &lt;h:commandButton action=&quot;#{<var>myHandler</var>.edit}&quot;
 *                    value=&quot;#{<var>myHandler</var>.buttonLabels['edit']}&quot;
 *                    rendered=&quot;#{<var>myHandler</var>.viewMode eq 'display'}&quot;
 *                    immediate=&quot;true&quot;/&gt;
 * </pre>
 * <h4>goBack</h4>
 * <p>From display mode, the user can request to go back to the previous
 *  page (<code>goBack()</code>). If the previous instance cannot be found, this
 *  action propagates the user to instances visited earlier, recursively.</p>
 * <p>HOW IS THIS IMPLEMENTED?  The user can request to go back by clicking a
 *  button defined as:</p>
 * <pre>
 *  MUDO (jand) NO IDEA YET
 * </pre>
 * <h4>navigate</h4>
 * <p>Finally, the user can request to navigate to another page
 *  (<code>nextPageHandler.navigate(nextPageId) [found(nextPageId)]</code>).
 *  Presumably, he clicks on a link to navigate to a related bean. This state
 *  transition is a placeholder for any number of possible navigation points in
 *  the page. The intention is to show the user a page for the next instance, in
 *  <code>display</code> mode. It is possible that this next instance cannot be
 *  found in persistent storage <code>(nextPageHandler.navigate(nextPageId) [!
 *  found(nextPageId)]</code>). In that case, we stay in <code>display</code>
 *  state in this page, and show the user a message.</p>
 * <p>This is not implemented in this generic handler.</p>
 * <h3>Edit</h3>
 * <p>In view mode <code>edit</code>, the data of the {@link
 *  be.peopleware.persistence_II.PersistentBean} is shown, in an editable way. This
 *  means that the user can change the current values of the properties of the
 *  persistent bean or fill in the value of properties that were not specified
 *  before.</p>
 * <h4>cancel</h4>
 * <p>From this state, we can try to return to the <code>display</code> state
 *  without applying the changes. When the bean is not found in persistent storage
 *  (<code>cancel(id) [!found(id)]</code>), the user is brought back to the
 *  previous page, and a message is shown. If the bean is found in persistent
 *  storage (<code>cancel(id) [found(id)]</code>), we return to the
 *  <code>display</code> state and the values that were filled in in the
 *  <code>edit</code> state are forgotten; this means that the old
 *  values of the bean are shown in the <code>display</code> state and that the
 *  persistent bean is unchanged.</p>
 * <p>This is implemented in action method {@link #cancelEdit()}. The user can
 *  request cancellation by clicking a button defined as:</p>
 * <pre>
 *   &lt;h:commandButton action=&quot;#{<var>myHandler</var>.cancelEdit}&quot;
 *                    value=&quot;#{<var>myHandler</var>.buttonLabels['cancel']}&quot;
 *                    rendered=&quot;#{<var>myHandler</var>.viewMode eq 'edit'}&quot;
 *                    immediate=&quot;true&quot;/&gt;
 * </pre>
 * <h4>update</h4>
 * <p>From the <code>edit</code> state, we can try to update the {@link
 *  be.peopleware.persistence_II.PersistentBean} with the values filled in by the
 *  user. When the bean is not found in persistent storage (<code>update(id, data)
 *  [! found(id)]</code>), the user is brought back to the
 *  previous page, and a message is shown. If the bean is found in persistent
 *  storage, updating might fail for semantic reasons (<code>update(id, data)
 *  [found(id) &amp;&amp; exception]</code>). In this case, we stay in the
 *  <code>edit</code> state, and show a message to the user about the exception.
 *  When updating succeeds (<code>update(id, data) [found(id) &amp;&amp;
 *  nominal]</code>), the handler goes to the <code>display</code> state and the
 *  persistent bean is updated in persistent storage.</p>
 * <p>This is implemented in action method {@link #update()}. The user can request
 *  storing the new information by clicking a button defined as:</p>
 * <pre>
 *   &lt;h:commandButton action=&quot;#{<var>myHandler</var>.update}&quot;
 *                    value=&quot;#{<var>myHandler</var>.buttonLabels['commit']}&quot;
 *                    rendered=&quot;#{<var>myHandler</var>.viewMode eq 'edit'}&quot; /&gt;
 * </pre>
 * <h3>Edit New</h3>
 * <p>In view mode <code>editNew</code>, the user is shown a page, with empty,
 *  editable fields for him to fill in. These fields represent a new bean of type
 *  {@link #getPersistentBeanType()}. It is
 *  possible that some data is filled out when this page is shown, e.g., default
 *  data, or good guesses.</p>
 * <h4>cancel</h4>
 * <p>If the user changes his mind, he can request to cancel the
 *  <code>editNew</code> state (<code>cancel()</code>). This amounts to the user
 *  issuing a request to go back to the previous page (<code>goBack()</code>). If
 *  the previous instance cannot be found, this action propagates the user to
 *  instances visited earlier, recursively.</p>
 * <p>This is implemented in action method {@link #cancelEditNew()}. The user can
 *  request cancellation by clicking a button defined as:</p>
 * <pre>
 *   &lt;h:commandButton action=&quot;#{<var>myHandler</var>.cancelEditNew}&quot;
 *                    value=&quot;#{<var>myHandler</var>.buttonLabels['cancel']}&quot;
 *                    rendered=&quot;#{<var>myHandler</var>.viewMode eq 'editNew'}&quot;
 *                    immediate=&quot;true&quot;/&gt;
 * </pre>
 * <h4>create</h4>
 * <p>From view mode <code>editNew</code>, the user can submit data for the actual
 *  creation of a new bean in persistent storage. This can fail with semantic
 *  exceptions (<code>create(data) [exception]</code>). We stay in the
 *  <code>editNew</code> state, and show the user messages about the errors. If
 *  creation succeeds (<code>create(data) [nominal]</code>), the new instance is
 *  created in persistent storage, and the handler goes to the
 *  <code>display</code> state.</p>
 * <p>This is implemented in action method {@link #create()}. The user can request
 *  storing the new information by clicking a button defined as:</p>
 * <pre>
 *   &lt;h:commandButton action=&quot;#{<var>myHandler</var>.create}&quot;
 *                    value=&quot;#{<var>myHandler</var>.buttonLabels['commit']}&quot;
 *                    rendered=&quot;#{<var>myHandler</var>.viewMode eq 'editNew'}&quot; /&gt;
 * </pre>
 * <h3>Deleted</h3>
 * <p>In view mode <code>deleted</code>, the data of the deleted {@link
 *  be.peopleware.persistence_II.PersistentBean} is shown, in a non-editable way,
 *  with visual feedback about the fact that it was deleted (e.g., strikethrough).
 * <h4>goBack</h4>
 * <p>From this mode, the only action of the user can be to request to go back to
 *  the previous page (<code>goBack()</code>). If the previous instance cannot be
 *  found, this action propagates the user to instances visited earlier,
 *  recursively.</p>
 * <p>HOW IS THIS IMPLEMENTED? BUTTON?</p>
 * <h2>Remembering State</h2>
 * <p>With this setup, the only state that must be remembered in-between HTTP
 *  requests is the id of the {@link PersistentBean} we are working with, and the
 *  view mode of the handler. By storing this information in the actual HTML page,
 *  we essentially make the handler stateless.</p>
 * <p>This information needs to be filled out in the handler properties ({@link
 *  #getId()} and {@link #getViewMode()} before the action methods are executed. This
 *  can be achieved by storing these values in hidden text fields in the JSF page
 *  with the <code>immediate</code> attribute set to <code>true</code>:</p>
 * <pre>
 *   &lt;h:inputHidden value=&quot;#{<var>myHandler</var>.id}&quot; immediate=&quot;true&quot; /&gt;
 *   &lt;h:inputHidden value=&quot;#{<var>myHandler</var>.viewMode}&quot; immediate=&quot;true&quot; /&gt;</pre>
 * <p>
 *   Since the <code>immediate</code> attribute of these inputHidden tags is set
 *   to true, the corresponding setters {@link #setId(Long)} and
 *   {@link #setViewMode(String)} will be called early in the request / response
 *   cycle, namely during the Apply Request Values Phase. We give the
 *   {@link #setId(Long)} method side-effects that initialise the persistent bean
 *   and all other resources that are needed during the request / response cycle.
 *   In this way, the handler is initialised early in the request / response cycle.
 *   This is necessary, because, e.g., for some of the action methods
 *   ({@link #update()} and {@link #create()}), the bean must be available in
 *   {@link #getInstance()} before the Update Model Values phase, to receive
 *   values from the UIView.<br />
 *   If no instance with the requested id can be found in persistent storage,
 *   {@link #getInstance()} will be <code>null</code> afterwards to signal this.
 *   <br />
 *   The {@link #setId(Long)} method cannot however be used to create a new bean
 *   for entry into view mode <code>editNew</code>, and for the {@link #create()}
 *   action method. This is because the setter {@link #setId(Long)} will not be
 *   called when the request parameter for the id is <code>null</code>,
 *   since the id property is <code>null</code> already (see below).
 *   Therefore, creating a new bean is done as a side-effect in the
 *   {@link #setViewMode(String)} method, whenever the requested view mode
 *   is <code>editNew</code>.</p>
 * <p>To make sure that the hidden fields described above contain the correct values at the
 *  beginning of a request / response cycle, the corresponding handler must hold
 *  the correct values for the properties {@link #getId()} and {@link #getViewMode()}
 *  before the Render Response phase of the previous cycle is entered.</p>
 * <p>To guarantee that the setters {@link #setId(Long)} and
 *  {@link #setViewMode(String)} are called during the Apply Request Values
 *  Phase, the {@link #getId()} and {@link #getViewMode()} should be set to null
 *  after the Render Response Phase. This is because
 *  the {@link #setId(Long)} (resp. {@link #setViewMode(String)}) method is only
 *  executed when the old value of {@link #getId()} (resp. {@link #getViewMode()})
 *  and the <code>id</code> (resp. <code>viewMode</code>) that comes as a
 *  parameter with the HTTP request are different. To achieve this, the
 *  {@link #getId()} (resp. {@link #getViewMode()}) should be set to null at the
 *  end of each cycle.</p>
 * <p>JavaServer Faces does not offer the possibility to do anything after the
 *  response is rendered. So we need an extra mechanism that clears the handler.
 *  This is done by a special {@link ServletRequestListener} that magically knows which
 *  handler to reset.</p>
 * <h2>Not Remembering State</h2>
 * <p>After the response is rendered completely, the {@link #getId()} and {@link
 *  #getInstance()} (and possibly other resources) are no longer needed, and
 *  should be set to <code>null</code>, to avoid clogging memory in-between user
 *  requests. Setting the {@link #getId()} and {@link #getViewMode()} to null
 *  is also necessary for functional reasons, as described above. Releasing
 *  resources can be achieved by setting the handlers in request scope.</p>
 * <h2>Configuration</h2>
 * <p>This class requires the definition of 2 filters in
 *  <kbd>WEB-INF/web.xml</kbd>:</p>
 * <pre>
 * &lt;listener&gt;
 *   &lt;listener-class&gt;be.peopleware.servlet_I.hibernate.SessionFactoryController&lt;/listener-class&gt;
 *   &lt;listener-class&gt;be.peopleware.servlet_I.hibernate.SessionInView&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </pre>
 *
 *
 * @author    Jan Dockx
 * @author    Nele Smeets
 * @author    Peopleware n.v.
 *
 * @invar (getViewMode() != null)
 *            ? isViewMode(getViewMode())
 *            : true;
 * @invar (getInstance() != null)
 *            ? getType().isInstance(getInstance())
 *            : true;
 * @invar getViewMode() != null;
 *
 * @idea (jand) gather viewmode in separate class
 * @mudo (jand) security
 */
public class InstanceHandler extends PersistentBeanHandler {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";
  /*</section>*/


  private static final Log LOG = LogFactory.getLog(InstanceHandler.class);

  /**
   * Default constructor.
   */
  public InstanceHandler() {
    LOG.debug("constructor of InstanceHandler");
  }

  /*<property name="id">*/
  //------------------------------------------------------------------

  /**
   * The id of the {@link PersistentBean} that is handled by the requests.
   * The id is not changed by {@link #skim()}.
   *
   * @basic
   * @init null;
   */
  public final Long getId() {
    return $id;
  }

  /**
   * Store the given id in {@link #getId()}.
   *
   * @param   id
   *          The id of the {@link PersistentBean} that will be handled in the
   *          requests.
   * @post    new.getId().equals(id);
   */
  public final void setId(final Long id) {
    // set the id
    $id = id;
    LOG.debug("id of " + this + " set to " + id);
  }

  /**
   * <p>Set {@link #getId()} from the request parameter with name
   *   <code>name</code>. If no such parameter is found, we do
   *   nothing.</p>
   * <p>For some reason, expressions like
   * <code>#{param.myParamName}</code>
   * return <code>0</code> when used in the <kbd>faces-config.xml</kbd>, where
   * there is a parameter like <code>myParamName=4567</code> in the HTTP
   * request. This is a workaround that does seem to work.</p>
   */
  public final void setIdFromRequestParameterName(final String name) {
    Map requestParameters = RobustCurrent.paramMap();
    String idString = (String)requestParameters.get(name);
    if (idString != null) {
      if (idString.equals(EMPTY)) {
        setId(null);
      }
      else {
        try {
          Long id = Long.valueOf(idString); // NumberFormatException
          setId(id);
        }
        catch (NumberFormatException nfExc) {
          RobustCurrent.fatalProblem(
              "The id value in the request is not a Long (" + idString +  ")",
              nfExc,  LOG);
          // IDEA (jand) this is not fatal; do goback()
        }
      }
    }
    // else (idString not in request param), do NOP
  }

  /**
   * The id of the {@link PersistentBean} that will be handled
   * by the requests.
   */
  private Long $id;

  /*</property>*/


  /*<property name="viewMode">*/
  //------------------------------------------------------------------

  /**
   * The string representing view mode "editNew".
   *
   * <strong>= &quot;editNew&quot;</strong>
   */
  public static final String VIEWMODE_EDITNEW = "editNew";

  /**
   * The string representing view mode "deleted".
   *
   * <strong>= &quot;deleted&quot;</strong>
   */
  public static final String VIEWMODE_DELETED = "deleted";

  /**
   * An array of strings containing view modes "editNew" and "deleted".
   *
   * <strong>= {VIEWMODE_EDITNEW, VIEWMODE_DELETED};</strong>
   */
  public static final String[] VIEWMODES =
      {VIEWMODE_EDITNEW, VIEWMODE_DELETED};

  /**
   * Does <code>viewMode</code> represent a valid view mode?
   *
   * @param   viewMode
   *          The viewMode to be checked.
   * @return  super.isViewMode(viewMode) ||
   *            Arrays.asList(VIEWMODES).contains(viewMode);
   */
  public boolean isValidViewMode(final String viewMode) {
    return super.isValidViewMode(viewMode)
             || Arrays.asList(VIEWMODES).contains(viewMode);
  }

  /**
   * Returns true when the handler is editable, i.e. when the view mode is
   * equal to {@link PersistentBeanHandler#VIEWMODE_EDIT} or
   * {@link #VIEWMODE_EDITNEW}. Returns <code>false</code> otherwise.
   *
   * This method is introduces to avoid writing
   * <code>myHandler.viewmode eq 'edit' or myHandler.viewmode eq 'editNew'</code>
   * and
   * <code>myHandler.viewmode neq 'edit' and myHandler.viewmode neq 'editNew'</code>
   * as value of the rendered attributes of in- and output fields in JSF pages,
   * which is cumbersome.
   * Now we can write
   * <code>myHandler.inViewModeEditOrEditNew</code>
   * and
   * <code>not myHandler.inViewModeEditOrEditNew</code>.
   *
   * @return  getViewMode().equals(PersistentBeanHandler.VIEWMODE_EDIT) ||
   *          getViewMode().equals(VIEWMODE_EDITNEW);
   * @throws  FatalFacesException
   *          getViewMode() == null;
   *
   * @deprecated
   */
  public final boolean isInViewModeEditOrEditNew() throws FatalFacesException {
    if (getViewMode() == null) {
      RobustCurrent.fatalProblem("ViewMode is null", LOG);
      return false;
    }
    else {
      return
        getViewMode().equals(PersistentBeanHandler.VIEWMODE_EDIT)
        || getViewMode().equals(VIEWMODE_EDITNEW);
    }
  }

  /**
   * @result false ? (! getViewMode().equals(VIEWMODE_EDITNEW));
   */
  public boolean isShowFields() throws FatalFacesException {
    return super.isShowFields()
             || getViewMode().equals(VIEWMODE_EDITNEW);
  }

  /*</property>*/


  /*<property name="instance">*/
  //------------------------------------------------------------------

  /**
   * <p>The {@link PersistentBean} that is handled in the requests.
   *   If {@link #getStoredInstance()} is not <code>null</code>,
   *   it is returned. If it is <code>null</code>, an instance
   *   is loaded from persistent storage with {@link #getAsyncCrudDao()},
   *   with type {@link #getPersistentBeanType()} and id {@link #getId()}.
   *   If {@link #getStoredInstance()} and {@link #getId()} are
   *   both <code>null</code>, a fresh instance of {@link #getPersistentBeanType()}
   *   will be created using the default constructor. If
   *   {@link #getPersistentBeanType()} is <code>null</code> when
   *   {@link #getStoredInstance()} is <code>null</code>,
   *   we cannot procede.</p>
   * <p>In the default implementation, {@link #skim()} removes the instances if
   *   {@link #getId()} is not <code>null</code>.
   * <p>This method calls {@link #instanceChanged()} when a fresh instance is
   *   retrieved from the persistent storage, and set.</p>
   *
   * @return new.getStoredInstance();
   * @post ((getStoredInstance() == null) && (getId() != null)) ?
   *            new.getStoredInstance() == getDao().retrievePersistentBean(getId(), getType());
   * @post ((getStoredInstance() == null) && (getId() == null)) ?
   *            new.getStoredInstance() == getType().fresh;
   * @throws FatalFacesException
   *         (getStoredInstance() == null) && (getType() == null);
   * @throws FatalFacesException
   *         (getStoredInstance() == null) && (getId() != null) && problems loading bean from DB;
   */
  public final PersistentBean getInstance() throws FatalFacesException {
    LOG.debug("instance requested; id = " + getId());
    if ($instance == null) {
      LOG.debug("instance is not cached");
      if (getPersistentBeanType() == null) {
        RobustCurrent.fatalProblem("should load instance from db or create fresh instance, "
                                   + "but type is null", LOG);
      }
      if (getId() != null) {
        LOG.debug("id = " + getId() + "; loading from DB");
        $instance = loadInstance();
      }
      else {
        LOG.debug("id is null; creating fresh");
        $instance = createInstance();
      }
      LOG.debug("instance = " + $instance);
      assert $instance.getId().equals(getId());
      instanceChanged();
    }
    else {
      LOG.debug("returning instance from cache: " + $instance);
    }
    return $instance;
  }

  /**
   * @basic
   * @init null;
   */
  public final PersistentBean getStoredInstance() {
    return $instance;
  }

  /**
   * This method calls {@link #instanceChanged()} when the new instance is set.
   *
   * @post new.getStoredInstance() == instance;
   * @post (instance != null) ? new.getId().equals(instance.getId());
   * @throws IllegalArgumentException
   *         (instance != null) && ! getType().isAssignableFrom(instance.getClass());
   */
  public final void setInstance(final PersistentBean instance) throws IllegalArgumentException {
    LOG.debug("setting instance to " + instance);
    if ((instance != null) && (!getPersistentBeanType().isAssignableFrom(instance.getClass()))) {
      throw new IllegalArgumentException("instance " + instance
                                         + " is not a subtype of "
                                         + getPersistentBeanType());
    }
    $instance = instance;
    if (instance != null) {
      setId(instance.getId());
    }
    else {
      // else, we do NOT set the ID to null
      LOG.debug("instance set to null; id is left untouched (" + getId() + ")");
    }
    instanceChanged();
  }

  /**
   * <p>Called whenever we become aware of the fact that the {@link #getInstance() instance}
   *   has changed. This is at the end of {@link #setInstance(PersistentBean)}, in
   *   {@link #getInstance()} when a bean is loaded from DB based on the {@link #getId()},
   *   in {@link #release()}, {@link #skim()}, and in {@link #editNew(PersistentBean)}.</p>
   * <p>The instance might be <code>null</code>.</p>
   * <p>This default implementation does nothing.</p>
   *
   * @pre getInstance() != null ? getInstance().getId().equals(getId());
   */
  protected void instanceChanged() {
    // NOP
  }

  /**
   * {@link #postLoadInstance(PersistentBean)} is called on the fresh
   * bean to do whatever configuration necessary.
   *
   * @pre getDao() != null;
   * @throws FatalFacesException
   *         {@link AsyncCrudDao#retrievePersistentBean(java.lang.Long, java.lang.Class)} /
   *         {@link TechnicalException}
   * @throws FatalFacesException
   *         MUDO (jand) other occurences must be replaced by goBack()
   */
  protected final PersistentBean loadInstance() throws FatalFacesException {
    LOG.debug("loading instance with id = " + getId()
              + " and type = " + getPersistentBeanType());
    assert getAsyncCrudDao() != null;
    PersistentBean result = null;
    try {
      if (getId() == null) {
        RobustCurrent.fatalProblem("id is null", LOG);
        // MUDO (jand) replace with goback?
      }
      if (getPersistentBeanType() == null) {
        RobustCurrent.fatalProblem("type is null", LOG);
        // MUDO (jand) replace with goback?
      }
      LOG.debug("retrieving persistent bean with id "
                  + getId() + " and type " + getPersistentBeanType() + "...");
      result = getAsyncCrudDao().retrievePersistentBean(getId(), getPersistentBeanType());
          // IdNotFoundException, TechnicalException
      assert result != null;
      assert result.getId().equals(getId());
      assert getPersistentBeanType().isInstance(result);
      if (LOG.isDebugEnabled()) {
        // if makes that there really is lazy loading if not in debug
        LOG.debug("retrieved persistent bean is " + result);
      }
      LOG.debug("Calling postLoadInstance");
      postLoadInstance(result);
      LOG.debug("retrieved instance after postLoadInstance: " + result);
    }
    catch (IdNotFoundException infExc) {
      // this will force $instance null
      LOG.info("could not find instance of type " + getPersistentBeanType()
               + " with id " + getId(), infExc);
      // MUDO goback() instead of exception
      RobustCurrent.fatalProblem("could not find persistent bean with id "
                                 + getId() + " of type "
                                 + getPersistentBeanType(), infExc, LOG);
    }
    catch (TechnicalException tExc) {
      RobustCurrent.fatalProblem("could not retrieve persistent bean with id "
                                 + getId() + " of type "
                                 + getPersistentBeanType(), tExc, LOG);
    }
    return result;
  }

  /**
   * Method called by {@link #loadInstance()},
   * to do additional configuration of retrieved beans of type
   * {@link #getPersistentBeanType()}.
   * This default does nothing, but subclasses should overwrite
   * when needed.
   *
   * @pre pb != null;
   * @pre pb.getClass() == getPersistentBeanType()
   */
  protected void postLoadInstance(final PersistentBean pb) {
    // NOP
  }

  /**
   * Method to be called after Render Response phase, to clear
   * semantic data from the session.
   *
   * @post $instance == null;
   */
  private void releaseInstance() {
    $instance = null;
    instanceChanged();
  }

  /**
   * The {@link PersistentBean} that is handled in the requests.
   */
  private PersistentBean $instance;

  /*</property>*/


  /**
   * This implementation automatically delegates this call
   * to all {@link #getUsedAssociationHandlers()}.
   */
  protected void updateValues() {
    if (getInstance() == null) {
      RobustCurrent.fatalProblem("could not update values of null instance", LOG);
    }
    Iterator iter = getUsedAssociationHandlers().values().iterator();
    while (iter.hasNext()) {
      PersistentBeanHandler pbh = (PersistentBeanHandler)iter.next();
      pbh.updateValues();
    }
    super.updateValues();
  }


  /**
   * <p>This method should be called to navigate to detail page
   *   for this {@link #getInstance()} in <code>viewMode</code>.</p>
   * <p>This handler is made available to the JSP/JSF page in request scope,
   *   as a variable with name
   *   {@link #RESOLVER}{@link PersistentBeanHandlerResolver#handlerVariableNameFor(Class) .PersistentBeanHandlerResolver#handlerVariableNameFor(getType())}.
   *   And we navigate to {@link #getViewId()}.</p>
   * <p>The {@link #getPersistentBeanType() type} and an {@link #getInstance()} should
   *   be set before this method is called.</p>
   *
   * @post    isViewMode(viewMode) ? new.getViewMode().equals(viewMode)
   *                             : new.getViewMode().equals(VIEWMODE_DISPLAY);
   * @post    RobustCurrent.lookup(RESOLVER.handlerVariableNameFor(getType())) == this;
   * @throws  FatalFacesException
   *          getType() == null;
   * @throws  FatalFacesException
   *          getInstance() == null;
   *
   * @mudo (jand) security
   */
  public final void navigateHere(final String viewMode) throws FatalFacesException {
    LOG.debug("InstanceHandler.navigate called; id = " + getId()
              + ", instance = " + getInstance());
    if (getInstance() == null) {
      LOG.fatal("cannot navigate to detail, because no instance is set ("
                + this);
    }
    super.navigateHere(viewMode);
  }

  /**
   * A string identifying the page corresponding to this handler.
   *
   * @pre getType() != null;
   * @return VIEW_ID_PREFIX + s/\./\//(getType().getName()) + VIEW_ID_SUFFIX;
   */
  public String getViewId() {
    assert getPersistentBeanType() != null : "type cannot be null";
    String typeName = getPersistentBeanType().getName();
    typeName = typeName.replace('.', '/');
    return VIEW_ID_PREFIX + typeName + VIEW_ID_SUFFIX;
  }

  /**
   * Put this handler in session scope with name
   * {@link PersistentBeanHandlerResolver#handlerVariableNameFor(Class)
   *        PersistentBeanHandlerResolver#handlerVariableNameFor(getType())}.
   *
   * @post  RESOLVER.putInSessionScope(this);
   */
  public void putInSessionScope() {
    RESOLVER.putInSessionScope(this);
  }

  /**
   * <p>Action method to navigate to collection page for this
   *   {@link #getPersistentBeanType()} via a call to {@link CollectionHandler#navigateHere()}.</p>
   * <p>The collection handler handler is made available to the JSP/JSF page in request scope,
   *   as a variable with the appropiate name (see {@link CollectionHandler#navigateHere()}.
   *   And we navigate to {@link CollectionHandler#getViewId()}.</p>
   *
   * @post    (CollectionHandler)CollectionHandler.RESOLVER.handlerFor(
   *              getType(), getDao()).navigateHere();
   * @except  (CollectionHandler)CollectionHandler.RESOLVER.handlerFor(
   *              getType(), getDao()).navigateHere();
   */
  public final void navigateToList(final ActionEvent aEv) throws FatalFacesException {
    LOG.debug("InstanceHandler.navigateToList called");
    CollectionHandler handler =
      (CollectionHandler)CollectionHandler.RESOLVER.handlerFor(
          getPersistentBeanType(), getDaoVariableName());
    LOG.debug("created collectionhandler for type " + getPersistentBeanType()
              + ": " + handler);
    handler.navigateHere(aEv);
  }

  /**
   * This is an action method that should be called by a button in the JSF
   * page to go to edit mode.
   *
   * A more detailed description of this action method can be found in the
   * class description.
   *
   * @post (getInstance() == null)
   *         ? 'return to previous page' &&
   *           result.equals(NO_INSTANCE)
   *         : true;
   * @post (getInstance() != null && !getViewMode().equals(VIEWMODE_DISPLAY))
   *         ? getViewMode().equals(VIEWMODE_DISPLAY) &&
   *           result.equals(null)
   *         : true;
   * @post (getInstance() != null && getViewMode().equals(VIEWMODE_DISPLAY))
   *         ? getViewMode().equals(VIEWMODE_EDIT) &&
   *           result.equals(null)
   *         : true;
   * @mudo (jand) security
   *
  public final String edit();
   */

  /**
   * This is an action method that should be called by a button in the JSF
   * page to update a persistent bean in persistent storage.
   *
   * A more detailed description of this action method can be found in the
   * class description.
   *
   * @post    (getInstance() == null)
   *            ? 'return to previous page' &&
   *              result.equals(NO_INSTANCE)
   *            : true;
   * @post    (getInstance() != null && !getViewMode().equals(VIEWMODE_EDIT))
   *            ? getViewMode().equals(VIEWMODE_DISPLAY) &&
   *              result.equals(null)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_EDIT)
   *               && 'updateValues generated messages')
   *            ? getViewMode().equals(VIEWMODE_EDIT) &&
   *              result.equals(null)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_EDIT)
   *               && 'updateValues succeeded without messages'
   *               && 'update in storage generates no semantic exceptions')
   *            ? 'bean is updated in persistent storage' &&
   *              getViewMode().equals(VIEWMODE_DISPLAY) &&
   *              result.equals(null)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_EDIT)
   *               && 'updateValues succeeded without messages'
   *               && 'update in storage generates semantic exceptions')
   *            ? getViewMode().equals(VIEWMODE_EDIT) &&
   *              result.equals(null)
   *            : true;
   * @throws  FatalFacesException
   *          When a TechnicalException is thrown by:
   *          {@link AsyncCrudDao#startTransaction()}
   *          {@link AsyncCrudDao#updatePersistentBean(be.peopleware.persistence_II.PersistentBean)}
   *          {@link AsyncCrudDao#commitTransaction(be.peopleware.persistence_II.PersistentBean)}
   *          {@link AsyncCrudDao#cancelTransaction()}
   *
  public String update() throws FatalFacesException;
   */

  /**
   * The actual update in persistent storage. No need to handle transaction:
   * it is open.
   *
   * @todo (jand) for now, this method should start and commit the transaction;
   *        one the commitTransaction method no longer needs the stupid
   *        PB argument, this can be moved out.
   */
  protected PersistentBean actualUpdate(final AsyncCrudDao dao)
      throws CompoundPropertyException, TechnicalException, FatalFacesException {
    dao.updatePersistentBean(getInstance()); // TechnicalException, CompoundPropertyException
    return getInstance(); // TODO (jand) stupid return stuff
  }

  /**
   * This method should be called from within another handler to pass
   * a newly created persistent bean of type {@link #getPersistentBeanType()} and show this
   * bean in editNew mode. To do this, the handler should be properly
   * initialised.
   *
   * A more detailed description of this action method can be found in the
   * class description.
   *
   * To initialise the handler properly, the following two steps are taken:
   * 1. The given {@link PersistentBean} is stored in {@link #getInstance()}.
   *    If the given bean is not effective, has an effective id (i.e. is not
   *    newly created), or is not of type {@link #getPersistentBeanType()}, this is signalled
   *    to the user by throwing an InvalidBeanException.
   * 2. We go to editNew mode.
   *
   * This method calls {@link #instanceChanged()} when the new instance is set.
   *
   * @param   instance
   *          The {@link PersistentBean} that should be displayed.
   * @post    new.getInstance() == instance;
   * @post    new.getViewMode().equals(VIEWMODE_EDITNEW);
   * @throws  InvalidBeanException
   *          instance == null ||
   *          instance.getId() != null ||
   *          !getType().isInstance(instance);
   */
  public final void editNew(final PersistentBean instance) throws InvalidBeanException {
    LOG.debug("InstanceHandler.editNew called; a new instance is stored in the handler");
    if (instance == null
        || instance.getId() != null
        || !getPersistentBeanType().isInstance(instance)) {
      throw new InvalidBeanException(instance, getPersistentBeanType());
    }
    $instance = instance;
    assert getInstance() != null;
    assert getInstance().getId() == null;
    assert getPersistentBeanType().isInstance(instance);
    instanceChanged();
    setViewMode(VIEWMODE_EDITNEW);
    LOG.debug("Stored new persistent bean successfully");
  }

  /**
   * A class of exceptions containing a persistent bean and a type.
   */
  public class InvalidBeanException extends Exception {

    /**
     * Create a new {@link InvalidBeanException} with the given persistent bean
     * and type.
     *
     * @post  new.getPersistentBean() == persistentBean;
     * @post  new.getType() == type;
     */
    public InvalidBeanException(final PersistentBean persistentBean, final Class type) {
      $persistentBean = persistentBean;
      $type = type;
    }

    /**
     * The persistent bean.
     *
     * @basic
     */
    public PersistentBean getPersistentBean() {
      return $persistentBean;
    }

    /**
     * The type of the persistent bean.
     *
     * @basic
     */
    public Class getType() {
      return $type;
    }

    private PersistentBean $persistentBean;
    private Class $type;
  }

  /**
   * This is an action method that should be called by a button in the JSF
   * page to add a newly created persistent bean to persistent storage.
   *
   * A more detailed description of this action method can be found in the
   * class description.
   *
   * @post    (getInstance() == null)
   *            ? 'return to previous page' &&
   *              result.equals(NO_INSTANCE)
   *            : true;
   * @post    (getInstance() != null && !getViewMode().equals(VIEWMODE_EDITNEW))
   *            ? 'return to previous page' &&
   *              result.equals(INCORRECT_VIEWMODE)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_EDITNEW)
   *               && 'updateValues generated messages')
   *            ? getViewMode().equals(VIEWMODE_EDITNEW) &&
   *              result.equals(null)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_EDITNEW)
   *               && 'updateValues succeeded without messages'
   *               && 'update in storage generates no semantic exceptions')
   *            ? 'bean is created in persistent storage' &&
   *              new.getId() == new.getInstance().getId()
   *              getViewMode().equals(VIEWMODE_DISPLAY) &&
   *              result.equals(null)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_EDITNEW)
   *               && 'updateValues succeeded without messages'
   *               && 'update in storage generates semantic exceptions')
   *            ? getViewMode().equals(VIEWMODE_EDITNEW) &&
   *              result.equals(null)
   *            : true;
   * @throws  FatalFacesException
   *          When a TechnicalException is thrown by:
   *          {@link AsyncCrudDao#startTransaction()}
   *          {@link AsyncCrudDao#createPersistentBean(be.peopleware.persistence_II.PersistentBean)}
   *          {@link AsyncCrudDao#commitTransaction(be.peopleware.persistence_II.PersistentBean)}
   *          {@link AsyncCrudDao#cancelTransaction()}
   */
  public final String create() throws FatalFacesException {
    LOG.debug("InstanceHandler.create called; a new bean is created and is "
        + "already partially updated");
    LOG.debug("persistentBean: " + getInstance());
    try {
      AsyncCrudDao dao = getAsyncCrudDao();
      try {
        if (getInstance() == null) {
          return goBack(NO_INSTANCE);
        }
        if (!getViewMode().equals(VIEWMODE_EDITNEW)) {
          return goBack(INCORRECT_VIEWMODE);
        }
        updateValues();
        LOG.debug("The bean is now fully updated");
        LOG.debug("persistentBean: " + getInstance());
        if (RobustCurrent.hasMessages()) {
          // updateValues can create FacesMessages that signal semantic errors
          return null;
        }
        else {
          dao.startTransaction(); // TechnicalException
          dao.createPersistentBean(getInstance()); // TechnicalException, CompoundPropertyException
          dao.commitTransaction(getInstance()); // TechnicalException, CompoundPropertyException
          assert getInstance().getId() != null;
          setId(getInstance().getId());
          setViewMode(VIEWMODE_DISPLAY);
          return null;
        }
      }
      catch (CompoundPropertyException cpExc) {
        LOG.debug("create action failed; cancelling ...", cpExc);
        dao.cancelTransaction(); // TechnicalException
        LOG.debug("create action cancelled; using exception as faces message");
        RobustCurrent.showCompoundPropertyException(cpExc);
        setViewMode(VIEWMODE_EDITNEW);
        return null;
      }
    }
    catch (TechnicalException exc) {
      RobustCurrent.fatalProblem("Could not create " + getInstance(), exc, LOG);
      return null;
    }
  }

  /**
   * The navigation string used when no persistent bean is attached to this
   * handler.
   *
   * <strong>= &quot;NO_INSTANCE&quot;</strong>
   */
  public static final String NO_INSTANCE = "NO_INSTANCE";

  /**
   * The navigation string used when an action cannot be executed in the current
   * view mode.
   *
   * <strong>= &quot;INCORRECT_VIEWMODE&quot;</strong>
   */
  public static final String INCORRECT_VIEWMODE = "INCORRECT_VIEWMODE";

  /**
   * The navigation string used when creating a new persistent bean is cancelled.
   *
   * <strong>= &quot;CANCEL_EDITNEW&quot;</strong>
   */
  public static final String CANCEL_EDITNEW = "CANCEL_EDITNEW";

  /**
   * This is an action method that should be called by a button in the JSF
   * page to delete a persistent bean from persistent storage.
   *
   * A more detailed description of this action method can be found in the
   * class description.
   *
   * @post    (getInstance() == null)
   *            ? 'return to previous page' &&
   *              result.equals(NO_INSTANCE)
   *            : true;
   * @post    (getInstance() != null && !getViewMode().equals(VIEWMODE_DISPLAY))
   *            ? getViewMode().equals(VIEWMODE_DISPLAY) &&
   *              result.equals(null)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_DISPLAY)
   *               && 'delete in storage generates no semantic exceptions')
   *            ? 'bean is deleted from persistent storage' &&
   *              getViewMode().equals(VIEWMODE_DELETED) &&
   *              result.equals(null)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_DISPLAY)
   *               && 'delete in storage generates semantic exceptions')
   *            ? getViewMode().equals(VIEWMODE_DISPLAY) &&
   *              result.equals(null)
   *            : true;
   * @throws  FatalFacesException
   *          When a TechnicalException is thrown by:
   *          {@link AsyncCrudDao#startTransaction()}
   *          {@link AsyncCrudDao#deletePersistentBean(be.peopleware.persistence_II.PersistentBean)}
   *          {@link AsyncCrudDao#commitTransaction(be.peopleware.persistence_II.PersistentBean)}
   *          {@link AsyncCrudDao#cancelTransaction()}
   */
  public final String delete() throws FatalFacesException {
    LOG.debug("InstanceHandler.delete() called");
    LOG.debug("persistentBean: " + getInstance());
    AsyncCrudDao dao = getAsyncCrudDao();
    try {
      try {
        checkConditions(VIEWMODE_DISPLAY); // ConditionException
        dao.startTransaction(); // TechnicalException
        dao.deletePersistentBean(getInstance()); // TechnicalException
        dao.commitTransaction(getInstance()); // TechnicalException, CompoundPropertyException
        assert getInstance().getId() == null;
        setId(null);
        setViewMode(VIEWMODE_DELETED);
        return getNavigationString();
      }
      catch (ConditionException exc) {
        return exc.getNavigationString();
      }
      catch (CompoundPropertyException cpExc) {
        LOG.debug("delete action failed; cancelling ...", cpExc);
        dao.cancelTransaction(); // TechnicalException
        LOG.debug("delete action cancelled; using exception as faces message");
        RobustCurrent.showCompoundPropertyException(cpExc);
        setViewMode(VIEWMODE_DISPLAY);
        return null;
      }
    }
    catch (TechnicalException exc) {
      RobustCurrent.fatalProblem("Could not delete " + getInstance(), exc, LOG);
      return null;
    }
  }

  /**
   * Helper method used in action methods to check whether the persistent bean
   * is effective and whether the current view mode corresponds to the
   * view mode in which the action method should be called.
   * If one of these conditions is not met, appropriate actions are taken.
   *
   * When {@link #getInstance()} is <code>null</code>, we return to a previous
   * page.
   * When the {@link #getInstance()} is effective, but the current view mode
   * does not correspond to the given expected view mode, we go to display mode.
   * In both cases, a ConditionException is thrown that contains the
   * navigation string.
   *
   * @param   expectedViewMode
   *          The view mode that should be checked.
   * @pre     isViewMode(expectedViewMode);
   * @post    true
   * @throws  ConditionException exc
   *          getInstance() == null
   *            && exc.getOutcome().equals(NO_INSTANCE);
   *          As a side effect, we return to a previous page.
   * @throws  ConditionException exc
   *          getInstance() != null && !getViewMode().equals(expectedViewMode)
   *            && exc.getOutcome().equals(display());
   *          As a side effect, we go to display mode.
   */
  protected void checkConditions(final String expectedViewMode) throws ConditionException {
    super.checkConditions(expectedViewMode);
    if (getInstance() == null) {
      goBack(NO_INSTANCE);
      throw new ConditionException(NO_INSTANCE);
    }
  }

  /**
   * Method to be called after Render Response phase, to clear
   * semantic data from the session.
   * This method calls {@link #instanceChanged()} when the new instance is set.
   *
   * @post getInstance() == null;
   */
  void release() {
    releaseInstance();
    // mudo (jand) more code or remove?
  }

  /**
   * This method returns to the page that was visited before this page.
   * It is possible that this previous page cannot be displayed anymore
   * (e.g. because the corresponding {@link PersistentBean} does not exist in
   * persistent storage anymore), so this process is recursive, until the first
   * page after login is reached.
   * The method returns the navigation string needed to navigate to the
   * previous page
   *
   * @param   message
   *          A message signalling why we are going back to a previous page.
   * @mudo
   */
  public String goBack(final String message) {
//  MUDO
    return message;
  }

  /**
   *
   */
  public String goBack() {
    LOG.debug("goBack method called");
    return null;
  }

  /**
   * This is an action method that should be called by a button in the JSF
   * page to cancel the update of an existing persistent bean
   * (i.e. a persistent bean that was loaded from persistent storage,
   * meaning that {@link #getInstance()} <code>!=null</code> and
   * {@link #getInstance()}.{@link #getId()} <code>!=null</code>).
   *
   * A more detailed description of this action method can be found in the
   * class description.
   *
   * @post    (getInstance() == null)
   *            ? 'return to previous page' &&
   *              result.equals(NO_INSTANCE)
   *            : true;
   * @post    (getInstance() != null && !getViewMode().equals(VIEWMODE_EDIT))
   *            ? getViewMode().equals(VIEWMODE_DISPLAY) &&
   *              result.equals(null)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_EDIT))
   *            ? 'reset the UI components' &&
   *              getViewMode().equals(VIEWMODE_DISPLAY) &&
   *              result.equals(null)
   *            : true;
   *
  public final String cancelEdit();
   */

  /**
   * This is an action method that should be called by a button in the JSF
   * page to cancel the creation of a new persistent bean.
   * (Remark: a 'new persistent bean' is a persistent bean that was created in
   * memory but not saved in persistent storage yet).
   *
   * A more detailed description of this action method can be found in the
   * class description.
   *
   * @post    'return to previous page'
   * @post    (getInstance() == null)
   *            ? result.equals(NO_INSTANCE)
   *            : true;
   * @post    (getInstance() != null && !getViewMode().equals(VIEWMODE_EDITNEW))
   *            ? result.equals(INCORRECT_VIEWMODE)
   *            : true;
   * @post    (getInstance() != null && getViewMode().equals(VIEWMODE_EDITNEW))
   *            ? result.equals(CANCEL_EDITNEW)
   *            : true;
   */
  public final String cancelEditNew() {
    LOG.debug("InstanceHandler.cancelEditNew called; returning to previous page");
    LOG.debug("persistentBean: " + getInstance());
    if (getInstance() == null) {
      return goBack(NO_INSTANCE);
    }
    if (!getViewMode().equals(VIEWMODE_EDITNEW)) {
      return goBack(INCORRECT_VIEWMODE);
    }
    return goBack(CANCEL_EDITNEW);
  }


  /*<section name="Association Handlers">*/
  //------------------------------------------------------------------
  /**
   * <p>{@link PersistentBean} instances are often related to other {@link PersistentBean}
   *   instances, according to the same pattern (a bidirectional one-to-many
   *   association). These relations need to be navigatable and editable.</p>
   * <p>For the to-many associations, there is a property in the {@link PersistentBean instance}
   *   that returns a {@link Collection} of other {@link PersistentBean PersistentBeans}.
   *   We often want to show this collection in some way, and interact with it, via
   *   the web interface. This is handled by a {@link CollectionHandler}.
   *   In the handler that wraps around the original instance (an object of this class),
   *   we need a way to create and access such a
   *   {@link CollectionHandler list handler}. Below you will find code
   *   to do this via reflection, automatically. Different
   *   {@link CollectionHandler list handlers} can be accessed through
   *   a (fake) {@link Map}, where the key is the property name of the JavaBean
   *   property that returns the collection of associated {@link PersistentBean PersistentBeans}
   *   from the instance this handler works for.</p>
   */
  public final Map getAssociationHandlers() {
    return $associationHandlers;
  }

  /**
   * The association handlers that are actually created already,
   * because we got a request for them through {@link #getAssociationHandlers()}.
   *
   * @return $associationHandlers.getInitializedAssociationHandlers();
   */
  public final Map getUsedAssociationHandlers() {
    return $associationHandlers.getInitializedAssociationHandlers();
  }

  /**
   * Remove the association handler corresponding to the given property name.
   *
   * @post !new.getAssociationHandlers().containsKey(propertyName);
   */
  public final void removeUsedAssociationHandlerFor(final String propertyName) {
    $associationHandlers.removeInitializedAssociationHandlerFor(propertyName);
  }

  /**
   * Alias for {@link #getAssociationHandlers()} with a shorter name.
   */
  public final Map getAssocH() {
    return getAssociationHandlers();
  }

  private final class AutomaticAssociationHandlersMap extends AbstractUnmodifiableMap
      implements Removable, Skimmable {

    public Set keySet() {
      if ($keySet == null) {
        initKeySet();
      }
      return $keySet;
    }

    private void initKeySet() {
      $keySet = new HashSet();
      $keySet.addAll(getAssociationMetaMap().keySet());
      PropertyDescriptor[] descriptors =
        PropertyUtils.getPropertyDescriptors(getPersistentBeanType());
      for (int i = 0; i < descriptors.length; i++) {
        PropertyDescriptor pd = descriptors[i];
        if (PersistentBean.class.isAssignableFrom(pd.getPropertyType())) {
          $keySet.add(pd.getDisplayName());
        }
      }
    }

    private HashSet $keySet;

    /**
     * The association handlers that are actually created already,
     * because we got a request for them through {@link #get(Object)}.
     */
    public Map getInitializedAssociationHandlers() {
      return Collections.unmodifiableMap($backingMap);
    }

    private Map $backingMap = new HashMap();

    public void removeInitializedAssociationHandlerFor(final String propertyName) {
      $backingMap.remove(propertyName);
    }

    public Object get(final Object key) throws FatalFacesException {
      if (!keySet().contains(key)) {
        LOG.warn("request for associations handler with unknown key (property name) \""
                 + key + "\"; returning null");
        return null;
      }
      Object result = $backingMap.get(key);
      if (result == null) {
        String propertyName = (String)key;
        try {
          Object propertyValue = PropertyUtils.getProperty(getInstance(), propertyName);
          if (propertyValue == null) {
            LOG.debug("propertyValue is null; cannot create handler, returning null");
            return null;
          }
          if (propertyValue instanceof PersistentBean) {
            result = freshPersistentBeanInstanceHandlerFor((PersistentBean)propertyValue);
          }
          else if (propertyValue instanceof Collection) {
            Class associatedType = (Class)getAssociationMetaMap().get(propertyName);
            result = freshCollectionHandlerFor(associatedType, (Collection)propertyValue);
          }
          else {
            LOG.warn("Property \"" + propertyName + "\" is not a PersistentBean or a "
                     + "Collection; returning null");
            return null;
          }
          $backingMap.put(key, result);
        }
        catch (ClassCastException ccExc) {
          RobustCurrent.fatalProblem(
              "Could not get persistentbean for property \"" + propertyName + "\"",
              ccExc, LOG);
        }
        catch (IllegalArgumentException iaExc) {
          RobustCurrent.fatalProblem(
              "Could not get property \"" + propertyName + "\"", iaExc, LOG);
        }
        catch (IllegalAccessException iaExc) {
          RobustCurrent.fatalProblem(
              "Could not get property \"" + propertyName + "\"", iaExc, LOG);
        }
        catch (InvocationTargetException itExc) {
          RobustCurrent.fatalProblem(
              "Could not get property \"" + propertyName + "\"", itExc, LOG);
        }
        catch (NullPointerException npExc) {
          RobustCurrent.fatalProblem(
              "Could not get property \"" + propertyName + "\"", npExc, LOG);
        }
        catch (ExceptionInInitializerError eiiErr) {
          RobustCurrent.fatalProblem(
              "Could not get property \"" + propertyName + "\"", eiiErr, LOG);
        }
        catch (NoSuchMethodException nsmExc) {
          RobustCurrent.fatalProblem(
              "Could not get property \"" + propertyName + "\"", nsmExc, LOG);
        }
      }
      return result;
    }

    private InstanceHandler freshPersistentBeanInstanceHandlerFor(final PersistentBean pb)
        throws FatalFacesException, IllegalArgumentException {
      InstanceHandler pbch =
        (InstanceHandler)RESOLVER.freshHandlerFor(pb.getClass(), getDaoVariableName());
      pbch.setInstance(pb);
      return pbch;
    }

    private CollectionHandler freshCollectionHandlerFor(
        final Class associatedType, final Collection c) throws FatalFacesException {
      CollectionHandler ch =
          (CollectionHandler)CollectionHandler.RESOLVER.freshHandlerFor(
              associatedType, getDaoVariableName());
      ch.setInstances(c);
      return ch;
    }

    /**
     * All cached association handlers are to be removed.
     */
    public boolean isToBeRemoved() {
      Iterator iter = $backingMap.values().iterator();
      while (iter.hasNext()) {
        PersistentBeanHandler pbH = (PersistentBeanHandler)iter.next();
        if (!pbH.isToBeRemoved()) {
          LOG.debug("cached association handler " + pbH + " is not to be removed");
          return false;
        }
      }
      LOG.debug("all cached association handlers are to be removed");
      return true;
    }

    public void skim() {
      $keySet = null;
      List $cachedEntries = new LinkedList($backingMap.entrySet());
      Iterator iter = $cachedEntries.iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        PersistentBeanHandler pbH = (PersistentBeanHandler)entry.getValue();
        if (pbH.isToBeRemoved()) {
          LOG.debug("    cached association handler " + pbH + " is to be removed");
          $backingMap.remove(entry.getKey());
        }
        else {
          LOG.debug("    cached assocation handler " + pbH + " will be skimmed");
          pbH.skim();
        }
      }
    }

  }

  private final AutomaticAssociationHandlersMap $associationHandlers =
    new AutomaticAssociationHandlersMap();

  /**
   * <p>The automated {@link #getAssociationHandlers() association handlers map} requires
   *   some meta information about the relations that instances of {@link #getPersistentBeanType()}
   *   are involved in. This map should list entries with the property name
   *   that represents the association as key, and the type of the related elements
   *   as value.</p>
   * <p>Since this is meta information, the implementation should probably use a
   *   class variable instead of an instance variable. The implementation could look
   *   like this:</p>
   * <pre>
   *   public final static Map ASSOCIATIONS_META_MAP = new HashMap();
   *
   *   static {
   *     ASSOCIATIONS_META_MAP.put("groups", Group.class);
   *     ASSOCIATIONS_META_MAP.put("friends", Person.class);
   *     ASSOCIATIONS_META_MAP.put("hobbies", Things.class);
   *   }
   *
   *   protected Map getAssociationMetaMap() {
   *    return ASSOCIATIONS_META_MAP;
   *   }
   * </pre>
   * <p>This default implementation returns an empty map. This method should never
   *   return <code>null</code>.</p>
   *
   * @result result != null;
   * @todo (jand) more contract
   *
   * @idea with 1.5 we will probably no longer need this meta information
   * @todo (jand) map should be made unmodifiable in example
   */
  protected Map getAssociationMetaMap() {
    return Collections.EMPTY_MAP;
  }

  /*</section>*/

  /**
   * The resolver.
   *
   * @invar RESOLVER.getHandlerDefaultClass() == InstanceHandler.class;
   * @invar RESOLVER.getHandlerVarNameSuffix().equals(EMPTY);
   */
  public static final PersistentBeanHandlerResolver RESOLVER =
      new PersistentBeanHandlerResolver(InstanceHandler.class, "");



  /*<section name="skimmable">*/
  //------------------------------------------------------------------

  /**
   * Sets the {@link #getStoredInstance() stored instance} to <code>null</code>
   * if the {@link #getId() id} is not <code>null</code>.
   * The {@link #getAssociationHandlers() associations handlers} are checked.
   * If they are {@link Removable}, and they {@link Removable#isToBeRemoved()
   * want to be removed}, they are removed from the backing cache of
   * association handlers. If they are not removed, and are {@link Skimmable},
   * they are {@link Skimmable#skim() skimmed}.
   * This method calls {@link #instanceChanged()} when the new instance is set.
   */
  public void skim() {
    super.skim();
    if (getId() != null) {
      LOG.debug("skimming instance");
      releaseInstance();
    }
    LOG.debug("skimming cached association handlers");
    $associationHandlers.skim();
  }

  /*</section>*/



  /*<section name="removable">*/
  //------------------------------------------------------------------

  /**
   * @return (getViewMode().equals(VIEWMODE_DISPLAY) ||
   *              getViewMode().equals(VIEWMODE_DELETED)) &&
   *            getAssociationHandler().isToBeRemoved();
   */
  public boolean isToBeRemoved() {
    boolean result = (getViewMode().equals(VIEWMODE_DISPLAY)
                       || getViewMode().equals(VIEWMODE_DELETED))
                     && $associationHandlers.isToBeRemoved();
    LOG.debug(this.toString() + ".isToBeRemoved() = " + result);
    return result;
  }

  /*</section>*/



  /*<section name="navigationInstance">*/
  //------------------------------------------------------------------


  /**
   * @result (ni == null) ? (result == null);
   * @result (result != null) ? (result == this);
   * @return ((getClass() == ni.getClass())
   *             && (getPersistentBeanType() == ((InstanceHandler)ni).getPersistentBeanType())
   *             && (equalsWithNull(getId(), ((InstanceHandler)ni).getId())
   *                    || ((getId() == null) && (((InstanceHandler)ni).getId() != null)))
   *           ? this
   *           : null;
   *         The last part of the condition takes care of creating a new instance: this previous
   *         NavigationInstance was the page with no id, and after succesful creation, we do have
   *         an id, but it is the same page.
   * @result (result != null) ? getTime().equals(NOW);
   */
  public NavigationInstance absorb(final NavigationInstance ni) {
    LOG.debug("request to absorb " + ni + " by " + this);
    if (ni == null) {
      return null;
    }
    else if ((getClass() == ni.getClass())
             && (getPersistentBeanType() == ((InstanceHandler)ni).getPersistentBeanType())
             && (equalsWithNull(getId(), ((InstanceHandler)ni).getId())
                 ||
                 ((getId() == null) && (((InstanceHandler)ni).getId() != null))
                 ||
                 ((getId() != null) && (((InstanceHandler)ni).getId() == null))
                )) {
      LOG.debug("absorbing");
      resetLastRenderedTime();
      if ((getId() == null)
          &&
          (((InstanceHandler)ni).getId() != null)
          ||
          ((getId() != null) && (((InstanceHandler)ni).getId() == null))
      ) {
        // we have just created a new instance; copy the id
        setId(((InstanceHandler)ni).getId());
      }
      return this;
    }
    else {
      LOG.debug("not absorbing");
      return null;
    }
  }

  /**
   * Is one value {@link Object#equals(java.lang.Object) equal}
   * to another, if <code>null</code> is also allowed as value
   * for a property.
   *
   * @return    (one == null)
   *                ? (other == null)
   *                : one.equals(other);
   *
   * @idea (jand) move to ppw-util or toryt
   */
  private static boolean equalsWithNull(final Object one, final Object other) {
    return (one == null)
              ? (other == null)
              : one.equals(other);
  }

  /*</section>*/

}
