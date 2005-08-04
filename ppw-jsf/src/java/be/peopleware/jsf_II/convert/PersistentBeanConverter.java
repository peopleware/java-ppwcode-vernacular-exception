/*
 * <license> Copyright 2004-2005, PeopleWare n.v. NO RIGHTS ARE GRANTED FOR THE
 * USE OF THIS SOFTWARE, EXCEPT, IN WRITING, TO SELECTED PARTIES. </license>
 */
package be.peopleware.jsf_II.convert;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import net.sf.hibernate.Session;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.DaoFactory;
import be.peopleware.persistence_I.IdNotFoundException;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.hibernate.HibernateAsyncCrudDao;
import be.peopleware.persistence_I.hibernate.HibernateDaoFactory;
import be.peopleware.servlet_I.hibernate.SessionInView;


/**
 * Converter for {@link PersistentBean}. The value must be the ID of
 * the class. To activate this, the following entry has to appear in
 * <kbd>faces-config.xml </kbd>:
 * 
 * <pre>
 * 
 *  &lt;converter&gt;
 *    &lt;converter-for-class&gt;be.peopleware.persistence_I.PersistentBean&lt;/converter-for-class&gt;
 *    &lt;converter-class&gt;be.peopleware.jsf_II.convertor.ResponsibilyDelegatorConverter&lt;/converter-class&gt;
 *  &lt;/converter&gt;
 *  
 * </pre>
 * 
 * @author David Van Keer
 * @author Peopleware n.v.
 */
public class PersistentBeanConverter implements Converter {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$
  /* </section> */

  /**
   * @return    (value != null)
   *              ? TODO...
   *              : null;
   * @throws    ConverterException
   *            TODO
   */
  public Object getAsObject(final FacesContext context,
                            final UIComponent component,
                            final String value)
      throws ConverterException {
    Object result = null;
    if (value != null && !value.equals(EMPTY)) {
      Long id = Long.valueOf(value);
      // Retrieve PersistentBean based on the ID found and return it.
      HibernateDaoFactory daoFactory = new HibernateDaoFactory();
      HibernateAsyncCrudDao dao = (HibernateAsyncCrudDao)daoFactory.getDao(DaoFactory.ASYNC_CRUD);
      try {
        Session session = SessionInView.getSession(RobustCurrent.httpServletRequest());
        dao.setSession(session);
        result = dao.retrievePersistentBean(id, PersistentBean.class);
      }
      catch (IdNotFoundException infExc) {
        throw new ConverterException("No PersistentBean bean found with id " + id);
      }
      catch (TechnicalException tExc) {
        throw new ConverterException("Unable to retrieve PersistentBean.");
      }
    }
    return result;
  }

  public final static String EMPTY = "";

  /**
   * @return    (value != null)
   *                ? (((PersistentBean)value).getId() != null)
   *                      ? ((PersistentBean)value).getId().toString
   *                      : ""
   *                : "";
   * @throws    ConverterException
   *            !(value instanceof PersistentBean))
   */
  public String getAsString(final FacesContext context,
                            final UIComponent component,
                            final Object value) throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (!(value instanceof PersistentBean)) {
      throw new ConverterException("Value is not subclass of PersistentBean Class");
    }
    PersistentBean persistentBean = ((PersistentBean)value);
    return (persistentBean.getId() != null) ? persistentBean.getId().toString() : EMPTY;
  }
}