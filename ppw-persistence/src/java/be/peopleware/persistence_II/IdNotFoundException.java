package be.peopleware.persistence_II;


/**
 * <p>This exception signals failure to locate a object with the given ID.</p>
 *
 * @invar     getId() != null;
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class IdNotFoundException extends IdException {

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
   *            The id for which we did not find a Object.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   * @param     beanClass
   *            The bean class which is involved in this Exception.
   *
   * @pre       (message != null) ==> ! message.equals("");
   * @pre       id != null;
   * @pre       beanClass != null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   * @post      new.getBeanClassName() == beanClassName;
   * @post      new.getId().equals(id);
   */
  public IdNotFoundException(final Long id,
                             final String message,
                             final Throwable cause,
                             final Class beanClass) {
    super(message, cause, beanClass);
    assert id != null;
    $id = id;
  }

  /*</construction;>*/



  /*<property name="id">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Long getId() {
    return $id;
  }

  /**
   * @invar     $id != null;
   */
  private Long $id;

  /*</property>*/

}
