package cn.daxpay.multi.service.result.merchant;

import cn.daxpay.multi.core.enums.SignTypeEnum;
import cn.daxpay.multi.core.enums.TradeNotifyTypeEnum;
import cn.daxpay.multi.service.enums.MchAppStautsEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class MchAppResult {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;

    /** 应用名称 */
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 签名方式
     * @see SignTypeEnum
     */
    @Schema(description = "签名方式")
    private String signType;

    /** 签名秘钥 */
    @Schema(description = "签名秘钥")
    private String signSecret;

    /** 是否对请求进行验签 */
    @Schema(description = "是否对请求进行验签")
    private boolean reqSign;

    /** 支付限额 */
    @Schema(description = "支付限额")
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    @Schema(description = "订单默认超时时间(分钟)")
    private Integer orderTimeout;

    /**
     * 状态
     * @see MchAppStautsEnum
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see TradeNotifyTypeEnum
     */
    @Schema(description = "异步消息通知类型")
    private String notifyType;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(description = "通知地址")
    private String notifyUrl;

}
