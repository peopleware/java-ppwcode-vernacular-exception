package be.peopleware.taglet.contract;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@pre</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class PreTaglet extends AbstractContractTaglet {
  /**
   * @see       Registrar#getName()
   */
  public String getName() {
    return "pre"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Pre Conditions:"; //$NON-NLS-1$
  }

  /**
   * @see       Registrar#setTagletScopes()
   */
  public PreTaglet() {
    $inField       = false;
    $inConstructor = true;
    inMethod      = true;
    inOverview    = false;
    inPackage     = false;
    inType        = false;
  }

}
