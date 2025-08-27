package org.dromara.daxpay.channel.alipay.entity.assist;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.channel.alipay.convert.AlipayMccConstConvert;
import org.dromara.daxpay.channel.alipay.result.assist.AlipayMccConstResult;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝经营类目
 * @author xxm
 * @since 2024/11/11
 */
@Data
@Accessors(chain = true)
@TableName("pay_alipay_mcc_const")
public class AlipayMccConst implements ToResult<AlipayMccConstResult> {

    /** 类目 */
    @TableId
    private String code;

    /** 上级类目 */
    private String parentCode;

    /** 类目名称 */
    private String name;

    /** 是否需要特殊资质 */
    private Boolean special;

    @Override
    public AlipayMccConstResult toResult() {
        return AlipayMccConstConvert.INSTANCE.toResult(this);
    }
}
