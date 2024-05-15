package cn.bootx.platform.iam.core.security.password.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.iam.core.security.password.convert.PasswordSecurityConfigConvert;
import cn.bootx.platform.iam.dto.security.PasswordSecurityConfigDto;
import cn.bootx.platform.iam.param.security.PasswordSecurityConfigParam;
import cn.bootx.table.modify.annotation.DbComment;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 密码安全策略
 * @author xxm
 * @since 2023/8/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@DbTable(comment = "密码安全策略")
@Accessors(chain = true)
@TableName("iam_password_security_config")
public class PasswordSecurityConfig extends MpBaseEntity implements EntityBaseFunction<PasswordSecurityConfigDto> {

    /** 最大密码错误数 */
    @DbComment("最大密码错误数")
    private int  maxPwdErrorCount;

    /** 密码错误锁定时间(分钟) */
    @DbComment("密码错误锁定时间(分钟)")
    private int errorLockTime;

    /** 强制修改初始密码 */
    @DbComment("强制修改初始密码")
    private  boolean  requireChangePwd;

    /** 更新频率(天数) */
    @DbComment("更新频率")
    private int updateFrequency;

    /** 到期提醒(天数) */
    @DbComment("到期提醒(天数)")
    private int expireRemind;

    /** 与登录名相同 */
    @DbComment("与登录名相同")
    private boolean sameAsLoginName;

    /** 不能与近期多少次密码相同 */
    @DbComment("不能与近期多少次密码相同")
    private  int recentPassword;

    /** 创建对象 */
    public static PasswordSecurityConfig init(PasswordSecurityConfigParam in) {
        return PasswordSecurityConfigConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public PasswordSecurityConfigDto toDto() {
        return PasswordSecurityConfigConvert.CONVERT.convert(this);
    }


}
