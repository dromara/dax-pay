package cn.daxpay.open.payment.trade.alloc.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 分账完结发起参数(内部编排用)
///
/// 多次分账的收尾动作: 解冻剩余冻结资金给商户本身。
/// 完结落为一张 allocType=finish 的分账单(无接收方明细), 复用幂等键与同步推进链路。
@Data
@Accessors(chain = true)
@Schema(title = "分账完结发起参数")
public class AllocFinishParam {

    /// 商户号(运营端代发必填; 商户端/开放API由上下文强制)
    @Size(max = 32, message = "商户号不可超过32位")
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户分账单号(幂等键, 同一商户下唯一, 与分账单共用 uk_pay_alloc_order_mch_biz 维度)
    @NotBlank(message = "商户分账单号必填")
    @Size(max = 100, message = "商户分账单号不可超过100位")
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /// 原支付资金交易号(tradeNo 与 bizOrderNo 二选一, tradeNo 优先)
    @Size(max = 100, message = "原支付交易号不可超过100位")
    @Schema(description = "原支付资金交易号")
    private String tradeNo;

    /// 原支付商户业务订单号(tradeNo 为空时用此定位原支付)
    @Size(max = 100, message = "商户业务订单号不可超过100位")
    @Schema(description = "原支付商户业务订单号")
    private String bizOrderNo;

    /// 完结描述
    @Size(max = 500, message = "完结描述不可超过500位")
    @Schema(description = "完结描述")
    private String description;

    /// 商户扩展参数, 回调时原样返回
    @Size(max = 500, message = "商户扩展参数不可超过500位")
    @Schema(description = "商户扩展参数, 回调时原样返回")
    private String attach;

    /// 异步通知地址
    @Size(max = 200, message = "回调通知地址不可超过200位")
    @Schema(description = "回调通知地址")
    private String notifyUrl;
}
