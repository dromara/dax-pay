package org.dromara.daxpay.payment.channel.service.apply;

import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.exception.operation.OperationUnsupportedException;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.common.service.MerchantPaymentQueryService;
import org.dromara.daxpay.payment.common.service.MerchantPermissionService;
import org.dromara.daxpay.platform.core.enums.channel.OnbApplyStatusEnum;
import org.dromara.daxpay.payment.channel.bo.OnbMchApplyStatusBo;
import org.dromara.daxpay.payment.channel.dao.apply.OnbMchApplyManager;
import org.dromara.daxpay.payment.channel.dao.mch.ChannelMerchantManager;
import org.dromara.daxpay.payment.channel.entity.apply.OnbMchApply;
import org.dromara.daxpay.payment.channel.entity.mch.ChannelMerchant;
import org.dromara.daxpay.platform.core.enums.channel.OnbApplySourceEnum;
import org.dromara.daxpay.platform.core.enums.channel.ChannelMerchantSourceEnum;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyAuditParam;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyParam;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyQuery;
import org.dromara.daxpay.payment.channel.result.apply.OnbMchApplyResult;
import org.dromara.daxpay.payment.channel.strategy.AbsOnbMchApplyStrategy;
import org.dromara.daxpay.payment.channel.strategy.AbsChannelMerchantStrategy;
import org.dromara.daxpay.payment.channel.util.OnbStrategyFactory;
import org.dromara.daxpay.payment.pay.service.masterdata.channel.PayChannelMasterDataService;
import org.dromara.daxpay.payment.pay.result.masterdata.channel.PayChannelResult;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import org.dromara.daxpay.platform.core.code.CommonCode;


