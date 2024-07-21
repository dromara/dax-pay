package cn.daxpay.multi.channel.alipay.strategy;

import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.close.AliPayCloseService;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.CloseTypeEnum;
import cn.daxpay.multi.service.strategy.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝支付订单关闭
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayCloseStrategy extends AbsPayCloseStrategy {
    private final AliPayConfigService alipayConfigService;

    private final AliPayCloseService aliPayCloseService;

    private AliPayConfig alipayConfig;

    @Override
    public String getChannel() {
        return ChannelEnum.ALI.getCode();
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        this.alipayConfig = alipayConfigService.getAliPayConfig();
    }

    /**
     * 关闭操作
     */
    @Override
    public CloseTypeEnum doCloseHandler() {
        if (this.isUseCancel()){
            aliPayCloseService.cancel(this.getOrder(), this.alipayConfig);
            return CloseTypeEnum.CANCEL;
        } else {
            aliPayCloseService.close(this.getOrder(), this.alipayConfig);
            return CloseTypeEnum.CLOSE;
        }
    }
}
