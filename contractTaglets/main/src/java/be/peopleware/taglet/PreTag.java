package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet for custom tag <code>@pre</code>
 * @author Peopleware n.v.
 */
public class PreTag extends TagletRegistrar {

	private final String name = "pre"; //$NON-NLS-1$
  private final String header = "Preconditions:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this tag to.
   */
  public static void register(Map tagletMap) {
    PreTag tag = new PreTag();
    TagletRegistrar.registerTaglet(tagletMap, tag);
  }

	protected void setTagScopes() {
	   bInField 			= false;
	   bInConstructor = true;
	   bInMethod 			= true;
	   bInOverview 		= false;
	   bInPackage 		= false;
	   bInType 				= false;
	   bInLine 				= false;
	}

}
