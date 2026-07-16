/**
 * 生成 capability-sensitive-word 后端骨架（一次性脚本）
 */
import fs from 'node:fs'
import path from 'node:path'

const base = path.resolve(
  'daxpay-platform/daxpay-platform-capability/capability-sensitive-word',
)

function w(rel, content) {
  const p = path.join(base, rel)
  fs.mkdirSync(path.dirname(p), { recursive: true })
  fs.writeFileSync(p, content.replace(/\r\n/g, '\n'), 'utf8')
  console.log('wrote', rel)
}

w(
  'pom.xml',
  `<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>cn.daxpay.open</groupId>
        <artifactId>daxpay-platform-capability</artifactId>
        <version>4.0.0-beta1</version>
    </parent>
    <artifactId>capability-sensitive-word</artifactId>
    <description>敏感词过滤（词库/命中审计/匹配引擎）</description>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.daxpay.open</groupId>
            <artifactId>common-mybatis-plus</artifactId>
            <version>\${project.version}</version>
        </dependency>
        <dependency>
            <groupId>cn.daxpay.open</groupId>
            <artifactId>capability-auth</artifactId>
            <version>\${project.version}</version>
        </dependency>
        <dependency>
            <groupId>cn.daxpay.open</groupId>
            <artifactId>capability-cache</artifactId>
            <version>\${project.version}</version>
        </dependency>
        <dependency>
            <groupId>cn.daxpay.open</groupId>
            <artifactId>common-i18n</artifactId>
            <version>\${project.version}</version>
        </dependency>
        <dependency>
            <groupId>cn.daxpay.open</groupId>
            <artifactId>common-spring</artifactId>
            <version>\${project.version}</version>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-dfa</artifactId>
            <version>\${hutool.version}</version>
        </dependency>
        <dependency>
            <groupId>com.github.houbb</groupId>
            <artifactId>opencc4j</artifactId>
            <version>1.8.1</version>
        </dependency>
    </dependencies>
</project>
`,
)

w(
  'src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports',
  'cn.daxpay.open.platform.capability.sensitiveword.SensitiveWordAutoConfiguration\n',
)

w(
  'src/main/java/cn/daxpay/open/platform/capability/sensitiveword/SensitiveWordAutoConfiguration.java',
  `package cn.daxpay.open.platform.capability.sensitiveword;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/// # 敏感词能力自动配置
///
@ComponentScan
@AutoConfiguration
public class SensitiveWordAutoConfiguration {
}
`,
)

// ---- enums ----
w(
  'src/main/java/cn/daxpay/open/platform/capability/sensitiveword/enums/SensitiveWordStatusEnum.java',
  `package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词状态
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordStatusEnum implements I18nSupport {

    /// 启用
    ENABLE("enable"),
    /// 禁用
    DISABLE("disable");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_status";
    }

    public static Optional<SensitiveWordStatusEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
`,
)

w(
  'src/main/java/cn/daxpay/open/platform/capability/sensitiveword/enums/SensitiveWordCategoryEnum.java',
  `package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词分类
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordCategoryEnum implements I18nSupport {

    /// 政治
    POLITIC("politic"),
    /// 色情
    PORN("porn"),
    /// 暴力
    VIOLENCE("violence"),
    /// 广告
    AD("ad"),
    /// 自定义
    CUSTOM("custom");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_category";
    }

    public static Optional<SensitiveWordCategoryEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
`,
)

w(
  'src/main/java/cn/daxpay/open/platform/capability/sensitiveword/enums/SensitiveWordMatchModeEnum.java',
  `package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词匹配模式
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordMatchModeEnum implements I18nSupport {

    /// 子串包含（AC）
    CONTAINS("contains"),
    /// 整词精确（预留）
    EXACT("exact");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_match_mode";
    }

    public static Optional<SensitiveWordMatchModeEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
`,
)

