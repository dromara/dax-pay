package cn.daxpay.open.platform.capability.sensitiveword.policy;

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

