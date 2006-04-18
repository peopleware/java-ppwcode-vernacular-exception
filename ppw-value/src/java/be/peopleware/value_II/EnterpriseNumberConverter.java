/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.bean_V.PropertyException;


/**
 * Converter for {@link EnterpriseNumber}. The {@link #getAsObject(FacesContext, UIComponent, String)}
 * method is very lenient: separators can be left out, or any of the characters
 * <code><var>space</var>-/|*:</code>.
 *
 * To activate this converter, the following entry has to appear in <kbd>faces-config.xml</kbd>:
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;be.peopleware.value_II.EnterpriseNumber&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.value_II.EnterpriseNumberConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public class EnterpriseNumberConverter implements Converter {

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



  private static final Log LOG = LogFactory.getLog(EnterpriseNumberConverter.class);



  /**
   * Convert the specified string value, which is associated with the
   * specified UIComponent, into a model data object that is appropriate for
   * being stored during the Apply Request Values phase of the request
   * processing lifecycle.
   *
   * @result  value == null || value.length == 0
   *            ==> result == null;
   * @result  result == a new Enterprise number created using the given string;
   * @throws  ConverterException
   *          The Enterprise number could not be created from the given string.
   * @todo  formal comment
   */
  public Object getAsObject(final FacesContext context,
                            final UIComponent component,
                            final String value) throws ConverterException {
    if ((value == null) || (value.length() == 0)) {
      return null;
    }
    else {
      try {
        return new EnterpriseNumber(value);
      }
      catch (PropertyException e) {
        LOG.debug("Enterprise number " + value + "could not be created", e);
        throw new ConverterException("Enterprise number " + value + "could not be created");
      }
    }
  }

  /**
   * The empty string.
   *
   * <strong>= &quot;&quot;</strong>
   */
  public static final String EMPTY = "";

  /**
   * Convert the specified model object value, which is associated with the
   * specified UIComponent, into a String that is suitable for being included
   * in the response generated during the Render Response phase of the request
   * processing lifecycle.
   *
   * @result  value == null
   *            ==> result == EMPTY;
   * @result  value != null && value instanceof EnterpriseNumber
   *            ==> result == ((EnterpriseNumber)value).toString();
   * @throws  ConverterException
   *          value != null && !(value instanceof EnterpriseNumber)
   */
  public String getAsString(final FacesContext context,
                            final UIComponent component,
                            final Object value) throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (! (value instanceof EnterpriseNumber)) {
      throw new ConverterException("value (" + value + ") is not a EnterpriseNumber");
    }
    return ((EnterpriseNumber)value).toString();
  }
}

