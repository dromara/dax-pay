package cn.bootx.platform.starter.file.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(title = "文件上传请求参数")
@Data
public class FileUploadRequestParams {

    @Schema(description = "文件名称,请携带扩展名")
    @NotBlank(message = "文件名称不可为空")
    private String fileName;

    @Schema(description = "媒体类型 MEDIA_TYPE")
    @NotBlank(message = "媒体类型不可为为空")
    private String mediaType;

    @Schema(description = "文件大小")
    @Min(value = 1, message = "文件大小不可为空")
    private Integer fileSize;
}
