package be.peopleware.taglet.standalone;


import java.util.Map;
import be.peopleware.taglet.StandaloneTagletRegistrar;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom taglet <code>@mudo</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class MudoTaglet extends StandaloneTagletRegistrar {
  /**
   * name of the taglet.
   */
  public String getName() {
    return "mudo"; //$NON-NLS-1$
  }

  /**
   * header of the taglet - used in generated documentation.
   */
  public String getHeader() {
    return "Must do:"; //$NON-NLS-1$
  }

  /**
   * Register this taglet
   *
   * @param     tagletMap
   * 						the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new MudoTaglet());
  }

  protected void setTagletScopes() {
     bInField 			= true;
     bInConstructor = true;
     bInMethod 			= true;
     bInOverview 		= true;
     bInPackage 		= true;
     bInType 				= true;
  }

}
