package cn.bootx.platform.starter.dingtalk.core.base.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 钉钉响应结果
 *
 * @author xxm
 * @since 2020/11/30
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉发送响应类")
public class DingTalkResult<T> implements Serializable {

    private static final long serialVersionUID = 4298060961428118100L;

    @JsonProperty("errcode")
    @Schema(description = "错误码")
    private Integer code;

    @Schema(description = "返回数据")
    private T result;

    @JsonProperty("errmsg")
    @Schema(description = "返回码描述")
    private String msg;

}
