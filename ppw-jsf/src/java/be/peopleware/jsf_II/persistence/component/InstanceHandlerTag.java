/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.persistence.component;




/**
 * Tag for {@link UIInstanceHandler}.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public class InstanceHandlerTag extends ViewModeHandlerTag {

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


  //private static final Log LOG = LogFactory.getLog(InstanceHandlerTag.class);

  /**
   * Return the component type for the component that is or will be bound
   * to this tag.
   *
   * @return  InstanceHandlerTag.class.getName();
   */
  public String getComponentType() {
    return InstanceHandlerTag.class.getName();
  }
}
