package cn.daxpay.open.payment.old.pay.entity.record.close;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.payment.old.pay.convert.record.PayCloseRecordConvert;
import cn.daxpay.open.payment.old.pay.result.record.close.PayCloseRecordResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付关闭记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_close_record")
public class PayCloseRecord extends MchBaseEntity implements ToResult<PayCloseRecordResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 订单号
    private String orderNo;

    /// 商户订单号
    private String bizOrderNo;

    /// 支付产品
    /// @see ProductEnum
    private String product;

    /// 关闭的支付通道
    /// @see ChannelEnum
    private String channel;

    /// 是否关闭成功
    private boolean closed;

    /// 关闭类型
    /// @see CloseTypeEnum
    private String closeType;

    /// 错误码
    private String errorCode;

    /// 错误消息
    private String errorMsg;

    /// 客户端IP
    private String clientIp;

    public PayCloseRecord setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg,0,300);
        return this;
    }

    /// 转换
    @Override
    public PayCloseRecordResult toResult() {
        return PayCloseRecordConvert.CONVERT.convert(this);
    }
}

