package org.buko.seckill.dto;

import lombok.Data;
import org.buko.seckill.entity.SuccessKilled;
import org.buko.seckill.enums.SeckillStatEnum;

// 秒杀结果封装
@Data
public class SeckillExecution {
    private long seckillId;
    private int state;
    private String stateInfo;
    private SuccessKilled successKilled;
    public SeckillExecution(long seckillId, SeckillStatEnum seckillStatEnum, SuccessKilled successKilled) {
        super();
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
        this.successKilled = successKilled;
    }
    public SeckillExecution(long seckillId, SeckillStatEnum seckillStatEnum) {
        super();
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
    }
    public SeckillExecution(SeckillStatEnum seckillStatEnum) {
        super();
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
    }

}
