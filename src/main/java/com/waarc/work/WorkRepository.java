package com.waarc.work;

import com.waarc.work.pojo.WorkRequest;

import java.util.List;
import java.util.Optional;

public interface WorkRepository {

    Optional<Work> getWork(int id);
    List<Work> getAllWorks();
    Work save(WorkRequest request);
    Work updateWork(WorkRequest request, int workId);
    Work deleteWork(int WorkId);

}
