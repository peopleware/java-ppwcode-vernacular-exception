package be.peopleware.bean_II.persistent;


import java.util.Set;

import be.peopleware.bean_II.persistent.AsynchronousCRUD;
import be.peopleware.exception_I.TechnicalException;


/**
 * Factory for delayed construction of {@link AsynchronousCRUD} instances.
 * Instance that have been created through an instance of this type
 * <strong>have</strong> to be discarded by {@link #release(AsynchronousCRUD)}
 * giving them back to that same instance after their use.
 * 
 * @toryt:cC org.toryt.contract.Collections;
 * @invar getActiveInstances() != null;
 * @invar cC:noNull(getActiveInstances());
 * @invar cC:instanceof(getActiveInstances(), AsynchronousCRUD.class);
 * 
 * @mudo (jand) move to ppw-bean
 */
public interface AsynchronousCrudFactory {

  /**
   * Create a fresh instance of type {@link AsynchronousCRUD}
   * or a subtype.
   * 
   * @result result != null;
   * @post getActiveInstances().contains(result);
   * @throws TechnicalException
   */
  AsynchronousCRUD create() throws TechnicalException;

  /**
   * Cleanup and discard <code>asyncCRUD</code>.
   * <code>asyncCRUD</code> <strong>will</strong> be an object that was
   * created by this factory.
   * 
   * @pre asyncCRUD != null;
   * @pre getActiveInstances().contains(asyncCrud);
   * @post ! getActiveInstances().contains(asyncCrud);
   */
  void release(AsynchronousCRUD asyncCrud);
  
  /**
   * The instances that are created by this instance, that have not yet
   * been released.
   * 
   * @basic
   */
  Set getActiveInstances();
  
}