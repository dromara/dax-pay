package org.dromara.daxpay.channel.union.sdk.api;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.union.sdk.bean.*;
import org.dromara.daxpay.unisdk.common.api.BasePayService;
import org.dromara.daxpay.unisdk.common.bean.*;
import org.dromara.daxpay.unisdk.common.bean.outbuilder.PayTextOutMessage;
import org.dromara.daxpay.unisdk.common.bean.result.PayException;
import org.dromara.daxpay.unisdk.common.exception.PayErrorException;
import org.dromara.daxpay.unisdk.common.http.HttpConfigStorage;
import org.dromara.daxpay.unisdk.common.http.UriVariables;
import org.dromara.daxpay.unisdk.common.util.Util;
import org.dromara.daxpay.unisdk.common.util.sign.CertDescriptor;
import org.dromara.daxpay.unisdk.common.util.sign.SignTextUtils;
import org.dromara.daxpay.unisdk.common.util.sign.SignUtils;
import org.dromara.daxpay.unisdk.common.util.sign.encrypt.RSA;
import org.dromara.daxpay.unisdk.common.util.sign.encrypt.RSA2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.*;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

/**
 * 云闪付支付服务类重命名, 避免与系统中类名冲突
 * @author xxm
 * @since 2024/3/8
 */
@Slf4j
public class UnionPayKit extends BasePayService<UnionPayConfigStorage> {
    /**
     * 测试域名
     */
    private static final String TEST_BASE_DOMAIN = "test.95516.com";
    /**
     * 正式域名
     */
    private static final String RELEASE_BASE_DOMAIN = "95516.com";
    /**
     * 交易请求地址
     */
    private static final String FRONT_TRANS_URL = "https://gateway.%s/gateway/api/frontTransReq.do";
    private static final String BACK_TRANS_URL = "https://gateway.%s/gateway/api/backTransReq.do";
    private static final String SINGLE_QUERY_URL = "https://gateway.%s/gateway/api/queryTrans.do";
    private static final String BATCH_TRANS_URL = "https://gateway.%s/gateway/api/batchTrans.do";
    private static final String FILE_TRANS_URL = "https://filedownload.%s/";
    private static final String APP_TRANS_URL = "https://gateway.%s/gateway/api/appTransReq.do";
    private static final String CARD_TRANS_URL = "https://gateway.%s/gateway/api/cardTransReq.do";
    /**
     * 证书解释器
     */
    private volatile CertDescriptor certDescriptor;

