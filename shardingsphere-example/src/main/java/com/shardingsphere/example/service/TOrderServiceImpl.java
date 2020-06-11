package com.shardingsphere.example.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shardingsphere.example.entity.TOrder;
import com.shardingsphere.example.mapper.TOrderMapper;
import org.springframework.stereotype.Service;

@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {
}
