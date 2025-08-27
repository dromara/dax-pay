package cn.bootx.platform.starter.file.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 上传文件信息
 * @author xxm
 * @since 2025/5/15
 */
@Data
@Accessors(chain = true)
@Schema(title = "上传文件信息")
public class UploadFileInfoParam {

    /**
     * 文件访问地址(名称)
     */
    @Schema(description = "文件访问地址")
    private String url;

    /**
     * 文件大小，单位字节
     */
    @Schema(description = "文件大小，单位字节")
    private Long size;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    private String filename;

    /**
     * 原始文件名
     */
    @Schema(description = "原始文件名")
    private String originalFilename;

    /**
     * 基础存储路径
     */
    @Schema(description = "基础存储路径")
    private String basePath;

    /**
     * 存储路径
     */
    @Schema(description = "存储路径")
    private String path;

    /**
     * 文件扩展名
     */
    @Schema(description = "文件扩展名")
    private String ext;

    /**
     * MIME 类型
     */
    @Schema(description = "MIME 类型")
    private String contentType;

    /**
     * 存储平台
     */
    @Schema(description = "存储平台")
    private String platform;

    /**
     * 缩略图访问路径
     */
    @Schema(description = "缩略图访问路径")
    private String thUrl;

    /**
     * 缩略图名称
     */
    @Schema(description = "缩略图名称")
    private String thFilename;

    /**
     * 缩略图大小，单位字节
     */
    @Schema(description = "缩略图大小，单位字节")
    private Long thSize;

    /**
     * 缩略图 MIME 类型
     */
    @Schema(description = "缩略图 MIME 类型")
    private String thContentType;

    /**
     * 文件所属对象id
     */
    @Schema(description = "文件所属对象id")
    private String objectId;

    /**
     * 文件所属对象类型，例如用户头像，评价图片
     */
    @Schema(description = "文件所属对象类型，例如用户头像，评价图片")
    private String objectType;
}
