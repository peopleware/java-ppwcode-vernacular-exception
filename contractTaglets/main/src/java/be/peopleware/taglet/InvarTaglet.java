package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet-class for custom taglet <code>@invar</code>
 * @author Peopleware n.v.
 */
public class InvarTaglet extends TagletRegistrar {

	/**
	 * name of the taglet.
	 */
  private static final String name = "invar"; //$NON-NLS-1$
	/**
	 * header of the taglet - used in generated documentation.
	 */
  private static final String header = "Type Invariants:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new InvarTaglet());
  }


	protected void setTagletScopes() {
	   bInField 			= false;
	   bInConstructor = false;
	   bInMethod 			= false;
	   bInOverview 		= false;
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
