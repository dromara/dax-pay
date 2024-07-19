package cn.daxpay.multi.channel.wechat.service.config;

import cn.daxpay.multi.channel.wechat.convert.config.WechatPayConfigConvert;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.param.config.WechatPayConfigParam;
import cn.daxpay.multi.channel.wechat.result.config.WechatPayConfigResult;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.core.exception.DataErrorException;
import cn.daxpay.multi.service.common.cache.ChannelConfigCacheService;
import cn.daxpay.multi.service.common.context.MchAppLocal;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.channel.ChannelConfigManager;
import cn.daxpay.multi.service.entity.channel.ChannelConfig;
import cn.daxpay.multi.service.service.config.PlatformConfigService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * 获取微信支付配置
     */
    public WechatPayConfig getWechatPayConfig(){
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        ChannelConfig channelConfig = channelConfigCacheService.get(mchAppInfo.getAppId(), ChannelEnum.WECHAT.getCode());
        return WechatPayConfig.convertConfig(channelConfig);
    }

    /**
     * 获取异步通知地址
     */
    public String getNotifyUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{}/{}/alipay",platformInfo.getGatewayServiceUrl(), mchAppInfo.getMchNo(),mchAppInfo.getAppId());
    }

    /**
     * 获取同步通知地址
     */
    public String getReturnUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/return/{}/{}/alipay",platformInfo.getGatewayServiceUrl(), mchAppInfo.getMchNo(),mchAppInfo.getAppId());
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

    /**
     * 官方SDK
     */
    public void wxv3Sdk(WechatPayConfig wechatPayConfig){
        Config config = new RSAConfig.Builder()
                        .merchantId(wechatPayConfig.getWxMchId())
                        .privateKey(wechatPayConfig.getPrivateKey())
                        .merchantSerialNumber(wechatPayConfig.getCertSerialNo())
                        .wechatPayCertificates(wechatPayConfig.getPrivateCert())
                        .build();
        // 构建service
        NativePayService service = new NativePayService.Builder().config(config).build();
    }

}
