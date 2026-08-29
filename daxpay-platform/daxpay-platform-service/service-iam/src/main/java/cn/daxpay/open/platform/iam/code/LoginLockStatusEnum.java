package cn.daxpay.open.platform.iam.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 登录锁定状态
///
/// 登录失败与敏感操作验证失败共用同一计数器, 状态由 [cn.daxpay.open.platform.iam.service.session.LoginLockService] 依据
/// 锁定结束时间与当前时间计算; 纯服务端筛选/回传用枚举, 前端展示文案由各端自行维护, 不走 I18nSupport 字典。
@Getter
@RequiredArgsConstructor
public enum LoginLockStatusEnum {

    /// 锁定中: 锁定结束时间未到, 登录与敏感操作验证均被拒绝
    LOCKED("locked"),

    /// 已到期: 锁定结束时间已过, 下次登录/验证时自动清除残留记录
    EXPIRED("expired"),

    /// 计数中: 失败次数 1~N-1 次未达锁定阈值, 有被爆破预警价值
    COUNTING("counting");

    private final String code;
}
