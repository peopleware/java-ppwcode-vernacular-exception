package be.peopleware.jsf_II.persistence;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.AsyncCrudDao;


/**
 * <p>Retrieve or create {@link AbstractPersistentBeanHandler} instances for a given
 *  subtype of {@link PersistentBean}.</p>
 * <p>{@link AbstractPersistentBeanHandler Handlers} should be defined in
 *  <code>faces-config.xml</code>, for each subtype of {@link PersistentBean}
 *  for which you create a specific JSF page, with name
 *  {@link #handlerVariableNameFor(Class)}. Here, the correct type and the
 *  {@link AsyncCrudDao} should be set.</p>
 * <p>If these properties are not set, the resolver methods will set fallback
 *  values, that are given by the caller as arguments.</p>
 * <p>If no managed handler can be found, or just any handler with the expected
 *  name, an instance of the {@link #getHandlerDefaultClass()} is created, and its
 *  properties are set to the fallback arguments.</p>
 * <p>For actual instances, see the static field <code>RESOLVER</code> in the
 *  class of the kind of handler you need.</p>
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar getHandlerDefaultClass() != null;
 * @invar AbstractPersistentBeanHandler.class.isAssignableFrom(getHandlerDefaultClass());
 * @invar handlerVarNameSuffix() != null;
 */
public class PersistentBeanHandlerResolver {

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



  private static final Log LOG = LogFactory.getLog(PersistentBeanHandlerResolver.class);


  /**
   * @pre handlerDefaultClass != null;
   * @pre AbstractPersistentBeanHandler.class.isAssignableFrom(handlerDefaultClass);
   * @pre handlerNameSuffix != null;
   * @post new.getHandlerDefaultClass() == handlerDefaultClass;
   * @post new.getHandlerVarNameSuffix().equals(handlerVarNameSuffix);
   */
  public PersistentBeanHandlerResolver(Class handlerDefaultClass, String handlerVarNameSuffix) {
    assert handlerDefaultClass != null;
    assert AbstractPersistentBeanHandler.class.isAssignableFrom(handlerDefaultClass);
    assert handlerVarNameSuffix != null;
    $handlerDefaultClass = handlerDefaultClass;
    $handlerVarNameSuffix = handlerVarNameSuffix;
  }

  /**
   * This is used to create a default handler, if no managed handler can
   * be found. All returned handlers must be a subtype of this type.
   *
   * @basic
   */
  public final Class getHandlerDefaultClass() {
    return $handlerDefaultClass;
  }

  /**
   * @invar $handlerDefaultClass != null;
   * @invar AbstractPersistentBeanHandler.class.isAssignableFrom(handlerDefaultClass);
   */
  private final Class $handlerDefaultClass;

  /**
   * @basic
   */
  public final String getHandlerVarNameSuffix() {
    return $handlerVarNameSuffix;
  }

  /**
   * @invar $handlerVarNameSuffix != null;
   */
  private final String $handlerVarNameSuffix;

  /**
   * Return the name for the managed bean {@link AbstractPersistentBeanHandler}
   * for instances of type <code>type</code>, to be used as main bean in a
   * JSF page. This is the FQCN of
   * <code>type</code>, with dots replaced by the dollar sign,
   * and potentially a suffix.
   *
   * @pre type != null;
   * @pre PersistentBean.class.isAssignableFrom(type);
   * @return type.getName().replace('.', '$') + handlerVarNameSuffix();
   */
  public final String handlerVariableNameFor(Class type) {
    assert type != null;
    assert PersistentBean.class.isAssignableFrom(type);
    String fqcn = type.getName();
    String result = fqcn.replace('.', '$') + getHandlerVarNameSuffix();
    return result;
  }

