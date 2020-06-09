package com.elastic.service.impl;

import com.elastic.entity.CloudHistory;
import com.elastic.mapper.CloudHistoryMapper;
import com.elastic.service.CloudHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网易云信群聊历史记录 服务实现类
 * </p>
 *
 * @author JiaMengwei
 * @since 2020-05-14
 */
@Service
public class CloudHistoryServiceImpl extends ServiceImpl<CloudHistoryMapper, CloudHistory> implements CloudHistoryService {

}
