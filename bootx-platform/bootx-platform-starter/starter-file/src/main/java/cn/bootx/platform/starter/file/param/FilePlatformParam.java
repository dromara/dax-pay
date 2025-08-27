package cn.bootx.platform.starter.file.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 存储平台配置参数
 * @author xxm
 * @since 2024/8/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "存储平台配置参数")
public class FilePlatformParam {

    @NotNull(message = "主键不得为空")
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "平台地址")
    private String url;

    /** 前端直传 */
    @Schema(description = "前端直传")
    private Boolean frontendUpload;

}
