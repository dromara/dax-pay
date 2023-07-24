package cn.bootx.platform.daxpay.param.channel.wechat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 钱包配置
 * @author xxm
 * @since 2023/7/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包配置")
public class WalletConfigParam {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppCode;

    @Schema(description = "默认余额")
    private BigDecimal defaultBalance;

    @Schema(description = "备注")
    private String remark;


}
