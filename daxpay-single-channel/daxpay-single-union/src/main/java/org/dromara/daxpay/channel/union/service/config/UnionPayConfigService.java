package org.dromara.daxpay.channel.union.service.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.union.convert.UnionPayConfigConvert;
import org.dromara.daxpay.channel.union.entity.config.UnionPayConfig;
import org.dromara.daxpay.channel.union.param.config.UnionPayConfigParam;
import org.dromara.daxpay.channel.union.result.UnionPayConfigResult;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayConfigStorage;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.ChannelNotEnableException;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.common.cache.ChannelConfigCacheService;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.entity.config.ChannelConfig;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
import org.dromara.daxpay.unisdk.common.bean.CertStoreType;
import org.dromara.daxpay.unisdk.common.http.HttpConfigStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;

/**
 * 银联支付
 * @author xxm
 * @since 2024/9/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayConfigService {

    private final ChannelConfigManager channelConfigManager;
    private final ChannelConfigCacheService channelConfigCacheService;
    private final PlatformConfigService platformConfigService;

    /**
     * 查询
     */
    public UnionPayConfigResult findById(Long id) {
        return channelConfigManager.findById(id)
                .map(UnionPayConfig::convertConfig)
                .map(UnionPayConfig::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("云闪付配置不存在"));
    }

    /**
     * 新增或更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(UnionPayConfigParam param){
        if (param.getId() == null){
            this.save(param);
        } else {
            this.update(param);
        }
    }

    /**
     * 添加
     */
    public void save(UnionPayConfigParam param) {
        UnionPayConfig entity = UnionPayConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // 判断商户应用下是否存在该配置
        if (channelConfigManager.existsByAppIdAndChannel(channelConfig.getAppId(), channelConfig.getChannel())){
            throw new DataErrorException("该应用下已存在云闪付配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新
     */
    public void update(UnionPayConfigParam param){
        ChannelConfig channelConfig = channelConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("云闪付配置不存在"));
        // 通道配置 --转换--> 云闪付配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        UnionPayConfig unionPayConfig = UnionPayConfig.convertConfig(channelConfig);
        BeanUtil.copyProperties(param, unionPayConfig, CopyOptions.create().ignoreNullValue());
        ChannelConfig channelConfigParam = unionPayConfig.toChannelConfig();
        // 手动清空一下默认的数据版本号
        channelConfigParam.setVersion(null);
        BeanUtil.copyProperties(channelConfigParam, channelConfig, CopyOptions.create().ignoreNullValue());
        channelConfigManager.updateById(channelConfig);
    }

    /**
     * 获取并检查支付配置
     */
    public UnionPayConfig getAndCheckConfig() {
        UnionPayConfig unionPayConfig = this.getUnionPayConfig();
        if (!unionPayConfig.getEnable()){
            throw new ChannelNotEnableException("云闪付支付未启用");
        }
        return unionPayConfig;
    }

    /**
     * 获取云闪付支付配置
     */
    public UnionPayConfig getUnionPayConfig(){
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        ChannelConfig channelConfig = channelConfigCacheService.get(mchAppInfo.getAppId(), ChannelEnum.UNION_PAY.getCode());
        return UnionPayConfig.convertConfig(channelConfig);
    }


    /**
     * 获取支付异步通知地址
     */
    public String getPayNotifyUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{}/union/pay",platformInfo.getGatewayServiceUrl(),mchAppInfo.getAppId());
    }

    /**
     * 获取退款异步通知地址
     */
    public String getRefundNotifyUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{}/union/refund",platformInfo.getGatewayServiceUrl(), mchAppInfo.getAppId());
    }

    /**
     * 获取同步通知地址
     */
    public String getReturnUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/return/{}/union",platformInfo.getGatewayServiceUrl(),mchAppInfo.getAppId());
    }

    /**
     * 生成云闪付支付接口
     */
    public UnionPayKit initPayKit(){
        UnionPayConfig config = this.getAndCheckConfig();
        return this.initPayKit(config);
    }
    /**
     * 生成云闪付支付接口
     */
    public UnionPayKit initPayKit(UnionPayConfig config){

        UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
        unionPayConfigStorage.setInputCharset(CharsetUtil.UTF_8);
        // 商户号
        unionPayConfigStorage.setMerId(config.getUnionMachId());
        // 云闪付必须使用证书才可以进行调用
        unionPayConfigStorage.setCertSign(true);

        // 中级证书 流
        unionPayConfigStorage.setAcpMiddleCert(new ByteArrayInputStream(Base64.decode(config.getAcpMiddleCert())));
        // 根证书 流
        unionPayConfigStorage.setAcpRootCert(new ByteArrayInputStream(Base64.decode(config.getAcpRootCert())));
        // 私钥证书 流
        unionPayConfigStorage.setKeyPrivateCert(new ByteArrayInputStream(Base64.decode(config.getKeyPrivateCert())));

        //私钥证书对应的密码 私钥证书对应的密码
        unionPayConfigStorage.setKeyPrivateCertPwd(config.getKeyPrivateCertPwd());
        //设置证书对应的存储方式，证书流
        unionPayConfigStorage.setCertStoreType(CertStoreType.INPUT_STREAM);
        // 签名方式
        unionPayConfigStorage.setSignType(config.getSignType());
        //是否为测试账号，沙箱环境
        unionPayConfigStorage.setTest(config.isSandbox());

        // 网络请求配置
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        httpConfigStorage.setCertStoreType(CertStoreType.INPUT_STREAM);
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);

        // 创建支付服务
        return new UnionPayKit(unionPayConfigStorage, httpConfigStorage);
    }

}
