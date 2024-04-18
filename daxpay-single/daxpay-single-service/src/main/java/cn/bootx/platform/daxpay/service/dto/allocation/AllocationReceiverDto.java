package cn.bootx.platform.daxpay.service.dto.allocation;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum;
import cn.bootx.platform.daxpay.code.AllocationRelationTypeEnum;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.starter.data.perm.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/3/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方")
public class AllocationReceiverDto extends BaseDto {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "账号别名")
    private String name;

    /**
     * @see PayChannelEnum
     */
    @Schema(description = "所属通道")
    private String channel;

    /**
     * 分账接收方类型 个人/商户
     * @see AllocationReceiverTypeEnum
     */
    @Schema(description = "分账接收方类型")
    private String receiverType;


    @Schema(description = "接收方账号")
    @SensitiveInfo
    private String receiverAccount;

    /** 接收方姓名 */
    @Schema(description = "接收方姓名")
    @SensitiveInfo
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocationRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    private String relationType;

    @Schema(description = "关系名称")
    private String relationName;

    @Schema(description = "是否已经同步到网关")
    private Boolean sync;

    @Schema(description = "备注")
    private String remark;
}
