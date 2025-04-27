package org.dromara.daxpay.service.service.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.GatewayCashierTypeEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.param.gateway.GatewayOrderAndConfigParam;
import org.dromara.daxpay.service.convert.gateway.AggregatePayConfigConvert;
import org.dromara.daxpay.service.convert.gateway.CashierGroupConfigConvert;
import org.dromara.daxpay.service.convert.gateway.CashierItemConfigConvert;
import org.dromara.daxpay.service.convert.gateway.GatewayPayConfigConvert;
import org.dromara.daxpay.service.dao.gateway.*;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.gateway.AggregatePayConfig;
import org.dromara.daxpay.service.entity.gateway.CashierCodeConfig;
import org.dromara.daxpay.service.entity.gateway.CashierItemConfig;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.result.gateway.AggregateOrderAndConfigResult;
import org.dromara.daxpay.service.result.gateway.GatewayCashierCodeConfigResult;
import org.dromara.daxpay.service.result.gateway.GatewayOrderAndConfigResult;
import org.dromara.daxpay.service.result.gateway.GatewayOrderResult;
import org.dromara.daxpay.service.result.gateway.config.CashierGroupConfigResult;
import org.dromara.daxpay.service.result.gateway.config.CashierItemConfigResult;
import org.dromara.daxpay.service.result.gateway.config.GatewayPayConfigResult;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
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
    private final GatewayPayConfigManager gatewayPayConfigManager;
    private final CashierGroupConfigManager cashierGroupConfigManager;
    private final CashierItemConfigManager gatewayItemConfigManager;
    private final AggregatePayConfigManager aggregatePayConfigManager;
    private final GatewayPayAssistService gatewayAssistService;
    private final PayOrderManager payOrderManager;
    private final CashierCodeConfigManager cashierCodeConfigManager;
    private final CashierCodeItemConfigManager cashierCodeItemConfigManager;
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

        GatewayOrderResult order = new GatewayOrderResult()
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
        gatewayInfo.setGroupConfigs(this.getGroupConfigs(payOrder.getAppId(), param.getCashierType(), param.getPayEnvType()));
        // 判断是否需要显示聚合扫码支付链接 同时满足 pc + 开启显示
        if (Objects.equals(param.getCashierType(), GatewayCashierTypeEnum.PC.getCode())
                &&gatewayInfo.getConfig().getAggregateShow()){
            var config = platformConfigService.getConfig();
            String url = StrUtil.format("{}/cashier/{}", config.getGatewayMobileUrl(), payOrder.getOrderNo());
            gatewayInfo.setAggregateUrl(url);
        }
        return gatewayInfo;
    }

    /**
     * 根据码牌编码和类型查询信息和配置
     */
    @IgnoreTenant
    public GatewayCashierCodeConfigResult getCashierCodeConfig(String cashierCode, String cashierType) {
        CashierCodeConfig codeConfig = cashierCodeConfigManager.findByCode(cashierCode)
                .orElseThrow(() -> new DataNotExistException("支付码牌不存在"));
        // 是否启用
        if (!codeConfig.getEnable()) {
            throw new ConfigNotEnableException("支付码牌已禁用");
        }
        var codeItemConfig = cashierCodeItemConfigManager.findByCodeAndType(codeConfig.getId(), cashierType)
                .orElseThrow(() -> new DataNotExistException("收银码牌配置不存在"));
        var result = new GatewayCashierCodeConfigResult();
        BeanUtil.copyProperties(codeItemConfig, result);
        return result.setName(codeConfig.getName());
    }


    /**
     * 获取收银台配置
     */
    private GatewayPayConfigResult getConfig(String appId){
        // 查询配置
        return gatewayPayConfigManager.findByAppId(appId)
                .map(GatewayPayConfigConvert.CONVERT::toResult)
                .orElse(new GatewayPayConfigResult());
    }

    /**
     * 获取收银台分组配置, 包含明细配置, 根据传入的 payEnvType 进行过滤, 不传返回全部
     */
    private List<CashierGroupConfigResult> getGroupConfigs(String appId, String cashierType, String payEnvType){
        // 查询类目
        var groups = cashierGroupConfigManager.findAllByAppIdAndType(appId, cashierType);
        if (groups.isEmpty()){
            return new ArrayList<>();
        }
        var groupIds = groups.stream()
                .map(MpIdEntity::getId)
                .toList();
        // 查询明细配置, 根据传入支付环境进行过滤后分组
        var itemGroupMap = gatewayItemConfigManager.findAllByGroupIds(groupIds).stream()
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
        AggregatePayConfig aggregateConfig = aggregatePayConfigManager.findByAppIdAndType(payOrder.getAppId(), aggregateType)
                .orElseThrow(() -> new DataNotExistException("聚合支付配置不存在"));
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
