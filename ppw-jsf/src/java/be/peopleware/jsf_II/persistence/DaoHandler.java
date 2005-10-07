/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.persistence;


import java.io.Serializable;

import javax.faces.el.VariableResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_II.dao.Dao;
import be.peopleware.servlet.sessionMopup.Removable;
import be.peopleware.servlet.sessionMopup.Skimmable;


/**
 * <p>Common functionality for JavaServer Faces backing beans (handlers)
 *  that use a {@link Dao}.</p>
 * <p>Handlers should be {@link Serializable}.
 *   This makes it possible to use them in session scope efficiently.
 *   The container might save memory by writing user sessions to disk,
 *   and this mechanism also makes fail-over and load balancing between
 *   different servers possible. Often, handlers will not be used in
 *   session scope, but it should be possible.</p>
 * <p>A {@link Dao} cannot be serialized (see the JavaDoc). This also
 *   doesn't make much sense in a web application. Daos often involve
 *   a database or application server connection. Such a connection
 *   should not be kept open in-between HTTP requests. Thus, it makes
 *   no sense whatsoever to serialize a {@link Dao} in any way in-between
 *   HTTP requests. A fresh {@link Dao} should be rebuilt for every HTTP
 *   request. It would be possible to only rebuild the back-end connection
 *   for a DAO, and keep the {@link Dao} itself, but this only moves the
 *   issue one layer. We decide that the {@link Dao} is the correct layer
 *   to make this separation. In fact, this is part of the definition
 *   of what a {@link Dao} is.</p>
 * <p>To make it possible to keep a <code>DaoHandler</code> in session
 *   scope, while a fresh {@link Dao} is provided for every HTTP request,
 *   instances of this type will look up the necessary {@link Dao}
 *   whenever they need it. <strong>Instances of this type will
 *   never store a reference to the {@link Dao} themselves, but
 *   look up the necessary {@link Dao} each time it is requested.</strong><br />
 *   The lookup happens by resolving a {@link #getDaoVariableName()}
 *   using the {@link RobustCurrent#variableResolver() current JSF
 *   application VariableResolver}. The {@link #getDaoVariableName()}
 *   property implements IoC (Inversion of Control) for the DAO.</p>
 * <p>Each time the {@link #getDao() DAO} is requested, we check that
 *   the instance to which the {@link #getDaoVariableName()} resolves
 *   is of the {@link #getDaoType() expected type}. The
 *   {@link #getDaoType()} is usually fixed for a given subclass, and
 *   thus set in the super constructor call. Subclasses may add
 *   a <code>public final <var>DaoType</var> get<var>DaoType</var>Dao()</code>
 *   to return the {@link #getDao()} with the expected static type.</p>
 * <p>Because the configuration of a DAO instance that is needed for
 *   a handler is most often rather dependent on the handler type, and
 *   not on the handler instance, we make it possible to define a
 *   {@link #getDefaultDaoVariableName() default dao variable name}
 *   in the handler. This name will be used when {@link #getDaoVariableName()}
 *   is <code>null</code> or <code>EMPTY</code> to resolve the DAO.</p>
 *
 * @note There is no reason for now to make this an interface.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar getDaoType() != null;
 * @invar Dao.class.isAssignableFrom(getDaoType());
 */
public abstract class DaoHandler implements Serializable, Removable, Skimmable {

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



  private static final Log LOG = LogFactory.getLog(DaoHandler.class);



  /*<construction>*/
  //------------------------------------------------------------------


  /**
   * @pre daoType != null;
   * @pre Dao.class.isAssignableFrom(daoType);
   * @post new.getDaoType() == daoType;
   * @post new.getDaoVariableName() == null;
   * @post new.getDefaultVariableName().equals(defaultDaoVariableName);
   */
  protected DaoHandler(final Class daoType, final String defaultDaoVariableName) {
    assert daoType != null : "daoType cannot be null";
    assert Dao.class.isAssignableFrom(daoType)
             : daoType.getName() + " is not a subtype of " + Dao.class.getName();
    $daoType = daoType;
    $defaultDaoVariableName = defaultDaoVariableName;
  }

  /*</construction>*/



  /*<property name="daoType">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Class getDaoType() {
    return $daoType;
  }

  /**
   * @invar $daoType != null;
   * @invar Dao.class.isAssignableFrom($daoType);
   */
  private final Class $daoType;

  /*</property>*/



  /*<property name="daoVariableName">*/
  //------------------------------------------------------------------

