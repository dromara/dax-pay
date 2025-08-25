package org.dromara.daxpay.service.pay.param.constant;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道终端报送类型
 * @author xxm
 * @since 2025/3/12
 */
@EqualsAndHashCode(callSuper = true)
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(title = "通道终端报送类型查询")
public class TerminalConstQuery extends SortParam {

    /** 通道编码 */
    @Schema(description = "通道编码")
    private String code;
}
