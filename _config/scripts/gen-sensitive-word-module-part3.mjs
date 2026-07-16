/**
 * 生成 service / controller / validation
 */
import fs from 'node:fs'
import path from 'node:path'

const base = path.resolve(
  'daxpay-platform/daxpay-platform-capability/capability-sensitive-word',
)
const pkg = 'cn.daxpay.open.platform.capability.sensitiveword'
const jp = 'src/main/java/cn/daxpay/open/platform/capability/sensitiveword'

function w(rel, content) {
  const p = path.join(base, rel)
  fs.mkdirSync(path.dirname(p), { recursive: true })
  fs.writeFileSync(p, content.replace(/\r\n/g, '\n'), 'utf8')
  console.log('wrote', rel)
}

w(
  `${jp}/service/SystemSensitiveWordService.java`,
  `package ${pkg}.service;

import cn.daxpay.open.platform.capability.sensitiveword.convert.SystemSensitiveWordConvert;
import cn.daxpay.open.platform.capability.sensitiveword.dao.SystemSensitiveWordManager;
import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWord;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordCategoryEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordMatchModeEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordStatusEnum;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordParam;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordQuery;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/// # 敏感词词库服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSensitiveWordService {

    public static final String CACHE_NAME = "system:sensitive-word";

    private final SystemSensitiveWordManager systemSensitiveWordManager;

    /// 分页
    public PageResult<SystemSensitiveWordResult> page(PageParam pageParam, SystemSensitiveWordQuery query) {
        return MpUtil.toPageResult(systemSensitiveWordManager.page(pageParam, query));
    }

    /// 详情
    public SystemSensitiveWordResult findById(Long id) {
        return getEntity(id).toResult();
    }

    /// 新增
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public SystemSensitiveWordResult add(SystemSensitiveWordParam param) {
        String word = StrUtil.trim(param.getWord());
        if (systemSensitiveWordManager.existsByWord(word, null)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWord.duplicate");
        }
        SystemSensitiveWord entity = SystemSensitiveWordConvert.CONVERT.toEntity(param);
        entity.setWord(word);
        if (StrUtil.isBlank(entity.getStatus())) {
            entity.setStatus(SensitiveWordStatusEnum.ENABLE.getCode());
        } else {
            validateStatus(entity.getStatus());
        }
        if (StrUtil.isBlank(entity.getMatchMode())) {
            entity.setMatchMode(SensitiveWordMatchModeEnum.CONTAINS.getCode());
        } else {
            validateMatchMode(entity.getMatchMode());
        }
        if (StrUtil.isBlank(entity.getCategory())) {
            entity.setCategory(SensitiveWordCategoryEnum.CUSTOM.getCode());
        } else {
            validateCategory(entity.getCategory());
        }
        if (StrUtil.isBlank(entity.getLevel())) {
            entity.setLevel("reject");
        }
        systemSensitiveWordManager.save(entity);
        return entity.toResult();
    }

    /// 修改
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void update(SystemSensitiveWordParam param) {
        SystemSensitiveWord entity = getEntity(param.getId());
        String originWord = entity.getWord();
        SystemSensitiveWordConvert.CONVERT.copy(param, entity);
        // 词面创建后允许改，但需查重
        if (StrUtil.isNotBlank(param.getWord())) {
            String word = StrUtil.trim(param.getWord());
            if (systemSensitiveWordManager.existsByWord(word, entity.getId())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.common.sensitiveWord.duplicate");
            }
            entity.setWord(word);
        } else {
            entity.setWord(originWord);
        }
        if (StrUtil.isNotBlank(param.getStatus())) {
            validateStatus(param.getStatus());
        }
        if (StrUtil.isNotBlank(param.getMatchMode())) {
            validateMatchMode(param.getMatchMode());
        }
        if (StrUtil.isNotBlank(param.getCategory())) {
            validateCategory(param.getCategory());
        }
        systemSensitiveWordManager.updateById(entity);
    }

    /// 删除
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void delete(Long id) {
        getEntity(id);
        systemSensitiveWordManager.deleteById(id);
    }

    /// 词面是否存在
    public boolean existsByWord(String word, Long excludeId) {
        return systemSensitiveWordManager.existsByWord(StrUtil.trim(word), excludeId);
    }

    /// 启用词列表（缓存）
    @Cacheable(value = CACHE_NAME, key = "'enabled:list'")
    public List<String> listEnabledWords() {
        return systemSensitiveWordManager.listEnabled().stream()
                .map(SystemSensitiveWord::getWord)
                .filter(StrUtil::isNotBlank)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
    }

    /// 启用词版本指纹（用于本地 matcher 重建）
    @Cacheable(value = CACHE_NAME, key = "'enabled:version'")
    public String enabledVersion() {
        List<String> words = listEnabledWordsUncached();
        if (words.isEmpty()) {
            return "empty";
        }
        return DigestUtil.md5Hex(String.join("\\n", words));
    }

    /// 无缓存加载（重建 matcher 时与 list 一致）
    public List<String> listEnabledWordsUncached() {
        return systemSensitiveWordManager.listEnabled().stream()
                .map(SystemSensitiveWord::getWord)
                .filter(StrUtil::isNotBlank)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
    }

    /// 按词面找启用词 ID
    public Long findEnabledWordId(String word) {
        if (StrUtil.isBlank(word)) {
            return null;
        }
        return systemSensitiveWordManager.lambdaQuery()
                .eq(SystemSensitiveWord::getWord, word)
                .eq(SystemSensitiveWord::getStatus, SensitiveWordStatusEnum.ENABLE.getCode())
                .oneOpt()
                .map(SystemSensitiveWord::getId)
                .orElse(null);
    }

    private SystemSensitiveWord getEntity(Long id) {
        return systemSensitiveWordManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.common.sensitiveWord.notFound"));
    }

    private void validateStatus(String status) {
        if (SensitiveWordStatusEnum.findByCode(status).isEmpty()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWord.statusInvalid");
        }
    }

    private void validateMatchMode(String mode) {
        if (SensitiveWordMatchModeEnum.findByCode(mode).isEmpty()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWord.matchModeInvalid");
        }
    }

    private void validateCategory(String category) {
        if (SensitiveWordCategoryEnum.findByCode(category).isEmpty()) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWord.categoryInvalid");
        }
    }
}
`,
)