/// # 商户入驻申请服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class OnbMchApplyService {
    private final OnbMchApplyManager onbMchApplyManager;
    private final MerchantPaymentQueryService merchantPaymentQueryService;
    private final ChannelMerchantManager channelMchManager;
    private final ClientCodeService clientCodeService;
    private final OnbProfileService onbProfileService;
    private final MerchantPermissionService merchantPermissionService;
    private final PayChannelMasterDataService PayChannelMasterDataService;
    private final PaymentContext apiContext;

    /// 分页
    public PageResult<OnbMchApplyResult> page(PageParam pageParam, OnbMchApplyQuery query){
        return MpUtil.toPageResult(onbMchApplyManager.page(pageParam,query));
    }

    /// 查询申请详情
    public OnbMchApplyResult findById(Long id){
        return onbMchApplyManager.findSimpleById(id).map(OnbMchApply::toResult).orElseThrow(() -> {
            // 商户入驻详情不存在
            return new DataNotExistException("error.payment.channel.applyNotExist", new Object[0]);
        });
    }

    /// 根据商户号查询进件通道
    public List<LabelValue> dropdownByMchNo(String mchNo) {
        List<PayChannelResult> channelList = PayChannelMasterDataService.listAllByApply();
        // 商户权限过滤
        var availableChannel = merchantPermissionService.getAvailableChannel(mchNo);
        return channelList.stream()
                .filter(o -> availableChannel.contains(o.getCode()))
                .map(o -> new LabelValue(o.getName(), o.getCode()))
                .toList();
    }

    /// 查询通道支持的进件类型
    public List<LabelValue> dropdownByChannel(String channel){
        var strategy = OnbStrategyFactory.create(channel, AbsOnbMchApplyStrategy.class);
        return strategy.getApplyType()
                .stream()
                .map(applyType -> new LabelValue(I18nUtil.getEnumName(applyType), applyType.getCode()))
                .toList();
    }

    /// 删除
    public void delete(Long id){
        OnbMchApply mchApply = onbMchApplyManager.findById(id)
                .orElseThrow(() -> {
                    // 商户入驻详情不存在
                    return new DataNotExistException("error.payment.channel.applyNotExist", new Object[0]);
                });
        if (!OnbApplyStatusEnum.DRAFT.getCode().equals(mchApply.getStatus())){
            // 非草稿状态，不能删除
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.onlyDraftCanDelete");
        }
        onbMchApplyManager.deleteById(id);
    }

    /// 创建进件申请
    @Transactional(rollbackFor = Exception.class)
    public Long create(OnbMchApplyParam param){
        OnbMchApply mchApply = OnbMchApply.init(param);
        // 生成申请号
        mchApply.setStatus(OnbApplyStatusEnum.DRAFT.getCode());
        // 查询商户信息
        if (Objects.equals(clientCodeService.getClientCode(), ClientEnum.MERCHANT.getCode())){
            mchApply.setSource(OnbApplySourceEnum.MERCHANT.getCode());
            mchApply.setMchNo(apiContext.getTradeInfo().getMchNo());
        } else {
            mchApply.setSource(OnbApplySourceEnum.ADMIN.getCode());
        }
        var merchant = java.util.Optional.ofNullable(merchantPaymentQueryService.getMerchantByMchNo(mchApply.getMchNo()))
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.merchantInfoNotFound"));
        // 检查是否有申请通道的权限
        var availableChannel = merchantPermissionService.getAvailableChannel(merchant.getMchNo());
        if (!availableChannel.contains(param.getChannel())){
            // 商户没有该通道的权限，无法添加进件申请
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.merchantNoChannelPermApply");
        }

        // 创建申请单
        onbMchApplyManager.save(mchApply);
        // 初始化进件数据
        var strategy = OnbStrategyFactory.create(mchApply.getChannel(), AbsOnbMchApplyStrategy.class);
        strategy.initApplyData(mchApply, param);
        return mchApply.getId();
    }

    /// 提交申请
    public void submit(Long id, String sign){
        // 如果sign有值说明是H5请求, 进行签名校验
        var mchApply = this.getAndCheckApplyInfo(id, sign);
        // 提交申请
        this.submit(mchApply);
    }
    /// 提交申请
    private void submit(OnbMchApply mchApply){
        var strategy = OnbStrategyFactory.create(mchApply.getChannel(), AbsOnbMchApplyStrategy.class);
        // 完成状态不可以重新发起提交
        if (OnbApplyStatusEnum.PASS.getCode().equals(mchApply.getStatus())){
            // 进件已完成，无需重复提交
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.applyCompletedNoResubmit");
        }
        // 根据状态选择调用具体实现进行处理
        String status = mchApply.getStatus();
        if (!List.of(OnbApplyStatusEnum.DRAFT.getCode(),
                OnbApplyStatusEnum.REJECT.getCode(),
                OnbApplyStatusEnum.COMPLETION.getCode(),
                OnbApplyStatusEnum.ERROR.getCode(),
                OnbApplyStatusEnum.CLOSED.getCode()).contains(status)){
            // 当前进件申请单状态不允许提交
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.applyStatusCannotSubmit");
        }
        // 根据申请客户端进行处理
        if (List.of(ClientEnum.GATEWAY.getCode(),ClientEnum.MERCHANT.getCode()).contains(clientCodeService.getClientCode())){
            // 网关端和商户端提交需要预审状态
            mchApply.setStatus(OnbApplyStatusEnum.PRE_TRIAL.getCode())
                    .setErrorMsg(null);
        } else {
            // 代理和运营直接提交申请
            // 根据状态选择调用具体实现进行处理
            var resultBo = strategy.apply(mchApply);
            // 更新状态
            mchApply.setErrorMsg(resultBo.getErrorMsg())
                    .setStatus(resultBo.getStatus().getCode())
                    .setOutStatus(resultBo.getOutStatus());
        }
        mchApply.setLastSubmitTime(OffsetDateTime.now(ZoneOffset.UTC));
        onbMchApplyManager.updateById(mchApply);
    }

    /// 同步状态
    public void sync(Long id){
        OnbMchApply mchApply = onbMchApplyManager.findById(id).orElseThrow(() -> {
            // 商户入驻详情不存在
            return new DataNotExistException("error.payment.channel.applyNotExist", new Object[0]);
        });
        // 草稿、预审、预审拒绝不允许同步
        if (List.of(OnbApplyStatusEnum.DRAFT.getCode(), OnbApplyStatusEnum.PRE_TRIAL.getCode(), OnbApplyStatusEnum.PRE_TRIAL_REJECT.getCode(), OnbApplyStatusEnum.COMPLETION.getCode()).contains(mchApply.getStatus())){
            // 申请未提交，不可以同步
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.applyNotSubmittedCannotSync");
        }
        var strategy = OnbStrategyFactory.create(mchApply.getChannel(), AbsOnbMchApplyStrategy.class);
        // 调用具体实现进行处理
        OnbMchApplyStatusBo resultBo = strategy.queryResult(mchApply);
        // 更新状态
        mchApply.setErrorMsg(resultBo.getErrorMsg())
                .setStatus(resultBo.getStatus().getCode())
                .setOutStatus(resultBo.getOutStatus());
        onbMchApplyManager.updateById(mchApply);
    }

    /// 生成进件商户信息
    @Transactional(rollbackFor = Exception.class)
    public void genMchInfo(Long applyId) {
        // 只有审核通过的才允许生成进件商户信息
        OnbMchApply mchApply = onbMchApplyManager.findById(applyId).orElseThrow(() -> {
            // 商户入驻详情不存在
            return new DataNotExistException("error.payment.channel.applyNotExist", new Object[0]);
        });
        if (!OnbApplyStatusEnum.PASS.getCode().equals(mchApply.getStatus())){
            // 当前申请单状态不允许生成进件商户信息
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.applyStatusCannotGenMch");
        }
        // 生成通道商户基础数据并设置ID, 通道策略中设置通道商户号后保存
        var channelMch = new ChannelMerchant();
        channelMch.setApplyId(mchApply.getId())
                .setSource(ChannelMerchantSourceEnum.APPLY.getCode())
                .setMchNo(mchApply.getMchNo())
                
                .setId(IdUtil.getSnowflakeNextId());
        mchApply.setStatus(OnbApplyStatusEnum.GENERATED.getCode());
        // 调用策略处理通道商户处理后并保存
        var strategy = OnbStrategyFactory.create(mchApply.getChannel(), AbsChannelMerchantStrategy.class);
        strategy.mchApplyHandler(channelMch);
        // 处理通道商户后并保存
        channelMchManager.save(channelMch);
        onbMchApplyManager.updateById(mchApply);
    }

    /// 审核
    public void audit(OnbMchApplyAuditParam param){
        var mchApply = onbMchApplyManager.findById(param.getId()).orElseThrow(() -> {
            // 商户入驻详情不存在
            return new DataNotExistException("error.payment.channel.applyNotExist", new Object[0]);
        });
        if (!List.of(OnbApplyStatusEnum.PRE_TRIAL.getCode(), OnbApplyStatusEnum.COMPLETION.getCode())
                .contains(mchApply.getStatus())){
            // 当前申请单状态不允许审核
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.applyStatusCannotAudit");
        }

        // 审核是否通过
        if (param.getPass()){
            // 通过后进入数据补填阶段
            mchApply.setStatus(OnbApplyStatusEnum.COMPLETION.getCode());
        } else {
            mchApply.setStatus(OnbApplyStatusEnum.PRE_TRIAL_REJECT.getCode())
                    .setErrorMsg(param.getRejectReason());
        }
        onbMchApplyManager.updateById(mchApply);
    }

    /// 获取并检查申请信息, 如果是H5端请求则通过签名校验
    public OnbMchApply getAndCheckApplyInfo(Long applyId, String sign){
        var mchApply = onbMchApplyManager.findById(applyId)
                .orElseThrow(() -> {
                    // 商户进件申请不存在
                    return new DataNotExistException("error.payment.channel.applyNotExist", new Object[0]);
                });
        // 存在签名说明是H5端请求
        if (StrUtil.isNotBlank(sign)) {
            // 通过申请时间生成摘要
            var date = DateUtil.format(mchApply.getCreateTime().toLocalDateTime(), DatePattern.PURE_DATETIME_PATTERN);
            String dateSign = MD5.create().digestHex16(date);
            // 校验签名是否一致
            if (!Objects.equals(sign, dateSign)) {
                // 签名验证失败，非法请求
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.signVerifyFailIllegal");
            }
            // 判断状态
            if (!List.of(OnbApplyStatusEnum.DRAFT.getCode(), OnbApplyStatusEnum.PRE_TRIAL_REJECT.getCode(), OnbApplyStatusEnum.REJECT.getCode())
                    .contains(mchApply.getStatus())) {
                // 当前进件申请单状态不允许查询
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.applyStatusCannotQuery");
            }
        }
        return mchApply;
    }

}
