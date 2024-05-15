package cn.bootx.platform.iam.core.third.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.iam.core.third.convert.UserThirdConvert;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.iam.dto.user.UserThirdDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户三方登录
 *
 * @author xxm
 * @since 2021/8/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("iam_user_third")
public class UserThird extends MpBaseEntity implements EntityBaseFunction<UserThirdDto> {

    /** 用户id */
    private Long userId;

    /** 微信(公众号) */
    private String weChatId;

    /** 微信(开放平台) */
    private String weChatOpenId;

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

    @Override
    public UserThirdDto toDto() {
        return UserThirdConvert.CONVERT.convert(this);
    }

}
