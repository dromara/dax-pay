package org.dromara.daxpay.channel.wechat.service.payment.config;

import org.dromara.daxpay.channel.wechat.convert.WechatPayConfigConvert;
import org.dromara.daxpay.channel.wechat.convert.WechatPaySubConfigConvert;
import org.dromara.daxpay.channel.wechat.entity.config.WechatIsvConfig;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPaySubConfig;
import org.dromara.daxpay.channel.wechat.enums.WechatAuthTypeEnum;
import org.dromara.daxpay.channel.wechat.param.config.WechatPayConfigParam;
import org.dromara.daxpay.channel.wechat.param.config.WechatPaySubConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.dromara.daxpay.channel.wechat.result.config.WechatPaySubConfigResult;
import org.dromara.daxpay.core.context.PaymentReqInfoLocal;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.ChannelNotEnableException;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.isv.cache.IsvChannelConfigCacheService;
import org.dromara.daxpay.service.merchant.cache.ChannelConfigCacheService;
import org.dromara.daxpay.service.merchant.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.merchant.local.MchContextLocal;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.pay.entity.config.ChannelConfig;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
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
    private final ChannelConfigCacheService channelConfigCacheService;
    private final PaymentAssistService paymentAssistService;
    private final IsvChannelConfigCacheService isvChannelConfigCacheService;

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
     * 查询特约商户信息
     */
    public WechatPaySubConfigResult findSubById(Long id) {
        return channelConfigManager.findById(id)
                .map(WechatPaySubConfig::convertConfig)
                .map(WechatPaySubConfig::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("微信支付特约商户配置不存在"));
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
        var mchApp = PaymentContextLocal.get().getReqInfo();
        WechatPayConfig entity = WechatPayConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // 如果运营端使用, 商户号写入上下文中
        MchContextLocal.setMchNo(channelConfig.getMchNo());
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
        var mchApp = PaymentContextLocal.get().getReqInfo();
        ChannelConfig channelConfig = channelConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("微信支付配置不存在"));
        // 通道配置 --转换--> 微信支付配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        WechatPayConfig wechatConfig = WechatPayConfig.convertConfig(channelConfig);
        BeanUtil.copyProperties(param, wechatConfig, CopyOptions.create().ignoreNullValue());
        ChannelConfig channelConfigParam = wechatConfig.toChannelConfig();
        // 手动写入一下原来的数据版本号
        channelConfigParam.setVersion(channelConfig.getVersion());
        BeanUtil.copyProperties(channelConfigParam, channelConfig, CopyOptions.create().ignoreNullValue());
        channelConfigManager.updateById(channelConfig);
    }


    /**
     * 新增或更新特约商户配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateSub(WechatPaySubConfigParam param){
        if (param.getId() == null){
            this.saveSub(param);
        } else {
            this.updateSub(param);
        }
    }

    /**
     * 创建特约商户
     */
    private void saveSub(WechatPaySubConfigParam param) {
        paymentAssistService.initMchAndApp(param.getAppId());
        // 转换类型
        var entity = WechatPaySubConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // 判断商户和应用下是否存在该配置
        if (channelConfigManager.existsByAppIdAndChannel(param.getAppId(), channelConfig.getChannel())){
            throw new DataErrorException("该应用下已存在微信配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新特约商户
     */
    private void updateSub(WechatPaySubConfigParam param) {
        paymentAssistService.initMchAndApp(param.getAppId());
        ChannelConfig channelConfig = channelConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("微信支付配置不存在"));
        // 通道配置 --转换--> 支付配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        var subConfig = WechatPaySubConfig.convertConfig(channelConfig);
        BeanUtil.copyProperties(param, subConfig, CopyOptions.create().ignoreNullValue());
        ChannelConfig channelConfigParam = subConfig.toChannelConfig();
        // 手动写入一下原来的数据版本号
        channelConfigParam.setVersion(channelConfig.getVersion());
        BeanUtil.copyProperties(channelConfigParam, channelConfig, CopyOptions.create().ignoreNullValue());
        channelConfigManager.updateById(channelConfig);
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
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        ChannelConfig channelConfig = channelConfigCacheService.getMchChannelConfig(reqInfo.getAppId(), isv ? ChannelEnum.WECHAT_ISV.getCode() : ChannelEnum.WECHAT.getCode());
        // 判断是特约商户还是正常商户
        if (isv) {
            // 特约商户
            var wechatPaySubConfig = WechatPaySubConfig.convertConfig(channelConfig);
            // 获取微信服务商配置
            var isvChannelConfig = isvChannelConfigCacheService.get(ChannelEnum.WECHAT_ISV.getCode());
            var wechatIsvConfig = WechatIsvConfig.convertConfig(isvChannelConfig);
            // 合并微信服务商配置和微信特约商户配置, 生成普通商户配置
            WechatPayConfig wechatPayConfig = new WechatPayConfig();
            BeanUtil.copyProperties(wechatIsvConfig, wechatPayConfig,CopyOptions.create().ignoreNullValue());
            wechatPayConfig.setSubAppId(wechatPaySubConfig.getSubAppId())
                    .setSubMchId(wechatPaySubConfig.getSubMchId())
                    .setIsv(true);
            // 使用二级商户微信认证配置
            if (Objects.equals(wechatPayConfig.getAuthType(), WechatAuthTypeEnum.SUB.getCode())){
                wechatPayConfig.setAuthUrl(wechatPaySubConfig.getWxAuthUrl())
                        .setAppSecret(wechatPaySubConfig.getWxAppSecret());
            }

            // 同时启用才可以使用
            wechatPayConfig.setEnable(wechatPaySubConfig.getEnable()&&wechatIsvConfig.getEnable());
            return wechatPayConfig;
        } else {
            return WechatPayConfig.convertConfig(channelConfig);
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
}
