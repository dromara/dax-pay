package cn.bootx.platform.daxpay.service.dto.order.reconcile;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.service.code.ReconcileResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 对账订单
 * @author xxm
 * @since 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账订单")
public class ReconcileOrderDto extends BaseDto {

    /** 对账号 */
    @Schema(description = "对账号")
    private String reconcileNo;

    /** 日期 */
    @Schema(description = "日期")
    private LocalDate date;

    /** 通道 */
    @Schema(description = "通道")
    private String channel;

    /** 是否下载成功 */
    @Schema(description = "是否下载或上传")
    private boolean downOrUpload;

    /** 是否比对完成 */
    @Schema(description = "是否比对完成")
    private boolean compare;

    /**
     * 对账结果
     * @see ReconcileResultEnum
     */
    @Schema(description = "对账结果")
    private String result;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;
}
