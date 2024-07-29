package cn.daxpay.multi.channel.wechat.service.sync.refund;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.channel.wechat.util.WechatPayUtil;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.bo.sync.RefundSyncResultBo;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.enums.RefundSyncResultEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants.RefundStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 微信退款订单同步
 * @author xxm
 * @since 2024/7/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundSyncV3Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 退款信息查询
     */
    public RefundSyncResultBo sync(RefundOrder refundOrder, WechatPayConfig weChatPayConfig){
        RefundSyncResultBo syncResult = new RefundSyncResultBo();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(weChatPayConfig);
        // 使用退款单号查询, 只返回当前这条, 如果使用支付订单号查询,返回所有相关的
        try {
            var result = wxPayService.refundQueryV3(refundOrder.getRefundNo());
            // 网关退款号
            syncResult.setOutRefundNo(result.getTransactionId())
                    .setAmount(PayUtil.conversionAmount(result.getAmount().getRefundFee()));
            // 退款状态 - 成功
            if (Objects.equals(RefundStatus.SUCCESS, result.getStatus())){
                syncResult.setSyncStatus(RefundSyncResultEnum.SUCCESS)
                        .setFinishTime(WechatPayUtil.parseV3(result.getSuccessTime()));
            }
            // 退款状态 - 退款关闭
            if (Objects.equals(RefundStatus.CLOSED, result.getStatus())){
                syncResult.setSyncStatus(RefundSyncResultEnum.CLOSE);
            }
            // 退款状态 - 失败
            if (Objects.equals(RefundStatus.ABNORMAL, result.getStatus())){
                syncResult.setSyncStatus(RefundSyncResultEnum.FAIL);
            }
        } catch (WxPayException e) {
            log.error("微信退款订单查询V3失败", e);
            syncResult.setErrorMsg(e.getCustomErrorMsg()).setSyncStatus(RefundSyncResultEnum.FAIL);
        }
        return syncResult;
    }
}
