package org.dromara.daxpay.channel.alipay.result.notice;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 资金单据状态变更通知
 * @author xxm
 * @since 2024/7/30
 */
@Data
@Accessors(chain = true)
public class AlipayOrderChangedResult {

    /**
     * 必选string(64)
     * 【描述】商户端的唯一订单号
     * 【示例值】201806300001
     */
    @Alias("out_biz_no")
    private String outBizNo;
    /**
     *可选string(20)
     * 【描述】支付完成时间
     * 【示例值】2013-01-01 08:08:08
     */
    @Alias("pay_date")
    private String payDate;
    /**
     *特殊可选string(64)
     * 【描述】特殊场景提供，当子单出现异常导致主单失败或者退款时，会提供此字段，用于透出子单具体的错误场景
     * 【示例值】MID_ACCOUNT_CARD_INFO_ERROR
     */
    @Alias("sub_order_error_code")
    private String subOrderErrorCode;
    /**
     *特殊可选string(64)
     * 【描述】失败子单具体状态
     * 【示例值】FAIL
     */
    @Alias("sub_order_status")
    private String subOrderStatus;
    /**
     * 必选string(32)
     * 【描述】描述特定的业务场景，如果传递了out_biz_no则该字段为必传。可取的业务场景如下： PERSONAL_PAY：C2C现金红包-发红包； PERSONAL_COLLECTION：C2C现金红包-领红包； REFUND：C2C现金红包-红包退回； DIRECT_TRANSFER：B2C现金红包、单笔无密转账
     * 【示例值】PERSONAL_PAY
     */
    @Alias("biz_scene")
    private String bizScene;
    /**
     * 任选string (32)
     * 【描述】支付宝支付资金流水号，仅当转账成功才会返回该参数
     * 【示例值】20190801110070001506380000251556
     */
    @Alias("pay_fund_order_id")
    private String payFundOrderId;
    /**
     * 必选string(32)
     * 【描述】资金订单的操作类型， CREATE-创建； FINISH- 订单处理已完结 CLOSE-超时关闭 ；
     * 【示例值】FINISH
     */
    @Alias("action_type")
    private String actionType;
    /**
     * 可选string(64)
     * 【描述】无忧收场景下的受理单号
     * 【示例值】202007162000000000461
     */
    @Alias("entrust_order_id")
    private String entrustOrderId;
    /**
     * product_code必选string(32)
     * 【描述】销售产品码，商家和支付宝签约的产品码。 STD_RED_PACKET：现金红包； TRANS_ACCOUNT_NO_PWD：单笔无密转账
     * 【示例值】 STD_RED_PACKET
     */
    @Alias("product_code")
    private String productCode;
    /**
     * 必选string(20)
     * 【描述】转账金额,单位元
     * 【示例值】32.00
     */
    @Alias("trans_amount")
    private String transAmount;
    /**
     * 清算机构流水号必选string(64)
     * 【描述】金融机构发起签约类、支付类、差错类业务时，应为每笔业务分配唯一的交易流水号。31位交易流水号组成规则为：“8位日期”+“16位序列号”+“1位预留位”+“6位控制位”，其中： a）“8位日期”为系统当前日期，ISODate格式：“YYYYMMDD” b）“16位序列号”由金融机构生成，金融机构应确保该值在网联当日唯一 c）“1位预留位”由平台分配 d）“6位控制位”由金融机构通过平台获取 例如：2023052993044491260542090100400
     * 【示例值】2023052993044491260542090100400
     */
    @Alias("settle_serial_no")
    private String settleSerialNo;
    /**
     * 可选string(20)
     * 【描述】自动退款时间
     * 【示例值】2013-01-02 08:08:08
     */
    @Alias("refund_date")
    private String refundDate;
    /**
     * 特殊可选string(128)
     * 【描述】特殊场景提供，当子单出现异常导致主单失败或者退款时，会提供此字段，用于透出子单具体的错误场景
     * 【示例值】收款方银行卡信息有误
     */
    @Alias("sub_order_fail_reason")
    private String subOrderFailReason;
    /**
     * origin_interface必选string(64)
     * 【描述】请求来源的接口
     * 【示例值】alipay.fund.trans.app.pay
     */
    @Alias("origin_interface")
    private String originInterface;
    /**
     * 必选string(64)
     * 【描述】支付宝转账单据号
     * 【示例值】20190624110075000006530000014566
     */
    @Alias("order_id")
    private String orderId;
    /**
     * string(16)
     * 【描述】转账单据状态。可能出现的状态如下：
     * SUCCESS：转账成功；
     * WAIT_PAY：等待支付；
     * CLOSED：订单超时关闭；
     * REFUND：退票；
     * DEALING：转账到银行卡处理中；
     * FAIL：转账失败；
     * alipay.fund.trans.app.pay涉及的状态： WAIT_PAY SUCCESS CLOSED
     * alipay.fund.trans.uni.transfer、alipay.fund.trans.refund涉及的状态：SUCCESS REFUND DEALING FAIL
     * 不同的转账接口涉及不同的单据状态，以实际场景为准
     * 【示例值】SUCCESS
     */
    @Alias("status")
    private String status;
}
