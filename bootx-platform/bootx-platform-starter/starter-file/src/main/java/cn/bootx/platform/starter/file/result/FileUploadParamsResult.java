package cn.bootx.platform.starter.file.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Schema(description = "文件上传参数")
@Data
public class FileUploadParamsResult {

    @Schema(description = "文件上传地址")
    private String url;

    @Schema(description = "文件上传请求头,上传时放在请求头里")
    private Map<String,String> headers;

}
