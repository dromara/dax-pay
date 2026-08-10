package cn.daxpay.open.payment.trade.alloc.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

/// # 分账订单结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账订单")
public class AllocOrderResult extends BaseResult {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户名称(由 mchNo 翻译, 走系统 @Trans 机制)
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 平台分账单号
    @Schema(description = "平台分账单号")
    private String allocNo;

    /// 商户分账单号
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /// 原支付资金交易号
    @Schema(description = "原支付资金交易号")
    private String tradeNo;

    /// 原支付交易形态
    @Schema(description = "原支付交易形态")
    private String tradeType;

    /// 商户业务订单号
    @Schema(description = "商户业务订单号")
    private String bizOrderNo;

    /// 通道支付订单号
    @Schema(description = "通道支付订单号")
    private String outOrderNo;

    /// 通道分账单号
    @Schema(description = "通道分账单号")
    private String outAllocNo;

    /// 标题
    @Schema(description = "标题")
    private String title;

    /// 分账描述
    @Schema(description = "分账描述")
    private String description;

    /// 分账总金额(分)
    @Schema(description = "分账总金额(分)")
    private Long amount;

    /// 原订单总金额(分)
    @Schema(description = "原订单总金额(分)")
    private Long orderAmount;

    /// 币种
    @Schema(description = "币种")
    private String currency;

    /// 分账状态
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum
    @Schema(description = "分账状态")
    private String status;

    /// 分账完成时间
    @Schema(description = "分账完成时间")
    private OffsetDateTime finishTime;

    /// 支付通道
    @Schema(description = "支付通道")
    private String channel;

    /// 支付渠道
    @Schema(description = "支付渠道")
    private String provider;

    /// 支付产品编码
    @Schema(description = "支付产品编码")
    private String product;

    /// 通道商户号
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 商户扩展参数
    @Schema(description = "商户扩展参数")
    private String attach;

    /// 错误码
    @Schema(description = "错误码")
    private String errorCode;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;

    /// 明细列表
    @Schema(description = "分账明细列表")
    private List<AllocDetailResult> details;
}
