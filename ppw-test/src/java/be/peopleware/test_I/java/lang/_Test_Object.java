package be.peopleware.test_I.java.lang;


import java.util.ArrayList;

import be.peopleware.test_I.CaseProvider;
import be.peopleware.test_I.TooManyFailuresError;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class _Test_Object extends CaseProvider {

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

  protected final void validate(final boolean condition)
      throws TooManyFailuresError {
    if (!condition) {
      Throwable t = new Throwable();
      addFailure(t.getStackTrace());
    }
  }

  protected final void unexpectedThrowable(final Throwable t)
      throws TooManyFailuresError {
    addFailure(t);
  }

  public final String toString() {
    String result = null;
    if ($failures.size() == 0) {
      result = "Class ok"; //$NON-NLS-1$
    }
    else {
      result = $failures.size() + " errors"; //$NON-NLS-1$
    }
    return result;
  }

  public final void printErrors(final java.io.PrintStream out) {
    if (!$failures.isEmpty()) {
      out.println("----"); //$NON-NLS-1$
      java.util.Iterator iter = $failures.iterator();
      while (iter.hasNext()) {
        Object next = iter.next();
        try {
          StackTraceElement[] errorTrace = (StackTraceElement[])next;
          for (int i = 0; i < errorTrace.length; i++) {
            out.println("    " + errorTrace[i]); //$NON-NLS-1$
          }
        }
        catch (ClassCastException cc1Exc) {
          try {
            ((Throwable)next).printStackTrace();
          }
          catch (ClassCastException cc2Exc) {
            out.println(next);
          }
        }
        out.println("----"); //$NON-NLS-1$
      }
    }
  }

  private void addFailure(final Object error) throws TooManyFailuresError {
    if ($failures.size() > TooManyFailuresError.LIMIT) {
      throw new TooManyFailuresError();
    }
    $failures.add(error);
  }

  private ArrayList $failures = new ArrayList();

  public final void test() throws TooManyFailuresError {
    testClassMethods();
    testInstanceMethods();
  }

  protected abstract void testClassMethods() throws TooManyFailuresError;

  protected abstract void testInstanceMethods() throws TooManyFailuresError;

}
