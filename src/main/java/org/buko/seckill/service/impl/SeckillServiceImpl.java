package org.buko.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.buko.seckill.dao.SeckillDao;
import org.buko.seckill.dao.SuccessKilledDao;
import org.buko.seckill.dto.Exposer;
import org.buko.seckill.entity.Seckill;
import org.buko.seckill.dto.SeckillExecution;
import org.buko.seckill.entity.SuccessKilled;
import org.buko.seckill.enums.SeckillStatEnum;
import org.buko.seckill.exception.RepeatKillException;
import org.buko.seckill.exception.SeckillCloseException;
import org.buko.seckill.exception.SeckillException;
import org.buko.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    // 日志对象
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    // 随机生成md5加盐字符串
    private final String slat = RandomStringUtils.randomAlphanumeric(24);

    @Override
    public List<Seckill> getSerkillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) return new Exposer(false, seckillId);
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(long seckillId) {
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    // 注解控制事务，仅只读操作需要
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, SeckillCloseException, RepeatKillException {
        if (md5 == null || !md5.equals(getMd5(seckillId))) throw new SeckillException("seckill data rewrite");
        Date nowTime = new Date();
        try {
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone, nowTime);
            if (insertCount == 0) throw new RepeatKillException("seckill repeated");
            else {
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount == 0) throw new SeckillCloseException("seckill is close");
                else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
            throws SeckillCloseException, RepeatKillException, SeckillException {
        if(md5 == null || !md5.equals(getMd5(seckillId))) throw new SeckillException("seckill data rewrite");
        Date nowTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", nowTime);
        map.put("result", null);
        try {
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            } else return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}