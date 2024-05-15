package cn.bootx.platform.iam.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户三方绑定信息
 *
 * @author xxm
 * @since 2022/7/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户三方绑定信息")
public class UserThirdBindInfo {

    @Schema(description = "微信(公众号)绑定信息")
    private BindInfo weChat;

    @Schema(description = "微信(开放平台)绑定信息")
    private BindInfo weChatOpen;

    @Schema(description = "企业微信绑定信息")
    private BindInfo weCom;

    @Schema(description = "钉钉绑定信息")
    private BindInfo dingTalk;

    @Schema(description = "钉钉绑定信息")
    private BindInfo weChatApplet;


    @Getter
    @Setter
    @Schema(title = "用户信息")
    public static class BindInfo {

        @Schema(description = "是否绑定")
        private boolean bind;

        @Schema(description = "名称")
        private String username;

        @Schema(description = "第三方UUID")
        private String thirdUserId;

    }

}