    /**
     * 构造函数
     *
     * @param payConfigStorage 支付配置
     */
    public UnionPayKit(UnionPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public UnionPayKit(UnionPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    /**
     * 设置支付配置
     *
     * @param payConfigStorage 支付配置
     */
    @Override
    public UnionPayKit setPayConfigStorage(UnionPayConfigStorage payConfigStorage) {
        this.payConfigStorage = payConfigStorage;
        if (null != certDescriptor) {
            return this;
        }
        //
        if (!payConfigStorage.isCertSign()){
            return this;
        }

        try {
            certDescriptor = new CertDescriptor();
            certDescriptor.initPrivateSignCert(payConfigStorage.getKeyPrivateCertInputStream(), payConfigStorage.getKeyPrivateCertPwd(), "PKCS12");
            certDescriptor.initPublicCert(payConfigStorage.getAcpMiddleCertInputStream());
            certDescriptor.initRootCert(payConfigStorage.getAcpRootCertInputStream());
        }
        catch (IOException e) {
            log.error("", e);
        }


        return this;
    }

    /**
     * 获取支付请求地址
     *
     * @param transactionType 交易类型
     * @return 请求地址
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {
        return (payConfigStorage.isTest() ? TEST_BASE_DOMAIN : RELEASE_BASE_DOMAIN);
    }

    /**
     * 根据是否为沙箱环境进行获取请求地址
     *
     * @return 请求地址
     */
    public String getReqUrl() {
        return getReqUrl(null);
    }

    public String getFrontTransUrl() {
        return String.format(FRONT_TRANS_URL, getReqUrl());
    }

    public String getBackTransUrl() {
        return String.format(BACK_TRANS_URL, getReqUrl());
    }

    public String getAppTransUrl() {
        return String.format(APP_TRANS_URL, getReqUrl());
    }

    public String getSingleQueryUrl() {
        return String.format(SINGLE_QUERY_URL, getReqUrl());
    }


    public String getFileTransUrl() {
        return String.format(FILE_TRANS_URL, getReqUrl());
    }

    /**
     * 初始化异步通知地址
     *
     * @param parameters 预订单信息
     * @param order      订单
     */
    private void initNotifyUrl(Map<String, Object> parameters, AssistOrder order) {
        OrderParaStructure.loadParameters(parameters, SDKConstants.param_backUrl, payConfigStorage.getNotifyUrl());
        OrderParaStructure.loadParameters(parameters, SDKConstants.param_backUrl, order.getNotifyUrl());
        OrderParaStructure.loadParameters(parameters, SDKConstants.param_backUrl, order);
    }


    /**
     * 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改
     *
     * @return 返回参数集合
     */
    private Map<String, Object> getCommonParam() {
        Map<String, Object> params = new TreeMap<>();
        UnionPayConfigStorage configStorage = payConfigStorage;
        //银联接口版本
        params.put(SDKConstants.param_version, configStorage.getVersion());
        //编码方式
        params.put(SDKConstants.param_encoding, payConfigStorage.getInputCharset().toUpperCase());
        //商户代码
        params.put(SDKConstants.param_merId, payConfigStorage.getPid());

        //订单发送时间
        params.put(SDKConstants.param_txnTime, DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));

        //交易币种
        params.put(SDKConstants.param_currencyCode, "156");
        //接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
        params.put(SDKConstants.param_accessType, configStorage.getAccessType());
        return params;
    }


    /**
     * 回调校验
     *
     * @param noticeParams 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify(NoticeParams noticeParams) {
        final Map<String, ?> result = noticeParams.getBody();
        if (null == result || result.get(SDKConstants.param_signature) == null) {
            log.debug("银联支付验签异常：params：{}", result);
            return false;
        }
        return this.signVerify(result, (String) result.get(SDKConstants.param_signature));
    }

    /**
     * 签名校验
     *
     * @param params 参数集
     * @param sign   签名原文
     * @return 签名校验 true通过
     */
    public boolean signVerify(Map<String, ?> params, String sign) {
        SignUtils signUtils = SignUtils.valueOf(payConfigStorage.getSignType());

        String data = SignTextUtils.parameterText(params, "&", "signature");
        return switch (signUtils) {
            case RSA -> {
                data = SignUtils.SHA1.createSign(data, "", payConfigStorage.getInputCharset());
                yield RSA.verify(data, sign, verifyCertificate(genCertificateByStr((String) params.get(SDKConstants.param_signPubKeyCert))).getPublicKey(), payConfigStorage.getInputCharset());
            }
            case RSA2 -> {
                data = SignUtils.SHA256.createSign(data, "", payConfigStorage.getInputCharset());
                yield RSA2.verify(data, sign, verifyCertificate(genCertificateByStr((String) params.get(SDKConstants.param_signPubKeyCert))).getPublicKey(), payConfigStorage.getInputCharset());
            }
            case SHA1, SHA256, SM3 -> {
                String before = signUtils.createSign(payConfigStorage.getKeyPublic(), "", payConfigStorage.getInputCharset());
                yield signUtils.verify(data, sign, "&" + before, payConfigStorage.getInputCharset());
            }
            default -> false;
        };
    }


    /**
     * 订单超时时间。
     * 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
     * 此时间建议取支付时的北京时间加15分钟。
     * 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
     *
     * @param expirationTime 超时时间
     * @return 具体的时间字符串
     */
    private String getPayTimeout(Date expirationTime) {
        Date date = Objects.requireNonNullElseGet(expirationTime, () -> new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000));
        return DateUtil.format(date,DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return 订单信息
     * @see UniOrder 支付订单信息
     */
    @Override
    public Map<String, Object> orderInfo(UniOrder order) {
        Map<String, Object> params = this.getCommonParam();

        UnionTransactionType type = (UnionTransactionType) order.getTransactionType();
        initNotifyUrl(params, order);

        //设置交易类型相关的参数
        type.convertMap(params);

        params.put(SDKConstants.param_orderId, order.getOutTradeNo());

        if (StrUtil.isNotEmpty(order.getAddition())) {
            params.put(SDKConstants.param_reqReserved, order.getAddition());
        }
        switch (type) {
            case WAP:
            case WEB:
            case B2B:
                params.put(SDKConstants.param_txnAmt, Util.conversionCentAmount(order.getPrice()));
                params.put("orderDesc", order.getSubject());
                params.put(SDKConstants.param_payTimeout, getPayTimeout(order.getExpirationTime()));
                params.put(SDKConstants.param_frontUrl, payConfigStorage.getReturnUrl());
                break;
            case CONSUME:
                params.put(SDKConstants.param_txnAmt, Util.conversionCentAmount(order.getPrice()));
                params.put(SDKConstants.param_qrNo, order.getAuthCode());
                break;
            case APPLY_QR_CODE:
                if (null != order.getPrice()) {
                    params.put(SDKConstants.param_txnAmt, Util.conversionCentAmount(order.getPrice()));
                }
                params.put(SDKConstants.param_payTimeout, getPayTimeout(order.getExpirationTime()));
                break;
            default:
                params.put(SDKConstants.param_txnAmt, Util.conversionCentAmount(order.getPrice()));
                params.put(SDKConstants.param_payTimeout, getPayTimeout(order.getExpirationTime()));
                params.put("orderDesc", order.getSubject());
        }
        params.putAll(order.getAttrs());
        return setSign(params);
    }


    /**
     * 生成并设置签名
     *
     * @param parameters 请求参数
     * @return 请求参数
     */
    private Map<String, Object> setSign(Map<String, Object> parameters) {

        SignUtils signUtils = SignUtils.valueOf(payConfigStorage.getSignType());

        String signStr;
        switch (signUtils) {
            case RSA:
                parameters.put(SDKConstants.param_signMethod, SDKConstants.SIGNMETHOD_RSA);
                parameters.put(SDKConstants.param_certId, certDescriptor.getSignCertId());
                signStr = SignUtils.SHA1.createSign(SignTextUtils.parameterText(parameters, "&", "signature"), "", payConfigStorage.getInputCharset());
                parameters.put(SDKConstants.param_signature, RSA.sign(signStr, certDescriptor.getSignCertPrivateKey(payConfigStorage.getKeyPrivateCertPwd()), payConfigStorage.getInputCharset()));
                break;
            case RSA2:
                parameters.put(SDKConstants.param_signMethod, SDKConstants.SIGNMETHOD_RSA);
                parameters.put(SDKConstants.param_certId, certDescriptor.getSignCertId());
                signStr = SignUtils.SHA256.createSign(SignTextUtils.parameterText(parameters, "&", "signature"), "", payConfigStorage.getInputCharset());
                parameters.put(SDKConstants.param_signature, RSA2.sign(signStr, certDescriptor.getSignCertPrivateKey(payConfigStorage.getKeyPrivateCertPwd()), payConfigStorage.getInputCharset()));
                break;
            case SHA1:
            case SHA256:
            case SM3:
                String key = payConfigStorage.getKeyPrivate();
                signStr = SignTextUtils.parameterText(parameters, "&", "signature");
                key = signUtils.createSign(key, "", payConfigStorage.getInputCharset()) + "&";
                parameters.put(SDKConstants.param_signature, signUtils.createSign(signStr, key, payConfigStorage.getInputCharset()));
                break;
            default:
                throw new PayErrorException(new PayException("sign fail", "未找到的签名类型"));
        }


        return parameters;
    }


    /**
     * 验证证书链
     *
     * @param cert 需要验证的证书
     */
    private X509Certificate verifyCertificate(X509Certificate cert) {
        try {
            cert.checkValidity();//验证有效期
            X509Certificate middleCert = certDescriptor.getPublicCert();
            X509Certificate rootCert = certDescriptor.getRootCert();

            X509CertSelector selector = new X509CertSelector();
            selector.setCertificate(cert);

            Set<TrustAnchor> trustAnchors = new HashSet<>();
            trustAnchors.add(new TrustAnchor(rootCert, null));
            PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, selector);

            Set<X509Certificate> intermediateCerts = new HashSet<>();
            intermediateCerts.add(rootCert);
            intermediateCerts.add(middleCert);
            intermediateCerts.add(cert);

            pkixParams.setRevocationEnabled(false);

            CertStore intermediateCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(intermediateCerts));
            pkixParams.addCertStore(intermediateCertStore);

            CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");

            /*PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult)*/
            builder.build(pkixParams);
            return cert;
        }
        catch (CertPathBuilderException e) {
            log.error("verify certificate chain fail.", e);
        }
        catch (GeneralSecurityException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 发送订单
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */

    public JSONObject postOrder(UniOrder order, String url) {
        Map<String, Object> params = orderInfo(order);
        String responseStr = getHttpRequestTemplate().postForObject(url, params, String.class);
        JSONObject response = UriVariables.getParametersToMap(responseStr);
        if (response.isEmpty()) {
            throw new PayErrorException(new PayException("failure", "响应内容有误!", responseStr));
        }
        return response;
    }

    @Override
    public String toPay(UniOrder order) {

        if (null == order.getTransactionType()) {
            order.setTransactionType(UnionTransactionType.WEB);
        }
        else if (UnionTransactionType.WEB != order.getTransactionType() && UnionTransactionType.WAP != order.getTransactionType() && UnionTransactionType.B2B != order.getTransactionType()) {
            throw new PayErrorException(new PayException("-1", "错误的交易类型:" + order.getTransactionType()));
        }

        return super.toPay(order);
    }

    /**
     * 获取输出二维码，用户返回给支付端,
     *
     * @param order 发起支付的订单信息
     * @return 返回图片信息，支付时需要的
     */
    @Override
    public String getQrPay(UniOrder order) {
        order.setTransactionType(UnionTransactionType.APPLY_QR_CODE);
        JSONObject response = postOrder(order, getBackTransUrl());
        if (this.verify(new NoticeParams(response))) {
            if (SDKConstants.OK_RESP_CODE.equals(response.get(SDKConstants.param_respCode))) {
                //成功
                return (String) response.get(SDKConstants.param_qrCode);
            }
            throw new PayErrorException(new PayException((String) response.get(SDKConstants.param_respCode), (String) response.get(SDKConstants.param_respMsg), response.toJSONString()));
        }
        throw new PayErrorException(new PayException("failure", "验证签名失败", response.toJSONString()));
    }

    /**
     * 刷卡付,pos主动扫码付款(条码付)
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */
    @Override
    public Map<String, Object> microPay(UniOrder order) {
        order.setTransactionType(UnionTransactionType.CONSUME);
        return postOrder(order, getBackTransUrl());
    }


    /**
     * 将字符串转换为X509Certificate对象.
     *
     * @param x509CertString 证书串
     * @return X509Certificate
     */
    public static X509Certificate genCertificateByStr(String x509CertString) {
        X509Certificate x509Cert;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream tIn = new ByteArrayInputStream(x509CertString.getBytes(StandardCharsets.ISO_8859_1));
            x509Cert = (X509Certificate) cf.generateCertificate(tIn);
        }
        catch (Exception e) {
            throw new PayErrorException(new PayException("证书加载失败", "gen certificate error:" + e.getLocalizedMessage()));
        }
        return x509Cert;
    }

    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code    状态
     * @param message 消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayTextOutMessage.TEXT().content(code.toLowerCase()).build();
    }

