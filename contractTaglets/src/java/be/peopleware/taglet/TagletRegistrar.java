package be.peopleware.taglet;


import java.util.Map;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;


/**
 * Generic Taglet that implements methods from interface Taglet.<br />
 * Custom taglet can extend this class and override method
 * {@link #setTagletScopes()}to define scopes where this taglet is valid.
 * <br />
 * In addition to implementing abstract methods defined here, each subclass
 * should also supply method <code>static void register(Map tagletMap)</code>.
 * For example, the following implementation for the class Foo should do the
 * trick: <code>
 * <dl>
 * <dt>public static void register(Map tagletMap) {</dt>
 * <dd>   TagletRegistrar.registerTaglet(tagletMap, new Foo());</dd>
 * <dt>}</dt>
 * <dl>
 * </code>
 *
 * @invar     getName() != null && getName().length() > 0
 *
 * @mudo (UnitTest): test taglet <code>@mudo</code>
 *
 * @idea (jand): use code generation tools to generate code for subclasses.
 *
 * @question (to david): maven properties for taglets
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public abstract class TagletRegistrar implements Taglet {
  /**
   * Used to determine if this taglet can be used in <strong>field documentation
   * </strong>. <br />
   * Default value is false.
   */
  protected boolean bInField = false;

  /**
   * Used to determine if this taglet can be used in <strong>constructor
   * documentation </strong>. <br />
   * Default value is false.
   */
  protected boolean bInConstructor = false;

  /**
   * Used to determine if this taglet can be used in <strong>method
   * documentation </strong>. <br />
   * Default value is false.
   */
  protected boolean bInMethod = false;

  /**
   * Used to determine if this taglet can be used in <strong>overview
   * documentation </strong>. <br />
   * Default value is false.
   */
  protected boolean bInOverview = false;

  /**
   * Used to determine if this taglet can be used in <strong>package
   * documentation </strong>. <br />
   * Default value is false.
   */
  protected boolean bInPackage = false;

  /**
   * Used to determine if this taglet can be used in <strong>type documentation
   * </strong>. <br />
   * Default value is false.
   */
  protected boolean bInType = false;

  /**
   * Used to determine if this taglet is an <strong>inline taglet </strong>.
   * <br />
   * Default value is false.
   */
  protected boolean bInLine = false;

  /**
   * Calls astract method <code>setTagScopes()</code> so that we are sure that
   * in superclass this method is called at least once.
   */
  protected TagletRegistrar() {
    setTagletScopes();
  }

  /**
   * Defines scopes where this taglet can be used. Setting certain boolean
   * variable to true enables usage in corresponding documentation scope. For
   * example, taglet <code>todo</code> would have following settings: <br />
   * <code>
   * <br />bInField 			= true;
   * <br />bInConstructor = true;
   * <br />bInMethod 			= true;
   * <br />bInOverview 		= true;
   * <br />bInPackage 		= true;
   * <br />bInType 				= true;
   * </code><br />
   * <br />
   * By default, all variables are set to false.
   *
   */
  protected abstract void setTagletScopes();

  /**
   * Makes some additional formatting of the content of the taglet.
   *
   * @param     text
   *            content of the taglet
   * @return    text - formatted content
   */
  public String parse(String text) {
    return text;
  }

  /**
   * Return the name of this custom taglet.
   *
   * @result    != null && result.getLength() > 0
   */
  public abstract String getName();

  /**
   * Used to determine if this taglet can be used in <strong>field documentation
   * </strong>. If you wish to override default behavior, which always returns
   * false, set the value of variable {@link #bInField}to true in method
   * {@link #setTagletScopes()}
   *
   * @return    value of {@link #bInField}
   */
  public boolean inField() {
    return bInField;
  }

  /**
   * Used to determine if this taglet can be used in <strong>constructor
   * documentation </strong>. If you wish to override default behavior, which
   * always returns false, set the value of variable {@link #bInConstructor}to
   * true in method {@link #setTagletScopes()}
   *
   * @return    value of {@link #bInConstructor}
   */
  public boolean inConstructor() {
    return bInConstructor;
  }

  /**
   * Used to determine if this taglet can be used in <strong>method
   * documentation </strong>. If you wish to override default behavior, which
   * always returns false, set the value of variable {@link #bInMethod}to true
   * in method {@link #setTagletScopes()}
   *
   * @return    value of {@link #bInMethod}
   */
  public boolean inMethod() {
    return bInMethod;
  }

  /**
   * Used to determine if this taglet can be used in <strong>overview
   * documentation </strong>. If you wish to override default behavior, which
   * always returns false, set the value of variable {@link #bInOverview}to
   * true in method {@link #setTagletScopes()}
   *
   * @return    value of {@link #bInOverview}
   */
  public boolean inOverview() {
    return bInOverview;
  }

  /**
   * Used to determine if this taglet can be used in <strong>package
   * documentation </strong>. If you wish to override default behavior, which
   * always returns false, set the value of variable {@link #bInPackage}to true
   * in method {@link #setTagletScopes()}
   *
   * @return    value of {@link #bInPackage}
   */
  public boolean inPackage() {
    return bInPackage;
  }

  /**
   * Used to determine if this taglet can be used in <strong>type documentation
   * </strong>. If you wish to override default behavior, which always returns
   * false, set the value of variable {@link #bInType}to true in method
   * {@link #setTagletScopes()}
   *
   * @return    value of {@link #bInType}
   */
  public boolean inType() {
    return bInType;
  }

  /**
   * Used to determine if this taglet can be used in <strong>inline
   * documentation </strong>. 
   */
  public abstract boolean isInlineTag();

  /**
   * Register this Taglet.
   *
   * @param     tagletMap
   *            the map to register this taglet to.
   * @pre       tag != null
   * @post      tagletMap.get(tag.getName()) == tag
   */
  public static final void registerTaglet(Map tagletMap, Taglet tag) {
    Taglet t = (Taglet)tagletMap.get(tag.getName());
    if (t != null) {
      tagletMap.remove(tag.getName());
    }
    tagletMap.put(tag.getName(), tag);
  }

  /**
   * Given the <code>com.sun.javadoc.Tag</code> representation of this custom
   * taglet, return its string representation.
   *
   * @param     taglet
   *            the <code>com.sun.javadoc.Tag</code> representation of this
   *            custom taglet.
   *
   * @result    null if taglet == null; valid html formatting code otherwise
   */
  public abstract String toString(Tag taglet);

  /**
   * Given an array of <code>com.sun.javadoc.Tag</code> s representing this
   * custom taglet, return its string representation.
   *
   * @param     taglets
   *            the array of <code>com.sun.javadoc.Tag</code> s representing of
   *            this custom taglet.
   * @result    null if taglets == null; valid html formatting code otherwise
   */
  public abstract String toString(Tag[] taglets);

}
