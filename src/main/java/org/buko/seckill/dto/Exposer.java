package org.buko.seckill.dto;

import lombok.Data;

@Data
public class Exposer {
    private boolean exposed;
    private String md5;
    private long seckillId;
    private long now;
    private long start;
    private long end;

    public Exposer(boolean exposed, String md5, long seckillId) {
        super();
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
        super();
        this.exposed = exposed;
        this.now = now;
        this.start = start;
        this.end = end;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId) {
        super();
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }
}
