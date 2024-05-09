package cn.bootx.platform.iam.dto.user;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户三方登录绑定详情
 *
 * @author xxm
 * @since 2022-07-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "用户三方登录绑定详情")
@Accessors(chain = true)
public class UserThirdInfoDto extends BaseDto {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "第三方终端类型")
    private String clientCode;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

}
