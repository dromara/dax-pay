package cn.bootx.platform.daxpay.service.dto.channel.union;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.service.code.UnionPayRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 云闪付流水记录
 * @author xxm
 * @since 2024/3/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "云闪付流水记录")
public class UnionPayRecordDto extends BaseDto {

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 金额 */
    @Schema(description = "金额")
    private Integer amount;

    /**
     * 业务类型
     * @see UnionPayRecordTypeEnum
     */
    @Schema(description = "业务类型")
    private String type;

    /** 本地订单号 */
    @Schema(description = "本地订单号")
    private Long orderId;

    /** 网关订单号 */
    @Schema(description = "网关订单号")
    private String gatewayOrderNo;

    /** 网关完成时间 */
    @Schema(description = "网关完成时间")
    private LocalDateTime gatewayTime;
}
