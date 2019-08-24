import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.ajax.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;
import com.its.common.service.RedisService;
import com.its.frd.dao.MesPointCheckDataDao;
import com.its.frd.entity.MesPointCheckData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml",
        "classpath:applicationContext-shiro-cas.xml", "classpath:redis/applicationContext-redis.xml" })
public class redisTest {

    @Resource
    private RedisService redisService;
    
    @Resource(name="redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private MesPointCheckDataDao mesPointCheckDataDao;

    /**
     * Jedis
     */
    @Test
    public void mesDriverTypeService() {
        try {
//            List<MesPointCheckData> list = mesPointCheckDataDao.findAll();
//            for(MesPointCheckData obj :list) {
//                Map<String,Object> mapColor = Maps.newHashMap();
//                mapColor.put("driverpointid", String.valueOf(obj.getMesDriverPoints().getId()));
//                mapColor.put("checkvalue", obj.getCheckvalue());
//                mapColor.put("name", obj.getName());
//                mapColor.put("colorcode", obj.getColorcode());
//                mapColor.put("companyfile_id", null != obj.getCompanyfileId()? String.valueOf(obj.getCompanyfileId()) : "");
////                System.out.println(obj.getMesDriverPoints().getId());
//                //redisTemplate.opsForHash().put("mes_point_check_data", obj.getCheckvalue() + String.valueOf(obj.getMesDriverPoints().getId()), JSON.toString(mapColor));
////                redisTemplate.opsForHash().delete("mes_point_check_data", obj.getCheckvalue() + String.valueOf(obj.getMesDriverPoints().getId()));
//            }
//
//            redisTemplate.opsForHash().put("mes_point_check_data", "0632", "123213" );

            System.out.println(redisTemplate.opsForHash().get("MesPointsTemplate_50-9A-4C-1B-6F-5B", "205617"));
//            outList.forEach(obj->{
//                Map<String, String> outMap = (Map<String, String>)obj;
//                System.out.println(outMap.get("mesPointKey"));
//            });
//            System.out.println(outList.size());
//            Map<String , String> map = (Map<String, String>) JSON.parse((String)redisTemplate.opsForHash().get("mes_point_check_data", "0632"));
//            System.out.println(map.get("driverpointid"));
//            Boolean rs = redisTemplate.opsForHash().hasKey("mes_point_check_data", "0632");
//            System.out.println(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
