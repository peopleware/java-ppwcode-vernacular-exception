package be.peopleware.taglet.standalone;


import java.util.Map;
import be.peopleware.taglet.StandaloneTagletRegistrar;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom taglet <code>@post</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class PostTaglet extends StandaloneTagletRegistrar {
  /**
   * name of the taglet.
   */
  public String getName() {
    return "post"; //$NON-NLS-1$
  }

  /**
   * header of the taglet - used in generated documentation.
   */
  public String getHeader() {
    return "Post Conditions:"; //$NON-NLS-1$
  }

  /**
   * Register this taglet
   *
   * @param     tagletMap
   * 						the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new PostTaglet());
  }

  protected void setTagletScopes() {
     bInField 			= false;
     bInConstructor = true;
     bInMethod 			= true;
     bInOverview 		= false;
     bInPackage 		= false;
     bInType 				= false;
  }

}
