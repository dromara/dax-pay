package org.dromara.daxpay.unisdk.common.api;

import org.dromara.daxpay.unisdk.common.bean.*;
import org.dromara.daxpay.unisdk.common.http.HttpConfigStorage;
import org.dromara.daxpay.unisdk.common.http.HttpRequestTemplate;

import java.util.Date;
import java.util.Map;

/**
 * 支付服务
 *
 * @author egan
 * <pre>
 *         email egzosn@gmail.com
 *         date 2016-5-18 14:09:01
 *         </pre>
 */
public interface UniPayService<PC extends PayConfigStorage> {


    /**
     * 设置支付配置
     *
     * @param payConfigStorage 支付配置
     * @return 支付服务
     */
    UniPayService<PC> setPayConfigStorage(PC payConfigStorage);

    /**
     * 获取支付配置
     *
     * @return 支付配置
     */
    PC getPayConfigStorage();

    /**
     * 获取http请求工具
     *
     * @return http请求工具
     */
    HttpRequestTemplate getHttpRequestTemplate();

    /**
     * 设置 请求工具配置  设置并创建请求模版， 代理请求配置这里是否合理？？，
     *
     * @param configStorage http请求配置
     * @return 支付服务
     */
    UniPayService<PC> setRequestTemplateConfigStorage(HttpConfigStorage configStorage);

    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    boolean verify(NoticeParams params);


    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @param <O>   预订单类型
     * @return 订单信息
     * @see UniOrder 支付订单信息
     */
    <O extends UniOrder> Map<String, Object> orderInfo(O order);

    /**
     * 页面转跳支付， 返回对应页面重定向信息
     *
     * @param order 订单信息
     * @param <O>   预订单类型
     * @return 对应页面重定向信息
     */
    <O extends UniOrder> String toPay(O order);

    /**
     * app支付
     *
     * @param order 订单信息
     * @param <O>   预订单类型
     * @return 对应app所需参数信息
     */
    <O extends UniOrder> Map<String, Object> app(O order);

    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return 签名
     */
    String createSign(String content, String characterEncoding);


    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code    状态
     * @param message 消息
     * @return 返回输出消息
     */
    PayOutMessage getPayOutMessage(String code, String message);

    /**
     * 获取成功输出消息，用户返回给支付端
     * 主要用于拦截器中返回
     *
     * @param payMessage 支付回调消息
     * @return 返回输出消息
     */
    PayOutMessage successPayOutMessage(PayMessage payMessage);

    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return 获取输出消息，用户返回给支付端, 针对于web端
     * @see MethodType 请求类型
     */
    String buildRequest(Map<String, Object> orderInfo, MethodType method);


    /**
     * 获取输出二维码信息,
     *
     * @param order 发起支付的订单信息
     * @param <O>   预订单类型
     * @return 返回二维码信息,，支付时需要的
     */
    <O extends UniOrder> String getQrPay(O order);

    /**
     * 小程序支付，返回小程序所需的订单构建信息
     *
     * @param order 发起支付的订单信息
     * @param <O>   预订单类型
     * @return 返回支付结果
     */
    <O extends UniOrder> Map<String, Object> jsApi(O order);
    /**
     * 刷卡付,pos主动扫码付款(条码付)
     * 刷脸付
     *
     * @param order 发起支付的订单信息
     * @param <O>   预订单类型
     * @return 返回支付结果
     */
    <O extends UniOrder> Map<String, Object> microPay(O order);


    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    Map<String, Object> query(AssistOrder assistOrder);



    /**
     * 交易关闭接口
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    Map<String, Object> close(AssistOrder assistOrder);


    /**
     * 交易交易撤销
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回支付方交易撤销后的结果
     */
    Map<String, Object> cancel(String tradeNo, String outTradeNo);


    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    RefundResult refund(UniRefundOrder refundOrder);


    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    Map<String, Object> refundquery(UniRefundOrder refundOrder);

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型 内部自动转化 {@link BillType}
     * @return 返回支付方下载对账单的结果
     */
    Map<String, Object> downloadBill(Date billDate, String billType);

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型
     * @return 返回支付方下载对账单的结果
     */
    Map<String, Object> downloadBill(Date billDate, BillType billType);


    /**
     * 转账
     *
     * @param order 转账订单
     * @return 对应的转账结果
     */
    Map<String, Object> transfer(UniTransferOrder order);

    /**
     * 获取支付请求地址
     *
     * @param transactionType 交易类型
     * @return 请求地址
     */
    String getReqUrl(TransactionType transactionType);

    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    PayMessage createMessage(Map<String, Object> message);

}
