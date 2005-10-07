/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II;


/**
 * <p>This exception signals failure to transform the given String into
 *   an id.</p>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class IdFormatException extends IdException {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/




  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     id
   *            The String we could not transform into an id.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   * @param     beanClass
   *            The bean class which is involved in this Exception.
   *
   * @pre       (message != null) ==> ! message.equals("");
   * @pre       beanClass != null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   * @post      new.getBeanClassName() == beanClassName;
   * @post      (id != null) ==> new.getId().equals(id);
   * @post      (id == null) ==> new.getId() == null;
   */
  public IdFormatException(final String id,
                           final String message,
                           final Throwable cause,
                           final Class beanClass) {
    super(message, cause, beanClass);
    $id = id;
  }

  /*</construction;>*/



  /*<property name="id">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final String getId() {
    return $id;
  }

  private String $id;

  /*</property>*/

}
