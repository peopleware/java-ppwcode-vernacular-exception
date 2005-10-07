/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.persistence;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.util.DynamicComparatorChain;
import be.peopleware.persistence_II.PersistentBean;
import be.peopleware.persistence_II.dao.AsyncCrudDao;
import be.peopleware.servlet.navigation.NavigationInstance;


/**
 * <p>Common functionality of JavaServer Faces backing beans that process requests
 *   for a collection of {@link PersistentBean PersistentBeans}.</p>
 * <p>If the collection is {@link #setInstances(Collection) set},
 *   that collection is used. If {@link #getStoredInstances()} returns <code>null</code>
 *   during operation, all instances of type {@link #getPersistentBeanType()} are retrieved
 *   from persistent storage using {@link #getAsyncCrudDao()} to work with.</p>
 * <h3>Creation of New Instances</h3>
 * <p>From a collection view, fresh instances for the corresponding type can be
 *   created. This means that an {@link InstanceHandler} for the appropriate type
 *   is created, and that we navigate there in {@link InstanceHandler#VIEWMODE_EDITNEW}.
 *   The user then gets the chance to fill out the fields for the fresh instance.
 *   This new handler needs to be provided with a fresh instance, where some
 *   fields can already be set by us. This functionality is offered to programmers
 *   by the method {@link #navigateToEditNew(PersistentBean)}.</p>
 * <p>End users call this functionality through an action method. This action method
 *   offers a fresh instance to {@link #navigateToEditNew(PersistentBean)}.
 *   How this new instance is configured, differs from case to case. Thus,
 *   programmers will have to write a distinct action method for each case that
 *   calls {@link #navigateToEditNew(PersistentBean)}.</p>
 * <p>A special case is the creation of a fresh unrelated instance. This happens often
 *   from a page that shows all instances of a type. For this case, an
 *   {@link #navigateToEditNew() action method}
 *   is offered that creates a fresh instance of type {@link #getPersistentBeanType()} with the
 *   default constructor.</p>
 * <h3>Sorting</h3>
 * <p>{@link #getInstances() Instances} are sorted with the {@link #getComparator() comparator}.
 *   The action listener method {@link #sort(ActionEvent)} is available
 *   for end users to control the sort order. The default {@link #getComparator()} sorts on
 *   {@link PersistentBean#getId()}, which is not very end-user friendly.</p>
 *
 * @note Can only be used as a navigation instance when we want to show all
 *       records of this type for now.
 *
 * @author     David Van Keer
 * @author     Peopleware n.v.
 *
 * @toryt cC:org.toryt.contract.Collections
 * @invar getInstances() != null;
 * @invar cC:instanceOf(getInstances(), getPersistentBeanType());
 * @invar cC:noNull(getInstances());
 *
 * @mudo (jand) this must be adapted to be more than display; also edit-in-grid
 */
public abstract class CollectionHandler extends PersistentBeanHandler {

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


  private static final Log LOG = LogFactory.getLog(CollectionHandler.class);



  /*<property name="instances">*/
  //------------------------------------------------------------------

  /**
   * If {@link #getStoredInstances()} is not <code>null</code>,
   * return that Collection; otherwise, retrieve all instances
   * of type {@link #getPersistentBeanType()} from persistent storage, using
   * {@link #getAsyncCrudDao()}. Only in this case, {@link #isSubtypesIncluded()}
   * is used.
   * The returned collection is sorted by {@link #getComparator()}.
   * Instances are {@link #skim() skimmed} always.
   * @mudo (jand) this skimming is probably not ok
   *
   * @return a sorted version of {@link #getStoredInstances()}
   * @throws FatalFacesException
   *         (getStoredInstances() != null) &&
   *             ((getType() == null) || (getDoa() == null));
   */
  public final Collection getInstances() throws FatalFacesException {
    LOG.debug("request for instances collection");
    if ($storedInstances == null) {
      $storedInstances = loadAllInstances();
    }
    else {
      LOG.debug("returing stored instances");
    }
    return getStoredInstances();
  }

