package be.peopleware.test_I.java.lang;


import be.peopleware.test_I.CaseProvider;

import java.util.Set;
import java.util.HashSet;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 * 
 * @todo (dvankeer): add license
 */
public class _Test_Integer extends CaseProvider {

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

  public Set getCases() {
    Set result = new HashSet();
    result.add(new Integer(0));
    result.add(new Integer(1));
    result.add(new Integer(5));
    result.add(new Integer(45));
    result.add(new Integer(511));
    result.add(new Integer(512));
    result.add(new Integer(513));
    result.add(new Integer(1023));
    result.add(new Integer(1024));
    result.add(new Integer(1025));
    result.add(new Integer(-Integer.MIN_VALUE));
    result.add(new Integer(Integer.MAX_VALUE));
    result.add(new Integer(-1));
    result.add(new Integer(-5));
    result.add(new Integer(-45));
    result.add(new Integer(-511));
    result.add(new Integer(-512));
    result.add(new Integer(-513));
    result.add(new Integer(-1023));
    result.add(new Integer(-1024));
    result.add(new Integer(-1025));
    result.add(new Integer(Integer.MIN_VALUE));
    result.add(new Integer(Integer.MAX_VALUE - 1));
    result.add(new Integer(Integer.MIN_VALUE + 1));
    return result;
  }

}
