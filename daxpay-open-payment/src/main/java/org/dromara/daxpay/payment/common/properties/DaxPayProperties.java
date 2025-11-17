package org.dromara.daxpay.payment.common.properties;

import org.dromara.daxpay.payment.common.exception.ConfigErrorException;
import org.dromara.daxpay.payment.common.util.RsaSignUtil;
import org.dromara.daxpay.payment.common.util.TradeNoGenerateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * daxpay配置类
 * @author xxm
 * @since 2024/2/8
 */
@Getter
@Setter
@Slf4j
@ConfigurationProperties(prefix = "dax-pay")
public class DaxPayProperties {

    /** 机器码(2位字符,和数字), 用于区分不同机器生成的流水号 */
    private String machineNo = "58";

    /**  业务环境前缀(最长10位,字符和数字)，会影响订单号的生成, 受部分通道, 所以尽量要独特, 保证全局唯一 */
    private String env = "";

    /** 平台公私钥配置 */
    private KeyConfig keyConfig = new KeyConfig();

    /** 证书读取密钥获取方式 */
    private LicenseKey licenseKey = new LicenseKey();

    public void setMachineNo(String machineNo) {
        if (machineNo.length() > 2){
            throw new ConfigErrorException("机器码配置参数最长为2位");
        }
        if (!Validator.isMatchRegex("^[a-zA-Z0-9]+$", machineNo)){
            throw new ConfigErrorException("机器码配置参数只能为数字和字母");
        }
        this.machineNo = machineNo;
        TradeNoGenerateUtil.setMachineNo(machineNo);
    }

    public void setEnv(String env) {
        if (env.length() > 10){
            throw new ConfigErrorException("业务环境前缀配置参数最长为10位");
        }
        if (!Validator.isMatchRegex("^[a-zA-Z0-9]+$", env)){
            throw new ConfigErrorException("业务环境前缀配置参数只能为数字和字母");
        }
        this.env = env;
        TradeNoGenerateUtil.setEnv(env);
    }

    /**
     * 平台公私钥配置
     */
    @Data
    @Accessors(chain = true)
    public static class KeyConfig {
        /** 平台公钥 */
        private String publicKey;

        /** 平台私钥 */
        private String privateKey;

        public KeyConfig setPublicKey(String publicKey) {
            if (StrUtil.isNotBlank(publicKey)){
                RsaSignUtil.loadPublicKeyFromPem(publicKey);
                log.info("加载平台公钥成功");
            } else {
                log.warn("未加载平台公钥");
            }
            this.publicKey = publicKey;
            return this;
        }

        public KeyConfig setPrivateKey(String privateKey) {
            if (StrUtil.isNotBlank(privateKey)){
                RsaSignUtil.loadPrivateKeyFromPem(privateKey);
                log.info("加载平台私钥成功");
            } else {
                log.warn("未加载平台私钥");
            }
            this.privateKey = privateKey;
            return this;
        }
    }

    /**
     * 证书方式
     */
    @Data
    @Accessors(chain = true)
    public static class LicenseKey {
        /** 获取标识类 */
        private String clazz;
        /** 获取密码key标识方法 */
        private String passwordMethod;
        /** 获取公钥方法 */
        private String publicKeyMethod;
    }
}
