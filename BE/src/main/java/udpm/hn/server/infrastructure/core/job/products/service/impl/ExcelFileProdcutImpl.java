package udpm.hn.server.infrastructure.core.job.products.service.impl;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.core.job.products.service.ExcelFileProduct;

import java.io.ByteArrayInputStream;

public class ExcelFileProdcutImpl implements ExcelFileProduct {
    @Override
    public ResponseObject<ByteArrayInputStream> downloadTemplate() {
        return null;
    }
}
