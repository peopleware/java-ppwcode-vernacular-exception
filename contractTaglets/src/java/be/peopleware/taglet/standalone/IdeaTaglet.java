package be.peopleware.taglet.standalone;


import java.util.Map;
import be.peopleware.taglet.AbstractStandaloneTaglet;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom taglet <code>@idea</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class IdeaTaglet extends AbstractStandaloneTaglet {
  /**
   * @see       TagletRegistrar#getName()
   */
  public String getName() {
    return "idea"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Idea:"; //$NON-NLS-1$
  }

  /**
   * Register this taglet.
   *
   * @param     tagletMap
   * 						The map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new IdeaTaglet());
  }

  /**
   * @see       TagletRegistrar#setTagletScopes()
   */
  protected void setTagletScopes() {
    inField       = true;
    inConstructor = true;
    inMethod      = true;
    inOverview    = true;
    inPackage     = true;
    inType        = true;
  }

}
