package cn.daxpay.multi.service.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.service.convert.constant.MethodConstConvert;
import cn.daxpay.multi.service.result.constant.MethodConstResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付方式常量
 * @see cn.daxpay.multi.core.enums.PayMethodEnum
 * @author xxm
 * @since 2024/6/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_method_const")
public class MethodConst extends MpIdEntity implements ToResult<MethodConstResult> {

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 是否启用 */
    private boolean enable;

    /** 备注 */
    private String remark;

    /**
     * 转换
     */
    @Override
    public MethodConstResult toResult() {
        return MethodConstConvert.CONVERT.toResult(this);
    }
}
