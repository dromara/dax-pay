package cn.daxpay.single.service.param.channel.wechat;

import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 可用支付方式 */
    @DbColumn(comment = "可用支付方式")
    private List<String> payWays;

    /** 单次支付最多少金额 */
    @Schema(description = "单次支付最多少金额")
    private Integer limitAmount;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
