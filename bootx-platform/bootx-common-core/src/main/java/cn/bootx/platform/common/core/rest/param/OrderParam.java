package cn.bootx.platform.common.core.rest.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 排序参数
 *
 * @author xxm
 * @since 2022/3/10
 */
@Getter
@Setter
@Schema(title = "分页查询参数")
public class OrderParam {

    @Schema(description = "排序字段")
    private String sortField;

    @Schema(description = "是否升序")
    private boolean asc = true;

}
