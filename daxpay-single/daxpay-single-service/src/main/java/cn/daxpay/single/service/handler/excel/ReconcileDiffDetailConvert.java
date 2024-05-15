package cn.daxpay.single.service.handler.excel;

import cn.daxpay.single.service.core.payment.reconcile.domain.ReconcileDiffDetail;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对账差异单转换
 * @author xxm
 * @since 2024/5/6
 */
public class ReconcileDiffDetailConvert implements Converter<List<ReconcileDiffDetail>> {

    public WriteCellData<?> convertToExcelData(List<ReconcileDiffDetail> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (CollUtil.isEmpty(value)){
            return new WriteCellData<>("");
        }
        // 转换为
        String collect = value.stream()
                .map(item -> item.getFieldName() + ":" + item.getLocalValue() + "<->" + item.getOutValue())
                .collect(Collectors.joining("|"));
        return new WriteCellData<>(collect);
    }
}
