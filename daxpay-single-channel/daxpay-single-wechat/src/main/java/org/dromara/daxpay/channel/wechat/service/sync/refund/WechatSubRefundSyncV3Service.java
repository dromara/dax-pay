package org.dromara.daxpay.channel.wechat.service.sync.refund;

import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryV3Request;
import com.github.binarywang.wxpay.constant.WxPayConstants.RefundStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.util.WechatPayUtil;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
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
public class WechatSubRefundSyncV3Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 退款信息查询
     */
    public RefundSyncResultBo sync(RefundOrder refundOrder, WechatPayConfig weChatPayConfig){
        RefundSyncResultBo syncResult = new RefundSyncResultBo();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(weChatPayConfig);
        // 使用退款单号查询, 只返回当前这条, 如果使用支付订单号查询,返回所有相关的
        try {
            WxPayRefundQueryV3Request request = new WxPayRefundQueryV3Request();
            request.setOutRefundNo(refundOrder.getRefundNo());
            request.setSubMchid(weChatPayConfig.getSubMchId());
            var result = wxPayService.refundPartnerQueryV3(request);
            // 网关退款号
            syncResult.setOutRefundNo(result.getTransactionId())
                    .setAmount(PayUtil.conversionAmount(result.getAmount().getRefundFee()));
            // 退款状态 - 成功
            if (Objects.equals(RefundStatus.SUCCESS, result.getStatus())){
                syncResult.setRefundStatus(RefundStatusEnum.SUCCESS)
                        .setFinishTime(WechatPayUtil.parseV3(result.getSuccessTime()));
            }
            // 退款状态 - 退款关闭
            if (Objects.equals(RefundStatus.CLOSED, result.getStatus())){
                syncResult.setRefundStatus(RefundStatusEnum.CLOSE);
            }
            // 退款状态 - 失败
            if (Objects.equals(RefundStatus.ABNORMAL, result.getStatus())){
                syncResult.setRefundStatus(RefundStatusEnum.CLOSE);
            }
        } catch (WxPayException e) {
            log.error("微信退款订单查询V3失败", e);
            syncResult.setSyncErrorMsg(e.getCustomErrorMsg())
                    .setSyncSuccess(false)
                    .setRefundStatus(RefundStatusEnum.FAIL);
        }
        return syncResult;
    }
}
