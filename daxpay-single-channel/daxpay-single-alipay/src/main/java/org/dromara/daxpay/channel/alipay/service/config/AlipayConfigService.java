package org.dromara.daxpay.channel.alipay.service.config;

import cn.bootx.platform.core.util.CertUtil;
import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.*;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayConfigConvert;
import org.dromara.daxpay.channel.alipay.entity.AliPayConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayConfigParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayConfigResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.ChannelNotEnableException;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.common.cache.ChannelConfigCacheService;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.dao.merchant.MchAppManager;
import org.dromara.daxpay.service.entity.config.ChannelConfig;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayConfigService {
    private final ChannelConfigManager channelConfigManager;
    private final ChannelConfigCacheService channelConfigCacheService;
    private final PlatformConfigService platformConfigService;
    private final MchAppManager mchAppManager;

    /**
     * 查询
     */
    public AlipayConfigResult findById(Long id) {
        return channelConfigManager.findById(id)
                .map(AliPayConfig::convertConfig)
                .map(AliPayConfig::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("支付宝商户配置不存在"));
    }


    /**
     * 新增或更新商户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(AlipayConfigParam param){
        if (param.getId() == null){
            this.save(param);
        } else {
            this.update(param);
        }
    }

    /**
     * 添加商户信息
     */
    public void save(AlipayConfigParam param) {
        var entity = AlipayConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // 判断商户和应用下是否存在该配置
        if (channelConfigManager.existsByAppIdAndChannel(param.getAppId(), channelConfig.getChannel())){
            throw new DataErrorException("该应用下已存在支付宝配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新
     */
    public void update(AlipayConfigParam param){
        ChannelConfig channelConfig = channelConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("支付宝配置不存在"));
        // 通道配置 --转换--> 支付宝配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        AliPayConfig alipayConfig = AliPayConfig.convertConfig(channelConfig);
        BeanUtil.copyProperties(param, alipayConfig, CopyOptions.create().ignoreNullValue());
        ChannelConfig channelConfigParam = alipayConfig.toChannelConfig();
        // 手动清空一下默认的数据版本号
        channelConfigParam.setVersion(null);
        BeanUtil.copyProperties(channelConfigParam, channelConfig, CopyOptions.create().ignoreNullValue());
        channelConfigManager.updateById(channelConfig);
    }


    /**
     * 获取异步通知地址
     */
    public String getNotifyUrl(boolean isv) {
        String url = isv?"{}/unipay/callback/{}/alipay/isv":"{}/unipay/callback/{}/alipay";
        var mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format(url,platformInfo.getGatewayServiceUrl(), mchAppInfo.getAppId());
    }

    /**
     * 获取同步通知地址
     */
    public String getReturnUrl(boolean isv) {
        String url = isv?"{}/unipay/return/{}/alipay/isv":"{}/unipay/return/{}/alipay";
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format(url,platformInfo.getGatewayServiceUrl(), mchAppInfo.getAppId());
    }

    /**
     * 获取支付宝支付配置
     */
    public AliPayConfig getAliPayConfig(boolean isv){
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        ChannelConfig channelConfig = channelConfigCacheService.get(mchAppInfo.getAppId(), isv?ChannelEnum.ALIPAY_ISV.getCode():ChannelEnum.ALIPAY.getCode());
        return AliPayConfig.convertConfig(channelConfig);
    }

    /**
     * 获取并检查支付配置
     */
    public AliPayConfig getAndCheckConfig(boolean isv) {
        var payConfig = this.getAliPayConfig(isv);
        if (!payConfig.getEnable()){
            throw new ChannelNotEnableException("支付宝支付通道未启用");
        }
        return payConfig;
    }

    /**
     * 获取支付宝SDK的配置
     */
    @SneakyThrows
    public AlipayClient getAlipayClient(AliPayConfig aliPayConfig){
        AlipayConfig config = new AlipayConfig();
        config.setAppId(aliPayConfig.getAliAppId());
        config.setFormat("json");
        config.setCharset("UTF-8");
        config.setSignType(aliPayConfig.getSignType());
        // 证书
        if (Objects.equals(aliPayConfig.getAuthType(), AlipayCode.AuthType.AUTH_TYPE_CART)){
            config.setPrivateKey(aliPayConfig.getPrivateKey());
            config.setAppCertContent(aliPayConfig.getAppCert());
            config.setRootCertContent(aliPayConfig.getAlipayRootCert());
            config.setAlipayPublicCertContent(aliPayConfig.getAlipayCert());
        } else {
            // 公钥
            config.setPrivateKey(aliPayConfig.getPrivateKey());
            config.setAlipayPublicKey(aliPayConfig.getAlipayPublicKey());
        }
        // 沙箱
        if (aliPayConfig.isSandbox()){
            config.setServerUrl(AlipayCode.ServerUrl.SANDBOX);
        } else {
            config.setServerUrl(AlipayCode.ServerUrl.PRODUCTION);
        }
        return new DefaultAlipayClient(config);
    }

    /**
     * 校验消息通知
     */
    public boolean verifyNotify(Map<String, String> params, boolean isv) {
        String callReq = JsonUtil.toJsonStr(params);
        log.info("支付宝消息通知报文: {}", callReq);
        String appId = params.get("app_id");
        if (StrUtil.isBlank(appId)) {
            log.error("支付宝消息通知报文appId为空");
            return false;
        }
        AliPayConfig alipayConfig = this.getAliPayConfig(isv);
        if (Objects.isNull(alipayConfig)) {
            log.error("支付宝支付配置不存在");
            return false;
        }
        // 根据认证类型使用证书或公钥验签
        try {
            if (Objects.equals(alipayConfig.getAuthType(), AlipayCode.AuthType.AUTH_TYPE_KEY)) {
                return AlipaySignature.verifyV1(params, alipayConfig.getAlipayPublicKey(), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
            else {
                return AlipaySignature.verifyV1(params, CertUtil.getCertByContent(alipayConfig.getAlipayCert()), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验签失败", e);
            return false;
        }
    }


    /**
     * 接口调用
     */
    public <T extends AlipayResponse> T execute(AlipayRequest<T> request, AliPayConfig aliPayConfig) throws AlipayApiException {
        AlipayClient alipayClient = this.getAlipayClient(aliPayConfig);
        if (Objects.equals(aliPayConfig.getAuthType(), AlipayCode.AuthType.AUTH_TYPE_CART)){
            return alipayClient.certificateExecute(request);
        } else {
            return alipayClient.execute(request);
        }
    }
}
