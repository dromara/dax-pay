package org.dromara.daxpay.service.result.allocation.receiver;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.AllocRelationTypeEnum;
import org.dromara.daxpay.core.result.MchAppResult;

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
public class AllocGroupReceiverVo extends MchAppResult {

    @Schema(description = "接收方ID")
    private Long receiverId;

    @Schema(description = "接收方编号")
    private String receiverNo;

    @Schema(description = "接收方名称")
    private String name;

    @Schema(description = "分账比例(百分之多少)")
    private BigDecimal rate;

    /**
     * 分账接收方类型
     * @see org.dromara.daxpay.core.enums.AllocReceiverTypeEnum
     */
    @Schema(description = "分账接收方类型")
    private String receiverType;

    @Schema(description = "接收方账号")
    @SensitiveInfo
    private String receiverAccount;

    /** 接收方姓名 */
    @Schema(description = "接收方姓名")
    @SensitiveInfo(SensitiveInfo.SensitiveType.CHINESE_NAME)
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    private String relationType;

    @Schema(description = "关系名称")
    private String relationName;
}
