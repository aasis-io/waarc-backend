package com.waarc.plan;

import com.waarc.plan.pojo.PlanRequest;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {

    Optional<Plan> getplan(int id);
    List<Plan> getAllplans();
    Plan save(PlanRequest request);
    Plan updateplan(PlanRequest request, int planId);
    Plan deleteplan(int planId);

}
