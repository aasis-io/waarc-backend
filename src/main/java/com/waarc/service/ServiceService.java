package com.waarc.service;

import com.waarc.service.pojo.ServiceRequest;

import java.util.List;
import java.util.Optional;

public interface ServiceService {

    Service getService(int serviceId);
    List<Service> getAllServices();
    Service save(ServiceRequest request);
    Service updateService(ServiceRequest request, int serviceId);
    Service deleteService(int serviceId);


}
