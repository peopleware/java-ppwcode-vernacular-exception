package be.peopleware.taglet.contract;


/**
 * Taglet-class for custom taglet <code>@invar</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class InvarTaglet extends AbstractContractTaglet {

  public String getName() {
    return AbstractContractTaglet.TAGLET_INVAR;
  }

  public String getHeader() {
    return "Type Invariants:"; //$NON-NLS-1$
  }


  public InvarTaglet() {
    $inField       = false;
    $inConstructor = false;
    $inMethod      = false;
    $inOverview    = false;
    $inPackage     = true;
    $inType        = true;
    
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_NEW);
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_FORALL);
  }

}
