package org.dromara.daxpay.service.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.dao.constant.ChannelConstManager;
import org.dromara.daxpay.service.entity.constant.ChannelConst;
import org.dromara.daxpay.service.param.constant.ChannelConstQuery;
import org.dromara.daxpay.service.result.constant.ChannelConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelConstService {
    private final ChannelConstManager channelConstManager;
    /**
     * 分页
     */
    public PageResult<ChannelConstResult> page(PageParam pageParam, ChannelConstQuery query) {
        return MpUtil.toPageResult(channelConstManager.page(pageParam, query));
    }

    @Cacheable(value = "cache:channel", key = "#code")
    public String findNameByCode(String code) {
        return channelConstManager.findByField(ChannelConst::getCode, code).map(ChannelConst::getName)
                .orElse(null);
    }
}
