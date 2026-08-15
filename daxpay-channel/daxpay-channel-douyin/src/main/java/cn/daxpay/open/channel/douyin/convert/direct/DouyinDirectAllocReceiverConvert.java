package cn.daxpay.open.channel.douyin.convert.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAllocReceiver;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAllocReceiverResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 抖音直连分账接收方转换
///
@Mapper
public interface DouyinDirectAllocReceiverConvert {

    DouyinDirectAllocReceiverConvert CONVERT = Mappers.getMapper(DouyinDirectAllocReceiverConvert.class);

    DouyinDirectAllocReceiverResult toResult(DouyinDirectAllocReceiver entity);
}
