package be.peopleware.taglet.standalone;


import java.util.Map;
import be.peopleware.taglet.AbstractStandaloneTaglet;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom taglet <code>@result</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class ResultTaglet extends AbstractStandaloneTaglet {
  /**
   * @see       TagletRegistrar#getName()
   */
  public String getName() {
    return "result"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Result:"; //$NON-NLS-1$
  }

  /**
   * Register this taglet.
   *
   * @param     tagletMap
   * 						the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new ResultTaglet());
  }

  /**
   * @see       TagletRegistrar#setTagletScopes()
   */
  protected void setTagletScopes() {
    inField       = false;
    inConstructor = false;
    inMethod      = true;
    inOverview    = false;
    inPackage     = false;
    inType        = false;
  }

}
