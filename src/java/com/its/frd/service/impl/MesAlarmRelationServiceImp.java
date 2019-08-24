package com.its.frd.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.its.common.entity.main.Dictionary;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesAlarmRelationDao;
import com.its.frd.entity.MesAlarmRelation;
import com.its.frd.entity.MesPoints;
import com.its.frd.service.MesAlarmRelationService;

@Service
public class MesAlarmRelationServiceImp implements MesAlarmRelationService {
	
	@Resource
	private MesAlarmRelationDao mesAlarmRelationDao;

	@Override
	public MesAlarmRelation findOne(Long id) {
		return mesAlarmRelationDao.findOne(id);
	}

	@Override
	public List<MesAlarmRelation> findPage(Specification<MesAlarmRelation> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesAlarmRelation> springDataPage = mesAlarmRelationDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public void deleteById(Long id) {
		mesAlarmRelationDao.delete(id);
	}

	@Override
	public void saveOrUpdate(MesAlarmRelation mesAlarmRelation) {
		mesAlarmRelationDao.save(mesAlarmRelation);
	}

	@Override
	public List<MesAlarmRelation> findByPointId(Long id) {
		return mesAlarmRelationDao.findByMesPointsId(id);
	}

	@Override
	public String saveModelRelation(List<Dictionary> itemList, MultipartFile alarmRelationFile, MesPoints point) {
		List<MesAlarmRelation> relationList = new ArrayList<>();
		//该测点已经存在的测点ID
		List<MesAlarmRelation> alarmRelations = this.findByPointId(point.getId());
		boolean isIntegrity = true;
		String statusCode = "ok";
		try {
			InputStreamReader isr = new InputStreamReader(alarmRelationFile.getInputStream(), "gbk");
			BufferedReader reader = new BufferedReader(isr);
			List<String> tempList = new ArrayList<>();
			String line;
			while(StringUtils.isNotEmpty(line=reader.readLine())){
				if(StringUtils.isEmpty(line)){
					isIntegrity = false;
					continue;
				}
				String[] split = compatibleArray(line, tempList);
				//验证每一行的数据长度
				if(split.length!=3){
					isIntegrity = false;
					continue;
				}
				//报警ID不可包含汉字
				if(isContainChinese(split[0]) || ("SSS".equals(split[0]))){
					isIntegrity = false;
					continue;
				}
				//报警测点ID不可重复
				boolean isDuplicate = false;
				for(MesAlarmRelation ma : alarmRelations){
					if(split[0].equals(ma.getAlarmCode())){
						isIntegrity = false;
						isDuplicate = true;
						break;
					}
				}
				if(isDuplicate){
					continue;
				}
				//检验报警类型是否符合字典规范
				boolean isLegal = false;
				for(Dictionary dti : itemList){
					if(split[1].equals(dti.getValue())){
						isLegal = true;
					}
				}
				if(!isLegal){
					isIntegrity = false;
					continue;
				}
				MesAlarmRelation relation = new MesAlarmRelation();
				relation.setMesPoints(point);
				relation.setAlarmCode(split[0]);
				relation.setAlarmType(split[1]);
				relation.setInfo(split[2]);
				relationList.add(relation);
			}
			reader.close();
			if(!isIntegrity){
				statusCode = "warning";
			}
			if(relationList.size()<1){
				statusCode = "warning";
			}
			for(MesAlarmRelation marl : relationList){
				mesAlarmRelationDao.save(marl);
			}
		} catch (Exception e) {
			return "error";
		}
		return statusCode;
	}
	
	/**
	 * 验证字符串是否包含汉字
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
	
	/**
	 * 兼容模板的空格间隔模式，和逗号分隔模式
	 * @param originalStr 原始的字符串
	 * @param tempList 临时数组
	 * @return
	 */
	public static String[] compatibleArray(String originalStr,List<String> tempList){
		String[] originalArray = originalStr.split(",");
		if(originalArray.length!=3){
			tempList.clear();
			String[] temp = originalStr.split(" ");
			for(String s : temp){
				if(StringUtils.isNotEmpty(s)){
					tempList.add(s);
				}
			}
			if(tempList.size()==3){
				originalArray = tempList.toArray(new String[tempList.size()]);
			}
		}
		//去除空格和csv产生的引号
		for(int i=0;i<originalArray.length;i++){
			originalArray[i] = originalArray[i].replaceAll(" ", "");
			originalArray[i] = originalArray[i].replace("\"", "");
		}
		
		return originalArray;
	}
	
}
