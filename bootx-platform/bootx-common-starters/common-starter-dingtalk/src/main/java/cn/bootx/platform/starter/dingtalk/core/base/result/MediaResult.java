package cn.bootx.platform.starter.dingtalk.core.base.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钉钉媒体上传返回类
 *
 * @author xxm
 * @since 2022/7/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉媒体上传返回类")
public class MediaResult {

    @JsonProperty("errcode")
    @Schema(description = "错误码")
    private Integer code;

    @JsonProperty("errmsg")
    @Schema(description = "返回码描述")
    private String msg;

    @JsonProperty("type")
    @Schema(description = "媒体文件类型")
    private String type;

    @JsonProperty("media_id")
    @Schema(description = "唯一标识")
    private String mediaId;

    @JsonProperty("created_at")
    @Schema(description = "媒体文件上传时间戳")
    private long createdAt;

}
