package be.peopleware.taglet.standalone;


import java.util.Map;
import be.peopleware.taglet.StandaloneTagletRegistrar;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom taglet <code>@todo</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class TodoTaglet extends StandaloneTagletRegistrar {
  /**
   * name of the taglet.
   */
  public String getName() {
    return "todo"; //$NON-NLS-1$
  }

  /**
   * header of the taglet - used in generated documentation.
   */
  public String getHeader() {
    return "Todo:"; //$NON-NLS-1$
  }

  /**
   * Register this taglet
   *
   * @param     tagletMap
   * 						the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new TodoTaglet());
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