    /**
     * 获取成功输出消息，用户返回给支付端
     * 主要用于拦截器中返回
     *
     * @param payMessage 支付回调消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        return getPayOutMessage("ok", null);
    }

    /**
     * 功能：生成自动跳转的Html表单
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return 生成自动跳转的Html表单返回给支付端, 针对于PC端
     * @see MethodType 请求类型
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        StringBuilder sf = new StringBuilder();
        sf.append("""
                        <html><head><meta http-equiv="Content-Type" content="text/html; charset=""")
                .append(payConfigStorage.getInputCharset())
                .append("""
                        "/></head><body>
                        <form id = "pay_form" action=\"""")
                .append(getFrontTransUrl())
                .append("""
                        " method="post">""");
        if (null != orderInfo && !orderInfo.isEmpty()) {
            for (Map.Entry<String, Object> entry : orderInfo.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                sf.append(MessageFormat.format("""
                        <input type="hidden" name="{0}" id="{1}" value="{2}"/>""", key, key, value));
            }
        }
        sf.append("</form>");
        sf.append("</body>");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.all.pay_form.submit();");
        sf.append("</script>");
        sf.append("</html>");
        return sf.toString();
    }

    /**
     * 功能：将订单信息进行签名并提交请求
     * 业务范围：手机支付控件（含安卓Pay）
     *
     * @param order 订单信息
     * @return 成功：返回支付结果  失败：返回
     */
    @Override
    public Map<String, Object> app(UniOrder order) {
        if (null == order.getTransactionType()) {
            order.setTransactionType(UnionTransactionType.APP);
        }
        JSONObject response = postOrder(order, getAppTransUrl());
        if (this.verify(new NoticeParams(response))) {
            if (SDKConstants.OK_RESP_CODE.equals(response.get(SDKConstants.param_respCode))) {
//                //成功,获取tn号
//                String tn =  (String)response.get(SDKConstants.param_tn);
                return response;
            }
            throw new PayErrorException(new PayException((String) response.get(SDKConstants.param_respCode), (String) response.get(SDKConstants.param_respMsg), response.toJSONString()));
        }
        throw new PayErrorException(new PayException("failure", "验证签名失败", response.toJSONString()));
    }

    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(AssistOrder assistOrder) {
        Map<String, Object> params = this.getCommonParam();
        UnionTransactionType.QUERY.convertMap(params);
        params.put(SDKConstants.param_orderId, assistOrder.getOutTradeNo());
        this.setSign(params);
        String responseStr = getHttpRequestTemplate().postForObject(this.getSingleQueryUrl(), params, String.class);
        return UriVariables.getParametersToMap(responseStr);
    }

