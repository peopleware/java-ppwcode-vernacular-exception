package be.peopleware.taglet.contract;


/**
 * Taglet-class for custom taglet <code>@invar</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class InvarTaglet extends AbstractContractTaglet {

  public String getName() {
    return "invar"; //$NON-NLS-1$
  }

  public String getHeader() {
    return "Type Invariants:"; //$NON-NLS-1$
  }

  public InvarTaglet() {
    $inPackage     = true;
    $inType        = true;
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_NEW);
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_FORALL);
  }

}
