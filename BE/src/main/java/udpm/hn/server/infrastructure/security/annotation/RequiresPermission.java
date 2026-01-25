package udpm.hn.server.infrastructure.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for method-level permission checks
 * Usage: @RequiresPermission("EDIT_APP")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {

    /**
     * Permission code required to execute the method
     */
    String value();

    /**
     * Optional custom error message
     */
    String message() default "Insufficient permissions";
}
