# common-i18n

后端国际化资源与 `JsonMessageSource` 实现。

## 目录结构

```
src/main/resources/i18n/
  zh-CN/
  en-US/
    enum/          # 字典/枚举翻译（文件名 snake_case）
    error/         # 业务异常
    pay/           # 支付域
    validation.json
```

## Key 生成规则

1. 路径：`i18n/{locale}/pay/route/error.json` → 前缀 `pay.route.error`
2. JSON：`{ "duplicateSceneConfig": "..." }` → `pay.route.error.duplicateSceneConfig`
3. 枚举：`enum/pay_provider.json` + `"union_pay"` + `PayProviderEnum.getCode()` → `enum.pay_provider.union_pay`

## 命名规范（摘要）

| 层级               | 风格                                                |
|------------------|---------------------------------------------------|
| 多词文件名、枚举项 code   | snake_case                                        |
| 业务消息叶子 key       | camelCase                                         |
| validation 字段与约束 | camelCase（`validation.field.bizOrderNo.notBlank`） |
| 占位符              | `{0}`、`{1}`                                       |

Java 中通过 `I18nUtil.get(code, args)` 或 Bean Validation 的 `{code}` 解析；枚举展示使用 `I18nUtil.getEnumName(I18nSupport)`。

实现 `I18nSupport` 的枚举仅保留 `code` 字段，中文说明写在常量上方的 `///` 注释与 `enum/*.json` 中，**不要**再新增 `private final String name` 构造参数。

业务异常（`BizException` 及其子类）对外 API 应使用 messageKey，例如 `new BizInfoException(code, "pay.route.error.noMatch")` 或 `new BizException("error.common.xxx", arg0)`，由 `RestExceptionHandler` 统一解析。

新增翻译时请同时维护 `zh-CN` 与 `en-US`。

## Bean Validation 国际化

校验注解 `message` 必须使用 i18n key，格式：`{validation.field.{字段名}.{约束}}`（约束名：`notBlank` / `notNull` / `notEmpty` 等）。

防回归检查（在 `dax-pay-open` 仓库根目录执行）：

```bash
npm run check:validation-i18n
# 或
node scripts/check-validation-i18n.mjs

# 列出未配置 message 的校验注解（仅警告，供二期补齐）
npm run check:validation-i18n:warn
```
