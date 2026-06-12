package org.dromara.daxpay.payment.pay.param.reconcile;

import org.dromara.daxpay.platform.core.annotation.QueryParam;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.payment.pay.param.MchQuery;
import org.dromara.daxpay.platform.core.enums.pay.reconcile.ReconcileResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 对账单查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "对账单查询参数")
public class ReconcileStatementQuery extends MchQuery {

    /// 名称
    @Schema(description = "名称")
    private String name;

    /// 对账号
    @Schema(description = "对账号")
    private String reconcileNo;

    /// 日期
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "日期")
    private LocalDate date;


    /// 支付产品
    /// @see org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "支付产品")
    private String product;

    /// 通道
    /// @see ChannelEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道")
    private String channel;

    /// 交易对账文件是否下载或上传成功
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易对账文件是否下载或上传成功")
    private Boolean downOrUpload;

    /// 交易对账文件是否比对完成
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易对账文件是否比对完成")
    private Boolean compare;

    /// 交易对账结果
    /// @see ReconcileResultEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易对账结果")
    private String result;

}

