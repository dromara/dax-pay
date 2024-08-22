package cn.daxpay.multi.channel.alipay.service.config;

import cn.daxpay.multi.channel.alipay.code.AliPayCode;
import cn.daxpay.multi.channel.alipay.convert.config.AlipayConfigConvert;
import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.param.config.AliPayConfigParam;
import cn.daxpay.multi.channel.alipay.result.config.AlipayConfigResult;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.core.exception.DataErrorException;
import cn.daxpay.multi.service.common.cache.ChannelConfigCacheService;
import cn.daxpay.multi.service.common.context.MchAppLocal;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.config.ChannelConfigManager;
import cn.daxpay.multi.service.entity.config.ChannelConfig;
import cn.daxpay.multi.service.service.config.PlatformConfigService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayConfigService {
    private final ChannelConfigManager channelConfigManager;
    private final ChannelConfigCacheService channelConfigCacheService;
    private final PlatformConfigService platformConfigService;

    /**
     * 查询
     */
    public AlipayConfigResult findById(Long id) {
        return channelConfigManager.findById(id)
                .map(AliPayConfig::convertConfig)
                .map(AliPayConfig::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("支付宝配置不存在"));
    }

    /**
     * 新增或更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(AliPayConfigParam param){
        if (param.getId() == null){
            this.save(param);
        } else {
            this.update(param);
        }
    }

    /**
     * 添加
     */
    public void save(AliPayConfigParam param) {
        AliPayConfig entity = AlipayConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // 如果运营端使用, 商户号写入上下文中
        MchContextLocal.setMchNo(channelConfig.getMchNo());
        // 判断商户和应用下是否存在该配置
        if (channelConfigManager.existsByAppIdAndChannel(channelConfig.getAppId(), channelConfig.getChannel())){
            throw new DataErrorException("该应用下已存在支付宝配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新
     */
    public void update(AliPayConfigParam param){
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
     * 获取支付宝支付配置
     */
    public AliPayConfig getAliPayConfig(){
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        ChannelConfig channelConfig = channelConfigCacheService.get(mchAppInfo.getAppId(), ChannelEnum.ALI.getCode());
        return AliPayConfig.convertConfig(channelConfig);
    }

    /**
     * 获取支付宝SDK的配置
     */
    public AlipayClient getAlipayClient(){
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        ChannelConfig channelConfig = channelConfigCacheService.get(mchAppInfo.getAppId(), ChannelEnum.ALI.getCode());
        AliPayConfig aliPayConfig = AliPayConfig.convertConfig(channelConfig);
        return this.getAlipayClient(aliPayConfig);
    }

    /**
     * 获取支付异步通知地址
     */
    public String getPayNotifyUrl() {
        var mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{}/{}/pay/alipay",platformInfo.getGatewayServiceUrl(), mchAppInfo.getMchNo(),mchAppInfo.getAppId());
    }

    /**
     * 获取退款异步通知地址
     */
    public String getRefundNotifyUrl() {
        var mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{}/{}/refund/alipay",platformInfo.getGatewayServiceUrl(), mchAppInfo.getMchNo(),mchAppInfo.getAppId());
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
        if (Objects.equals(aliPayConfig.getAuthType(), AliPayCode.AUTH_TYPE_CART)){
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
            config.setServerUrl("https://openapi-sandbox.dl.alipaydev.com/gateway.do");
        } else {
            config.setServerUrl("https://openapi.alipay.com/gateway.do");
        }

        return new DefaultAlipayClient(config);
    }

}
