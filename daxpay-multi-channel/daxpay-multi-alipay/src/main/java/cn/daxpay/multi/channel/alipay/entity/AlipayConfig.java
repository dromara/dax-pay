package cn.daxpay.multi.channel.alipay.entity;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.channel.alipay.code.AliPayCode;
import cn.daxpay.multi.channel.alipay.convert.AlipayConfigConvert;
import cn.daxpay.multi.channel.alipay.result.AlipayConfigResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/6/25
 */
@Data
@Accessors(chain = true)
public class AlipayConfig implements ToResult<AlipayConfigResult> {

    /** 支付宝商户appId */
    @JsonIgnore
    private String appId;

    /** 是否启用, 只影响支付和退款操作 */
    @JsonIgnore
    private Boolean enable;

    /** 是否支付分账 */
    private Boolean allocation;

    /** 支付限额 */
    private Integer limitAmount;

    /**
     * 服务器异步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 调用顺序 支付宝网关 -> 本网关进行处理 -> 发送消息通知业务系统
     */
    private String notifyUrl;

    /**
     * 服务器同步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 支付宝网关 -> 本网关进行处理 -> 重定向到业务系统中
     */
    private String returnUrl;

    /** 支付网关地址 */
    private String serverUrl;

    /** 授权回调地址 */
    private String redirectUrl;

    /**
     * 认证类型 证书/公钥
     * @see AliPayCode#AUTH_TYPE_KEY
     */
    private String authType;

    /** 签名类型 RSA2 */
    public String signType;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    private String alipayUserId;

    /** 支付宝公钥 */
    public String alipayPublicKey;

    /** 私钥 */
    private String privateKey;

    /** 应用公钥证书 */
    private String appCert;

    /** 支付宝公钥证书 */
    private String alipayCert;

    /** 支付宝CA根证书 */
    private String alipayRootCert;

    /** 是否沙箱环境 */
    private boolean sandbox;

    /** 备注 */
    private String remark;

    @Override
    public AlipayConfigResult toResult() {
        return AlipayConfigConvert.CONVERT.toResult(this);
    }
}
