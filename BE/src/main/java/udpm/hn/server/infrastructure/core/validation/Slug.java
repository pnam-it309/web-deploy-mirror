package udpm.hn.server.infrastructure.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SlugValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Slug {
    String message() default "Invalid slug format. Only lowercase letters, numbers, and hyphens are allowed.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