  private Set loadAllInstances() throws FatalFacesException {
    LOG.debug("no instances collection set; will try to retrieve data from storage");
    if (getPersistentBeanType() == null) {
      RobustCurrent.fatalProblem("type is unknown", LOG);
    }
    if (getAsyncCrudDao() == null) {
      RobustCurrent.fatalProblem("dao is unknown", LOG);
    }
    LOG.debug("will try to load all instances of type \"" + getPersistentBeanType()
              + "\" with dao " + getAsyncCrudDao());
    Set result = Collections.EMPTY_SET;
    try {
      // we are not doing this in a transaction, deliberately
      result = getAsyncCrudDao().retrieveAllPersistentBeans(
                     getPersistentBeanType(), isSubtypesIncluded());
    }
    catch (TechnicalException tExc) {
      RobustCurrent.fatalProblem("Failed to retrieve all PersistenBeans of type \""
                                 + getPersistentBeanType() + "\" with dao "
                                 + getAsyncCrudDao(), tExc, LOG);
    }
    return result;
  }

  /**
   * Return the collection of {@link PersistentBean PersistentBeans},
   * which was set by means of {@link #setInstances(Collection)}.
   *
   * @basic
   * @init null;
   */
  public final Collection getStoredInstances() {
    return Collections.unmodifiableCollection($storedInstances);
  }

  /**
   * Set a {@link Collection} containing {@link PersistentBean PersistentBeans} as
   * the instances of this Handler.
   *
   * @pre  getType() != null;
   * @pre  cC:instanceOf(instances, getType());
   * @post cC:hasSameContents(new.getStoredInstances(), instances);
   */
  public final void setInstances(final Collection instances) {
    LOG.debug("Setting " + instances + " as instances");
    assert getPersistentBeanType() != null;
    $storedInstances = (instances == null) ? null : new ArrayList(instances);
  }

  // MUDO (jand) we probably need to retain the collection of id's sometimes
  private void releaseInstances() {
    $storedInstances = null;
  }

  private Collection $storedInstances;

  /*</property>*/



  /*<property name="subtypesIncluded">*/
  //------------------------------------------------------------------

  /**
   * Whether we will retrieve instances of only {@link #getPersistentBeanType()},
   * or also of all subtypes of {@link #getPersistentBeanType()}.
   * <strong>Note that the default value is <code>true</code></strong>
   * (where it is <code>false</code> for most boolean properties).
   *
   * @basic
   * @init true;
   */
  public final boolean isSubtypesIncluded() {
    return $subtypesIncluded;
  }

  /**
   * Set whether we will retrieve instances of only {@link #getPersistentBeanType()},
   * or also of all subtypes of {@link #getPersistentBeanType()}.
   *
   * @post getType() == type;
   */
  public final void setSubtypesIncluded(final boolean subtypesIncluded) {
    $subtypesIncluded = subtypesIncluded;
  }

  /**
   * Whether we will retrieve instances of only {@link #getPersistentBeanType()},
   * or also of all subtypes of {@link #getPersistentBeanType()}.
   */
  private boolean $subtypesIncluded = true;

  /*</property>*/


  /**
   * The actual update in persistent storage. No need to handle rollback;
   *
   * @todo (jand) for now, this method should start and commit the transaction;
   *        one the commitTransaction method no longer needs the stupid
   *        PB argument, this can be moved out.
   */
  protected PersistentBean actualUpdate(final AsyncCrudDao dao)
      throws CompoundPropertyException, TechnicalException, FatalFacesException {
    PersistentBean stupidResult = null;
    Iterator iter = getInstanceHandlers().iterator();
    while (iter.hasNext()) {
      InstanceHandler ih = (InstanceHandler)iter.next();
      stupidResult = ih.actualUpdate(dao);
    }
    return stupidResult; // hack, returning last; just for stupid dao.commitTransaction
  }



  /*<property name="creatable">*/
  //------------------------------------------------------------------

  /**
   * Can we create a new object of the type represented by this form?
   *
   * @basic
   * @init      false;
   */
  public final boolean isCreateable() {
    return $createable;
  }

  /**
   * @param     createable
   *            Marks the persistent bean as a object that can be created.
   * @post      new.isCreateable() == createable;
   */
  protected void setCreateable(final boolean createable) {
    $createable = createable;
  }

  private boolean $createable;

  /*</property>*/



  /*<section name="sorting">*/
  //------------------------------------------------------------------

