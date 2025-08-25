package org.dromara.daxpay.service.merchant.param.gateway;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.AggregateBarPayTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 聚合付款码支付配置
 * @author xxm
 * @since 2025/3/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "聚合付款码支付配置")
public class AggregateBarPayConfigParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 付款码类型
     * @see AggregateBarPayTypeEnum
     */
    @NotBlank(message = "付款码类型不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "付款码类型")
    private String barPayType;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @NotBlank(message = "支付通道不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @NotBlank(message = "支付方式不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "支付方式")
    private String payMethod;

    /**
     * 其他支付方式
     */
    @Schema(description = "其他支付方式")
    private String otherMethod;




    /** 应用号 */
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    @Schema(description = "应用号")
    private String appId;

}
