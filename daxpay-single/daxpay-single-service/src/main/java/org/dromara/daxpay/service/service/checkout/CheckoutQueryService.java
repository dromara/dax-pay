package org.dromara.daxpay.service.service.checkout;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.result.checkout.*;
import org.dromara.daxpay.service.convert.config.CheckoutAggregateConfigConvert;
import org.dromara.daxpay.service.convert.config.CheckoutConfigConvert;
import org.dromara.daxpay.service.convert.config.CheckoutGroupConfigConvert;
import org.dromara.daxpay.service.convert.config.CheckoutItemConfigConvert;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutAggregateConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutGroupConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutItemConfigManager;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutAggregateConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutItemConfig;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 收银码牌查询服务
 * @author xxm
 * @since 2024/11/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutQueryService {
    private final CheckoutConfigManager checkoutConfigManager;
    private final CheckoutGroupConfigManager checkoutGroupConfigManager;
    private final CheckoutItemConfigManager checkoutItemConfigManager;
    private final CheckoutAggregateConfigManager checkoutAggregateConfigManager;
    private final CheckoutAssistService checkoutAssistService;
    private final PayOrderManager payOrderManager;

    /**
     * 获取收银台配置
     */
    public CheckoutConfigResult getConfig(String appId){
        // 查询配置
        return checkoutConfigManager.findByAppId(appId)
                .map(CheckoutConfigConvert.CONVERT::toResult)
                .orElseThrow(() -> new DataNotExistException("未查询到收银台配置"));

    }

    /**
     * 获取收银台分组配置, 包含明细配置
     */
    public List<CheckoutGroupConfigResult> getGroupConfigs(String appId, String checkoutType){
        // 查询类目
        var groups = checkoutGroupConfigManager.findAllSortByAppIdAndType(appId, checkoutType);
        if (groups.isEmpty()){
            return new ArrayList<>();
        }
        var groupIds = groups.stream()
                .map(MpIdEntity::getId)
                .toList();
        // 查询明细配置
        List<CheckoutItemConfig> itemConfigs = checkoutItemConfigManager.findAllByGroupIds(groupIds);
        // 分组
        var itemGroupMap = itemConfigs.stream()
                .collect(Collectors.groupingBy(CheckoutItemConfig::getGroupId, LinkedHashMap::new, Collectors.toList()));
        // 转换
        return groups.stream()
                .map(o->{
                    var result = CheckoutGroupConfigConvert.CONVERT.toResult(o);
                    List<CheckoutItemConfigResult> list = itemGroupMap.getOrDefault(o.getId(), new ArrayList<>())
                            .stream()
                            .map(CheckoutItemConfigConvert.CONVERT::toResult)
                            .toList();
                    result.setItems(list);
                    return result;
                })
                .toList();
    }


    /**
     * 获取收银台配置和订单相关信息, 不需要签名和鉴权
     */
    public CheckoutOrderAndConfigResult getOrderAndConfig(String orderNo, String checkoutType){
        CheckoutOrderAndConfigResult checkoutInfoResult = new CheckoutOrderAndConfigResult();
        // 订单信息
        PayOrder payOrder = checkoutAssistService.getOrderAndCheck(orderNo);
        CheckoutOrderResult order = new CheckoutOrderResult()
                .setTitle(payOrder.getTitle())
                .setDescription(payOrder.getDescription())
                .setAmount(payOrder.getAmount())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOrderNo(payOrder.getOrderNo());
        checkoutInfoResult.setOrder(order);
        // 获取收银台配置
        checkoutInfoResult.setConfig(this.getConfig(payOrder.getAppId()));
        // 获取分组和明细配置
        checkoutInfoResult.setGroupConfigs(this.getGroupConfigs(payOrder.getAppId(), checkoutType));
        return checkoutInfoResult;
    }

    /**
     * 收银台聚合支付配置
     */
    public CheckoutAggregateOrderAndConfigResult getAggregateConfig(String orderNo, String aggregateType){
        var checkoutInfoResult = new CheckoutAggregateOrderAndConfigResult();
        // 订单信息
        PayOrder payOrder = checkoutAssistService.getOrderAndCheck(orderNo);
        CheckoutOrderResult order = new CheckoutOrderResult()
                .setTitle(payOrder.getTitle())
                .setDescription(payOrder.getDescription())
                .setAmount(payOrder.getAmount())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOrderNo(payOrder.getOrderNo());
        checkoutInfoResult.setOrder(order);
        // 获取收银台配置
        checkoutInfoResult.setConfig(this.getConfig(payOrder.getAppId()));
        // 获取聚合支付配置
        CheckoutAggregateConfig aggregateConfig = checkoutAggregateConfigManager.findByAppIdAndType(payOrder.getAppId(), aggregateType)
                .orElseThrow(() -> new DataNotExistException("聚合支付配置"));
        checkoutInfoResult.setAggregateConfig(CheckoutAggregateConfigConvert.CONVERT.toResult(aggregateConfig));
        return checkoutInfoResult;
    }

    /**
     * 查询订单状态
     */
    public Boolean findStatusByOrderNo(String orderNo) {
        String status = payOrderManager.findByOrderNo(orderNo)
                .map(PayOrder::getStatus)
                .orElse(null);
        return Objects.equals(status, PayStatusEnum.SUCCESS.getCode());
    }
}
