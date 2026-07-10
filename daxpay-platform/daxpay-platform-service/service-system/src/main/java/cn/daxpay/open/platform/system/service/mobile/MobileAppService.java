package cn.daxpay.open.platform.system.service.mobile;

import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.system.convert.mobile.MobileAppConvert;
import cn.daxpay.open.platform.system.dao.mobile.MobileAppManager;
import cn.daxpay.open.platform.system.entity.mobile.MobileApp;
import cn.daxpay.open.platform.system.param.mobile.MobileAppParam;
import cn.daxpay.open.platform.system.result.mobile.MobileAppResult;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/// # 移动端应用配置服务
///
/// 平台级配置, 按端类型(appType)+移动平台(platform)维度管理。
/// app_config/notify_config 经 [cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler] 加密入库;
/// 返回前端时对 app_config 内敏感键(appSecret/privateKey/alipayPublicKey/证书等)脱敏;
/// 保存时敏感键为空则保留库中原值(配合前端 diffForm)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MobileAppService {

    /// app_config 中需要脱敏/保护的敏感键
    /// 含支付宝小程序公钥字段与证书模式三本证书
    private static final Set<String> SENSITIVE_KEYS = Set.of(
            "appSecret", "privateKey", "alipayPublicKey", "clientSecret",
            "appCert", "alipayCert", "alipayRootCert");

    private final MobileAppManager manager;

    /// 查询全部(前端按端类型分组展示卡片)
    public List<MobileAppResult> findAll() {
        return manager.findAll().stream()
                .map(this::toMaskedResult)
                .toList();
    }

    /// 按端类型查询所有平台配置(端详情页Tab列表)
    public List<MobileAppResult> findAllByAppType(String appType) {
        return manager.findAllByField(MobileApp::getAppType, appType).stream()
                .map(this::toMaskedResult)
                .toList();
    }

    /// 查询单条
    public MobileAppResult findById(Long id) {
        // 通用: 移动端应用配置不存在
        MobileApp entity = manager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.mobile_app.notExist"));
        return toMaskedResult(entity);
    }

    /// 保存(按端类型+平台组合 upsert)
    @Transactional(rollbackFor = Exception.class)
    public MobileAppResult save(MobileAppParam param) {
        var existing = manager.lambdaQuery()
                .eq(MobileApp::getAppType, param.getAppType())
                .eq(MobileApp::getPlatform, param.getPlatform())
                .oneOpt();
        if (existing.isPresent()) {
            var entity = existing.get();
            // 敏感键为空时保留库中原值, 避免 diffForm 跳过字段后被清空
            param.setAppConfig(mergeSensitiveJson(entity.getAppConfig(), param.getAppConfig()));
            MobileAppConvert.CONVERT.copy(param, entity);
            manager.updateById(entity);
            return toMaskedResult(entity);
        }
        var entity = MobileAppConvert.CONVERT.toEntity(param);
        manager.save(entity);
        return toMaskedResult(entity);
    }

    /// 更新启用状态
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(Long id, Boolean enabled) {
        var entity = manager.findById(id)
                // 通用: 移动端应用配置不存在
                .orElseThrow(() -> new DataNotExistException("error.mobile_app.notExist"));
        entity.setEnabled(enabled);
        manager.updateById(entity);
    }

    /// 实体转结果并对 app_config 敏感键脱敏
    private MobileAppResult toMaskedResult(MobileApp entity) {
        MobileAppResult result = entity.toResult();
        result.setAppConfig(maskSensitiveJson(result.getAppConfig()));
        return result;
    }

    /// 对 JSON 文本中的敏感键做脱敏(password 风格), 非 JSON 或空串原样返回
    private String maskSensitiveJson(String json) {
        if (StrUtil.isBlank(json)) {
            return json;
        }
        try {
            Map<String, Object> map = JacksonUtil.toBean(json, new TypeReference<Map<String, Object>>() {});
            if (map == null || map.isEmpty()) {
                return json;
            }
            boolean changed = false;
            for (String key : SENSITIVE_KEYS) {
                Object val = map.get(key);
                if (val instanceof String s && StrUtil.isNotBlank(s)) {
                    map.put(key, DesensitizedUtil.password(s));
                    changed = true;
                }
            }
            return changed ? JacksonUtil.toJson(map) : json;
        } catch (Exception e) {
            log.warn("maskSensitiveJson parse failed, return raw: {}", e.getMessage());
            return json;
        }
    }

    /// 合并新旧 app_config: 新值中敏感键为空/null 时沿用旧值
    private String mergeSensitiveJson(String oldJson, String newJson) {
        if (StrUtil.isBlank(newJson)) {
            // 前端未传则整段保留旧值
            return oldJson;
        }
        if (StrUtil.isBlank(oldJson)) {
            return newJson;
        }
        try {
            Map<String, Object> oldMap = JacksonUtil.toBean(oldJson, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> newMap = JacksonUtil.toBean(newJson, new TypeReference<Map<String, Object>>() {});
            if (oldMap == null) {
                oldMap = new LinkedHashMap<>();
            }
            if (newMap == null) {
                return oldJson;
            }
            for (String key : SENSITIVE_KEYS) {
                Object newVal = newMap.get(key);
                if (newVal == null || (newVal instanceof String s && StrUtil.isBlank(s))) {
                    Object oldVal = oldMap.get(key);
                    if (oldVal != null) {
                        newMap.put(key, oldVal);
                    }
                }
            }
            return JacksonUtil.toJson(newMap);
        } catch (Exception e) {
            log.warn("mergeSensitiveJson parse failed, use newJson: {}", e.getMessage());
            return newJson;
        }
    }
}
