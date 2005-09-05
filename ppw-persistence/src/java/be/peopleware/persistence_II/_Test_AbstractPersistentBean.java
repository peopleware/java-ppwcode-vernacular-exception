package be.peopleware.persistence_II;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.peopleware.bean_V.RousseauBean;
import be.peopleware.bean_V._Test_AbstractRousseauBean;
import be.peopleware.test_I.Test;
import be.peopleware.test_I.java.lang._Test_Long;


/**
 * @author      Jan Dockx
 * @author      PeopleWare n.v.
 */
public class _Test_AbstractPersistentBean extends _Test_AbstractRousseauBean {

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

  protected static final String EMPTY = ""; //$NON-NLS-1$

  public static void main(final String[] args) {
    Test.main(new String[]
        {"be.peopleware.presistence_I._Test_PersistentBean"}); //$NON-NLS-1$
  }

  protected void testClassMethods() {
    test_PersistentBean__();
  }

  protected void testInstanceMethods() {
    super.testInstanceMethods();
    test_setId_Long_();
    test_hasSameId_PersistentBean_();
    test_toString__();
  }

  /*<section name="test cases">*/
  //-------------------------------------------------------------------

  protected final RousseauBean create_RousseauBean() {
    return create_PersistentBean();
  }

  protected PersistentBean create_PersistentBean() {
    return new Stub();
  }

  protected Set getCases() {
    Set result = new HashSet();
    PersistentBean po;
    Iterator iterLong = new _Test_Long().getCasesWithNull().iterator();
    while (iterLong.hasNext()) {
      Long id = (Long)iterLong.next();
      po = create_PersistentBean();
      po.setId(id);
      result.add(po);
    }
    return result;
  }

  /*</section>*/



  /*<section name="type invariants">*/
  //-------------------------------------------------------------------

  // nothing to overwrite here

  /*</section>*/



  /*<section name="class methods">*/
  //-------------------------------------------------------------------

  private void test_PersistentBean__() {
    try {
      PersistentBean po = create_PersistentBean();
      validate(po.getId() == null);
      validateTypeInvariants(po);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/



  /*<section name="instance methods">*/
  //-------------------------------------------------------------------

  protected final void test_setId_Long_() {
    Iterator iterPO = getCases().iterator();
    while (iterPO.hasNext()) {
      PersistentBean subject = (PersistentBean)iterPO.next();
      Iterator iterLong = new _Test_Long().getCasesWithNull().iterator();
      while (iterLong.hasNext()) {
        Long id = (Long)iterLong.next();
        test_setId_Long_(subject, id);
      }
    }
  }

  protected void test_setId_Long_(final PersistentBean subject,
                                  final Long id) {
    try {
      subject.setId(id);
      validate(id != null ? subject.getId().equals(id)
                          : subject.getId() == null);
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/

  protected final void test_toString__() {
    Iterator iter = getCases().iterator();
    while (iter.hasNext()) {
      test_toString__((PersistentBean)iter.next());
    }
  }

  protected void test_toString__(final PersistentBean subject) {
    try {
      subject.toString();
      // nothing to validate, not interested in result
      validateTypeInvariants(subject);
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  protected final void test_hasSameId_PersistentBean_() {
    Iterator iterOther = getCases().iterator();
    PersistentBean other1 = null;
    if (iterOther.hasNext()) {
      other1 = (PersistentBean)iterOther.next();
    }
    else {
      assert false;
    }
    Iterator iter1 = getCases().iterator();
    while (iter1.hasNext()) {
      PersistentBean subject = (PersistentBean)iter1.next();
      test_hasSameId_PersistentBean_(subject, other1);
      test_hasSameId_PersistentBean_(subject, subject);
      test_hasSameId_PersistentBean_(subject, null);
      if (other1 != null) {
        test_hasSameId_PersistentBean_(other1, subject);
      }
    }
  }

  protected final void test_hasSameId_PersistentBean_(
      final PersistentBean subject,
      final PersistentBean other) {
    try {
      boolean result = subject.hasSameId(other);
      validate(result == ((other != null)
                          && ((subject.getId() == null)
                              ? other.getId() == null
                              : subject.getId().equals(other.getId()))));
      validateTypeInvariants(subject);
      if (other != null) {
        validateTypeInvariants(other);
      }
    }
    catch (Throwable t) {
      unexpectedThrowable(t);
    }
  }

  /*</section>*/

  private static class Stub extends AbstractPersistentBean {
    // NOP
  }

}
