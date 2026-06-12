package org.dromara.daxpay.payment.merchant.service.config;

import org.dromara.daxpay.payment.merchant.dao.config.MchProductConfigManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantInfoManager;
import org.dromara.daxpay.payment.merchant.entity.config.MchProductConfig;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantInfo;
import org.dromara.daxpay.payment.merchant.param.config.MchProductConfigBatchParam;
import org.dromara.daxpay.payment.merchant.param.config.MchProductConfigEnableParam;
import org.dromara.daxpay.payment.merchant.result.config.MchProductConfigResult;
import org.dromara.daxpay.payment.pay.service.masterdata.product.PayProductMasterDataService;
import org.dromara.daxpay.platform.common.mybatisplus.function.CollectorsFunction;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 商户产品配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchProductConfigService {

    private final MchProductConfigManager productConfigManager;

    private final PayProductMasterDataService PayProductMasterDataService;

    private final MerchantInfoManager merchantInfoManager;

    /// 产品配置列表, 根据商户进行查询, 默认返回所有产品配置, 未配置的产品默认启用状态为false
    public List<MchProductConfigResult> findAllByMchNo(String mchNo){
        Map<String, MchProductConfig> productConfigMap = productConfigManager.findByMchNo(mchNo)
                .stream()
                .collect(Collectors.toMap(MchProductConfig::getProduct, Function.identity(), CollectorsFunction::retainFirst));
        var productList = PayProductMasterDataService.listAllByApply();
        MerchantInfo merchantInfo = merchantInfoManager.findByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.merchantNotExist"));
        return productList.stream().map(o -> {
            var productConfig = productConfigMap.get(o.getCode());
            if (Objects.isNull(productConfig)) {
                return new MchProductConfigResult()
                        .setProduct(o.getCode())
                        .setName(o.getName())
                        .setChannel(o.getChannel())
                        .setChannelName(o.getChannelName())
                        .setMchNo(merchantInfo.getMchNo())
                        .setEnable(false);
            }
            return productConfig.toResult()
                    .setName(o.getName())
                    .setChannel(o.getChannel())
                    .setChannelName(o.getChannelName())
                    .setMchNo(merchantInfo.getMchNo());
        }).collect(Collectors.toList());
    }

    /// 更新启用状态，如果配置不存在则自动创建
    @Transactional(rollbackFor = Exception.class)
    public void updateEnable(MchProductConfigEnableParam param) {
        Optional<MchProductConfig> optional = productConfigManager
                .findByMchNoAndProduct(param.getMchNo(), param.getProduct());
        if (optional.isPresent()) {
            MchProductConfig config = optional.get();
            config.setEnable(param.getEnable());
            productConfigManager.updateById(config);
        } else {
            MchProductConfig config = new MchProductConfig();
            config.setMchNo(param.getMchNo());
            config.setProduct(param.getProduct());
            config.setChannel(param.getChannel());
            config.setEnable(param.getEnable());
            productConfigManager.save(config);
        }
    }

    /// 批量保存商户产品配置
    /// 已有的配置更新启用状态，没有的则新增
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(MchProductConfigBatchParam param) {
        String mchNo = param.getMchNo();
        for (var item : param.getItems()) {
            Optional<MchProductConfig> existingConfig = productConfigManager.findByMchNoAndProduct(mchNo, item.getProduct());
            if (existingConfig.isPresent()) {
                MchProductConfig config = existingConfig.get();
                config.setEnable(item.getEnable());
                productConfigManager.updateById(config);
            }
            else {
                MchProductConfig config = new MchProductConfig();
                config.setMchNo(mchNo);
                config.setProduct(item.getProduct());
                config.setChannel(item.getChannel());
                config.setEnable(item.getEnable());
                productConfigManager.save(config);
            }
        }
    }
}

