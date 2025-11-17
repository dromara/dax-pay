package org.dromara.daxpay.payment.merchant.service.gateway;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.isv.dao.gateway.IsvCheckoutCounterConfigManager;
import org.dromara.daxpay.payment.merchant.convert.gateway.CheckoutCounterConfigConvert;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.gateway.CheckoutCounterManager;
import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.entity.gateway.CheckoutCounterConfig;
import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayReadConfig;
import org.dromara.daxpay.payment.merchant.param.gateway.CheckoutCounterConfigParam;
import org.dromara.daxpay.payment.merchant.result.gateway.CheckoutCounterConfigResult;
import org.dromara.daxpay.payment.unipay.enums.CheckoutCounterTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 收银台配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutCounterConfigService {
    private final CheckoutCounterManager checkoutCounterManager;
    private final IsvCheckoutCounterConfigManager isvCheckoutCounterConfigManager;
    private final MchAppManager mchAppManager;

    /**
     * 获取指定类型收银台分组列表
     */
    public List<CheckoutCounterConfigResult> findAll(String appId, String type){
        return MpUtil.toListResult(checkoutCounterManager.findAllByAppIdAndType(appId,type));
    }

    /**
     * 查询详情
     */
    public CheckoutCounterConfigResult findById(Long id){
        return checkoutCounterManager.findById(id)
                .map(CheckoutCounterConfig::toResult)
                .orElseThrow(() -> new DataNotExistException("收银台配置不存在"));
    }

    /**
     * 保存配置项
     */
    public void save(CheckoutCounterConfigParam param) {
        MchApp mchApp = mchAppManager.findByAppId(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("商户应用不存在"));
        var entity = CheckoutCounterConfig.init(param);
        entity.setMchNo(mchApp.getMchNo())
                .setAppId(mchApp.getAppId())
                .setIsvNo(mchApp.getIsvNo());
        checkoutCounterManager.save(entity);
    }

    /**
     * 更新配置
     */
    public void update(CheckoutCounterConfigParam param) {
        CheckoutCounterConfig checkoutCounterConfig = checkoutCounterManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银台配置不存在"));
        CheckoutCounterConfigConvert.CONVERT.copy(param, checkoutCounterConfig);
        checkoutCounterManager.updateById(checkoutCounterConfig);
    }

    /**
     * 删除配置
     */
    public void delete(Long id) {
        checkoutCounterManager.deleteById(id);
    }

    /**
     * 收银台配置, 根据配置使用自定义或读取服务商配置
     */
    public List<CheckoutCounterConfig> getCheckoutCounterConfigs(String appId, String type, GatewayPayReadConfig readConfig) {
        boolean readSystem = Objects.equals(type, CheckoutCounterTypeEnum.H5.getCode())? readConfig.isH5ReadSystem() : readConfig.isPcReadSystem();
        if (readSystem){
            return isvCheckoutCounterConfigManager.findAllByIsvNoAndType(readConfig.getIsvNo(), type).stream()
                    .map(CheckoutCounterConfigConvert.CONVERT::toEntity)
                    .toList();
        } else {
            return checkoutCounterManager.findAllByAppIdAndType(appId, type);
        }
    }

}