w(
  `${jp}/service/SystemSensitiveWordHitService.java`,
  `package ${pkg}.service;

import cn.daxpay.open.platform.capability.sensitiveword.dao.SystemSensitiveWordHitManager;
import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordHitQuery;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 敏感词命中记录服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSensitiveWordHitService {

    private final SystemSensitiveWordHitManager systemSensitiveWordHitManager;

    /// 分页
    public PageResult<SystemSensitiveWordHitResult> page(PageParam pageParam, SystemSensitiveWordHitQuery query) {
        return MpUtil.toPageResult(systemSensitiveWordHitManager.page(pageParam, query));
    }

    /// 详情
    public SystemSensitiveWordHitResult findById(Long id) {
        return systemSensitiveWordHitManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.common.sensitiveWord.hitNotFound"))
                .toResult();
    }

    /// 写入命中
    @Transactional(rollbackFor = Exception.class)
    public void record(SystemSensitiveWordHit hit) {
        systemSensitiveWordHitManager.save(hit);
    }
}
`,
)

w(
  `${jp}/service/SensitiveWordCheckService.java`,
  `package ${pkg}.service;

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
        String hitWord = hits.get(0);
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
                    "error.common.sensitiveWord.hit", hitWord);
        }
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                "error.common.sensitiveWord");
    }

    /// 管理端试检：返回命中列表，可选写审计
    public List<String> checkText(String text, boolean recordHit) {
        List<String> hits = findHits(text, false);
        if (recordHit && CollUtil.isNotEmpty(hits)) {
            try {
                recordHit(text, hits.get(0), SensitiveWordSceneEnum.MANUAL_CHECK);
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
            if (path.startsWith("/unipay/") || path.startsWith("/gateway/")) {
                return SensitiveWordSourceEnum.UNIPAY.getCode();
            }
        } catch (Exception ignored) {
        }
        return SensitiveWordSourceEnum.UNKNOWN.getCode();
    }
}
`,
)

w(
  `${jp}/validation/SensitiveWord.java`,
  `package ${pkg}.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// # 敏感词校验注解
///
/// 用于管理端/商户端 Param 自由文本字段；开放 API 请用 [SensitiveWordCheckService]。
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SensitiveWordValidator.class)
public @interface SensitiveWord {

    String message() default "{validation.field.sensitiveWord.rejected}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /// 是否按 HTML 剥离后再检
    boolean html() default false;
}
`,
)

