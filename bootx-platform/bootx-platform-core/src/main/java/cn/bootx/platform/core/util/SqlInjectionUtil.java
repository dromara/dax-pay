package cn.bootx.platform.core.util;

import cn.bootx.platform.core.exception.DangerSqlException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql注入处理工具类 参考自 Jeecg Boot
 *
 * @author zhoujf
 */
@Slf4j
@UtilityClass
public class SqlInjectionUtil {

    private final String XSS_STR = "and |extractvalue|updatexml|geohash|gtid_subset|gtid_subtract|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|user()";

    /**
     * 正则 user() 匹配更严谨
     */
    private final String REGULAR_EXPRE_USER = "user[\\s]*\\([\\s]*\\)";

    /** 正则 show tables */
    private final String SHOW_TABLES = "show\\s+tables";

    /**
     * sql注释的正则
     */
    private final Pattern SQL_ANNOTATION = Pattern.compile("/\\*[\\s\\S]*\\*/");

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     */
    public void filterContent(String value) {
        filterContent(value, null);
    }

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     *
     */
    public void filterContent(String value, String customXssString) {
        if (value == null || value.isEmpty()) {
            return;
        }
        // 校验sql注释 不允许有sql注释
        checkSqlAnnotation(value);
        // 统一转为小写
        value = value.toLowerCase();
        // SQL注入检测存在绕过风险
        String[] xssArr = XSS_STR.split("\\|");
        for (String s : xssArr) {
            if (value.contains(s)) {
                log.error("请注意，存在SQL注入关键词---> {}", s);
                log.error("请注意，值可能存在SQL注入风险!---> {}", value);
                throw new DangerSqlException("请注意，值可能存在SQL注入风险!--->" + value);
            }
        }
        // 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
        if (customXssString != null) {
            String[] xssArr2 = customXssString.split("\\|");
            for (String s : xssArr2) {
                if (value.contains(s)) {
                    log.error("请注意，存在SQL注入关键词---> {}", s);
                    log.error("请注意，值可能存在SQL注入风险!---> {}", value);
                    throw new DangerSqlException("请注意，值可能存在SQL注入风险!--->" + value);
                }
            }
        }
        // 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
        if (Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)) {
            throw new DangerSqlException("请注意，值可能存在SQL注入风险!--->" + value);
        }
    }

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     */
    public void filterContent(String[] values) {
        filterContent(values, null);
    }

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     */
    public void filterContent(String[] values, String customXssString) {
        String[] xssArr = XSS_STR.split("\\|");
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return;
            }
            // 校验sql注释 不允许有sql注释
            checkSqlAnnotation(value);
            // 统一转为小写
            value = value.toLowerCase();

            for (String s : xssArr) {
                if (value.contains(s)) {
                    log.error("请注意，存在SQL注入关键词---> {}", s);
                    log.error("请注意，值可能存在SQL注入风险!---> {}", value);
                    throw new DangerSqlException("请注意，值可能存在SQL注入风险!--->" + value);
                }
            }
            // 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
            if (customXssString != null) {
                String[] xssArr2 = customXssString.split("\\|");
                for (String s : xssArr2) {
                    if (value.contains(s)) {
                        log.error("请注意，存在SQL注入关键词---> {}", s);
                        log.error("请注意，值可能存在SQL注入风险!---> {}", value);
                        throw new DangerSqlException("请注意，值可能存在SQL注入风险!--->" + value);
                    }
                }
            }
            // 除了XSS_STR这些提前设置好的，还需要额外的校验比如 单引号
            if (Pattern.matches(SHOW_TABLES, value) || Pattern.matches(REGULAR_EXPRE_USER, value)) {
                throw new DangerSqlException("请注意，值可能存在SQL注入风险!--->" + value);
            }
        }
    }

    /**
     * 校验是否有sql注释
     */
    public void checkSqlAnnotation(String str) {
        Matcher matcher = SQL_ANNOTATION.matcher(str);
        if (matcher.find()) {
            String error = "请注意，值可能存在SQL注入风险---> \\*.*\\";
            log.error(error);
            throw new DangerSqlException(error);
        }
    }

}