  /**
   * Return the comparator chain which is/can be used for sorting the
   * retrieved {@link PersistentBean}s. This is never
   * {@link #skim() skimmed}.
   */
  public DynamicComparatorChain getComparator() {
    return $comparator;
  }

  /**
   * Set a comparator for sorting the retrieved {@link PersistentBean}s.
   *
   * @param     comparator
   *            The new Comparator to use for sorting
   */
  public void setComparator(final DynamicComparatorChain comparator) {
    $comparator = comparator;
  }

  /**
   * <strong>= {@value}</strong>
   */
  public static final String SORT_PROPERTY_REQUEST_PARAMETER_NAME = "sortProperty";


  /**
   * <strong>= {@value}</strong>
   */
  public static final String EMPTY = "";

  /**
   * <p>Action listener method that calls {@link DynamicComparatorChain#bringToFront(String)}
   *   with the value of HTTP request parameter {@link #SORT_PROPERTY_REQUEST_PARAMETER_NAME}
   *   as argument. Since the previous order needs to be remembered, the {@link #getComparator()}
   *   is best implemented as a managed bean in session scope.</p>
   * <p>This action listener method could be called with a <code>commandLink</code>
   *   as follows:</p>
   * <pre>
   *   &lt;h:commandLink value=&quot;<var>label</var>&quot; actionListener=&quot;#{handler.sort}&quot; immediate=&quot;true&quot;&gt;
   *     &lt;f:param name=&quot;<var>{@link #SORT_PROPERTY_REQUEST_PARAMETER_NAME}</var>&quot; value=&quot;<var>property name</var>&quot; /&gt;
   *   &lt;/h:commandLink&gt;
   * </pre>
   * <p>If there is no {@link #getComparator()}, or there is no
   *   {@link #SORT_PROPERTY_REQUEST_PARAMETER_NAME} HTTP request parameter,
   *   nothing happens.</p>
   */
  public final void sort(final ActionEvent ae) {
    LOG.debug("call to sort");
    if (getComparator() == null) {
      LOG.warn("call to sort, but no comparator found");
      return;
    }
    String sortPropertyName =
      RobustCurrent.requestParameterValues(SORT_PROPERTY_REQUEST_PARAMETER_NAME)[0];
    if ((sortPropertyName == null) || (sortPropertyName.equals(EMPTY))) {
      LOG.warn("call to sort, but no sort property name found in HTTP request");
      return;
    }
    else {
      LOG.debug("sort property is " + sortPropertyName);
    }
    getComparator().bringToFront(sortPropertyName);
    if ($handlers != null) {
      Collections.sort($handlers, $handlerComparator);
    }
  }

  private DynamicComparatorChain $comparator = new DynamicComparatorChain();

  {
    $comparator.addComparator("id", null, true); // Long is Comparable
  }

  /*</section>*/

  /**
   * The name of the request parameter containing the id.
   *
   * <strong>= &quot;pbId&quot;</strong>
   */
  public static final String ID_REQUEST_PARAMETER_NAME = "pbId";

  /**
   * Returns a {@link List} containing all persistent beans in {@link #getInstances()}
   * wrapped in a corresponding {@link InstanceHandler}.
   * During {@link #skim() skimming}, the cache is removed.
   * @mudo (jand) This is probably wrong once we have modes.
   *
   * @return    Set
   *            A Set with PersistentBeanHandlers based on the instances of this Handler..
   * @throws    FatalFacesException
   *            getInstances();
   * @throws    FatalFacesException
   *            freshInstanceHandlerFor(bean);
   */
  public final List getInstanceHandlers() throws FatalFacesException {
    Collection instances = getInstances();
    if (isInstanceHandlersObsolete()) {
      LOG.debug("no handlers cached or collection changed; creating new handlers");
      List handlers = new ArrayList();
      Iterator iter = instances.iterator();
      LOG.debug("got instances");
      LOG.debug("creating handler for each instance");
      while (iter.hasNext()) {
        PersistentBean bean = (PersistentBean)iter.next();
        handlers.add(freshInstanceHandlerFor(bean));
      }
      $handlers = handlers;
      Collections.sort($handlers, $handlerComparator);
      LOG.debug("handlers created and cached");
    }
    else {
      LOG.debug("returning cached handlers");
    }
    return Collections.unmodifiableList($handlers);
  }

