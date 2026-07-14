package cn.daxpay.open.platform.common.translate.spi;

import cn.daxpay.open.platform.common.translate.model.DictItemData;

import java.util.Map;

/// # 字典翻译器 SPI
///
/// 由字典模块实现，翻译模块通过此接口获取字典映射数据
/// 当系统中不存在字典模块时，字典翻译功能自动跳过
public interface DictTranslator {

    /// 根据字典编码获取所有字典项的多语言数据
    /// 返回的 DictItemData 包含 i18nKey（优先）和 nameCn/nameEn（fallback）
    ///
    /// @param dictCode 字典编码
    /// @return code -> DictItemData 的映射
    Map<String, DictItemData> findByDictCode(String dictCode);
}

