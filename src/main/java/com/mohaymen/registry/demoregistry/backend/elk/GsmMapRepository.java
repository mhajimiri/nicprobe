package com.mohaymen.registry.demoregistry.backend.elk;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GsmMapRepository extends CrudRepository<GsmMapModel, String> {
//    @Query(value = "select * from gsm_map t,(SELECT tid,invoke_Id FROM GSM_MAP where status=0 GROUP BY tid,invoke_Id HAVING COUNT(tid)>1) j where t.tid=j.tid and t.invoke_Id=j.invoke_Id", nativeQuery = true)
//    List<GsmMapModel> findSameReqAndResp();
//
//    @Query(value = "SELECT * FROM gsm_map WHERE tid=? and invoke_id=? and status=0 ORDER BY id", nativeQuery = true)
//    List<GsmMapModel> findByTidAndInvokeId(String tid,String invokeId);
//
//    @Modifying
//    @Query(value = "DELETE FROM gsm_map WHERE status!=0", nativeQuery = true)
//    void deleteAllCheckRecords();
}
