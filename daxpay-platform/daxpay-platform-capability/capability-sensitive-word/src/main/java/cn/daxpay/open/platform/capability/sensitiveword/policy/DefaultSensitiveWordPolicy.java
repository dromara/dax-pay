package cn.daxpay.open.platform.capability.sensitiveword.policy;

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

