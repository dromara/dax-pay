package cn.daxpay.multi.service.entity.merchant;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户商户关联关系
 * @author xxm
 * @since 2024/8/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_user_merchant")
public class MerchantUser extends MpCreateEntity {

    /** 用户ID */
    private Long userId;

    /** 商户号 */
    private String mchNo;
}
