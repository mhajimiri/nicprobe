package com.mohaymen.registry.nicprobe.backend.dao;

import com.mohaymen.registry.nicprobe.backend.model.GsmMapModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GsmMapRepository extends CrudRepository<GsmMapModel, String> {
}
