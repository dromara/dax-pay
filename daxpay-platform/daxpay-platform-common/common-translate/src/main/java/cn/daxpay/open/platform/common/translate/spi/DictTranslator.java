package cn.daxpay.open.platform.common.translate.spi;

import cn.daxpay.open.platform.common.translate.model.DictItemData;

import java.util.Map;

/// # 字典翻译器 SPI
///
/// 由字典模块实现，翻译模块通过此接口获取字典映射数据
/// 当系统中不存在字典模块时，字典翻译功能自动跳过
public interface DictTranslator {

    /// 根据字典编码获取所有字典项的多语言数据
    /// 返回的 DictItemData 中包含各语言名称，翻译引擎按 @Trans.result 提取对应语言值
    ///
    /// @param dictCode 字典编码
    /// @return code -> DictItemData 的映射（如 {"01": {nameCn="微信支付", nameEn="WeChat Pay"}}）
    Map<String, DictItemData> findByDictCode(String dictCode);
}


