package cn.daxpay.open.payment.trade.transfer.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 转账资金凭证查询参数(跨通道)
///
@Data
@Accessors(chain = true)
@Schema(title = "转账资金凭证查询参数")
public class TransferTradeQuery {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 平台转账交易号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台转账交易号")
    private String tradeNo;

    /// 所属通道(wechat/alipay/douyin)
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "所属通道")
    private String containerChannel;

    /// 通道编码
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道编码")
    private String channel;

    /// 转账状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "转账状态")
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
