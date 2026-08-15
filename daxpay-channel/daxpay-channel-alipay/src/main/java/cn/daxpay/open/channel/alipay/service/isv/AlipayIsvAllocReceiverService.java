package cn.daxpay.open.channel.alipay.service.isv;

import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayAllocReceiverReq;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvAllocReceiverManager;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAllocReceiver;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAllocReceiverBindParam;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAllocReceiverCreateParam;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAllocReceiverQuery;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAllocReceiverResult;
import cn.daxpay.open.channel.alipay.service.payment.alloc.AlipayAllocReceiverChannelService;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # 支付宝服务商分账接收方服务
///
/// 接收方档案 CRUD 与通道侧绑定编排:
/// 新增一步绑定(落库留痕 → 组装服务商凭证(app_auth_token 自动) → 调子应用 → 回写状态),
/// 绑定失败记录保留(fail)可修正后重新绑定; 解绑保留记录(unbound)。
///
/// 凭证组装全自动(子商户授权绑定决定服务商用哪个应用), 无需存应用字段;
/// 支付宝 out_request_no 用记录 id(重试同 id 幂等)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAllocReceiverService {

    private final AlipayIsvAllocReceiverManager allocReceiverManager;
    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;
    private final AlipayIsvConfigAssembler alipayIsvConfigAssembler;
    private final AlipayAllocReceiverChannelService alipayAllocReceiverChannelService;

    /// 分页查询
    public PageResult<AlipayIsvAllocReceiverResult> page(PageParam pageParam, AlipayIsvAllocReceiverQuery query) {
        return MpUtil.toPageResult(allocReceiverManager.page(pageParam, query));
    }

    /// 新增并绑定接收方
    ///
    /// 绑定失败时记录保留(状态 fail + 失败原因), 并向调用方抛出失败异常以便即时提示。
    @Transactional(rollbackFor = Exception.class)
    public void create(AlipayIsvAllocReceiverCreateParam param) {
        // 子商户授权绑定存在性与通道商户一致性校验(ISV 绑定按 mchNo 定位)
        AlipayIsvChannelMerchant isvMerchant = alipayIsvChannelMerchantManager.findByMchNo(param.getMchNo())
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.DATA_NOT_EXIST,
                        "error.channel.alipay.mchAppNotFound"));
        if (!Objects.equals(isvMerchant.getChannelMchNo(), param.getChannelMchNo())) {
            // 支付宝: 通道商户不存在或与商户号不匹配
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.alipay.channelMerchantMismatch");
        }
        // 类型合法性(USER_ID/LOGIN_NAME)
        if (!Objects.equals(param.getReceiverType(), AllocReceiverTypeEnum.USER_ID.getCode())
                && !Objects.equals(param.getReceiverType(), AllocReceiverTypeEnum.LOGIN_NAME.getCode())) {
            // 支付宝: 不支持的分账接收方类型
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.allocReceiverTypeInvalid", param.getReceiverType());
        }
        // 同通道商户同类型同账号查重(按明文哈希)
        String accountHash = SecureUtil.sha256(param.getReceiverAccount());
        if (allocReceiverManager.existsByChannelMchNoAndTypeAndHash(param.getChannelMchNo(),
                param.getReceiverType(), accountHash)) {
            // 分账接收方已存在
            throw new BizInfoException(CommonErrorCode.REPETITIVE_OPERATION_ERROR,
                    "error.channel.allocReceiverDuplicate");
        }
        AlipayIsvAllocReceiver entity = new AlipayIsvAllocReceiver()
                .setChannelMchNo(param.getChannelMchNo())
                .setReceiverType(param.getReceiverType())
                .setReceiverAccount(param.getReceiverAccount())
                .setAccountHash(accountHash)
                .setReceiverName(param.getReceiverName())
                .setStatus(AllocReceiverStatusEnum.FAIL.getCode());
        // 运营端不装载商户上下文, 必须显式赋值
        entity.setMchNo(param.getMchNo());
        allocReceiverManager.save(entity);
        this.doBind(entity);
    }

    /// 重新绑定(fail/unbound 状态)
    @Transactional(rollbackFor = Exception.class)
    public void bind(AlipayIsvAllocReceiverBindParam param) {
        AlipayIsvAllocReceiver entity = this.loadAndCheck(param.getId());
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方无需重复绑定
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverAlreadyBound");
        }
        this.doBind(entity);
    }

    /// 解绑(bound 状态), 成功后保留记录置 unbound
    @Transactional(rollbackFor = Exception.class)
    public void unbind(Long id) {
        AlipayIsvAllocReceiver entity = this.loadAndCheck(id);
        if (!Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 仅已绑定的接收方可解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverNotBound");
        }
        AlipaySdkCredential credential = alipayIsvConfigAssembler.buildConfig(entity.getMchNo());
        try {
            alipayAllocReceiverChannelService.unbind(this.buildReq(entity, credential));
        } catch (Exception e) {
            // 失败原因落库(保持 bound), 异常上抛提示
            entity.setErrorMsg(e.getMessage());
            allocReceiverManager.updateById(entity);
            throw e;
        }
        entity.setStatus(AllocReceiverStatusEnum.UNBOUND.getCode())
                .setUnbindTime(OffsetDateTime.now())
                .setErrorMsg(null);
        allocReceiverManager.updateById(entity);
    }

    /// 删除(仅 fail/unbound, bound 必须先解绑)
    public void delete(Long id) {
        AlipayIsvAllocReceiver entity = this.loadAndCheck(id);
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方不可删除, 请先解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverBoundCannotDelete");
        }
        allocReceiverManager.deleteById(id);
    }

    /// 执行通道侧绑定并回写状态
    private void doBind(AlipayIsvAllocReceiver entity) {
        AlipaySdkCredential credential = alipayIsvConfigAssembler.buildConfig(entity.getMchNo());
        try {
            alipayAllocReceiverChannelService.bind(this.buildReq(entity, credential));
        } catch (Exception e) {
            entity.setStatus(AllocReceiverStatusEnum.FAIL.getCode())
                    .setErrorMsg(e.getMessage());
            allocReceiverManager.updateById(entity);
            throw e;
        }
        entity.setStatus(AllocReceiverStatusEnum.BOUND.getCode())
                .setBindTime(OffsetDateTime.now())
                .setUnbindTime(null)
                .setErrorMsg(null);
        allocReceiverManager.updateById(entity);
    }

    /// 组装通道绑定请求(out_request_no 用记录 id, 重试同 id 幂等)
    private AlipayAllocReceiverReq buildReq(AlipayIsvAllocReceiver entity, AlipaySdkCredential credential) {
        return new AlipayAllocReceiverReq()
                .setOutRequestNo(String.valueOf(entity.getId()))
                .setReceiverType(entity.getReceiverType())
                .setReceiverAccount(entity.getReceiverAccount())
                .setReceiverName(entity.getReceiverName())
                .setCredential(credential);
    }

    /// 加载记录
    private AlipayIsvAllocReceiver loadAndCheck(Long id) {
        return allocReceiverManager.findById(id)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.DATA_NOT_EXIST,
                        "error.common.dataNotExist", id));
    }
}
