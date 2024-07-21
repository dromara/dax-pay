package cn.bootx.platform.common.mybatisplus.query.code;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 匹配条件类型
 *
 * @author xxm
 * @since 2021/11/17
 */
@Getter
@AllArgsConstructor
public enum CompareTypeEnum {

    GT("gt", ">", "大于"),

    GE("ge", ">=", "大于等于"),

    LT("lt", "<", "小于"),

    LE("le", "<=", "小于等于"),

    EQ("eq", "=", "等于"),

    NE("ne", "!=", "不等于"),

    IN("in", "IN", "包含"),

    NOT_IN("not_in", "NOT_IN", "不包含"),

    BETWEEN("between", "BETWEEN", "在之间"),

    NOT_BETWEEN("not_between", "NOT_BETWEEN", "不在之间"),

    LIKE("like", "LIKE", "全模糊匹配"),

    NOT_LIKE("not_like", "NOT_LIKE", "全模糊不匹配"),

    LIKE_LEFT("like_left", "LIKE_LEFT", "左模糊"),

    LIKE_RIGHT("like_right", "LIKE_RIGHT", "右模糊"),

    IS_NULL("is_null", "IS_NULL", "为空"),

    NOT_NULL("not_null", "NOT_NULL", "不为空");

    private final String code;

    private final String value;

    private final String msg;

    public static CompareTypeEnum getByCode(String code) {
        if (StrUtil.isEmpty(code)) {
            return null;
        }
        for (CompareTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

}
