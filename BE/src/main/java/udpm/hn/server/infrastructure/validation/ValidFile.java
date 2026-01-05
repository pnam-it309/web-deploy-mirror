package udpm.hn.server.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {
    String message() default "Invalid file";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    // Maximum file size in MB. Default is 5MB
    long maxSize() default 5 * 1024 * 1024;
    
    // Allowed file extensions
    String[] allowedExtensions() default {};
    
    // Allowed MIME types
    String[] mimeTypes() default {};
}
