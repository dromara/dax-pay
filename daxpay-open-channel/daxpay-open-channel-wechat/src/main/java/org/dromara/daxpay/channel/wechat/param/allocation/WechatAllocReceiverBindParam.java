package org.dromara.daxpay.channel.wechat.param.allocation;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信分账接收方绑定参数
 * @author xxm
 * @since 2025/1/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信分账接收方绑定参数")
public class WechatAllocReceiverBindParam {

    /** 主键 */
    @NotNull(message = "id不能为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 接收方类型
     * @see AllocReceiverTypeEnum
     */
    @NotBlank(message = "接收方类型不可为空")
    @Schema(description = "接收方类型")
    private String receiverType;

    /** 接收方账号 */
    @NotBlank(message = "接收方账号不可为空")
    @Schema(description = "接收方账号")
    private String receiverAccount;


    /** 接收方名称 */
    @Schema(description = "接收方名称")
    private String receiverName;

    /** 分账关系类型 */
    @NotBlank(message = "分账关系类型不可为空")
    @Schema(description = "分账关系类型")
    private String relationType;

    /** 分账关系名称 */
    @Schema(description = "分账关系名称")
    private String relationName;

    /** 是否服务商模式 */
    @Schema(description = "是否服务商模式")
    private boolean isv;

    /** 是否绑定 */
    @Schema(description = "是否绑定")
    private boolean bind;

    /** 应用Id */
    @NotBlank(message = "应用Id不可为空")
    @Schema(description = "应用Id")
    private String appId;

}
