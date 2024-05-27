package cn.daxpay.multi.service.entity.merchant;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户应用
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_mch_app")
public class MchApp extends MpBaseEntity {

    /** 商户号 */
    private String mchNo;

    /** 商户ID */
    private String mchId;

    /** 应用号 */
    private String appId;

    /** 应用名称 */
    private String appName;
}
