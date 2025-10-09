package udpm.hn.server.infrastructure.core.job.products.upload;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileUploadService {
    void save(InputStream file) throws IOException, InvalidFormatException;
    void uploadFile(MultipartFile file) throws Exception;

}
