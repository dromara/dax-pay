package cn.daxpay.single.service.handler.excel;

import cn.daxpay.single.core.code.ReconcileTradeEnum;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 对账交易订单转换
 * @author xxm
 * @since 2024/5/6
 */
public class ReconcileTradeConvert implements Converter<String> {

    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null){
            return new WriteCellData<>("");
        }
        ReconcileTradeEnum tradeEnum = ReconcileTradeEnum.findByCode(value);
        return new WriteCellData<>(tradeEnum.getName());
    }
}
