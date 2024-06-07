package cn.daxpay.single.service.core.payment.pay.strategy;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayService;
import cn.daxpay.single.service.func.AbsPayStrategy;
import cn.daxpay.single.service.param.channel.wechat.WeChatPayParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付
 * 注意: channelOrder对象需要单独处理, 直接获取会空指针
 * @author xxm
 * @since 2021/4/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatPayStrategy extends AbsPayStrategy {

    private final WeChatPayConfigService weChatPayConfigService;

    private final WeChatPayService weChatPayService;

    private WeChatPayConfig weChatPayConfig;

    private WeChatPayParam weChatPayParam;

    /**
     * 类型
     */
    @Override
    public String getChannel() {
        return PayChannelEnum.WECHAT.getCode();
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        // 微信参数验证
        Map<String, Object> channelParam = this.getPayParam().getExtraParam();
        if (CollUtil.isNotEmpty(channelParam)) {
            this.weChatPayParam = BeanUtil.toBean(channelParam, WeChatPayParam.class);
        } else {
            this.weChatPayParam = new WeChatPayParam();
        }
        // 检查并获取微信支付配置
        this.initWeChatPayConfig();
        weChatPayService.validation(this.getPayParam(), weChatPayConfig);
    }

    /**
     * 发起支付
     */
    @Override
    public void doPayHandler() {
        weChatPayService.pay(this.getOrder(), this.weChatPayParam, this.weChatPayConfig);
    }

    /**
     * 初始化微信支付
     */
    private void initWeChatPayConfig() {
        this.weChatPayConfig = weChatPayConfigService.getAndCheckConfig();
    }

}
