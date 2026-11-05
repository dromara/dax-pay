package cn.daxpay.open.platform.common.json.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import tools.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/// # 敏感信息过滤
///
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface SensitiveInfo {

    /// 敏感信息类型
    SensitiveType value() default SensitiveType.OTHER;

    /// 敏感类型为其他可用 front – 保留：前面的front位数；从1开始
    int front() default 4;

    /// 敏感类型为其他可用 end – 保留：后面的end位数；从1开始
    int end() default 4;

    /// 压缩脱敏(仅 front/end 型生效): 中间星号固定 6 颗而非全长度填充,
    /// - 长密钥脱敏串从注解的 front/end 位置保留明文, 中间 6 星(如 24 时总长约 54 字符), 便于表单回显与头尾核对;
    /// - 值为多行 PEM 内容(首行 `-----BEGIN`, 末行 `-----END`)时不看本参数与 front/end, 统一按行结构脱敏:
    ///   完整 BEGIN/END 行 + 正文首尾各 32 字符明文 + 中间星号行, 见 [SensitiveInfoSerialize#hidePem];
    /// 保留位数之和不小于原长时退化为全量脱敏 4 星
    boolean compact() default false;

    /// # 敏感信息
    ///
    enum SensitiveType {

        /// 中文名
        CHINESE_NAME,
        /// 用户id
        USER_ID,
        /// 密码
        PASSWORD,
        /// 身份证号
        ID_CARD,
        /// 座机号
        FIXED_PHONE,
        /// 手机号
        MOBILE_PHONE,
        /// ip地址
        IP,
        /// 地址
        ADDRESS,
        /// 电子邮件
        EMAIL,
        /// 车牌号
        CAR_LICENSE,
        /// 银行卡
        BANK_CARD,
        /// 公司开户银行联号
        CNAPS_CODE,

        /// 其他
        OTHER,

    }

}