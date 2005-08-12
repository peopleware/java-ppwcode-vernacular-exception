package be.peopleware.jsf_II.persistence;


import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.AsyncCrudDao;


/**
 * <p>Retrieve or create {@link PersistentBeanHandler} instances for a given
 *  subtype of {@link PersistentBean}.</p>
 * <p>{@link PersistentBeanHandler Handlers} should be defined in
 *  <code>faces-config.xml</code>, for each subtype of {@link PersistentBean}
 *  for which you create a specific JSF page, with name
 *  {@link #handlerVariableNameFor(Class)}. Here, the correct type and the
 *  {@link AsyncCrudDao} should be set.</p>
 * <p>If these properties are not set, the resolver methods will set fallback
 *  values, that are given by the caller as arguments.</p>
 * <p>If no managed handler can be found, or just any handler with the expected
 *  name, an instance of the {@link #getDefaultHandlerClass()} is created, and its
 *  properties are set to the fallback arguments.</p>
 * <p>For actual instances, see the static field <code>RESOLVER</code> in the
 *  class of the kind of handler you need.</p>
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar getMinimalHandlerClass() != null;
 * @invar PersistentBeanHandler.class.isAssignableFrom(getMinimalHandlerClass());
 * @invar getDefaultHandlerClass() != null;
 * @invar getMinimalHandlerClass.isAssignableFrom(getDefaultHandlerClass());
 * @invar handlerVarNameSuffix() != null;
 */
public class PersistentBeanHandlerResolver implements Serializable {

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
   * @pre minimalHandlerClass != null;
   * @pre PersistentBeanHandler.class.isAssignableFrom(minimalHandlerClass);
   * @pre (defaultHandlerClass != null) ?
   *            minimalHandlerClass.isAssignableFrom(defaultHandlerClass);
   * @pre handlerNameSuffix != null;
   * @post new.getMininalHandlerClass() == minimalHandlerClass;
   * @post (defaultHandlerClass != null) ?
   *            (new.getDefaultHandlerClass() == defaultHandlerClass) :
   *            (new.getDefaultHandlerClass() == minimalHandlerClass);
   * @post new.getHandlerVarNameSuffix().equals(handlerVarNameSuffix);
   */
  public PersistentBeanHandlerResolver(Class defaultHandlerClass,
                                       Class minimalHandlerClass,
                                       String handlerVarNameSuffix) {
    assert minimalHandlerClass != null;
    assert PersistentBeanHandler.class.isAssignableFrom(minimalHandlerClass);
    assert (defaultHandlerClass != null) ?
              minimalHandlerClass.isAssignableFrom(defaultHandlerClass) : true;
    assert handlerVarNameSuffix != null;
    $mininalHandlerClass = minimalHandlerClass;
    $defaultHandlerClass = (defaultHandlerClass != null) ? defaultHandlerClass : minimalHandlerClass;
    $handlerVarNameSuffix = handlerVarNameSuffix;
  }

  /**
   * @pre defaultHandlerClass != null;
   * @pre PersistentBeanHandler.class.isAssignableFrom(defaultHandlerClass);
   * @pre handlerNameSuffix != null;
   * @post new.getMinimalHandlerClass() == minimalHandlerClass;
   * @post new.getDefaultHandlerClass() == minimalHandlerClass;
   * @post new.getHandlerVarNameSuffix().equals(handlerVarNameSuffix);
   */
  public PersistentBeanHandlerResolver(Class minimalHandlerClass,
                                       String handlerVarNameSuffix) {
    this(minimalHandlerClass, minimalHandlerClass, handlerVarNameSuffix);
  }

  /**
   * This is used to state what type the resolved handlers have to have minimally.
   *
   * @basic
   */
  public final Class getMinimalHandlerClass() {
    return $mininalHandlerClass;
  }

  /**
   * @invar $mininalHandlerClass != null;
   * @invar PersistentBeanHandler.class.isAssignableFrom($mininalHandlerClass);
   */
  private final Class $mininalHandlerClass;

