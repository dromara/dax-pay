package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.code.WeChatPayWay;
import cn.bootx.platform.daxpay.service.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.core.system.payinfo.service.PayChannelInfoService;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WeChatPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    private final PayChannelInfoService payChannelInfoService;

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(WeChatPayConfigParam param) {
        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findById(ID).orElseThrow(() -> new PayFailureException("微信支付配置不存在"));
        // 启用或停用
        if (!Objects.equals(param.getEnable(), weChatPayConfig.getEnable())){
            payChannelInfoService.setEnable(PayChannelEnum.WECHAT.getCode(), param.getEnable());
        }
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
     * 微信支持支付方式
     */
    public List<LabelValue> findPayWays() {
        return WeChatPayWay.getPayWays()
            .stream()
            .map(e -> new LabelValue(e.getName(),e.getCode()))
            .collect(Collectors.toList());
    }

}
