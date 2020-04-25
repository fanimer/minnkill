package org.buko.seckill.service;

import org.buko.seckill.dto.Exposer;
import org.buko.seckill.entity.Seckill;
import org.buko.seckill.dto.SeckillExecution;

import java.util.List;

public interface SeckillService {
    /**
     * 查询所有秒杀商品
     * @return 商品对象列表
     */
    List<Seckill> getSerkillList();

    /**
     * 查询单个秒杀商品
     * @param seckillId 商品id
     * @return 商品信息封装类
     */
    Seckill getById(long seckillId);

    /**
     * 获取秒杀地址
     * @param seckillId 商品id
     * @return 商品地址封装类
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作，存在失败的可能
     * @param seckillId 商品id
     * @param userPhone 用户唯一标识
     * @param md5 md5加盐字符串
     * @return 提交结果
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5);

    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
