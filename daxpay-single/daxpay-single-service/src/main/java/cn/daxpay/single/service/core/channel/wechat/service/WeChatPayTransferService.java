package cn.daxpay.single.service.core.channel.wechat.service;

import cn.daxpay.single.core.exception.UnsupportedAbilityException;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信转账到零钱
 * @author xxm
 * @since 2024/6/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayTransferService {

    /**
     * 转账接口
     */
    @SneakyThrows
    public void transfer(TransferOrder order, WeChatPayConfig config) {
        throw new UnsupportedAbilityException("微信转账暂未实现");
    }
}
