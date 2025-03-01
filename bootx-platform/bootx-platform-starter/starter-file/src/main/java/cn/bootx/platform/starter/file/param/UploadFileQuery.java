package cn.bootx.platform.starter.file.param;

import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 上传文件查询参数
 * @author xxm
 * @since 2023/11/15
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(title = "上传文件查询参数")
public class UploadFileQuery {

    /** 文件访问地址 */
    @Schema(description = "文件访问地址")
    private String url;

    /** 文件名称 */
    @Schema(description = "文件名称")
    private String filename;

    /** 原始文件名 */
    @Schema(description = "原始文件名")
    private String originalFilename;

    /** 文件扩展名 */
    @Schema(description = "文件扩展名")
    private String ext;

    /** MIME 类型 */
    @Schema(description = "MIME 类型")
    private String contentType;

}
