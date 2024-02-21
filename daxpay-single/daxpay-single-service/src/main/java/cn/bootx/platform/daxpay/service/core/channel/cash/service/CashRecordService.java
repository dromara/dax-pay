package cn.bootx.platform.daxpay.service.core.channel.cash.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.code.CashRecordTypeEnum;
import cn.bootx.platform.daxpay.service.core.channel.cash.dao.CashRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.cash.entity.CashRecord;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.dto.channel.cash.CashRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.cash.CashRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 现金记录
 *
 * @author xxm
 * @since 2021/6/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashRecordService {
    private final CashRecordManager cashRecordManager;

    /**
     * 支付保存
     */
    public void pay(PayChannelOrder channelOrder, String title){
        CashRecord record = new CashRecord()
                .setTitle(title)
                .setType(CashRecordTypeEnum.PAY.getCode())
                .setAmount(channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getPaymentId()));
        cashRecordManager.save(record);
    }

    /**
     * 退款保存
     */
    public void refund(RefundChannelOrder channelOrder, String title){
        CashRecord record = new CashRecord()
                .setTitle(title)
                .setType(CashRecordTypeEnum.REFUND.getCode())
                .setAmount(channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getRefundId()));
        cashRecordManager.save(record);
    }

    /**
     * 支付关闭
     */
    public void payClose(PayChannelOrder channelOrder, String title){
        CashRecord record = new CashRecord()
                .setTitle(title)
                .setType(CashRecordTypeEnum.CLOSE_PAY.getCode())
                .setAmount(channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getPaymentId()));
        cashRecordManager.save(record);
    }


    /**
     * 分页查询
     */
    public PageResult<CashRecordDto> page(PageParam pageParam, CashRecordQuery param) {
        return MpUtil.convert2DtoPageResult(cashRecordManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public CashRecordDto findById(Long id){
        return cashRecordManager.findById(id).map(CashRecord::toDto).orElseGet(CashRecordDto::new);
    }
}
