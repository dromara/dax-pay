package cn.daxpay.open.payment.merchant.service.device;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.service.ChannelTerminalQuerySupport;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 通道终端台账查询（商户端，只读）
///
/// 通道侧终端的建档/报备/手动登记与系统终端绑定全部收口运营端, 商户端只做自身范围内的只读查询,
/// 用于核对通道侧终端号与绑定关系。商户号强制取自 [PaymentContext], 入参中的商户号一律覆盖。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchChannelTerminalService {

    private final ChannelTerminalQuerySupport channelTerminalQuerySupport;
    private final PaymentContext paymentContext;

    /// 分页查询(查询与系统终端回填复用内核共用逻辑, 此处只负责商户号隔离)
    public PageResult<ChannelTerminalResult> page(PageParam pageParam, ChannelTerminalQuery query) {
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            // 终端: 商户上下文未装载, 无法进行数据隔离操作
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        // 商户端强制覆盖商户号, 防越权查询其它商户的通道终端
        query.setMchNo(mchNo);
        return channelTerminalQuerySupport.page(pageParam, query);
    }
}
