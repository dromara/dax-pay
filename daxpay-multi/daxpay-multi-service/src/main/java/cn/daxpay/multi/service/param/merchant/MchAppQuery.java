package cn.daxpay.multi.service.param.merchant;

import cn.daxpay.multi.core.enums.MerchantNotifyTypeEnum;
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
@Schema(title = "商户应用查询参数")
public class MchAppQuery {

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 应用名称 */
    @Schema(description = "应用号")
    private String appId;

    /** 应用名称 */
    @Schema(description = "应用名称")
    private String appName;

    /** 签名方式 */
    @Schema(description = "签名方式")
    private String signType;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see MerchantNotifyTypeEnum
     */
    @Schema(description = "异步消息通知类型")
    private String notifyType;


}
