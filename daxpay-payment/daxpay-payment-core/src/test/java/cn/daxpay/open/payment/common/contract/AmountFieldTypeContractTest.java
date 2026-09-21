package cn.daxpay.open.payment.common.contract;

import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/// # 金额字段类型契约测试
///
/// param/result 契约层的金额语义字段只允许 `Long`(分, 最小货币单位):
/// - unipay 对外契约与内部存储统一为分, 见契约正典 `_doc/design/sdk-contract.md`
/// - 2026-09-21 转账/分账参数回归 Long 分后, 以本测试防 BigDecimal(元) 形态复发
///
/// 扫描范围: 本模块 `cn.daxpay.open.payment` 下类名以 Param/Result/Query/Bo 结尾的契约类
/// (含 `$` 嵌套类, 如 AllocParam.AllocReceiverParam); 测试夹具不在 target/classes, 天然排除。
/// 豁免: `@ExcelProperty` 导出列(展示层元格式串)、词尾为元数据词的字段(amountType 等)。
class AmountFieldTypeContractTest {

    /// 金额语义字段名词(驼峰拆词后整词匹配)
    private static final String[] AMOUNT_KEYWORDS = { "amount", "fee", "price", "balance" };

    /// 元数据词尾(该词结尾的字段是金额的元数据而非金额值)
    private static final Set<String> METADATA_SUFFIX_WORDS =
            Set.of("type", "mode", "status", "currency", "unit", "flag", "symbol");

    @Test
    void amountFieldsMustBeLongCents() throws Exception {
        // 枚举 classpath 上全部命中目录(target/classes 与 test-classes 都会出现), 只扫主代码目录
        List<File> classFiles = new ArrayList<>();
        Enumeration<URL> roots = getClass().getClassLoader().getResources("cn/daxpay/open/payment");
        int mainRoots = 0;
        while (roots.hasMoreElements()) {
            URL root = roots.nextElement();
            if (!"file".equals(root.getProtocol()) || root.toString().contains("test-classes")) {
                continue;
            }
            mainRoots++;
            File rootDir = new File(root.toURI());
            try (Stream<Path> paths = Files.walk(rootDir.toPath())) {
                paths.filter(p -> p.toString().endsWith(".class")).map(Path::toFile).forEach(classFiles::add);
            }
        }
        assertTrue(mainRoots > 0, "类路径上未找到主代码目录 cn/daxpay/open/payment");
        List<String> violations = new ArrayList<>();
        int checked = 0;
        for (File file : classFiles) {
            String path = file.getAbsolutePath().replace(File.separatorChar, '.');
            String className = path.substring(path.indexOf("cn.daxpay.open.payment."))
                    .replaceAll("\\.class$", "");
            Class<?> clazz;
            try {
                clazz = Class.forName(className, false, getClass().getClassLoader());
            } catch (Throwable ignored) {
                // 依赖缺失等加载失败的类跳过, 不影响金额契约检查
                continue;
            }
            if (clazz.isInterface() || clazz.isEnum() || clazz.isAnnotation()) {
                continue;
            }
            String simpleName = className.substring(className.lastIndexOf('.') + 1);
            boolean isContractClass = simpleName.endsWith("Param") || simpleName.endsWith("Result")
                    || simpleName.endsWith("Query") || simpleName.endsWith("Bo");
            if (!isContractClass) {
                continue;
            }
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                // Excel 导出列是展示层(元格式化字符串), 不属传输契约, 豁免
                if (field.getAnnotation(ExcelProperty.class) != null) {
                    continue;
                }
                // 驼峰拆词: 金额词命中且词尾非元数据词(type/mode/currency 等)才算金额值字段
                // 如 amount 命中、amountType 是金额元数据不命中
                String[] words = field.getName()
                        .replaceAll("([a-z])([A-Z])", "$1 $2")
                        .toLowerCase()
                        .split(" ");
                String lastWord = words[words.length - 1];
                if (METADATA_SUFFIX_WORDS.contains(lastWord)) {
                    continue;
                }
                for (String keyword : AMOUNT_KEYWORDS) {
                    if (List.of(words).contains(keyword)) {
                        checked++;
                        Class<?> type = field.getType();
                        boolean longType = type == Long.class || type == long.class;
                        if (!longType) {
                            violations.add(className + "#" + field.getName() + " 类型为 "
                                    + type.getSimpleName() + ", 金额字段只允许 Long(分)");
                        }
                        break;
                    }
                }
            }
        }
        assertTrue(violations.isEmpty(),
                "金额字段类型违例(应统一为 Long 分):\n" + String.join("\n", violations));
        assertTrue(checked > 20, "扫描到的金额字段数量异常偏少(" + checked + "), 检查类路径扫描是否失效");
    }
}
