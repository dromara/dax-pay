package cn.bootx.platform.common.mybatisplus.query.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Between 类型参数
 *
 * @author xxm
 * @since 2021/11/18
 */
@Data
@Accessors(chain = true)
@Schema(title = "Between 类型参数")
public class QueryBetweenParam {

    @Schema(description = "开始参数")
    private Object start;

    @Schema(description = "结束参数")
    private Object end;

}
