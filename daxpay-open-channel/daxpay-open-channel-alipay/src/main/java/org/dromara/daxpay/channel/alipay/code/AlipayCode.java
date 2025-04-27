package org.dromara.daxpay.channel.alipay.code;

/**
 * 支付宝支付参数
 *
 * @author xxm
 * @since 2021/2/27
 */
public interface AlipayCode {

    /**
     * 支付网管地址
     */
    interface ServerUrl{
        /** 沙箱地址 */
        String SANDBOX = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
        /** 生产地址 */
        String PRODUCTION = "https://openapi.alipay.com/gateway.do";
    }

    /**
     * 退款状态
     * SUCCESS：转账成功；
     * WAIT_PAY：等待支付；
     * CLOSED：订单超时关闭；
     * FAIL：失败（适用于"单笔转账到银行卡"）；
     * DEALING：处理中（适用于"单笔转账到银行卡"）；
     * REFUND：退票（适用于"单笔转账到银行卡"）；
     */
    interface TransferStatus{
        String SUCCESS = "SUCCESS";
        String WAIT_PAY = "WAIT_PAY";
        String CLOSED = "CLOSED";
        String FAIL = "FAIL";
        String DEALING = "DEALING";
        String REFUND = "REFUND";
    }

    /**
     * 认证类型
     */
    interface AuthType{
        /** 公钥 */
        String AUTH_TYPE_KEY = "key";

        /** 证书 */
        String AUTH_TYPE_CART = "cart";
    }


    /**
     * 渠道和产品枚举
     */
    interface Products {
        /** 目前PC支付必填 */
        String FAST_INSTANT_TRADE_PAY = "FAST_INSTANT_TRADE_PAY";

        /** WAP支付必填 手机网站支付产品 */
        String QUICK_WAP_PAY = "QUICK_WAP_WAY";

        /** APP支付必填 APP支付产品 */
        String QUICK_MSECURITY_PAY = "QUICK_MSECURITY_PAY";

        /** 询余额账户类型 */
        String QUERY_ACCOUNT_TYPE = "ACCTRANS_ACCOUNT";

        /** jsapi */
        String JSAPI_PAY ="JSAPI_PAY";

        /** 付款码支付 */
        String BAR_CODE = "bar_code";
    }


    /**
     * 响应字段
     */
    interface ResponseParams{
        /** 交易状态 */
        String TRADE_STATUS = "trade_status";

        /** 通道支付订单号 - 商户订单号 */
        String OUT_TRADE_NO = "out_trade_no";

        /** 支付流水号 */
        String TRADE_NO = "trade_no";

        /** 支付金额 */
        String TOTAL_AMOUNT = "total_amount";

        /** 交易付款时间 yyyy-MM-dd HH:mm:ss */
        String GMT_PAYMENT = "gmt_payment";

        /** 退款业务号 */
        String OUT_BIZ_NO = "out_biz_no";

        /** 退款金额 */
        String REFUND_FEE = "refund_fee";

        /** 退款时间 yyyy-MM-dd HH:mm:ss */
        String GMT_REFUND = "GMT_REFUND";

        /** 返回退款时间 */
        String GMT_REFUND_PAY = "gmt_refund_pay";
    }

    /**
     *  支付交易状态说明
     *  WAIT_BUYER_PAY（交易创建，等待买家付款）、
     *  TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
     *  TRADE_SUCCESS（交易支付成功）、
     *  TRADE_FINISHED（交易结束，不可退款）
     */
    interface PayStatus {
        /** 交易创建，等待买家付款 */
        String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

        /** 未付款交易超时关闭，或支付完成后全额退款 */
        String TRADE_CLOSED = "TRADE_CLOSED";

        /** 交易支付成功 */
        String TRADE_SUCCESS = "TRADE_SUCCESS";

        /** 交易结束，不可退款 */
        String TRADE_FINISHED = "TRADE_FINISHED";
    }

    /**
     * 退款状态
     */
    interface RefundStatus {
        /** 退款成功 */
        String REFUND_SUCCESS = "REFUND_SUCCESS";
    }

    /**
     * 响应状态码
     */
    interface ResponseCode {
        /** 转账成功 */
        String TRANSFER_SUCCESS = "SUCCESS";

        // 错误提示
        /** 交易不存在 */
        String ACQ_TRADE_NOT_EXIST = "ACQ.TRADE_NOT_EXIST";
        /** 交易不存在 */
        String ACQ_TRADE_STATUS_ERROR = "ACQ.TRADE_STATUS_ERROR";

        // 网关返回码
        String SUCCESS = "10000";

        // 网关返回码 支付进行中 order success pay inprocess
        String INPROCESS = "10003";
    }

    /* 分账相关 */
    /** 分账接收方不存在 */
    String USER_NOT_EXIST = "USER_NOT_EXIST";

    /** 分账金额超过最大可分账金额 */
    String ALLOC_AMOUNT_VALIDATE_ERROR = "ACQ.ALLOC_AMOUNT_VALIDATE_ERROR";

    /** 分账 进行中 */
    String ALLOC_PROCESSING = "PROCESSING";
    /** 分账 成功 */
    String ALLOC_SUCCESS = "SUCCESS";
    /** 分账 失败 */
    String ALLOC_FAIL = "FAIL";

    /** 异步分账 */
    String ALLOC_ASYNC = "async";
    /** 同步分账 */
    String ALLOC_SYNC = "sync";
}
