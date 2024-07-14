package cn.bootx.platform.iam.result.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 请求资源
 * @author xxm
 */
@Data
@Accessors(chain = true)
@Schema(title = "请求资源")
public class PermPathResult {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "上级编码")
    private String parentCode;

    @Schema(description = "标识(分组/模块)")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "请求类型")
    private String method;

    @Schema(description = "请求路径")
    private String path;

    @Schema(description = "叶子节点")
    private boolean leaf;

    @Schema(description = "子节点")
    private List<PermPathResult> children;

    /**
     * 转简单对象
     */
    public SimplePermPathResult toSimple(){
        SimplePermPathResult simplePermPathResult = new SimplePermPathResult();
        simplePermPathResult.setId(id)
                .setParentCode(parentCode)
                .setCode(code)
                .setName(name)
                .setMethod(method)
                .setPath(path);
        return simplePermPathResult;
    }
}
