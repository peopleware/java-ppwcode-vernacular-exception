package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet for custom tag <code>@invar</code>
 * @author Peopleware n.v.
 */
public class InvarTag extends TagletRegistrar {

	/**
	 * name of the tag.
	 */
  private static final String name = "invar"; //$NON-NLS-1$
	/**
	 * header of the tag - used in generated documentation.
	 */
  private static final String header = "Type Invariants:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this tag to.
   */
  public static void register(Map tagletMap) {
    InvarTag tag = new InvarTag();
    TagletRegistrar.registerTaglet(tagletMap, tag);
  }


	protected void setTagScopes() {
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
