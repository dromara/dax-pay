package cn.daxpay.multi.channel.wechat.strategy;

import cn.daxpay.multi.channel.wechat.code.WechatPayCode;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.channel.wechat.service.refund.WechatRefundV2Service;
import cn.daxpay.multi.channel.wechat.service.refund.WechatRefundV3Service;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.bo.trade.RefundResultBo;
import cn.daxpay.multi.service.strategy.AbsRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatRefundStrategy extends AbsRefundStrategy {

    private final WechatPayConfigService weChatPayConfigService;

    private final WechatRefundV2Service wechatRefundV2Service;

    private final WechatRefundV3Service wechatRefundV3Service;

    private WechatPayConfig config;

    /**
     * 策略标识
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 退款前对处理, 初始化微信支付配置
     */
    @Override
    public void doBeforeRefundHandler() {
        this.config = weChatPayConfigService.getWechatPayConfig();
    }


    /**
     * 退款操作
     */
    @Override
    public RefundResultBo doRefundHandler() {
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
            return wechatRefundV2Service.refund(this.getRefundOrder(),this.config);
        } else {
            return wechatRefundV3Service.refund(this.getRefundOrder(),this.config);
        }
    }

}
