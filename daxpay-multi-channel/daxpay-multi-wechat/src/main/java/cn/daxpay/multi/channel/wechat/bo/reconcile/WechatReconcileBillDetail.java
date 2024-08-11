package cn.daxpay.multi.channel.wechat.bo.reconcile;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 微信交易对账解析文件
 * @author xxm
 * @since 2024/8/11
 */
@Data
@Accessors(chain = true)
public class WechatReconcileBillDetail {

    // 用户标识
    @Excel(name="用户标识")
    private String openid;

    // 交易类型 JSAPI/NATIVE
    @Excel(name="交易类型")
    private String tradeType;

    // 交易状态
    @Excel(name="交易状态")
    private String tradeState;

    // 付款银行
    @Excel(name="付款银行")
    private String bankType;

    // 货币种类
    @Excel(name="货币种类")
    private String feeType;

    // 应结订单金额
    @Excel(name="应结订单金额")
    private String settlementTotalFee;

    // 代金券金额
    @Excel(name="代金券金额")
    private String couponFee;

    // 微信退款单号
    @Excel(name="微信退款单号")
    private String refundId;

    // 商户退款单号
    @Excel(name="商户退款单号")
    private String outRefundNo;

    // 退款金额
    @Excel(name="退款金额")
    private String refundFee;

    // 充值券退款金额
    @Excel(name="充值券退款金额")
    private String couponRefundFee;

    // 退款类型
    @Excel(name="退款类型")
    private String refundType;

    // 退款状态
    @Excel(name="退款状态")
    private String refundStatus;

    // 商品名称
    @Excel(name="商品名称")
    private String body;

    // 商户数据包
    @Excel(name="商户数据包")
    private String attach;

    // 手续费
    @Excel(name="手续费")
    private String fee;

    // 费率
    @Excel(name="费率")
    private String feeRate;

    // 订单金额
    @Excel(name="订单金额")
    private String totalFee;

    // 申请退款金额
    @Excel(name="申请退款金额")
    private String refundFeeType;

    // 费率备注
    @Excel(name="费率备注")
    private String feeRemark;
}
