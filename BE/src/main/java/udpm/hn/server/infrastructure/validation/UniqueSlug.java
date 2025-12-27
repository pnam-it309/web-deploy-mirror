package udpm.hn.server.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueSlugValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSlug {
    String message() default "Slug is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    /**
     * The entity class that contains the slug field
     */
    Class<?> entityClass();
    
    /**
     * The name of the field that contains the slug
     */
    String fieldName() default "slug";
}
