package be.peopleware.test_I.java.lang;


import be.peopleware.test_I.CaseProvider;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_Throwable extends CaseProvider {

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

  private static final String NESTED = "nested"; //$NON-NLS-1$

  public Set getCases() {
    Set result = new HashSet();
    createThrowables(result, new Throwable(NESTED)); //$NON-NLS-1$
    createException(result, new Throwable(NESTED)); //$NON-NLS-1$
    createError(result, new Throwable(NESTED)); //$NON-NLS-1$
    createRuntimeException(result, new Throwable(NESTED)); //$NON-NLS-1$
    return result;
  }

  private void createThrowables(final Set result,
                                final Throwable cause) {
    Iterator iterString = new _Test_String().getCasesWithNull().iterator();
    while (iterString.hasNext()) {
      String message = (String)iterString.next();
      result.add(new Throwable(message));
      result.add(new Throwable(message, cause));
    }
  }

  private void createRuntimeException(final Set result,
                                      final Throwable cause) {
    Iterator iterString = new _Test_String().getCasesWithNull().iterator();
    while (iterString.hasNext()) {
      String message = (String)iterString.next();
      result.add(new RuntimeException(message));
      result.add(new RuntimeException(message, cause));
    }
  }

  private void createError(final Set result,
                           final Throwable cause) {
    Iterator iterString = new _Test_String().getCasesWithNull().iterator();
    while (iterString.hasNext()) {
      String message = (String)iterString.next();
      result.add(new Error(message));
      result.add(new Error(message, cause));
    }
  }

  private void createException(final Set result,
                               final Throwable cause) {
    Iterator iterString = new _Test_String().getCasesWithNull().iterator();
    while (iterString.hasNext()) {
      String message = (String)iterString.next();
      result.add(new Exception(message));
      result.add(new Exception(message, cause));
    }
  }

}
