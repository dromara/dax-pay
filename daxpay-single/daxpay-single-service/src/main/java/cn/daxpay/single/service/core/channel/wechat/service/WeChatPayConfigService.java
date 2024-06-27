package cn.daxpay.single.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.daxpay.single.core.exception.ConfigNotEnableException;
import cn.daxpay.single.service.code.WeChatPayWay;
import cn.daxpay.single.service.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.system.config.service.PlatformConfigService;
import cn.daxpay.single.service.param.channel.wechat.WeChatPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信支付配置
 *
 * @author xxm
 * @since 2021/3/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayConfigService {
    /** 默认微信支付配置的主键ID */
    private final static Long ID = 0L;
    private final WeChatPayConfigManager weChatPayConfigManager;

    private final PlatformConfigService platformConfigService;

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(WeChatPayConfigParam param) {
        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findById(ID).orElseThrow(() -> new ConfigNotEnableException("微信支付配置不存在"));
        BeanUtil.copyProperties(param, weChatPayConfig, CopyOptions.create().ignoreNullValue());
        weChatPayConfigManager.updateById(weChatPayConfig);
    }


    /**
     * 获取支付配置
     */
    public WeChatPayConfig getConfig(){
        return weChatPayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("微信支付配置不存在"));
    }

    /**
     * 获取并检查配置
     */
    public WeChatPayConfig getAndCheckConfig(){
        WeChatPayConfig weChatPayConfig = getConfig();
        if (!weChatPayConfig.getEnable()){
            throw new ConfigNotEnableException("微信支付未启用");
        }
        return weChatPayConfig;
    }


    /**
     * 微信支持支付方式
     */
    public List<LabelValue> findPayWays() {
        return WeChatPayWay.getPayWays()
            .stream()
            .map(e -> new LabelValue(e.getName(),e.getCode()))
            .collect(Collectors.toList());
    }

    /**
     * 生成微信通知地址
     */
    public String generateNotifyUrl(){
        return platformConfigService.getConfig().getWebsiteUrl() + "/unipay/callback/wechat";
    }

    /**
     * 生成同步跳转地址
     */
    public String generateReturnUrl(){
        return platformConfigService.getConfig().getWebsiteUrl() + "/unipay/return/wechat";
    }

}
