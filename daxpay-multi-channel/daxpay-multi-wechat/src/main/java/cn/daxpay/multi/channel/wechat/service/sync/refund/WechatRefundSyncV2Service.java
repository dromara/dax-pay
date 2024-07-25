package cn.daxpay.multi.channel.wechat.service.sync.refund;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.service.bo.sync.RefundSyncResultBo;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信退款订单信息同步
 * @author xxm
 * @since 2024/7/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundSyncV2Service {

    /**
     * 退款信息查询
     */
    public RefundSyncResultBo syncRefundStatus(RefundOrder refundOrder, WechatPayConfig weChatPayConfig){
        RefundSyncResultBo syncResult = new RefundSyncResultBo();
        return syncResult;
    }
}
