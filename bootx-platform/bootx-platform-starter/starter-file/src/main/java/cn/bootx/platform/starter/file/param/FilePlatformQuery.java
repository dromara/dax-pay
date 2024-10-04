package cn.bootx.platform.starter.file.param;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 存储平台查询参数
 * @author xxm
 * @since 2024/8/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "存储平台查询参数")
public class FilePlatformQuery extends SortParam {
}
