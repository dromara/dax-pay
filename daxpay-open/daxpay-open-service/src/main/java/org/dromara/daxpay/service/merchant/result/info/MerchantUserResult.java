package org.dromara.daxpay.service.merchant.result.info;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.code.UserStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户用户信息
 * @author xxm
 * @since 2025/8/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户用户信息")
public class MerchantUserResult extends MchResult implements ToResult<MerchantUserResult> {
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

    /**
     * 转换
     */
    @Override
    public MerchantUserResult toResult() {
        return this;
    }
}
