package be.peopleware.taglet.contract;


/**
 * Taglet-class for custom taglet <code>@result</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class ResultTaglet extends AbstractContractTaglet {

  public String getName() {
    return AbstractContractTaglet.TAGLET_RESULT;
  }

  public String getHeader() {
    return "Result:"; //$NON-NLS-1$
  }

  public ResultTaglet() {
    $inField       = false;
    $inConstructor = false;
    $inMethod      = true;
    $inOverview    = false;
    $inPackage     = false;
    $inType        = false;
  }

}
