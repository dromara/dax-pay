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

    /// PEM 脱敏行宽(运营端抽屉内 12px 等宽字体一行约 53 字符, 留余量取 48)
    private static final int PEM_LINE_WIDTH = 48;

    /// PEM 正文前后保留的明文字符数(便于与源文件比对; 2048 位私钥约 1590 字符, 露出 64 字符不足以推导密钥)
    private static final int PEM_PLAIN_LENGTH = 32;

    /// PEM 正文中间顶替 base64 主体的星号行数
    private static final int PEM_STAR_LINES = 2;

    /// PEM 起始行前缀
    private static final String PEM_BEGIN = "-----BEGIN";

    /// PEM 结束行前缀
    private static final String PEM_END = "-----END";

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
            case OTHER -> jsonGenerator.writeString(this.hide(s, sensitiveInfo.front(), sensitiveInfo.end(),
                    sensitiveInfo.compact()));
            default -> jsonGenerator.writeString(s);
        }

    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext ctxt, BeanProperty property) {
        if (Objects.isNull(property)) { // 为空直接跳过
            return this;
        }
        if (!Objects.equals(property.getType().getRawClass(), String.class)) { // 非 String 类直接跳过
            return null;
        }
        SensitiveInfo sensitiveInfo = property.getAnnotation(SensitiveInfo.class);
        if (Objects.isNull(sensitiveInfo)) {
            sensitiveInfo = property.getContextAnnotation(SensitiveInfo.class);
        }
        if (Objects.nonNull(sensitiveInfo)) { // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
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
        return this.hide(str, front, end, false);
    }

    /// 字段隐藏(可压缩: 中间星号固定 6 颗而非全长度填充, 便于长证书头尾查看)
    /// @param str 字符串
    /// @param front 前多少位不隐藏
    /// @param end 后多少位不隐藏
    /// @param compact 压缩脱敏, 仅 front/end 保留型生效
    /// @return 处理后的字段
    private String hide(String str, int front, int end, boolean compact) {
        // 字符串不能为空
        if (StrUtil.isBlank(str)) {
            return StrUtil.EMPTY;
        }
        // 需要截取的不能小于0
        if (front < 0 || end < 0) {
            return StrUtil.EMPTY;
        }
        // PEM 内容优先按行结构脱敏(与注解的 front/end/compact 无关, 6/6 与 24/24 统一为可核对的密钥形态)
        String pemMasked = this.hidePem(str);
        if (pemMasked != null) {
            return pemMasked;
        }
        // 保留位数之和超过原长度时, hide 区间为空或反转可能导致明文暴露, 统一全量脱敏
        if (front + end >= str.length()) {
            return StrUtil.repeat('*', 4);
        }
        if (compact) {
            return str.substring(0, front) + StrUtil.repeat('*', 6) + str.substring(str.length() - end);
        }
        return StrUtil.hide(str, front, str.length() - end);
    }

    /// PEM 内容脱敏(多行形态: 完整 BEGIN/END 行 + 正文首尾各露 [#PEM_PLAIN_LENGTH] 个明文字符 + 中间星号行)
    ///
    /// 首尾各 24 字符的旧压缩策略在 PEM 上只能露出 `-----BEGIN PRIVATE KEY--` 这类固定标记, 看不到任何 base64 内容,
    /// 不同密钥的脱敏串完全相同, 无法核对是否配置错; 此处按 PEM 行结构重排, 保留完整头尾标记与正文首尾明文。
    /// 行宽固定 [#PEM_LINE_WIDTH] 字符以适配运营端抽屉内的等宽字体展示, 超出会折行。
    /// @param str 原始值
    /// @return 脱敏串; 非 PEM(无 BEGIN/END 行, 或多行不足三行, 或 BEGIN/END 之间无正文)返回 null, 交由原 front/end 逻辑处理
    private String hidePem(String str) {
        String[] lines = str.strip().split("\\R");
        if (lines.length < 3) {
            return null;
        }
        String beginLine = lines[0].trim();
        String endLine = lines[lines.length - 1].trim();
        if (!beginLine.startsWith(PEM_BEGIN) || !endLine.startsWith(PEM_END)) {
            return null;
        }
        // 证书链等含多个 BEGIN/END 块的场景: 取首个正文行与末个正文行作首尾明文, 中间块由星号概括
        String bodyHead = null;
        for (int i = 1; i < lines.length - 1; i++) {
            String line = lines[i].trim();
            if (!line.isEmpty()) {
                bodyHead = line;
                break;
            }
        }
        String bodyTail = null;
        for (int i = lines.length - 2; i >= 1; i--) {
            String line = lines[i].trim();
            if (!line.isEmpty()) {
                bodyTail = line;
                break;
            }
        }
        if (bodyHead == null || bodyTail == null) {
            return null;
        }
        String starLine = StrUtil.repeat('*', PEM_LINE_WIDTH);
        StringBuilder masked = new StringBuilder(beginLine).append('\n');
        masked.append(this.pemBodyLine(StrUtil.subPre(bodyHead, PEM_PLAIN_LENGTH), true)).append('\n');
        for (int i = 0; i < PEM_STAR_LINES; i++) {
            masked.append(starLine).append('\n');
        }
        int tailIndex = Math.max(0, bodyTail.length() - PEM_PLAIN_LENGTH);
        masked.append(this.pemBodyLine(StrUtil.subSuf(bodyTail, tailIndex), false)).append('\n');
        masked.append(endLine);
        return masked.toString();
    }

    /// PEM 正文行的定宽填充(明文在行首则尾部补星, 明文在行尾则头部补星)
    /// @param plain 保留的明文片段
    /// @param atHead 明文字段是否位于行首
    /// @return 行宽为 [#PEM_LINE_WIDTH] 的脱敏行, 明文本身超宽时原样返回
    private String pemBodyLine(String plain, boolean atHead) {
        String stars = StrUtil.repeat('*', Math.max(0, PEM_LINE_WIDTH - plain.length()));
        return atHead ? plain + stars : stars + plain;
    }

}