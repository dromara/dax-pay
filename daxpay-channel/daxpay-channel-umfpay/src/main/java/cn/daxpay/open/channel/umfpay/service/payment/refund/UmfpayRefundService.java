package cn.daxpay.open.channel.umfpay.service.payment.refund;

import cn.daxpay.open.channel.umfpay.client.UmfpayChannelClient;
import cn.daxpay.open.channel.umfpay.client.credential.UmfpaySdkCredential;
import cn.daxpay.open.channel.umfpay.client.req.UmfpayRefundReq;
import cn.daxpay.open.channel.umfpay.client.resp.UmfpayRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/// # 联动优势退款业务服务
///
/// 通过 [UmfpayChannelClient] 调用子应用发起联动优势 mer_refund 退款。
/// 联动优势退款为**同步终态**(对齐参考实现能力): 受理成功即退款成功,
/// 无退款异步通知与退款查询接口。
///
/// 退款需原商户单号(order_id = 平台支付交易号) + 原下单日期(mer_date, yyyyMMdd 东八区),
/// 联动优势按 order_id+mer_date 定位原订单, mer_date 由主应用反查原支付订单创建时间按东八区转换
/// (与子应用下单时 mer_date 生成口径一致, 不跟随用户时区)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayRefundService {

    private final UmfpayChannelClient umfpayChannelClient;
    private final PayTradeManager payTradeManager;

    /// mer_date 格式(yyyyMMdd)
    private static final DateTimeFormatter MER_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /// 东八区偏移(与子应用下单 mer_date 口径一致)
    private static final ZoneOffset CST = ZoneOffset.ofHours(8);

    /// 执行联动优势退款
    public RefundResultBo refund(RefundOrder refundOrder, UmfpaySdkCredential credential) {
        // 反查原支付订单(mer_refund 需原下单日期 mer_date 与原订单总金额)
        PayTrade originOrder = payTradeManager.findByTradeNo(refundOrder.getTradeNo())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.originOrderNotExist"));

        UmfpayRefundReq req = new UmfpayRefundReq();
        req.setCredential(credential);
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原商户订单号(order_id = 原支付交易号)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原下单日期(mer_date, 东八区 yyyyMMdd, 与子应用下单口径一致)
        req.setOriginMerDate(originOrder.getCreateTime().withOffsetSameInstant(CST).format(MER_DATE_FORMATTER));
        // 原订单总金额(org_amount)
        req.setTotalAmount(refundOrder.getOrderAmount());
        // 退款金额(refund_amount)
        req.setRefundAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());

        DaxResult<UmfpayRefundResp> result = umfpayChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("联动优势通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // mer_refund 同步终态, 受理成功即完成
        return new RefundResultBo()
                .setComplete(true)
                .setStatus(RefundOrderStatusEnum.SUCCESS);
    }
}