  /**
   * Check that the handler is of the required type, and that it's properties are set correctly.
   *
   * @result result == handler;
   * @result result != null ? result.getType() != null;
   * @result result != null ? result.getDao() != null;
   * @throws FatalFacesException
   *         ! getHandlerDefaultClass().isAssignableFrom(RobustCurrent.
   *                      resolve(handlerVariableNameFor(pbType)));
   */
  private AbstractPersistentBeanHandler initHandler(Class pbType, AsyncCrudDao dao, String varName, Object handler)
      throws FatalFacesException {
    if (handler == null) {
      return null;
    }
    else {
      if (! (getHandlerDefaultClass().isInstance(handler))) {
        RobustCurrent.fatalProblem("variable name \"" + varName +
                                   "\" resolved to an object that is not an instance of " +
                                   getHandlerDefaultClass() + ": " + handler, LOG);
      }
      AbstractPersistentBeanHandler pbh = (AbstractPersistentBeanHandler)handler;
      if (pbh.getType() == null) {
        LOG.debug("type was not set in managed bean with name \"" + varName +
                  "\"; now set to " + pbType);
        pbh.setType(pbType);
      }
      if (pbh.getDao() == null) {
        LOG.debug("dao was not set in managed bean with name \"" + varName +
                  "\"; now set to " + dao);
        pbh.setDao(dao);
      }
      return pbh;
    }
  }

  /**
   * <p>Retrieve the managed bean {@link AbstractPersistentBeanHandler} for
   *   {@link PersistentBean PersistentBeans} of type <code>pbType</code>
   *   with name {@link #variableName(Class) variableName(pbType)} from
   *   request, session or application scope.</p>
   * <p>If no such handler can be found, return <code>null</code>.</p>
   * <p>The dao and type of this instance should be set in
   *   <code>faces-config.xml</code>. If they are not set, they are set to
   *   <code>pbType</code> and <code>dao</code>.</p>
   *
   * @pre pbType != null;
   * @pre PersistentBean.class.isAssignableFrom(pbType);
   * @result RobustCurrent.resolve(handlerVariableNameFor(pbType));
   * @result result != null ? result.getType() != null;
   * @result result != null ? result.getDao() != null;
   * @except RobustCurrent.resolve(handlerVariableNameFor(pbType));
   * @throws FatalFacesException
   *         ! getHandlerDefaultClass().isAssignableFrom(RobustCurrent.
   *                      resolve(handlerVariableNameFor(pbType)));
   */
  private AbstractPersistentBeanHandler managedHandlerForDirect(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    assert pbType != null;
    assert PersistentBean.class.isAssignableFrom(pbType);
    assert AbstractPersistentBeanHandler.class.isAssignableFrom(getHandlerDefaultClass());
    String varName = handlerVariableNameFor(pbType);
    Object result = RobustCurrent.resolve(varName);
    return initHandler(pbType, dao, varName, result);
  }

  /**
   * <p>Retrieve the managed bean {@link AbstractPersistentBeanHandler} for
   *   {@link PersistentBean PersistentBeans} of type <code>pbType</code>
   *   with name {@link #handlerVariableNameFor(Class) handlerVariableNameFor(pbType)}.</p>
   * <p>If no direct handler can be found for type <code>pbType</code>, look for
   *   a handler for the super class of <code>pbType</code>, unless  we pass beyond
   *   {@link PersistentBean} itself. In that case, return <code>null</code>.</p>
   * <p>The dao and type of this instance should be set in
   *   <code>faces-config.xml</code>. If they are not set, they are set to
   *   <code>pbType</code> and <code>dao</code>.</p>
   *
   * @pre pbType != null;
   * @pre PersistentBean.class.isAssignableFrom(pbType);
   * @result result != null ? result.getType() != null;
   * @result result != null ? result.getDao() != null;
   * @return (! PersistentBean.class.isAssignableFrom(pbType)) ?
   *              null :
   *              ((RobustCurrent.resolve(handlerVariableNameFor(pbType) != null)) ?
   *                  RobustCurrent.resolve(handlerVariableNameFor(pbType)) :
   *                  managedHandler(pbType.getSuperClass()));
   * @except RobustCurrent.resolve(handlerVariableNameFor(pbType));
   * @throws FatalFacesException
   *         ! getHandlerDefaultClass().isAssignableFrom(RobustCurrent.
   *                      resolve(handlerVariableNameFor(pbType)));
   * @except managedHandler(pbType.getSuperClass());
   */
  public final AbstractPersistentBeanHandler managedHandlerFor(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    assert pbType != null;
    if (! PersistentBean.class.isAssignableFrom(pbType)) {
      return null;
    }
    else {
      AbstractPersistentBeanHandler result = managedHandlerForDirect(pbType, dao);
      if (result != null) {
        return result;
      }
      else {
        return managedHandlerFor(pbType.getSuperclass(), dao);
      }
    }
  }

