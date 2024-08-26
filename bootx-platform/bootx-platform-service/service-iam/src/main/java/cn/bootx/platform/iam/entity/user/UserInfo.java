package cn.bootx.platform.iam.entity.user;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.iam.code.UserStatusEnum;
import cn.bootx.platform.iam.convert.user.UserConvert;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户核心信息
 *
 * @author xxm
 * @since 2020/4/24 15:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_info")
public class UserInfo extends MpBaseEntity implements ToResult<UserInfoResult> {

    /** 名称 */
    private String name;

    /** 账号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String account;

    /** 密码 */
    private String password;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 是否管理员, 管理员用户不在列表中显示 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private boolean administrator;

    /**
     * 账号状态
     * @see UserStatusEnum
     */
    private String status;

    @Override
    public UserInfoResult toResult() {
        return UserConvert.CONVERT.convert(this);
    }

    public static UserInfo init(UserInfoParam param) {
        return UserConvert.CONVERT.convert(param);
    }

    public UserDetail toUserDetail() {
        return new UserDetail().setId(this.getId())
            .setPassword(this.password)
            .setAccount(this.getAccount())
            .setName(this.name)
            .setAdmin(this.administrator)
            .setStatus(this.status);
    }

}