w(
  'src/main/java/cn/daxpay/open/platform/capability/sensitiveword/enums/SensitiveWordSceneEnum.java',
  `package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词校验场景
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordSceneEnum implements I18nSupport {

    /// 支付标题
    PAY_TITLE("pay_title"),
    /// 支付描述
    PAY_DESCRIPTION("pay_description"),
    /// 商品名称
    GOODS_NAME("goods_name"),
    /// 商品描述
    GOODS_DESCRIPTION("goods_description"),
    /// 商户名称
    MCH_NAME("mch_name"),
    /// 应用名称
    APP_NAME("app_name"),
    /// 门店名称
    STORE_NAME("store_name"),
    /// 用户显示名
    USER_NAME("user_name"),
    /// 码牌名称
    QR_NAME("qr_name"),
    /// 公告
    NOTICE("notice"),
    /// 协议
    PROTOCOL("protocol"),
    /// 管理端试检
    MANUAL_CHECK("manual_check"),
    /// 通用文本
    GENERAL("general");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_scene";
    }

    public static Optional<SensitiveWordSceneEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
`,
)

w(
  'src/main/java/cn/daxpay/open/platform/capability/sensitiveword/enums/SensitiveWordSourceEnum.java',
  `package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词请求来源
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordSourceEnum implements I18nSupport {

    /// 运营端
    ADMIN("admin"),
    /// 商户端
    MERCHANT("merchant"),
    /// 开放支付
    UNIPAY("unipay"),
    /// 管理端小程序
    APP_ADMIN("app_admin"),
    /// 未知
    UNKNOWN("unknown");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_source";
    }

    public static Optional<SensitiveWordSourceEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}
`,
)

// ---- entity ----
w(
  'src/main/java/cn/daxpay/open/platform/capability/sensitiveword/entity/SystemSensitiveWord.java',
  `package cn.daxpay.open.platform.capability.sensitiveword.entity;

import cn.daxpay.open.platform.capability.sensitiveword.convert.SystemSensitiveWordConvert;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordCategoryEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordMatchModeEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordStatusEnum;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 敏感词词库
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("system_sensitive_word")
public class SystemSensitiveWord extends MpBaseEntity implements ToResult<SystemSensitiveWordResult> {

    /// 敏感词原文（建议简体录入）
    private String word;

    /// 分类
    /// @see SensitiveWordCategoryEnum
    private String category;

    /// 匹配模式
    /// @see SensitiveWordMatchModeEnum
    private String matchMode;

    /// 处理级别（一期固定 reject）
    private String level;

    /// 状态
    /// @see SensitiveWordStatusEnum
    private String status;

    /// 备注
    private String remark;

    @Override
    public SystemSensitiveWordResult toResult() {
        return SystemSensitiveWordConvert.CONVERT.toResult(this);
    }
}
`,
)

w(
  'src/main/java/cn/daxpay/open/platform/capability/sensitiveword/entity/SystemSensitiveWordHit.java',
  `package cn.daxpay.open.platform.capability.sensitiveword.entity;

import cn.daxpay.open.platform.capability.sensitiveword.convert.SystemSensitiveWordHitConvert;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSceneEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSourceEnum;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 敏感词命中记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("system_sensitive_word_hit")
public class SystemSensitiveWordHit extends MpBaseEntity implements ToResult<SystemSensitiveWordHitResult> {

    /// 关联词库 ID（可空）
    private Long wordId;

    /// 命中词快照
    private String hitWord;

    /// 原文摘要
    private String contentPreview;

    /// 场景
    /// @see SensitiveWordSceneEnum
    private String scene;

    /// 来源
    /// @see SensitiveWordSourceEnum
    private String source;

    /// 商户号
    private String mchNo;

    /// 应用号
    private String appId;

    /// 操作人用户 ID
    private Long operatorId;

    /// 客户端 IP
    private String clientIp;

    /// 请求 path
    private String requestPath;

    /// 备注
    private String remark;

    @Override
    public SystemSensitiveWordHitResult toResult() {
        return SystemSensitiveWordHitConvert.CONVERT.toResult(this);
    }
}
`,
)

console.log('part1 ok')
