import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.its.frd.dao.MesDataDriverStatusDao;
import com.its.frd.entity.MesDataDriverStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml",
        "classpath:applicationContext-shiro-cas.xml", "classpath:redis/applicationContext-redis.xml" })
public class driverStatusTest {

    @Resource
    private MesDataDriverStatusDao mesDataDriverStatusDao;

    @Test
    public void mesDriverTypeService() {
        try {
            List<MesDataDriverStatus> statusList = mesDataDriverStatusDao.getDriverStatusCount(516L, 242L, 358L, "0");
            System.out.println(statusList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
