package org.dromara.daxpay.service.result.allocation.receiver;

import cn.bootx.platform.common.jackson.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.result.MchAppResult;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/3/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方")
public class AllocReceiverVo extends MchAppResult {

    @Schema(description = "接收方编号")
    private String receiverNo;

    /** 名称 */
    private String name;

    /**
     * @see ChannelEnum
     */
    @Schema(description = "所属通道")
    private String channel;

    /**
     * 分账接收方类型 个人/商户
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
     * @see org.dromara.daxpay.core.enums.AllocRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    private String relationType;

    @Schema(description = "关系名称")
    private String relationName;
}
