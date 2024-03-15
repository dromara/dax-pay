package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.code.UnionPayRecordTypeEnum;
import cn.bootx.platform.daxpay.service.core.channel.union.dao.UnionPayRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayRecord;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.dto.channel.union.UnionPayRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.union.UnionPayRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 云闪付支付记录服务
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayRecordService {
    private final UnionPayRecordManager unionPayRecordManager;

    /**
     * 支付
     */
    public void pay(PayOrder order, PayChannelOrder channelOrder){
        UnionPayRecord unionPayRecord = new UnionPayRecord()
                .setType(UnionPayRecordTypeEnum.PAY.getCode())
                .setTitle(order.getTitle())
                .setOrderId(order.getId())
                .setAmount(channelOrder.getAmount())
                .setGatewayOrderNo(order.getGatewayOrderNo())
                .setGatewayTime(channelOrder.getPayTime());
        unionPayRecordManager.save(unionPayRecord);
    }

    /**
     * 退款
     */
    public void refund(RefundOrder order, RefundChannelOrder channelOrder){
        UnionPayRecord unionPayRecord = new UnionPayRecord()
                .setType(UnionPayRecordTypeEnum.REFUND.getCode())
                .setTitle(order.getTitle())
                .setOrderId(order.getId())
                .setAmount(channelOrder.getAmount())
                .setGatewayOrderNo(order.getGatewayOrderNo())
                .setGatewayTime(channelOrder.getRefundTime());
        unionPayRecordManager.save(unionPayRecord);
    }

    /**
     * 分页
     */
    public PageResult<UnionPayRecordDto> page(PageParam pageParam, UnionPayRecordQuery param){
        return MpUtil.convert2DtoPageResult(unionPayRecordManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public UnionPayRecordDto findById(Long id){
        return unionPayRecordManager.findById(id).map(UnionPayRecord::toDto).orElseThrow(DataNotExistException::new);
    }
}
