package cn.daxpay.multi.service.param.merchant;

import cn.bootx.platform.core.validation.ValidationGroup;
import cn.daxpay.multi.core.enums.TradeNotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

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
    @Schema(title = "主键")
    @NotNull(message = "主键ID不可为空", groups = ValidationGroup.edit.class)
    private Long id;

    /** 商户号 */
    @Schema(title = "商户号")
    @NotNull(message = "商户号不可为空", groups = ValidationGroup.add.class)
    private String mchNo;

    /** 应用名称 */
    @Schema(title = "应用名称")
    @NotNull(message = "应用名称不可为空", groups = ValidationGroup.add.class)
    private String appName;

    /** 签名方式 */
    @Schema(title = "签名方式")
    @NotNull(message = "签名方式不可为空", groups = ValidationGroup.add.class)
    private String signType;

    /** 公钥 */
    @Schema(title = "公钥")
    private String publicKey;

    /** 私钥 */
    @Schema(title = "私钥")
    private String privateKey;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see TradeNotifyTypeEnum
     */
    @Schema(title = "异步消息通知类型")
    private String notifyType;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(title = "通知地址")
    private String notifyUrl;
}
