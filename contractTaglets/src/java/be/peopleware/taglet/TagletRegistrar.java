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
  protected boolean inField = false;

  /**
   * Used to determine if this taglet can be used in <strong>constructor
   * documentation </strong>. <br />
   * Default value is false.
   */
  protected boolean inConstructor = false;

  /**
   * Used to determine if this taglet can be used in <strong>method
   * documentation </strong>. <br />
   * Default value is false.
   */
  protected boolean inMethod = false;

  /**
   * Used to determine if this taglet can be used in <strong>overview
   * documentation </strong>. <br />
   * Default value is false.
   */
  protected boolean inOverview = false;

  /**
   * Used to determine if this taglet can be used in <strong>package
   * documentation </strong>. <br />
   * Default value is false.
   */
  protected boolean inPackage = false;

  /**
   * Used to determine if this taglet can be used in <strong>type documentation
   * </strong>. <br />
   * Default value is false.
   */
  protected boolean inType = false;

  /**
   * Used to determine if this taglet is an <strong>inline taglet </strong>.
   * <br />
   * Default value is false.
   */
  protected boolean inLine = false;

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
   * <br />inField 			= true;
   * <br />inConstructor = true;
   * <br />inMethod 			= true;
   * <br />inOverview 		= true;
   * <br />inPackage 		= true;
   * <br />inType 				= true;
   * </code><br /><br />
   *
   * By default, all variables are set to false.
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
   * @result    getName() != null && getName().getLength() > 0
   */
  public abstract String getName();

  /**
   * Used to determine if this taglet can be used in <strong>field documentation
   * </strong>. If you wish to override default behavior, which always returns
   * false, set the value of variable {@link #inField}to true in method
   * {@link #setTagletScopes()}
   *
   * @return    value of {@link #inField}
   */
  public boolean inField() {
    return inField;
  }

  /**
   * Used to determine if this taglet can be used in <strong>constructor
   * documentation </strong>. If you wish to override default behavior, which
   * always returns false, set the value of variable {@link #inConstructor}to
   * true in method {@link #setTagletScopes()}
   *
   * @return    value of {@link #inConstructor}
   */
  public boolean inConstructor() {
    return inConstructor;
  }

  /**
   * Used to determine if this taglet can be used in <strong>method
   * documentation </strong>. If you wish to override default behavior, which
   * always returns false, set the value of variable {@link #inMethod}to true
   * in method {@link #setTagletScopes()}
   *
   * @return    value of {@link #inMethod}
   */
  public boolean inMethod() {
    return inMethod;
  }

  /**
   * Used to determine if this taglet can be used in <strong>overview
   * documentation </strong>. If you wish to override default behavior, which
   * always returns false, set the value of variable {@link #inOverview}to
   * true in method {@link #setTagletScopes()}
   *
   * @return    value of {@link #inOverview}
   */
  public boolean inOverview() {
    return inOverview;
  }

  /**
   * Used to determine if this taglet can be used in <strong>package
   * documentation </strong>. If you wish to override default behavior, which
   * always returns false, set the value of variable {@link #inPackage}to true
   * in method {@link #setTagletScopes()}
   *
   * @return    value of {@link #inPackage}
   */
  public boolean inPackage() {
    return inPackage;
  }

  /**
   * Used to determine if this taglet can be used in <strong>type documentation
   * </strong>. If you wish to override default behavior, which always returns
   * false, set the value of variable {@link #inType}to true in method
   * {@link #setTagletScopes()}
   *
   * @return    value of {@link #inType}
   */
  public boolean inType() {
    return inType;
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
   * @result    (taglet != null) ? valid html formatting code
   *                             : null
   */
  public abstract String toString(Tag taglet);

  /**
   * Given an array of <code>com.sun.javadoc.Tag</code> s representing this
   * custom taglet, return its string representation.
   *
   * @param     taglets
   *            the array of <code>com.sun.javadoc.Tag</code> s representing of
   *            this custom taglet.
   * @result    (taglets != null) ? valid html formatting code
   *                              : null
   */
  public abstract String toString(Tag[] taglets);

}
