package org.dromara.daxpay.service.common.param;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户查询参数
 * @author xxm
 * @since 2024/8/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户查询参数")
public class MchAppQuery extends SortParam {

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;
}
