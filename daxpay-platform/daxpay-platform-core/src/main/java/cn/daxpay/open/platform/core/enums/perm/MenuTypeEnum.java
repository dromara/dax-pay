package cn.daxpay.open.platform.core.enums.perm;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 菜单类型枚举
///
@Getter
@RequiredArgsConstructor
public enum MenuTypeEnum implements I18nSupport {

    /// 目录
    CATALOG("catalog"),
    /// 菜单
    MENU("menu"),
    /// 子页面
    SUBPAGE("subpage"),
    /// 子页面分组
    SUBPAGE_GROUP("subpage_group"),
    /// 内嵌页面
    EMBEDDED("embedded"),
    /// 外链
    LINK("link");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.menu_type";
    }
    public static MenuTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (MenuTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

    public boolean equalsCode(String code) {
        return this.code.equals(code);
    }

    public boolean canAddChild() {
        return this == CATALOG || this == MENU || this == SUBPAGE_GROUP;
    }

    public boolean needParent() {
        return this != CATALOG;
    }

}
