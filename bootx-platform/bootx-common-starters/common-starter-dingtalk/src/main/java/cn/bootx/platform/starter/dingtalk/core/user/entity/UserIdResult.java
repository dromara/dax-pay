package cn.bootx.platform.starter.dingtalk.core.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2022/7/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉id消息")
public class UserIdResult {

    @JsonProperty("contact_type")
    @Schema(description = "联系类型")
    private String contactType;

    @JsonProperty("userid")
    @Schema(description = "用户id")
    private String userId;

}
