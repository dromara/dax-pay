package cn.bootx.platform.common.mybatisplus.query.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 查询参数
 *
 * @author xxm
 * @since 2021/11/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "查询参数")
public class QueryParams {

    @Schema(description = "参数集合")
    private List<QueryParam> queryParams;

    @Schema(description = "排序集合")
    private List<SortParam> queryOrders;

}
