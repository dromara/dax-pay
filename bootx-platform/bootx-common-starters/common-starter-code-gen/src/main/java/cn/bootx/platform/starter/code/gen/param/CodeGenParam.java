package cn.bootx.platform.starter.code.gen.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 代码生成参数
 *
 * @author xxm
 * @since 2022/2/18
 */
@Data
@Accessors(chain = true)
@Schema(title = "代码生成参数")
public class CodeGenParam {

    @Schema(description = "数据库源")
    private String dataSourceCode;

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "生成对象名称")
    private String entityName;

    @Schema(description = "基于什么类型的基类")
    private String baseEntity;

    @Schema(description = "编辑窗类型")
    private String editType;

    @Schema(description = "编辑窗类型")
    private String deleteType;

    @Schema(description = "vue版本")
    private String vueVersion;

    @Schema(description = "core目录(service/entity/dao等所在的包)")
    private String corePack;

    @Schema(description = "param参数类包名")
    private String paramPack;

    @Schema(description = "dto参数类包名")
    private String dtoPack;

    @Schema(description = "控制器包名")
    private String controllerPack;

    @Schema(description = "请求地址")
    private String requestPath;

    @Schema(description = "前端接口文件所在目录")
    private String vueApiPath;

    @Schema(description = "创建人")
    private String author;

}
