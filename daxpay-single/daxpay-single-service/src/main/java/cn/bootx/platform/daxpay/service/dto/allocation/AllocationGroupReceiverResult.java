package cn.bootx.platform.daxpay.service.dto.allocation;

import cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum;
import cn.bootx.platform.daxpay.code.AllocationRelationTypeEnum;
import cn.bootx.platform.starter.data.perm.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账组接收方信息
 * @author xxm
 * @since 2024/4/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账组接收方信息")
public class AllocationGroupReceiverResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "接收方ID")
    private Long receiverId;

    @Schema(description = "接收方账号别名")
    private String name;

    @Schema(description = "分账比例(万分之多少)")
    private Integer rate;

    /**
     * 分账接收方类型
     * @see AllocationReceiverTypeEnum
     */
    @Schema(description = "分账接收方类型")
    private String receiverType;


    @Schema(description = "接收方账号")
    @SensitiveInfo
    private String receiverAccount;

    /** 接收方姓名 */
    @Schema(description = "接收方姓名")
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocationRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    private String relationType;

    @Schema(description = "关系名称")
    private String relationName;
}
