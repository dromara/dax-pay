package cn.daxpay.open.platform.core.annotation;

import java.lang.annotation.*;

/// # 字段翻译注解
///
/// 标注在 Result 类的字段上，运行时通过 TransService 自动关联查询并回填翻译结果
///
/// 支持两种翻译模式：
/// - **实体翻译**：通过 entity 指定目标实体，查询数据库翻译（现有方式）
/// - **字典翻译**：通过 dictCode 指定字典编码，从字典项中翻译（新增）
///
/// 翻译流程：
/// - 读取当前对象中 source 字段的值作为查询条件
/// - 到目标实体 entity 中匹配对应记录（on 指定匹配字段，缺省用 source）
/// - 取出 result 字段的值回填到当前被注解的字段
///
/// 使用示例：
/// ```java
/// // 实体翻译：
/// @Trans(entity = MerchantInfo.class, source = "mchNo", result = "name")
/// private String isvName;
///
/// // 字典翻译：
/// @Trans(dictCode = "product", source = "productCode", result = "nameCn")
/// private String productName;
/// ```
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trans {

    /// 目标实体类（实体翻译时使用）
    /// 翻译时通过此实体类对应的 Mapper 和 TableInfo 执行数据库查询
    /// 例如：IsvInfo.class 表示查询 isv_info 表
    /// 字典翻译时无需指定
    Class<?> entity() default Void.class;

    /// 字典编码（字典翻译时使用）
    /// 通过字典编码获取字典项列表，根据 source 的值匹配字典项编码，取出 result 对应的字段值
    /// 例如：dictCode = "product" 表示从 product 字典中翻译
    /// 实体翻译时无需指定
    String dictCode() default "";

    /// 源字段名（当前 Result 中作为查询条件的字段名）
    /// 翻译时从当前 Result 对象中读取此字段的值，作为数据库查询的条件值
    /// 例如：source = "mchNo" 表示取当前对象的 mchNo 属性值
    String source();

    /// 目标实体中的匹配字段名
    /// 在目标实体中按此字段匹配查询条件，不指定时默认与 source 相同
    /// 当 Result 中的字段名与目标实体中的属性名不一致时使用
    /// 例如：source = "isvId", on = "id" 表示用 isvId 的值去匹配目标实体的 id 字段
    String on() default "";

    /// 目标实体/字典项中要翻译出的字段名
    /// 从目标实体匹配到的记录中取出此字段的值，回填到当前被 @Trans 注解的字段
    /// 例如：result = "name" 表示从目标实体中取出 name 字段的值作为翻译结果
    String result();

    /// 缓存存活时间（秒）
    /// 翻译结果在缓存中的存活时长，超过后自动过期重新查询
    /// 默认 30 秒，设置为 0 表示不缓存
    int cacheTtl() default 30;
}

