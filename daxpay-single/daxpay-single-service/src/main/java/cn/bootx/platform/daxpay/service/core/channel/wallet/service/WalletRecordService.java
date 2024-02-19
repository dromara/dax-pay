package cn.bootx.platform.daxpay.service.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.channel.wallet.dao.WalletRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.WalletRecord;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.wallet.WalletRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钱包记录服务类
 * @author xxm
 * @since 2024/2/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletRecordService {
    private final WalletRecordManager walletRecordManager;
    private final WalletQueryService walletQueryService;

    /**
     * 创建保存
     */
    public void create(Wallet wallet){
        WalletRecord walletRecord = new WalletRecord()
                .setAmount(wallet.getBalance())
                .setNewAmount(wallet.getBalance())
                .setOldAmount(0)
                .setWalletId(wallet.getId());
        walletRecordManager.save(walletRecord);
    }

    /**
     * 支付保存
     */
    public void pay(PayChannelOrder channelOrder, Wallet wallet){
        WalletRecord walletRecord = new WalletRecord()
                .setAmount(channelOrder.getAmount())
                .setNewAmount(wallet.getBalance())
                .setOldAmount(wallet.getBalance() - channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getPaymentId()))
                .setWalletId(wallet.getId());
        walletRecordManager.save(walletRecord);
    }

    /**
     * 退款保存
     */
    public void refund(PayRefundChannelOrder channelOrder, Wallet wallet){
        WalletRecord walletRecord = new WalletRecord()
                .setAmount(channelOrder.getAmount())
                .setNewAmount(wallet.getBalance())
                .setOldAmount(wallet.getBalance() + channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getRefundId()))
                .setWalletId(wallet.getId());
        walletRecordManager.save(walletRecord);
    }

    /**
     * 支付关闭
     */
    public void payClose(PayChannelOrder channelOrder, Wallet wallet){
        WalletRecord walletRecord = new WalletRecord()
                .setAmount(channelOrder.getAmount())
                .setNewAmount(wallet.getBalance())
                .setOldAmount(wallet.getBalance() - channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getPaymentId()))
                .setWalletId(wallet.getId());
        walletRecordManager.save(walletRecord);
    }

    /**
     * 退款关闭
     */
    public void refundClose(PayRefundChannelOrder channelOrder, Wallet wallet){
        WalletRecord walletRecord = new WalletRecord()
                .setAmount(channelOrder.getAmount())
                .setNewAmount(wallet.getBalance())
                .setOldAmount(wallet.getBalance() - channelOrder.getAmount())
                .setOrderId(String.valueOf(channelOrder.getRefundId()))
                .setWalletId(wallet.getId());
        walletRecordManager.save(walletRecord);
    }

    /**
     * 分页
     */
    public PageResult<WalletRecordDto> page(PageParam pageParam, WalletRecordQuery param) {
        return MpUtil.convert2DtoPageResult(walletRecordManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public WalletRecordDto findById(Long id){
        return walletRecordManager.findById(id).map(WalletRecord::toDto).orElseThrow(DataNotExistException::new);
    }

}
