package org.dromara.daxpay.platform.core.util;

import org.dromara.daxpay.platform.core.annotation.PermCode;
import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/// # 权限码解析工具
///
/// 统一 {@link PermCode} 注解的 menuCode、code 拼接规则，供扫描同步与路由鉴权共用。
/// 完整权限码 = menuCode + ":" + code（menuCode 非空时）。
public final class PermCodeUtil {

    private PermCodeUtil() {
    }

    /// 解析完整权限码；方法注解优先于类注解的各字段。
    public static String resolveFullCode(PermCode classPermCode, PermCode methodPermCode) {
        String code = resolveActionCode(classPermCode, methodPermCode);
        if (StrUtil.isBlank(code)) {
            return "";
        }
        String menuCode = resolveMenuCode(classPermCode, methodPermCode);
        if (StrUtil.isNotBlank(menuCode)) {
            return menuCode + ":" + code;
        }
        return code;
    }

    /// 解析操作码（未拼接 menuCode）。
    public static String resolveActionCode(PermCode classPermCode, PermCode methodPermCode) {
        if (Objects.nonNull(methodPermCode) && StrUtil.isNotBlank(methodPermCode.code())) {
            return methodPermCode.code();
        }
        if (Objects.nonNull(classPermCode) && StrUtil.isNotBlank(classPermCode.code())) {
            return classPermCode.code();
        }
        return "";
    }

    /// 解析归属菜单编码。
    public static String resolveMenuCode(PermCode classPermCode, PermCode methodPermCode) {
        if (Objects.nonNull(methodPermCode) && StrUtil.isNotBlank(methodPermCode.menuCode())) {
            return methodPermCode.menuCode();
        }
        if (Objects.nonNull(classPermCode) && StrUtil.isNotBlank(classPermCode.menuCode())) {
            return classPermCode.menuCode();
        }
        return "";
    }

    /// 解析中文名称。
    public static String resolveNameCn(PermCode classPermCode, PermCode methodPermCode) {
        if (Objects.nonNull(methodPermCode) && StrUtil.isNotBlank(methodPermCode.nameCn())) {
            return methodPermCode.nameCn();
        }
        if (Objects.nonNull(classPermCode) && StrUtil.isNotBlank(classPermCode.nameCn())) {
            return classPermCode.nameCn();
        }
        return "";
    }

    /// 解析英文名称。
    public static String resolveNameEn(PermCode classPermCode, PermCode methodPermCode) {
        if (Objects.nonNull(methodPermCode) && StrUtil.isNotBlank(methodPermCode.nameEn())) {
            return methodPermCode.nameEn();
        }
        if (Objects.nonNull(classPermCode) && StrUtil.isNotBlank(classPermCode.nameEn())) {
            return classPermCode.nameEn();
        }
        return "";
    }

}
