package org.dromara.daxpay.payment.merchant.service.miniapp;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.merchant.dao.appinfo.MchAppInfoManager;
import org.dromara.daxpay.payment.merchant.entity.appinfo.MchAppInfo;
import org.dromara.daxpay.payment.merchant.result.appinfo.MchAppInfoResult;
import org.dromara.daxpay.payment.merchant.result.config.ChannelConfigResult;
import org.dromara.daxpay.payment.merchant.result.info.MerchantInfoResult;
import org.dromara.daxpay.payment.merchant.service.appinfo.MchAppInfoService;
import org.dromara.daxpay.payment.merchant.service.config.ChannelConfigService;
import org.dromara.daxpay.payment.merchant.service.info.MerchantInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 小程序基础服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MiniMchBasicService {
    private final MchAppInfoManager mchAppInfoManager;
    private final MchAppInfoService mchAppInfoService;
    private final MerchantInfoService merchantInfoService;
    private final ChannelConfigService channelConfigService;
    private final PaymentContext apiContext;

    /// 查询商户信息
    public MerchantInfoResult findMchInfo() {
        return merchantInfoService.getMerchant();
    }

    /// 商户应用下拉列表
    public List<LabelValue> dropdownMchApp() {
        return mchAppInfoService.dropdown(apiContext.getTradeInfo().getMchNo());
    }

    /// 查询默认应用
    public MchAppInfoResult findDefaultMchApp() {
        return mchAppInfoManager.findDefaultByMchNo(apiContext.getTradeInfo().getMchNo())
                .map(MchAppInfo::toResult)
                // 商户: 默认应用未配置
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.defaultAppNotConfigured"));
    }

    /// 根据应用ID查询通道配置列表
    public List<ChannelConfigResult> findAllConfigByAppId(String appId) {
        return channelConfigService.findAllByAppId(appId);
    }
}
