package be.peopleware.taglet.contract;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@result</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class ResultTaglet extends AbstractContractTaglet {
  /**
   * @see       Registrar#getName()
   */
  public String getName() {
    return "result"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Result:"; //$NON-NLS-1$
  }

  /**
   * @see       Registrar#setTagletScopes()
   */
  public ResultTaglet() {
    $inField       = false;
    $inConstructor = false;
    inMethod      = true;
    inOverview    = false;
    inPackage     = false;
    inType        = false;
  }

}
