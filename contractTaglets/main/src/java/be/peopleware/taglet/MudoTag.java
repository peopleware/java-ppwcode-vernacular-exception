package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet for custom tag <code>@mudo</code>
 * @author Peopleware n.v.
 */
public class MudoTag extends TagletRegistrar {

	/**
	 * name of the tag.
	 */
  private static final String name = "mudo"; //$NON-NLS-1$
	/**
	 * header of the tag - used in generated documentation.
	 */
  private static final String header = "Must do:"; //$NON-NLS-1$
	
  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this tag to.
   */
  public static void register(Map tagletMap) {
    MudoTag tag = new MudoTag();
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


	public String getName() {
		return name;
	}

	public String getHeader() {
		return header;
	}

}
