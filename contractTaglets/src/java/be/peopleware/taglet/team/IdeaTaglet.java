package be.peopleware.taglet.team;


/**
 * Taglet-class for custom taglet <code>@idea</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class IdeaTaglet extends AbstractTeamTaglet {

  public String getName() {
    return "idea"; //$NON-NLS-1$
  }

  public String getHeader() {
    return "Idea:"; //$NON-NLS-1$
  }

  public IdeaTaglet() {
    $inField       = true;
    $inConstructor = true;
    $inMethod      = true;
    $inOverview    = true;
    $inPackage     = true;
    $inType        = true;
  }

}