  /**
   * <p>Create a fresh managed bean {@link AbstractPersistentBeanHandler} for
   *   {@link PersistentBean PersistentBeans} of type <code>pbType</code>
   *   with name {@link #variableName(Class) variableName(pbType)}.
   *   This bean will not be in any scope.</p>
   * <p>If no such definition can be found, return <code>null</code>.</p>
   * <p>The dao and type of this instance should be set in
   *   <code>faces-config.xml</code>. If they are not set, they are set to
   *   <code>pbType</code> and <code>dao</code>.</p>
   *
   * @pre pbType != null;
   * @pre PersistentBean.class.isAssignableFrom(pbType);
   * @result RobustCurrent.freshManagedBean(handlerVariableNameFor(pbType));
   * @result result != null ? result.getType() != null;
   * @result result != null ? result.getDao() != null;
   * @except RobustCurrent.freshManagedBean(handlerVariableNameFor(pbType));
   * @throws FatalFacesException
   *         ! getHandlerDefaultClass().isAssignableFrom(RobustCurrent.
   *                      freshManagedBean(handlerVariableNameFor(pbType));
   */
  private AbstractPersistentBeanHandler freshManagedHandlerForDirect(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    assert pbType != null;
    assert PersistentBean.class.isAssignableFrom(pbType);
    assert AbstractPersistentBeanHandler.class.isAssignableFrom(getHandlerDefaultClass());
    String varName = handlerVariableNameFor(pbType);
    Object result = RobustCurrent.freshManagedBean(varName);
    return initHandler(pbType, dao, varName, result);
  }

  /**
   * <p>Retrieve a fresh managed bean {@link AbstractPersistentBeanHandler} for
   *   {@link PersistentBean PersistentBeans} of type <code>pbType</code>
   *   with name {@link #handlerVariableNameFor(Class) handlerVariableNameFor(pbType)}.
   *   This bean is not stored in any scope.</p>
   * <p>If no direct handler can be found for type <code>pbType</code>, look for
   *   a handler for the super class of <code>pbType</code>, unless  we pass beyond
   *   {@link PersistentBean} itself. In that case, return <code>null</code>.</p>
   * <p>The dao and type of this instance should be set in
   *   <code>faces-config.xml</code>. If they are not set, they are set to
   *   <code>pbType</code> and <code>dao</code>.</p>
   *
   * @pre pbType != null;
   * @pre PersistentBean.class.isAssignableFrom(pbType);
   * @result result != null ? result.getType() != null;
   * @result result != null ? result.getDao() != null;
   * @return (! PersistentBean.class.isAssignableFrom(pbType)) ?
   *              null :
   *              ((RobustCurrent.freshManagedBean(handlerVariableNameFor(pbType) != null)) ?
   *                  RobustCurrent.freshManagedBean(handlerVariableNameFor(pbType)) :
   *                  freshManagedHandler(pbType.getSuperClass()));
   * @except RobustCurrent.freshManagedBean(handlerVariableNameFor(pbType));
   * @throws FatalFacesException
   *         ! getHandlerDefaultClass().isAssignableFrom(RobustCurrent.
   *                      freshManagedBean(handlerVariableNameFor(pbType)));
   * @except freshManagedHandler(pbType.getSuperClass());
   */
  public final AbstractPersistentBeanHandler freshManagedHandlerFor(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    assert pbType != null;
    if (! PersistentBean.class.isAssignableFrom(pbType)) {
      return null;
    }
    else {
      AbstractPersistentBeanHandler result = freshManagedHandlerForDirect(pbType, dao);
      if (result != null) {
        return result;
      }
      else {
        return freshManagedHandlerFor(pbType.getSuperclass(), dao);
      }
    }
  }

