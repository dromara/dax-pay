package cn.daxpay.open.payment.masterdata.service.channel;

import cn.daxpay.open.payment.masterdata.dao.channel.PayChannelManager;
import cn.daxpay.open.payment.masterdata.entity.channel.PayChannel;
import cn.daxpay.open.payment.masterdata.param.channel.PayChannelQuery;
import cn.daxpay.open.payment.masterdata.result.channel.PayChannelResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 支付通道主数据
///
/// 通道指接入层分类（如微信通道、支付宝通道），与「支付渠道」不同。数据来自 `ChannelEnum` 与 `pay_channel` 表。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayChannelService {

    private final PayChannelManager payChannelManager;

    /// 分页查询支付通道
    public PageResult<PayChannelResult> page(PageParam pageParam, PayChannelQuery query, String nameKeyword) {
        PageResult<PayChannelResult> pageResult = MpUtil.toPageResult(payChannelManager.page(pageParam, query));
        pageResult.setRecords(pageResult.getRecords().stream()
                .map(this::fillChannelName)
                .filter(row -> matchNameKeyword(row, nameKeyword))
                .toList());
        return pageResult;
    }

    /// 库表无记录时回退到通道枚举定义
    public PayChannelResult findByCode(String code) {
        return payChannelManager.findByCode(code)
                .map(PayChannel::toResult)
                .map(this::fillChannelName)
                .orElseGet(() -> resolveFromEnum(code));
    }

    /// 库表无记录时从通道枚举构造结果
    private PayChannelResult resolveFromEnum(String code) {
        ChannelEnum enumVal = ChannelEnum.findByCode(code);
        if (enumVal == null) {
            throw new DataNotExistException("error.payment.channel.notExist");
        }
        return fillChannelName(toChannelResult(enumVal, Map.of()));
    }

    /// 支付通道下拉选项
    public List<LabelValue> dropdown() {
        return payChannelManager.listAllOrdered().stream()
                .map(PayChannel::toResult)
                .map(this::fillChannelName)
                .map(e -> new LabelValue(e.getName(), e.getCode()))
                .toList();
    }

    /// 返回全部支付通道
    public List<PayChannelResult> listAll() {
        return buildChannels();
    }

    /// 枚举与库表合并为通道列表
    private List<PayChannelResult> buildChannels() {
        Map<String, PayChannel> dbMap = payChannelManager.listAllOrdered().stream()
                .collect(Collectors.toMap(PayChannel::getCode, c -> c, (a, b) -> a));

        return Arrays.stream(ChannelEnum.values())
                .map(enumVal -> toChannelResult(enumVal, dbMap))
                .sorted(Comparator.comparing(PayChannelResult::getSortNo,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    /// 枚举与库表合并为单条通道结果
    private PayChannelResult toChannelResult(ChannelEnum enumVal, Map<String, PayChannel> dbMap) {
        PayChannelResult result = new PayChannelResult()
                .setCode(enumVal.getCode())
                .setName(resolveChannelName(enumVal.getCode()));

        PayChannel dbRow = dbMap.get(enumVal.getCode());
        if (dbRow != null) {
            result.setId(dbRow.getId());
            result.setSortNo(dbRow.getSortNo());
            result.setDescription(dbRow.getDescription());
            result.setIcon(dbRow.getIcon());
        } else {
            result.setId(null);
            result.setSortNo(0);
        }
        return result;
    }

    /// 补全通道 i18n 展示名
    private PayChannelResult fillChannelName(PayChannelResult result) {
        if (result == null || result.getCode() == null) {
            return result;
        }
        return result.setName(resolveChannelName(result.getCode()));
    }

    /// 按编码取通道展示名
    private String resolveChannelName(String code) {
        ChannelEnum channelEnum = ChannelEnum.findByCode(code);
        return channelEnum != null ? I18nUtil.getEnumName(channelEnum) : code;
    }

    /// 分页后在内存中按展示名过滤
    private boolean matchNameKeyword(PayChannelResult row, String nameKeyword) {
        if (StrUtil.isBlank(nameKeyword)) {
            return true;
        }
        String name = row.getName();
        return name != null && name.contains(nameKeyword);
    }

}
