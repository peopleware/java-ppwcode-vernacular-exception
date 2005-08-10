package be.peopleware.jsf_II.persistence;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.PersistentBean;


/**
 * Common functionality of JavaServer Faces backing beans that process requests
 * for a list of {@link PersistentBean PersistentBeans}.
 *
 * @author     David Van Keer
 * @author     Peopleware n.v.
 *
 * @mudo (jand) this must be adapted to be more then display; also edit-in-grid
 */
public abstract class AbstractPersistentBeanListHandler extends AbstractPersistentBeanHandler {

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

  static private final Log LOG = LogFactory.getLog(AbstractPersistentBeanListHandler.class);


  /*<property name="instances">*/
  //------------------------------------------------------------------

  /**
   * The set of {@link PersistentBean PersistentBeans},
   * retrieved from persistent storage, that contains all the instances
   * in the storage with type {@link #getType()}. If no data is found,
   * this method returns <code>null</code>.
   *
   * @basic
   * @init null;
   */
  public abstract Collection getInstances();

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
        PersistentBeanCrudHandler handler = (PersistentBeanCrudHandler)PersistentBeanCrudHandler.
            RESOLVER.freshHandlerFor(bean.getClass(), getDao());
        handler.setViewMode(PersistentBeanCrudHandler.VIEWMODE_DISPLAY);
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
      PersistentBeanCrudHandler handler = (PersistentBeanCrudHandler)PersistentBeanCrudHandler.
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

}
