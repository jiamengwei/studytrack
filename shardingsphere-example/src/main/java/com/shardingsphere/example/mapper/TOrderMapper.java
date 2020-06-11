package com.shardingsphere.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shardingsphere.example.entity.TOrder;

public interface TOrderMapper extends BaseMapper<TOrder> {
    int save(TOrder tOrder);
}
