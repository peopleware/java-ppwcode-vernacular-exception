/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II;


import java.io.IOException;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.config.ManagedBeanBuilder;
import org.apache.myfaces.config.RuntimeConfig;
import org.apache.myfaces.config.element.ManagedBean;
import org.apache.myfaces.el.VariableResolverImpl;

import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.bean_V.PropertyException;
import be.peopleware.i18n_I.Properties;
import be.peopleware.jsf_II.i18n.JsfResourceBundleLoadStrategy;


/**
 * Utility methods that return {@link FacesContext}, {@link ServletRequest},
 * etc., in a robust way. This means a {@link FacesException} is thrown if they
 * cannot be found, or are not of the expected type. Failures are also logged.

 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public class RobustCurrent {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$
  /*</section>*/


  private static final Log LOG = LogFactory.getLog(RobustCurrent.class);

  /**
   * The {@link JsfResourceBundleLoadStrategy} used.
   */
  public static final JsfResourceBundleLoadStrategy JSF_RESOURCE_BUNDLE_LOAD_STRATEGY
      = new JsfResourceBundleLoadStrategy();

  /**
   * The current {@link FacesContext}. Exception if <code>null</code>.
   *
   * @result result == FacesContext.getCurrentInstance();
   * @result FacesContext.getCurrentInstance() != null;
   * @throws FatalFacesException
   *         FacesContext.getCurrentInstance() == null;
   */
  public static FacesContext facesContext() throws FatalFacesException {
    FacesContext result =  FacesContext.getCurrentInstance();
    if (result == null) {
      fatalProblem("no faces context found");
    }
    return result;
  }

  /**
   * The current {@link UIViewRoot}. Exception if <code>null</code>.
   *
   * @result result == facesContext().getViewRoot();
   * @result facesContext().getViewRoot() != null;
   * @except facesContext();
   * @throws FatalFacesException
   *         facesContext().getViewRoot() == null;
   */
  public static UIViewRoot uiViewRoot() throws FatalFacesException {
    UIViewRoot result =  facesContext().getViewRoot();
    if (result == null) {
      fatalProblem("no faces view root found");
    }
    return result;
  }

  /**
   * Removes the value and submitted value of all {@link UIInput} elements
   * in the UI tree.
   *
   * <p>{@link UIInput} elements in the UI tree, starting from {@link #uiViewRoot()},
   *   can contain a local value, that was the result of a previous request / response
   *   cycle. If the component is not used in the mean time, it will still have that
   *   local value and show it in later responses.</p>
   * <p>This method sets {@link UIInput#getSubmittedValue()} and
   *   {@link UIInput#getValue()} to <code>null</code>, and
   *   {@link UIInput#isLocalValueSet()} to <code>false</code>.</p>
   *
   * @except uiViewRoot();
   */
  public static void resetUIInputComponents() throws FatalFacesException {
    resetUIInputComponent(uiViewRoot());
  }

  /**
   * Removes the value and submitted value of all {@link UIInput} elements
   * in the UI tree with <code>component</code> as its root.
   *
   * Helper method for {@link #resetUIInputComponents()}.
   * Depth-first tree traversal.
   *
   * @pre  component != null;
   */
  private static void resetUIInputComponent(UIComponent component) {
    assert component != null;
    if (component instanceof UIInput) {
      UIInput inputComponent = (UIInput)component;
      inputComponent.setSubmittedValue(null);
      inputComponent.setValue(null);
      inputComponent.setLocalValueSet(false);
    }
    // handle children
    Iterator iter = component.getChildren().iterator();
    while (iter.hasNext()) {
      UIComponent child = (UIComponent)iter.next();
      resetUIInputComponent(child);
    }
  }

  /**
   * The current {@link Locale}. Exception if <code>null</code>.
   *
   * @result result == uiViewRoot().getLocale();
   * @result uiViewRoot().getLocale() != null;
   * @except uiViewRoot();
   * @throws FatalFacesException
   *         uiViewRoot().getLocale() == null;
   */
  public static Locale locale() throws FatalFacesException {
    Locale result = uiViewRoot().getLocale();
    if (result == null) {
      fatalProblem("no locale found");
    }
    return result;
  }

  /**
   * The resource bundle with <code>baseName</code> as base name,
   * with the locale currently in the {@link UIViewRoot} ({@link #locale()}),
   * retrieved with {@link #JSF_RESOURCE_BUNDLE_LOAD_STRATEGY}. If no
   * resource bundle could be found in this way, <code>null</code> is returned.
   *
   * @return  RobustCurrent.JSF_RESOURCE_BUNDLE_LOAD_STRATEGY
   *            .loadResourceBundle(baseName);
   * @throws FatalFacesException
   *         locale();
   */
  public static ResourceBundle resourceBundle(String baseName) throws FatalFacesException {
    return RobustCurrent.JSF_RESOURCE_BUNDLE_LOAD_STRATEGY.loadResourceBundle(baseName);
  }

  /**
   * The current {@link ExternalContext}. Exception if <code>null</code>.
   *
   * @result result == facesContext().getExternalContext();
   * @result facesContext().getExternalContext() != null;
   * @except facesContext();
   * @throws FatalFacesException
   *         facesContext().getExternalContext() == null;
   */
  public static ExternalContext externalContext() throws FatalFacesException {
    ExternalContext result =  facesContext().getExternalContext();
    if (result == null) {
      fatalProblem("no external context found");
    }
    return result;
  }

  /**
   * The current {@link Principal user principal}. Returns <code>null</code> if there
   * is no user principal in the {@link ExternalContext}.
   *
   * @return externalContext().getUserPrincipal();
   * @except externalContext();
   */
  public static Principal principal() throws FacesException {
    return externalContext().getUserPrincipal();
  }

  /**
   * Check whether the current {@link Principal user principal} has
   * role <code>role</code>. Returns <code>false</code> if there
   * is no user principal in the {@link ExternalContext}.
   *
   * @return externalContext().isUserInRole(role);
   * @except externalContext();
   */
  public static boolean isUserInRole(String role) throws FacesException {
    return externalContext().isUserInRole(role);
  }

  /**
   * The session associated with the current request.
   * Exception if <code>null</code>.
   * Create a session now if there isn't one.
   *
   * @result result == externalContext().getSession(true);
   * @result externalContext().getSession(true) != null;
   * @except externalContext();
   * @throws FatalFacesException
   *         externalContext().getSession(true) == null;
   */
  public static Object session() throws FatalFacesException {
    Object result = externalContext().getSession(true);
    if (result == null) {
      fatalProblem("no session found");
    }
    return result;
  }

  /**
   * The {@link HttpSession} associated with the current request.
   * Exception if <code>null</code> or of another type.
   * Create a session now if there isn't one.
   *
   * @result result == session();
   * @except session();
   * @throws FatalFacesException
   *         ! (session() instanceof HttpSession)
   */
  public static HttpSession httpSession() throws FatalFacesException {
    HttpSession result = null;
    try {
      result = (HttpSession)session();
    }
    catch (ClassCastException ccExc) {
      fatalProblem("session is not a HttpSession", ccExc);
    }
    return result;
  }

 /**
   * The current session map. Exception if <code>null</code>.
   *
   * @result result == externalContext().getSessionMap();
   * @result externalContext().getSessionMap() != null;
   * @except externalContext();
   * @throws FatalFacesException
   *         externalContext().getSessionMap() == null;
   */
  public static Map sessionMap() throws FatalFacesException {
    Map result = externalContext().getSessionMap();
    if (result == null) {
      fatalProblem("no session map found");
    }
    return result;
  }

  /**
   * Redirect robustly to <code>link</code>
   *
   * @post   externalContext().redirect(link);
   * @except externalContext();
   * @throws FatalFacesException
   *         ; redirect failed with an IOException
   * @throws IllegalArgumentException
   *         If the specified url is relative
   * @throws IllegalStateException
   *         If, in a portlet environment, the current response object is a
   *         RenderResponse instead of an ActionResponse.
   * @throws IllegalStateException
   *         If, in a servlet environment, the current response has already been
   *         committed
   */
  public static void redirect(String link) throws FatalFacesException {
    try {
      externalContext().redirect(link);
    }
    catch (IOException e) {
      fatalProblem("error redirecting to link: " + link, e);
    }
  }

  /**
   * The current {@link Application}. Exception if <code>null</code>.
   *
   * @result result == facesContext().getApplication();
   * @result facesContext().getApplication() != null;
   * @except facesContext();
   * @throws FatalFacesException
   *         facesContext().getApplication() == null;
   */
  public static Application application() throws FatalFacesException {
    Application result =  facesContext().getApplication();
    if (result == null) {
      fatalProblem("no faces application instance found");
    }
    return result;
  }

  /**
   * The fully qualified class name of the ResourceBundle to be used
   * for JavaServer Faces messages for this application (the name of the
   * current <dfn>application resource bundle</dfn>).
   * This can be <code>null</code>.
   *
   * @return result == application().getMessageBundle();
   * @except application();
   */
  public static String applicationBundleName() throws FatalFacesException {
    return application().getMessageBundle();
  }

  /**
   * The current <dfn>application resource bundle</dfn>.
   * This can be <code>null</code>.
   *
   * @return ResourceBundle.getBundle(applicationBundleName(),
                                                     locale());
   * @except application();
   * @except locale();
   * @throws NullPointerException
   *         If <code>baseName</code> or <code>locale</code> is <code>null</code>.
   * @throws MissingResourceException
   *         If no resource bundle for the specified base name can be found.
   */
  public static ResourceBundle applicationBundle() throws FatalFacesException {
    ResourceBundle result = ResourceBundle.getBundle(applicationBundleName(),
                                                     locale());
    return result;
  }

  /**
   * The current servlet or portlet application scope variables map.
   * Exception if <code>null</code>.
   *
   * @result result == externalContext().getApplicationMap();
   * @result externalContext().getApplicationMap() != null;
   * @except externalContext();
   * @throws FatalFacesException
   *         externalContext().getApplicationMap() == null;
   */
  public static Map applicationMap() throws FatalFacesException {
    Map result = externalContext().getApplicationMap();
    if (result == null) {
      fatalProblem("no application scope variables map found");
    }
    return result;
  }

  /**
   * The current {@link ViewHandler}. Exception if <code>null</code>.
   *
   * @result result == application().getViewHandler();
   * @result application().getViewHandler() != null;
   * @except application();
   * @throws FatalFacesException
   *         application().getViewHandler() == null;
   */
  public static ViewHandler viewHandler() throws FatalFacesException {
    ViewHandler result =  application().getViewHandler();
    if (result == null) {
      fatalProblem("no faces view handler found");
    }
    return result;
  }

  /**
   * The current servlet or portlet request. Exception if <code>null</code>.
   *
   * @result result == externalContext().getRequest();
   * @result externalContext().getRequest() != null;
   * @except externalContext();
   * @throws FatalFacesException
   *         externalContext().getRequest() == null;
   */
  public static Object request() throws FatalFacesException {
    Object result = externalContext().getRequest();
    if (result == null) {
      fatalProblem("no request found");
    }
    return result;
  }

  /**
   * The current {@link HttpServletRequest}.
   * Exception if <code>null</code> or of another type.
   *
   * @result request();
   * @except request();
   * @throws FatalFacesException
   *         ! (request() instanceof HttpServletRequest);
   */
  public static HttpServletRequest httpServletRequest() throws FatalFacesException {
    HttpServletRequest result = null;
    try {
      result = (HttpServletRequest)request();
    }
    catch (ClassCastException ccExc) {
      fatalProblem("request is not a HttpServletReques", ccExc);
    }
    return result;
  }

  /**
   * The current servlet or portlet response. Exception if <code>null</code>.
   *
   * @result result == externalContext().getResponse();
   * @result externalContext().getResponse() != null;
   * @except externalContext();
   * @throws FatalFacesException
   *         externalContext().getResponse() == null;
   */
  public static Object response() throws FatalFacesException {
    Object result = externalContext().getResponse();
    if (result == null) {
      fatalProblem("no response found");
    }
    return result;
  }

  /**
   * The current {@link HttpServletResponse}.
   * Exception if <code>null</code> or of another type.
   *
   * @result response();
   * @except response();
   * @throws FatalFacesException
   *         ! (response() instanceof HttpServletResponse);
   */
  public static HttpServletResponse httpServletResponse() throws FatalFacesException {
    HttpServletResponse result = null;
    try {
      result = (HttpServletResponse)response();
    }
    catch (ClassCastException ccExc) {
      fatalProblem("response is not a HttpServletReques", ccExc);
    }
    return result;
  }

  /**
   * The current servlet or portlet request scope variables map.
   * Exception if <code>null</code>.
   *
   * @result externalContext().getRequestMap();
   * @result externalContext().getRequestMap() != null;
   * @except externalContext();
   * @throws FatalFacesException
   *         externalContext().getRequestMap() == null;
   */
  public static Map requestMap() throws FatalFacesException {
    Map result = externalContext().getRequestMap();
    if (result == null) {
      fatalProblem("no request scope variables map found");
    }
    return result;
  }

  /**
   * The current servlet or portlet request parameter map.
   * Exception if <code>null</code>.
   *
   * @result externalContext().getRequestParameterMap();
   * @result externalContext().getRequestParameterMap() != null;
   * @except externalContext();
   * @throws FatalFacesException
   *         externalContext().getRequestParameterMap() == null;
   */
  public static Map paramMap() throws FatalFacesException {
    Map result = externalContext().getRequestParameterMap();
    if (result == null) {
      fatalProblem("no request scope parameter map found");
    }
    return result;
  }

  /**
   * Look for a variable with name <code>var</code> in request scope,
   * session scope, and application scope, in that order. Return
   * <code>null</code> if no such variable is found.
   *
   * @return [ (requestMap().get(var) != null)
   *             ? (result == requestMap().get(var))
   *             : [ (sessionMap().get(var) != null)
   *                   ? (result == sessionMap().get(var))
   *                   : [ (applicationMap().get(var) != null)
   *                         ? (result == applicationMap().get(var))
   *                         : (result == null)
   *                     ]
   *               ]
   *         ]
   *
   * @except requestMap();
   * @except sessionMap();
   *         (if requestMap().get(var) == null)
   * @except applicationMap();
   *         (if requestMap().get(var) == null && sessionMap().get(var) == null)
   */
  public static Object variable(String var) throws FatalFacesException {
    Object result = requestMap().get(var);
    if (result == null) {
      result = sessionMap().get(var);
      if (result == null) {
        result = applicationMap().get(var);
      }
    }
    return result;
  }

  /**
   * Log out the user and throw a {@link FatalFacesException}.
   *
   * This method is to be called when a fatal error occurs.
   * We invalidate the {@link HttpSession}, actually logging
   * out the user, and throw a {@link FatalFacesException}.
   * This will propagate through the entire application to the
   * container, and an error page will be shown.
   * The <code>message</code> is logged in {@link #LOG}
   * as <code>fatal</code>.
   *
   * @post false;
   * @throws FatalFacesException ffExc
   *         ffExc.getMessage().equals(message) && (ffExc.getCause() == null);
   *         A FacesException that reports the problem.
   */
  private static void fatalProblem(String message) throws FatalFacesException {
    fatalProblem(message, LOG);
  }

  /**
   * Log out the user and throw a {@link FatalFacesException}.
   *
   * This method is to be called when a fatal error occurs.
   * We invalidate the {@link HttpSession}, actually logging
   * out the user, and throw a {@link FatalFacesException}.
   * This will propagate through the entire application to the
   * container, and an error page will be shown.
   * The <code>message</code> is logged in {@link #LOG}
   * as <code>fatal</code>.
   *
   * @post false;
   * @throws FatalFacesException ffExc
   *         ffExc.getMessage().equals(message) && (ffExc.getCause() == t);
   *         A FacesException that reports the problem.
   */
  private static void fatalProblem(String message, Throwable t) throws FatalFacesException {
    fatalProblem(message, t, LOG);
  }

  /**
   * Log out the user and throw a {@link FatalFacesException}.
   *
   * This method is to be called when a fatal error occurs.
   * We invalidate the {@link HttpSession}, actually logging
   * out the user, and throw a {@link FatalFacesException}.
   * This will propagate through the entire application to the
   * container, and an error page will be shown.
   * The <code>message</code> is logged as <code>fatal</code>.
   *
   * @post false;
   * @throws FatalFacesException ffExc
   *         ffExc.getMessage().equals(message) && (ffExc.getCause() == null);
   *         A FacesException that reports the problem.
   */
  public static void fatalProblem(String message, Log log) throws FatalFacesException {
    fatalProblem(message, null, log);
  }

  /**
   * Log out the user and throw a {@link FatalFacesException}.
   *
   * This method is to be called when a fatal error occurs.
   * We invalidate the {@link HttpSession}, actually logging
   * out the user, and throw a {@link FatalFacesException}.
   * This will propagate through the entire application to the
   * container, and an error page will be shown.
   * The <code>message</code> is logged as <code>fatal</code>.
   *
   * @idea (jand) this should send a mail to administrator and developers
   *
   * @post false;
   * @throws FatalFacesException ffExc
   *         ffExc.getMessage().equals(message) && (ffExc.getCause() == t);
   *         A FacesException that reports the problem.
   */
  public static void fatalProblem(String message, Throwable t, Log log) throws FatalFacesException {
    log.fatal(message, t);
    httpSession().invalidate();
    throw new FatalFacesException(message, t);
  }



  // TODO (jand) code lower still needs to be checked; does it belong here?




  /**
   * Get the {@link Throwable#getLocalizedMessage()} from <code>pExc</code>,
   * using the resource bundle load strategy {@link #JSF_RESOURCE_BUNDLE_LOAD_STRATEGY}
   * and turn it into a {@link FacesMessage}; use
   * {@link #showCompoundPropertyException(CompoundPropertyException)} when
   * <code>pExc</code> is a {@link CompoundPropertyException}.
   *
   * The severity of the message is {@link FacesMessage#SEVERITY_INFO}.
   * @todo The JSF component id of the message will be the
   * {@link PropertyException#getPropertyName() property name found in the exception}.
   *
   * @throws FatalFacesException
   */
  public static void showPropertyException(PropertyException pExc) throws FatalFacesException {
    if (pExc instanceof CompoundPropertyException) {
      showCompoundPropertyException((CompoundPropertyException)pExc);
    }
    else {
      LOG.debug("requested to create message for property exception", pExc);
      pExc.setLocalizedMessageResourceBundleLoadStrategy(JSF_RESOURCE_BUNDLE_LOAD_STRATEGY);
      String messageText =  pExc.getLocalizedMessage();
      LOG.debug("messageText = " + messageText);
      FacesMessage message =  new FacesMessage(FacesMessage.SEVERITY_INFO, messageText, null);
      LOG.debug("faces message = " + message);
      /* do we have a 1 component whose id ends in pExc.getPropertyName()?
       * the use that id for the message, else null */
      String componentId = null;
// MUDO (jand) code below creates a weird problem
//      List components  = componentsWithIdEndingIn(pExc.getPropertyName());
//      if (components.size() == 1) {
//        UIComponent component = (UIComponent)components.get(0);
//        componentId = component.getClientId(facesContext());
//      }
      RobustCurrent.facesContext().addMessage(componentId, message);
    }
  }

  /**
   * A list of all the components in the current {@link #uiViewRoot()}
   * whose {@link UIComponent#getClientId(javax.faces.context.FacesContext)}'s
   * part after the last colon is <code>componentIdPart</code>.
   */
  public static List componentsWithIdEndingIn(String componentIdPart) throws FatalFacesException {
    if ((componentIdPart == null) || (componentIdPart.equals(EMPTY))) {
      return Collections.EMPTY_LIST;
    }
    List result = new ArrayList();
    componentsWithIdEndingIn(uiViewRoot(), componentIdPart, facesContext(), result);
    return result;
  }

  /**
   * A list of all the components in the current {@link #uiViewRoot()}
   * whose {@link UIComponent#getClientId(javax.faces.context.FacesContext)}'s
   * part after the last colon is <code>componentIdPart</code>.
   */
  private static void componentsWithIdEndingIn(UIComponent current,
                                               String componentIdPart,
                                               FacesContext fc,
                                               List acc) {
    assert current != null;
    assert componentIdPart != null;
    String clientId = current.getClientId(fc);
    String[] parts = clientId.split(":");
    if (parts[parts.length - 1].equals(componentIdPart)) {
      acc.add(current);
    }
    // handle children
    Iterator iter = current.getChildren().iterator();
    while (iter.hasNext()) {
      UIComponent child = (UIComponent)iter.next();
      componentsWithIdEndingIn(child, componentIdPart, fc, acc);
    }
  }

  /**
   * When a {@link CompoundPropertyException} is caught, mostly
   * as a result of a call to {@link be.peopleware.bean_V.RousseauBean#checkCivility()},
   * turn it into faces messages. The faces action should return <code>null</code>:
   * we should stay on the same page.
   *
   * @throws FatalFacesException
   */
  public static void showCompoundPropertyException (CompoundPropertyException cpe)
      throws FatalFacesException {
    Iterator iter = cpe.getElementExceptions().values().iterator();
    while (iter.hasNext()) {
      Set propertyExceptions = (Set)iter.next();
      Iterator iter2 = propertyExceptions.iterator();
      while (iter2.hasNext()) {
        PropertyException pExc = (PropertyException)iter2.next();
        showPropertyException(pExc);
      }
    }
  }

  /**
   * Navigation string used when an expected instance has disappeared from
   * persistent storage.
   *
   * <strong>= {@value}</strong>
   *
   * @see #noInstance(Class, Log)
   */
  public final static String OUTCOME_NO_INSTANCE = "noInstance";

  /**
   * The key in the {@link #applicationBundle()} for the message
   * that reports on a disappeared object.
   *
   * <strong>= {@value}</strong>
   *
   * @see #noInstance(Class, Log)
   */
  public final static String MESSAGE_KEY_NO_INSTANCE = "noInstance";

  /**
   * This method is to be called by handlers in a situation where an expected
   * record has disappeared from the data base. We create a message about this,
   * log the occurrence, and send the user 1 page upstream (probably the previous
   * page). We agree that the navigation outcome to go 1 page upstream is
   * {@link #OUTCOME_NO_INSTANCE}.
   * The severity of the message is {@link FacesMessage#SEVERITY_WARN}.
   *
   * @pre type != null;
   * @pre log != null;
   * @except application();
   */
  public static String noInstance(Class type, Log log) throws FatalFacesException {
    assert type != null;
    assert log != null;
    log.info("current " + type + " not found in DB anymore; type: " + type);
    String typeLabel = Properties.i18nTypeLabel(type, false, JSF_RESOURCE_BUNDLE_LOAD_STRATEGY);
    log.debug("type label = " + typeLabel);
    addApplicationMessage(MESSAGE_KEY_NO_INSTANCE, new String[] {typeLabel},
                          FacesMessage.SEVERITY_WARN, null);
    return OUTCOME_NO_INSTANCE;
  }

  /**
   * Add a message to the faces context, based on a key,
   * looked up in the application bundle. If <code>arguments</code>
   * is <code>null</code>, no formatting is done.
   *
   * @param  key
   *         This key is used to look up a message in the current
   *         {@link #applicationBundle()}.
   * @param  arguments
   *         Arguments that can be substituted in the message if this message
   *         has parameters.
   * @param  severity
   *         The severity of the message.
   * @param  componentId
   *         The id to which the message is associated.
   * @except applicationBundle();
   * @except facesContext();
   */
  public static void addApplicationMessage(String key, Object[] arguments,
                                            FacesMessage.Severity severity, String componentId)
      throws FatalFacesException {
    ResourceBundle rb = applicationBundle();
    LOG.debug("bundle is: " + rb);
    String messageText;
    if (rb != null) {
      String messagePattern =  rb.getString(key);
      LOG.debug("message pattern = " + messagePattern);
      if (arguments != null) {
        messageText = MessageFormat.format(messagePattern, arguments);
      }
      else {
        messageText = messagePattern;
      }
    }
    else {
      messageText = MESSAGE_KEY_NO_INSTANCE;
    }
    LOG.debug("message text = " + messageText);
    FacesMessage fm = new FacesMessage(severity, messageText, null);
    LOG.debug("faces message = " + fm);
    facesContext().addMessage(componentId, fm);
  }

  /**
   * Returns true when the faces context contains at least one message.
   * Returns false otherwise.
   *
   * @return facesContext().getMessages().hasNext();
   * @except facesContext();
   */
  public static boolean hasMessages() throws FatalFacesException {
    return facesContext().getMessages().hasNext();
  }

  /**
   * The empty string.
   *
   * <strong>= &quot;&quot;</strong>
   */
  public final static String EMPTY = "";

  /**
   * Create a value binding instance with expression <code>value</code>,
   * and add it to the <code>component</code> with name <code>name</code>.
   *
   * @pre component != null;
   * @pre name != null;
   * @pre ! name.equals(EMPTY);
   * @except application();
   * @throws FatalFacesException
   *         ! UIComponentTag.isValueReference(value)
   * @throws NullPointerException
   *         value == null;
   * @throws ReferenceSyntaxException
   *         <code>value</code> has invalid syntax
   */
  public static void creatValueBinding(UIComponent component, String name, String value)
      throws FatalFacesException {
    assert component != null;
    assert name != null;
    assert ! name.equals(EMPTY);
    if (! UIComponentTag.isValueReference(value)) {
      fatalProblem("\"" + value + "\" is not a valid value reference", LOG);
    }
    ValueBinding vb = application().createValueBinding(value);
    assert vb != null;
    component.setValueBinding(name, vb);
  }

  /**
   * Return all values in the current request that correspond to the HTTP
   * request parameter with name <code>parameterName</code>.
   * If the parameter does not exist, <code>null</code> is returned.
   *
   * @param   parameterName
   *          The name of a request parameter.
   * @return  result
   *          ==
   *          externalContext().getRequestParameterValuesMap().get(parameterName);
   * @except  externalContext();
   */
  public static String[] requestParameterValues(String parameterName) throws FatalFacesException {
    return (String[])externalContext().getRequestParameterValuesMap().get(parameterName);
    // exceptions cannot happen according to contract of getRequestParameterValuesMap
  }

  /**
   * The current {@link VariableResolver}
   * This cannot be <code>null</code>.
   *
   * @return application().getVariableResolver();
   * @except application();
   */
  public static VariableResolver variableResolver() throws FatalFacesException {
    VariableResolver result = application().getVariableResolver();
    assert result != null;
    return result;
  }

  /**
   * Retrieve the variable with name <code>variableName</code> using the current
   * variable resolver {@link #variableResolver()} and create a managed bean
   * from faces-config.xml if needed.
   * If the name cannot be resolved, <code>null</code> is returned.
   *
   * @return variableResolver().resolveVariable(facesContext(), variableName);
   * @except variableResolver();
   * @throws FatalFacesException
   *         An exception occured when resolving <code>variableName</code>.
   */
  public static Object resolve(String variableName) throws FatalFacesException {
    Object result = null;
    try {
      result = variableResolver().resolveVariable(facesContext(), variableName);
    }
    catch (NullPointerException eExc) {
      fatalProblem("The given variableName is not effective: \"" + variableName + "\"", eExc);
    }
    catch (EvaluationException eExc) {
      fatalProblem("Exception resolving variable with name \"" + variableName + "\"", eExc);
    }
    return result;
  }

  /**
   * Retrieve the variable with name <code>variableName</code>, without
   * creating new managed beans. Also, we do not look for the implicit
   * variables like <code>param</code>, <code>header</code>, etc.
   * If the name cannot be resolved, <code>null</code> is returned.
   *
   * @return (requestMap().get(variableName) != null) ?
   *            requestMap().get(variableName) :
   *            ((sessionMap().get(variableName) != null) ?
   *                sessionMap().get(variableName) :
   *                applicationMap().get(variableName));
   * @except requestMap();
   * @except sessionMap();
   * @except applicationMap();
   */
  public static ScopeEntry lookup(String variableName) throws FatalFacesException {
    try {
      return new ScopeEntry(variableName, requestMap());
    }
    catch (NoSuchElementException nseExcR) {
      try {
        return new ScopeEntry(variableName, sessionMap());
      }
      catch (NoSuchElementException nseExcS) {
        try {
          return new ScopeEntry(variableName, applicationMap());
        }
        catch (NoSuchElementException nseExcA) {
          return null;
        }
      }
    }
  }

  /**
   * A class representing a variable in some scope.
   * Each instance of this class has 3 properties:
   * <ul>
   *   <li>key: the name of the variable</li>
   *   <li>scope: a map that contains or has contained the variable under the
   *              given key</li>
   *   <li>value: the value of the variable</li>
   * </ul>
   * The value that is returned by {@link #getValue()} is the current value
   * of the variable with name {@link #getKey()} in scope {@link #getScope()},
   * if that value still exists in that scope. Otherwise, it is the value
   * the variable had when we last consulted the scope
   * ({@link #getLastKnownValue()}). The scope is consulted during construction,
   * a call to {@link #getValue()}, {@link #remove()} or {@link #resetValue()}.
   *
   *
   * @invar  getKey() != null;
   * @invar  getScope() != null;
   * @invar  getLastKnownValue() != null;
   */
  public static class ScopeEntry {

    /**
     * Create a new scope entry with the given key and scope.
     *
     * @param   key
     *          The name of the variable.
     * @param   scope
     *          The map containing the variable.
     * @pre key != null;
     * @pre scope != null;
     * @post    new.getKey().equals(key);
     * @post    new.getScope().equals(scope);
     * @post    new.getLastKnownValue() == scope.get(key);
     * @throws  NoSuchElementException
     *          scope.get(key) == null;
     */
    public ScopeEntry(String key, Map scope) throws NoSuchElementException {
      assert key != null;
      assert scope != null;
      if (scope.get(key) == null) {
        throw new NoSuchElementException();
      }
      $key = key;
      $scope = scope;
      $lastKnownValue = scope.get(key);
    }

    /**
     * The name of the variable.
     *
     * @basic
     */
    public final String getKey() {
      return $key;
    }

    /**
     * @invar  $key != null;
     */
    private String $key;

    /**
     * The map containing the variable.
     *
     * @basic
     */
    public final Map getScope() {
      return $scope;
    }

    /**
     * @invar  $scope != null;
     */
    private Map $scope;

    /**
     * The value of the variable.
     *
     * @basic
     */
    public final Object getLastKnownValue() {
      return $lastKnownValue;
    }

    /**
     * @return (getScope().get(getKey()) != null) ? getScope().get(getKey()) : getLastKnownValue();
     * @post  new.getLastKnownValue() == getValue();
     */
    public final Object getValue() {
      resetValue();
      return $lastKnownValue;
    }

    /**
     * @post new.getLastKnownValue() == getValue();
     */
    public final void resetValue() {
      Object current = $scope.get($key);
      if (current != null) {
        $lastKnownValue = current;
      }
    }

    /**
     * @invar  $lastKnownValue != null;
     */
    private Object $lastKnownValue;

    /**
     * Remove the key from the scope.
     *
     * @post  !new.getScope().containsKey(getKey());
     * @post  new.getLastKnownValue() == getValue();
     */
    public final void remove() {
      resetValue();
      $scope.remove(getKey());
    }

    /**
     * Put the key and the value into the scope.
     *
     * @post  new.getScope().containsKey(getKey());
     * @post  new.getScope().get(key) == getValue();
     */
    public final void reput() {
      $scope.put(getKey(), getLastKnownValue());
    }

  }

  /**
   * Remove the variable with name <code>variableName</code> from all
   * scopes.
   *
   * @post ! requestMap().containsKey(variableName);
   * @post ! sessionMap().containsKey(variableName);
   * @post ! applicationMap().containsKey(variableName);
   * @except requestMap();
   * @except sessionMap();
   * @except applicationMap();
   */
  public static void remove(String variableName) throws FatalFacesException {
    requestMap().remove(variableName);
    sessionMap().remove(variableName);
    applicationMap().remove(variableName);
  }

  /**
   * Create a fresh managed bean, defined in <code>faces-config.xml</code>,
   * with name <code>name</code>. If no definition for <code>name</code>
   * can be found, return <code>null</code>. The created bean is not stored in
   * any scope, but returned.
   *
   * @note This method makes this class dependent on MyFaces. The API used
   *       is not part of the general JSF API, which doesn't expose the
   *       possibility to make managed beans ourselves. A workaround would
   *       be to get the current managed bean with name <code>name</code>,
   *       if any, without creating a new one (by looking in all scopes
   *       directly ourselves), remove it from that scope and remember it,
   *       then ask for the variable with name <code>name</code> via
   *       {@link #resolve(String)}, and then putting back the original bean
   *       where we found it.
   * @note This code is based on
   *        {@link VariableResolverImpl#resolveVariable(FacesContext, String)}.
   */
  public static Object freshManagedBean(String name) throws FatalFacesException {
    LOG.debug("request for fresh managed bean with name \"" + name + "\"");
    RuntimeConfig rtcfg = RuntimeConfig.getCurrentInstance(externalContext());
    ManagedBean mbc = rtcfg.getManagedBean(name);
    if (mbc == null) {
      return null;
    }
    ManagedBeanBuilder builder = new ManagedBeanBuilder();
    Object result = null;
    try {
      result = builder.buildManagedBean(facesContext(), mbc);
    }
    catch (Exception exc) {
      fatalProblem("error building managed bean with name \"" +
                   name + "\"", exc);
    }
    LOG.debug("created new managed bean: " + result);
    return result;
    /* MyFaces independent implementation
    ScopeEntry alreadyExisting = lookup(name);
    if (alreadyExisting != null) {
      alreadyExisting.remove();
    }
    Object result = resolve(name);
    if (alreadyExisting != null) {
      alreadyExisting.reput();
    }
    return result;
    */
  }

}