  protected boolean isInstanceHandlersObsolete() {
    return ($handlers == null) || ($handlers.size() != getInstances().size());
//  MUDO (jand) test if the contents is actually changed
  }

  /**
   * Delegate this call to all handlers in {@link #getInstanceHandlers()}.
   */
  protected void updateValues() {
    if (getInstanceHandlers() == null) {
      RobustCurrent.fatalProblem("cannot update values if there are no instance handlers", LOG);
    }
    Iterator iter = getInstanceHandlers().iterator();
    while (iter.hasNext()) {
      InstanceHandler ih = (InstanceHandler)iter.next();
      ih.updateValues();
    }
    super.updateValues();
  }

  /**
   * Create a fresh instance handler for <code>bean</code>.
   *
   * @throws FatalFacesException
   */
  protected InstanceHandler freshInstanceHandlerFor(final PersistentBean bean)
      throws FatalFacesException {
    LOG.debug("    instance is " + bean + "; RESOLVER is " + InstanceHandler.RESOLVER);
    InstanceHandler handler =
        (InstanceHandler)InstanceHandler.RESOLVER.freshHandlerFor(
            bean.getClass(), getDaoVariableName());
    handler.setInstance(bean);
    handler.setViewMode(PersistentBeanHandler.VIEWMODE_DISPLAY);
    LOG.debug("    handler is " + handler);
    return handler;
  }

  /**
   * A comparator that wraps around the {@link #getComparator()}, to
   * pass comparison through the handler to the instance.
   */
  private final transient Comparator $handlerComparator =
      new Comparator() {

            public int compare(final Object o1, final Object o2) {
              InstanceHandler h1 = (InstanceHandler)o1;
              InstanceHandler h2 = (InstanceHandler)o2;
              return getComparator().compare(h1.getInstance(), h2.getInstance());
            }

          };

  private List $handlers;

  /**
   * The name of the suffix .
   *
   * <strong>= &quot;_list&quot; + VIEW_ID_SUFFIX;</strong>
   */
  public static final String LIST_VIEW_ID_SUFFIX = "_list" + VIEW_ID_SUFFIX;

  /**
   *
   * @param event
   * @throws FatalFacesException
   */
  public final void navigateToDetail(final ActionEvent event) throws FatalFacesException {
    LOG.debug("request to navigate to detail");
    String idString = null;
    try {
      idString = RobustCurrent.requestParameterValues(ID_REQUEST_PARAMETER_NAME)[0];
      LOG.debug("request parameter " + ID_REQUEST_PARAMETER_NAME + " = " + idString);
      Long id = Long.valueOf(idString);
      InstanceHandler handler = (InstanceHandler)InstanceHandler.
          RESOLVER.handlerFor(getPersistentBeanType(), getDaoVariableName());
      // MUDO (jand) this type does not allow for polymorphic navigation!
      // solution: put the type in the HTML page too!
      // same problem as in PersistentBeanConverter!
      LOG.debug("Created handler for id = " + id
                + "and type " + getPersistentBeanType() + ": " + handler);
      handler.setId(id);
      handler.navigateHere(event);
    }
    catch (ArrayIndexOutOfBoundsException aioobExc) {
      RobustCurrent.fatalProblem(
          "no parameter " + ID_REQUEST_PARAMETER_NAME
          + " found in HTTP request parameters", aioobExc, LOG);
    }
    catch (NumberFormatException nfExc) {
      RobustCurrent.fatalProblem(
          "HTTP request parameter " + ID_REQUEST_PARAMETER_NAME
          + " (" + idString + ") is not a Long", nfExc, LOG);
    }
  }

  /**
   * <p>Action method that creates a fresh instance of {@link #getPersistentBeanType()} with
   *   the default constructor, and then calls {@link #navigateToEditNew(PersistentBean)}
   *   with it.</p>
   * <p>The fresh instance handler is made available to the JSP/JSF page in request scope,
   *   as a variable with the appropriate name, and we navigate to
   *   {@link InstanceHandler#getViewId()}.</p>
   *
   * @throws  FatalFacesException
   */
  public final void navigateToEditNew() throws FatalFacesException {
    PersistentBean fresh = createInstance();
    navigateToEditNew(fresh);
  }

