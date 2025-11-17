package org.dromara.daxpay.payment.pay.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.payment.pay.dao.constant.ChannelConstManager;
import org.dromara.daxpay.payment.pay.entity.constant.ChannelConst;
import org.dromara.daxpay.payment.pay.param.constant.ChannelConstQuery;
import org.dromara.daxpay.payment.pay.result.constant.ChannelConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String findNameByCode(String code) {
        return channelConstManager.findByField(ChannelConst::getCode, code).map(ChannelConst::getName)
                .orElse(null);
    }

    /**
     * 启用道通列表
     */
    public List<LabelValue> dropdownByEnable() {
        List<ChannelConst> channelList = channelConstManager.findAllByEnable();
        return channelList.stream()
                .map(o->new LabelValue(o.getName(), o.getCode()))
                .toList();
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
