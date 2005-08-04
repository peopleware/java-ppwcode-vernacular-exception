package be.peopleware.jsf_II.convert;


import java.util.Map;

import be.peopleware.value_I.Country;


/**
 * Converter for {@link Country}.
 * The value must be the string representation of the class.
 *
 * To activate this converter, the following entry has to appear in <kbd>faces-config.xml</kbd>:
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;be.peopleware.value_I.Country&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.jsf_II.convert.CountryConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 *
 * @author nsmeets
 * @author ashoudou
 * @author Peopleware n.v.
 */
public class CountryConverter extends EnumerationValueConverter {

  public Map getValuesMap() {
    return Country.VALUES;
  }

  public Class getType() {
    return Country.class;
  }

}

