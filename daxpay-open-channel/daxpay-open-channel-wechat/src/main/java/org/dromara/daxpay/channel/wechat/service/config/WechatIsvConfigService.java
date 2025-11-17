package org.dromara.daxpay.channel.wechat.service.config;

import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.channel.wechat.convert.WechatIsvConfigConvert;
import org.dromara.daxpay.channel.wechat.dao.config.WechatIsvConfigManager;
import org.dromara.daxpay.channel.wechat.entity.config.WechatIsvConfig;
import org.dromara.daxpay.channel.wechat.param.config.WechatIsvConfigParam;
import org.dromara.daxpay.payment.common.exception.ChannelNotEnableException;
import org.dromara.daxpay.payment.isv.dao.config.IsvChannelConfigManager;
import org.dromara.daxpay.payment.isv.dao.isv.IsvInfoManager;
import org.dromara.daxpay.payment.isv.entity.config.IsvChannelConfig;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 微信服务商配置
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvConfigService {
    private final IsvChannelConfigManager channelConfigManager;
    private final WechatIsvConfigManager wechatIsvConfigManager;
    private final IsvInfoManager isvInfoManager;

    /**
     * 根据服务商编号查询
     */
    public WechatIsvConfig findByIsvNo(String isvNo) {
        var optional = wechatIsvConfigManager.findByIsvNo(isvNo);
        if (optional.isEmpty()){
            var isvInfo = isvInfoManager.findByIsvNo(isvNo)
                    .orElseThrow(() -> new DataNotExistException("服务商不存在"));
            var entity = new WechatIsvConfig();
            entity.setIsvNo(isvInfo.getIsvNo());
            IsvChannelConfig channelConfig = new IsvChannelConfig()
                    .setChannel(ChannelEnum.WECHAT.getCode())
                    .setIsvNo(isvInfo.getIsvNo());
            channelConfigManager.save(channelConfig);
            wechatIsvConfigManager.save(entity);
            return entity;
        }
        return optional.get();
    }

    /**
     * 更新
     */
    public void update(WechatIsvConfigParam param) {
        WechatIsvConfig wechatIsvConfig = wechatIsvConfigManager.findByIsvNo(param.getIsvNo())
                .orElseThrow(() -> new DataNotExistException("微信服务商配置不存在"));
        WechatIsvConfigConvert.CONVERT.copy(param, wechatIsvConfig);
        // 更新通道列表的状态
        var channelConfig = channelConfigManager.findByIsvNoAndChannel(param.getIsvNo(), ChannelEnum.WECHAT_ISV.getCode())
                .orElseThrow(() -> new DataNotExistException("微信服务商配置不存在"));
        channelConfig.setEnable(wechatIsvConfig.isEnable());
        channelConfigManager.updateById(channelConfig);
        wechatIsvConfigManager.updateById(wechatIsvConfig);
    }
}

