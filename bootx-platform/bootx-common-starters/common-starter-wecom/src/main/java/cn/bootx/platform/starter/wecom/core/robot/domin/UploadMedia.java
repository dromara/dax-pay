package cn.bootx.platform.starter.wecom.core.robot.domin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;

/**
 * 企微文件上传
 *
 * @author xxm
 * @since 2022/7/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "企微文件上传")
public class UploadMedia {

    @Schema(description = "文件名称")
    private String filename;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件流")
    private InputStream inputStream;

}
