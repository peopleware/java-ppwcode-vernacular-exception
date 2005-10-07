/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.junit.hibernate;



import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.toryt.ClassContract;
import org.toryt.Condition;
import org.toryt.Contracts;

import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.bean_V.PropertyException;
import be.peopleware.persistence_II.PersistentBean;


/**
 * A simple helper class for hibernate actions within jUnit tests.
 *
 * @invar   getClassUnderTest() != null;
 * @invar   PersistentBean.class.isAssignableFrom(getClassUnderTest());
 * @author  David Van Keer
 * @author  Peopleware n.v.
 * @todo    (nsmeets) Copied from WoundPilot.
 */
public abstract class AbstractHibernatePersistentBeanTest extends AbstractHibernateTest {

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


  private static final Log LOG = LogFactory.getLog(AbstractHibernatePersistentBeanTest.class);

  /**
   * Create a new test for the given class.
   *
   * @param  classUnderTest
   * @pre    classUnderTest != null;
   * @pre    PersistentBean.class.isAssignableFrom(classUnderTest);
   * @post   new.getClassUnderTest() == classUnderTest;
   */
  protected AbstractHibernatePersistentBeanTest(final Class classUnderTest) {
    assert classUnderTest != null;
    assert PersistentBean.class.isAssignableFrom(classUnderTest);
    $classUnderTest = classUnderTest;
  }

 /*<property name="alarmSymptoms">*/
 //------------------------------------------------------------------

  /**
   * Returns the class that is tested.
   *
   * @basic
   */
  public final Class getClassUnderTest() {
    return $classUnderTest;
  }

  private Class $classUnderTest;

  /*</property>*/

  /**
   * The number of persistent beans that is tested when logging is debug enabled.
   *
   * <strong>= {@value}</strong>
   */
  public static final int NB_TESTS_DEBUG = 100;

  /**
   * Tests all instances of {@link #getClassUnderTest()} in the underlying
   * storage.
   * The method {@link #validatePersistentBean(PersistentBean)} is used to test
   * the persistent beans.
   * When logging is debug enabled, the number of persistent beans that is tested is
   * limited to NB_TESTS_DEBUG.
   */
  public void testAlInstances() {
    LOG.debug("Opening Hibernate session and starting a new transaction.");
    openSession();
    LOG.info("Retrieving instances of " + getClassUnderTest() + " from database in a new session.");
    Collection pbs = loadInstancesToTest();
    LOG.info("Retrieved " + pbs.size() + " PersistentBeans.");
    Iterator iter = pbs.iterator();
    int count = 0;
    while (iter.hasNext()) {
      count++;
      if (LOG.isDebugEnabled() && (count > NB_TESTS_DEBUG)) {
        LOG.debug("limiting checks to " + NB_TESTS_DEBUG + " instances when debug is enabled");
        break;
      }
      if (LOG.isWarnEnabled() && (count % 5000 == 0)) {
        LOG.info("instances processed: " + count);
      }
      PersistentBean pb = (PersistentBean)iter.next();
      validatePersistentBean(pb);
    }
    LOG.debug("Closing session");
    closeSession();
  }

  /**
   * Overwrite if you do not wish to test all instances.
   * Session is open.
   */
  protected Collection loadInstancesToTest() {
    return retrieve(getClassUnderTest());
  }

  /**
   * Retrieves the class contract corresponding to the class that is tested.
   *
   * @return (ClassContract)Contracts.typeContractInstance(getClassUnderTest())
   *               does not throw an exception
   *           ? result == (ClassContract)Contracts.typeContractInstance(getClassUnderTest())
   *           : result == null;
   */
  protected final ClassContract getClassContract() {
    ClassContract result = null;
    try {
      result = (ClassContract)Contracts.typeContractInstance(getClassUnderTest());
    }
    catch (IOException e) {
      assert false : "IOException should not happen: " + e;
    }
    catch (ClassNotFoundException e) {
      assert false : "ClassNotFoundException should not happen: " + e;
    }
    return result;
  }

  /**
   * Validate the given persistent bean.
   * The following validations are executed:
   * - the given persistent bean should be effective
   * - the invariants are checked
   * - some extra validation, using {@link #extraPersistentBeanValidation(PersistentBean)}
   */
  protected void validatePersistentBean(final PersistentBean pb) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("pb: " + ((pb == null) ? "null" : pb.toStringLong()));
    }
    assertNotNull(pb);
    validateTypeInvariants(pb);
    boolean civilized = pb.isCivilized();
    /* data in DB must not really be civilized. What we STORE must be,
     * but what we get doesn't have to be (as long as type invariants
     * are ok.
     * But it is something weird: WARN.
     */
    if (LOG.isWarnEnabled() && (!civilized)) {
      CompoundPropertyException cpe = pb.getWildExceptions();
      LOG.warn("Not civilized: " + pb);
      Iterator iter1 = cpe.getElementExceptions().values().iterator();
      while (iter1.hasNext()) {
        Set peSet = (Set)iter1.next();
        Iterator iter2 = peSet.iterator();
        while (iter2.hasNext()) {
          PropertyException pe = (PropertyException)iter2.next();
          LOG.warn("    " + pe.getLocalizedMessage());
          LOG.warn("    originType: " + pe.getOriginType());
          LOG.warn("    origin: " + pe.getOrigin());
          LOG.warn("    propertyName: " + pe.getPropertyName());
        }
      }
    }
    extraPersistentBeanValidation(pb);
  }

  /**
   * Some extra validation to be performed on the given persistent bean.
   * Should be overridden by subclasses.
   */
  protected void extraPersistentBeanValidation(final PersistentBean pb) {
    // NOP
  }

  private void validateTypeInvariants(final Object instance) {
    assert instance != null;
    LOG.debug("getClassContract(): " + getClassContract());
    Set invars = getClassContract().getTypeInvariantConditions();
    Map context = new HashMap();
    context.put(Condition.SUBJECT_KEY, instance);
    Iterator iter = invars.iterator();
    while (iter.hasNext()) {
      Condition c = (Condition)iter.next();
      boolean result = c.validate(context);
      if (LOG.isErrorEnabled() && (!result)) {
        LOG.error("type invariant violation: " + c + " for " + instance);
      }
      assertTrue(result);
    }
  }

}
