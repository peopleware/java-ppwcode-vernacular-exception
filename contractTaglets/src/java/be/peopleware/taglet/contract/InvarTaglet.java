package be.peopleware.taglet.contract;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@invar</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class InvarTaglet extends AbstractContractTaglet {
  /**
   * @see       Registrar#getName()
   */
  public String getName() {
    return "invar"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Type Invariants:"; //$NON-NLS-1$
  }

  /**
   * @see       Registrar#setTagletScopes()
   */
  public InvarTaglet() {
    $inField       = false;
    $inConstructor = false;
    inMethod      = false;
    inOverview    = false;
    inPackage     = true;
    inType        = true;
  }

}
