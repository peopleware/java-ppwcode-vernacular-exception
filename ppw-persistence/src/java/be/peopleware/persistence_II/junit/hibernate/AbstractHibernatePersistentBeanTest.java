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

  protected AbstractHibernatePersistentBeanTest(Class classUnderTest) {
    assert classUnderTest != null;
    assert PersistentBean.class.isAssignableFrom(classUnderTest);
    $classUnderTest = classUnderTest;
  }

  public final Class getClassUnderTest() {
    return $classUnderTest;
  }

  private Class $classUnderTest;


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
      if (LOG.isDebugEnabled() && (count > 100)) {
        LOG.debug("limiting checks to 100 instances when debug is enabled");
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

  protected final ClassContract getClassContract() {
    ClassContract result = null;
    try {
      result = (ClassContract)Contracts.typeContractInstance(getClassUnderTest());
    }
    catch (IOException e) {
      assert false : "IOExceptionshould not happen: " + e;
    }
    catch (ClassNotFoundException e) {
      assert false : "ClassNotFoundExceptionshould not happen: " + e;
    }
    return result;
  }

  protected void validatePersistentBean(PersistentBean pb) {
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
    if (LOG.isWarnEnabled() && (! civilized)) {
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

  protected void extraPersistentBeanValidation(PersistentBean pb) {
    // NOP
  }

  private void validateTypeInvariants(Object instance) {
    assert instance != null;
    LOG.debug("getClassContract(): " + getClassContract());
    Set invars = getClassContract().getTypeInvariantConditions();
    Map context = new HashMap();
    context.put(Condition.SUBJECT_KEY, instance);
    Iterator iter = invars.iterator();
    while (iter.hasNext()) {
      Condition c = (Condition)iter.next();
      boolean result = c.validate(context);
      if (LOG.isErrorEnabled() && (! result)) {
        LOG.error("type invariant violation: " + c + " for " + instance);
      }
      assertTrue(result);
    }
  }

}
