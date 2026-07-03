package cn.daxpay.open.payment.core.trade.record.entity;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
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
/// 记录每次支付关闭/撤销操作的结果, 通过 closeType 区分关闭(close)与撤销(cancel)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_close_record")
public class PayCloseRecord extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 平台交易号(对应 pay_trade.trade_no)
    private String tradeNo;

    /// 商户业务单号(对应 pay_normal_order.biz_order_no)
    private String bizTradeNo;

    /// 支付产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 支付通道
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum
    private String channel;

    /// 是否关闭成功
    private boolean closed;

    /// 关闭类型(close/cancel)
    /// @see cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum
    private String closeType;

    /// 错误码
    private String errorCode;

    /// 错误信息
    private String errorMsg;

    /// 终端 IP
    private String clientIp;

    /// 错误信息截断(最长300), 防止超长写入
    public PayCloseRecord setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg, 0, 300);
        return this;
    }
}
