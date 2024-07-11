package cn.bootx.platform.iam.result.permission;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 简单路径权限返回类
 * @author xxm
 * @since 2024/7/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "简单路径权限返回类")
public class SimplePermPathResult {

    @Schema(description = "id")
    private Long id;

    /**
     * 因为前端树进行搜索时, 需要获取上级节点标识来展开, 唯一标识是id
     */
    @Schema(description = "父级ID")
    private Long pid;

    @JsonIgnore
    @Schema(description = "上级编码")
    private String parentCode;

    @JsonIgnore
    @Schema(description = "标识(分组/模块)")
    private String code;

    @JsonIgnore
    @Schema(description = "名称")
    private String name;

    @JsonIgnore
    @Schema(description = "请求类型")
    private String method;

    @JsonIgnore
    @Schema(description = "请求路径")
    private String path;

    @Schema(description = "子节点")
    private List<SimplePermPathResult> children;

    /**
     * 显示标题
     */
    public String getTitle(){
        if (StrUtil.isBlank(path)){
            return name;
        }
        return StrUtil.format("[{}] {} ({})", method, name, path);
    }
}
