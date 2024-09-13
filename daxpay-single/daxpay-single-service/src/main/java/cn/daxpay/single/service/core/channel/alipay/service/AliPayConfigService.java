package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.daxpay.single.core.exception.ChannelNotEnableException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.code.AliPayWay;
import cn.daxpay.single.service.core.channel.alipay.dao.AliPayConfigManager;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.system.config.service.PlatformConfigService;
import cn.daxpay.single.service.param.channel.alipay.AliPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付宝支付
 *
 * @author xxm
 * @since 2020/12/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayConfigService {
    /** 默认支付宝配置的主键ID */
    private final static Long ID = 0L;
    private final AliPayConfigManager alipayConfigManager;
    private final PlatformConfigService platformConfigService;

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(AliPayConfigParam param) {
        AliPayConfig alipayConfig = alipayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("支付宝配置不存在"));
        BeanUtil.copyProperties(param, alipayConfig, CopyOptions.create().ignoreNullValue());
        alipayConfigManager.updateById(alipayConfig);
    }

    /**
     * 支付宝支持支付方式
     */
    public List<LabelValue> findPayWays() {
        return AliPayWay.getPayWays()
                .stream()
                .map(e -> new LabelValue(e.getName(),e.getCode()))
                .collect(Collectors.toList());
    }

    /**
     * 获取支付配置
     */
    public AliPayConfig getConfig(){
        return alipayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("支付宝配置不存在"));
    }

    /**
     * 获取并检查支付配置
     */
    public AliPayConfig getAndCheckConfig() {
        AliPayConfig alipayConfig = this.getConfig();
        if (!alipayConfig.getEnable()){
            throw new ChannelNotEnableException("支付宝支付未启用");
        }
        return alipayConfig;
    }

    /**
     * 生成通知地址
     */
    public String generateNotifyUrl(){
        return platformConfigService.getConfig().getWebsiteUrl() + "/unipay/callback/alipay";
    }

    /**
     * 生成同步跳转地址
     */
    public String generateReturnUrl(){
        return platformConfigService.getConfig().getWebsiteUrl() + "/unipay/return/alipay";
    }

    /**
     * 获取支付宝SDK的配置
     */
    public AlipayClient getAlipayClient(){
        AliPayConfig aliPayConfig = this.getConfig();
        return this.getAlipayClient(aliPayConfig);
    }

    /**
     * 获取支付宝SDK的配置
     */
    @SneakyThrows
    public AlipayClient getAlipayClient(AliPayConfig aliPayConfig){
        AlipayConfig config = new AlipayConfig();
        config.setServerUrl(aliPayConfig.getServerUrl());
        config.setAppId(aliPayConfig.getAppId());
        config.setFormat("json");
        config.setCharset("UTF-8");
        config.setSignType(aliPayConfig.getSignType());
        // 证书
        if (Objects.equals(aliPayConfig.getAuthType(), AliPayCode.AUTH_TYPE_CART)){
            config.setPrivateKey(aliPayConfig.getPrivateKey());
            config.setAppCertContent(aliPayConfig.getAppCert());
            config.setRootCertContent(aliPayConfig.getAlipayRootCert());
            config.setAlipayPublicCertContent(aliPayConfig.getAlipayCert());
        } else {
            // 公钥
            config.setPrivateKey(aliPayConfig.getPrivateKey());
            config.setAlipayPublicKey(aliPayConfig.getAlipayPublicKey());
        }
        return new DefaultAlipayClient(config);
    }
}
