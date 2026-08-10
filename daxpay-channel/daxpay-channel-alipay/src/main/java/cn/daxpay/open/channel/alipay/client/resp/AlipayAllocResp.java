package cn.daxpay.open.channel.alipay.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付宝通道分账响应(发起/同步共用)
///
/// 与子应用 dax-pay-channel-one 的 `AlipayAllocResp` 镜像, 字段对齐。
@Data
@Accessors(chain = true)
public class AlipayAllocResp {

    /// 通道分账单号(发起返回支付宝 settle_no, 写入 outAllocNo)
    private String settleNo;

    /// 同步返回的逐明细结果列表
    private List<RoyaltyDetailResult> royaltyDetailList;

    /// 支付宝网关返回码(10000=成功, 40004=业务失败)
    private String code;

    /// 业务失败子错误码(如 USER_NOT_EXIST)
    private String subCode;

    /// 业务失败子描述
    private String subMsg;

    /// 同步查询的逐明细结果(对应支付宝 royalty_detail_list)
    @Data
    @Accessors(chain = true)
    public static class RoyaltyDetailResult {

        /// 通道明细ID(对应 detail_id → outDetailId)
        private String detailId;

        /// 接收方账号(对应 trans_in, 用于回查明细)
        private String transIn;

        /// 明细状态(PROCESSING/SUCCESS/FAIL)
        private String state;

        /// 错误描述(对应 error_desc)
        private String errorDesc;

        /// 执行时间(对应 execute_dt, 东八区 yyyy-MM-dd HH:mm:ss)
        private String executeDt;
    }
}
