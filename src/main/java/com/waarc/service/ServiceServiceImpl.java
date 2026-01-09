package com.waarc.service;

import com.waarc.exception.ResourceNotFoundException;
import com.waarc.service.pojo.ServiceRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository  serviceRepository = new ServiceRepositoryImplementation();
    private final Logger log = LogManager.getLogger(ServiceServiceImpl.class);

    @Override
    public Service getService(int serviceId) {
        Service service = serviceRepository.getService(serviceId).orElseThrow(()-> new ResourceNotFoundException("Service Not Found with id :" + serviceId));
        log.info("Getting Service for id:",serviceId);
        return new Service(service.getId(),service.getImage(),service.getTitle(),service.getDescription());
    }

    @Override
    public List<Service> getAllServices() {
        log.info("Getting All services!");
        return serviceRepository.getAllServices();
    }

    @Override
    public Service save(ServiceRequest request) {
        log.info("Creating new Service !");
        return serviceRepository.save(request);
    }

    @Override
    public Service updateService(ServiceRequest request, int serviceId) {
        Service service = serviceRepository.getService(serviceId).orElseThrow(()-> new ResourceNotFoundException("Service not found with the id : "+ serviceId));
        log.info("Updating Service with id : ",serviceId);
         return serviceRepository.updateService(request,serviceId);
    }

    @Override
    public Service deleteService(int serviceId) {
        Service service = serviceRepository.getService(serviceId).orElseThrow(()-> new ResourceNotFoundException("Service not found with the id : "+ serviceId));
         log.info("Deleting service with id : ",serviceId);
         serviceRepository.deleteService(serviceId);
         return service;
    }
}
