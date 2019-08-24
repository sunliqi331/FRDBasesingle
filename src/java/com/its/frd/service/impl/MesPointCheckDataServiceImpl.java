package	com.its.frd.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesPointCheckDataDao;
import com.its.frd.entity.MesPointCheckData;
import com.its.frd.service.MesPointCheckDataService;
import org.eclipse.jetty.util.ajax.JSON;
@Service
@Transactional
public class MesPointCheckDataServiceImpl implements MesPointCheckDataService {

    @Resource(name="redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

	@Autowired
	private MesPointCheckDataDao dao;

    @Override
    public List<MesPointCheckData> findPage(Page page) {
    	page.setOrderField("id");
        org.springframework.data.domain.Page<MesPointCheckData> springDataPage = dao.findAll(PageUtils.createPageable(page));
        page.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public List<MesPointCheckData> findPage(Specification<MesPointCheckData> specification, Page page) {
    	page.setOrderField("id");
        org.springframework.data.domain.Page<MesPointCheckData> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
        page.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public MesPointCheckData saveOrUpdate(MesPointCheckData t) {
        Map<String,Object> mapColor = Maps.newHashMap();
        mapColor.put("driverpointid", String.valueOf(t.getMesDriverPoints().getId()));
        mapColor.put("checkvalue", t.getCheckvalue());
        mapColor.put("name", t.getName());
        mapColor.put("colorcode", t.getColorcode());
        mapColor.put("companyfile_id", null != t.getCompanyfileId()? String.valueOf(t.getCompanyfileId()) : "");
        redisTemplate.opsForHash().put("mes_point_check_data", t.getCheckvalue() + String.valueOf(t.getMesDriverPoints().getId()), JSON.toString(mapColor));
        return dao.save(t);
    }

    @Override
    public List<MesPointCheckData> findByMesDriverPointsIdAndCheckvalue(Long mesDriverPointsId, Long checkvalue) {
        return dao.findByMesDriverPointsIdAndCheckvalue(mesDriverPointsId, checkvalue);
    }

    @Override
    public List<MesPointCheckData> findByMesDriverPointsIdAndName(Long mesDriverPointsId, String name) {
        return dao.findByMesDriverPointsIdAndName(mesDriverPointsId, name);
    }

    @Override
    public void deleteById(Long id) {
        MesPointCheckData delObj = dao.findOne(id);
        redisTemplate.opsForHash().delete("mes_point_check_data", delObj.getCheckvalue() + String.valueOf(delObj.getMesDriverPoints().getId()));
        dao.delete(id);
    }

	@Override
	public List<MesPointCheckData> findByPointsId(List<Long> ids) {
		// TODO Auto-generated method stub
		if(ids == null || ids.size() <= 0)
			return null;
		return dao.findByMesDriverPointsIdIn(ids);
	}

	@Override
	public MesPointCheckData findOne(Long id) {
		return dao.findOne(id);
		
	}
}
