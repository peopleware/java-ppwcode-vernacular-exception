package be.peopleware.taglet.contract;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@post</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class PostTaglet extends AbstractContractTaglet {
  /**
   * @see       Registrar#getName()
   */
  public String getName() {
    return "post"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Post Conditions:"; //$NON-NLS-1$
  }

  /**
   * @see       Registrar#setTagletScopes()
   */
  public PostTaglet() {
    $inField       = false;
    $inConstructor = true;
    inMethod      = true;
    inOverview    = false;
    inPackage     = false;
    inType        = false;
  }

}
