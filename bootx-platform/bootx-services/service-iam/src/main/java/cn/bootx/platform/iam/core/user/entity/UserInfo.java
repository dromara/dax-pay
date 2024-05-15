package cn.bootx.platform.iam.core.user.entity;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.LongListTypeHandler;
import cn.bootx.platform.iam.code.UserStatusCode;
import cn.bootx.platform.iam.core.user.convert.UserConvert;
import cn.bootx.platform.iam.dto.online.OnlineUserInfoDto;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户的核心信息
 *
 * @author xxm
 * @since 2020/4/24 15:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "iam_user_info",autoResultMap = true)
public class UserInfo extends MpBaseEntity implements EntityBaseFunction<UserInfoDto> {

    /** 名称 */
    private String name;

    /** 账号 */
    private String username;

    /** 密码 */
    private String password;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 关联终端id集合 */
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> clientIds;

    /** 是否管理员 */
    private boolean administrator;

    /**
     * 账号状态
     * @see UserStatusCode
     */
    private String status;

    @Override
    public UserInfoDto toDto() {
        return UserConvert.CONVERT.convert(this);
    }

    public OnlineUserInfoDto toOnline(){
        return UserConvert.CONVERT.toOnline(this);
    }

    public static UserInfo init(UserInfoParam param) {
        return UserConvert.CONVERT.convert(param);
    }

    public UserDetail toUserDetail() {
        return new UserDetail().setId(this.getId())
            .setPassword(this.password)
            .setUsername(this.getUsername())
            .setName(this.name)
            .setAdmin(this.administrator)
            .setStatus(this.status);
    }

}
