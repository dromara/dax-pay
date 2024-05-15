package cn.bootx.platform.iam.core.third.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.iam.core.third.convert.UserThirdInfoConvert;
import cn.bootx.platform.common.mybatisplus.base.MpDelEntity;
import cn.bootx.platform.iam.dto.user.UserThirdInfoDto;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("iam_user_third_info")
@Accessors(chain = true)
public class UserThirdInfo extends MpDelEntity implements EntityBaseFunction<UserThirdInfoDto> {

    /** 用户id */
    private Long userId;

    /** 第三方终端类型 */
    private String clientCode;

    /** 三方平台用户名 */
    private String username;

    /** 三方平台用户昵称 */
    private String nickname;

    /** 三方平台用户头像 */
    private String avatar;

    /** 关联第三方平台中的用户id(例如钉钉) */
    private String thirdUserId;

    /** 转换成dto */
    @Override
    public UserThirdInfoDto toDto() {
        return UserThirdInfoConvert.CONVERT.convert(this);
    }

}
