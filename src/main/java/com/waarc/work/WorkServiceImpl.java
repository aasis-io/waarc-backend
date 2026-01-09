package com.waarc.work;

import com.waarc.exception.ResourceNotFoundException;
import com.waarc.work.pojo.WorkRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class WorkServiceImpl implements WorkService {
    private final WorkRepository workRepository = new WorkRepositoryImplementation();
    private final Logger log = LogManager.getLogger(WorkServiceImpl.class);

    @Override
    public Work getWork(int workId) {
        Work work = workRepository.getWork(workId).orElseThrow(()-> new ResourceNotFoundException("Work Not Found with id :" + workId));
        return new Work(work.getId(),work.getTag(), work.getImage(), work.getTitle(), work.getDescription());
    }

    @Override
    public List<Work> getAllWorks() {
        return workRepository.getAllWorks();
    }

    @Override
    public Work save(WorkRequest request) {
        return workRepository.save(request);
    }

    @Override
    public Work updateWork(WorkRequest request, int workId) {
        Work work = workRepository.getWork(workId).orElseThrow(()-> new ResourceNotFoundException("Work not found with the id : "+ workId));

         return workRepository.updateWork(request,workId);
    }

    @Override
    public Work deleteWork(int workId) {
        Work work = workRepository.getWork(workId).orElseThrow(()-> new ResourceNotFoundException("Work not found with the id : "+ workId));
        workRepository.deleteWork(workId);
        return work;
    }
}
