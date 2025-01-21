package cn.bootx.platform.common.mybatisplus.query.code;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参数类型
 *
 * @author xxm
 * @since 2021/11/17
 */
@Getter
@AllArgsConstructor
public enum ParamTypeEnum {

    /** 数字 */
    NUMBER("number", "数字"),

    /** 字符串 */
    STRING("string", "字符串"),

    /** 布尔 */
    BOOLEAN("boolean", "布尔"),

    /** 日期 */
    DATE("date", "日期"),

    /** 时间 */
    TIME("time", "时间"),

    /** 日期时间 */
    DATE_TIME("date_time", "日期时间"),

    /** 列表 */
    LIST("list", "列表");

    /** 类型编码 */
    private final String code;

    /** 类型名称 */
    private final String name;

    public static ParamTypeEnum getByCode(String code) {
        if (StrUtil.isEmpty(code)) {
            return null;
        }
        for (ParamTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

}