  /**
   * Create a default {@link AbstractPersistentBeanHandler} for <code>pbType</code>,
   * of type {@link #getHandlerDefaultClass()}.
   * We set the type of this new instance to <code>pbType</code>, and the dao <code>dao</code>.
   * The handler is not stored in any scope.
   *
   * @pre pbType != null;
   * @pre PersistentBean.class.isAssignableFrom(pbType);
   * @result result.getType() == pbType;
   * @result result.getDao() == dao;
   */
  public final AbstractPersistentBeanHandler createDefaultHandlerFor(Class pbType, AsyncCrudDao dao) {
    assert pbType != null;
    assert PersistentBean.class.isAssignableFrom(pbType);
    AbstractPersistentBeanHandler handler = null;
    try {
      handler = (AbstractPersistentBeanHandler)getHandlerDefaultClass().newInstance(); // Exc
      handler.setType(pbType);
      handler.setDao(dao);
      LOG.debug("created new default handler for PersistentBean type \"" +
                pbType + "\": " + handler);
    }
    catch (Exception exc) {
      LOG.fatal("exception should not happen: ", exc);
      assert false : "should not happen: " + exc;
    }
    catch (ExceptionInInitializerError eiiErr) {
      LOG.fatal("error should not happen: ", eiiErr);
      assert false : "should not happen: " + eiiErr;
    }
    return handler;
  }

  /**
   * <p>Return a fitting {@link AbstractPersistentBeanHandler} for <code>pbType</code>. This is
   *   {@link #managedHandlerFor(Class, AsyncCrudDao) managedHandlerFor(pbType, dao)},
   *   or, if no such handler exist,
   *   {@link #createDefaultHandlerFor(Class, AsyncCrudDao) createDefaultHandlerFor(pbType, dao)}.
   *   If so, this new default handler is stored in request scope with name
   *   {@link #handlerVariableNameFor(Class) handlerVariableNameFor(pbType)}.</p>
   */
  public final AbstractPersistentBeanHandler handlerFor(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    AbstractPersistentBeanHandler result = managedHandlerFor(pbType, dao);
    if (result == null) {
      result = createDefaultHandlerFor(pbType, dao);
      RobustCurrent.requestMap().put(handlerVariableNameFor(pbType), result);
    }
    return result;
  }

  /**
   * <p>Return a fitting {@link AbstractPersistentBeanHandler} for <code>pbType</code>. This is
   *   {@link #freshManagedHandlerFor(Class, AsyncCrudDao) freshManagedHandlerFor(pbType, dao)},
   *   or, if no such handler exist,
   *   {@link #createDefaultHandlerFor(Class, AsyncCrudDao) createDefaultHandlerFor(pbType, dao)}.
   *   The resulting handler is not stored in any scope.</p>
   */
  public final AbstractPersistentBeanHandler freshHandlerFor(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    AbstractPersistentBeanHandler result = freshManagedHandlerFor(pbType, dao);
    if (result == null) {
      result = createDefaultHandlerFor(pbType, dao);
    }
    return result;
  }

  /**
   * Put <code>handler</code> in request scope
   * with name {@link #handlerVariableNameFor(Class) handlerVariableNameFor(handler.getType())}.
   *
   * @pre handler != null;
   * @pre getHandlerDefaultClass().isAssignableFrom(handler.getType());
   * @post RobustCurrent.variable(handlerVariableNameFor(handler.getType()));
   */
  public final void putInRequestScope(AbstractPersistentBeanHandler handler) {
    assert handler != null;
    assert getHandlerDefaultClass().isAssignableFrom(handler.getType());
    String varName = handlerVariableNameFor(handler.getType());
    RobustCurrent.requestMap().put(varName, handler);
    LOG.debug("handler " + handler + " put in request scope with name \"" +
              varName + "\"");
  }

}
