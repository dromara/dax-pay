package cn.daxpay.single.service.handler.excel;

import cn.daxpay.single.service.code.TradeTypeEnum;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 支付类型转换
 * @author xxm
 * @since 2024/5/6
 */
public class PaymentTypeConvert implements Converter<String> {
    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null){
            return new WriteCellData<>("");
        }
        TradeTypeEnum typeEnum = TradeTypeEnum.findByCode(value);
        return new WriteCellData<>(typeEnum.getName());
    }
}
