package cn.daxpay.open.payment.trade.alloc.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 分账订单查询参数
///
@Data
@Accessors(chain = true)
@Schema(title = "分账订单查询参数")
public class AllocOrderQuery {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 平台分账单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台分账单号")
    private String allocNo;

    /// 商户分账单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /// 原支付交易号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "原支付交易号")
    private String tradeNo;

    /// 商户业务订单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户业务订单号")
    private String bizOrderNo;

    /// 支付通道
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付通道")
    private String channel;

    /// 分账状态
    /// @see cn.daxpay.open.payment.trade.alloc.enums.AllocOrderStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "分账状态")
    private String status;

    /// 创建时间-开始
    @QueryParam(type = QueryParam.CompareTypeEnum.GE, fieldName = "create_time")
    @Schema(description = "创建时间-开始")
    private OffsetDateTime createTimeStart;

    /// 创建时间-结束
    @QueryParam(type = QueryParam.CompareTypeEnum.LE, fieldName = "create_time")
    @Schema(description = "创建时间-结束")
    private OffsetDateTime createTimeEnd;
}
