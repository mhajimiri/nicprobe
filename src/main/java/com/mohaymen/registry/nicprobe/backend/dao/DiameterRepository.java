package com.mohaymen.registry.nicprobe.backend.dao;

import com.mohaymen.registry.nicprobe.backend.model.DiameterModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiameterRepository extends CrudRepository<DiameterModel, String> {
}
