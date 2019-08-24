package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.MesDriverFaultTime;
import com.its.frd.entity.MesEnergy;

public interface MesDriverFaultTimeDao extends JpaRepository<MesDriverFaultTime, Long>, JpaSpecificationExecutor<MesDriverFaultTime> {
//	
//	@Query(value="select me from MesEnergy me where "
//			+ "me.updatetime = (select max(m.updatetime) from MesEnergy m where me.mesDriver.id = m.mesDriver.id) and me.energytype = :pointTypeKey"
//			+ " and me.mesDriver.id in :ids group by me.mesDriver.id order by me.updatetime desc"
//		)
//	public List<MesEnergy> findLatestRecord(@Param("pointTypeKey") String pointTypeKey,@Param("ids") List<Long> ids);

    @Query("FROM MesDriverFaultTime M WHERE M.mesDriver.id = ?1")
    public List<MesDriverFaultTime> findByMesDriverId(Long driverid);
}
