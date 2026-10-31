package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/// # 付款码识别工具单元测试
///
/// 锁定识别层两级规则: 前两位白名单(微信 10–15 / 支付宝 25–30 / 银联 62) + 完整格式校验
/// (纯数字与精确长度), 以及显式传入分钱包 method 时与 authCode 的一致性校验。
/// 错误码统一为 [CommonErrorCode#VALIDATE_PARAMETERS_ERROR], 仅 messageKey 随失败原因分化。
class PayBarCodeUtilTest {

    /// 微信付款码: 10 开头 18 位
    private static final String WECHAT_CODE = "101234567890123456";

    /// 支付宝付款码: 28 开头 16 位(长度下边界)
    private static final String ALIPAY_CODE_MIN = "2812345678901234";

    /// 支付宝付款码: 28 开头 24 位(长度上边界)
    private static final String ALIPAY_CODE_MAX = "281234567890123456789012";

    /// 银联付款码: 62 开头 19 位
    private static final String UNION_CODE = "6212345678901234567";

    // ========== 合法码识别 ==========

    @Test
    @DisplayName("三通道合法付款码: 识别为对应付款码支付方式")
    void resolveMethod_validCodes_shouldResolve() {
        assertEquals(PayMethodEnum.WECHAT_BARCODE, PayBarCodeUtil.resolveMethod(WECHAT_CODE));
        assertEquals(PayMethodEnum.ALIPAY_BARCODE, PayBarCodeUtil.resolveMethod(ALIPAY_CODE_MIN));
        assertEquals(PayMethodEnum.ALIPAY_BARCODE, PayBarCodeUtil.resolveMethod(ALIPAY_CODE_MAX));
        assertEquals(PayMethodEnum.UNION_BARCODE, PayBarCodeUtil.resolveMethod(UNION_CODE));
        // 编码出口与枚举出口一致
        assertEquals(PayMethodEnum.WECHAT_BARCODE.getCode(), PayBarCodeUtil.resolveMethodCode(WECHAT_CODE));
        assertEquals(PayMethodEnum.ALIPAY_BARCODE.getCode(), PayBarCodeUtil.resolveMethodCode(ALIPAY_CODE_MIN));
        assertEquals(PayMethodEnum.UNION_BARCODE.getCode(), PayBarCodeUtil.resolveMethodCode(UNION_CODE));
    }

    // ========== 微信付款码长度 ==========

    @Test
    @DisplayName("微信付款码: 17/19 位长度不合规, 18 位合法")
    void resolveMethod_wechatLengthBoundary_shouldFail() {
        assertCodeInvalid("10123456789012345", "pay.error.barcode.wechatFormatInvalid");
        assertCodeInvalid("1012345678901234567", "pay.error.barcode.wechatFormatInvalid");
        assertEquals(PayMethodEnum.WECHAT_BARCODE, PayBarCodeUtil.resolveMethod(WECHAT_CODE));
    }

    // ========== 支付宝付款码长度 ==========

    @Test
    @DisplayName("支付宝付款码: 15/25 位长度不合规, 16/24 位合法")
    void resolveMethod_alipayLengthBoundary_shouldFail() {
        // 15 位(下边界外) / 25 位(上边界外)
        assertCodeInvalid("281234567890123", "pay.error.barcode.alipayFormatInvalid");
        assertCodeInvalid("2812345678901234567890123", "pay.error.barcode.alipayFormatInvalid");
        assertEquals(PayMethodEnum.ALIPAY_BARCODE, PayBarCodeUtil.resolveMethod(ALIPAY_CODE_MIN));
        assertEquals(PayMethodEnum.ALIPAY_BARCODE, PayBarCodeUtil.resolveMethod(ALIPAY_CODE_MAX));
    }

    // ========== 银联付款码长度 ==========

    @Test
    @DisplayName("银联付款码: 18/20 位长度不合规, 19 位合法")
    void resolveMethod_unionLengthBoundary_shouldFail() {
        assertCodeInvalid("621234567890123456", "pay.error.barcode.unionFormatInvalid");
        assertCodeInvalid("62123456789012345678", "pay.error.barcode.unionFormatInvalid");
        assertEquals(PayMethodEnum.UNION_BARCODE, PayBarCodeUtil.resolveMethod(UNION_CODE));
    }

    // ========== 纯数字校验 ==========

    @Test
    @DisplayName("非纯数字付款码: 三通道均按格式不合规拒绝")
    void resolveMethod_nonDigit_shouldFail() {
        // 长度合规但含字母/空白, 前缀命中后仍须纯数字
        assertCodeInvalid("10123456789012345a", "pay.error.barcode.wechatFormatInvalid");
        assertCodeInvalid("10123456789012345 ", "pay.error.barcode.wechatFormatInvalid");
        assertCodeInvalid("281234567890123a", "pay.error.barcode.alipayFormatInvalid");
        assertCodeInvalid("28123456789012345678901a", "pay.error.barcode.alipayFormatInvalid");
        assertCodeInvalid("621234567890123456a", "pay.error.barcode.unionFormatInvalid");
    }

