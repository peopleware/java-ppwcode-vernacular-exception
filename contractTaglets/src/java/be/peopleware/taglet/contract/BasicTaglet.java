package be.peopleware.taglet.contract;

import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@result</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class BasicTaglet extends AbstractStandaloneTaglet {

  public String getName() {
    return "basic"; //$NON-NLS-1$
  }

  public BasicTaglet() {
    $inMethod      = true;
  }

  /**
   * @return "Basic Inspector";
   * 
   * @mudo add some emphasis, a color or something
   */
  public final String getHeader() {
    return "Basic Inspector"; //$NON-NLS-1$
  }

  /**
   * @return EMPTY;
   * 
   * @mudo this is a small problem: here we do not have a contents of the tag;
   *       but the {@link toString(Tag[])} method calling this has already
   *       started a table and a row and a cell, which will remain empty,
   *       but will create a lot of whitespace in the resulting HTML page;
   *       further, there can only be 1 @basic tag in a documentation block 
   */
  public String parse(String text) {
    if (text.length() > 0) {
      System.err.println("There should be no text with a @basic tag."); //$NON-NLS-1$
      // MUDO add source location to err message
    }
    return EMPTY;
  }

}
