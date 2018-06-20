package com.mohaymen.registry.demoregistry.backend.elk;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GsmMapRepository extends CrudRepository<GsmMapModel, String> {
    @Query(value = "SELECT t.tid,t.invokeId,t.calledDigit,t.callingDigit FROM GSM_MAP t GROUP BY t.tid,t.invokeId,t.calledDigit,t.callingDigit HAVING COUNT(t.tid,t.invokeId,t.calledDigit,t.callingDigit) > 1", nativeQuery = true)
    List<GsmMapModel> findSameReqAndResp();
}
