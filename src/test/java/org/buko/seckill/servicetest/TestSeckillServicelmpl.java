package org.buko.seckill.servicetest;

import org.apache.log4j.Logger;
import org.buko.seckill.dto.Exposer;
import org.buko.seckill.entity.Seckill;
import org.buko.seckill.dto.SeckillExecution;
import org.buko.seckill.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestSeckillServicelmpl {
    private static final Logger logger = Logger.getLogger(TestSeckillServicelmpl.class);

    @Resource
    private SeckillService seckillService;

    @Test
    public void getSerkillList() {
        List<Seckill> seckills = seckillService.getSerkillList();
        seckills.forEach(System.out::println);
    }

    @Test
    public void getById() {
        Seckill seckill = seckillService.getById(1000L);
        System.out.println(seckill);
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer = seckillService.exportSeckillUrl(1000L);
        System.out.println(exposer);
    }

    @Test
    public void executeSeckill() {
        Exposer exposer = seckillService.exportSeckillUrl(1000L);
        SeckillExecution execution = seckillService.executeSeckill(1000L, 13209231213L, exposer.getMd5());
        System.out.println(exposer);
        System.out.println(execution);
    }

    @Test
    public void executeSeckillProcedure() {
        Exposer exposer = seckillService.exportSeckillUrl(1000L);
        SeckillExecution execution = seckillService.executeSeckillProcedure(1000L, 1231414156L, exposer.getMd5());
        System.out.println(exposer);
        System.out.println(execution);
    }

}
