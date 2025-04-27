package org.dromara.daxpay.channel.alipay.param.allocation;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝分账接收方绑定
 * @author xxm
 * @since 2025/1/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝分账接收方绑定")
public class AlipayAllocReceiverBindParam {

    /** 主键 */
    @NotNull(message = "id不能为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /** 接收方账号 */
    @NotBlank(message = "接收方账号不能为空")
    @Schema(description = "接收方账号")
    private String receiverAccount;

    /**
     * 接收方类型
     * @see AllocReceiverTypeEnum
     */
    @NotBlank(message = "接收方类型不能为空")
    @Schema(description = "接收方类型")
    private String receiverType;

    /** 接收方名称 */
    @Schema(description = "接收方名称")
    private String receiverName;

    /** 分账关系类型 */
    @Schema(description = "分账关系类型")
    private String relationType;

    /** 分账关系名称 */
    @Schema(description = "分账关系名称")
    private String relationName;

    /** 是否服务商模式 */
    @Schema(description = "是否服务商模式")
    private boolean isv;

    /** 商户AppId */
    @NotBlank(message = "商户AppId不能为空")
    @Schema(description = "商户AppId")
    private String appId;


}
