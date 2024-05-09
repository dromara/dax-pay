package cn.bootx.platform.starter.code.gen.util;

import cn.hutool.core.text.NamingCase;
import lombok.experimental.UtilityClass;

/**
 * 代码生成工具类
 *
 * @author xxm
 * @since 2022/8/2
 */
@UtilityClass
public class CodeGenUtil {

    /**
     * 表名转换成Java类名 大驼峰
     */
    public String tableToJava(String tableName) {
        // 自动去除表前缀
        return NamingCase.toPascalCase(tableName.substring(tableName.indexOf("_") + 1));
    }

}
