package be.peopleware.bean_II.persistent;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import be.peopleware.bean_II.persistent.AsynchronousCRUD;


/**
 * Support for implementations of {@link AbstractAsynchronousCrudFactory}.
 * 
 * @toryt:cC org.toryt.contract.Collections;
 * 
 * @mudo (jand) move to ppw-bean
 */
public abstract class AbstractAsynchronousCrudFactory implements AsynchronousCrudFactory {

  public final Set getActiveInstances() {
    return Collections.unmodifiableSet($activeInstances);
  }
  
  /**
   * @pre asyncCrud != null;
   * @pre ! getActiveInstances().contains(asyncCrud);
   * @post getActiveInstances().contains(asyncCrud);
   */
  protected final void addActiveInstance(AsynchronousCRUD asyncCrud) {
    assert asyncCrud != null;
    assert ! getActiveInstances().contains(asyncCrud);
    $activeInstances.add(asyncCrud);
  }
  
  /**
   * @pre getActiveInstances().contains(asyncCrud);
   * @post ! getActiveInstances().contains(asyncCrud);
   */
  protected final void removeActiveInstance(AsynchronousCRUD asyncCrud) {
    assert getActiveInstances().contains(asyncCrud);
    $activeInstances.remove(asyncCrud);
  }

  /**
   * @invar $activeInstances != null;
   * @invar cC:noNull($activeInstances);
   * @invar cC:instanceof($activeInstances, AsynchronousCRUD.class);
   */
  private final Set $activeInstances = new HashSet();

}