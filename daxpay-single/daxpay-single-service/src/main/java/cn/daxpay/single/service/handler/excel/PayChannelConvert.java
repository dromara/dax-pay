package cn.daxpay.single.service.handler.excel;

import cn.daxpay.single.core.code.PayChannelEnum;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 支付通道转换
 * TODO 改为读取数据库
 * @author xxm
 * @since 2024/5/6
 */
public class PayChannelConvert implements Converter<String> {
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception{
        if (value == null){
            return new WriteCellData<>("");
        }
        PayChannelEnum typeEnum = PayChannelEnum.findByCode(value);
        return new WriteCellData<>(typeEnum.getName());
    }
}
