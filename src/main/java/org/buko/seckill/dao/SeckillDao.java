package org.buko.seckill.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.buko.seckill.entity.Seckill;

import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    /**
     * 库存下降一个单位
     * @param seckillId
     * @param killTime
     * @return 更新条数
     */
    @Update("update seckill set number=number-1 where seckill_id=#{seckillId} and " +
            "start_time<=#{killTime} and end_time>=#{killTime} and number>0")
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 查询单个秒杀商品
     * @param seckillId
     * @return 商品封装类
     */
    @Select("select seckill_id, name, number, start_time, end_time, create_time " +
            "from seckill where seckill_id=#{seckillId}")
    @ResultType(Seckill.class)
    Seckill queryById(@Param("seckillId") long seckillId);

    /**
     * 查询多个秒杀商品
     * @param offset 左区间
     * @param limit 右区间
     * @return 商品列表
     */
    @Select("select seckill_id, name, number, start_time, end_time, create_time " +
            "from seckill order by create_time desc limit #{offset}, #{limit}")
    @ResultType(Seckill.class)
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    @Select({"call excuteSeckill(" +
            "#{seckillId, jdbcType=BIGINT, mode=IN}," +
            "#{phone, jdbcType=BIGINT, mode=IN}," +
            "#{killTime, jdbcType=TIMESTAMP, mode=IN}," +
            "#{result, jdbcType=INTEGER, mode=OUT})"})
    @Options(statementType = StatementType.CALLABLE)
    void killByProcedure(Map<String, Object> paramMap);
}
