package cn.bootx.platform.baseapi.param.dict;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/4/10 14:46
 */
@Data
@Accessors(chain = true)
@Schema(title = "数据字典目录")
public class DictionaryParam implements Serializable {

    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    @NotEmpty(message = "编码不可以为空", groups = ValidationGroup.add.class)
    @Schema(description = "编码")
    private String code;

    @NotEmpty(message = "编码不可以为空", groups = ValidationGroup.add.class)
    @Schema(description = "名称")
    private String name;

    @NotNull(message = "启用状态不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "启用状态")
    private Boolean enable;

    @Schema(description = "分类标签")
    private String groupTag;

    @Schema(description = "描述")
    private String remark;

}
