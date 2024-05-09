package cn.bootx.platform.starter.dingtalk.core.base.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "AccessToken响应类")
public class AccessTokenResult<T> extends DingTalkResult<T> {

    @JsonProperty("access_token")
    @Schema(description = "访问凭证")
    private String accessToken;

    @JsonProperty("expires_in")
    @Schema(description = "过期时间")
    private Integer expiresIn;

}
