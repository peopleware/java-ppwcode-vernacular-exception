package be.peopleware.jsp_I.tag;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import be.peopleware.bean_I.PropertyException;
import be.peopleware.jsp_I.JSTLResourceBundleLoadStrategy;

import java.io.IOException;


/**
 * Show everything there is to know about a Throwable, and all its causes.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
 public class ShowFullThrowableDataTag extends SimpleTagSupport {

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

  private static final String EMPTY = ""; //$NON-NLS-1$
  private static final String NEW_LINE = "\n"; //$NON-NLS-1$
  private static final String START_ROW = "<tr>"; //$NON-NLS-1$
  private static final String END_ROW = "</tr>"; //$NON-NLS-1$
  private static final String START_COLUMN = "<td>"; //$NON-NLS-1$
  private static final String END_COLUMN = "</td>"; //$NON-NLS-1$

  /**
   * Write all information about {@link #getThrowable()}.
   *
   * @throws    IOException
   *            ; when there is a problem writing to the page.
   */
  public void doTag() throws IOException {
    printSwitch("<table align=\"center\" class=\"throwableTableClass\">", //$NON-NLS-1$
                NEW_LINE);
    printThrowableAndNextCause(getThrowable());
    printSwitch("</table>", NEW_LINE); //$NON-NLS-1$
  }

  private static final JSTLResourceBundleLoadStrategy
      JSTL_RESOURCE_BUNDLE_LOAD_STRATEGY
          = new JSTLResourceBundleLoadStrategy();

  private void printThrowableAndNextCause(final Throwable t)
      throws IOException {
    if (t == null) {
      // NOP and stop recursion
      return;
    }
    JspWriter out = getJspContext().getOut();
    printSwitch(START_ROW, EMPTY);
// @idea (jand): make classes attributes
    printSwitch("<td colspan=\"4\" class=\"throwableHeadCellClass\">", //$NON-NLS-1$
                "Throwable: "); //$NON-NLS-1$
    out.print(t.getClass().getName());
    printSwitch(END_COLUMN, EMPTY);
    printSwitch(END_ROW, NEW_LINE);
    printSwitch(START_ROW, EMPTY);
    printSwitch("<td colspan=\"4\" class=\"throwableMessageCellClass\">", //$NON-NLS-1$
                "Message: "); //$NON-NLS-1$
    try {
      JSTL_RESOURCE_BUNDLE_LOAD_STRATEGY.setPageContext((PageContext)getJspContext());
      ((PropertyException)t).setLocalizedMessageResourceBundleLoadStrategy(
          JSTL_RESOURCE_BUNDLE_LOAD_STRATEGY);
    }
    catch (ClassCastException ccExc) {
      // too bad
    }
    out.print(t.getLocalizedMessage());
    printSwitch(END_COLUMN, EMPTY);
    printSwitch(END_ROW, NEW_LINE);
    printStackTrace(t.getStackTrace()); // 4 cells
    printSwitch(EMPTY, NEW_LINE);
    Throwable cause = t.getCause();
    printThrowableAndNextCause(cause);
    try {
      /* JspExceptions, that are rather important to us, have an older
         rootCause, is different from the cause -- yet */
      Throwable rootCause = ((JspException)t).getRootCause();
      // ClassCastException
      if (rootCause != cause) {
        printThrowableAndNextCause(rootCause);
      }
    }
    catch (ClassCastException ccExc) {
      // NOP: so it is not a JspException, ok
    }
    try {
      /* ELExceptions, that are rather important to us, have an older
         rootCause, is different from the cause -- yet */
      Throwable rootCause = ((ELException)t).getRootCause();
      // ClassCastException
      if (rootCause != cause) {
        printThrowableAndNextCause(rootCause);
      }
    }
    catch (ClassCastException ccExc) {
      // NOP: so it is not a ELException, ok
    }
  }

  /**
   * @param   elements
   *          An array of stack trace elements to write to the HTML page.
   * @pre     elements != null;
   * @post    writes <code>elements.length</code> &lt;tr&gt;'s, with 4 cells
   * @throws  IOException
   *          ; When writing to the HTML page fails.
   */
  private void printStackTrace(final StackTraceElement[] elements)
      throws IOException {
    JspWriter out = getJspContext().getOut();
    for (int i = 0; i < elements.length; i++) {
      StackTraceElement e = elements[i];
      if (isHtml()) {
        out.print("<tr class=\"throwableStackTraceRowClass\">"); //$NON-NLS-1$
        out.print(START_COLUMN + "&nbsp;&nbsp;" + END_COLUMN); //$NON-NLS-1$
        out.print(START_COLUMN);
        out.print("<code>" + e.getClassName() + "." + e.getMethodName() //$NON-NLS-1$ //$NON-NLS-2$
                  + "()</code>"); //$NON-NLS-1$
        out.print(END_COLUMN);
        out.print(START_COLUMN);
        out.print("<kbd>" + e.getFileName() + "</kbd>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.print(END_COLUMN);
        out.print(START_COLUMN);
        out.print(e.getLineNumber());
        out.print(END_COLUMN);
        out.print(END_ROW);
      }
      else {
        out.println("    " + e.toString()); //$NON-NLS-1$
      }
    }
  }

  /**
   * Print <code>htmlString</code> if {@link #isHtml}. Otherwise,
   * print <code>plainString</code>.
   *
   * @param     htmlString
   *            HTML encoded string to print.
   * @param     plainString
   *            Plain string to print
   * @throws    IOException
   *            When writing to the HTML page fails.
   */
  private void printSwitch(final String htmlString, final String plainString)
      throws IOException {
    JspWriter out = getJspContext().getOut();
    if (isHtml()) {
      out.print(htmlString);
    }
    else {
      out.print(plainString);
    }
  }



  /*<property name="throwable">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Throwable getThrowable() {
    return $throwable;
  }

  /**
   * @param     throwable
   *            The new throwable
   * @post      new.getThrowable() == throwable;
   */
  public final void setThrowable(final Throwable throwable) {
    $throwable = throwable;
  }

  private Throwable $throwable;

  /*</property>*/



  /*<property name="html">*/
  //------------------------------------------------------------------

  /**
   * Output will be in an HTML table. When false, it will be formatted
   * plain text.
   *
   * @basic
   * @init true;
   */
  public final boolean isHtml() {
    return $html;
  }

  /**
   * @param     html
   *            The new html value
   * @post      new.isHtml() == html;
   */
  public final void setHtml(final boolean html) {
    $html = html;
  }

  private boolean $html = true;

  /*</property>*/

}
