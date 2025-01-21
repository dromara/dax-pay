/**
 * Licensed Property to China UnionPay Co., Ltd.
 * <p>
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 * All Rights Reserved.
 * <p>
 * <p>
 * Modification History:
 * =============================================================================
 * Author         Date          Description
 * ------------ ---------- ---------------------------------------------------
 * xshu       2014-05-28       证书工具类.
 * =============================================================================
 */
package org.dromara.daxpay.unisdk.common.util.sign;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;


/**
 * acpsdk证书工具类，主要用于对证书的加载和使用
 * date 2016-7-22 下午2:46:20
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
@Slf4j
public class CertDescriptor {
    protected static final Logger LOG = LoggerFactory.getLogger(CertDescriptor.class);
    /**
     * 证书容器，存储对商户请求报文签名私钥证书.
     */
    private KeyStore keyStore = null;

    /**
     * 验签公钥/中级证书
     */
    private X509Certificate publicKeyCert = null;
    /**
     * 验签根证书
     */
    private X509Certificate rootKeyCert = null;

    public CertDescriptor() {
    }

    /**
     * 通过证书路径初始化为公钥证书
     *
     * @param certIn 证书流
     * @return X509 证书
     */
    private static X509Certificate initCert(InputStream certIn) {
        X509Certificate encryptCertTemp = null;
        CertificateFactory cf;
        try {
            cf = CertificateFactory.getInstance("X.509");
            encryptCertTemp = (X509Certificate) cf.generateCertificate(certIn);
            // 打印证书加载信息,供测试阶段调试
            if (log.isWarnEnabled()) {
                log.warn("[CertId={}]", encryptCertTemp.getSerialNumber()
                        .toString());
            }
        }
        catch (CertificateException e) {
            log.error("InitCert Error", e);
        }
        finally {
            if (null != certIn) {
                try {
                    certIn.close();
                }
                catch (IOException e) {
                    log.error(e.toString());
                }
            }
        }
        return encryptCertTemp;
    }

    /**
     * 通过证书路径初始化为公钥证书
     *
     * @param path 证书地址
     * @return X509 证书
     */
    private static X509Certificate initCert(String path) {
        X509Certificate encryptCertTemp = null;
        FileInputStream in;
        try {
            in = new FileInputStream(path);
            encryptCertTemp = initCert(in);
        }
        catch (FileNotFoundException e) {
            log.error("InitCert Error File Not Found", e);
        }
        return encryptCertTemp;
    }

    /**
     * 通过keyStore 获取私钥签名证书PrivateKey对象
     *
     * @param pwd 证书对应密码
     * @return PrivateKey 私钥
     */
    public PrivateKey getSignCertPrivateKey(String pwd) {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            return (PrivateKey) keyStore.getKey(keyAlias, pwd.toCharArray());
        }
        catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            log.error("getSignCertPrivateKey Error", e);
            return null;
        }
    }


    /**
     * 配置的签名私钥证书certId
     *
     * @return 证书的物理编号
     */
    public String getSignCertId() {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        }
        catch (Exception e) {
            log.error("getSignCertId Error", e);
            return null;
        }
    }


    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param signCertPath 证书文件名
     * @param signCertPwd  证书密码
     * @param signCertType 证书类型
     */
    public void initPrivateSignCert(String signCertPath, String signCertPwd, String signCertType) {
        if (null != keyStore) {
            keyStore = null;
        }
        try {
            keyStore = getKeyInfo(signCertPath, signCertPwd, signCertType);
            if (log.isInfoEnabled()) {
                log.info("InitSignCert Successful. CertId=[{}]", getSignCertId());
            }
        }
        catch (IOException e) {
            log.error("InitSignCert Error", e);
        }
    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param signCert     证书文件
     * @param signCertPwd  证书密码
     * @param signCertType 证书类型
     */
    public void initPrivateSignCert(InputStream signCert, String signCertPwd, String signCertType) {

        if (null != keyStore) {
            keyStore = null;
        }
        keyStore = getKeyInfo(signCert, signCertPwd, signCertType);
        if (log.isInfoEnabled()) {
            log.info("InitSignCert Successful. CertId=[{}]", getSignCertId());
        }
    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param fxKeyFile 证书文件名
     * @param keyPwd    证书密码
     * @param type      证书类型
     * @return 证书对象
     * @throws IOException
     */
    private KeyStore getKeyInfo(String fxKeyFile, String keyPwd, String type) throws IOException {
        if (log.isWarnEnabled()) {
            log.warn("加载签名证书==>{}", fxKeyFile);
        }
        FileInputStream fis = new FileInputStream(fxKeyFile);
        return getKeyInfo(fis, keyPwd, type);

    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param fxKeyFile 证书文件
     * @param keyPwd    证书密码
     * @param type      证书类型
     * @return 证书对象
     */
    public KeyStore getKeyInfo(InputStream fxKeyFile, String keyPwd, String type) {

        try {
            KeyStore ks = KeyStore.getInstance(type);
            if (log.isWarnEnabled()) {
                log.warn("Load RSA CertPath,Pwd=[{}],type=[{}]", keyPwd, type);
            }

            char[] nPassword;
            nPassword = null == keyPwd || keyPwd.trim()
                    .isEmpty() ? null : keyPwd.toCharArray();
            ks.load(fxKeyFile, nPassword);
            return ks;
        }
        catch (Exception e) {
            log.error("getKeyInfo Error", e);
            return null;
        }
        finally {
            if (null != fxKeyFile) {
                try {
                    fxKeyFile.close();
                }
                catch (IOException e) {
                    log.error("getKeyInfo Error", e);
                }
            }
        }
    }


    /**
     * 通过keystore获取私钥证书的certId值
     *
     * @param keyStore
     * @return
     */
    private String getCertIdIdByStore(KeyStore keyStore) {
        Enumeration<String> aliasenum;
        try {
            aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            X509Certificate cert = (X509Certificate) keyStore
                    .getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        }
        catch (KeyStoreException e) {
            log.error("getCertIdIdByStore Error", e);
            return null;
        }
    }


    /**
     * 加载中级证书
     *
     * @param certPath 证书地址
     */
    public void initPublicCert(String certPath) {
        if (!StrUtil.isEmpty(certPath)) {
            publicKeyCert = initCert(certPath);
            if (log.isInfoEnabled()) {
                log.info("Load PublicKeyCert Successful");
            }
        }
        else if (log.isInfoEnabled()) {
            log.info("PublicKeyCert is empty");
        }
    }

    /**
     * 加载中级证书
     *
     * @param cert 证书文件
     */
    public void initPublicCert(InputStream cert) {
        if (null != cert) {
            publicKeyCert = initCert(cert);
            if (log.isInfoEnabled()) {
                log.info("Load PublicKeyCert Successful");
            }
        }
        else if (log.isInfoEnabled()) {
            log.info("PublicKeyCert is empty");
        }
    }

    /**
     * 加载根证书
     *
     * @param certPath 证书地址
     */
    public void initRootCert(String certPath) {
        if (!StrUtil.isEmpty(certPath)) {
            try {
                initRootCert(new FileInputStream(certPath));
            }
            catch (FileNotFoundException e) {
                log.info("RootCert is empty");
            }

        }
        else if (log.isInfoEnabled()) {
            log.info("RootCert is empty");
        }
    }

    /**
     * 加载根证书
     *
     * @param cert 证书文件
     */
    public void initRootCert(InputStream cert) {
        if (null != cert) {
            rootKeyCert = initCert(cert);
            if (log.isInfoEnabled()) {
                log.info("Load RootCert Successful");
            }
        }
        else if (log.isInfoEnabled()) {
            log.info("RootCert is empty");
        }
    }

    /**
     * 获取公钥/中级证书
     *
     * @return X509Certificate
     */
    public X509Certificate getPublicCert() {
        return publicKeyCert;
    }

    /**
     * 获取中级证书
     *
     * @return X509Certificate
     */
    public X509Certificate getRootCert() {
        return rootKeyCert;
    }

}
