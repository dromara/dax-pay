package org.dromara.daxpay.channel.alipay.service.config;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayConfigConvert;
import org.dromara.daxpay.channel.alipay.dao.config.AlipayIsvConfigManager;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvConfig;
import org.dromara.daxpay.channel.alipay.param.config.AlipayIsvConfigParam;
import org.dromara.daxpay.payment.common.exception.ChannelNotEnableException;
import org.dromara.daxpay.payment.isv.dao.config.IsvChannelConfigManager;
import org.dromara.daxpay.payment.isv.dao.isv.IsvInfoManager;
import org.dromara.daxpay.payment.isv.entity.config.IsvChannelConfig;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import cn.hutool.core.bean.BeanUtil;
import com.alipay.api.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * 支付宝服务商配置
 * @author xxm
 * @since 2024/11/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvConfigService {
    private final IsvChannelConfigManager channelConfigManager;
    private final AlipayIsvConfigManager alipayIsvConfigManager;
    private final IsvInfoManager isvInfoManager;

    /**
     * 根据服务商编号查询
     */
    public AlipayIsvConfig findByIsvNo(String isvNo) {
        Optional<AlipayIsvConfig> optional = alipayIsvConfigManager.findByIsvNo(isvNo);
        if (optional.isEmpty()){
            var isvInfo = isvInfoManager.findByIsvNo(isvNo)
                    .orElseThrow(() -> new DataNotExistException("服务商不存在"));
            var entity = new AlipayIsvConfig();
            entity.setIsvNo(isvInfo.getIsvNo());
            IsvChannelConfig channelConfig = new IsvChannelConfig()
                    .setChannel(ChannelEnum.ALIPAY_ISV.getCode())
                    .setIsvNo(isvInfo.getIsvNo());
            channelConfigManager.save(channelConfig);
            alipayIsvConfigManager.save(entity);
            return entity;
        }
        return optional.get();
    }

    /**
     * 更新
     */
    public void update(AlipayIsvConfigParam param) {
        AlipayIsvConfig alipayIsvConfig = alipayIsvConfigManager.findByIsvNo(param.getIsvNo())
                .orElseThrow(() -> new DataNotExistException("支付宝服务商配置不存在"));
        AlipayConfigConvert.CONVERT.copy(param, alipayIsvConfig);
        // 更新通道列表的状态
        var channelConfig = channelConfigManager.findByIsvNoAndChannel(param.getIsvNo(), ChannelEnum.ALIPAY_ISV.getCode())
                .orElseThrow(() -> new DataNotExistException("支付宝通道配置不存在"));
        channelConfig.setEnable(alipayIsvConfig.isEnable());
        channelConfigManager.updateById(channelConfig);
        alipayIsvConfigManager.updateById(alipayIsvConfig);
    }



    /**
     * 获取支付宝SDK的配置
     */
    @SneakyThrows
    public AlipayClient getAlipayClient(AlipayIsvConfig alipayIsvConfig){
        AlipayConfig config = new AlipayConfig();
        config.setAppId(alipayIsvConfig.getAliAppId());
        config.setFormat("json");
        config.setCharset("UTF-8");
        config.setSignType(alipayIsvConfig.getSignType());
        // 证书
        if (Objects.equals(alipayIsvConfig.getAuthType(), AlipayCode.AuthType.AUTH_TYPE_CART)){
            config.setPrivateKey(alipayIsvConfig.getPrivateKey());
            config.setAppCertContent(alipayIsvConfig.getAppCert());
            config.setRootCertContent(alipayIsvConfig.getAlipayRootCert());
            config.setAlipayPublicCertContent(alipayIsvConfig.getAlipayCert());
        } else {
            // 公钥
            config.setPrivateKey(alipayIsvConfig.getPrivateKey());
            config.setAlipayPublicKey(alipayIsvConfig.getAlipayPublicKey());
        }
        // 沙箱
        if (alipayIsvConfig.isSandbox()){
            config.setServerUrl(AlipayCode.ServerUrl.SANDBOX);
        } else {
            config.setServerUrl(AlipayCode.ServerUrl.PRODUCTION);
        }

        return new DefaultAlipayClient(config);
    }

    /**
     * 接口调用
     */
    public <T extends AlipayResponse> T execute(AlipayRequest<T> request, AlipayIsvConfig alipayIsvConfig) throws AlipayApiException {
        AlipayClient alipayClient = this.getAlipayClient(alipayIsvConfig);
        if (Objects.equals(alipayIsvConfig.getAuthType(), AlipayCode.AuthType.AUTH_TYPE_CART)){
            return this.getAlipayClient(alipayIsvConfig).certificateExecute(request);
        } else {
            return alipayClient.execute(request);
        }
    }
}
