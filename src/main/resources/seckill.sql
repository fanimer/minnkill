-- 秒杀执行存储过程
DELIMITER $$ -- console ;
-- 定义存储参数
-- rowCount(): 返回上一条修改类型sql (delete, insert, update) 的影响行数
-- rowCount: 0: 未修改数据, >0: 表示修改的行数, <0: sql错误/未执行修改
Create procedure excuteSeckill(in fadeSeckillId BIGINT, in fadeUserPhone BIGINT, IN fadeKillTime TIMESTAMP, OUT fadeResult INT)
    BEGIN
        DECLARE insert_count int default 0;
        START TRANSACTION ;
        INSERT ignore success_killed(seckill_id, user_phone, state, create_time) VALUES (fadeSeckillId, fadeUserPhone, 0, fadeKillTime);
        SELECT ROW_COUNT() INTO insert_count;
        IF(insert_count = 0) THEN
            ROLLBACK ;
            SET fadeResult = -1; -- 重复秒杀
        ELSEIF (insert_count < 0) THEN
            ROLLBACK ;
            SET fadeResult = -2;
        ELSE
            UPDATE seckill SET `number` = `number` - 1 WHERE seckill_id = fadeSeckillId AND start_time < fadeKillTime AND end_time > fadeKillTime AND `number` > 0;
            SELECT ROW_COUNT() INTO insert_count;
            IF (insert_count = 0) THEN
                ROLLBACK ;
                SET fadeResult = 0; -- 秒杀关闭
            ELSEIF (insert_count < 0) THEN
                ROLLBACK ;
                SET fadeResult =  -2; -- 内部错误
            ELSE
                COMMIT ; -- 秒杀成功，事务提交
                SET fadeResult = 1; -- 秒杀成功
            END IF;
        END IF;
    END
$$ DELIMITER ;

SET @fadeResult = -3;

CALL excuteSeckill(1003, 18810464493, NOW(), @fadeResult);

SELECT @fadeResult;