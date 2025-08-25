package org.dromara.daxpay.service.pay.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.pay.convert.constant.ChannelConstConvert;
import org.dromara.daxpay.service.pay.result.constant.ChannelConstResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付通道常量
 * @author xxm
 * @since 2024/6/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_channel_const")
public class ChannelConst extends MpIdEntity implements ToResult<ChannelConstResult> {

    /** 通道编码 */
    private String code;

    /** 通道名称 */
    private String name;

    /** 支持服务商模式 */
    private boolean isv;

    /** 是否启用 */
    private boolean enable;

    /** 是否支持分账 */
    private boolean allocatable;

    /** 终端上报 */
    private boolean terminal;

    /** 进件 */
    private boolean apply;

    /**
     * 转换
     */
    @Override
    public ChannelConstResult toResult() {
        return ChannelConstConvert.CONVERT.toResult(this);
    }
}
