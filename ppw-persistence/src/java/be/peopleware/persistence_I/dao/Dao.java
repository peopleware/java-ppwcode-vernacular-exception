/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_I.dao;


/**
 * Data Access Object. This interface is mainly used for documentation
 * purposes, to flag a type as a <acronym title="Data Access Object">DAO</acronym>.
 * In projects, interfaces should be defined that extend this interface,
 * with methods that have a technology independent contract that describes
 * interaction with persistent storage. The actual implementation of these
 * DAO methods will depend on the persistence technology used: different
 * classes that implement the DAO interface will produce the desired result
 * in different technologies. Those classes can extend a technology
 * specific superclass that offers support for that technology (e.g.,
 * JDBC, Hibernate, JDO, EJB, RMI, &hellip;).
 * 
 * DAO instances are almost always stateful, because of the underlying
 * persistence technology.
 * 
 * Implementations should be JavaBeans,
 * with a default constructor. Further dependencies should be filled
 * out using setters, and DAO methods should be allowed to throw a
 * {@link be.peopleware.exception_I.TechnicalException} if the depencies
 * are not fulfilled when the DAO method is called.
 * 
 * Subtypes may dependent on the fact that the objects in persistent storage are
 * {@link be.peopleware.persistence_I.PersistentBean PersistentBeans},
 * although this will not always be necessary.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public interface Dao {

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

}
