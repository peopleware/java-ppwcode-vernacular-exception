package be.peopleware.jsf_II.persistence;


import java.io.Serializable;

import javax.faces.el.VariableResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.dao.Dao;


/**
 * <p>Common functionality for JavaServer Faces backing beans (handlers)
 *  that use a {@link Dao}.</p>
 * <p>Handlers should be {@link Serializable}.
 *   This makes it an possible to use them in session scope efficiently.
 *   The container might save memory by writing user sesssion to disk,
 *   and this mechanism also makes fail-over and load balancing between
 *   different servers possible. Often, handlers will not be used in
 *   session scope, but it should be possible.</p>
 * <p>A {@link Dao} cannot be serialized (see the JavaDoc). This also
 *   doesn't make much sense in a web application. Dao's often involve
 *   a database or application server connection. Such a connection
 *   should not be kept open inbetween HTTP requests. Thus, it makes
 *   no sense whatsoever to serialize a {@link Dao} in any way inbetween
 *   HTTP requests. A fresh {@link Dao} should be rebuild for every HTTP
 *   request. It would be possible to only rebuild the backend connection
 *   for a DAO, and keep the {@link Dao} itself, but this only moves the
 *   issue one layer. We decide that the {@link Dao} is the correct layer
 *   to make this separation. In fact, this is part of the definition
 *   of what a {@link Dao} is.</p>
 * <p>To make it possible to keep a <code>DaoHandler</code> in session
 *   scope, while a fresh {@link Dao} is provided each HTTP session,
 *   instances of this type will lookup the necessary {@link Dao}
 *   whenever they need it. <strong>Instances of this type will
 *   never store a reference to the {@link Dao} themselves, but
 *   lookup the necessary {@link Dao} each time it is requested.<br />
 *   The lookup happens by resolving a {@link #getDaoVariableName()}
 *   using the {@link RobustCurrent#variableResolver() current JSF
 *   application VariableResolver}. The {@link #getDaoVariableName()}
 *   property implements IoC for the DAO.</p>
 * <p>Each time the {@link #getDao() DAO is requested}, we check that
 *   the instance to which the {@link #getDaoVariableName()} resolves
 *   is of the {@link #getDaoType() expected type}. The
 *   {@link #getDaoType()} is usually fixed for a given subclass, and
 *   thus set in the super constructor call. Subclasses may add
 *   a <code>public final <var>DaoType</var> get<var>DaoType</var>Dao()</code>
 *   to return the {@link #getDao()} with the expected static type.</p>
 *
 * @note There is no reason for now to make this an interface.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar getDaoType() != null;
 * @invar Dao.class.isAssignableFrom(getDaoType());
 */
public abstract class DaoHandler implements Serializable {

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
   */
  protected DaoHandler(Class daoType) {
    assert daoType != null : "daoType cannot be null";
    assert Dao.class.isAssignableFrom(daoType) :
            daoType.getName() + " is not a subtype of " + Dao.class.getName();
    $daoType = daoType;
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
   * @post new.getDaoVariableName().equals(daoVariableName);
   */
  public final void setDaoVariableName(String daoVariableName) {
    LOG.debug("daoVariableName set: " + daoVariableName);
    $daoVariableName = daoVariableName;
  }

  private String $daoVariableName;

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
   * @return RobustCurrent.resolve(getDaoVariableName());
   * @throws FatalFacesException
   *         (getDaoVariableName() == null) ||
   *            (getDaoVariableName().equals(EMPTY));
   * @throws FatalFacesException
   *         RobustCurrent.resolve(getDaoVariableName());
   * @throws FatalFacesException
   *         ! getDaoType().isInstance(dao);
   */
  public final Dao getDao() throws FatalFacesException {
    LOG.debug("request for dao (dao variable name = " + getDaoVariableName() + ")");
    if ((getDaoVariableName() == null) ||
           (getDaoVariableName().equals(EMPTY))) {
      RobustCurrent.fatalProblem("dao variable is null or the empty String", LOG);
    }
    Object dao = (Dao)RobustCurrent.resolve(getDaoVariableName());
    LOG.debug("resolved object with name \"" + getDaoVariableName() + "\" to " +
              dao);
    if (! getDaoType().isInstance(dao)) {
      RobustCurrent.fatalProblem("dao is not of the expected type (" +
                                 getDaoType() + ")", LOG);
    }
    return (Dao)dao;
  }

  /*</property>*/

}
