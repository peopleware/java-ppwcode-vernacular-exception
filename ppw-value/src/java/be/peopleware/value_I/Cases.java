package be.peopleware.value_I;


import org.toryt.support.straightlist.ArrayStraightList;
import org.toryt.support.straightlist.StraightList;

import be.peopleware.bean_IV.PropertyException;
import be.peopleware.value_I.NationalNumber;


/**
 * <p>Static methods to retrieve TestObjectList instances.</p>
 *
 * @author    Nele Smeets
 * @author    PeopleWare n.v.
 */
public final class Cases {

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



  public static void loadClass() {
    // NOP, something to call to load the class
  }

 //  NationalNumber
  private final static StraightList TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER;
  static {
    NationalNumber[] nn = new NationalNumber[4];
    try {
      nn[0] = new NationalNumber("000000", "000", "97");
      nn[1] = new NationalNumber("781101", "456", "65");
      nn[2] = new NationalNumber("700509", "169", "96");
      nn[3] = new NationalNumber("760905", "145", "06");
    }
    catch(PropertyException exc) {
      assert false : "Shouldn't happen";
    }
    TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER = new ArrayStraightList(nn);
    org.toryt.Cases.addTol(NationalNumber.class, TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER);
  }

}
