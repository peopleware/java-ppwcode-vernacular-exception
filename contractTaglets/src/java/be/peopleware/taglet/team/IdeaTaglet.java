package be.peopleware.taglet.team;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@idea</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class IdeaTaglet extends AbstractStandaloneTaglet {
  /**
   * @see       Registrar#getName()
   */
  public String getName() {
    return "idea"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Idea:"; //$NON-NLS-1$
  }

  /**
   * @see       Registrar#setTagletScopes()
   */
  public IdeaTaglet() {
    $inField       = true;
    $inConstructor = true;
    inMethod      = true;
    inOverview    = true;
    inPackage     = true;
    inType        = true;
  }

}
