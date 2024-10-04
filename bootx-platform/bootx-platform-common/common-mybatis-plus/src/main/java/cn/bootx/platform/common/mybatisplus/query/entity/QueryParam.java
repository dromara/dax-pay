package cn.bootx.platform.common.mybatisplus.query.entity;

import cn.bootx.platform.common.mybatisplus.query.code.CompareTypeEnum;
import cn.bootx.platform.common.mybatisplus.query.code.ParamTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xxm
 * @since 2021/11/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "查询项")
public class QueryParam {

    @Schema(description = "拼接条件是否为或")
    private boolean or;

    @Schema(description = "参数名称")
    private String paramName;

    /**
     * 比较类型
     * @see CompareTypeEnum
     */
    @Schema(description = "比较类型")
    private String compareType;

    /**
     * @see ParamTypeEnum
     */
    @Schema(description = "参数类型")
    private String paramType;

    @Schema(description = "参数值")
    private Object paramValue;

    @Schema(description = "参数名称是否需要转换成下划线命名")
    private boolean underLine = true;

    @Schema(description = "嵌套查询")
    private List<QueryParam> nestedParams;

}
