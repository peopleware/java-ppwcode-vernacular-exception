package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet-class for custom taglet <code>@pre</code>
 * @author Peopleware n.v.
 */
public class PreTaglet extends TagletRegistrar {

	/**
	 * name of the taglet.
	 */
  private static final String name = "pre"; //$NON-NLS-1$
	/**
	 * header of the taglet - used in generated documentation.
	 */
  private static final String header = "Preconditions:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new PreTaglet());
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
