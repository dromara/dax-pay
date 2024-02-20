package cn.bootx.platform.daxpay.service.core.payment.repair.strategy.refund;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayRecordService;
import cn.bootx.platform.daxpay.service.func.AbsRefundRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2024/1/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WeChatRefundRepairStrategy extends AbsRefundRepairStrategy {

    private final WeChatPayRecordService wechatPayRecordService;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 退款成功修复
     */
    @Override
    public void doSuccessHandler() {
        LocalDateTime finishTime = PaymentContextLocal.get()
                .getRepairInfo()
                .getFinishTime();
        // 首先执行父类的修复逻辑
        super.doSuccessHandler();
        // 异步支付需要追加完成时间
        this.getRefundChannelOrder().setRefundTime(finishTime);
        // 记录退款成功记录
        wechatPayRecordService.refund(this.getRefundOrder(), this.getRefundChannelOrder());
    }
}
