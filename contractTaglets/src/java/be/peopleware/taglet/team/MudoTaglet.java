package be.peopleware.taglet.team;


/**
 * Taglet-class for custom taglet <code>@mudo</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class MudoTaglet extends AbstractTeamTaglet {

  public String getName() {
    return "mudo"; //$NON-NLS-1$
  }


  public String getHeader() {
    return "Must do:"; //$NON-NLS-1$
  }


  public MudoTaglet() {
    $inField       = true;
    $inConstructor = true;
    $inMethod      = true;
    $inOverview    = true;
    $inPackage     = true;
    $inType        = true;
  }

}
