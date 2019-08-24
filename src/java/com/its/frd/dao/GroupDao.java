package com.its.frd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.its.frd.entity.Group;

public interface GroupDao extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

}
