package cn.bootx.platform.daxpay.service.handler.excel;

import cn.bootx.platform.daxpay.service.code.ReconcileDiffTypeEnum;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 对账差异转换
 * @author xxm
 * @since 2024/5/6
 */
public class ReconcileDiffTypeConvert implements Converter<String> {
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null){
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(ReconcileDiffTypeEnum.findByCode(value).getName());
    }
}
