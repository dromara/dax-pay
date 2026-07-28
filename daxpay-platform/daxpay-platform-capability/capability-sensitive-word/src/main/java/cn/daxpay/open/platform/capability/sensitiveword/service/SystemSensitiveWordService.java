package cn.daxpay.open.platform.capability.sensitiveword.service;

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
            // 敏感词: 敏感词已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWordDuplicate");
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
                // 敏感词: 敏感词已存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.common.sensitiveWordDuplicate");
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
        return DigestUtil.md5Hex(String.join("\n", words));
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
                .orElseThrow(() -> new DataNotExistException("error.common.sensitiveWordNotFound"));
    }

    private void validateStatus(String status) {
        if (SensitiveWordStatusEnum.findByCode(status).isEmpty()) {
            // 敏感词: 状态无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWordStatusInvalid");
        }
    }

    private void validateMatchMode(String mode) {
        if (SensitiveWordMatchModeEnum.findByCode(mode).isEmpty()) {
            // 敏感词: 匹配模式无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWordMatchModeInvalid");
        }
    }

    private void validateCategory(String category) {
        if (SensitiveWordCategoryEnum.findByCode(category).isEmpty()) {
            // 敏感词: 分类无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.common.sensitiveWordCategoryInvalid");
        }
    }
}

