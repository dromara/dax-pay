package org.dromara.daxpay.channel.wechat.service.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.convert.config.WechatPayConfigConvert;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.param.config.WechatPayConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.ChannelNotEnableException;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.common.cache.ChannelConfigCacheService;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.entity.config.ChannelConfig;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 微信支付配置
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayConfigService {
    private final ChannelConfigManager channelConfigManager;
    private final ChannelConfigCacheService channelConfigCacheService;
    private final PlatformConfigService platformConfigService;

    /**
     * 查询
     */
    public WechatPayConfigResult findById(Long id) {
        return channelConfigManager.findById(id)
                .map(WechatPayConfig::convertConfig)
                .map(WechatPayConfig::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("微信支付配置不存在"));
    }

    /**
     * 新增或更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(WechatPayConfigParam param){
        if (param.getId() == null){
            this.save(param);
        } else {
            this.update(param);
        }
    }

    /**
     * 添加
     */
    public void save(WechatPayConfigParam param) {
        WechatPayConfig entity = WechatPayConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // 判断商户应用下是否存在该配置
        if (channelConfigManager.existsByAppIdAndChannel(channelConfig.getAppId(), channelConfig.getChannel())){
            throw new DataErrorException("该应用下已存在微信配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新
     */
    public void update(WechatPayConfigParam param){
        ChannelConfig channelConfig = channelConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("微信支付配置不存在"));
        // 通道配置 --转换--> 微信支付配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        WechatPayConfig alipayConfig = WechatPayConfig.convertConfig(channelConfig);
        BeanUtil.copyProperties(param, alipayConfig, CopyOptions.create().ignoreNullValue());
        ChannelConfig channelConfigParam = alipayConfig.toChannelConfig();
        // 手动清空一下默认的数据版本号
        channelConfigParam.setVersion(null);
        BeanUtil.copyProperties(channelConfigParam, channelConfig, CopyOptions.create().ignoreNullValue());
        channelConfigManager.updateById(channelConfig);
    }

    /**
     * 获取支付异步通知地址
     */
    public String getPayNotifyUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{}/wechat/pay",platformInfo.getGatewayServiceUrl(),mchAppInfo.getAppId());
    }

    /**
     * 获取退款异步通知地址
     */
    public String getRefundNotifyUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{/wechat/refund",platformInfo.getGatewayServiceUrl(), mchAppInfo.getAppId());
    }

    /**
     * 转账回调地址
     */
    public String getTransferNotifyUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{}/wechat/transfer",platformInfo.getGatewayServiceUrl(), mchAppInfo.getAppId());
    }


    /**
     * 获取并检查支付配置
     */
    public WechatPayConfig getAndCheckConfig(){
        var payConfig = this.getWechatPayConfig();
        if (!payConfig.getEnable()){
            throw new ChannelNotEnableException("支付宝支付通道未启用");
        }
        return payConfig;
    }

    /**
     * 获取微信支付配置
     */
    public WechatPayConfig getWechatPayConfig(){
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        ChannelConfig channelConfig = channelConfigCacheService.get(mchAppInfo.getAppId(), ChannelEnum.WECHAT.getCode());
        return WechatPayConfig.convertConfig(channelConfig);
    }

    /**
     * wxjava 支付开发包
     */
    public WxPayService wxJavaSdk(WechatPayConfig wechatPayConfig){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(wechatPayConfig.getWxMchId());
        payConfig.setAppId(wechatPayConfig.getWxAppId());
        payConfig.setMchKey(wechatPayConfig.getApiKeyV2());
        payConfig.setApiV3Key(wechatPayConfig.getApiKeyV3());
        payConfig.setPrivateKeyString(wechatPayConfig.getPrivateKey());
        payConfig.setPrivateCertString(wechatPayConfig.getPrivateCert());
        payConfig.setCertSerialNo(wechatPayConfig.getCertSerialNo());
        payConfig.setKeyString(wechatPayConfig.getP12());
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
