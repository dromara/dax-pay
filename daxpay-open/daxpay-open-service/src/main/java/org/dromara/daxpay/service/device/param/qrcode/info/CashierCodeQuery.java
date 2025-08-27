package org.dromara.daxpay.service.device.param.qrcode.info;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 收银码牌查询参数
 * @author xxm
 * @since 2025/7/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "收银码牌查询参数")
public class CashierCodeQuery extends SortParam {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "编码")
    private String code;
}
