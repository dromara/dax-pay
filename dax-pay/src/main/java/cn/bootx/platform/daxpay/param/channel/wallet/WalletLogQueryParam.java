package cn.bootx.platform.daxpay.param.channel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xxm
 * @date 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包日志查询参数")
public class WalletLogQueryParam implements Serializable {

    private static final long serialVersionUID = -4046664021959786637L;

    @Schema(description = "钱包ID (与userId至少存在一个)")
    private Long walletId;

    @Schema(description = "用户ID (钱包至少存在一个)")
    private Long userId;

    @Schema(description = "开始日期")
    private LocalDateTime startDate;

    @Schema(description = "结束日期")
    private LocalDateTime endDate;

    @Schema(description = "日志类型，不传则查询全部")
    private List<Integer> type;

}
