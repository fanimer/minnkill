package org.buko.seckill.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;

    public SeckillResult(boolean success, T data) {
        super();
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        super();
        this.success = success;
        this.error = error;
    }

}
