package org.dromara.daxpay.payment.miniapp.service;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.local.MchContextLocal;
import org.dromara.daxpay.payment.merchant.result.app.MchAppResult;
import org.dromara.daxpay.payment.merchant.result.config.ChannelConfigResult;
import org.dromara.daxpay.payment.merchant.result.info.MerchantResult;
import org.dromara.daxpay.payment.merchant.service.app.MchAppService;
import org.dromara.daxpay.payment.merchant.service.config.ChannelConfigService;
import org.dromara.daxpay.payment.merchant.service.info.MerchantInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小程序基础服务
 * @author xxm
 * @since 2025/4/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MiniMchBasicService {
    private final MchAppManager mchAppManager;
    private final MchAppService mchAppService;
    private final MerchantInfoService merchantInfoService;
    private final ChannelConfigService channelConfigService;

    /**
     * 查询商户信息
     */
    public MerchantResult findMchInfo() {
        return merchantInfoService.getMerchant();
    }

    /**
     * 商户应用下拉列表
     */
    public List<LabelValue> dropdownMchApp() {
        return mchAppService.dropdown(MchContextLocal.getMchNo());
    }

    /**
     * 查询默认应用
     */
    public MchAppResult findDefaultMchApp() {
        return mchAppManager.findDefaultByMchNo(MchContextLocal.getMchNo())
                .map(MchApp::toResult)
                .orElseThrow(() -> new DataNotExistException("商户未配置默认应用"));
    }

    /**
     * 根据应用ID查询通道配置列表
     */
    public List<ChannelConfigResult> findAllConfigByAppId(String appId) {
        return channelConfigService.findAllByAppId(appId);
    }
}
