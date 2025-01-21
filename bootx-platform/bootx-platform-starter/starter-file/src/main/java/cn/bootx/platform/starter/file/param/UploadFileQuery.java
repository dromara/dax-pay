package cn.bootx.platform.starter.file.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 上传文件查询参数
 * @author xxm
 * @since 2023/11/15
 */
@Data
@Accessors(chain = true)
@Schema(title = "上传文件查询参数")
public class UploadFileQuery {

    /** 原始文件名 */
    @Schema(description = "原始文件名")
    private String originalFilename;

    /** 开始时间 */
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /** 文件扩展名 */
    @Schema(description = "文件扩展名")
    private String ext;

    /** MIME 类型 */
    @Schema(description = "MIME 类型")
    private String contentType;

}
