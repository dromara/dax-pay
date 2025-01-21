package org.dromara.daxpay.unisdk.common.api;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * 支付基础配置存储
 *
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2017/3/5 20:33
 *  </pre>
 */
public abstract class BasePayConfigStorage implements PayConfigStorage {

    @Setter
    private Object attach;

    /**
     * 应用私钥，rsa_private pkcs8格式 生成签名时使用
     */
    @Setter
    private String keyPrivate;

    /**
     * 支付平台公钥(签名校验使用)
     */
    @Setter
    private String keyPublic;
    /**
     * 异步回调地址
     */
    @Setter
    private String notifyUrl;
    /**
     * 同步回调地址，支付完成后展示的页面
     */
    @Setter
    private String returnUrl;
    /**
     * 签名加密类型
     */
    @Setter
    private String signType;
    /**
     * 字符类型
     */
    @Setter
    private String inputCharset;


    /**
     * 支付类型 aliPay 支付宝， wxPay微信..等等，扩展支付模块定义唯一。
     */
    @Setter
    private String payType;


    /**
     * 访问令牌 每次请求其他方法都要传入的值
     * -- GETTER --
     *  获取访问令牌
     *
     * @return 访问令牌

     */
    @Setter
    @Getter
    private volatile String accessToken;
    /**
     * access token 到期时间时间戳
     * -- GETTER --
     *  强制将access token过期掉
     *
     * @return 过期时间

     */
    @Setter
    @Getter
    private volatile long expiresTime;
    /**
     * 授权码锁
     * -- GETTER --
     *  获取access token锁
     *
     * @return access token锁

     */
    @Setter
    @Getter
    private Lock accessTokenLock;
    /**
     * 是否为沙箱环境，默认为正式环境
     */
    private boolean isTest = false;

    /**
     * 是否为证书签名
     */
    @Setter
    @Getter
    private boolean certSign = false;

    /**
     * 配置附加信息，可用于预设未提供的参数，这里会覆盖以上所有的配置信息，
     */
    private volatile Map<String, Object> attr;

    @Override
    public Object getAttach() {
        return attach;
    }

    @Override
    public String getKeyPrivate() {
        return keyPrivate;
    }


    @Override
    public String getKeyPublic() {
        return keyPublic;
    }

    @Override
    public String getNotifyUrl() {
        return notifyUrl;
    }

    @Override
    public String getReturnUrl() {
        return returnUrl;
    }

    @Override
    public String getSignType() {
        return signType;
    }

    @Override
    public String getInputCharset() {
        return inputCharset;
    }

    @Override
    public String getPayType() {
        return payType;
    }

    /**
     * 访问令牌是否过期
     *
     * @return true过期
     */
    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > this.expiresTime;
    }


    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        updateAccessToken(accessToken, System.currentTimeMillis() + (expiresInSeconds - 600) * 1000L);
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, long expiresTime) {
        this.accessToken = accessToken;
        this.expiresTime = expiresTime;
    }


    /**
     * 强制将access token过期掉
     */
    public void expireAccessToken() {
        this.expiresTime = 0;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    @Override
    public Map<String, Object> getAttrs() {
        if (null == attr) {
            attr = new HashMap<>();
        }
        return attr;
    }

    @Override
    public Object getAttr(String key) {
        return getAttrs().get(key);
    }


    /**
     * 添加配置信息
     *
     * @param key   key
     * @param value 值
     */
    public void addAttr(String key, Object value) {
        getAttrs().put(key, value);
    }
}
