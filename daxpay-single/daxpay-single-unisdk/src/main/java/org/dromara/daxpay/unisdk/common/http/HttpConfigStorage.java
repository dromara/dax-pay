package org.dromara.daxpay.unisdk.common.http;


import lombok.Getter;
import lombok.Setter;
import org.dromara.daxpay.unisdk.common.bean.CertStoreType;

import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP 配置
 * @author egan
 *  <pre>
 * email egzosn@gmail.com
 * date 2017/3/3 20:48
 *  </pre>
 */
@Getter
public class HttpConfigStorage {
    /**
     * http代理地址
     * -- GETTER --
     *  http代理地址
     *
     * @return http代理地址

     */
    @Setter
    private String httpProxyHost;
    /**
     * 代理端口
     * -- GETTER --
     *    代理端口
     *
     * @return 代理端口

     */
    @Setter
    private int httpProxyPort;
    /**
     * 请求授权用户名
     * -- GETTER --
     *  请求授权用户名
     *
     * @return 用户名

     */
    @Setter
    private String authUsername;
    /**
     * 请求授权密码
     * -- GETTER --
     *  请求授权密码
     *
     * @return 密码

     */
    @Setter
    private String authPassword;


    /**
     * 证书存储类型
     * @see #keystore 是否为https请求所需的证书（PKCS12）的地址,默认为地址，否则为证书信息串，文件流
     */
    @Setter
    private CertStoreType certStoreType = CertStoreType.PATH;

    /**
     * https请求所需的证书（PKCS12）
     * 证书内容
     * -- GETTER --
     *  获取证书信息
     *
     * @return 证书信息 根据 {@link #getCertStoreType()}进行区别地址与信息串

     */
    private Object keystore;
    /**
     * 证书对应的密码
     * -- GETTER --
     *  证书对应的密码
     *
     * @return 密码

     */
    @Setter
    private String storePassword;
    /**
     * 最大连接数
     */
    @Setter
    private int maxTotal = 0;
    /**
     * 默认的每个路由的最大连接数
     */
    @Setter
    private int defaultMaxPerRoute = 0;
    /**
     * 默认使用的响应编码
     */
    @Setter
    private String charset;

    @Setter
    private int socketTimeout = -1;

    @Setter
    private int connectTimeout = -1;


    /**
     * 获取证书信息
     * @return 证书信息 根据 {@link #getCertStoreType()}进行区别地址与信息串
     * @throws IOException 找不到文件异常
     */
    public InputStream getKeystoreInputStream() throws IOException {
        if (null == keystore) {
            return null;
        }
        return certStoreType.getInputStream(keystore);
    }

    /**
     * 获取证书信息 证书地址
     * @return 证书信息 根据 {@link #getCertStoreType()}进行区别地址与信息串
     */
    public String getKeystoreStr() {
        return (String) keystore;
    }

    /**
     * 设置证书字符串信息或证书绝对地址
     * @param keystore 证书信息字符串信息或证书绝对地址
     */
    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }
    /**
     * 设置证书字符串信息输入流
     * @param keystore 证书信息 输入流
     */
    public void setKeystore(InputStream keystore) {
        this.keystore = keystore;
    }

}
