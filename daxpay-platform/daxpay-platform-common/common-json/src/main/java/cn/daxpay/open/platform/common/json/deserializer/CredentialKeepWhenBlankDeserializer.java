package cn.daxpay.open.platform.common.json.deserializer;

import cn.hutool.core.util.StrUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/// # 凭据字段反序列化器: 空白字符串归一为 null
///
/// 用于密钥/证书等凭据材料的保存参数: 前端回显的是脱敏串, 用户留空提交时上送空串,
/// 归一为 null 后由更新侧 convert 的空值不覆盖策略(IGNORE)跳过该字段, 实现"留空保留原值";
/// 若不做归一, 空串会被当作有效值直接覆盖库中已配置的凭据。
public class CredentialKeepWhenBlankDeserializer extends ValueDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        String text = p.getString();
        return StrUtil.isBlank(text) ? null : text;
    }
}
