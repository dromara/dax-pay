package org.dromara.daxpay.service.param.reconcile;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.common.param.MchAppQuery;
import org.dromara.daxpay.service.enums.ReconcileResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 对账单查询参数
 * @author xxm
 * @since 2024/8/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "对账单查询参数")
public class ReconcileStatementQuery extends MchAppQuery {

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 对账号 */
    @Schema(description = "对账号")
    private String reconcileNo;

    /** 日期 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "日期")
    private LocalDate date;

    /**
     * 通道
     * @see ChannelEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道")
    private String channel;

    /** 交易对账文件是否下载或上传成功 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易对账文件是否下载或上传成功")
    private Boolean downOrUpload;

    /** 交易对账文件是否比对完成 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易对账文件是否比对完成")
    private Boolean compare;

    /**
     * 交易对账结果
     * @see ReconcileResultEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "交易对账结果")
    private String result;

}
