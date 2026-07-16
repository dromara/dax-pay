package cn.daxpay.open.platform.capability.sensitiveword.service;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.capability.sensitiveword.engine.SensitiveTextNormalizer;
import cn.daxpay.open.platform.capability.sensitiveword.engine.SensitiveWordMatcher;
import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSceneEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSourceEnum;
import cn.daxpay.open.platform.capability.sensitiveword.policy.SensitiveWordPolicy;
import cn.daxpay.open.platform.common.request.context.RequestContextHolder;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/// # 敏感词运行时校验
///
/// 编程式入口；与 UI locale 无关。命中默认拒绝提交。
@Slf4j
@Service
@RequiredArgsConstructor
public class SensitiveWordCheckService {

    private final SensitiveWordPolicy sensitiveWordPolicy;
    private final SystemSensitiveWordService systemSensitiveWordService;
    private final SystemSensitiveWordHitService systemSensitiveWordHitService;
    private final SensitiveTextNormalizer sensitiveTextNormalizer;
    private final SensitiveWordMatcher sensitiveWordMatcher;

    /// 是否启用
    public boolean isEnabled() {
        return sensitiveWordPolicy.isEnabled();
    }

    /// 仅检测（不写审计、不抛错）
    public List<String> findHits(String text) {
        return findHits(text, false);
    }

    /// 仅检测
    public List<String> findHits(String text, boolean html) {
        if (!sensitiveWordPolicy.isEnabled() || StrUtil.isBlank(text)) {
            return Collections.emptyList();
        }
        ensureMatcher();
        String normalized = html
                ? sensitiveTextNormalizer.normalizeHtml(text)
                : sensitiveTextNormalizer.normalize(text);
        return sensitiveWordMatcher.findHits(normalized);
    }

    public boolean contains(String text) {
        return CollUtil.isNotEmpty(findHits(text));
    }

    /// 命中则写审计并抛错
    public void assertClean(String text, SensitiveWordSceneEnum scene) {
        assertClean(text, scene, false);
    }

    /// 批量，任一命中即抛
    public void assertClean(SensitiveWordSceneEnum scene, String... texts) {
        if (texts == null) {
            return;
        }
        for (String t : texts) {
            assertClean(t, scene, false);
        }
    }

    /// 命中则写审计并抛错
    public void assertClean(String text, SensitiveWordSceneEnum scene, boolean html) {
        if (!sensitiveWordPolicy.isEnabled() || StrUtil.isBlank(text)) {
            return;
        }
        List<String> hits = findHits(text, html);
        if (CollUtil.isEmpty(hits)) {
            return;
        }
        String hitWord = hits.getFirst();
        SensitiveWordSceneEnum sc = scene == null ? SensitiveWordSceneEnum.GENERAL : scene;
        if (sensitiveWordPolicy.isRecordHit()) {
            try {
                recordHit(text, hitWord, sc);
            } catch (Exception e) {
                log.warn("记录敏感词命中失败 word={}: {}", hitWord, e.getMessage());
            }
        }
        if (sensitiveWordPolicy.isRevealWord()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWordHit", hitWord);
        }
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                "error.common.sensitiveWord");
    }

    /// 管理端试检：返回命中列表，可选写审计
    public List<String> checkText(String text, boolean recordHit) {
        List<String> hits = findHits(text, false);
        if (recordHit && CollUtil.isNotEmpty(hits)) {
            try {
                recordHit(text, hits.getFirst(), SensitiveWordSceneEnum.MANUAL_CHECK);
            } catch (Exception e) {
                log.warn("试检写命中失败: {}", e.getMessage());
            }
        }
        return hits;
    }

    private void ensureMatcher() {
        List<String> words = systemSensitiveWordService.listEnabledWords();
        String version = systemSensitiveWordService.enabledVersion();
        // listEnabledWords 与 version 同缓存域，重建成本低
        sensitiveWordMatcher.rebuild(words, version);
    }

    private void recordHit(String originalText, String hitWord, SensitiveWordSceneEnum scene) {
        int maxLen = Math.max(1, sensitiveWordPolicy.contentPreviewMaxLen());
        String preview = StrUtil.maxLength(StrUtil.nullToEmpty(originalText), maxLen);
        Long wordId = systemSensitiveWordService.findEnabledWordId(hitWord);
        Long operatorId = null;
        try {
            operatorId = SecurityUtil.getUserIdOrDefaultId();
            if (operatorId != null && operatorId <= 0) {
                operatorId = null;
            }
        } catch (Exception ignored) {
            // 开放 API 无登录态
        }
        String clientIp = null;
        try {
            clientIp = WebServletUtil.getClientIp();
        } catch (Exception ignored) {
        }
        String path = null;
        try {
            path = RequestContextHolder.getRequestUri();
        } catch (Exception ignored) {
        }
        SystemSensitiveWordHit hit = new SystemSensitiveWordHit()
                .setWordId(wordId)
                .setHitWord(hitWord)
                .setContentPreview(preview)
                .setScene(scene.getCode())
                .setSource(resolveSource())
                .setOperatorId(operatorId)
                .setClientIp(clientIp)
                .setRequestPath(StrUtil.maxLength(path, 255));
        systemSensitiveWordHitService.record(hit);
    }

    private String resolveSource() {
        try {
            String path = RequestContextHolder.getRequestUri();
            if (path == null) {
                return SensitiveWordSourceEnum.UNKNOWN.getCode();
            }
            if (path.startsWith("/admin/")) {
                return SensitiveWordSourceEnum.ADMIN.getCode();
            }
            if (path.startsWith("/merchant/")) {
                return SensitiveWordSourceEnum.MERCHANT.getCode();
            }
            if (path.startsWith("/app-admin/")) {
                return SensitiveWordSourceEnum.APP_ADMIN.getCode();
            }
            if (path.startsWith("/unipay/") || path.startsWith("/client/")) {
                return SensitiveWordSourceEnum.UNIPAY.getCode();
            }
        } catch (Exception ignored) {
        }
        return SensitiveWordSourceEnum.UNKNOWN.getCode();
    }
}

