package org.dromara.daxpay.channel.union.code;

import org.dromara.daxpay.channel.union.sdk.bean.SDKConstants;

/**
 * 云闪付常量编码
 * @author xxm
 * @since 2024/9/6
 */
public interface UnionPayCode {

    /** 应答码 00表示成功 */
    String RESP_CODE = SDKConstants.param_respCode;

    /** 原交易应答码 00表示成功 */
    String RESP_ORIG_CODE = SDKConstants.param_origRespCode;

    /** 应答码信息 */
    String RESP_MSG = SDKConstants.param_respMsg;

    /** 业务结果 00表示成功 */
    String RESP_SUCCESS = SDKConstants.OK_RESP_CODE;

    /** 交易类型 */
    String TXN_TYPE = "txnType";

    /** 网关订单号 */
    String QUERY_ID = "queryId";

    /** 第三方订单号(本地订单号) */
    String ORDER_ID = "orderId";

    /** APP支付 银联订单号 */
    String PAY_APP_TN = SDKConstants.param_tn;

    /** 交易类型 支付 */
    String TXN_TYPE_PAY = "01";

    /** 交易类型 退款 */
    String TXN_TYPE_REFUND = "04";

    /**
     * 订单发送时间
     * 格式: yyyyMMddHHmmss
     */
    String TXN_TIME = "txnTime";


    /** 退款金额 */
    String TXN_AMT = "txnAmt";

    /** 总金额 */
    String TOTAL_FEE = "settleAmt";

    /** 对账单下载类型编码 */
    String RECONCILE_BILL_TYPE = "00";

    /** 文件内容 */
    String FILE_CONTENT = "fileContent";

    /** 明细对账单文件前缀 */
    String RECONCILE_FILE_PREFIX = "INN";

    /* 对账单交易代码 */
    /** 消费 */
    String RECONCILE_TYPE_PAY = "S22";

    /** 退款 */
    String RECONCILE_TYPE_REFUND = "S30 ";


    /** 对账各字段位数游标 */
    int[] RECONCILE_BILL_SPLIT = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,32,13,2,1,12,67};

    /**
     * 签名类型
     */
    interface SignType{
        String RSA2 = "RSA2";
    }
}
