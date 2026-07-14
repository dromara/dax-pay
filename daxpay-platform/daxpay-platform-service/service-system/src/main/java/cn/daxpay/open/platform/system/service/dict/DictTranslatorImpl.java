package cn.daxpay.open.platform.system.service.dict;

import cn.daxpay.open.platform.common.translate.model.DictItemData;
import cn.daxpay.open.platform.common.translate.spi.DictTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import cn.daxpay.open.platform.system.entity.dict.DictItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// # 字典翻译器实现
///
/// 通过 DictItemService 获取字典数据，构建为 DictItemData 返回给翻译引擎
@Component
@RequiredArgsConstructor
public class DictTranslatorImpl implements DictTranslator {

    private final DictItemService dictItemService;

    @Override
    public Map<String, DictItemData> findByDictCode(String dictCode) {
        List<DictItem> items = dictItemService.findAllEnableByDictCode(dictCode);
        Map<String, DictItemData> result = new HashMap<>();
        for (DictItem item : items) {
            result.put(item.getCode(), new DictItemData(item.getI18nKey()));
        }
        return result;
    }
}
