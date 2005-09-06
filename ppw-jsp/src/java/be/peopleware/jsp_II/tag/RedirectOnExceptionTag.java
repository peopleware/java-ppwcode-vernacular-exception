package be.peopleware.jsp_II.tag;


import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspException;
import javax.servlet.ServletException;
import java.io.IOException;


/**
 * A try-catch tag, that looks for exceptions of a given type, also
 * in the {@link java.lang.Throwable#getCause() cause} or
 * {@link javax.servlet.jsp.JspException#getRootCause() "root cause"}
 * or {@link javax.servlet.jsp.el.ELException#getRootCause() "root cause"}
 * of the exception, and redirects to a given URL when a match is
 * encountered. Other exceptions are rethrown, if necessary encapsulated
 * in a JspTagException. This also throws JspTagExceptions if the
 * exception type name is not acceptable, or the forward URL doesn't work.
 * An exception that is found, is put in the request scope variable
 * <code>exception</code> before the forward.
 *
 * @invar     getExceptionType() != null;
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class RedirectOnExceptionTag extends SimpleTagSupport {

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


  // default constructor


  /*<property name="exceptionTypeName">*/
  //------------------------------------------------------------------

  /**
   * The name of the exception type that will be caugth.
   */
  public final String getExceptionTypeName() {
    return $exceptionTypeName;
  }

  public final Class getExceptionType() throws JspTagException {
    try {
      return Class.forName(getExceptionTypeName());
    }
    catch (ClassNotFoundException cnfExc) {
      throw new JspTagException(cnfExc);
    }
  }

  /**
   * @param     exceptionTypeName
   *            The fully qualified name of the exception to look for.
   *
   * @pre       exceptionTypeName != null;
   * @pre       Class.forName(exceptionTypeName)
   *                .isAssignableFrom(Exception.class);
   * @post      getExceptionTypeName().equals(exceptionTypeName);
   */
  public final void setExceptionTypeName(final String exceptionTypeName) {
    assert exceptionTypeName != null : "exceptionTypeName cannot be null"; //$NON-NLS-1$
    $exceptionTypeName = exceptionTypeName;
  }

  /**
   * @invar     $exceptionTypeName != null;
   */
  private String $exceptionTypeName = ""; //$NON-NLS-1$

  /*</property>*/



  /*<property name="forwardTo">*/
  //------------------------------------------------------------------

  /**
   * The URL to forward to if an exception of the observed kind occurs.
   */
  public final String getForwardTo() {
    return $forwardTo;
  }

  /**
   * @param     forwardTo
   *            The new URL to forward to.
   * @pre       forwardTo != null;
   * @post      getForwardTo().equals(forwardTo);
   */
  public final void setForwardTo(final String forwardTo) {
    assert forwardTo != null : "forwardTo cannot be null"; //$NON-NLS-1$
    $forwardTo = forwardTo;
  }

  /**
   * @invar     $forwardTo != null;
   */
  private String $forwardTo = ""; //$NON-NLS-1$

  /*</property>*/



  /*<property name="page context">*/
  //------------------------------------------------------------------

  /**
   * The page context of this page. Only works for a JSP.
   *
   * @throws    JspTagException
   *            !(getJspContext() instanceof PageContext)
   */
  protected final PageContext getPageContext() throws JspTagException {
    try {
      return (PageContext)getJspContext();
    }
    catch (ClassCastException ccExc) {
      throw new JspTagException("NO_PAGE_CONTEXT", ccExc); //$NON-NLS-1$
    }
  }

  /*</property>*/


  /**
   * The name used for the request variable that holds the exception
   * on forward.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_VAR_NAME = "exception"; //$NON-NLS-1$

  private static final String COULD_NOT_FORWARD = "COULD_NOT_FORWARD"; //$NON-NLS-1$

  public void doTag() throws JspException {
    try {
      if (getJspBody() != null) {
        getJspBody().invoke(null);
      }
    }
    catch (Throwable t) {
      Throwable recognized = null;
      recognized =
          be.peopleware.exception_I.Exceptions.huntFor(t, getExceptionType());
      if (recognized != null) {
        getJspContext().setAttribute(REQUEST_VAR_NAME,
                                     recognized,
                                     PageContext.REQUEST_SCOPE);
        try {
          getPageContext().forward(getForwardTo());
        }
        catch (ServletException sExc) {
          throw new JspTagException(COULD_NOT_FORWARD, sExc);
        }
        catch (IOException ioExc) {
          throw new JspTagException(COULD_NOT_FORWARD, ioExc);
        }
      }
      else {
        if (t instanceof JspException) {
          throw (JspException)t;
        }
        else {
          throw new JspTagException(t);
        }
      }
    }
  }

}
