package cn.daxpay.single.service.dto.allocation;

import cn.daxpay.single.core.code.AllocReceiverTypeEnum;
import cn.daxpay.single.core.code.AllocRelationTypeEnum;
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
public class AllocGroupReceiverResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "接收方ID")
    private Long receiverId;

    @Schema(description = "接收方编号")
    private String receiverNo;

    @Schema(description = "分账比例(万分之多少)")
    private Integer rate;

    /**
     * 分账接收方类型
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
