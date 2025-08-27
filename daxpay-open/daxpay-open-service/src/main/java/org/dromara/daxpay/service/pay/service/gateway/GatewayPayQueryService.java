package org.dromara.daxpay.service.pay.service.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.enums.GatewayCashierTypeEnum;
import org.dromara.daxpay.core.enums.GatewayPayEnvTypeEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.param.gateway.GatewayOrderAndConfigParam;
import org.dromara.daxpay.service.common.service.config.PlatformConfigService;
import org.dromara.daxpay.service.merchant.convert.gateway.AggregatePayConfigConvert;
import org.dromara.daxpay.service.merchant.convert.gateway.CashierGroupConfigConvert;
import org.dromara.daxpay.service.merchant.convert.gateway.CashierItemConfigConvert;
import org.dromara.daxpay.service.merchant.entity.gateway.CashierItemConfig;
import org.dromara.daxpay.service.merchant.result.gateway.CashierGroupConfigResult;
import org.dromara.daxpay.service.merchant.result.gateway.CashierItemConfigResult;
import org.dromara.daxpay.service.merchant.result.gateway.GatewayPayConfigResult;
import org.dromara.daxpay.service.merchant.service.gateway.AggregateConfigService;
import org.dromara.daxpay.service.merchant.service.gateway.CashierConfigService;
import org.dromara.daxpay.service.merchant.service.gateway.GatewayPayConfigService;
import org.dromara.daxpay.service.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.pay.result.gateway.AggregateOrderAndConfigResult;
import org.dromara.daxpay.service.pay.result.gateway.GatewayOrderAndConfigResult;
import org.dromara.daxpay.service.pay.result.gateway.GatewayOrderResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 收银码牌查询服务
 * @author xxm
 * @since 2024/11/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayQueryService {
    private final AggregateConfigService aggregateConfigService;
    private final GatewayPayConfigService gatewayPayConfigService;
    private final CashierConfigService cashierConfigService;
    private final GatewayPayAssistService gatewayAssistService;
    private final PayOrderManager payOrderManager;
    private final PlatformConfigService platformConfigService;

    /**
     * 获取收银台配置和订单相关信息, 不需要签名和鉴权, 忽略租户拦截
     */
    @IgnoreTenant
    public GatewayOrderAndConfigResult getOrderAndConfig(GatewayOrderAndConfigParam param){
        var gatewayInfo = new GatewayOrderAndConfigResult();
        // 订单信息
        var payOrder = Optional.ofNullable(gatewayAssistService.getOrderAndCheck(param.getOrderNo()))
                .orElseThrow(() -> new DataNotExistException("订单不存在"));

        var order = new GatewayOrderResult()
                .setTitle(payOrder.getTitle())
                .setDescription(payOrder.getDescription())
                .setAmount(payOrder.getAmount())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setExpiredTime(payOrder.getExpiredTime())
                .setOrderNo(payOrder.getOrderNo());
        gatewayInfo.setOrder(order);
        // 获取收银台配置
        gatewayInfo.setConfig(this.getConfig(payOrder.getAppId()));
        // 获取收银台分组和明细配置
        List<CashierGroupConfigResult> groupConfigs = this.getGroupConfigs(payOrder.getAppId(), param.getCashierType(), param.getPayEnvType());
        gatewayInfo.setGroupConfigs(groupConfigs);
        // 判断是否需要显示聚合扫码支付链接 同时满足 pc + 开启显示
        if (Objects.equals(param.getCashierType(), GatewayCashierTypeEnum.PC.getCode())
                &&gatewayInfo.getConfig().getAggregateShow()){
            var config = platformConfigService.getUrlConfig();
            String url = StrUtil.format("{}/cashier/{}", config.getGatewayH5Url(), payOrder.getOrderNo());
            gatewayInfo.setAggregateUrl(url);
        }
        return gatewayInfo;
    }

    /**
     * 获取收银台配置
     */
    private GatewayPayConfigResult getConfig(String appId){
        return gatewayPayConfigService.getGatewayConfig(appId).toResult();
    }

    /**
     * 获取收银台分组配置, 包含明细配置, 根据传入的 payEnvType 进行过滤, 不传返回全部
     * @see GatewayPayEnvTypeEnum payEnvType
     */
    private List<CashierGroupConfigResult> getGroupConfigs(String appId, String cashierType, String payEnvType){
        // 查询类目
        var gatewayConfig = gatewayPayConfigService.getGatewayConfig(appId);
        var groups = cashierConfigService.getCashierGroupConfigs(appId, cashierType, gatewayConfig);
        if (groups.isEmpty()){
            return new ArrayList<>();
        }
        var groupIds = groups.stream()
                .map(MpIdEntity::getId)
                .toList();
        // 查询明细配置, 根据传入支付环境进行过滤后分组
        var itemGroupMap = cashierConfigService.getCashierItemConfigs(groupIds, gatewayConfig).stream()
                .filter(o-> {
                    // 如果传入支付环境不为空, 则进行筛选
                    if (StrUtil.isNotBlank(payEnvType)){
                        return CollUtil.contains(o.getPayEnvTypes(), payEnvType);
                    }
                    return true;
                })
                .collect(Collectors.groupingBy(CashierItemConfig::getGroupId, LinkedHashMap::new, Collectors.toList()));
        // 转换
        return groups.stream()
                .map(o->{
                    var result = CashierGroupConfigConvert.CONVERT.toResult(o);
                    List<CashierItemConfigResult> list = itemGroupMap.getOrDefault(o.getId(), new ArrayList<>())
                            .stream()
                            .map(CashierItemConfigConvert.CONVERT::toResult)
                            .toList();
                    result.setItems(list);
                    return result;
                })
                .toList();
    }
    /**
     * 聚合支付配置
     */
    @IgnoreTenant
    public AggregateOrderAndConfigResult getAggregateConfig(String orderNo, String aggregateType){
        var gatewayInfoResult = new AggregateOrderAndConfigResult();
        // 订单信息
        PayOrder payOrder = gatewayAssistService.getOrderAndCheck(orderNo);
        GatewayOrderResult order = new GatewayOrderResult()
                .setTitle(payOrder.getTitle())
                .setDescription(payOrder.getDescription())
                .setAmount(payOrder.getAmount())
                .setExpiredTime(payOrder.getExpiredTime())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOrderNo(payOrder.getOrderNo());
        gatewayInfoResult.setOrder(order);
        // 获取收银台配置
        gatewayInfoResult.setConfig(this.getConfig(payOrder.getAppId()));
        // 获取聚合支付配置
        var gatewayConfig = gatewayPayConfigService.getGatewayConfig(payOrder.getAppId());
        var aggregateConfig = aggregateConfigService.getAggregatePayConfig(payOrder.getAppId(), aggregateType, gatewayConfig);
        gatewayInfoResult.setAggregateConfig(AggregatePayConfigConvert.CONVERT.toResult(aggregateConfig));
        return gatewayInfoResult;
    }

    /**
     * 查询订单状态
     */
    @IgnoreTenant
    public Boolean findStatusByOrderNo(String orderNo) {
        String status = payOrderManager.findByOrderNo(orderNo)
                .map(PayOrder::getStatus)
                .orElse(null);
        // 如果不是这三类状态则抛出异常
        if (!List.of(PayStatusEnum.WAIT.getCode(),PayStatusEnum.PROGRESS.getCode(),PayStatusEnum.SUCCESS.getCode()).contains(status)){
            throw new DataNotExistException("订单状态异常, 请重新支付! ");
        }
        return Objects.equals(status, PayStatusEnum.SUCCESS.getCode());
    }

    /**
     * 查询订单信息
     */
    @IgnoreTenant
    public GatewayOrderResult findOrderByOrderNo(String orderNo) {
        var payOrder = payOrderManager.findByOrderNo(orderNo).orElseThrow(() -> new DataNotExistException("订单不存在"));
        return new GatewayOrderResult()
                .setTitle(payOrder.getTitle())
                .setDescription(payOrder.getDescription())
                .setAmount(payOrder.getAmount())
                .setExpiredTime(payOrder.getExpiredTime())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOrderNo(payOrder.getOrderNo());
    }


}
