package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet for custom tag <code>@result</code>
 * @author Peopleware n.v.
 */
public class ResultTag extends TagletRegistrar {

	private final String name = "result"; //$NON-NLS-1$
  private final String header = "Result:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this tag to.
   */
  public static void register(Map tagletMap) {
    ResultTag tag = new ResultTag();
    TagletRegistrar.registerTaglet(tagletMap, tag);
  }

	protected void setTagScopes() {
	   bInField 			= false;
	   bInConstructor = false;
	   bInMethod 			= true;
	   bInOverview 		= false;
	   bInPackage 		= false;
	   bInType 				= false;
	   bInLine 				= false;
	}

}
