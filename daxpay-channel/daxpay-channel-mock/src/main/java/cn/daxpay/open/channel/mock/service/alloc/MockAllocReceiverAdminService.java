package cn.daxpay.open.channel.mock.service.alloc;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.dao.alloc.MockAllocReceiverManager;
import cn.daxpay.open.channel.mock.entity.alloc.MockAllocReceiver;
import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverBindParam;
import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverCreateParam;
import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverQuery;
import cn.daxpay.open.channel.mock.result.alloc.MockAllocReceiverResult;
import cn.daxpay.open.channel.mock.service.MockAllocReceiverService;
import cn.daxpay.open.channel.mock.service.MockCredentialSupport;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.trade.alloc.AllocReceiverFailSupport;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # Mock 通道分账接收方台账服务(运营端)
///
/// 接收方档案 CRUD 与通道侧绑定编排, 形态对齐微信/支付宝/抖音的接收方报备:
/// 新增一步绑定(落库留痕 → 装配凭证 → 调通道子应用 → 回写状态),
/// 绑定失败记录保留(fail)可修正后重新绑定; 解绑保留记录(unbound)。
///
/// mock 零资料接入的差异点:
/// - 无应用维度(不选 appid), 产品由通道商户反解并固化到 [MockAllocReceiver#product];
/// - 接收方类型只开 MERCHANT_ID(商户号)与 USER_ID(用户ID)两类, 覆盖「商户间分账 + 个人账号分账」;
/// - 通道侧恒成功, 失败只可能来自凭证未配置/子应用不可达等基础设施异常, 仍走同一状态机留痕。
///
/// 事务边界: 各写方法不开事务, 落库与状态回写独立提交——失败留痕优先于原子性
/// (一步绑定模型无跨记录一致性需求, 事务包裹通道 HTTP 调用反而会把失败留痕整体回滚掉)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockAllocReceiverAdminService {

    private final MockAllocReceiverManager allocReceiverManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final MockCredentialSupport mockCredentialSupport;
    private final MockAllocReceiverService mockAllocReceiverService;

    /// 分页查询
    public PageResult<MockAllocReceiverResult> page(PageParam pageParam, MockAllocReceiverQuery query) {
        return MpUtil.toPageResult(allocReceiverManager.page(pageParam, query));
    }

    /// 新增并绑定接收方
    ///
    /// 绑定失败时记录保留(状态 fail + 失败原因), 并向调用方抛出失败异常以便即时提示。
    public void create(MockAllocReceiverCreateParam param) {
        // 通道商户存在性与归属校验(运营端按 mchNo + channelMchNo 双键定位)
        ChannelMerchant channelMerchant = channelMerchantManager
                .findByMchNoAndChannelMchNo(param.getMchNo(), param.getChannelMchNo())
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.CONFIG_NOT_EXIST,
                        "error.payment.channel.channelMerchantNotExist"));
        String product = channelMerchant.getProduct();
        // 防串用: 本端点只服务模拟通道双产品, 其它产品走各自通道的接收方管理
        if (!ProductEnum.MOCK_PAY.getCode().equals(product) && !ProductEnum.MOCK_ISV.getCode().equals(product)) {
            // 该通道商户不属于模拟通道, 无法在此维护接收方
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.mock.allocReceiverNotMockProduct");
        }
        // 参数校验(类型/名称约束)
        this.validateParam(param.getReceiverType(), param.getReceiverName());
        // 同通道商户同类型同账号查重(按明文哈希)
        String accountHash = SecureUtil.sha256(param.getReceiverAccount());
        if (allocReceiverManager.existsByChannelMchNoAndTypeAndHash(param.getChannelMchNo(),
                param.getReceiverType(), accountHash)) {
            // 分账接收方已存在
            throw new BizInfoException(CommonErrorCode.REPETITIVE_OPERATION_ERROR,
                    "error.channel.allocReceiverDuplicate");
        }
        MockAllocReceiver entity = new MockAllocReceiver()
                .setChannelMchNo(param.getChannelMchNo())
                .setProduct(product)
                .setReceiverType(param.getReceiverType())
                .setReceiverAccount(param.getReceiverAccount())
                .setAccountHash(accountHash)
                .setReceiverName(param.getReceiverName())
                .setAlias(StrUtil.trimToNull(param.getAlias()))
                .setStatus(AllocReceiverStatusEnum.FAIL.getCode());
        // 运营端不装载商户上下文, 必须显式赋值
        entity.setMchNo(param.getMchNo());
        allocReceiverManager.save(entity);
        this.doBind(entity);
    }

    /// 重新绑定(fail/unbound 状态)
    public void bind(MockAllocReceiverBindParam param) {
        MockAllocReceiver entity = this.loadAndCheck(param.getId());
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方无需重复绑定
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverAlreadyBound");
        }
        this.doBind(entity);
    }

    /// 解绑(bound 状态), 成功后保留记录置 unbound
    public void unbind(Long id) {
        MockAllocReceiver entity = this.loadAndCheck(id);
        if (!Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 仅已绑定的接收方可解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverNotBound");
        }
        try {
            MockSdkCredential credential = mockCredentialSupport.build(entity.getProduct(), entity.getChannelMchNo());
            mockAllocReceiverService.unbind(mockAllocReceiverService.buildReq(
                    entity.getReceiverAccount(), entity.getReceiverName(), credential));
        } catch (Exception e) {
            // 失败原因落库(保持 bound), HTTP 网络类异常标注"通道结果未知", 异常上抛提示
            log.warn("模拟通道接收方解绑失败: id={}, account={}", entity.getId(), entity.getReceiverAccount(), e);
            entity.setErrorMsg(AllocReceiverFailSupport.recordMessage(e));
            allocReceiverManager.updateById(entity);
            throw AllocReceiverFailSupport.toUserException(e, AllocReceiverFailSupport.KEY_UNBIND_UNKNOWN);
        }
        entity.setStatus(AllocReceiverStatusEnum.UNBOUND.getCode())
                .setUnbindTime(OffsetDateTime.now())
                .setErrorMsg(null);
        allocReceiverManager.updateById(entity);
    }

    /// 删除(仅 fail/unbound, bound 必须先解绑)
    public void delete(Long id) {
        MockAllocReceiver entity = this.loadAndCheck(id);
        if (Objects.equals(entity.getStatus(), AllocReceiverStatusEnum.BOUND.getCode())) {
            // 已绑定的接收方不可删除, 请先解绑
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.allocReceiverBoundCannotDelete");
        }
        allocReceiverManager.deleteById(id);
    }

    /// 修改别名(纯本地字段, 不触发通道调用, 任意绑定状态均可改; 留空即清空)
    public void updateAlias(Long id, String alias) {
        MockAllocReceiver entity = this.loadAndCheck(id);
        entity.setAlias(StrUtil.trimToNull(alias));
        allocReceiverManager.updateById(entity);
    }

    /// 执行通道侧绑定并回写状态
    private void doBind(MockAllocReceiver entity) {
        try {
            MockSdkCredential credential = mockCredentialSupport.build(entity.getProduct(), entity.getChannelMchNo());
            mockAllocReceiverService.bind(mockAllocReceiverService.buildReq(
                    entity.getReceiverAccount(), entity.getReceiverName(), credential));
        } catch (Exception e) {
            // 失败留痕(fail + 可读原因), HTTP 网络类异常标注"通道结果未知"
            log.warn("模拟通道接收方绑定失败: id={}, account={}", entity.getId(), entity.getReceiverAccount(), e);
            entity.setStatus(AllocReceiverStatusEnum.FAIL.getCode())
                    .setErrorMsg(AllocReceiverFailSupport.recordMessage(e));
            allocReceiverManager.updateById(entity);
            throw AllocReceiverFailSupport.toUserException(e, AllocReceiverFailSupport.KEY_BIND_UNKNOWN);
        }
        entity.setStatus(AllocReceiverStatusEnum.BOUND.getCode())
                .setBindTime(OffsetDateTime.now())
                .setUnbindTime(null)
                .setErrorMsg(null);
        allocReceiverManager.updateById(entity);
    }

    /// 加载记录
    private MockAllocReceiver loadAndCheck(Long id) {
        return allocReceiverManager.findById(id)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.DATA_NOT_EXIST,
                        "error.common.dataNotExist", id));
    }

    /// 模拟通道接收方参数约束校验
    private void validateParam(String receiverType, String receiverName) {
        // 类型合法性: mock 只开商户号与用户ID两类
        if (!Objects.equals(receiverType, AllocReceiverTypeEnum.MERCHANT_ID.getCode())
                && !Objects.equals(receiverType, AllocReceiverTypeEnum.USER_ID.getCode())) {
            // 模拟通道: 不支持的分账接收方类型
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.allocReceiverTypeInvalid", receiverType);
        }
        // 商户号类型必填商户全称(对齐微信/抖音口径, 便于对账时人工核对)
        if (Objects.equals(receiverType, AllocReceiverTypeEnum.MERCHANT_ID.getCode())
                && StrUtil.isBlank(receiverName)) {
            // 模拟通道: 商户号类型接收方必须填写商户全称
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.allocReceiverNameRequired");
        }
    }
}
