package org.dromara.daxpay.payment.isv.entity.info;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import org.dromara.daxpay.payment.isv.convert.info.IsvInfoConvert;
import org.dromara.daxpay.payment.isv.result.info.IsvInfoResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 服务商信息
 * @author xxm
 * @since 2024/10/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_isv_info")
public class IsvInfo extends MpBaseEntity implements ToResult<IsvInfoResult> {

    /** 名称 */
    private String name;

    /** 简称 */
    private String shortName;

    /**
     * 应用状态
     * @see IsvStatusEnum
     */
    private String status;

    /** 服务商号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String isvNo;

    /**
     * 转换
     */
    @Override
    public IsvInfoResult toResult() {
        return IsvInfoConvert.CONVERT.toResult(this);
    }
}
