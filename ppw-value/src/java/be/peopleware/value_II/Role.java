/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A value type for role.
 *
 * @author    David Van Keer
 * @author    Peopleware n.v.
 *
 * @invar     VALUES != null;
 * @invar     VALUES.size() > 0;
 * @invar     ! VALUES.keySet().contains(null);
 * @invar     ! VALUES.values().contains(null);
 * @invar     (forall Object o; VALUES.keySet().contains(o);
 *                t instanceof String);
 * @invar     (forall Object o; VALUES.values().contains(o);
 *                o.getClass() == Role.class);
 * @invar     VALUES.values().contains(this);
 * @invar     this.equals(VALUES.get(toString()));
 */
public class Role extends EnumerationValue {

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
   * The string that is used internally to identify a reader.
   *
   * <strong>= {@value}</strong>
   */
  public static final String READER_STRING = "RDR"; //$NON-NLS-1$

  /**
   * The string that is used internally to identify a editor.
   *
   * <strong>= {@value}</strong>
   */
  public static final String EDITOR_STRING = "EDI"; //$NON-NLS-1$

  /**
   * The string that is used internally to identify a reviewer.
   *
   * <strong>= {@value}</strong>
   */
  public static final String REVIEWER_STRING = "RVW"; //$NON-NLS-1$

  /**
   * The string that is used internally to identify a administrator.
   *
   * <strong>= {@value}</strong>
   */
  public static final String ADMINISTRATOR_STRING = "ADM"; //$NON-NLS-1$


  /**
   * The instance representing the reader role.
   */
  public static final Role READER = new Role(READER_STRING);

  /**
   * The instance representing the editor role.
   */
  public static final Role EDITOR = new Role(EDITOR_STRING);

  /**
   * The instance representing the reviewer role.
   */
  public static final Role REVIEWER = new Role(REVIEWER_STRING);

  /**
   * The instance representing the administrator role.
   */
  public static final Role ADMINISTRATOR = new Role(ADMINISTRATOR_STRING);

  /**
   * A map containing all possible values for this value type.
   */
  public static final Map VALUES = generateValues();

  private static Map generateValues() {
    Map result = new HashMap();
    result.put(READER.toString(), READER);
    result.put(EDITOR.toString(), EDITOR);
    result.put(REVIEWER.toString(), REVIEWER);
    result.put(ADMINISTRATOR.toString(), ADMINISTRATOR);
    return Collections.unmodifiableMap(result);
  }

  /**
   * @param     discriminator
   *            The string that discriminates this value.
   * @pre       discriminator != null;
   * @pre       discriminator.length > 0;
   * @post      new.toString().equals(discriminator);
   */
  private Role(final String discriminator) {
    super(discriminator);
  }

  /**
   * Enumeration types require a default constructor for
   * serializability. It is ill-advised to use this default
   * constructor yourself. Use the constants instead.
   *
   * @post    new.equals(READER);
   */
  public Role() {
    this(READER_STRING);
  }

}
