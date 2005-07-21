package be.peopleware.jsf_II.persistence;


import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.i18n_I.Properties;
import be.peopleware.i18n_I.ResourceBundleLoadStrategy;
import be.peopleware.jsf_II.i18n.I18nPropertyLabelMap;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.AsyncCrudDao;
import be.peopleware.persistence_I.dao.Dao;


/**
 * Common functionality of JavaServer Faces backing beans that process requests
 * for {@link PersistentBean PersistentBeans}. This provides code for an
 * {@link Dao} and a {@link CrudSecurityStrategy}.
 * These are stateful instances, that should be stored in request scope.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar (getType() != null)
 *            ? PersistentBean.class.isAssignableFrom(getType())
 *            : true;
 */
public abstract class AbstractPersistentBeanHandler {

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



  private static final Log LOG = LogFactory.getLog(AbstractPersistentBeanHandler.class);


  public AbstractPersistentBeanHandler() {
    // NOP
    LifecycleFactory lifecycleFactory =
      (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    lifecycle.addPhaseListener(
        new PhaseListener() {
          public void beforePhase(PhaseEvent event) {
            LOG.debug("Before phase "+event.getPhaseId());
            System.out.println("Before phase "+event.getPhaseId());
          }
          public void afterPhase(PhaseEvent event) {
            LOG.debug("After phase "+event.getPhaseId());
            System.out.println("After phase "+event.getPhaseId());
          }
          public PhaseId getPhaseId() {
            return PhaseId.ANY_PHASE;
          }
        }
    );
  }


  /*<property name="dao">*/
  //------------------------------------------------------------------

  /**
   * The Data Access Object that will fulfill the request.
   *
   * @basic
   */
  public final AsyncCrudDao getDao() {
    return $dao;
  }

  /**
   * Set the Data Access Object that will fulfill the request.
   *
   * @post getDao() == dao;
   */
  public void setDao(AsyncCrudDao dao) {
    LOG.debug("Dao set: " + this + ".setDao(" + dao + ")");
    $dao = dao;
  }

  /**
   * The DAO that will fullfil this request.
   */
  private AsyncCrudDao $dao;

  /*</property>*/



// MUDO (jand) first show me this is needed, than remove the setter for the dao
//  /*<property name="daoFactory">*/
//  //------------------------------------------------------------------
//
//  /**
//   * The dao factory.
//   *
//   * @basic
//   * @init null;
//   */
//  public final DaoFactory getDaoFactory() {
//    return $daoFactory;
//  }
//
//  /**
//   * @post    (new.getDaoFactory() == daoFactory);
//   */
//  public final void setDaoFactory(final DaoFactory daoFactory) {
//    $daoFactory = daoFactory;
//    LOG.debug("new daoFactory is set: " + daoFactory);
//  }
//
//  private DaoFactory $daoFactory ;
//
//  /*</property>*/



  /*<property name="securityStrategy">*/
  //------------------------------------------------------------------

  /**
   * @basic
   * @init null;
   */
  public final CrudSecurityStrategy getSecurityStrategy() {
    return $securityStrategy;
  }

  /**
   * @post new.getSecurityStragegy() == securityStrategy;
   */
  public final void setSecurityStrategy(final CrudSecurityStrategy securityStrategy) {
    LOG.debug("CrudSecurityStrategy set: "
              + ((securityStrategy == null) ? "null" : securityStrategy.toString()));
    $securityStrategy = securityStrategy;
  }

  private CrudSecurityStrategy $securityStrategy;

  /*</property>*/



  /*<property name="editable">*/
  //------------------------------------------------------------------

  /**
   * Can we edit the object represented by this form?
   *
   * @basic
   * @init      false;
   */
  public final boolean isEditable() {
    return $editable;
  }

//  /**
//   * @param     editable
//   *            Marks the persistent bean as a object that can be edited.
//   * @post      new.isEditable() == editable;
//   */
//  private void setEditable(final boolean editable) {
//    $editable = editable;
//  }

  private boolean $editable;

  /*</property>*/



  /*<property name="deleteable">*/
  //------------------------------------------------------------------

  /**
   * Can we delete the object represented by this form?.
   *
   * @basic
   * @init      false;
   */
  public final boolean isDeleteable() {
    return $deleteable;
  }

//  /**
//   * @param     deleteable
//   *            Marks the persistent bean as a object that can be deleted.
//   * @post      new.isDeleteable() == deleteable;
//   */
//  private void setDeleteable(final boolean deleteable) {
//    $deleteable = deleteable;
//  }

  private boolean $deleteable;

  /*</property>*/



  /*<property name="type">*/
  //------------------------------------------------------------------

  /**
   * The type of the {@link PersistentBean} that will be handled
   * by the requests.
   *
   * @basic
   * @init null;
   */
  public final Class getType() {
    return $type;
  }

  /**
   * Set the type of the {@link PersistentBean} that will be handled
   * by the requests.
   *
   * @param  type
   *         The type to be set.
   * @pre (type != null) ? PersistentBean.class.isAssignableFrom(type);
   * @post   getType() == type;
   *
   * @mudo (jand) doc: and set the label maps
   */
  public final void setType(Class type) {
    // pre and not exception, because this is a programmatic error
    assert ((type != null) ? PersistentBean.class.isAssignableFrom(type) : true)
            : "Type must be a subtype of " + PersistentBean.class +
              " (and " + type + " isn't).";
    $type = type;
    LOG.debug("type of " + this + " set to Class " + type.getName());
    LOG.debug("loading label properties");
    $labels = new I18nPropertyLabelMap(type, false);
    $shortLabels = new I18nPropertyLabelMap(type, true);
    LOG.debug("label properties loaded");
  }

  /**
   * Set the type of the {@link PersistentBean} that will be handled
   * by the requests, as text.
   *
   * @todo This method is here for the faces-config managed bean stuff. Apparently,
   *       converters are not used during creation and property setting of managed
   *       beans. So, with the {@link #setType(Class)} method, we would be trying
   *       to push a String into a Class type parameter (actually, it seems, but
   *       we are not sure, that JSF attempts to <em>instantiate</em> the class
   *       for which the name is given, which is pretty weird). When this gets
   *       solved, or when we no longer need this method here, this method should
   *       be removed.
   *
   * @param   typeName
   *          The fully qualified name of the type to be set.
   * @post    getType() == Class.forName(type);
   * @throws  FacesException
   *          {@link Class#forName(String)}
   */
  public void setTypeAsString(String typeName) throws FacesException {
    Class type;
    try {
      type = Class.forName(typeName);
    }
    catch (LinkageError e) {
      throw new FacesException("cannot convert String to Class", e);
    }
    catch (ClassNotFoundException e) {
      throw new FacesException("cannot convert String to Class", e);
    }
    $type = type;
    LOG.debug("type of " + this + " set to Class " + type.getName());
  }

  /**
   * The type of the {@link PersistentBean} that will be handled
   * by the requests.
   */
  private Class $type;

  /*</property>*/



  /*<property name="labels">*/
  //------------------------------------------------------------------

  /**
   * The i18n short labels for the properties of {@link #getType()}
   * according to the rules in
   * {@link Properties#i18nPropertyLabel(java.lang.String, java.lang.Class, boolean, ResourceBundleLoadStrategy)},
   * as a map. This property makes it easy to use this i18n strategy in JSF
   * fashion, without the need for a <code>f:loadBundle</code>
   * tag in every page.
   */
  public Map getLabels() {
    return $labels;
  }

  /**
   * @invar (getType() != null) ==> ($labels != null);
   * @invar (getType() != null) ==> ($labels.getType().equals(getType()));
   */
  private I18nPropertyLabelMap $labels;

  /*</property>*/



  /*<property name="shortLabels">*/
  //------------------------------------------------------------------

  /**
   * The i18n short labels for the properties of {@link #getType()}
   * according to the rules in
   * {@link Properties#i18nPropertyLabel(java.lang.String, java.lang.Class, boolean, ResourceBundleLoadStrategy)},
   * as a map. This property makes it easy to use this i18n strategy in JSF
   * fashion, without the need for a <code>f:loadBundle</code>
   * tag in every page.
   */
  public Map getShortLabels() {
    return $shortLabels;
  }


  /**
   * @invar (getType() != null) ==> ($shortLabels != null);
   * @invar (getType() != null) ==> ($shortLabels.getType().equals(getType()));
   */
  private I18nPropertyLabelMap $shortLabels;

  /*</property>*/


  /*<property name="navigationString">*/
  //------------------------------------------------------------------

  /**
   * The navigation string used in faces-config.xml to come to this page.
   *
   * @basic
   * @init null;
   */
  public final String getNavigationString() {
    return $navigationString;
  }

  /**
   * Set the navigation string to the given string.
   *
   * @param   navigationString
   *          The navigation string to set.
   * @post    (navigationString == null)
   *            ? new.getNavigationString() == null;
   *            : new.getNavigationString().equals(navigationString);
   */
  public void setNavigationString(String navigationString) {
    $navigationString = navigationString;
  }

  /**
   * The navigation string used in faces-config.xml to come to this page.
   */
  private String $navigationString;

  /*</property>*/

}