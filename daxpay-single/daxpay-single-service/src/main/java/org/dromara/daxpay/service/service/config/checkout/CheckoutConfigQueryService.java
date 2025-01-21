package org.dromara.daxpay.service.service.config.checkout;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutAggregateConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutGroupConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutItemConfigManager;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutAggregateConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutGroupConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutItemConfig;
import org.dromara.daxpay.service.result.config.checkout.CheckoutAggregateConfigVo;
import org.dromara.daxpay.service.result.config.checkout.CheckoutConfigVo;
import org.dromara.daxpay.service.result.config.checkout.CheckoutGroupConfigVo;
import org.dromara.daxpay.service.result.config.checkout.CheckoutItemConfigVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收银台配置查询服务
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutConfigQueryService {
    private final CheckoutConfigManager checkoutConfigManager;
    private final CheckoutGroupConfigManager checkoutGroupConfigManager;
    private final CheckoutItemConfigManager checkoutItemConfigManager;
    private final CheckoutAggregateConfigManager checkoutAggregateConfigManager;

    /**
     * 收银台配置
     */
    public CheckoutConfigVo getConfig(String appId){
        return checkoutConfigManager.findByAppId(appId).map(CheckoutConfig::toResult)
                .orElse(new CheckoutConfigVo());
    }

    /**
     * 获取指定类型收银台分组列表
     */
    public List<CheckoutGroupConfigVo> getGroupConfigs(String appId, String checkoutType){
        return MpUtil.toListResult(checkoutGroupConfigManager.findAllSortByAppIdAndType(appId, checkoutType));
    }

    /**
     * 获取收银台分组配置
     */
    public CheckoutGroupConfigVo getGroupConfig(Long id){
        return checkoutGroupConfigManager.findById(id).map(CheckoutGroupConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("收银台分组配置不存在"));
    }

    /**
     * 获取配置项列表
     */
    public List<CheckoutItemConfigVo> getItemConfigs(Long groupId){
        return MpUtil.toListResult(checkoutItemConfigManager.findAllByGroupId(groupId));
    }

    /**
     * 获取配置项列表
     */
    public CheckoutItemConfigVo getItemConfig(Long groupId){
        return checkoutItemConfigManager.findById(groupId).map(CheckoutItemConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("收银台配置项不存在"));
    }

    /**
     * 获取聚合支付配置列表
     */
    public List<CheckoutAggregateConfigVo> getAggregateConfigs(String appId){
        return MpUtil.toListResult(checkoutAggregateConfigManager.findAllByAppId(appId));
    }

    /**
     * 获取聚合支付配置
     */
    public CheckoutAggregateConfigVo getAggregateConfig(Long id){
        return checkoutAggregateConfigManager.findById(id).map(CheckoutAggregateConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("聚合支付配置不存在"));
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByAppIdAndType(String appId, String type){
        return checkoutAggregateConfigManager.existsByAppIdAndType(appId, type);
    }

    /**
     * 根据应用ID和类型查询配置是否存在
     */
    public boolean existsByAppIdAndType(String appId, String type, Long id){
        return checkoutAggregateConfigManager.existsByAppIdAndType(appId, type, id);
    }
}