    // ========== 空码与短码 ==========

    @Test
    @DisplayName("空码/null/长度不足2位: messageKey 保持「付款码无效」")
    void resolveMethod_blankOrTooShort_shouldFail() {
        assertCodeInvalid(null, "pay.error.barcode.invalid");
        assertCodeInvalid("", "pay.error.barcode.invalid");
        assertCodeInvalid("   ", "pay.error.barcode.invalid");
        // 长度 < 2: 无法取前两位前缀
        assertCodeInvalid("6", "pay.error.barcode.invalid");
        assertCodeInvalid("1", "pay.error.barcode.invalid");
    }

    // ========== 未知前缀 ==========

    @Test
    @DisplayName("未知前缀(24/16 等): messageKey 保持「不支持的付款码类型」")
    void resolveMethod_unknownPrefix_shouldFail() {
        assertCodeInvalid("2412345678901234", "pay.error.barcode.unsupportedType");
        assertCodeInvalid("1612345678901234", "pay.error.barcode.unsupportedType");
        // 前缀不在白名单时不进入格式校验, 非数字同样按未知类型拒绝
        assertCodeInvalid("9912345678901234", "pay.error.barcode.unsupportedType");
    }

    // ========== 显式 method 与付款码一致性 ==========

    @Test
    @DisplayName("显式 method 与付款码渠道一致: 校验通过")
    void validateMethodMatchesAuthCode_matched_shouldPass() {
        assertDoesNotThrow(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.WECHAT_BARCODE.getCode(), WECHAT_CODE));
        assertDoesNotThrow(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.ALIPAY_BARCODE.getCode(), ALIPAY_CODE_MIN));
        assertDoesNotThrow(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.UNION_BARCODE.getCode(), UNION_CODE));
    }

    @Test
    @DisplayName("显式 method 与付款码渠道不一致: messageKey 保持「付款码与支付方式不匹配」")
    void validateMethodMatchesAuthCode_mismatched_shouldFail() {
        assertMessage(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.WECHAT_BARCODE.getCode(), ALIPAY_CODE_MIN), "pay.error.barcode.methodMismatch");
        assertMessage(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.UNION_BARCODE.getCode(), WECHAT_CODE), "pay.error.barcode.methodMismatch");
        assertMessage(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.ALIPAY_BARCODE.getCode(), UNION_CODE), "pay.error.barcode.methodMismatch");
    }

    @Test
    @DisplayName("显式条码 method + 格式不合规码: 走同一格式校验并快速失败")
    void validateMethodMatchesAuthCode_barcodeWithBadFormat_shouldFail() {
        assertMessage(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.WECHAT_BARCODE.getCode(), "10123456789012345"), "pay.error.barcode.wechatFormatInvalid");
        assertMessage(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.UNION_BARCODE.getCode(), "6212345678901234"), "pay.error.barcode.unionFormatInvalid");
    }

    @Test
    @DisplayName("显式非条码 method: 不校验付款码直接放行")
    void validateMethodMatchesAuthCode_nonBarcodeMethod_shouldPass() {
        // 非条码方式(扫码/H5/APP)与付款码无语义关联, 即便码值格式不合规也放行
        assertDoesNotThrow(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.WECHAT_QR.getCode(), WECHAT_CODE));
        assertDoesNotThrow(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.WECHAT_QR.getCode(), "not-a-code"));
        // method/authCode 任一为空直接返回
        assertDoesNotThrow(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(null, WECHAT_CODE));
        assertDoesNotThrow(() -> PayBarCodeUtil.validateMethodMatchesAuthCode(
                PayMethodEnum.WECHAT_BARCODE.getCode(), null));
        assertDoesNotThrow(() -> PayBarCodeUtil.validateMethodMatchesAuthCode("", ""));
    }

    // ========== 断言辅助 ==========

    /// 断言付款码识别失败: 错误码固定为参数校验错误, messageKey 精确匹配
    private static void assertCodeInvalid(String authCode, String expectedMessageKey) {
        assertMessage(() -> PayBarCodeUtil.resolveMethod(authCode), expectedMessageKey);
    }

    /// 断言抛业务异常且错误码/messageKey 匹配
    private static void assertMessage(Executable executable, String expectedMessageKey) {
        BizInfoException ex = assertThrows(BizInfoException.class, executable);
        assertEquals(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, ex.getCode());
        assertEquals(expectedMessageKey, ex.getMessage());
    }
}
