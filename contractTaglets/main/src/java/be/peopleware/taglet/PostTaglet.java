package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet-class for custom taglet <code>@post</code>
 * @author Peopleware n.v.
 */
public class PostTaglet extends TagletRegistrar {

	/**
	 * name of the taglet.
	 */
  private static final String name = "post"; //$NON-NLS-1$
	/**
	 * header of the taglet - used in generated documentation.
	 */
  private static final String header = "Postconditions:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this taglet to.
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
	   bInLine 				= false;
	}

	public String getName() {
		return name;
	}

	public String getHeader() {
		return header;
	}

}