    /**
     * 交易关闭接口
     * TODO 这个是冲正接口, 后续进行迁移
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(AssistOrder assistOrder) {
        Map<String, Object> params = this.getCommonParam();
        //交易类型
        params.put(SDKConstants.param_txnType, "99");
        //交易子类
        params.put(SDKConstants.param_txnSubType, "01");
        //业务类型
        params.put(SDKConstants.param_bizType,"000000");
        //渠道类型
        params.put(SDKConstants.param_channelType,"08");
        // 订单号
        params.put(SDKConstants.param_orderId, assistOrder.getOutTradeNo());
        this.setSign(params);
        String responseStr = getHttpRequestTemplate().postForObject(this.getBackTransUrl(), params, String.class);
        return UriVariables.getParametersToMap(responseStr);
    }

    /**
     * 消费撤销/退货接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public UnionRefundResult refund(UniRefundOrder refundOrder) {
        Map<String, Object> params = this.getCommonParam();
        initNotifyUrl(params, refundOrder);
        UnionTransactionType.REFUND.convertMap(params);
        params.put(SDKConstants.param_orderId, refundOrder.getRefundNo());
        params.put(SDKConstants.param_txnAmt, Util.conversionCentAmount(refundOrder.getRefundAmount()));
        params.put(SDKConstants.param_origQryId, refundOrder.getTradeNo());
        params.putAll(refundOrder.getAttrs());
        this.setSign(params);
        String responseStr = getHttpRequestTemplate().postForObject(this.getBackTransUrl(), params, String.class);
        JSONObject response = UriVariables.getParametersToMap(responseStr);

        if (this.verify(new NoticeParams(response))) {
            final UnionRefundResult refundResult = UnionRefundResult.create(response);
            if (SDKConstants.OK_RESP_CODE.equals(refundResult.getRespCode())) {
                return refundResult;

            }
            throw new PayErrorException(new PayException(response.getString(SDKConstants.param_respCode), response.getString(SDKConstants.param_respMsg), response.toJSONString()));
        }
        throw new PayErrorException(new PayException("failure", "验证签名失败", response.toJSONString()));
    }


    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(UniRefundOrder refundOrder) {
        return this.query(refundOrder);
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间
     * @param fileType 文件类型 文件类型，一般商户填写00即可
     * @return 返回fileContent 请自行将数据落地
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, String fileType) {
        return downloadBill(billDate, new UnionPayBillType(fileType));
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间
     * @param billType 账单类型
     * @return 返回fileContent 请自行将数据落地
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, BillType billType) {

        Map<String, Object> params = this.getCommonParam();
        UnionTransactionType.FILE_TRANSFER.convertMap(params);

        params.put(SDKConstants.param_settleDate, DateUtil.format(billDate, "MMdd"));
        params.put(SDKConstants.param_fileType, billType.getFileType());
        params.remove(SDKConstants.param_backUrl);
        params.remove(SDKConstants.param_currencyCode);
        this.setSign(params);
        String responseStr = getHttpRequestTemplate().postForObject(this.getFileTransUrl(), params, String.class);
        JSONObject response = UriVariables.getParametersToMap(responseStr);
//        if (this.verify(response)) {
//            if (SDKConstants.OK_RESP_CODE.equals(response.get(SDKConstants.param_respCode))) {
        return response;
//            }
//            throw new PayErrorException(new PayException(response.get(SDKConstants.param_respCode).toString(), response.get(SDKConstants.param_respMsg).toString(), response.toString()));
//        }
//        throw new PayErrorException(new PayException("failure", "验证签名失败", response.toString()));
    }


    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return UnionPayMessage.create(message);
    }
}
