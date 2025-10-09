package udpm.hn.server.infrastructure.core.job.products.service;

import udpm.hn.server.core.common.base.ResponseObject;

import java.io.ByteArrayInputStream;

public interface ExcelFileProduct {
    ResponseObject<ByteArrayInputStream> downloadTemplate();

}
