package com.its.common.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.common.entity.main.Permission;

public interface PermissionDAO extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    @Query("FROM Permission P WHERE P.sn=?1 AND P.module.id=?2")
    public List<Permission> findBySnAndModuleId(String sn,Long moduleId);
}