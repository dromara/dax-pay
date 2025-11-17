package org.dromara.daxpay.channel.alipay.service.config;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.util.CertUtil;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.config.AlipayConfigManager;
import org.dromara.daxpay.channel.alipay.dao.config.AlipaySubConfigManager;
import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.entity.config.AlipaySubConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayConfigParam;
import org.dromara.daxpay.channel.alipay.param.config.AlipaySubConfigParam;
import org.dromara.daxpay.payment.common.context.PaymentReqInfoLocal;
import org.dromara.daxpay.payment.common.exception.ChannelNotEnableException;
import org.dromara.daxpay.payment.common.exception.ConfigNotEnableException;
import org.dromara.daxpay.payment.common.exception.OperationFailException;
import org.dromara.daxpay.payment.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.payment.merchant.dao.config.ChannelConfigManager;
import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.service.onboarded.OnbMchInfoService;
import org.dromara.daxpay.payment.pay.entity.config.ChannelConfig;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.local.PaymentContextLocal;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.*;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/11/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayConfigService {
    private final ChannelConfigManager channelConfigManager;
    private final AlipayIsvConfigService alipayIsvConfigService;
    private final AlipaySubConfigManager alipaySubConfigManager;
    private final OnbMchInfoService onbMchInfoService;
    private final MchAppManager mchAppManager;
    private final AlipayConfigManager alipayConfigManager;

    /**
     * 查询配置
     */
    public AliPayConfig findByAppId(String appId) {
        var optional = alipayConfigManager.findByAppId(appId);
        if (optional.isEmpty()){
            MchApp mchApp = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new DataNotExistException("商户应用不存在"));
            var payConfig = new AliPayConfig();
            payConfig.setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            var channelConfig = new ChannelConfig();
            channelConfig.setChannel(ChannelEnum.ALIPAY.getCode())
                    .setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            channelConfigManager.save(channelConfig);
            alipayConfigManager.save(payConfig);
            return payConfig;
        }
        return optional.get();
    }

    /**
     * 子商户配置
     */
    public AlipaySubConfig findSubByAppId(String appId){
        var optional = alipaySubConfigManager.findByAppId(appId);
        if (optional.isEmpty()){
            MchApp mchApp = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new DataNotExistException("商户应用不存在"));
            var subConfig = new AlipaySubConfig();
            subConfig.setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            var channelConfig = new ChannelConfig();
            channelConfig.setChannel(ChannelEnum.ALIPAY_ISV.getCode())
                    .setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            channelConfigManager.save(channelConfig);
            alipaySubConfigManager.save(subConfig);
            return subConfig;
        }
        return optional.get();
    }

    /**
     * 更新配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(AlipayConfigParam param){
        var payConfig = this.findByAppId(param.getAppId());
        // 权限校验
        if (!onbMchInfoService.existsByOnbMchNo(param.getAliAppId(), payConfig.getMchNo(), ChannelEnum.ALIPAY.getCode())) {
            throw new OperationFailException("你没有设置该子商户的权限!");
        }
        ChannelConfig channelConfig = channelConfigManager.findByAppIdAndChannel(param.getAppId(), ChannelEnum.ALIPAY.getCode())
                .orElseThrow(() -> new ConfigNotEnableException("支付宝配置不存在"));


        channelConfig.setEnable(param.getEnable());
        AlipayConfigConvert.CONVERT.copy(param, payConfig);
        channelConfigManager.updateById(channelConfig);
        alipayConfigManager.updateById(payConfig);
    }

    /**
     * 更新子商户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSub(AlipaySubConfigParam param) {
        var subConfig = this.findSubByAppId(param.getAppId());
        // 判断是否有权限选择该子商户
        if (!onbMchInfoService.existsByOnbMchNo(param.getAppAuthToken(), subConfig.getMchNo(), ChannelEnum.ALIPAY_ISV.getCode())){
            throw new OperationFailException("你没有设置该支付宝子商户的权限!");
        }
        ChannelConfig channelConfig = channelConfigManager.findByAppIdAndChannel(param.getAppId(), ChannelEnum.ALIPAY_ISV.getCode())
                .orElseThrow(() -> new ConfigNotEnableException("支付宝子商户配置不存在"));
        channelConfig.setEnable(param.getEnable());
        AlipayConfigConvert.CONVERT.copy(param, subConfig);
        channelConfigManager.updateById(channelConfig);
        alipaySubConfigManager.updateById(subConfig);
    }

    /**
     * 获取并检查支付配置
     */
    public AliPayConfig getAndCheckConfig(boolean isv){
        var payConfig = this.getAliPayConfig(isv);
        if (!payConfig.getEnable()){
            throw new ChannelNotEnableException("支付宝通道未启用");
        }
        return payConfig;
    }

    /**
     * 获取支付宝配置
     */
    public AliPayConfig getAliPayConfig(boolean isv){
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        // 判断是特约商户还是正常商户
        if (isv) {
            // 子商户
            var alipaySubConfig = this.findSubByAppId(reqInfo.getAppId());
            // 获取支付宝服务商配置
            var alipayIsvConfig = alipayIsvConfigService.findByIsvNo(alipaySubConfig.getIsvNo());
            // 合并支付宝服务商配置和支付宝特约商户配置, 生成普通商户配置
            var alipayConfig = new AliPayConfig();
            BeanUtil.copyProperties(alipayIsvConfig, alipayConfig, CopyOptions.create().ignoreNullValue());
            alipayConfig.setAppAuthToken(alipaySubConfig.getAppAuthToken())
                    .setEnable(alipaySubConfig.isEnable());
            // 同时启用才可以使用
            alipayConfig.setEnable(alipaySubConfig.isEnable() && alipayIsvConfig.isEnable());
            return alipayConfig;
        } else {
            return findByAppId(reqInfo.getAppId());
        }
    }

    /**
     * 获取异步通知地址
     */
    public String getNotifyUrl(boolean isv) {
        String url = isv?"{}/unipay/callback/{}/{}/alipay/isv":"{}/unipay/callback/{}/{}/alipay";
        var reqInfo = PaymentContextLocal.get().getReqInfo();
        return StrUtil.format(url,reqInfo.getGatewayServiceUrl(), reqInfo.getMchNo(),reqInfo.getAppId());
    }

    /**
     * 获取同步通知地址
     */
    public String getReturnUrl(boolean isv) {
        String url = isv?"{}/unipay/return/{}/{}/alipay/isv":"{}/unipay/return/{}/{}/alipay";
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        return StrUtil.format(url,reqInfo.getGatewayServiceUrl(), reqInfo.getMchNo(),reqInfo.getAppId());
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
        String callReq = JacksonUtil.toJson(params);
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