w(
  `${jp}/validation/SensitiveWordValidator.java`,
  `package ${pkg}.validation;

import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSceneEnum;
import cn.daxpay.open.platform.capability.sensitiveword.service.SensitiveWordCheckService;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/// # 敏感词 Bean Validation
///
@Component
@RequiredArgsConstructor
public class SensitiveWordValidator implements ConstraintValidator<SensitiveWord, String> {

    private final SensitiveWordCheckService sensitiveWordCheckService;

    private boolean html;

    @Override
    public void initialize(SensitiveWord annotation) {
        this.html = annotation.html();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return true;
        }
        if (!sensitiveWordCheckService.isEnabled()) {
            return true;
        }
        // 走 assertClean 以写命中审计；失败转校验错误
        try {
            sensitiveWordCheckService.assertClean(value, SensitiveWordSceneEnum.GENERAL, html);
            return true;
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage() == null
                            ? "{validation.field.sensitiveWord.rejected}"
                            : e.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }
}
`,
)

w(
  `${jp}/controller/admin/SystemSensitiveWordController.java`,
  `package ${pkg}.controller.admin;

import cn.daxpay.open.platform.capability.sensitiveword.param.SensitiveWordCheckTextParam;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordParam;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordQuery;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordResult;
import cn.daxpay.open.platform.capability.sensitiveword.service.SensitiveWordCheckService;
import cn.daxpay.open.platform.capability.sensitiveword.service.SystemSensitiveWordService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/// # 敏感词词库管理（运营端）
///
@PermCode(menuCode = PermCodes.System.SensitiveWord.MENU)
@Validated
@Tag(name = "敏感词词库")
@RestController
@RequestMapping("/admin/system/sensitive-word")
@RequiredArgsConstructor
public class SystemSensitiveWordController {

    private final SystemSensitiveWordService systemSensitiveWordService;
    private final SensitiveWordCheckService sensitiveWordCheckService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<SystemSensitiveWordResult>> page(PageParam pageParam, SystemSensitiveWordQuery query) {
        return Res.ok(systemSensitiveWordService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "详情")
    @GetMapping("/get-by-id")
    public Result<SystemSensitiveWordResult> getById(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(systemSensitiveWordService.findById(id));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result<SystemSensitiveWordResult> add(
            @RequestBody @Validated(ValidationGroup.add.class) SystemSensitiveWordParam param) {
        return Res.ok(systemSensitiveWordService.add(param));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(
            @RequestBody @Validated(ValidationGroup.edit.class) SystemSensitiveWordParam param) {
        systemSensitiveWordService.update(param);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<Void> delete(@NotNull(message = "{validation.field.id.notNull}") Long id) {
        systemSensitiveWordService.delete(id);
        return Res.ok();
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "词面是否存在")
    @GetMapping("/exists-by-word")
    public Result<Boolean> existsByWord(
            @RequestParam String word,
            @RequestParam(required = false) Long id) {
        return Res.ok(systemSensitiveWordService.existsByWord(word, id));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "试检文本")
    @PostMapping("/check-text")
    public Result<Map<String, Object>> checkText(@RequestBody @Validated SensitiveWordCheckTextParam param) {
        boolean record = Boolean.TRUE.equals(param.getRecordHit());
        List<String> hits = sensitiveWordCheckService.checkText(param.getText(), record);
        return Res.ok(Map.of("hits", hits, "hit", !hits.isEmpty()));
    }
}
`,
)

w(
  `${jp}/controller/admin/SystemSensitiveWordHitController.java`,
  `package ${pkg}.controller.admin;

import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordHitQuery;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import cn.daxpay.open.platform.capability.sensitiveword.service.SystemSensitiveWordHitService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 敏感词命中记录（运营端，只读）
///
@PermCode(menuCode = PermCodes.System.SensitiveWordHit.MENU)
@Validated
@Tag(name = "敏感词命中记录")
@RestController
@RequestMapping("/admin/system/sensitive-word-hit")
@RequiredArgsConstructor
public class SystemSensitiveWordHitController {

    private final SystemSensitiveWordHitService systemSensitiveWordHitService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult<SystemSensitiveWordHitResult>> page(
            PageParam pageParam, SystemSensitiveWordHitQuery query) {
        return Res.ok(systemSensitiveWordHitService.page(pageParam, query));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "详情")
    @GetMapping("/get-by-id")
    public Result<SystemSensitiveWordHitResult> getById(
            @NotNull(message = "{validation.field.id.notNull}") Long id) {
        return Res.ok(systemSensitiveWordHitService.findById(id));
    }
}
`,
)

console.log('part3 done')
