package be.peopleware.taglet.standalone;


import java.util.Map;
import be.peopleware.taglet.AbstractStandaloneTaglet;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom taglet <code>@post</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class PostTaglet extends AbstractStandaloneTaglet {
  /**
   * @see       TagletRegistrar#getName()
   */
  public String getName() {
    return "post"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Post Conditions:"; //$NON-NLS-1$
  }

  /**
   * Register this taglet.
   *
   * @param     tagletMap
   * 						the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new PostTaglet());
  }

  /**
   * @see       TagletRegistrar#setTagletScopes()
   */
  protected void setTagletScopes() {
    inField       = false;
    inConstructor = true;
    inMethod      = true;
    inOverview    = false;
    inPackage     = false;
    inType        = false;
  }

}
