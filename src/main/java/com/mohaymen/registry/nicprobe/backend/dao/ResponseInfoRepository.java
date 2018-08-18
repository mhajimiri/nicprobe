package com.mohaymen.registry.nicprobe.backend.dao;


import com.mohaymen.registry.nicprobe.backend.model.ResponseInfoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseInfoRepository extends CrudRepository<ResponseInfoModel,String> {
}
