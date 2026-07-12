package cn.daxpay.open.payment.core.trade.record.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付同步记录
///
/// 记录每次资金交易状态同步的结果, 含通道返回原始报文与是否触发本地状态调整
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_sync_record")
public class PaySyncRecord extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 平台交易号(对应 pay_trade.trade_no)
    private String tradeNo;

    /// 商户业务单号(对应 pay_normal_order.biz_order_no)
    private String bizTradeNo;

    /// 通道交易号(三方通道返回的订单号)
    private String outTradeNo;

    /// 通道返回的资金状态
    private String outTradeStatus;

    /// 交易类型
    /// @see cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum
    private String tradeType;

    /// 支付产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 支付通道
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum
    private String channel;

    /// 网关返回的同步原始报文(json)
    private String syncInfo;

    /// 本地与通道状态不一致时是否进行了调整
    private boolean adjust;

    /// 错误码
    private String errorCode;

    /// 错误信息
    private String errorMsg;

    /// 终端 IP
    private String clientIp;

    /// 错误信息截断(最长300), 防止超长写入
    public PaySyncRecord setErrorMsg(String errorMsg) {
        this.errorMsg = StrUtil.sub(errorMsg, 0, 300);
        return this;
    }
}
