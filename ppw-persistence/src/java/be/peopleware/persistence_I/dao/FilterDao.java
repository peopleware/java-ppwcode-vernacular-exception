package be.peopleware.persistence_I.dao;

import java.util.Map;
import java.util.Set;

import be.peopleware.exception_I.TechnicalException;


/**
 * <p>An {@link AsyncCrudDao} that contains an extra method for retrieving
 *   persistent beans of a given type that have properties with given values.
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
   * Retrieve all persistent beans with the given type that have properties
   * with given values.
   *
   * @param   type
   *          The type of the persistent beans to retrieve.
   * @param   criteriaMap
   *          A map containing propertyName-value pairs.
   * @pre     type != null;
   * @pre     criteriaMap != null;
   * @pre     (forall Object key;
   *                   criteriaMap.keySet().contains(key);
   *                   key instanceof String);
   * @pre     (forall Object key;
   *                   criteriaMap.keySet().contains(key);
   *                   ** key is a property of the given type ** );
   * @mudo  (nsmeets) formal specification for the preceding precondition
   * @result  result != null
   * @result  (forall Object bean;
   *                   result.contains(bean);
   *                   bean.isInstance(type) &&
   *                   ( forall Object key;
   *                         criteriaMap.keySet().contains(key);
   *                         the property "key" of the bean has value criteriaMap.get(key)
   *                   )
   *          );
   * @throws TechnicalException
   */
  Set retrievePersistentBeans(final Class type, final Map criteriaMap)
      throws TechnicalException;


}

