package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet-class for custom taglet <code>@result</code>
 * @author Peopleware n.v.
 */
public class ResultTaglet extends TagletRegistrar {

	/**
	 * name of the taglet.
	 */
  private static final String name = "result"; //$NON-NLS-1$
	/**
	 * header of the taglet - used in generated documentation.
	 */
  private static final String header = "Result:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new ResultTaglet());
  }

	protected void setTagletScopes() {
	   bInField 			= false;
	   bInConstructor = false;
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
