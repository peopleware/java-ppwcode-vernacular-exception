package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet-class for custom taglet <code>@idea</code>
 * @author Peopleware n.v.
 */
public class IdeaTaglet extends TagletRegistrar {

	/**
	 * name of the taglet.
	 */
  private static final String name = "idea"; //$NON-NLS-1$
	/**
	 * header of the taglet - used in generated documentation.
	 */
  private static final String header = "Idea:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new IdeaTaglet());
  }

	protected void setTagletScopes() {
	   bInField 			= true;
	   bInConstructor = true;
	   bInMethod 			= true;
	   bInOverview 		= true;
	   bInPackage 		= true;
	   bInType 				= true;
	   bInLine 				= false;
	}

	public String getName() {
		return name;
	}

	public String getHeader() {
		return header;
	}

}
