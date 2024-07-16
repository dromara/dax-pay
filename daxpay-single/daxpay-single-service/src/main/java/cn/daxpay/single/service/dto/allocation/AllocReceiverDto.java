package cn.daxpay.single.service.dto.allocation;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.daxpay.single.core.code.AllocReceiverTypeEnum;
import cn.daxpay.single.core.code.AllocRelationTypeEnum;
import cn.daxpay.single.core.code.PayChannelEnum;
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
public class AllocReceiverDto extends BaseDto {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "账号别名")
    private String name;

    @Schema(description = "接收方编号")
    private String receiverNo;

    /**
     * @see PayChannelEnum
     */
    @Schema(description = "所属通道")
    private String channel;

    /**
     * 分账接收方类型 个人/商户
     * @see AllocReceiverTypeEnum
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
