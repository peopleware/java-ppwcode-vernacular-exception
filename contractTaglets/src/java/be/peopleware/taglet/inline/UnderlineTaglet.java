package be.peopleware.taglet.inline;


import java.util.Map;
import com.sun.javadoc.Tag;
import be.peopleware.taglet.AbstractInlineTaglet;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom inline taglet <code>@underline</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class UnderlineTaglet extends AbstractInlineTaglet {
  /**
   * @see       TagletRegistrar#getName()
   */
  public String getName() {
    return "underline"; //$NON-NLS-1$
  }

  /**
   * Register this taglet.
   *
   * @param     tagletMap
   *            the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new UnderlineTaglet());
  }

  /**
   * @see       TagletRegistrar#setTagletScopes()
   */
  protected void setTagletScopes() {
     inField       = true;
     inConstructor = true;
     inMethod      = true;
     inOverview    = true;
     inPackage     = true;
     inType        = true;
     inLine        = true;
  }

  /**
   * @todo (dvankeer0: This needs to be generalized.
   */
  public String toString(Tag tag) {
      return "<u>" + tag.text() + "</u>";  //$NON-NLS-1$//$NON-NLS-2$
  }

}
