package org.dromara.daxpay.channel.wechat.service.isv;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.Applyment4SubService;
import com.github.binarywang.wxpay.service.MerchantMediaService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.Applyment4SubServiceImpl;
import com.github.binarywang.wxpay.service.impl.MerchantMediaServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.convert.WechatIsvConfigConvert;
import org.dromara.daxpay.channel.wechat.entity.config.WechatIsvConfig;
import org.dromara.daxpay.channel.wechat.param.config.WechatIsvConfigParam;
import org.dromara.daxpay.channel.wechat.result.config.WechatIsvConfigResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.ChannelNotEnableException;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.isv.cache.IsvChannelConfigCacheService;
import org.dromara.daxpay.service.isv.dao.config.IsvChannelConfigManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 微信服务商服务
 * @author xxm
 * @since 2024/11/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvConfigService {

    private final IsvChannelConfigManager channelConfigManager;
    private final IsvChannelConfigCacheService channelConfigCacheService;

    /**
     * 查询
     */
    public WechatIsvConfigResult findById(Long id) {
        return channelConfigManager.findById(id)
                .map(WechatIsvConfig::convertConfig)
                .map(WechatIsvConfig::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("微信服务商配置不存在"));
    }

    /**
     * 新增或更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(WechatIsvConfigParam param){
        if (param.getId() == null){
            this.save(param);
        } else {
            this.update(param);
        }
    }

    /**
     * 添加
     */
    public void save(WechatIsvConfigParam param) {
        WechatIsvConfig entity = WechatIsvConfigConvert.CONVERT.toEntity(param);
        var channelConfig = entity.toChannelConfig();
        // 判断商户和应用下是否存在该配置
        if (channelConfigManager.existsByChannel(channelConfig.getChannel())){
            throw new DataErrorException("该服务商下已存在微信配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新
     */
    public void update(WechatIsvConfigParam param){
        var channelConfig = channelConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("微信支付配置不存在"));
        // 通道配置 --转换--> 微信支付配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        WechatIsvConfig wechatIsvConfig = WechatIsvConfig.convertConfig(channelConfig);
        BeanUtil.copyProperties(param, wechatIsvConfig, CopyOptions.create().ignoreNullValue());
        var channelConfigParam = wechatIsvConfig.toChannelConfig();
        // 手动写入一下原来的数据版本号
        channelConfigParam.setVersion(channelConfig.getVersion());
        BeanUtil.copyProperties(channelConfigParam, channelConfig, CopyOptions.create().ignoreNullValue());
        channelConfigManager.updateById(channelConfig);
    }

    /**
     * 获取并检查支付配置
     */
    public WechatIsvConfig getAndCheckConfig(){
        var payConfig = this.getWechatIsvConfig();
        if (!payConfig.getEnable()){
            throw new ChannelNotEnableException("微信服务商通道未启用");
        }
        return payConfig;
    }

    /**
     * 获取微信支付配置
     */
    public WechatIsvConfig getWechatIsvConfig(){
        var channelConfig = channelConfigCacheService.get(ChannelEnum.WECHAT.getCode());
        return WechatIsvConfig.convertConfig(channelConfig);
    }

    /**
     * wxjava 支付开发包
     */
    public WxPayConfig wxJavaConfig(WechatIsvConfig wechatIsvConfig){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(wechatIsvConfig.getWxMchId());
        payConfig.setAppId(wechatIsvConfig.getWxAppId());
        payConfig.setMchKey(wechatIsvConfig.getApiKeyV2());
        payConfig.setApiV3Key(wechatIsvConfig.getApiKeyV3());
        // 注意不要使用base64的方式进行配置, 因为wxjava 是直接读取文本并不会进行解码, 会导致证书异常
        if (StrUtil.isNotBlank(wechatIsvConfig.getPublicKey())){
            payConfig.setPublicKeyContent(Base64.decode(wechatIsvConfig.getPublicKey()));
        }
        payConfig.setPublicKeyId(wechatIsvConfig.getPublicKeyId());
        if (StrUtil.isNotBlank(wechatIsvConfig.getPrivateCert())){
            payConfig.setPrivateCertContent(Base64.decode(wechatIsvConfig.getPrivateCert()));
        }
        if (StrUtil.isNotBlank(wechatIsvConfig.getPrivateKey())){
            payConfig.setPrivateKeyContent(Base64.decode(wechatIsvConfig.getPrivateKey()));
        }
        payConfig.setCertSerialNo(wechatIsvConfig.getCertSerialNo());
        if (StrUtil.isNotBlank(wechatIsvConfig.getP12())){
            payConfig.setKeyContent(Base64.decode(wechatIsvConfig.getP12()));
        }
        return payConfig;
    }

    /**
     * wxjava 支付开发包
     */
    public WxPayService wxJavaPay(WechatIsvConfig wechatIsvConfig){
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxJavaConfig(wechatIsvConfig));
        return wxPayService;
    }


    /**
     * 特约商户进件
     */
    public Applyment4SubService applyment4SubService(WechatIsvConfig wechatIsvConfig){
        return new Applyment4SubServiceImpl(wxJavaPay(wechatIsvConfig));
    }


    /**
     * 微信支付通用媒体接口.
     */
    public MerchantMediaService merchantMediaService(WechatIsvConfig wechatIsvConfig){
        return new MerchantMediaServiceImpl(wxJavaPay(wechatIsvConfig));
    }

}

