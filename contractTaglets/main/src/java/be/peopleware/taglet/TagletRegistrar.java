package be.peopleware.taglet;


import java.util.Map;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;


/**
 * Generic Taglet that implements methods from interface Taglet.
 * <br />Custom taglet can extend this class and override method 
 * {@link #setTagletScopes()} to define scopes where this taglet is valid.
 * <br />In addition to implementing abstract methods defined here, 
 * each subclass should also supply method 
 * <code>static void register(Map tagletMap)</code>.
 * For example, the following implementation 
 * for the class Foo should do the trick:
 * <code>
 * <dl>
 * <dt>public static void register(Map tagletMap) {</dt>
 * <dd>   TagletRegistrar.registerTaglet(tagletMap, new Foo());</dd>
 * <dt>}</dt>
 * <dl>
 * </code>
 * @todo  This taglet can be used in any kind of {@link com.sun.javadoc.Doc}. 
 * 				It is not an inline tag. 
 * 				The text reminds the developer to perform a task. 
 * 				<br />For example, <code>@todo Fix this!</code> would be shown as:
 *        <dl>
 * 					<dt><strong>To Do: </strong></dt>
 * 					<dd>
 *        		<table cellpadding=2 cellspacing=0>
 *        			<tr><td><code>Fix this!</code></td></tr>
 *        		</table>
 * 					</dd>
 * 				</dl>
 * 
 * @invar    getName()   != null && getName().length()   > 0 
 * 		    && getHeader() != null && getHeader().length() > 0
 * 
 * @mudo test taglet <code>@mudo</code>
 * 
 * @idea use code generation tools to generate code for subclasses.
 * 
 * @question (to david) maven properties for taglets
 * 
 * @author      Jan Dockx
 */
public abstract class TagletRegistrar implements Taglet {

  /**
   * Used to determine if this taglet can be used 
   * in <strong>field documentation</strong>.
   * <br />Default value is false.
   */
  protected boolean bInField = false;
  /**
   * Used to determine if this taglet can be used 
   * in <strong>constructor documentation</strong>.
   * <br />Default value is false.
   */
  protected boolean bInConstructor = false;

  /**
   * Used to determine if this taglet can be used 
   * in <strong>method documentation</strong>.
   * <br />Default value is false.
   */
  protected boolean bInMethod = false;

  /**
   * Used to determine if this taglet can be used 
   * in <strong>overview documentation</strong>.
   * <br />Default value is false.
   */
  protected boolean bInOverview = false;

  /**
   * Used to determine if this taglet can be used 
   * in <strong>package documentation</strong>.
   * <br />Default value is false.
   */
  protected boolean bInPackage = false;

  /**
   * Used to determine if this taglet can be used 
   * in <strong>type documentation</strong>.
   * <br />Default value is false.
   */
  protected boolean bInType = false;

  /**
   * Used to determine if this taglet is an <strong>inline taglet</strong>.
   * <br />Default value is false.
   */
  protected boolean bInLine = false;

  /**
   * Calls astract method <code>setTagScopes()</code>
   * so that we are sure that in superclass this method is called at least once. 
   */
  protected TagletRegistrar() {
  	setTagletScopes();
  }
  
  /**
   * Defines scopes where this taglet can be used.
   * Setting certain boolean variable to true enables usage 
   * in corresponding documentation scope.
   * For example, taglet <code>todo</code> would have following settings:
   * <br />
   * <code>
   * <br />bInField 			= true;
   * <br />bInConstructor = true;
   * <br />bInMethod 			= true;
   * <br />bInOverview 		= true;
   * <br />bInPackage 		= true;
   * <br />bInType 				= true;
   * <br />bInLine 				= false;
   * </code>
   * <br />
   * <br />By default, all variables are set to false.
   *
   */
  protected abstract void setTagletScopes();
  
  /** 
   * Makes some additional formatting of the content of the taglet.
   * 
   * @param text content of the taglet
   * @return text - formatted content
   */
  public String parse(String text) {
  	return text;
  }
  
