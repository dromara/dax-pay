package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.platform.capability.audit.log.dao.UnipayApiLogDbManager;
import cn.daxpay.open.platform.capability.audit.log.entity.UnipayApiLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.UnipayApiLogQuery;
import cn.daxpay.open.platform.capability.audit.log.result.UnipayApiLogResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.common.context.PaymentContext;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 统一支付接口调用日志(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；mchNo/mchName 对商户无意义不下发；
/// 请求/响应体在写入时已全量脱敏截断(见运营端 [cn.daxpay.open.platform.capability.audit.log.service.log.UnipayApiLogService])，可安全下发。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchUnipayApiLogService {

    private final PaymentContext paymentContext;
    private final UnipayApiLogDbManager unipayApiLogManager;

    /// 分页查询
    public PageResult<UnipayApiLogResult> page(PageParam pageParam, UnipayApiLogQuery query) {
        // 防止按他商户号过滤, 强制覆盖为当前商户
        query.setMchNo(requireMchNo());
        Page<UnipayApiLogDb> page = unipayApiLogManager.page(pageParam, query);
        PageResult<UnipayApiLogResult> pageResult = MpUtil.toPageResult(page);
        pageResult.getRecords().forEach(this::sanitizeForMerchant);
        return pageResult;
    }

    /// 详情
    public UnipayApiLogResult findById(Long id) {
        String mchNo = requireMchNo();
        UnipayApiLogResult result = unipayApiLogManager.findById(id)
                .map(UnipayApiLogDb::toResult)
                .orElseThrow(DataNotExistException::new);
        if (!mchNo.equals(result.getMchNo())) {
            // 非本商户数据按不存在处理, 防止越权探测
            throw new DataNotExistException();
        }
        return sanitizeForMerchant(result);
    }

    /// 商户端字段裁剪: mchNo/mchName 是本商户自身冗余, 不下发(最小披露原则)
    private UnipayApiLogResult sanitizeForMerchant(UnipayApiLogResult result) {
        result.setMchNo(null);
        result.setMchName(null);
        return result;
    }

    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (Objects.isNull(mchNo) || mchNo.isBlank()) {
            // 商户: 数据错误未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return mchNo;
    }
}
