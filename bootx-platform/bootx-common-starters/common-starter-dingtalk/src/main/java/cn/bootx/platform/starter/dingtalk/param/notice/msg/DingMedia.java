package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 钉钉图片
 *
 * @author xxm
 * @since 2022/7/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉媒体")
@NoArgsConstructor
@AllArgsConstructor
public class DingMedia {

    @JsonProperty("media_id")
    @Schema(description = "媒体文件id")
    private String mediaId;

}
