package cn.daxpay.open.payment.trade.transfer.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 微信转账单查询参数
///
@Data
@Accessors(chain = true)
@Schema(title = "微信转账单查询参数")
public class WechatTransferOrderQuery {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    /// 平台转账单号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台转账单号")
    private String transferNo;

    /// 商户转账号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 收款人微信 openid
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "收款人微信 openid")
    private String payeeOpenid;

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
