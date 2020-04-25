package org.buko.seckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SuccessKilled {
    private long seckillId;
    private long userPhone;
    private int state;
    private Date createTime;
    private Seckill seckill;
}
