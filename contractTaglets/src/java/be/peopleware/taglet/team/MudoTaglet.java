package be.peopleware.taglet.team;


import be.peopleware.taglet.AbstractStandaloneTaglet;
import be.peopleware.taglet.TagletRegistration;


/**
 * Taglet-class for custom taglet <code>@mudo</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class MudoTaglet extends AbstractStandaloneTaglet {
  /**
   * @see       Registrar#getName()
   */
  public String getName() {
    return "mudo"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Must do:"; //$NON-NLS-1$
  }

  /**
   * @see       Registrar#setTagletScopes()
   */
  public MudoTaglet() {
    $inField       = true;
    $inConstructor = true;
    inMethod      = true;
    inOverview    = true;
    inPackage     = true;
    inType        = true;
  }

}
