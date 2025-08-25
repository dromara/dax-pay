package org.dromara.daxpay.service.pay.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.pay.dao.constant.ChannelConstManager;
import org.dromara.daxpay.service.pay.entity.constant.ChannelConst;
import org.dromara.daxpay.service.pay.param.constant.ChannelConstQuery;
import org.dromara.daxpay.service.pay.result.constant.ChannelConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通道常量
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

    /**
     * 通道名称
     */
    @Cacheable(value = "cache:channel", key = "#code")
    public String findNameByCode(String code) {
        return channelConstManager.findByField(ChannelConst::getCode, code).map(ChannelConst::getName)
                .orElse(null);
    }

    /**
     * 服务商通道列表
     */
    public List<LabelValue> dropdownByIsv(){
        List<ChannelConst> channelList = channelConstManager.findAllByIsvAndEnable();
        return channelList.stream()
                .map(o->new LabelValue(o.getName(), o.getCode()))
                .toList();
    }

    /**
     * 可进件通道下拉列表
     */
    public List<LabelValue> dropdownByApply() {
        List<ChannelConst> channelList = channelConstManager.findAllByApplyAndEnable();
        return channelList.stream()
                .map(o->new LabelValue(o.getName(), o.getCode()))
                .toList();

    }
}
