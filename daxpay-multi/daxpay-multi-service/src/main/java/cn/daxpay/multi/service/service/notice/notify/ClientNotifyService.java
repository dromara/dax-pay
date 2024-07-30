package cn.daxpay.multi.service.service.notice.notify;

import cn.daxpay.multi.core.enums.TradeNotifyTypeEnum;
import cn.daxpay.multi.service.common.context.MchAppLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.enums.NotifyTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 商户消息通知服务类
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNotifyService {
    /**
     * 注册支付通知
     */
    public void registerPayNotice(PayOrder order) {
        if (this.nonRegister(NotifyTypeEnum.PAY)){
            return;
        }

        log.info("注册支付通知");
    }

    /**
     * 注册退款通知
     */
    public void registerRefundNotice(RefundOrder order) {
        if (this.nonRegister(NotifyTypeEnum.REFUND)){
            return;
        }
        log.info("注册退款通知");
    }

    /**
     * 注册转账通知
     */
    public void registerTransferNotice(TransferOrder order) {
        if (this.nonRegister(NotifyTypeEnum.TRANSFER)){
            return;
        }
        log.info("注册转账通知");
    }

    /**
     * 判断是否需要注册通知
     */
    private boolean nonRegister(NotifyTypeEnum notifyType) {

        // 判断是否开启消息通知功能
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        if (!Objects.equals(mchAppInfo.getNotifyType(), TradeNotifyTypeEnum.HTTP)){
            return false;
        }

        return true;
    }

}
