package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.convert.RefundOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundService;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSettleService;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSyncService;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 退款订单管理服务
///
/// 运营端专属。跨商户查询，按 mchNo 翻译商户名称。
@Service
@RequiredArgsConstructor
public class RefundOrderAdminService {

    private final RefundOrderManager refundOrderManager;
    private final RefundService refundService;
    private final RefundSyncService refundSyncService;
    private final RefundSettleService refundSettleService;
    private final TransService transService;

    /// 分页查询
    public PageResult<RefundOrderResult> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> page = refundOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(RefundOrderConvert.CONVERT::toResult)
                .toList();
        PageResult<RefundOrderResult> pageResult = new PageResult<RefundOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询
    public RefundOrderResult findById(Long id) {
        RefundOrder entity = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(entity);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 发起退款(运营端代发, mchNo 必传: 订单定位与幂等查重均按商户维度)
    public RefundOrderResult refund(RefundParam param) {
        if (StrUtil.isBlank(param.getMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "validation.field.mchNo.notBlank");
        }
        RefundOrder refundOrder = refundService.refund(param);
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(refundOrder);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 同步退款状态(传入退款单ID)
    public RefundOrderResult sync(Long id) {
        RefundOrder refundOrder = refundSyncService.syncById(id);
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(refundOrder);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 手动关闭异常退款单
    ///
    /// 仅允许处理 PROGRESS 且创建超过 7 天的退款单(定时同步任务自然淘汰后的滞留单,
    /// 余额永久预占无法自动恢复)。关闭前必须查通道确认未成功退款, 避免误关已退款导致双重支出。
    public RefundOrderResult manualClose(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        // 校验: 仅 PROGRESS 可手动关闭
        if (!Objects.equals(RefundOrderStatusEnum.PROGRESS.getCode(), refundOrder.getStatus())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.manualCloseStatus");
        }
        // 校验: 创建须超过 7 天(与定时同步淘汰窗口对齐)
        if (refundOrder.getCreateTime() == null
                || refundOrder.getCreateTime().isAfter(OffsetDateTime.now(ZoneOffset.UTC).minusDays(7))) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.manualCloseTooEarly");
        }
        // 关闭前查通道确认未成功退款, 避免误关已退款导致平台双重支出
        RefundResultBo channelResult = refundSyncService.queryChannel(refundOrder);
        if (!channelResult.isSyncSuccess() || channelResult.getStatus() == null) {
            // 通道查单失败, 无法确认状态, 拒绝关闭(可稍后重试)
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.closeChannelQueryFailed");
        }
        if (Objects.equals(channelResult.getStatus(), RefundOrderStatusEnum.SUCCESS)) {
            // 通道实际已退款成功: 改为结算成功(纠正)而非关闭
            refundSettleService.settleSuccess(
                    refundOrder.getId(), channelResult.getFinishTime(),
                    channelResult.getOutRefundNo(), channelResult.getRelationOrderNo());
            // 退款: 通道确认该退款已成功, 已自动纠正为成功, 无需关闭
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.alreadySuccessOnClose");
        }
        // 通道确认非成功(PROGRESS/FAIL/CLOSE), 可安全关闭
        // 结算关闭(自行加 trade 级锁, 回滚预占余额)
        refundSettleService.settleClose(
                refundOrder.getId(), OffsetDateTime.now(ZoneOffset.UTC),
                null, null, "运营手动关闭异常退款单");
        RefundOrder updated = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(updated);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }
}
