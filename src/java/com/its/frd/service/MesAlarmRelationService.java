package com.its.frd.service;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import com.its.common.entity.main.Dictionary;
import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesAlarmRelation;
import com.its.frd.entity.MesPoints;

public interface MesAlarmRelationService {
	
	public MesAlarmRelation findOne(Long id);
	
	public List<MesAlarmRelation> findPage(Specification<MesAlarmRelation> specification,Page page);

	public void deleteById(Long id);

	public void saveOrUpdate(MesAlarmRelation mesAlarmRelation);
	
	public List<MesAlarmRelation> findByPointId(Long id);
	
	/**
	 * 解析txt文档，插入数据库数据
	 * @param itemList 报警类型的字典集合
	 * @param alarmRelationFile 导入的文件
	 * @param pointId 报警测点ID
	 * @return 状态值
	 */
	public String saveModelRelation(List<Dictionary> itemList, MultipartFile alarmRelationFile, MesPoints point);
	
}
