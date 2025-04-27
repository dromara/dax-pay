package org.dromara.daxpay.service.result.allocation.receiver;

import org.dromara.daxpay.service.common.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 分账组接收方信息
 * @author xxm
 * @since 2024/4/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账组接收方信息")
public class AllocGroupReceiverVo extends MchResult {

    @Schema(description = "接收方ID")
    private Long receiverId;

    @Schema(description = "接收方编号")
    private String receiverNo;

    @Schema(description = "接收方名称")
    private String name;

    @Schema(description = "分账比例(百分之多少)")
    private BigDecimal rate;

    @Schema(description = "接收方类型")
    private String receiverType;

    @Schema(description = "接收方账号")
    private String receiverAccount;

    /** 接收方名称 */
    @Schema(description = "接收方名称")
    private String receiverName;

}
