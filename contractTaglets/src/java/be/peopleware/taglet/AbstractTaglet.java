package be.peopleware.taglet;


import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;


/**
 * <p>Generic Taglet that implements methods from interface {@link Taglet}.
 * <br /> Custom taglets can extend this class and define scopes 
 * where they are valid.</p>
 *
 * @invar     getName() != null && getName().length() > 0;
 * 
 * @idea tags below are not real, but are here to demonstrate the
 *       taglets in this package themselves.
 *
 * @mudo (UnitTest): demo must do: test 
 *
 * @idea (jand): demo idea: use code generation tools to generate code for subclasses.
 *
 * @question (to david): demo question maven properties for taglets
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 * 
 *
 * @invar getName() != null;
 * @invar getName().getLength() > 0;
 */
public abstract class AbstractTaglet implements Taglet {
  
  /**
   * Used to determine if this taglet can be used in 
   * <strong>field documentation</strong>.
   * <br />Default value is <code>false</code>.
   */
  protected boolean $inField = false;

  /**
   * Used to determine if this taglet can be used in 
   * <strong>constructor documentation</strong>.
   * <br />Default value is <code>false</code>.
   */
  protected boolean $inConstructor = false;

  /**
   * Used to determine if this taglet can be used in 
   * <strong>method documentation</strong>.
   * <br />Default value is <code>false</code>.
   */
  protected boolean $inMethod = false;

  /**
   * Used to determine if this taglet can be used in 
   * <strong>overview documentation</strong>.
   * <br />Default value is <code>false</code>.
   */
  protected boolean $inOverview = false;

  /**
   * Used to determine if this taglet can be used in 
   * <strong>package documentation</strong>.
   * <br />Default value is <code>false</code>.
   */
  protected boolean $inPackage = false;

  /**
   * Used to determine if this taglet can be used in 
   * <strong>type documentation</strong>.
   * <br />Default value is <code>false</code>.
   */
  protected boolean $inType = false;

  /**
   * Used to determine if this taglet is an 
   * <strong>inline taglet</strong>.
   * <br />Default value is <code>false</code>.
   */
  protected boolean $inLine = false;

  /**
   * <p>Use a (default) constructor in your subclasses to define scopes 
   * where this taglet can be used. Setting certain boolean
   * variable to true enables usage in corresponding documentation scope. 
   * E.g.:</p>
   * <pre>
   *   $inField       = true;
   *   $inConstructor = true;
   *   $inMethod      = true;
   *   $inOverview    = true;
   *   $inPackage     = true;
   *   $inType        = true;
   * </pre>
   * <p>By default, all variables are set to <code>false</code>.</p>
   * 
   * @post new.inField() 			 == false;
   * @post new.inConstructor() == false;
   * @post new.inMethod()      == false;
   * @post new.inOverview()    == false;
   * @post new.inPackage()     == false;
   * @post new.inType()        == false;
   * 
   */
  protected AbstractTaglet() {
    // NOP
  }

  /**
   * Makes some additional formatting of the content of the taglet.
   *
   * @param     text
   *            content of the taglet
   * @pre       text != null;
   */
  public abstract String parse(String text);

  /**
   * Return the name of this custom taglet.
   * 
   * @basic
   */
  public abstract String getName();

  /**
   * <p>Used to determine if this taglet can be used in 
   * <strong>field documentation</strong>.</p> 
   * 
   * @protected
   * <p>If you wish to override default behavior, which always
   *  returns <code>false</code>, set the value of variable
   *  {@link #$inField} to true in the constructor of your class.</p>
   * 
   * @basic
   */
  public final boolean inField() {
    return $inField;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>constructor documentation </strong>. 
   * 
   * @protected
   * <p>If you wish to override default behavior, which always
   *  returns <code>false</code>, set the value of variable
   *  {@link #$inConstructor} to true in the constructor of your class.</p>
   * 
   * @basic
   */
  public final boolean inConstructor() {
    return $inConstructor;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>method documentation</strong>. 
   * 
   * @protected
   * <p>If you wish to override default behavior, which always
   *  returns <code>false</code>, set the value of variable
   *  {@link #$inMethod} to true in the constructor of your class.</p>
   * 
   * @basic
   */
  public final boolean inMethod() {
    return $inMethod;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>overview documentation</strong>. 
   * 
   * @protected
   * <p>If you wish to override default behavior, which always
   *  returns <code>false</code>, set the value of variable
   *  {@link #$inOverview} to true in the constructor of your class.</p>
   * 
   * @basic
   */
  public final boolean inOverview() {
    return $inOverview;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>package documentation</strong>. 
   * 
   * @protected
   * <p>If you wish to override default behavior, which always
   *  returns <code>false</code>, set the value of variable
   *  {@link #$inPackage} to true in the constructor of your class.</p>
   * 
   * @basic
   */
  public final boolean inPackage() {
    return $inPackage;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>type documentation</strong>. 
   * 
   * @protected
   * <p>If you wish to override default behavior, which always
   *  returns <code>false</code>, set the value of variable
   *  {@link #$inType} to true in the constructor of your class.</p>
   * 
   * @basic
   */
  public final boolean inType() {
    return $inType;
  }

  /**
   * Used to determine if this taglet can be used in 
   * <strong>inline documentation </strong>.
   * 
   * @basic
   */
  public abstract boolean isInlineTag();

  /**
   * Given the {@link Tag} representation of this custom
   * taglet, return its string representation.
   * This method is called by the standard doclet if this is the
   * last tag of this type in the documentation block, or if
   * the next tag is of a different type.
   * 
   * @see #toString(Tag[])
   *
   * @param     taglet
   *            the <code>com.sun.javadoc.Tag</code> representation of this
   *            custom taglet.
   * @pre taglet != null;
   */
  public abstract String toString(Tag taglet);

  /**
   * Given an array of {@link Tag}s representing this
   * custom taglet, return its string representation.
   * This method is called by the standard doclet if there
   * are more then 1 consecutive tags of the same type.
   * 
   * @see #toString(Tag)
   *
   * @param     taglets
   *            the array of <code>com.sun.javadoc.Tag</code> s representing of
   *            this custom taglet.
   * @pre taglets != null
   * @pre (forall int i; (i >= 0) && (i < taglets.length);
   *            taglets[i] != null);
   */
  public abstract String toString(Tag[] taglets);

}
