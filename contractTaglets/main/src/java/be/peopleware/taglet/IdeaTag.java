package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet for custom tag <code>@idea</code>
 * @author Peopleware n.v.
 */
public class IdeaTag extends TagletRegistrar {

	private final String name = "idea"; //$NON-NLS-1$
  private final String header = "Idea:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this tag to.
   */
  public static void register(Map tagletMap) {
    IdeaTag tag = new IdeaTag();
    TagletRegistrar.registerTaglet(tagletMap, tag);
  }

	protected void setTagScopes() {
	   bInField 			= true;
	   bInConstructor = true;
	   bInMethod 			= true;
	   bInOverview 		= true;
	   bInPackage 		= true;
	   bInType 				= true;
	   bInLine 				= false;
	}

}
