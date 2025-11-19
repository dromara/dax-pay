package org.dromara.daxpay.channel.wechat.service.config;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.channel.wechat.convert.WechatPayConfigConvert;
import org.dromara.daxpay.channel.wechat.dao.config.WechatPayConfigManager;
import org.dromara.daxpay.channel.wechat.dao.config.WechatPaySubConfigManager;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfigEntity;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPaySubConfig;
import org.dromara.daxpay.channel.wechat.enums.WechatAuthTypeEnum;
import org.dromara.daxpay.channel.wechat.param.config.WechatPayConfigParam;
import org.dromara.daxpay.channel.wechat.param.config.WechatPaySubConfigParam;
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
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
    private final WechatIsvConfigService wechatIsvConfigService;
    private final WechatPaySubConfigManager wechatPaySubConfigManager;
    private final WechatPayConfigManager wechatPayConfigManager;
    private final OnbMchInfoService onbMchInfoService;
    private final MchAppManager mchAppManager;

    /**
     * 查询
     */
    public WechatPayConfigEntity findByAppId(String appId) {
        var optional = wechatPayConfigManager.findByAppId(appId);
        if (optional.isEmpty()){
            MchApp mchApp = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new DataNotExistException("商户应用不存在"));
            var payConfig = new WechatPayConfigEntity();
            payConfig.setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            var channelConfig = new ChannelConfig();
            channelConfig.setChannel(ChannelEnum.WECHAT.getCode())
                    .setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            channelConfigManager.save(channelConfig);
            wechatPayConfigManager.save(payConfig);
            return payConfig;
        }
        return optional.get();
    }

    /**
     * 查询特约商户信息
     */
    public WechatPaySubConfig findSubByAppId(String appId) {
        var optional = wechatPaySubConfigManager.findByAppId(appId);
        if (optional.isEmpty()){
            MchApp mchApp = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new DataNotExistException("商户应用不存在"));
            var subConfig = new WechatPaySubConfig();
            subConfig.setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            var channelConfig = new ChannelConfig();
            channelConfig.setChannel(ChannelEnum.WECHAT_ISV.getCode())
                    .setAppId(appId)
                    .setMchNo(mchApp.getMchNo())
                    .setIsvNo(mchApp.getIsvNo());
            channelConfigManager.save(channelConfig);
            wechatPaySubConfigManager.save(subConfig);
            return subConfig;
        }
        return optional.get();
    }


    /**
     * 更新配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(WechatPayConfigParam param){
        var payConfig = this.findByAppId(param.getAppId());
        // 权限校验
        if (!onbMchInfoService.existsByOnbMchNo(param.getWxMchId(), payConfig.getMchNo(), ChannelEnum.WECHAT.getCode())) {
            throw new OperationFailException("你没有设置该子商户的权限!");
        }
        ChannelConfig channelConfig = channelConfigManager.findByAppIdAndChannel(param.getAppId(), ChannelEnum.WECHAT.getCode())
                .orElseThrow(() -> new ConfigNotEnableException("微信支付配置不存在"));
        channelConfig.setEnable(param.getEnable());
        WechatPayConfigConvert.CONVERT.copy(param, payConfig);
        channelConfigManager.updateById(channelConfig);
        wechatPayConfigManager.updateById(payConfig);
    }


    /**
     * 更新子商户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSub(WechatPaySubConfigParam param) {
        var subConfig = this.findSubByAppId(param.getAppId());
        // 判断是否有权限选择该子商户
        if (!onbMchInfoService.existsByOnbMchNo(param.getSubMchId(), subConfig.getMchNo(), ChannelEnum.WECHAT_ISV.getCode())){
            throw new OperationFailException("你没有设置该微信子商户的权限!");
        }
        ChannelConfig channelConfig = channelConfigManager.findByAppIdAndChannel(param.getAppId(), ChannelEnum.WECHAT_ISV.getCode())
                .orElseThrow(() -> new ConfigNotEnableException("微信子商户配置不存在"));
        channelConfig.setEnable(param.getEnable());
        WechatPayConfigConvert.CONVERT.copy(param, subConfig);
        channelConfigManager.updateById(channelConfig);
        wechatPaySubConfigManager.updateById(subConfig);
    }

    /**
     * 获取并检查支付配置
     */
    public WechatPayConfig getAndCheckConfig(boolean isv){
        var payConfig = this.getWechatPayConfig(isv);
        if (!payConfig.isEnable()){
            throw new ChannelNotEnableException("商户或服务商微信支付通道未启用");
        }
        return payConfig;
    }

    /**
     * 获取微信支付配置
     */
    public WechatPayConfig getWechatPayConfig(boolean isv){
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        // 判断是特约商户还是正常商户
        if (isv) {
            // 子商户
            var wechatPaySubConfig = findSubByAppId(reqInfo.getAppId());
            // 获取微信服务商配置
            var wechatIsvConfig = wechatIsvConfigService.findByIsvNo(wechatPaySubConfig.getIsvNo());
            // 合并微信服务商配置和微信特约商户配置, 生成普通商户配置
            var wechatPayConfig = new WechatPayConfig();
            BeanUtil.copyProperties(wechatIsvConfig, wechatPayConfig, CopyOptions.create().ignoreNullValue());
            wechatPayConfig.setSubAppId(wechatPaySubConfig.getSubAppId())
                    .setSubMchId(wechatPaySubConfig.getSubMchId())
                    .setMchNo(wechatPaySubConfig.getMchNo())
                    .setAppId(wechatPaySubConfig.getAppId());
            // 使用二级商户微信认证配置
            if (Objects.equals(wechatPayConfig.getAuthType(), WechatAuthTypeEnum.SUB.getCode())){
                wechatPayConfig.setAuthUrl(wechatPaySubConfig.getWxAuthUrl())
                        .setAppSecret(wechatPaySubConfig.getWxAppSecret());
            }
            // 同时启用才可以使用
            wechatPayConfig.setEnable(wechatPaySubConfig.isEnable()&&wechatIsvConfig.isEnable());
            return wechatPayConfig;
        } else {
            var wechatPayConfig = findByAppId(reqInfo.getAppId());
            return WechatPayConfigConvert.CONVERT.toConfig(wechatPayConfig);
        }
    }

    /**
     * wxjava 支付开发包
     */
    public WxPayService wxJavaSdk(WechatPayConfig wechatPayConfig){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(wechatPayConfig.getWxMchId());
        payConfig.setAppId(wechatPayConfig.getWxAppId());
        payConfig.setSubMchId(wechatPayConfig.getSubMchId());
        payConfig.setSubAppId(wechatPayConfig.getSubAppId());
        payConfig.setMchKey(wechatPayConfig.getApiKeyV2());
        payConfig.setApiV3Key(wechatPayConfig.getApiKeyV3());
        // 注意不要使用base64的方式进行配置, 因为wxjava 是直接读取文本并不会进行解码, 会导致证书异常
        if (StrUtil.isNotBlank(wechatPayConfig.getPublicKey())){
            payConfig.setPublicKeyContent(Base64.decode(wechatPayConfig.getPublicKey()));
        }
        payConfig.setPublicKeyId(wechatPayConfig.getPublicKeyId());
        if (StrUtil.isNotBlank(wechatPayConfig.getPrivateCert())){
            payConfig.setPrivateCertContent(Base64.decode(wechatPayConfig.getPrivateCert()));
        }
        if (StrUtil.isNotBlank(wechatPayConfig.getPrivateKey())){
            payConfig.setPrivateKeyContent(Base64.decode(wechatPayConfig.getPrivateKey()));
        }
        payConfig.setCertSerialNo(wechatPayConfig.getCertSerialNo());
        if (StrUtil.isNotBlank(wechatPayConfig.getP12())){
            payConfig.setKeyContent(Base64.decode(wechatPayConfig.getP12()));
        }
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 获取支付异步通知地址
     */
    public String getPayNotifyUrl(boolean isv) {
        String url = isv ? "{}/unipay/callback/{}/{}/wechat/isv/pay":"{}/unipay/callback/{}/{}/wechat/pay";
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        return StrUtil.format(url,reqInfo.getGatewayServiceUrl(), reqInfo.getMchNo(),reqInfo.getAppId());
    }

    /**
     * 获取退款异步通知地址
     */
    public String getRefundNotifyUrl(boolean isv) {
        String url = isv ? "{}/unipay/callback/{}/{}/wechat/isv/refund":"{}/unipay/callback/{}/{}/wechat/refund";
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        return StrUtil.format(url,reqInfo.getGatewayServiceUrl(), reqInfo.getMchNo(),reqInfo.getAppId());
    }

    /**
     * 转账回调地址
     */
    public String getTransferNotifyUrl(boolean isv) {
        String url = isv ? "{}/unipay/callback/{}/{}/wechat/isv/transfer":"{}/unipay/callback/{}/{}/wechat/transfer";
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        return StrUtil.format(url,reqInfo.getGatewayServiceUrl(), reqInfo.getMchNo(),reqInfo.getAppId());
    }

}
