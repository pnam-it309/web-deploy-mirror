package udpm.hn.server.infrastructure.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    
    private long maxSize;
    private String[] allowedExtensions;
    private String[] mimeTypes;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowedExtensions = constraintAnnotation.allowedExtensions();
        this.mimeTypes = constraintAnnotation.mimeTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Let @NotNull or @NotBlank handle empty files
        }

        // Check file size
        if (file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "File size must be less than " + (maxSize / (1024 * 1024)) + "MB"
            ).addConstraintViolation();
            return false;
        }

        // Check file extension
        if (allowedExtensions.length > 0) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return false;
            }
            
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            if (Arrays.stream(allowedExtensions).noneMatch(ext -> ext.equalsIgnoreCase(fileExtension))) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "File type not allowed. Allowed types: " + String.join(", ", allowedExtensions)
                ).addConstraintViolation();
                return false;
            }
        }

        // Check MIME type
        if (mimeTypes.length > 0 && !Arrays.asList(mimeTypes).contains(file.getContentType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "File type not allowed. Allowed MIME types: " + String.join(", ", mimeTypes)
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
