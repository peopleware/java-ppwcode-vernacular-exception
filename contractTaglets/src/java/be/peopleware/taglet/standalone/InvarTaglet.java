package be.peopleware.taglet.standalone;


import java.util.Map;
import be.peopleware.taglet.StandaloneTagletRegistrar;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom taglet <code>@invar</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class InvarTaglet extends StandaloneTagletRegistrar {
  /**
   * name of the taglet.
   */
  public String getName() {
    return "invar"; //$NON-NLS-1$
  }

  /**
   * header of the taglet - used in generated documentation.
   */
  public String getHeader() {
    return "Type Invariants:"; //$NON-NLS-1$
  }

  /**
   * Register this taglet
   *
   * @param     tagletMap
   * 						the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new InvarTaglet());
  }

  protected void setTagletScopes() {
     bInField 			= false;
     bInConstructor = false;
     bInMethod 			= false;
     bInOverview 		= false;
     bInPackage 		= true;
     bInType 				= true;
  }

}
