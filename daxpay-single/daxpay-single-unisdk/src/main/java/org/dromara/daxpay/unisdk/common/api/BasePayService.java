package org.dromara.daxpay.unisdk.common.api;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.unisdk.common.bean.*;
import org.dromara.daxpay.unisdk.common.http.HttpConfigStorage;
import org.dromara.daxpay.unisdk.common.http.HttpRequestTemplate;
import org.dromara.daxpay.unisdk.common.util.sign.SignUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * 支付基础服务
 *
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2017/3/5 20:36
 *   </pre>
 */
@Slf4j
public abstract class BasePayService<PC extends PayConfigStorage> implements UniPayService<PC> {
    protected PC payConfigStorage;

    protected HttpRequestTemplate requestTemplate;


    /**
     * 设置支付配置
     *
     * @param payConfigStorage 支付配置
     */
    @Override
    public BasePayService setPayConfigStorage(PC payConfigStorage) {
        this.payConfigStorage = payConfigStorage;
        return this;
    }

    @Override
    public PC getPayConfigStorage() {
        return payConfigStorage;
    }

    @Override
    public HttpRequestTemplate getHttpRequestTemplate() {
        return requestTemplate;
    }

    /**
     * 设置并创建请求模版， 代理请求配置这里是否合理？？，
     *
     * @param configStorage http请求配置
     * @return 支付服务
     */
    @Override
    public BasePayService setRequestTemplateConfigStorage(HttpConfigStorage configStorage) {
        this.requestTemplate = new HttpRequestTemplate(configStorage);
        return this;
    }


    public BasePayService(PC payConfigStorage) {
        this(payConfigStorage, null);
    }

    public BasePayService(PC payConfigStorage, HttpConfigStorage configStorage) {
        setPayConfigStorage(payConfigStorage);
        setRequestTemplateConfigStorage(configStorage);

    }


    /**
     * Generate a Base64 encoded String from  user , password
     *
     * @param user     用户名
     * @param password 密码
     * @return authorizationString
     */
    protected String authorizationString(String user, String password) {
        String base64ClientID = null;
        base64ClientID = Base64.encode(String.format("%s:%s", user, password).getBytes(StandardCharsets.UTF_8));

        return base64ClientID;
    }


    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    @Override
    public String createSign(String content, String characterEncoding) {

        return SignUtils.valueOf(payConfigStorage.getSignType()).createSign(content, payConfigStorage.getKeyPrivate(), characterEncoding);
    }

    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    public String createSign(Map<String, Object> content, String characterEncoding) {
        return SignUtils.valueOf(payConfigStorage.getSignType()).sign(content, payConfigStorage.getKeyPrivate(), characterEncoding);
    }

    /**
     * 页面转跳支付， 返回对应页面重定向信息
     *
     * @param order 订单信息
     * @return 对应页面重定向信息
     */
    @Override
    public <O extends UniOrder> String toPay(O order) {
        if (StrUtil.isNotEmpty(order.getSubject()) && order.getSubject().contains("'")) {
            order.setSubject(order.getSubject().replace("'", ""));
        }
        if (StrUtil.isNotEmpty(order.getBody()) && order.getBody().contains("'")) {
            order.setBody(order.getBody().replace("'", ""));
        }
        Map<String, Object> orderInfo = orderInfo(order);
        return buildRequest(orderInfo, MethodType.POST);
    }

    /**
     * app支付
     *
     * @param order 订单信息
     * @param <O>   预订单类型
     * @return 对应app所需参数信息
     */
    @Override
    public <O extends UniOrder> Map<String, Object> app(O order) {
        return orderInfo(order);
    }


    /**
     * 小程序支付，返回小程序所需的订单构建信息
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */
    @Override
    public <O extends UniOrder> Map<String, Object> jsApi(O order) {
        return Collections.emptyMap();
    }


    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易撤销后的结果
     */
    @Override
    public Map<String, Object> cancel(String tradeNo, String outTradeNo) {
        return Collections.emptyMap();
    }


    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型 内部自动转化 {@link BillType}
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, String billType) {
        return Collections.emptyMap();
    }

    /**
     * 转账
     *
     * @param order 转账订单
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(UniTransferOrder order) {
        return Collections.emptyMap();
    }

    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return new PayMessage(message);
    }

}
