package org.buko.seckill.daotest;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.buko.seckill.dao.SeckillDao;
import org.buko.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestSeckillDao {
    private static final Logger logger = Logger.getLogger(TestSeckillDao.class);

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println(updateCount);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Date date = new Date();
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill.getStartTime());
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        seckills.forEach(System.out::println);
    }

    @Test
    public void killByProcedure() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", 1000L);
        map.put("phone", 13160737213L);
        map.put("killTime", new Date());
        map.put("result", null);
        seckillDao.killByProcedure(map);
        int result = MapUtils.getInteger(map, "result", -2);
        System.out.println(result);
    }

}
