package cn.bootx.platform.iam.dto.user;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2021/8/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户三方登录关系")
public class UserThirdDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = -7537453271488369310L;

    /** 用户id */
    private Long userId;

    /** 微信openId */
    private String weChatId;

    /** qqId */
    private String qqId;

    /** 微博Id */
    private String weiboId;

    /** 码云唯一标识 */
    private String giteeId;

    /** 钉钉唯一标识 */
    private String dingTalkId;

    /** 企业微信唯一标识 */
    private String weComId;

}
