package org.dromara.daxpay.service.device.param.qrcode.config;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 码牌配置查询参数
 * @author xxm
 * @since 2025/7/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "码牌配置查询参数")
public class CashierCodeConfigQuery extends SortParam {

    @Schema(description = "配置名称")
    private String name;
}
