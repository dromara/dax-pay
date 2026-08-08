package cn.daxpay.open.payment.trade.transfer.util;

import cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum;
import cn.daxpay.open.payment.trade.transfer.result.AlipayTransferOrderResult;
import cn.daxpay.open.payment.trade.transfer.result.DouyinTransferOrderResult;
import cn.daxpay.open.payment.trade.transfer.result.WechatTransferOrderResult;
import cn.hutool.core.util.StrUtil;

/// # 收款人信息脱敏工具
///
/// 用于转账单查询/详情返回时对收款人姓名、收款账号进行展示脱敏(底层存储已加密)。
/// 仅作用于 Result 出口, 不影响实体上送通道(通道策略读到的仍是解密后的明文)。
///
/// 脱敏策略:
/// - **姓名**: 两字保留首字, 三字及以上保留首尾, 中间用 `*` 占位
/// - **收款账号**: 按 [TransferPayeeTypeEnum] 分级
///   - 手机号(`phone` / `login_name` 为手机号): `138****5678`(前3后4)
///   - 邮箱(`login_name` 为邮箱): `z***@example.com`(首字符 + 域名)
///   - `openid` / `user_id` / `open_id`: 伪匿名标识, 原样返回(运维排查需可见)
public final class PayeeDesensitizeUtil {

    private PayeeDesensitizeUtil() {
    }

    /// 姓名脱敏: 两字保留首字, 三字及以上保留首尾, 中间用 * 占位
    public static String maskName(String name) {
        if (StrUtil.isBlank(name)) {
            return name;
        }
        int len = name.length();
        if (len == 1) {
            return name;
        }
        if (len == 2) {
            // 张三 -> 张*
            return name.charAt(0) + "*";
        }
        // 张三丰 -> 张*丰; 诸葛亮 -> 诸**亮
        return name.charAt(0) + "*".repeat(len - 2) + name.charAt(len - 1);
    }

    /// 收款账号脱敏: 按收款人类型分级处理
    ///
    /// @param account   收款账号明文(实体层已由 [DataEncryptTypeHandler] 解密)
    /// @param payeeType 收款人账号类型, @see TransferPayeeTypeEnum
    public static String maskAccount(String account, String payeeType) {
        if (StrUtil.isBlank(account)) {
            return account;
        }
        // 仅对真个人信息(手机号/邮箱)脱敏; openid/user_id/open_id 为伪匿名标识, 原样返回
        if (!isSensitiveAccount(payeeType)) {
            return account;
        }
        // login_name 可能是手机号或邮箱, 按内容自动判断
        if (isEmail(account)) {
            return maskEmail(account);
        }
        if (isMobile(account)) {
            return maskMobile(account);
        }
        // 兜底: 保留首尾各1字符
        return maskBothEnds(account);
    }

    /// 判断该收款人类型的账号是否需要脱敏(手机号/邮箱为个人信息, 需脱敏)
    private static boolean isSensitiveAccount(String payeeType) {
        return TransferPayeeTypeEnum.LOGIN_NAME.getCode().equals(payeeType)
                || TransferPayeeTypeEnum.PHONE.getCode().equals(payeeType);
    }

    /// 简单邮箱判断: 包含 @ 且 @ 不在首尾
    private static boolean isEmail(String value) {
        int at = value.indexOf('@');
        return at > 0 && at < value.length() - 1;
    }

    /// 简单手机号判断: 11 位纯数字(国内)
    private static boolean isMobile(String value) {
        return value.length() == 11 && value.chars().allMatch(Character::isDigit);
    }

    /// 手机号脱敏: 138****5678(前3后4)
    private static String maskMobile(String mobile) {
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /// 邮箱脱敏: z***@example.com(本地名首字符 + 3星 + 域名)
    private static String maskEmail(String email) {
        int at = email.indexOf('@');
        String local = email.substring(0, at);
        String domain = email.substring(at);
        if (local.length() <= 1) {
            return "*" + domain;
        }
        return local.charAt(0) + "***" + domain;
    }

    /// 兜底脱敏: 保留首尾各1字符, 中间用 * 占位
    private static String maskBothEnds(String value) {
        if (value.length() <= 2) {
            return "*".repeat(value.length());
        }
        return value.charAt(0) + "*".repeat(value.length() - 2) + value.charAt(value.length() - 1);
    }

    // ===== Result 级脱敏入口(供 Admin/Merchant Service 出口统一调用) =====

    /// 支付宝转账单收款人脱敏: 姓名 + 收款账号(按类型)
    public static void desensitize(AlipayTransferOrderResult result) {
        if (result == null) {
            return;
        }
        result.setPayeeName(maskName(result.getPayeeName()));
        result.setPayeeAccount(maskAccount(result.getPayeeAccount(), result.getPayeeType()));
    }

    /// 抖音转账单收款人脱敏: 姓名 + 收款账号(按类型)
    public static void desensitize(DouyinTransferOrderResult result) {
        if (result == null) {
            return;
        }
        result.setPayeeName(maskName(result.getPayeeName()));
        result.setPayeeAccount(maskAccount(result.getPayeeAccount(), result.getPayeeType()));
    }

    /// 微信转账单收款人脱敏: 仅姓名(openid 为伪匿名标识, 不脱敏)
    public static void desensitize(WechatTransferOrderResult result) {
        if (result == null) {
            return;
        }
        result.setUserName(maskName(result.getUserName()));
    }
}
