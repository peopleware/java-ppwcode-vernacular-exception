/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.dao;

import java.util.List;
import java.util.Set;

import be.peopleware.exception_I.TechnicalException;


/**
 * <p>An {@link AsyncCrudDao} that contains an extra method for retrieving
 *   persistent beans of a given type that satisfy certain criteria.
 * </p>
 *
 * @author    nsmeets
 * @author    ashoudou
 * @author    Peopleware n.v.
 */
public interface FilterDao extends AsyncCrudDao {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  /**
   * Retrieve all persistent beans with the given type that satisfy the given
   * criteria.
   *
   * @param   type
   *          The type of the persistent beans to retrieve.
   * @param   criteriaList
   *          A list of criteria.
   * @pre     type != null;
   * @pre     criteriaList != null;
   * @pre     (forall Object criterion;
   *                   criteriaList.contains(criterion);
   *                   criterion instanceof FilterCriterion);
   * @result  result != null
   * @result  (forall Object bean;
   *                   result.contains(bean);
   *                   type.isInstance(bean) &&
   *                   ( forall Object criterion;
   *                         criteriaList.contains(criterion);
   *                         the bean satisfies the criterion
   *                   )
   *          );
   * @throws TechnicalException
   *         true;
   */
  Set retrievePersistentBeans(final Class type, final List criteriaList)
      throws TechnicalException;


}

