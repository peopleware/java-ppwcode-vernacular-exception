/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_I.ramstorage;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import be.peopleware.bean_IV.CompoundPropertyException;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_I.IdNotFoundException;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.AsyncCrudDao;


/**
 * {@link AsyncCrudDao} that works
 * with a {@link RamStorage}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public class RamStorageAsyncCrudDao extends AbstractRamStorageDao implements AsyncCrudDao {

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

  public final void startTransaction() throws TechnicalException {
    $toCreate = new HashSet();
    $toDelete = new HashSet();
    setInTransaction(true);
  }

  public final void commitTransaction(final PersistentBean arg0)
      throws CompoundPropertyException, TechnicalException {
    commitCreate();
    commitDelete();
    setInTransaction(false);
  }

  public final void cancelTransaction() throws TechnicalException {
    // TODO (jand) cannot rollback changes to values of pb's
    $toCreate = null;
    $toDelete = null;
    setInTransaction(false);
  }

  public final void createPersistentBean(final PersistentBean arg0)
      throws CompoundPropertyException, TechnicalException {
    arg0.normalize();
    arg0.checkCivility();
    $toCreate.add(arg0);
  }

  public final PersistentBean retrievePersistentBean(final Long arg0, final Class arg1)
      throws IdNotFoundException, TechnicalException {
    if (getRamStorage() == null) {
      throw new TechnicalException("no RamStorage found", null);
    }
    PersistentBean result = getRamStorage().getMap(arg1).find(arg0);
    if (result == null) {
      throw new IdNotFoundException(arg0, "could not find object", null, arg1);
    }
    return result;
  }

  public final Set retrieveAllPersistentBeans(final Class arg0, final boolean arg1)
      throws TechnicalException {
    if (getRamStorage() == null) {
      throw new TechnicalException("no RamStorage found", null);
    }
    return getRamStorage().getMap(arg0).all();
  }

  public final void updatePersistentBean(final PersistentBean arg0)
      throws CompoundPropertyException, TechnicalException {
    // NOP
  }

  public final void deletePersistentBean(final PersistentBean arg0)
      throws TechnicalException {
    $toDelete.add(arg0);
  }

  public final boolean isDeleted(final PersistentBean arg0) {
    return $toDelete.contains(arg0);
  }

  private void commitDelete() throws TechnicalException {
    if (getRamStorage() == null) {
      throw new TechnicalException("no RamStorage found", null);
    }
    Iterator iter = $toDelete.iterator();
    while (iter.hasNext()) {
      PersistentBean pb = (PersistentBean)iter.next();
      getRamStorage().getMap(pb.getClass()).remove(pb);
      pb.setId(null);
    }
    $toDelete = null;
  }

  private Set $toDelete = new HashSet();

  private void commitCreate() throws TechnicalException {
    if (getRamStorage() == null) {
      throw new TechnicalException("no RamStorage found", null);
    }
    Iterator iter = $toCreate.iterator();
    while (iter.hasNext()) {
      PersistentBean pb = (PersistentBean)iter.next();
      getRamStorage().getMap(pb.getClass()).add(pb);
    }
    $toCreate = null;
  }

  private Set $toCreate;

  public final boolean isInTransaction() {
    return $isInTransaction;
  }

  protected final void setInTransaction(final boolean inTransaction) {
    $isInTransaction = inTransaction;
  }

  private boolean $isInTransaction;

}
