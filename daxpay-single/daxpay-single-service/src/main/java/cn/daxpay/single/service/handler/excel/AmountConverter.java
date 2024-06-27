package cn.daxpay.single.service.handler.excel;

import cn.daxpay.single.core.util.PayUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;

/**
 * 金额分转元
 * @author xxm
 * @since 2024/5/6
 */
public class AmountConverter implements Converter<Integer> {

    /**
     * 将分转为元
     */
    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null){
            return new WriteCellData<>("");
        }
        BigDecimal divide = PayUtil.conversionAmount(value);
        return new WriteCellData<>(divide);
    }
}
