package be.peopleware.value_II;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A value type for gender.
 *
 * @author    David Van Keer
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar     VALUES != null;
 * @invar     VALUES.size() > 0;
 * @invar     ! VALUES.keySet().contains(null);
 * @invar     ! VALUES.values().contains(null);
 * @invar     (forall Object o; VALUES.keySet().contains(o);
 *                t instanceof String);
 * @invar     (forall Object o; VALUES.values().contains(o);
 *                o.getClass() == Gender.class);
 * @invar     VALUES.values().contains(this);
 * @invar     this.equals(VALUES.get(toString()));
 */
public final class Gender extends EnumerationValue {

  /*<section name="Meta Information">*/
  //  ------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  /**
   * The string that is used internally to identify males.
   *
   * <strong>= {@value}</strong>
   */
  public static final String MALE_STRING = "M"; //$NON-NLS-1$

  /**
   * The string that is used internally to identify females.
   *
   * <strong>= {@value}</strong>
   */
  public static final String FEMALE_STRING = "F"; //$NON-NLS-1$

  /**
   * The instance representing the male gender.
   */
  public static final Gender MALE   = new Gender(MALE_STRING);

  /**
   * The instance representing the female gender.
   */
  public static final Gender FEMALE = new Gender(FEMALE_STRING);

  public static final Map VALUES = generateValues();

  private static Map generateValues() {
    Map result = new HashMap();
    result.put(MALE.toString(), MALE);
    result.put(FEMALE.toString(), FEMALE);
    return Collections.unmodifiableMap(result);
  }

  /**
   * @param     discriminator
   *            The string that discriminates this value.
   * @pre       discriminator != null;
   * @pre       discriminator.length > 0;
   * @post      new.toString().equals(discriminator);
   */
  private Gender(final String discriminator) {
    super(discriminator);
  }

  /**
   * Enumeration types require a default constructor for
   * serializability. It is ill-advised to use this default
   * constructor yourself. Use the constants instead.
   *
   * @post    new.equals(MALE);
   */
  public Gender() {
    this(MALE_STRING);
  }

}
