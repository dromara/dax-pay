package cn.bootx.platform.daxpay.service.dto.channel.voucher;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.service.code.VoucherRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 储值卡记录
 * @author xxm
 * @since 2024/2/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "储值卡记录")
public class VoucherRecordDto extends BaseDto {

    /** 储值卡id */
    @Schema(description = "储值卡id")
    private Long voucherId;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /**
     * 业务类型
     * @see VoucherRecordTypeEnum
     */
    @Schema(description = "业务类型")
    private String type;

    /** 金额 */
    @Schema(description = "金额")
    private Integer amount;

    /** 变动之前的金额 */
    @Schema(description = "变动之前的金额")
    private Integer oldAmount;

    /** 变动之后的金额 */
    @Schema(description = "变动之后的金额")
    private Integer newAmount;

    /**
     * 交易订单号
     * 支付订单/退款订单
     */
    @Schema(description = "交易订单号")
    private String orderId;

    /** 终端ip */
    @Schema(description = "终端ip")
    private String ip;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
