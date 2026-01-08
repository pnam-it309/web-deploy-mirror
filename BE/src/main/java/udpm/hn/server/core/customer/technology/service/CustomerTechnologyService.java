package udpm.hn.server.core.customer.technology.service;

import udpm.hn.server.core.customer.technology.model.CustomerTechnologyResponse;

import java.util.List;

public interface CustomerTechnologyService {
    List<CustomerTechnologyResponse> getAllActiveInfo();
}
