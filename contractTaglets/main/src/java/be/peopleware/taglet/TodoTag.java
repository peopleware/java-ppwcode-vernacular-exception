package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet for custom tag <code>@todo</code>
 * @author Peopleware n.v.
 */
public class TodoTag extends TagletRegistrar {

	private final String name = "todo"; //$NON-NLS-1$
  private final String header = "To Do:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this tag to.
   */
  public static void register(Map tagletMap) {
    TodoTag tag = new TodoTag();
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

}
