package cn.daxpay.multi.service.result.merchant;

import cn.daxpay.multi.core.enums.TradeNotifyTypeEnum;
import cn.daxpay.multi.service.enums.MchAppStautsEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class MchAppResult {

    /** 主键 */
    @Schema(title = "主键")
    private Long id;

    /** 商户号 */
    @Schema(title = "商户号")
    private String mchNo;

    /** 应用号 */
    @Schema(title = "应用号")
    private String appId;

    /** 应用名称 */
    @Schema(title = "应用名称")
    private String appName;

    /** 签名方式 */
    @Schema(title = "签名方式")
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
     * 状态
     * @see MchAppStautsEnum
     */
    @Schema(title = "状态")
    private String status;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(title = "通知地址")
    private String notifyUrl;

}
