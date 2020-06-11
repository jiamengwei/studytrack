package com.shardingsphere.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public TOrder(Integer userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    @TableId(value = "order_id", type = IdType.AUTO)
    private long orderId;

    private Integer userId;

    private String status;
}
