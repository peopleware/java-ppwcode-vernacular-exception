package be.peopleware.taglet.team;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@idea</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class IdeaTaglet extends AbstractTeamTaglet {
  /**
   * @see       AbstractTaglet#getName()
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
   * @see       AbstractTaglet#AbstractTaglet()
   */
  public IdeaTaglet() {
    $inField       = true;
    $inConstructor = true;
    $inMethod      = true;
    $inOverview    = true;
    $inPackage     = true;
    $inType        = true;
  }

}
