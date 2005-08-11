package be.peopleware.jsf_II.persistence;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.util.DynamicComparatorChain;
import be.peopleware.persistence_I.PersistentBean;


/**
 * <p>Common functionality of JavaServer Faces backing beans that process requests
 *   for a collection of {@link PersistentBean PersistentBeans}.</p>
 * <p>If the collection is {@link #setInstances(Collection) set},
 *   that collection is used. If {@link #getStoredInstances()} returns <code>null</code>
 *   during operation, all instances of type {@link #getType()} are retrieved
 *   from persistent storage using {@link #getDao()} to work with.</p>
 * <h3>Creation of New Instances</h3>
 * <p>From a collection view, fresh instances for a given type can be created.
 *   This means that an {@link InstanceHandler} for the appropriate type
 *   is created, and that we navigate there in {@link InstanceHandler#VIEWMODE_EDITNEW}.
 *   The user then gets the change to fill out the fields for the fresh instance.
 *   This new handler needs to be provided with a fresh instance, where some
 *   fields can already be set by us. This functionality is offered to programmers
 *   with {@link #navigateToEditNew(PersistentBean)}.</p>
 * <p>End users call this functionality through an action method. How the fresh
 *   instance this action method offers to {@link #navigateToEditNew(PersistentBean)}
 *   is configured, differs from case to case. Thus, programmers will have to write
 *   a distinct action method for each case that calls {@link #navigateToEditNew(PersistentBean)}.</p>
 * <p>A special case is the creation of a fresh unrelated instance. This happens often
 *   from a page that shows all instances of a type. For this case, an
 *   {@link #navigateToEditNew() action method}
 *   is offered that creates a fresh instance of type {@link #getType()} with the
 *   default constructor.</p>
 * <h3>Sorting</h3>
 * <p>{@link #getInstances() Instances} are sorted with the {@link #getComparator() comparator}.
     The action listener method {@link #sort(ActionEvent)} is available
 *   for end users to control the sort order. The default {@link #getComparator()} sorts on
 *   {@link PersistentBean#getId()}, which is not very end-user friendly.</p>
 *
 * @author     David Van Keer
 * @author     Peopleware n.v.
 *
 * @toryt cC:org.toryt.contract.Collections
 * @invar getInstances() != null;
 * @invar cC:instanceOf(getInstances(), getType());
 * @invar cC:noNull(getInstances());
 *
 * @mudo (jand) this must be adapted to be more then display; also edit-in-grid
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


  static private final Log LOG = LogFactory.getLog(CollectionHandler.class);



  /*<property name="instances">*/
  //------------------------------------------------------------------

  /**
   * If {@link #getStoredInstances()} is not <code>null</code>,
   * return that Collection. Otherwise, retrieve all instances
   * of type {@link #getType()} from persistent storage, using
   * {@link #getDao()}. Only in this case, {@link #isSubtypesIncluded()}
   * is used.
   * The returned collection is sorted by {@link #getComparator()}.
   *
   * @return a sorted version of {@link #getStoredInstances()}
   * @throw FatalFacesException
   *        (getStoredInstances() != null) &&
   *            ((getType() == null) || (getDoa() == null));
   */
  public final SortedSet getInstances() throws FatalFacesException {
    LOG.debug("request for instances collection");
    if ($storedInstances == null) {
      $storedInstances = Collections.unmodifiableCollection(loadAllInstances());
    }
    else {
      LOG.debug("returing stored instances");
    }
    SortedSet result = new TreeSet(getComparator());
    try {
      result.addAll($storedInstances); // ClassCastException from sorting
    }
    catch (ClassCastException ccExc) {
      RobustCurrent.fatalProblem("problem sorting instances", ccExc, LOG);
    }
    return Collections.unmodifiableSortedSet(result);
  }

  private Collection loadAllInstances() throws FatalFacesException {
    LOG.debug("no instances collection set; will try to retrieve data from storage");
    if (getType() == null) {
      RobustCurrent.fatalProblem("type is unknown", LOG);
    }
    if (getDao() == null) {
      RobustCurrent.fatalProblem("dao is unknown", LOG);
    }
    LOG.debug("will try to load all instances of type \"" + getType() +
              "\" with dao " + getDao());
    Collection result = Collections.EMPTY_SET;
    try {
      // we are not doing this in a transaction, deliberately
      result = getDao().retrieveAllPersistentBeans(getType(), isSubtypesIncluded());
    }
    catch (TechnicalException tExc) {
      RobustCurrent.fatalProblem("Failed to retrieve all PersistenBeans of type \"" +
                                 getType() + "\" with dao " + getDao(), tExc, LOG);
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
    return $storedInstances;
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
    assert getType() != null;
    $storedInstances = (instances == null) ? null
                         : Collections.unmodifiableCollection(instances);
  }

  private Collection $storedInstances;

  /*</property>*/



  /*<property name="subtypesIncluded">*/
  //------------------------------------------------------------------

  /**
   * Whether we will retrieve instances of only {@link #getType()},
   * or also of all subtypes of {@link #getType()}.
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
   * Set whether we will retrieve instances of only {@link #getType()},
   * or also of all subtypes of {@link #getType()}.
   *
   * @post getType() == type;
   */
  public final void setSubtypesIncluded(boolean subtypesIncluded) {
    $subtypesIncluded = subtypesIncluded;
  }

  /**
   * Whether we will retrieve instances of only {@link #getType()},
   * or also of all subtypes of {@link #getType()}.
   */
  private boolean $subtypesIncluded = true;

  /*</property>*/



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
   * Return the comparator which is/can be used for sorting the
   * retrieved {@link PersistentBean}'s
   */
  public DynamicComparatorChain getComparator() {
    return $comparator;
  }

  /**
   * Set a comparator for sorting the retrieved {@link PersistentBean}'s
   *
   * @param     comparator
   *            The new Comparator to use for sorting
   */
  public void setComparator(final DynamicComparatorChain comparator) {
    $comparator = comparator;
  }

//  /**
//   * @return (getComparator() == null) ? EMPTY :
//   *                stringArrayToConcatString(getComparator().getSortOrder());
//   */
//  public final String getSortOrder() {
//    return (getComparator() == null) ?
//               EMPTY :
//               stringArrayToConcatString(getComparator().getSortOrder());
//  }

  /**
   * <strong>= {@value}</strong>
   */
  public final static String SORT_PROPERTY_REQUEST_PARAMETER_NAME = "sortProperty";

//  /**
//   * <strong>= {@value}</strong>
//   */
//  public final static String SORT_ORDER_REQUEST_PARAMETER_NAME = "sortOrder";

  /**
   * <strong>= {@value}</strong>
   */
  public final static String EMPTY = "";

  /**
   * <p>Action listener method that calls {@link DynamicComparatorChain#bringToFront(String)}
   *   with the value of HTTP request parameter {@link #SORT_PROPERTY_REQUEST_PARAMETER_NAME}
   *   as argument. Since the previous order needs to be remembered, the {@link #getComparator()}
   *   is best implemented as a managed bean in session scope.</p>
   * <p>This action listener method could be called with a <code>commandLink</code> as follows:</p>
   * <pre>
   *   <h:commandLink value="<var>label</var>" action="#{handler.sort}" immediate="true" />
   *     <f:param name="<var>{@link #SORT_PROPERTY_REQUEST_PARAMETER_NAME}</var>" value="<var>property name</var>" />
   *   </h:commandLink>
   * </pre>
   * <p>If there is no {@link #getComparator()}, or there is no {@link #SORT_PROPERTY_REQUEST_PARAMETER_NAME}
   *   HTTP request parameter, nothing happens.</p>
   */
  public final void sort(ActionEvent ae) {
    LOG.debug("call to sort");
    if (getComparator() == null) {
      LOG.warn("call to sort, but no comparator found");
      return;
    }
    String sortPropertyName = RobustCurrent.requestParameterValues(SORT_PROPERTY_REQUEST_PARAMETER_NAME)[0];
    if ((sortPropertyName != null) && (! sortPropertyName.equals(EMPTY))) {
      LOG.warn("call to sort, but no sort property name found in HTTP request");
      return;
    }
    else {
      LOG.debug("sort property is " + sortPropertyName);
    }
//    String sortOrderString = RobustCurrent.requestParameterValues(SORT_ORDER_REQUEST_PARAMETER_NAME)[0];
//    if (LOG.isWarnEnabled() && (sortOrderString != null) && (! sortOrderString.equals(EMPTY))) {
//      LOG.warn("no previous sort order given; starting from comparator default");
//    }
//    else {
//      LOG.debug("sort order is " + sortOrderString);
//      String[] sortOrder = concatStringToStringArray(sortOrderString);
//      getComparator().setSortOrder(sortOrder);
//    }
    getComparator().bringToFront(sortPropertyName);
  }

//  private final static String SORT_ORDER_SEPARATOR = ",";
//
//  /**
//   * @pre Strings in strings do not contain ','
//   * @idea (jand) move to util
//   */
//  public static String stringArrayToConcatString(String[] strings) {
//    if (strings == null) {
//      return EMPTY;
//    }
//    else {
//      StringBuffer result = new StringBuffer(strings.length * 20);
//      for (int i = 0; i < strings.length; i++) {
//        result.append(strings[i]);
//        if (i < strings.length - 1) {
//          result.append(SORT_ORDER_SEPARATOR);
//        }
//      }
//      return result.toString();
//    }
//  }
//
//  /**
//   * @pre Strings in strings do not contain ','
//   * @idea (jand) move to util
//   */
//  public static String[] concatStringToStringArray(String concatString) {
//    if (concatString == null) {
//      return new String[0];
//    }
//    else {
//      return concatString.split(SORT_ORDER_SEPARATOR);
//    }
//  }

  private DynamicComparatorChain $comparator = new DynamicComparatorChain();

  {
    $comparator.addComparator("id", null, true); // Long is Comparable
  }

  /*</section>*/


  public static final String ID_REQUEST_PARAMETER_NAME = "pbId";

  /**
   * Returns a Set containing all peristentBeans wrapped in the associated Handler.
   *
   * @return    Set
   *            A Set with PersistentBeanHandlers based on the instances of this Handler..
   */
  public final Set getInstanceHandlers() {
    if ($handlers == null) {
      LOG.debug("no handlers cached; creating new handlers");
      List handlers = new ArrayList();
      List beans = new ArrayList(getInstances());
      LOG.debug("retrieved instances");
      LOG.debug("creating handler for each instance");
      Iterator iter = beans.iterator();
      while (iter.hasNext()) {
        PersistentBean bean = (PersistentBean)iter.next();
        LOG.debug("    instance is " + bean);
        InstanceHandler handler = (InstanceHandler)InstanceHandler.RESOLVER.freshHandlerFor(bean.getClass(), getDao());
        handler.setInstance(bean);
        handler.setViewMode(InstanceHandler.VIEWMODE_DISPLAY);
        LOG.debug("    handler is " + handler);
        handlers.add(handler);
      }
      $handlers = new HashSet(handlers);
      LOG.debug("handlers created and cached");
    }
    else {
      LOG.debug("returning cached handlers");
    }
    return $handlers;
  }

  private Set $handlers;

  public static final String LIST_VIEW_ID_SUFFIX = "_list" + VIEW_ID_SUFFIX;

  /**
   * @pre getType() != null;
   * @return VIEW_ID_PREFIX + s/\./\//(getType().getName()) + LIST_VIEW_ID_SUFFIX;
   */
  public String getCollectionViewId() {
    assert getType() != null : "type cannot be null";
    String typeName = getType().getName();
    typeName = typeName.replace('.', '/');
    return VIEW_ID_PREFIX + typeName + LIST_VIEW_ID_SUFFIX;
  }

  /**
   *
   * @param event
   * @throws FatalFacesException
   */
  public final void navigateToDetail(ActionEvent event) throws FatalFacesException {
    LOG.debug("request to navigate to detail");
    String idString = null;
    try {
      idString = RobustCurrent.requestParameterValues(ID_REQUEST_PARAMETER_NAME)[0];
      LOG.debug("request parameter " + ID_REQUEST_PARAMETER_NAME + " = " + idString);
      Long id = Long.valueOf(idString);
      InstanceHandler handler = (InstanceHandler)InstanceHandler.
          RESOLVER.handlerFor(getType(), getDao());
      // MUDO (jand) this type does not allow for polymorphic navigation!
      // solution: put the type in the HTML page too!
      // same problem as in PersistentBeanConverter!
      LOG.debug("Created handler for id = " + id +
                "and type " + getType() + ": " + handler);
      handler.setId(id);
      handler.navigateHere(event);
    }
    catch (ArrayIndexOutOfBoundsException aioobExc) {
      RobustCurrent.fatalProblem("no parameter " + ID_REQUEST_PARAMETER_NAME +
                                 " found in HTTP request parameters", aioobExc, LOG);
    }
    catch (NumberFormatException nfExc) {
      RobustCurrent.fatalProblem("HTTP request parameter " + ID_REQUEST_PARAMETER_NAME +
                                 " (" + idString + ") is not a Long", nfExc, LOG);
    }
  }

  /**
   * Create a new instance of type {@link #getType()}.
   *
   * @post new.getInstance() isfresh
   * @post new.getInstance() == getType().newInstance();
   */
  private PersistentBean createInstance() throws FatalFacesException {
    LOG.debug("creating new instance of type \"" + getType() + "\"");
    PersistentBean result = null;
    try {
      result = (PersistentBean)getType().newInstance();
      LOG.debug("fresh instance: " + result);
    }
    // all exceptions are programmatic errors here, in subclass, in config or in JSF
    catch (InstantiationException iExc) {
      RobustCurrent.fatalProblem("could not create fresh instance of type " + getType(), iExc, LOG);
    }
    catch (IllegalAccessException iaExc) {
      RobustCurrent.fatalProblem("could not create fresh instance of type " + getType(), iaExc, LOG);
    }
    catch (ExceptionInInitializerError eiiErr) {
      RobustCurrent.fatalProblem("could not create fresh instance of type " + getType(), eiiErr, LOG);
    }
    catch (SecurityException sExc) {
      RobustCurrent.fatalProblem("could not create fresh instance of type " + getType(), sExc, LOG);
    }
    catch (ClassCastException ccExc) {
      RobustCurrent.fatalProblem("could not create fresh instance of type " + getType(), ccExc, LOG);
    }
    return result;
  }

  /**
   * <p>Action method that creates a fresh instances of {@link #getType()} with
   *   the default instructor, and then calls {@link #navigateToEditNew(PersistentBean)}
   *   with it.</p>
   * <p>The fresh instance handler is made available to the JSP/JSF page in request scope,
   *   as a variable with the appropriate name, and we navigate to
   *   {@link InstanceHandler#getDetailViewId()}.</p>
   */
  public final void navigateToEditNew() throws FatalFacesException {
    PersistentBean fresh = createInstance();
    navigateToEditNew(fresh);
  }

  /**
   * <p>Create a fresh handler, most approproate for <code>fresh.getClass()</code>,
   *   and navigate there in {@link InstanceHandler#VIEWMODE_EDITNEW} using
   *   {@link InstanceHandler#navigateHere(String)}.</p>
   * <p>Although this is not obligatory, <code>fresh</code> should be an instance of
   *   {@link #getType()} or one of its subtypes.</p>
   * <p>The fresh handler is made available to the JSP/JSF page in request scope,
   *   as a variable with the appropriate name, and we navigate to
   *   {@link InstanceHandler#getDetailViewId()}.</p>
   */
  public final void navigateToEditNew(PersistentBean fresh) throws FatalFacesException {
    assert fresh != null;
    InstanceHandler handler = (InstanceHandler)InstanceHandler.RESOLVER.handlerFor(fresh.getClass(), getDao());
    handler.setInstance(fresh);
    handler.navigateHere(InstanceHandler.VIEWMODE_EDITNEW);
  }

  /**
   * <p>This method should be called to navigate to the collection page
   *   for this {@link #getType()}.</p>
   * <p>This handler is made available to the JSP/JSF page in request scope,
   *   as a variable with name
   *   {@link #RESOLVER}{@link PersistentBeanHandlerResolver#handlerVariableNameFor(Class) .PersistentBeanHandlerResolver#handlerVariableNameFor(getType())}.
   *   And we navigate to {@link #getCollectionViewId()}.</p>
   * <p>The {@link #getType() type} should
   *   be set before this method is called. If {@link #getInstances()}
   *   is not <code>null</code>, that collection is shown. If it is
   *   <code>null</code>, all objects of type {@link #getType()} are
   *   retrieved from the database and shown.</p>
   *
   * @post    RobustCurrent.lookup(RESOLVER.handlerVariableNameFor(getType())) == this;
   * @throws  FatalFacesException
   *          getType() == null;
   */
  public final void navigateHere() throws FatalFacesException {
    LOG.debug("CollectionHandler.navigateHere called");
    if (getType() == null) {
      LOG.fatal("cannot navigate to collection, because no type is set (" +
                this);
    }
    // put this handler in request scope, under an agreed name, create new view & navigate
    RESOLVER.putInRequestScope(this);
    FacesContext context = RobustCurrent.facesContext();
    UIViewRoot viewRoot = RobustCurrent.viewHandler().createView(context, getCollectionViewId());
    context.setViewRoot(viewRoot);
    context.renderResponse();
  }

  /**
   * <strong>= {@value}</strong>
   */
  public static final String HANDLER_VARNAME_SUFFIX = "_collection";

  /**
   * @invar RESOLVER.getHandlerDefaultClass() == CollectionHandler.class;
   * @invar RESOLVER.getHandlerVarNameSuffix().equals(HANDLER_VARNAME_SUFFIX);
   */
  public final static PersistentBeanHandlerResolver RESOLVER =
      new PersistentBeanHandlerResolver(DataModelHandler.class,
                                        CollectionHandler.class,
                                        HANDLER_VARNAME_SUFFIX);

}