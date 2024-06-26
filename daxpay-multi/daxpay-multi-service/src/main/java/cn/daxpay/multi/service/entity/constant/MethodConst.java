package cn.daxpay.multi.service.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付方式常量
 * @author xxm
 * @since 2024/6/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_method_const")
public class MethodConst extends MpIdEntity {

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 是否启用 */
    private boolean enable;

    /** 备注 */
    private String remark;
}
