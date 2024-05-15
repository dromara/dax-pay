package cn.bootx.platform.common.core.rest.param;

import cn.bootx.platform.common.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2021/11/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "查询排序")
public class QueryOrder {

    @QueryParam(ignore = true)
    @Schema(description = "设置排序字段")
    private String sortField;

    @QueryParam(ignore = true)
    @Schema(description = "是否升序")
    private boolean asc = true;

    @QueryParam(ignore = true)
    @Schema(description = "参数名称是否需要转换成下划线命名")
    private boolean underLine = true;

}
