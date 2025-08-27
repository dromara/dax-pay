package org.dromara.daxpay.service.merchant.param.app;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.MchAppStatusEnum;
import org.dromara.daxpay.core.enums.SignTypeEnum;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 商户应用
 * @author xxm
 * @since 2024/6/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户应用")
public class MchAppParam {

    /** 主键 */
    @Schema(description = "主键")
    @NotNull(message = "主键ID不可为空", groups = ValidationGroup.edit.class)
    private Long id;

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 应用名称 */
    @Schema(description = "应用名称")
    @NotNull(message = "应用名称不可为空")
    private String appName;

    /**
     * 签名方式
     * @see SignTypeEnum
     */
    @Schema(description = "签名方式")
    @NotNull(message = "签名方式不可为空")
    private String signType;


    /** 签名秘钥 */
    @Schema(description = "签名秘钥")
    @NotNull(message = "签名秘钥不可为空", groups = ValidationGroup.add.class)
    private String signSecret;

    /** 是否对请求进行验签 */
    @NotNull(message = "是否对请求进行验签不可为空")
    @Schema(description = "是否对请求进行验签")
    private Boolean reqSign;

    /** 是否验证请求时间是否超时 */
    @NotNull(message = "是否验证请求时间是否超时不可为空")
    @Schema(description = "是否验证请求时间是否超时")
    private Boolean reqTimeout;

    /**
     * 请求超时时间(秒)
     * 如果传输的请求时间与当前服务时间差值超过配置的时长, 将会请求失败
     */
    @Min(value = 5, message = "请求超时时间(秒)不可小于5秒")
    @Schema(description = "请求超时时间(秒)")
    private Integer reqTimeoutSecond;

    /** 支付限额 */
    @Schema(description = "支付限额")
    @DecimalMin(value = "0.01", message = "支付限额不可小于0.01元")
    @Digits(integer = 10, fraction = 2, message = "最多只允许两位小数")
    @NotNull(message = "支付限额不可为空")
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    @Schema(description = "订单默认超时时间(分钟)")
    @NotNull(message = "订单默认超时时间(分钟)不可为空")
    @Min(value = 5, message = "订单默认超时时间(分钟)不可小于5分钟")
    private Integer orderTimeout;

    /**
     * 应用状态
     * @see MchAppStatusEnum
     */
    @Schema(description = "应用状态")
    @NotBlank(message = "应用状态不可为空")
    private String status;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see MerchantNotifyTypeEnum
     */
    @Schema(description = "异步消息通知类型")
    private String notifyType;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(description = "通知地址")
    private String notifyUrl;
}
