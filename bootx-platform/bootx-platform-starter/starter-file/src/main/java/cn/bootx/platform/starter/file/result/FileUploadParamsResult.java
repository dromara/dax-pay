package cn.bootx.platform.starter.file.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 文件上传参数
 */
@Schema(description = "文件上传参数")
@Data
@Accessors(chain = true)
public class FileUploadParamsResult {

    @Schema(description = "文件上传地址")
    private String url;

    @Schema(description = "上传后文件名称")
    private String attachName;

    @Schema(description = "文件上传请求头,上传时放在请求头里")
    private Map<String,String> headers;

}
