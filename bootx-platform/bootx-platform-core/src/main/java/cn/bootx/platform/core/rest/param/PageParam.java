package cn.bootx.platform.core.rest.param;

import cn.hutool.core.util.PageUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分页查询参数
 */
@Getter
@Setter
@Schema(title = "分页查询参数")
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {

    /**
     * 当前页
     */
    @Schema(description = "当前页", defaultValue = "1")
    private int current = 1;

    /**
     * 每页显示条数，默认 10
     */
    @Schema(description = "每页显示条数，默认 10", defaultValue = "10")
    private int size = 10;

    /**
     * 开始条数
     */
    public int start() {
        // hutool 的下标从0开始
        return PageUtil.transToStartEnd(current - 1, size)[0];
    }

    /**
     * 结束条数
     */
    public int end() {
        // hutool 的下标从0开始
        return PageUtil.transToStartEnd(current - 1, size)[1];
    }

}