  /**
   * <p>Create a fresh handler, most appropriate for the type of the given
   *   persistent bean <code>fresh.getClass()</code>,
   *   and navigate there in {@link InstanceHandler#VIEWMODE_EDITNEW} using
   *   {@link InstanceHandler#navigateHere(String)}.</p>
   * <p>Although this is not obligatory, <code>fresh</code> should be an instance of
   *   {@link #getPersistentBeanType()} or one of its subtypes.</p>
   * <p>The fresh handler is made available to the JSP/JSF page in request scope,
   *   as a variable with the appropriate name, and we navigate to
   *   {@link InstanceHandler#getViewId()}.</p>
   *
   * @throws  FatalFacesException
   */
  public final void navigateToEditNew(final PersistentBean fresh) throws FatalFacesException {
    assert fresh != null;
    InstanceHandler handler =
      (InstanceHandler)InstanceHandler.RESOLVER.handlerFor(fresh.getClass(), getDaoVariableName());
    handler.setInstance(fresh);
    handler.navigateHere(InstanceHandler.VIEWMODE_EDITNEW);
  }

  /**
   * @pre getType() != null;
   * @return VIEW_ID_PREFIX + s/\./\//(getType().getName()) + LIST_VIEW_ID_SUFFIX;
   */
  public String getViewId() {
    assert getPersistentBeanType() != null : "type cannot be null";
    String typeName = getPersistentBeanType().getName();
    typeName = typeName.replace('.', '/');
    return VIEW_ID_PREFIX + typeName + LIST_VIEW_ID_SUFFIX;
  }

  /**
   * Put this handler in session scope with name
   * {@link PersistentBeanHandlerResolver#handlerVariableNameFor(Class)
   *        PersistentBeanHandlerResolver#handlerVariableNameFor(getType())}.
   *
   * @post  RESOLVER.putInSessionScope(this);
   */
  public final void putInSessionScope() {
    RESOLVER.putInSessionScope(this);
  }

  /*<section name="skimmable">*/
  //------------------------------------------------------------------

  /**
   * Sets the {@link #getStoredInstances() stored instances} to <code>null</code>.
   * Sets the cached instance handlers to <code>null</code>.
   */
  public void skim() {
    super.skim();
    LOG.debug("instances and handlers are skimmed");
    $handlers = null;
    releaseInstances();
  }

  /*</section>*/



  /*<section name="removable">*/
  //------------------------------------------------------------------

  // yep, for now

  /*</section>*/


  /*<section name="navigationInstance">*/
  //------------------------------------------------------------------

  /**
   * @note Can only be used as a navigation instance when we want to show all
   *       records of this type for now.
   *
   * @result (ni == null) ? (result == null);
   * @result (result != null) ? (result == this);
   * @return ((getClass() == ni.getClass()) &&
   *              (getPersistentBeanType() == ((InstanceHandler)ni).getPersistentBeanType()) &&
   *              equalsWithNull(getId(), ((InstanceHandler)ni).getId())) ?
   *           this : null;
   * @result (result != null) ? getTime().equals(NOW);
   */
  public NavigationInstance absorb(final NavigationInstance ni) {
    LOG.debug("request to absorb " + ni + " by " + this);
    if (ni == null) {
      return null;
    }
    else if ((getClass() == ni.getClass())
             && (getPersistentBeanType() == ((CollectionHandler)ni).getPersistentBeanType())) {
      LOG.debug("absorbing");
      resetLastRenderedTime();
      return this;
    }
    else {
      LOG.debug("not absorbing");
      return null;
    }
  }

  /*</section>*/


  /**
   * <strong>= {@value}</strong>
   */
  public static final String HANDLER_VARNAME_SUFFIX = "_collection";

  /**
   * The resolver.
   *
   * @invar RESOLVER.getHandlerDefaultClass() == CollectionHandler.class;
   * @invar RESOLVER.getHandlerVarNameSuffix().equals(HANDLER_VARNAME_SUFFIX);
   */
  public static final PersistentBeanHandlerResolver RESOLVER =
      new PersistentBeanHandlerResolver(DataModelHandler.class,
                                        CollectionHandler.class,
                                        HANDLER_VARNAME_SUFFIX);

}
