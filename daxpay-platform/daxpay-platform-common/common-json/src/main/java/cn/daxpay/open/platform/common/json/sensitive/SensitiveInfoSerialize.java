package cn.daxpay.open.platform.common.json.sensitive;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/// # 敏感信息脱敏序列化
///
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveInfoSerialize extends ValueSerializer<String> {

    private SensitiveInfo sensitiveInfo;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializationContext ctxt)
            throws JacksonException {
        switch (this.sensitiveInfo.value()) {
            case CHINESE_NAME -> jsonGenerator.writeString(DesensitizedUtil.chineseName(s));
            case USER_ID -> jsonGenerator.writeString(String.valueOf(DesensitizedUtil.userId()));
            case PASSWORD -> jsonGenerator.writeString(DesensitizedUtil.password(s));
            case ID_CARD -> jsonGenerator.writeString(DesensitizedUtil.idCardNum(s, 6, 2));
            case FIXED_PHONE -> jsonGenerator.writeString(DesensitizedUtil.fixedPhone(s));
            case MOBILE_PHONE -> jsonGenerator.writeString(DesensitizedUtil.mobilePhone(s));
            case IP -> jsonGenerator.writeString(this.ip(s));
            case ADDRESS -> jsonGenerator.writeString(DesensitizedUtil.address(s, 6));
            case CAR_LICENSE -> jsonGenerator.writeString(DesensitizedUtil.carLicense(s));
            case EMAIL -> jsonGenerator.writeString(DesensitizedUtil.email(s));
            case BANK_CARD -> jsonGenerator.writeString(DesensitizedUtil.bankCard(s));
            case CNAPS_CODE -> jsonGenerator.writeString(this.hide(s, 4, 4));
            case OTHER -> jsonGenerator.writeString(this.hide(s, sensitiveInfo.front(), sensitiveInfo.end()));
            default -> jsonGenerator.writeString(s);
        }

    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext ctxt, BeanProperty property) {
        if (property == null) { // 为空直接跳过
            return this;
        }
        if (!Objects.equals(property.getType().getRawClass(), String.class)) { // 非 String 类直接跳过
            return null;
        }
        SensitiveInfo sensitiveInfo = property.getAnnotation(SensitiveInfo.class);
        if (sensitiveInfo == null) {
            sensitiveInfo = property.getContextAnnotation(SensitiveInfo.class);
        }
        if (sensitiveInfo != null) { // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
            return new SensitiveInfoSerialize(sensitiveInfo);
        }
        return null; // 无注解，让 Jackson 使用默认序列化器
    }

    /// ip脱敏
    private String ip(String ip) {
        List<String> ipList = StrUtil.split(ip, '.');
        if (ipList.size() < 2) {
            return "*.*.*.*";
        }
        return ipList.get(0) + "." + ipList.get(1) + ".*.*";
    }

    /// 字段隐藏
    /// @param str 字符串
    /// @param front 前多少位不隐藏
    /// @param end 后多少位不隐藏
    /// @return 处理后的字段
    private String hide(String str, int front, int end) {
        // 字符串不能为空
        if (StrUtil.isBlank(str)) {
            return StrUtil.EMPTY;
        }
        // 需要截取的不能小于0
        if (front < 0 || end < 0) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(str, front, str.length() - end);
    }

}