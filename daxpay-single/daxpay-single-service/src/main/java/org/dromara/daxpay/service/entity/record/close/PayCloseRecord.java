package org.dromara.daxpay.service.entity.record.close;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.CloseTypeEnum;
import org.dromara.daxpay.service.common.entity.MchAppRecordEntity;
import org.dromara.daxpay.service.convert.record.PayCloseRecordConvert;
import org.dromara.daxpay.service.result.record.close.PayCloseRecordResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/6/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_close_record")
public class PayCloseRecord extends MchAppRecordEntity implements ToResult<PayCloseRecordResult> {

    /** 订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /**
     * 关闭的支付通道
     * @see ChannelEnum
     */
    private String channel;

    /** 是否关闭成功 */
    private boolean closed;

    /**
     * 关闭类型
     * @see CloseTypeEnum
     */
    private String closeType;

    /** 错误码 */
    private String errorCode;

    /** 错误消息 */
    private String errorMsg;

    /** 客户端IP */
    private String clientIp;

    /**
     * 转换
     */
    @Override
    public PayCloseRecordResult toResult() {
        return PayCloseRecordConvert.CONVERT.convert(this);
    }
}
