package com.its.frd.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.its.frd.entity.MesEnergy;

public interface MesEnergyDao extends JpaRepository<MesEnergy, Long>, JpaSpecificationExecutor<MesEnergy> {
	
	@Query(value="select me from MesEnergy me where "
			+ "me.updatetime = (select max(m.updatetime) from MesEnergy m where me.mesDriver.id = m.mesDriver.id) and me.energytype = :pointTypeKey"
			+ " and me.mesDriver.id in :ids group by me.mesDriver.id order by me.updatetime desc"
		)
	public List<MesEnergy> findLatestRecord(@Param("pointTypeKey") String pointTypeKey,@Param("ids") List<Long> ids);

	public List<MesEnergy> findByUpdatetimeBetween(Timestamp startDate,Timestamp endDate);
	
	@Query("FROM MesEnergy M WHERE M.updatetime>=?1 AND M.updatetime<?2 AND M.mesDriver.id=?3")
	public List<MesEnergy> findByUpdatetimeBetweenAndmesDriver(Timestamp startDate,Timestamp endDate,Long driverId);

	public List<MesEnergy> findByMesDriverIdAndUpdatetimeBetween(Long driverId,Timestamp startDate,Timestamp endDate);
	
	@Query("FROM MesEnergy M WHERE M.energytype=?1 AND M.updatetime>=?2 AND M.updatetime<?3 AND M.mesDriver.id=?4")
	public List<MesEnergy> findEnergy(String energyType, Timestamp startDate, Timestamp endDate, Long driverId);
	
	@Query("FROM MesEnergy M WHERE M.energytype=?1 AND M.updatetime>=?2 AND M.updatetime<?3")
	public List<MesEnergy> findEnergy(String energyType, Timestamp startDate, Timestamp endDate);
}
