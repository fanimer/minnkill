package org.buko.seckill.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.buko.seckill.entity.SuccessKilled;

import java.util.Date;

public interface SuccessKilledDao {

    @Insert("insert ignore into success_killed(seckill_id, user_phone, state, create_time) " +
            "values (#{seckillId}, #{userPhone}, 0, #{killTime})")
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone, @Param("killTime") Date killTime);

    @Select("select sk.seckill_id, sk.user_phone, sk.create_time, " +
            "s.seckill_id \"seckill.seckill_id\"," +
            "s.name \"seckill.name\"," +
            "s.number \"seckill.number\"," +
            "s.start_time \"seckill.start_time\"," +
            "s.end_time \"seckill.end_time\"," +
            "s.create_time \"seckill.create_time\"" +
            "from success_killed sk " +
            "inner join seckill s on sk.seckill_id = s.seckill_id " +
            "where sk.seckill_id=#{seckillId} and sk.user_phone=#{userPhone}")
    @ResultType(SuccessKilled.class)
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
