package com.mohaymen.registry.demoregistry.backend.elk;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseInfoRepository extends CrudRepository<ResponseInfoModel,String> {

}
