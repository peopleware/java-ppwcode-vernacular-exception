/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_II.ramstorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Emulation of a database in RAM.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public class RamStorage {

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

  /**
   * Map with all the {@link RamStorageMap} instances.
   * The key is the type of the map.
   */
  private HashMap $pbMaps = new HashMap();

  /**
   * @mudo missing specification
   */
  public synchronized RamStorageMap getMap(final Class beanType) {
    assert beanType != null;
    RamStorageMap skpbm =
      (RamStorageMap)$pbMaps.get(beanType);
    if (skpbm == null) {
      skpbm = new RamStorageMap(beanType);
      $pbMaps.put(beanType, skpbm);
    }
    return skpbm;
  }
  
  public synchronized Map getRamStorageMaps() {
    return Collections.unmodifiableMap($pbMaps);
  }

}
