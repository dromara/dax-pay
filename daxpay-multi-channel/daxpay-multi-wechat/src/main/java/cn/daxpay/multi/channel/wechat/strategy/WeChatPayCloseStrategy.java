package cn.daxpay.multi.channel.wechat.strategy;

import cn.daxpay.multi.channel.wechat.code.WechatPayCode;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.close.WechatPayCloseV2Service;
import cn.daxpay.multi.channel.wechat.service.close.WechatPayCloseV3Service;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.CloseTypeEnum;
import cn.daxpay.multi.core.enums.PayMethodEnum;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.strategy.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付关闭策略
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WeChatPayCloseStrategy extends AbsPayCloseStrategy {

    private final WechatPayConfigService wechatPayConfigService;
    private final WechatPayCloseV2Service payCloseV2Service;
    private final WechatPayCloseV3Service payCloseV3Service;

    private WechatPayConfig config;

    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        this.config = wechatPayConfigService.getWechatPayConfig();
    }

    /**
     * 关闭操作
     */
    @Override
    public CloseTypeEnum doCloseHandler() {
        PayOrder order = this.getOrder();
        // 只有付款码可以撤销支付
        if (this.isUseCancel() && order.getMethod().equals(PayMethodEnum.BARCODE.getCode())){
            payCloseV2Service.cancel(order,config);
            return CloseTypeEnum.CANCEL;
        }
        // 判断接口是v2还是v3
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)){
            payCloseV2Service.close(order,config);
        } else {
            payCloseV3Service.close(order,config);
        }
        return CloseTypeEnum.CLOSE;
    }
}
