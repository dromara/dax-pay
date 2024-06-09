package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.code.AliPayWay;
import cn.daxpay.single.service.core.channel.alipay.dao.AliPayConfigManager;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.system.config.service.PayChannelConfigService;
import cn.daxpay.single.service.param.channel.alipay.AliPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.CharsetUtil;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
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
    private final PayChannelConfigService payChannelConfigService;

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
            throw new PayFailureException("支付宝支付未启用");
        }
        return alipayConfig;
    }


    /**
     * 初始化IJPay服务
     */
    @SneakyThrows
    public void initConfig(AliPayConfig alipayConfig) {
        AliPayApiConfig aliPayApiConfig;
        // 公钥
        if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_KEY)) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(alipayConfig.getAppId())
                    .setPrivateKey(alipayConfig.getPrivateKey())
                    .setAliPayPublicKey(alipayConfig.getAlipayPublicKey())
                    .setCharset(CharsetUtil.UTF_8)
                    .setServiceUrl(alipayConfig.getServerUrl())
                    .setSignType(alipayConfig.getSignType())
                    .build();
        }
        // 证书
        else if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_CART)) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(alipayConfig.getAppId())
                    .setPrivateKey(alipayConfig.getPrivateKey())
                    .setAppCertContent(alipayConfig.getAppCert())
                    .setAliPayCertContent(alipayConfig.getAlipayCert())
                    .setAliPayRootCertContent(alipayConfig.getAlipayRootCert())
                    .setCharset(CharsetUtil.UTF_8)
                    .setServiceUrl(alipayConfig.getServerUrl())
                    .setSignType(alipayConfig.getSignType())
                    .buildByCertContent();
        }
        else {
            throw new BizException("支付宝认证方式不可为空");
        }
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }

}
