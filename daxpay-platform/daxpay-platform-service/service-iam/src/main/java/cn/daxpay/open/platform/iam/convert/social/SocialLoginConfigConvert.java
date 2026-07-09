package cn.daxpay.open.platform.iam.convert.social;

import java.util.Collections;
import java.util.Map;

import cn.daxpay.open.platform.iam.entity.social.SocialLoginConfig;
import cn.daxpay.open.platform.iam.param.social.SocialLoginConfigParam;
import cn.daxpay.open.platform.iam.result.social.SocialLoginConfigResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 第三方平台登录配置转换
///
/// extra 字段在实体层为 jsonb 原始文本(String), 在 Param/Result 层为 Map,
/// 通过 default 类型转换方法桥接, MapStruct 会按参数方向自动套用.
///
@Mapper
public interface SocialLoginConfigConvert {

    SocialLoginConfigConvert CONVERT = Mappers.getMapper(SocialLoginConfigConvert.class);

    SocialLoginConfigResult toResult(SocialLoginConfig entity);

    SocialLoginConfig toEntity(SocialLoginConfigParam param);

    void copy(SocialLoginConfigParam param, @MappingTarget SocialLoginConfig entity);

    /// Map -> jsonb 原始文本(Param.extra -> Entity.extra)
    default String map(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        return JSONUtil.toJsonStr(map);
    }

    /// jsonb 原始文本 -> Map(Entity.extra -> Result.extra)
    default Map<String, String> map(String json) {
        if (StrUtil.isBlank(json)) {
            return Collections.emptyMap();
        }
        return JSONUtil.toBean(json, Map.class);
    }
}
