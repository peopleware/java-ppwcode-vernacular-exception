package be.peopleware.test.java.lang;


import be.peopleware.test.CaseProvider;
import java.util.Set;
import java.util.HashSet;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 * 
 * @todo (dvankeer): add license
 */
public class _Test_Long extends CaseProvider {

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
    result.add(new Long(0));
    result.add(new Long(1));
    result.add(new Long(5));
    result.add(new Long(45));
    result.add(new Long(511));
    result.add(new Long(512));
    result.add(new Long(513));
    result.add(new Long(1023));
    result.add(new Long(1024));
    result.add(new Long(1025));
    result.add(new Long(-Integer.MIN_VALUE));
    result.add(new Long(Integer.MAX_VALUE));
    result.add(new Long(4 * Integer.MAX_VALUE));
    result.add(new Long(10 * Integer.MAX_VALUE));
    result.add(new Long(100000 * Integer.MAX_VALUE));
    result.add(new Long(Long.MAX_VALUE / 4));
    result.add(new Long(Long.MAX_VALUE / 2));
    result.add(new Long(Long.MAX_VALUE));
    result.add(new Long(-1));
    result.add(new Long(-5));
    result.add(new Long(-45));
    result.add(new Long(-511));
    result.add(new Long(-512));
    result.add(new Long(-513));
    result.add(new Long(-1023));
    result.add(new Long(-1024));
    result.add(new Long(-1025));
    result.add(new Long(Integer.MIN_VALUE));
    result.add(new Long(Integer.MAX_VALUE - 1));
    result.add(new Long(Integer.MIN_VALUE + 1));
    result.add(new Long(4 * Integer.MIN_VALUE));
    result.add(new Long(10 * Integer.MIN_VALUE));
    result.add(new Long(100000 * Integer.MIN_VALUE));
    result.add(new Long(Long.MIN_VALUE / 4));
    result.add(new Long(Long.MIN_VALUE / 2));
    result.add(new Long(Long.MIN_VALUE));
    result.add(new Long(Long.MAX_VALUE - 1));
    result.add(new Long(Long.MIN_VALUE + 1));
    return result;
  }

  public Set getSomeCases() {
    Set result = new HashSet();
    result.add(new Long(0));
    result.add(new Long(1));
    result.add(new Long(Integer.MAX_VALUE));
    result.add(new Long(-1));
    result.add(new Long(Integer.MIN_VALUE));
    result.add(new Long(Integer.MAX_VALUE - 1));
    result.add(new Long(Integer.MIN_VALUE + 1));
    return result;
  }

}
