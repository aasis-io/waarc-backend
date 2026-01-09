package com.waarc.plan;

import com.waarc.plan.pojo.PlanRequest;

import java.util.List;

public interface PlanService {

    Plan getplan(int planId);
    List<Plan> getAllplans();
    Plan save(PlanRequest request);
    Plan updateplan(PlanRequest request, int planId);
    Plan deleteplan(int planId);


}
