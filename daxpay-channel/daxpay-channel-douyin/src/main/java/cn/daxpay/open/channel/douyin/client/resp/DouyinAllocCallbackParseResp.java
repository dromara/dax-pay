package cn.daxpay.open.channel.douyin.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

/// # 抖音通道分账回调验签解析响应
///
/// 与子应用 dax-pay-channel-one 的 `DouyinAllocCallbackParseResp` 镜像, 字段对齐。
/// 抖音分账异步通知, 通知体含逐明细结果, 用于回查分账单并推进状态。
@Data
@Accessors(chain = true)
public class DouyinAllocCallbackParseResp {

    /// 通道分账单号(抖音 order_id, 用于回查分账单 outAllocNo)
    private String orderId;

    /// 商户分账单号(平台 allocNo, 发起时上送的 out_trade_no, 回调定位分账单主路径)
    private String outTradeNo;

    /// 分账状态(SUCCESS/PROCESSING/CLOSED/FAIL)
    private String state;

    /// 分账完成时间(子应用侧已解析为 OffsetDateTime)
    private OffsetDateTime splitFinishTime;

    /// 逐明细结果
    private List<ReceiverResult> receiverResults;

    /// 验签是否通过
    private boolean verified;

    /// 逐明细结果(单个接收方)
    @Data
    @Accessors(chain = true)
    public static class ReceiverResult {

        /// 接收方账号(用于回查明细)
        private String account;

        /// 明细结果(PENDING/SUCCESS/CLOSED)
        private String splitStatus;

        /// 失败原因
        private String failReason;

        /// 明细完成时间(子应用侧已解析为 OffsetDateTime)
        private OffsetDateTime finishTime;
    }
}
