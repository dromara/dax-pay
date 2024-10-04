package org.dromara.daxpay.service.entity.merchant;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户商户关联关系
 * @author xxm
 * @since 2024/8/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
@TableName("pay_merchant_user")
public class MerchantUser extends MpCreateEntity {

    /** 用户ID */
    private Long userId;


    /** 是否为商户管理员 */
    private boolean administrator;
}
