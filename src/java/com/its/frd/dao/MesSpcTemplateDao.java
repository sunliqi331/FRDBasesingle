package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.MesSpcTemplate;

public interface MesSpcTemplateDao extends JpaSpecificationExecutor<MesSpcTemplate>, JpaRepository<MesSpcTemplate, Double> {

}
