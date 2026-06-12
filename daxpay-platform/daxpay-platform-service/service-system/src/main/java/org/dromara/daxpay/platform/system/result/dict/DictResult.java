package org.dromara.daxpay.platform.system.result.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// # 数据字典目录返回结果
///
@Data
@Accessors(chain = true)
@Schema(title = "数据字典目录")
public class DictResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "中文名称")
    private String nameCn;

    @Schema(description = "英文名称")
    private String nameEn;

    @Schema(description = "启用状态")
    private Boolean enable;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "是否内置")
    private Boolean internal;

    @Schema(description = "描述")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
