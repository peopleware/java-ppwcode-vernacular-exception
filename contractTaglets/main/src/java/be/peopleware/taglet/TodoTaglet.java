package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet-class for custom taglet <code>@todo</code>
 * @author Peopleware n.v.
 */
public class TodoTaglet extends TagletRegistrar {

	/**
	 * name of the taglet.
	 */
  private static final String name = "todo"; //$NON-NLS-1$
	/**
	 * header of the taglet - used in generated documentation.
	 */
  private static final String header = "To do:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this taglet to.
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
	   bInLine 				= false;
	}


	public String getName() {
		return name;
	}

	public String getHeader() {
		return header;
	}

}
