package be.peopleware.taglet.contract;


/**
 * Taglet-class for custom taglet <code>@pre</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class PreTaglet extends AbstractContractTaglet {

  public String getName() {
    return AbstractContractTaglet.TAGLET_PRE;
  }

  public String getHeader() {
    return "Pre Conditions:"; //$NON-NLS-1$
  }

  public PreTaglet() {
    $inField       = false;
    $inConstructor = true;
    $inMethod      = true;
    $inOverview    = false;
    $inPackage     = false;
    $inType        = false;
  }

}
