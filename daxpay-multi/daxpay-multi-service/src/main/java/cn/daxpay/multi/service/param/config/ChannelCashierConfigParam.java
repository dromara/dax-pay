package cn.daxpay.multi.service.param.config;

import cn.daxpay.multi.core.enums.CashierTypeEnum;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道收银台配置")
public class ChannelCashierConfigParam {

    /** 主健 */
    @Schema(description = "主健")
    private Long id;

    /**
     * 收银台类型
     * @see CashierTypeEnum
     */
    @NotBlank(message = "收银台类型不可为空")
    @Schema(description = "收银台类型")
    private String cashierType;

    /**
     * 收银台名称
     */
    @NotBlank(message = "收银台名称不可为空")
    @Schema(description = "收银台名称")
    private String cashierName;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @NotBlank(message = "支付通道不可为空")
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @NotBlank(message = "支付方式不可为空")
    @Schema(description = "支付方式")
    private String payMethod;


    /** 是否开启分账 */
    @NotNull(message = "是否开启分账不可为空")
    @Schema(description = "是否开启分账")
    private Boolean allocation;

    /** 自动分账 */
    @NotNull(message = "自动分账不可为空")
    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 商户号 */
    @NotBlank(message = "商户号不可为空")
    @Schema(description = "商户号")
    private String mchNo;

    /** 商户AppId */
    @Schema(description = "商户AppId")
    @NotBlank(message = "商户AppId不可为空")
    private String appId;
}
