package be.peopleware.jsf_II.persistence;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.PersistentBean;


/**
 * <p>Common functionality of JavaServer Faces backing beans that process requests
 *   for a collection of {@link PersistentBean PersistentBeans}.</p>
 * <p>If the collection is {@link #setInstances(Collection) set},
 *   that collection is used. If {@link #getStoredInstances()} returns <code>null</code>
 *   during operation, all instances of type {@link #getType()} are retrieved
 *   from persistent storage using {@link #getDao()} to work with.</p>
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
   *
   * @throw FatalFacesException
   *        (getStoredInstances() != null) &&
   *            ((getType() == null) || (getDoa() == null));
   */
  public final Collection getInstances() throws FatalFacesException {
    LOG.debug("request for instances collection");
    if ($storedInstances == null) {
      $storedInstances = Collections.unmodifiableCollection(loadAllInstances());
    }
    else {
      LOG.debug("returing stored instances");
    }
    return getStoredInstances();
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
    assert org.toryt.contract.Collections.instanceOf(instances, getType());
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
