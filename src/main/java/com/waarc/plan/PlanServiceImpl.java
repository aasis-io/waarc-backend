package com.waarc.plan;

import com.waarc.exception.ResourceNotFoundException;
import com.waarc.plan.pojo.PlanRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository = new PlanRepositoryImplementation();
    private final Logger log = LogManager.getLogger(PlanServiceImpl.class);

    @Override
    public Plan getplan(int planId) {
        Plan plan = planRepository.getplan(planId).orElseThrow(()-> new ResourceNotFoundException("plan Not Found with id :" + planId));
        return new Plan(plan.getId(), plan.getTag(), plan.getImage(), plan.getTitle(), plan.getDescription());
    }

    @Override
    public List<Plan> getAllplans() {
        return planRepository.getAllplans();
    }

    @Override
    public Plan save(PlanRequest request) {
        return planRepository.save(request);
    }

    @Override
    public Plan updateplan(PlanRequest request, int planId) {
        Plan plan = planRepository.getplan(planId).orElseThrow(()-> new ResourceNotFoundException("plan not found with the id : "+ planId));
         return planRepository.updateplan(request,planId);
    }

    @Override
    public Plan deleteplan(int planId) {
        Plan plan = planRepository.getplan(planId).orElseThrow(()-> new ResourceNotFoundException("plan not found with the id : "+ planId));
        planRepository.deleteplan(planId);
        return plan;
    }
}
