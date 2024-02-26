package cn.bootx.platform.daxpay.service.configuration.ijpay;

import com.ijpay.core.http.AbstractHttpDelegate;
import lombok.AllArgsConstructor;

import java.io.InputStream;

/**
 * 重写IJPay的请求类, 把请求加密方式从TLS更改为TLS1.2
 * @author xxm
 * @since 2024/2/26
 */
@AllArgsConstructor
public class DaxPayHttpDelegate extends AbstractHttpDelegate {
    /** TLS版本 */
    private final String tlsVersion;


    /**
     * post 请求
     *
     * @param url      请求url
     * @param data     请求参数
     * @param certPath 证书路径
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public String post(String url, String data, String certPath, String certPass) {
        return post(url, data, certPath, certPass, tlsVersion);
    }

    /**
     * 上传文件
     *
     * @param url      请求url
     * @param data     请求参数
     * @param certPath 证书路径
     * @param certPass 证书密码
     * @param filePath 上传文件路径
     * @return {@link String}  请求返回的结果
     */
    public String upload(String url, String data, String certPath, String certPass, String filePath) {
        return upload(url, data, certPath, certPass, filePath, tlsVersion);
    }

    /**
     * post 请求
     *
     * @param url      请求url
     * @param data     请求参数
     * @param certFile 证书文件输入流
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public String post(String url, String data, InputStream certFile, String certPass) {
        return post(url, data, certFile, certPass, tlsVersion);
    }


}
