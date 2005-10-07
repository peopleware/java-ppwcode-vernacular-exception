/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.junit.hibernate;



import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;
import be.peopleware.persistence_II.PersistentBean;


/**
 * A simple helper class for hibernate actions within jUnit tests.
 *
 * @author  David Van Keer
 * @author  Peopleware n.v.
 * @todo    (nsmeets) Copied from WoundPilot.
 */
public abstract class AbstractHibernateTest extends TestCase {

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


//  private static final Log LOG = LogFactory.getLog(AbstractHibernateTest.class);

  private static SessionFactory $sessionFactory;

  private static final String JUNIT_CONFIG_FILE_LOCATION =
      "/hibernate_junit.cfg.xml";

  static {
    Configuration configuration = new Configuration();
    try {
      configuration.configure(JUNIT_CONFIG_FILE_LOCATION);
      $sessionFactory = configuration.buildSessionFactory();
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Hibernate configuration is invalid.");
    }
  }

  public void openSession() {
    try {
      $session = $sessionFactory.openSession();
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Couldn't open a new hibernate session.");
    }
  }

  public void closeSession() {
    try {
      $session.close();
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Failed to close the hibernate session.");
    }
  }

  public void beginTransaction() {
    try {
      $tx = $session.beginTransaction();
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Couldn't start a hibernate transaction.");
    }
  }

  public void commitTransaction() {
    try {
      $tx.commit();
      $tx = null;
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Failed to commit the hibernate transaction.");
    }
  }

  public void rollbackTransaction() {
    try {
      $tx.rollback();
      $tx = null;
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Failed to cancel the hibernate transaction.");
    }
  }

  public Long create(final Object object) {
    try {
      $session.save(object);
      if (object instanceof PersistentBean) {
        return ((PersistentBean)object).getId();
      }
      else {
        return null;
      }
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Failed to create the object in the database.");
    }
    return null;
  }

  public void update(final Object object) {
    try {
      $session.update(object);
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Failed to update the object in the database.");
    }
  }

  public void delete(final Object object) {
    try {
      $session.delete(object);
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Failed to delete the object to the database.");
    }
  }

  public Object retrieve(final Class clazz, final Long id) {
    Object result = null;
    try {
      result = $session.load(clazz, id);
    }
    catch (ObjectNotFoundException onfExc) {
      return null;
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Failed to retrieve the object from the database.");
    }
    return result;
  }

  public Set retrieve(final Class persistentObjectType) {
    Set results = new HashSet();
    try {
      results.addAll($session.createCriteria(persistentObjectType).list());
    }
    catch (HibernateException hExc) {
      hExc.printStackTrace();
      fail("Failed to retrieve objects from database");
    }
    return results;
  }

  public List retrieve(String HqlQueryString) {
    List roles = null;
    try {
      Query q = getSession().createQuery(HqlQueryString);
      roles = q.list();
    }
    catch (HibernateException e) {
      assert false : "HibernateExceptionshould not happen: " + e;
    }
    return roles;
  }

  public Session getSession() {
    return $session;
  }

  private Session $session;



  public Transaction getTransaction() {
    return $tx;
  }

  private Transaction $tx;

}
