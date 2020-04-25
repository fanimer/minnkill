package org.buko.seckill.daotest;

import org.apache.log4j.Logger;
import org.buko.seckill.dao.SuccessKilledDao;
import org.buko.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestSeckillSuccessDao {
    private static Logger logger = Logger.getLogger(TestSeckillSuccessDao.class);

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        int count = successKilledDao.insertSuccessKilled(1000L, 12345678901L, new Date());
        System.out.println(count);
    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L, 12345678901L);
        System.out.println(successKilled);
    }

}
