package be.peopleware.taglet.inline;


import com.sun.javadoc.Tag;
import be.peopleware.taglet.AbstractInlineTaglet;


/**
 * Taglet-class for custom inline taglet <code>@underline</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class UnderlineTaglet extends AbstractInlineTaglet {
  /**
   * @see       AbstractTaglet#getName()
   */
  public String getName() {
    return "underline"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractTaglet#AbstractTaglet()
   * 
   * @mudo cleanup
   */
  public UnderlineTaglet() {
     $inField       = true;
     $inConstructor = true;
     $inMethod      = true;
     $inOverview    = true;
     $inPackage     = true;
     $inType        = true;
     $inLine        = true;
  }

  /**
   * @todo (dvankeer0: This needs to be generalized.
   */
  public String toString(Tag tag) {
      return "<u>" + tag.text() + "</u>";  //$NON-NLS-1$//$NON-NLS-2$
  }

}
