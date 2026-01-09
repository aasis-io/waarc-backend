package com.waarc.work;

import com.waarc.work.pojo.WorkRequest;

import java.util.List;

public interface WorkService{

    Work getWork(int WorkId);
    List<Work> getAllWorks();
    Work save(WorkRequest request);
    Work updateWork(WorkRequest request, int WorkId);
    Work deleteWork(int WorkId);


}
