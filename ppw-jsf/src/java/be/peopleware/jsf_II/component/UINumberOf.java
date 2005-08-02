package be.peopleware.jsf_II.component;


import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;


/**
 * This {@link UIComponent} is used in JSF pages to count the number of elements of a Collection. 
 * The collection is given as a value of the attribute <code>collection</code>
 *
 * @author    Jan Dockx
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 *
 */
public class UINumberOf extends UIOutput {

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


  private static final Log LOG = LogFactory.getLog(UINumberOf.class);



  /*<property name="collection">*/
  //------------------------------------------------------------------

  /**
   * <strong>= {@value}</strong>
   */
  public final static String COLLECTION_VALUE_BINDING_NAME = "value";

  /**
   * Return the result of {@link #COLLECTION_VALUE_BINDING_NAME} value binding.
   *
   * @return result != null;
   * @throws FatalFacesException
   *         ; could not locate a collection through the value binding
   */
  public Collection getValue(FacesContext context)
      throws FatalFacesException {
    ValueBinding vb = getValueBinding(COLLECTION_VALUE_BINDING_NAME);
    if (vb == null) {
      RobustCurrent.fatalProblem("Could not locate collection", LOG);
    }
    Object result = vb.getValue(context);
    if ((result == null) ||
        (! (result instanceof java.util.Collection))) {
      RobustCurrent.fatalProblem("Could not locate collection", LOG);
    }
    return (Collection)result;
  }

  /*</property>*/





  /*<section name="rendering">*/
  //------------------------------------------------------------------

  /**
   * Writes to the output stream the size of the collection.
   *
   * @throws FatalFacesException
   *         getValue();
   */
  public void encodeBegin(FacesContext context) throws IOException, FatalFacesException {
    ResponseWriter writer = context.getResponseWriter();
    writer.write(format(getValue(context).size()));
  }
  public void encodeEnd(FacesContext context) throws IOException, FatalFacesException {
    // NOP
  }

  private NumberFormat getNumberFormat() {
    Locale loc = RobustCurrent.locale();
    return NumberFormat.getIntegerInstance(loc);
  }

  private final static String ZERO_REPLACEMENT = "-";
  
  /**
   * Convenience method to format a number.
   */
  private String format(final int integer) {
    String result = getNumberFormat().format(integer);
    if (result.equals("0")) { //$NON-NLS-1$
      return ZERO_REPLACEMENT;
    }
    else {
      return result;
    }
  }
  
  /*</rendering>*/
  
}
