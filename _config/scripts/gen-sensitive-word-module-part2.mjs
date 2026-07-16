/**
 * 生成 capability-sensitive-word 其余 Java 源码
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

// convert / result / param / dao
w(
  `${jp}/result/SystemSensitiveWordResult.java`,
  `package ${pkg}.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 敏感词结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "敏感词结果")
public class SystemSensitiveWordResult extends BaseResult {

    @Schema(description = "敏感词")
    private String word;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "匹配模式")
    private String matchMode;

    @Schema(description = "处理级别")
    private String level;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
`,
)

w(
  `${jp}/result/SystemSensitiveWordHitResult.java`,
  `package ${pkg}.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 敏感词命中结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "敏感词命中结果")
public class SystemSensitiveWordHitResult extends BaseResult {

    @Schema(description = "词库ID")
    private Long wordId;

    @Schema(description = "命中词")
    private String hitWord;

    @Schema(description = "原文摘要")
    private String contentPreview;

    @Schema(description = "场景")
    private String scene;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "请求路径")
    private String requestPath;

    @Schema(description = "备注")
    private String remark;
}
`,
)

w(
  `${jp}/param/SystemSensitiveWordParam.java`,
  `package ${pkg}.param;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 敏感词参数
///
@Data
@Accessors(chain = true)
@Schema(title = "敏感词参数")
public class SystemSensitiveWordParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    @Schema(description = "敏感词")
    @NotBlank(message = "{validation.field.word.notBlank}", groups = ValidationGroup.add.class)
    @Size(max = 64, message = "{validation.field.word.size}")
    private String word;

    @Schema(description = "分类")
    @Size(max = 32, message = "{validation.field.category.size}")
    private String category;

    @Schema(description = "匹配模式")
    @Size(max = 16, message = "{validation.field.matchMode.size}")
    private String matchMode;

    @Schema(description = "状态")
    @Size(max = 16, message = "{validation.field.status.size}")
    private String status;

    @Schema(description = "备注")
    @Size(max = 255, message = "{validation.field.remark.size}")
    private String remark;
}
`,
)

w(
  `${jp}/param/SystemSensitiveWordQuery.java`,
  `package ${pkg}.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 敏感词查询
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Accessors(chain = true)
@Schema(title = "敏感词查询")
public class SystemSensitiveWordQuery {

    @Schema(description = "敏感词")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String word;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "匹配模式")
    private String matchMode;
}
`,
)

w(
  `${jp}/param/SystemSensitiveWordHitQuery.java`,
  `package ${pkg}.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 敏感词命中查询
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Accessors(chain = true)
@Schema(title = "敏感词命中查询")
public class SystemSensitiveWordHitQuery {

    @Schema(description = "命中词")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String hitWord;

    @Schema(description = "场景")
    private String scene;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "商户号")
    private String mchNo;
}
`,
)

w(
  `${jp}/param/SensitiveWordCheckTextParam.java`,
  `package ${pkg}.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 敏感词试检参数
///
@Data
@Accessors(chain = true)
@Schema(title = "敏感词试检参数")
public class SensitiveWordCheckTextParam {

    @Schema(description = "待检文本")
    @NotBlank(message = "{validation.field.content.notBlank}")
    @Size(max = 2000, message = "{validation.field.content.size}")
    private String text;

    @Schema(description = "是否写入命中审计")
    private Boolean recordHit;
}
`,
)

w(
  `${jp}/convert/SystemSensitiveWordConvert.java`,
  `package ${pkg}.convert;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWord;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordParam;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 敏感词转换
///
@Mapper
public interface SystemSensitiveWordConvert {
    SystemSensitiveWordConvert CONVERT = Mappers.getMapper(SystemSensitiveWordConvert.class);

    SystemSensitiveWordResult toResult(SystemSensitiveWord entity);

    SystemSensitiveWord toEntity(SystemSensitiveWordParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(SystemSensitiveWordParam param, @MappingTarget SystemSensitiveWord entity);
}
`,
)

w(
  `${jp}/convert/SystemSensitiveWordHitConvert.java`,
  `package ${pkg}.convert;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 敏感词命中转换
///
@Mapper
public interface SystemSensitiveWordHitConvert {
    SystemSensitiveWordHitConvert CONVERT = Mappers.getMapper(SystemSensitiveWordHitConvert.class);

    SystemSensitiveWordHitResult toResult(SystemSensitiveWordHit entity);
}
`,
)

w(
  `${jp}/dao/SystemSensitiveWordMapper.java`,
  `package ${pkg}.dao;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWord;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 敏感词 Mapper
///
@Mapper
public interface SystemSensitiveWordMapper extends MPJBaseMapper<SystemSensitiveWord> {
}
`,
)

w(
  `${jp}/dao/SystemSensitiveWordHitMapper.java`,
  `package ${pkg}.dao;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 敏感词命中 Mapper
///
@Mapper
public interface SystemSensitiveWordHitMapper extends MPJBaseMapper<SystemSensitiveWordHit> {
}
`,
)

w(
  `${jp}/dao/SystemSensitiveWordManager.java`,
  `package ${pkg}.dao;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWord;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordStatusEnum;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 敏感词 Manager
///
@Repository
public class SystemSensitiveWordManager extends BaseManager<SystemSensitiveWordMapper, SystemSensitiveWord> {

    /// 分页
    public Page<SystemSensitiveWord> page(PageParam pageParam, SystemSensitiveWordQuery query) {
        Page<SystemSensitiveWord> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<SystemSensitiveWord> wrapper = QueryGenerator.generator(query);
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// 词面是否已存在
    public boolean existsByWord(String word, Long excludeId) {
        if (StrUtil.isBlank(word)) {
            return false;
        }
        return lambdaQuery()
                .eq(SystemSensitiveWord::getWord, word.trim())
                .ne(excludeId != null, SystemSensitiveWord::getId, excludeId)
                .exists();
    }

    /// 全部启用词
    public List<SystemSensitiveWord> listEnabled() {
        return lambdaQuery()
                .eq(SystemSensitiveWord::getStatus, SensitiveWordStatusEnum.ENABLE.getCode())
                .list();
    }
}
`,
)

w(
  `${jp}/dao/SystemSensitiveWordHitManager.java`,
  `package ${pkg}.dao;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordHitQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 敏感词命中 Manager
///
@Repository
public class SystemSensitiveWordHitManager extends BaseManager<SystemSensitiveWordHitMapper, SystemSensitiveWordHit> {

    /// 分页
    public Page<SystemSensitiveWordHit> page(PageParam pageParam, SystemSensitiveWordHitQuery query) {
        Page<SystemSensitiveWordHit> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<SystemSensitiveWordHit> wrapper = QueryGenerator.generator(query);
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }
}
`,
)

// policy
w(
  `${jp}/policy/SensitiveWordPolicy.java`,
  `package ${pkg}.policy;

/// # 敏感词策略（总开关等）
///
/// 默认实现返回内置默认值；平台配置模块可提供覆盖 Bean。
public interface SensitiveWordPolicy {

    /// 是否启用过滤
    boolean isEnabled();

    /// 错误是否回显命中词
    boolean isRevealWord();

    /// 是否写入命中审计
    boolean isRecordHit();

    /// 原文摘要最大长度
    int contentPreviewMaxLen();

    /// 默认策略
    SensitiveWordPolicy DEFAULT = new SensitiveWordPolicy() {
        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public boolean isRevealWord() {
            return false;
        }

        @Override
        public boolean isRecordHit() {
            return true;
        }

        @Override
        public int contentPreviewMaxLen() {
            return 200;
        }
    };
}
`,
)

w(
  `${jp}/policy/DefaultSensitiveWordPolicy.java`,
  `package ${pkg}.policy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/// # 默认敏感词策略（无平台配置 Bean 时）
///
@Component
@ConditionalOnMissingBean(SensitiveWordPolicy.class)
public class DefaultSensitiveWordPolicy implements SensitiveWordPolicy {

    @Override
    public boolean isEnabled() {
        return DEFAULT.isEnabled();
    }

    @Override
    public boolean isRevealWord() {
        return DEFAULT.isRevealWord();
    }

    @Override
    public boolean isRecordHit() {
        return DEFAULT.isRecordHit();
    }

    @Override
    public int contentPreviewMaxLen() {
        return DEFAULT.contentPreviewMaxLen();
    }
}
`,
)

// engine
w(
  `${jp}/engine/SensitiveTextNormalizer.java`,
  `package ${pkg}.engine;

import cn.hutool.core.util.StrUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.springframework.stereotype.Component;

/// # 敏感词文本规范化（中文主场景）
///
/// 去零宽、全角转半角、繁体转简体；与 UI locale 无关。
@Component
public class SensitiveTextNormalizer {

    /// 零宽/不可见字符
    private static final String ZERO_WIDTH = "[\\\\u200B-\\\\u200D\\\\uFEFF\\\\u2060\\\\u00AD]";

    /// 规范化待检文本
    public String normalize(String text) {
        if (StrUtil.isBlank(text)) {
            return "";
        }
        String s = text.replaceAll(ZERO_WIDTH, "");
        s = StrUtil.trim(s);
        s = fullWidthToHalf(s);
        // 繁转简，词库约定简体录入
        s = ZhConverterUtil.toSimple(s);
        return s;
    }

    /// 富文本：剥 HTML 标签后再规范化
    public String normalizeHtml(String html) {
        if (StrUtil.isBlank(html)) {
            return "";
        }
        String plain = html.replaceAll("(?is)<script[^>]*>.*?</script>", " ")
                .replaceAll("(?is)<style[^>]*>.*?</style>", " ")
                .replaceAll("(?s)<[^>]+>", " ")
                .replace("&nbsp;", " ")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replaceAll("\\\\s+", " ");
        return normalize(plain);
    }

    private static String fullWidthToHalf(String input) {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == 12288) {
                chars[i] = ' ';
            } else if (c >= 65281 && c <= 65374) {
                chars[i] = (char) (c - 65248);
            }
        }
        return new String(chars);
    }
}
`,
)

w(
  `${jp}/engine/SensitiveWordMatcher.java`,
  `package ${pkg}.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.WordTree;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/// # 敏感词匹配器（Hutool WordTree / AC）
///
@Component
public class SensitiveWordMatcher {

    private final AtomicReference<WordTree> treeRef = new AtomicReference<>(new WordTree());
    private final AtomicReference<String> versionRef = new AtomicReference<>("");

    /// 按启用词列表重建（version 变化时）
    public void rebuild(List<String> words, String version) {
        String ver = StrUtil.nullToEmpty(version);
        if (ver.equals(versionRef.get()) && treeRef.get() != null && CollUtil.isNotEmpty(words)) {
            return;
        }
        WordTree tree = new WordTree();
        if (CollUtil.isNotEmpty(words)) {
            for (String w : words) {
                if (StrUtil.isNotBlank(w)) {
                    tree.addWord(w.trim());
                }
            }
        }
        treeRef.set(tree);
        versionRef.set(ver);
    }

    /// 查找全部命中（最长匹配）
    public List<String> findHits(String normalizedText) {
        if (StrUtil.isBlank(normalizedText)) {
            return Collections.emptyList();
        }
        WordTree tree = treeRef.get();
        if (tree == null) {
            return Collections.emptyList();
        }
        List<String> hits = tree.matchAll(normalizedText, -1, false, false);
        return hits == null ? Collections.emptyList() : hits;
    }

    public boolean contains(String normalizedText) {
        return CollUtil.isNotEmpty(findHits(normalizedText));
    }
}
`,
)

console.log('part2 done')