  /**
   * This is used to create a default handler, if no managed handler can
   * be found. All returned handlers must be a subtype of this type.
   *
   * @basic
   */
  public final Class getDefaultHandlerClass() {
    return $defaultHandlerClass;
  }

  /**
   * @invar $defaultHandlerClass != null;
   * @invar $minimalHandlerClass.isAssignableFrom($defaultHandlerClass);
   */
  private final Class $defaultHandlerClass;

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
   * Return the name for the managed bean {@link PersistentBeanHandler}
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
   *         ! getMinimalHandlerClass().isAssignableFrom(RobustCurrent.
   *                      resolve(handlerVariableNameFor(pbType)));
   */
  private PersistentBeanHandler initHandler(Class pbType, AsyncCrudDao dao, String varName, Object handler)
      throws FatalFacesException {
    if (handler == null) {
      return null;
    }
    else {
      if (! (getMinimalHandlerClass().isInstance(handler))) {
        RobustCurrent.fatalProblem("variable name \"" + varName +
                                   "\" resolved to an object that is not an instance of " +
                                   getDefaultHandlerClass() + ": " + handler, LOG);
      }
      PersistentBeanHandler pbh = (PersistentBeanHandler)handler;
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
   * <p>Retrieve the managed bean {@link PersistentBeanHandler} for
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
   *         ! getMinimalHandlerClass().isAssignableFrom(RobustCurrent.
   *                      resolve(handlerVariableNameFor(pbType)));
   */
  private PersistentBeanHandler managedHandlerForDirect(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    assert pbType != null;
    assert PersistentBean.class.isAssignableFrom(pbType);
    assert PersistentBeanHandler.class.isAssignableFrom(getDefaultHandlerClass());
    String varName = handlerVariableNameFor(pbType);
    Object result = RobustCurrent.resolve(varName);
    return initHandler(pbType, dao, varName, result);
  }

  /**
   * <p>Retrieve the managed bean {@link PersistentBeanHandler} for
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
   *         ! getMinimalHandlerClass().isAssignableFrom(RobustCurrent.
   *                      resolve(handlerVariableNameFor(pbType)));
   * @except managedHandler(pbType.getSuperClass());
   */
  public final PersistentBeanHandler managedHandlerFor(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    assert pbType != null;
    if (! PersistentBean.class.isAssignableFrom(pbType)) {
      return null;
    }
    else {
      PersistentBeanHandler result = managedHandlerForDirect(pbType, dao);
      if (result != null) {
        return result;
      }
      else {
        return managedHandlerFor(pbType.getSuperclass(), dao);
      }
    }
  }

  /**
   * <p>Create a fresh managed bean {@link PersistentBeanHandler} for
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
   *         ! getMinimalHandlerClass().isAssignableFrom(RobustCurrent.
   *                      freshManagedBean(handlerVariableNameFor(pbType));
   */
  private PersistentBeanHandler freshManagedHandlerForDirect(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    assert pbType != null;
    assert PersistentBean.class.isAssignableFrom(pbType);
    assert PersistentBeanHandler.class.isAssignableFrom(getDefaultHandlerClass());
    String varName = handlerVariableNameFor(pbType);
    Object result = RobustCurrent.freshManagedBean(varName);
    return initHandler(pbType, dao, varName, result);
  }

  /**
   * <p>Retrieve a fresh managed bean {@link PersistentBeanHandler} for
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
   *         ! getMinimalHandlerClass().isAssignableFrom(RobustCurrent.
   *                      freshManagedBean(handlerVariableNameFor(pbType)));
   * @except freshManagedHandler(pbType.getSuperClass());
   */
  public final PersistentBeanHandler freshManagedHandlerFor(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    assert pbType != null;
    if (! PersistentBean.class.isAssignableFrom(pbType)) {
      return null;
    }
    else {
      PersistentBeanHandler result = freshManagedHandlerForDirect(pbType, dao);
      if (result != null) {
        return result;
      }
      else {
        return freshManagedHandlerFor(pbType.getSuperclass(), dao);
      }
    }
  }

  /**
   * Create a default {@link PersistentBeanHandler} for <code>pbType</code>,
   * of type {@link #getDefaultHandlerClass()}.
   * We set the type of this new instance to <code>pbType</code>, and the dao <code>dao</code>.
   * The handler is not stored in any scope.
   *
   * @pre pbType != null;
   * @pre PersistentBean.class.isAssignableFrom(pbType);
   * @result result.getType() == pbType;
   * @result result.getDao() == dao;
   */
  public final PersistentBeanHandler createDefaultHandlerFor(Class pbType, AsyncCrudDao dao) {
    assert pbType != null;
    assert PersistentBean.class.isAssignableFrom(pbType);
    PersistentBeanHandler handler = null;
    try {
      handler = (PersistentBeanHandler)getDefaultHandlerClass().newInstance(); // Exc
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
   * <p>Return a fitting {@link PersistentBeanHandler} for <code>pbType</code>. This is
   *   {@link #managedHandlerFor(Class, AsyncCrudDao) managedHandlerFor(pbType, dao)},
   *   or, if no such handler exist,
   *   {@link #createDefaultHandlerFor(Class, AsyncCrudDao) createDefaultHandlerFor(pbType, dao)}.
   *   If so, this new default handler is stored in request scope with name
   *   {@link #handlerVariableNameFor(Class) handlerVariableNameFor(pbType)}.</p>
   */
  public final PersistentBeanHandler handlerFor(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    LOG.debug("request for handler for type " + pbType);
    PersistentBeanHandler result = managedHandlerFor(pbType, dao);
    if (result == null) {
      LOG.debug("Could not find handler for type " + pbType +
                "; will create default handler and put it in request scope");
      result = createDefaultHandlerFor(pbType, dao);
      RobustCurrent.requestMap().put(handlerVariableNameFor(pbType), result);
    }
    else {
      LOG.debug("handler found in some scope, or created as managed bean: " + result);
    }
    LOG.debug("handler is " + result);
    return result;
  }

  /**
   * <p>Return a fitting {@link PersistentBeanHandler} for <code>pbType</code>. This is
   *   {@link #freshManagedHandlerFor(Class, AsyncCrudDao) freshManagedHandlerFor(pbType, dao)},
   *   or, if no such handler exist,
   *   {@link #createDefaultHandlerFor(Class, AsyncCrudDao) createDefaultHandlerFor(pbType, dao)}.
   *   The resulting handler is not stored in any scope.</p>
   */
  public final PersistentBeanHandler freshHandlerFor(Class pbType, AsyncCrudDao dao)
      throws FatalFacesException {
    LOG.debug("request for fresh handler for type " + pbType);
    PersistentBeanHandler result = freshManagedHandlerFor(pbType, dao);
    if (result == null) {
      LOG.debug("Could not create fresh managed handler for type " + pbType +
                "; will create default handler");
      result = createDefaultHandlerFor(pbType, dao);
    }
    else {
      LOG.debug("fresh handler created according to managed bean definition: " + result);
    }
    LOG.debug("fresh handler is " + result);
    return result;
  }

  /**
   * Put <code>handler</code> in request scope
   * with name {@link #handlerVariableNameFor(Class) handlerVariableNameFor(handler.getType())}.
   *
   * @pre handler != null;
   * @pre getMinimalHandlerClass().isInstance(handler);
   * @post RobustCurrent.variable(handlerVariableNameFor(handler.getType()));
   */
  public final void putInRequestScope(PersistentBeanHandler handler) {
    assert handler != null;
    assert getMinimalHandlerClass().isInstance(handler);
    String varName = handlerVariableNameFor(handler.getType());
    RobustCurrent.requestMap().put(varName, handler);
    LOG.debug("handler " + handler + " put in request scope with name \"" +
              varName + "\"");
  }

}
