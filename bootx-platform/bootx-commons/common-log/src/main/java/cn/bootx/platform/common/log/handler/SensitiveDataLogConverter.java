package cn.bootx.platform.common.log.handler;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 敏感数据脱敏
 *
 * @author xxm
 * @since 2021/1/4
 */
public class SensitiveDataLogConverter extends MessageConverter {

    // 过滤规则
    private static final Map<String, String> REPLACE_RULES = new HashMap<>();

    // 敏感信息配置
    static {
        REPLACE_RULES.put("(\"cardNum[\\\\]*\"[\\s+]*:[\\s+]*[\\\\]*\")(.*?)(\\w{4})([\\\\]*\")",
                "$1**** **** **** $3$4");
        REPLACE_RULES.put("(\"cardNumber[\\\\]*\"[\\s+]*:[\\s+]*[\\\\]*\")(.*?)(\\w{4})([\\\\]*\")",
                "$1**** **** **** $3$4");
        REPLACE_RULES.put("(\"cardSecurity[\\\\]*\"[\\s+]*:[\\s+]*[\\\\]*\")(.*?)([\\\\]*\")", "$1****$3");
        REPLACE_RULES.put("(\"cvv[\\\\]*\"[\\s+]*:[\\s+]*[\\\\]*\")(.*?)([\\\\]*\")", "$1****$3");
        REPLACE_RULES.put("(\"cardCode[\\\\]*\"[\\s+]*:[\\s+]*[\\\\]*\")(.*?)([\\\\]*\")", "$1****$3");
    }

    @Override
    public String convert(ILoggingEvent event) {
        return convert(event.getFormattedMessage());
    }

    /**
     * 过滤敏感信息
     */
    private static String convert(String msg) {
        if (StrUtil.isBlank(msg)) {
            return msg;
        }
        for (Map.Entry<String, String> entry : REPLACE_RULES.entrySet()) {
            msg = msg.replaceAll(entry.getKey(), entry.getValue());
        }
        return msg;
    }

}
