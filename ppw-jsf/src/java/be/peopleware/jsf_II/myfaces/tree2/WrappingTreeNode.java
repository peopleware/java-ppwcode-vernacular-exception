package be.peopleware.jsf_II.myfaces.tree2;


import java.util.List;

import org.apache.myfaces.custom.tree2.TreeNode;


/**
 * <p>An implementation of {@link TreeNode} that is intended as a wrapper around
 *   {@link #getWrapped() business objects}. The tree node type is the
 *   FQCN of the wrapped object.</p>
 * <p>The intention is for subclasses to implement {@link #getChildren()}
 *   in function of {@link #getWrapped()}.</p>
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 *
 * @invar getInstance() != null;
 */
public abstract class WrappingTreeNode implements TreeNode {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";
  /*</section>*/



//  private static final Log LOG = LogFactory.getLog(WrappingTreeNode.class);



  /**
   * After construction, {@link #getWrapped()} <code>== instance</code>.
   *
   * @param wrapped
   *        The business object this is a wrapper for.
   *        This cannot be <code>null</code>.
   * @post new.getWrapped() == wrapped;
   * @exception IllegalArgumentException
   *            <code>instance</code> is <code>null</code>.
   */
  protected WrappingTreeNode(Object wrapped)
      throws IllegalArgumentException {
    if (wrapped == null) {
      throw new IllegalArgumentException("instance cannot be null");
    }
    $wrapped = wrapped;
  }

  /**
   * The business object this is a wrapper for. This will never return
   * <code>null</code>.
   *
   * @basic
   */
  public final Object getWrapped() {
    return $wrapped;
  }

  /**
   * The business object this is a wrapper for.
   * This cannot be <code>null</code>.
   *
   * @invar $wrapped != null;
   */
  private final Object $wrapped;

  /**
   * @basic
   */
  public final boolean isLeaf() {
    return getChildCount() == 0;
  }

  public final void setLeaf(boolean l) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * As a default implementation, we offer the FQCN if
   * the wrapped business object. Subclasses can override this method.
   *
   * @return-protected getInstance().getClass().getName(); the FQCN of {@link #getWrapped()}.
   */
  public String getType() {
    return getWrapped().getClass().getName();
  }

  public final void setType(String t) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * As a default implementation, we offer the {@link List#size()}
   * of {link #getChildren()}. Subclasses can override this method.
   *
   * @return-protected getChildren().size();
   */
  public int getChildCount() {
    return getChildren().size();
  }

  public final String getDescription() {
    return $description;
  }

  public final void setDescription(String description) {
    $description = description;
  }

  private String $description;



  public final String getIdentifier() {
    return $identifier;
  }

  public final void setIdentifier(String identifier) {
    $identifier = identifier;
  }

  private String $identifier;

}

