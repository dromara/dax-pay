package cn.bootx.platform.iam.core.security.password.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbComment;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 密码登录失败记录
 * @author xxm
 * @since 2023/8/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@DbTable(comment =  "密码登录失败记录")
@Accessors(chain = true)
@TableName("iam_password_login_fail_record")
public class PasswordLoginFailRecord extends MpBaseEntity {

    /** 用户id */
    @DbComment("用户id")
    private Long userId;
    /** 登录失败次数 */
    @DbComment("登录失败次数")
    private Integer failCount;
    /** 登录失败时间 */
    @DbComment("登录失败时间")
    private LocalDateTime failTime;
}
