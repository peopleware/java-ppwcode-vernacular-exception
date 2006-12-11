package be.peopleware.metainfo.vcs;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for CVS meta-data. By using this annotation,
 * the CVS data about the source revision the compiled code is based on,
 * is available in the code.
 *
 * Usage pattern:
 * <pre>
 * ATCvsInfo(revision = &quot;$Revision$&quot;,
 *          date     = &quot;$Date$&quot;,
 *          state    = &quot;$State$&quot;,
 *          tag      = &quot;$Name$&quot;)
 * public class ... {
 *  ...
 *  }
 * </pre>
 *
 * @author    Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface CvsInfo {

  /**
   * Source code revision.
   * Fill out with &quot;$Revision$&quot;
   */
  String revision();

  /**
   * Source code revision. Fill out with
   * &quot;$Date$&quot;
   */
  String date();

  /**
   * Source code revision.
   * Fill out with &quot;$State$&quot;
   */
  String state();

  /**
   * Source code revision.
   * Fill out with &quot;$Name$&quot;
   */
  String tag();

}