  /**
   * Return the name of this custom taglet.
   * @result != null && result.getLength() > 0
   */
  public abstract String getName();

  /**
   * Return the header of this custom taglet - used in generated documentation.
   * @result != null && result.getLength() > 0
   */
  public abstract String getHeader();

  /**
   * Used to determine if this taglet can be used in 
   * <strong>field documentation</strong>.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInField} to true
   * in method {@link #setTagletScopes()}
   * @return value of {@link #bInField}
   */
  public boolean inField() {
    return bInField;
  }

  /**
   * Used to determine if this taglet can be used 
   * in <strong>constructor documentation</strong>.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInConstructor} to true
   * in method {@link #setTagletScopes()}
   * @return value of {@link #bInConstructor}
   */
  public boolean inConstructor() {
    return bInConstructor;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>method documentation</strong>.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInMethod} to true
   * in method {@link #setTagletScopes()}
   * @return value of {@link #bInMethod}
   */
  public boolean inMethod() {
    return bInMethod;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>overview documentation</strong>.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInOverview} to true
   * in method {@link #setTagletScopes()}
   * @return value of {@link #bInOverview}
   */
  public boolean inOverview() {
    return bInOverview;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>package documentation</strong>.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInPackage} to true
   * in method {@link #setTagletScopes()}
   * @return value of {@link #bInPackage}
   */
  public boolean inPackage() {
    return bInPackage;
  }

  /**
   * Used to determine if this taglet can be used 
   * in <strong>type documentation</strong>.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInType} to true
   * in method {@link #setTagletScopes()}
   * @return value of {@link #bInType}
   */
  public boolean inType() {
    return bInType;
  }

  /**
   * Used to determine if this taglet can be used 
   * in <strong>inline documentation</strong>.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInLine} to true
   * in method {@link #setTagletScopes()}
   * @return value of {@link #bInLine}
   */
  public boolean isInlineTag() {
    return bInLine;
  }

  /**
   * Register this Taglet.
   * 
   * @param tagletMap
   *          the map to register this taglet to.
   * @pre  tag != null
   * @post tagletMap.get(tag.getName()) == tag
   */
  public static void registerTaglet(Map tagletMap, Taglet tag) {
    Taglet t = (Taglet)tagletMap.get(tag.getName());
    if (t != null) {
      tagletMap.remove(tag.getName());
    }
    tagletMap.put(tag.getName(), tag);
  }

  /**
   * Given the <code>com.sun.javadoc.Tag</code> representation 
   * of this custom taglet, return its string representation.
   * 
   * @param taglet
   *          the <code>com.sun.javadoc.Tag</code> representation 
   * 						of this custom taglet.
   * 
   * @result null if taglet == null; valid html formatting code otherwise
   */
  public String toString(Tag taglet) {
    return toString(new Tag[] {taglet}); 
  }

  /**
   * Given an array of <code>com.sun.javadoc.Tag</code> s representing 
   * this custom taglet, return its string representation.
   * 
   * @param taglets
   *     			the array of <code>com.sun.javadoc.Tag</code> s representing 
   * 					of this custom taglet.
   * @result null if taglets == null; valid html formatting code otherwise
   */
  public String toString(Tag[] taglets) {
    if (taglets.length == 0) {
      return null;
    }
    StringBuffer result = new StringBuffer();
    result.append("\n<dl><dt><b>");//$NON-NLS-1$
    result.append(getHeader());
    result.append("</b></dt><dd>");//$NON-NLS-1$
    result.append("<table cellpadding=2 cellspacing=0>"); //$NON-NLS-1$
    for (int i = 0; i < taglets.length; i++) {
      result.append("<tr><td><code>"); //$NON-NLS-1$
      result.append(taglets[i].text());
      result.append("</code></td></tr>"); //$NON-NLS-1$
    }
    result.append("</table></dd></dl>\n"); //$NON-NLS-1$
    return result.toString();
  }
}