package be.peopleware.taglet;


import java.util.Map;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;


/**
 * Generic Taglet that implements methods from interface Taglet.
 * <br />Other custom taglets can extend this class and override method 
 * {@link #setTagScopes()} to define scopes where this tag is valid.
 * <br />In addition to abstract methods defined in this class, 
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
 * </code>.
 * @todo. This tag can be used in any kind of {@link com.sun.javadoc.Doc}. It
 *        is not an inline tag. The text reminds the
 *        developer to perform a task. 
 * 				<br />For example, "@todo Fix this!" would be
 *        shown as:
 *        <dl>
 * 					<dt><b>To Do: </b></dt>
 * 					<dd>
 *        		<table cellpadding=2 cellspacing=0>
 *        			<tr><td><code>Fix this!</code></td></tr>
 *        		</table>
 * 					</dd>
 * 				</dl>
 * 
 * @author      Jan Dockx
 */
public abstract class TagletRegistrar implements Taglet {

  /**
   * Used to determine if this tag can be used in <strong>field</strong> documentation.
   * <br />Default value is false.
   */
  protected boolean bInField = false;
  /**
   * Used to determine if this tag can be used in <strong>constructor</strong> documentation.
   * <br />Default value is false.
   */
  protected boolean bInConstructor = false;

  /**
   * Used to determine if this tag can be used in <strong>method</strong> documentation.
   * <br />Default value is false.
   */
  protected boolean bInMethod = false;

  /**
   * Used to determine if this tag can be used in <strong>overview</strong> documentation.
   * <br />Default value is false.
   */
  protected boolean bInOverview = false;

  /**
   * Used to determine if this tag can be used in <strong>package</strong> documentation.
   * <br />Default value is false.
   */
  protected boolean bInPackage = false;

  /**
   * Used to determine if this tag can be used in <strong>type</strong> documentation.
   * <br />Default value is false.
   */
  protected boolean bInType = false;

  /**
   * Used to determine if this tag is an <strong>inline</strong> tag
   * <br />Default value is false.
   */
  protected boolean bInLine = false;

  /**
   * Calls astract method <code>setTagScopes()</code>
   * so that we are sure that in superclass this method is called at least once. 
   */
  protected TagletRegistrar() {
  	setTagScopes();
  }
  
  /**
   * Defines scopes where this tag can be used by setting boolean variables.
   * For example, tag <code>todo</code> would have following settings:
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
  protected abstract void setTagScopes();
  
  /** 
   * Makes some additional formatting of the content of the tag.
   * 
   * @param text content of the tag
   * @return text - formatted content
   */
  public String parse(String text) {
  	return text;
  }
  
  /**
   * Return the name of this custom tag.
   */
  public abstract String getName();

  /**
   * Return the header of this custom tag - used in generated documentation.
   */
  public abstract String getHeader();

  /**
   * Used to determine if this tag can be used in <strong>field</strong> documentation.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInField} to true
   * in method {@link #setTagScopes()}
   * @return value of {@link #bInField}
   */
  public boolean inField() {
    return bInField;
  }

  /**
   * Used to determine if this tag can be used in <strong>constructor</strong> documentation.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInConstructor} to true
   * in method {@link #setTagScopes()}
   * @return value of {@link #bInConstructor}
   */
  public boolean inConstructor() {
    return bInConstructor;
  }

  /**
   * Used to determine if this tag can be used in <strong>method</strong> documentation.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInMethod} to true
   * in method {@link #setTagScopes()}
   * @return value of {@link #bInMethod}
   */
  public boolean inMethod() {
    return bInMethod;
  }

  /**
   * Used to determine if this tag can be used in <strong>overview</strong> documentation.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInOverview} to true
   * in method {@link #setTagScopes()}
   * @return value of {@link #bInOverview}
   */
  public boolean inOverview() {
    return bInOverview;
  }

  /**
   * Used to determine if this tag can be used in <strong>package</strong> documentation.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInPackage} to true
   * in method {@link #setTagScopes()}
   * @return value of {@link #bInPackage}
   */
  public boolean inPackage() {
    return bInPackage;
  }

  /**
   * Used to determine if this tag can be used in <strong>type</strong> documentation.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInType} to true
   * in method {@link #setTagScopes()}
   * @return value of {@link #bInType}
   */
  public boolean inType() {
    return bInType;
  }

  /**
   * Used to determine if this tag can be used in <strong>inline</strong> documentation.
   * If you wish to override default behavior, which always returns false,
   * set the value of variable {@link #bInLine} to true
   * in method {@link #setTagScopes()}
   * @return value of {@link #bInLine}
   */
  public boolean isInlineTag() {
    return bInLine;
  }

  /**
   * Register this Taglet.
   * 
   * @param tagletMap
   *          the map to register this tag to.
   */
  public static void registerTaglet(Map tagletMap, Taglet tag) {
//    TagletRegistrar tag = new TagletRegistrar();
    Taglet t = (Taglet)tagletMap.get(tag.getName());
    if (t != null) {
      tagletMap.remove(tag.getName());
    }
    tagletMap.put(tag.getName(), tag);
  }

  /**
   * Given the <code>Tag</code> representation of this custom tag, return its
   * string representation.
   * 
   * @param tag
   *          the <code>Tag</code> representation of this custom tag.
   */
  public String toString(Tag tag) {
    return toString(new Tag[] {tag}); 
  }

  /**
   * Given an array of <code>Tag</code> s representing this custom tag, return
   * its string representation.
   * 
   * @param tags
   *          the array of <code>Tag</code> s representing of this custom tag.
   */
  public String toString(Tag[] tags) {
    if (tags.length == 0) {
      return null;
    }
    String result = "\n<dl><dt><b>" + getHeader() + "</b></dt><dd>"; //$NON-NLS-1$ //$NON-NLS-2$
    result += "<table cellpadding=2 cellspacing=0>"; //$NON-NLS-1$
    for (int i = 0; i < tags.length; i++) {
      result += "<tr><td><code>"; //$NON-NLS-1$
      result += tags[i].text();
      result += "</code></td></tr>"; //$NON-NLS-1$
    }
    return result + "</table></dd></dl>\n"; //$NON-NLS-1$
  }
}