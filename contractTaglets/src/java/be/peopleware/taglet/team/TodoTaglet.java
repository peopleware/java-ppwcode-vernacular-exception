package be.peopleware.taglet.team;


/**
 * Taglet-class for custom taglet <code>@todo</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class TodoTaglet extends AbstractTeamTaglet {

  public String getName() {
    return "todo"; //$NON-NLS-1$
  }


  public String getHeader() {
    return "Todo:"; //$NON-NLS-1$
  }


  public TodoTaglet() {
    $inField       = true;
    $inConstructor = true;
    $inMethod      = true;
    $inOverview    = true;
    $inPackage     = true;
    $inType        = true;
  }

}
