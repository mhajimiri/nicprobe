package com.mohaymen.registry.demoregistry.backend.elk;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiameterRepository extends CrudRepository<DiameterModel, String> {
//    @Query(value = "select * from diameter where session_Id in (SELECT session_Id FROM diameter WHERE status=0 GROUP BY session_Id HAVING COUNT(session_Id)>1)", nativeQuery = true)
//    List<DiameterModel> findSameReqAndResp();
//
//    @Query(value = "SELECT * FROM diameter WHERE session_Id=? and status=0 ORDER BY id", nativeQuery = true)
//    List<DiameterModel> findBySessionId(String session_id);
//
//    @Modifying
//    @Query(value = "DELETE FROM diameter WHERE status!=0", nativeQuery = true)
//    void deleteAllCheckRecords();
}
