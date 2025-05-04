package org.dromara.daxpay.channel.wechat.service.payment.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.convert.WechatPayConfigConvert;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.param.config.WechatPayConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.dromara.daxpay.core.context.MchAppLocal;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.MerchantTypeEnum;
import org.dromara.daxpay.core.exception.ChannelNotEnableException;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.common.cache.ChannelConfigCacheService;
import org.dromara.daxpay.service.common.local.MchContextLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.entity.config.ChannelConfig;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
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
    private final ChannelConfigCacheService channelConfigCacheService;
    private final PlatformConfigService platformConfigService;
    private final PaymentAssistService paymentAssistService;

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
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        // 判断商户配置类型
        if (Objects.equals(mchApp.getMchType(), MerchantTypeEnum.PARTNER.getCode())){
            throw new ChannelNotEnableException("请使用特约商户配置保存");
        }
        WechatPayConfig entity = WechatPayConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // 判断商户和应用下是否存在该配置
        if (channelConfigManager.existsByAppIdAndChannel(channelConfig.getAppId(), channelConfig.getChannel())){
            throw new DataErrorException("该应用下已存在微信配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新
     */
    public void update(WechatPayConfigParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        // 判断商户配置类型
        if (Objects.equals(mchApp.getMchType(), MerchantTypeEnum.PARTNER.getCode())){
            throw new ChannelNotEnableException("请使用特约商户配置更新");
        }
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
    public String getPayNotifyUrl(boolean isv) {
        String url = isv ? "{}/unipay/callback/{}/wechat/isv/pay":"{}/unipay/callback/{}/wechat/pay";
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        return StrUtil.format(url,mchAppInfo.getGatewayServiceUrl(),mchAppInfo.getAppId());
    }

    /**
     * 获取退款异步通知地址
     */
    public String getRefundNotifyUrl(boolean isv) {
        String url = isv ? "{}/unipay/callback/{}/wechat/isv/refund":"{}/unipay/callback/{}/wechat/refund";
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        return StrUtil.format(url,mchAppInfo.getGatewayServiceUrl(),mchAppInfo.getAppId());
    }

    /**
     * 转账回调地址
     */
    public String getTransferNotifyUrl(boolean isv) {
        String url = isv ? "{}/unipay/callback/{}/wechat/isv/transfer":"{}/unipay/callback/{}/wechat/transfer";
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        return StrUtil.format(url,mchAppInfo.getGatewayServiceUrl(),mchAppInfo.getAppId());
    }

    /**
     * 获取并检查支付配置
     */
    public WechatPayConfig getAndCheckConfig(boolean isv){
        var payConfig = this.getWechatPayConfig(isv);
        if (!payConfig.getEnable()){
            throw new ChannelNotEnableException("微信支付通道未启用");
        }
        return payConfig;
    }

    /**
     * 获取微信支付配置
     */
    public WechatPayConfig getWechatPayConfig(boolean isv){
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        ChannelConfig channelConfig = channelConfigCacheService.getMchChannelConfig(mchAppInfo.getAppId(), isv?ChannelEnum.WECHAT_ISV.getCode():ChannelEnum.WECHAT.getCode());
        return WechatPayConfig.convertConfig(channelConfig);
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
}