  /**
   * The variable name under which we will look for the dao using
   * the JSF application's {@link VariableResolver}.
   *
   * @basic
   */
  public final String getDaoVariableName() {
    return $daoVariableName;
  }

  /**
   * @return (getDaoVariableName() == null) ||
   *               getDoaVariableName().equals(EMPTY);
   */
  public final boolean isDaoVariableNameEmpty() {
    return ($daoVariableName == null)
            || $daoVariableName.equals(EMPTY);
  }

  /**
   * @post new.getDaoVariableName().equals(daoVariableName);
   */
  public final void setDaoVariableName(final String daoVariableName) {
    LOG.debug("daoVariableName set: " + daoVariableName);
    $daoVariableName = daoVariableName;
  }

  private String $daoVariableName;

  /*</property>*/



  /*<property name="defaultDaoVariableName">*/
  //------------------------------------------------------------------

  /**
   * The default variable name under which we will look for the
   * dao using the JSF application's {@link VariableResolver} if
   * {@link #getDaoVariableName()} is <code>null</code> or
   * <code>EMPTY</code>.
   *
   * @basic
   */
  public final String getDefaultDaoVariableName() {
    return $defaultDaoVariableName;
  }

  /**
   * @return (getDefaultDaoVariableName() == null) ||
   *               getDefaultDoaVariableName().equals(EMPTY);
   */
  public final boolean isDefaultDaoVariableNameEmpty() {
    return ($defaultDaoVariableName == null)
            || $defaultDaoVariableName.equals(EMPTY);
  }

  private String $defaultDaoVariableName;

  /*</property>*/



  /*<property name="dao">*/
  //------------------------------------------------------------------

  /**
   * The empty String.
   */
  public static final String EMPTY = "";

  /**
   * The Data Access Object that will fulfill the request.
   *
   * @return RobustCurrent.resolve(isDaoVariableNameEmpty() ?
   *                                    getDefaultDaoVariableName() :
   *                                    getDaoVariableName());
   * @throws FatalFacesException
   *         ! hasDaoVariableName();
   * @throws FatalFacesException
   *         RobustCurrent.resolve(isDaoVariableNameEmpty() ?
   *                                    getDefaultDaoVariableName() :
   *                                    getDaoVariableName());
   * @throws FatalFacesException
   *         ! getDaoType().isInstance(RobustCurrent.resolve(isDaoVariableNameEmpty() ?
   *                                    getDefaultDaoVariableName() :
   *                                    getDaoVariableName()));
   */
  public final Dao getDao() throws FatalFacesException {
    LOG.debug("request for dao (dao variable name = " + getDaoVariableName() + ")");
    if (!hasDaoVariableName()) {
      RobustCurrent.fatalProblem("dao variable name is null or the empty String "
                                 + "and no default set", LOG);
    }
    Object dao = (Dao)RobustCurrent.resolve(isDaoVariableNameEmpty()
                                              ? getDefaultDaoVariableName()
                                              : getDaoVariableName());
    LOG.debug("resolved object with name \""
              + (isDaoVariableNameEmpty()
                   ? getDefaultDaoVariableName()
                   : getDaoVariableName()) + "\" to " + dao);
    if (!getDaoType().isInstance(dao)) {
      RobustCurrent.fatalProblem("dao is not of the expected type ("
                                 + getDaoType() + ")", LOG);
    }
    return (Dao)dao;
  }

  /**
   * @return (! isDaoVariableNameEmpty()) &&
   *          (! isDefaultDaoVariableNameEmpty());
   */
  public final boolean hasDaoVariableName() {
    return !(isDaoVariableNameEmpty() && isDefaultDaoVariableNameEmpty());
  }

  /*</property>*/



  /*<section name="removable">*/
  //------------------------------------------------------------------

  /**
   * @protected
   * By default, objects are to be removed at the end of an HTTP request.
   * This means that <code>DaoHandlers</code> in session scope behave by
   * default actually as if they are in request scope. Subclasses should
   * overwrite this method as needed.
   */
  public boolean isToBeRemoved() {
    LOG.debug("request to be removed (" + this + "); giving default answer true");
    return true;
  }

  /*</section>*/



  /*<section name="skimmable">*/
  //------------------------------------------------------------------

  /**
   * @protected
   * Nothing to skim on this level. Subclasses should overwrite this
   * method as appropriate.
   */
  public void skim() {
    LOG.debug("request to skim this (" + this + ")");
    // NOP
  }

  /*</section>*/


  /**
   * Navigation string that says we want to stay on this page.
   *
   * @mudo (jand) probably remove
   */
  public static final String NAVIGATION_STRING_STAY_HERE = "success";

}
