package be.peopleware.test_I;





/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class TooManyFailuresError extends Error {

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

  /**
   * If there are too many errors, we give up.
   */
  public static final int LIMIT = 20;

  public String toString() {
    return "There are more then " //$NON-NLS-1$
            + LIMIT
            + " failures. Aborting."; //$NON-NLS-1$
  }

}

