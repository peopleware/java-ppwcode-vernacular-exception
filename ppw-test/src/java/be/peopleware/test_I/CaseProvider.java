/*<license>
Copyright 2004, PeopleWare n.v.
NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
TO SELECTED PARTIES.
</license>*/


package be.peopleware.test_I;


import java.util.Set;


/**
 * Implementations provide sets of test cases.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class CaseProvider {

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
   * All possible relevant cases for this type,
   * with <code>null</code>.
   */
  public final Set getCasesWithNull() {
    Set result = getCases();
    result.add(null);
    return result;
  }

  /**
   * All possible relevant cases for this type,
   * without <code>null</code>.
   */
  protected abstract Set getCases();

  /**
   * A limited number of most important cases for this type,
   * without <code>null</code>.
   */
  public final Set getSomeCasesWithNull() {
    Set result = getSomeCases();
    result.add(null);
    return result;
  }

  /**
   * A limited number of most important cases for this type,
   * with <code>null</code>.
   */
  protected Set getSomeCases() {
    return getCases();
  }
}
