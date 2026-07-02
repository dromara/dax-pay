package cn.daxpay.open.payment.merchant.service.miniapp;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.payment.common.runtime.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.payment.merchant.result.config.ChannelConfigResult;
import cn.daxpay.open.payment.merchant.result.info.MerchantInfoResult;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.payment.merchant.service.config.ChannelConfigService;
import cn.daxpay.open.payment.merchant.service.info.MerchantInfoService;
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
    private final PaymentContext paymentContext;

    /// 查询商户信息
    public MerchantInfoResult findMchInfo() {
        return merchantInfoService.getMerchant();
    }

    /// 商户应用下拉列表
    public List<LabelValue> dropdownMchApp() {
        return mchAppInfoService.dropdown(paymentContext.getMchNo());
    }

    /// 查询默认应用
    public MchAppInfoResult findDefaultMchApp() {
        return mchAppInfoManager.findDefaultByMchNo(paymentContext.getMchNo())
                .map(MchAppInfo::toResult)
                // 商户: 默认应用未配置
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.defaultAppNotConfigured"));
    }

    /// 根据应用ID查询通道配置列表
    public List<ChannelConfigResult> findAllConfigByAppId(String appId) {
        return channelConfigService.findAllByAppId(appId);
    }
}
