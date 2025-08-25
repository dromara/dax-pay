package org.dromara.daxpay.service.common.code;

/**
 * OCR识别类型
 */
public interface OCRType {
    /**
     * 身份证类型
     */
    enum ID_CARD {
        // 正面
        ID_CARD_FRONT,
        // 反面
        ID_CARD_BACK;
    }

}
