package cn.daxpay.open.device.virtual.dao;

import cn.daxpay.open.device.virtual.entity.VirtualDevice;
import cn.daxpay.open.device.virtual.param.VirtualDeviceQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/// # 虚拟设备管理
///
/// 虚拟设备独立台账的唯一 DAO: 台账(分页/生成查重/归属维护) + 运行面(凭证/回执/在线回写)。
/// 本表恒为内置厂商 virtual, 无 vendor 过滤条件。
/// (开源版无代理商域: 无 assignAgent/recoverAgent; 监控大屏不在范围: 无离线候选/回执异常粗筛)
@Slf4j
@Repository
@RequiredArgsConstructor
public class VirtualDeviceManager extends BaseManager<VirtualDeviceMapper, VirtualDevice> {

    /// 分页
    public Page<VirtualDevice> page(PageParam pageParam, VirtualDeviceQuery query) {
        Page<VirtualDevice> mpPage = MpUtil.getMpPage(pageParam);
        return this.page(mpPage, QueryGenerator.generator(query));
    }

    /// 按主键集合分页(导出选中行专用: 与列表同数据源, 忽略筛选条件)
    ///
    /// mchNo 为空时不叠加商户条件(运营端); 非空时只返回该商户名下设备,
    /// 非本商户的勾选行在 SQL 层静默过滤(不报错、不泄露存在性)。
    public Page<VirtualDevice> pageByIds(PageParam pageParam, Collection<Long> ids, String mchNo) {
        Page<VirtualDevice> mpPage = MpUtil.getMpPage(pageParam);
        LambdaQueryWrapper<VirtualDevice> wrapper = Wrappers.<VirtualDevice>lambdaQuery()
                .in(VirtualDevice::getId, ids)
                .eq(StrUtil.isNotBlank(mchNo), VirtualDevice::getMchNo, mchNo)
                .orderByDesc(VirtualDevice::getId);
        return this.page(mpPage, wrapper);
    }

    /// 批次号是否已存在
    public boolean existedByBatchNo(String batchNo) {
        return existedByField(VirtualDevice::getBatchNo, batchNo);
    }

    /// 批量绑定商户与应用与门店
    public void bindMerchant(Collection<Long> ids, String mchNo, String appId, String storeNo) {
        lambdaUpdate()
                .set(VirtualDevice::getMchNo, mchNo)
                .set(VirtualDevice::getAppId, appId)
                .set(VirtualDevice::getStoreNo, storeNo)
                .in(VirtualDevice::getId, ids)
                .update();
    }

    /// 批量解绑商户(回平台库存, 保留设备档案; 清空应用/门店等使用配置)
    public void unbindMerchant(Collection<Long> ids) {
        lambdaUpdate()
                .set(VirtualDevice::getMchNo, null)
                .set(VirtualDevice::getAppId, null)
                .set(VirtualDevice::getStoreNo, null)
                .in(VirtualDevice::getId, ids)
                .update();
    }

    /// 批量绑定应用(须已绑商户)
    public void bindApp(Collection<Long> ids, String appId) {
        lambdaUpdate()
                .set(VirtualDevice::getAppId, appId)
                .in(VirtualDevice::getId, ids)
                .update();
    }

    /// 批量解绑应用(保留商户归属)
    public void unbindApp(Collection<Long> ids) {
        lambdaUpdate()
                .set(VirtualDevice::getAppId, null)
                .in(VirtualDevice::getId, ids)
                .update();
    }

    /// 按设备号查询(SN 激活入口)
    public Optional<VirtualDevice> findByDeviceNo(String deviceNo) {
        return lambdaQuery()
                .eq(VirtualDevice::getDeviceNo, deviceNo)
                .oneOpt();
    }

    /// 批量查询设备号是否已存在(号段生成查重, 未删除范围)
    public List<VirtualDevice> findByDeviceNos(Collection<String> deviceNos) {
        return lambdaQuery()
                .in(VirtualDevice::getDeviceNo, deviceNos)
                .list();
    }

    /// 查商户名下启用且已激活的虚拟设备(播报/打印目标设备集)
    ///
    /// 支付通知匹配语义: appId 非空只命中"设备 appId 为空或与支付应用相同"的设备;
    /// storeNo 非空命中**并集**——该店专属设备 + 未分配门店的商户通用设备
    /// (未绑门店的存量设备继续全店命中, 行为零变化; 换取连锁店精确播报不串播)。
    /// 未激活设备(secret 为空)无法解密推送, 直接排除。
    /// 本重载固定非严格匹配(storeStrict=false), 供不关心门店严格开关的查询方使用。
    public List<VirtualDevice> findAllEnabledByMchNo(String mchNo, String appId, String storeNo) {
        return findAllEnabledByMchNo(mchNo, appId, storeNo, false);
    }

    /// 查商户名下启用且已激活的虚拟设备(全参: storeStrict=门店严格匹配)
    ///
    /// storeStrict=true 且 storeNo 非空时只命中该店专属设备(未绑门店设备不参与,
    /// 应用级「门店订单严格匹配」开关联动设备定向); false 走门店并集语义(存量行为)。
    public List<VirtualDevice> findAllEnabledByMchNo(String mchNo, String appId, String storeNo, boolean storeStrict) {
        var query = lambdaQuery()
                .eq(VirtualDevice::getMchNo, mchNo)
                .eq(VirtualDevice::getEnable, true)
                .isNotNull(VirtualDevice::getSecret);
        if (StrUtil.isNotBlank(storeNo)) {
            if (storeStrict) {
                // 门店严格匹配: 仅该店专属设备, 未绑门店设备不再参与
                query.eq(VirtualDevice::getStoreNo, storeNo);
            } else {
                // 门店并集: 该店专属设备 + 未分配门店的商户通用设备
                query.and(wrapper -> wrapper
                        .eq(VirtualDevice::getStoreNo, storeNo)
                        .or()
                        .isNull(VirtualDevice::getStoreNo));
            }
        }
        if (appId != null) {
            query.and(wrapper -> wrapper
                    .isNull(VirtualDevice::getAppId)
                    .or()
                    .eq(VirtualDevice::getAppId, appId));
        }
        return query.list();
    }

    /// 按主键加载当前商户名下的虚拟设备(商户端读写统一入口)
    public Optional<VirtualDevice> findOwnedById(Long id, String mchNo) {
        return lambdaQuery()
                .eq(VirtualDevice::getId, id)
                .eq(VirtualDevice::getMchNo, mchNo)
                .oneOpt();
    }

    /// 按设备号加载当前商户名下的虚拟设备(商户端激活二维码入口)
    public Optional<VirtualDevice> findOwnedByDeviceNo(String deviceNo, String mchNo) {
        return lambdaQuery()
                .eq(VirtualDevice::getDeviceNo, deviceNo)
                .eq(VirtualDevice::getMchNo, mchNo)
                .oneOpt();
    }

    /// 商户调整使用应用(appId 为空=全部应用)
    public void changeApp(Long id, String appId) {
        lambdaUpdate()
                .set(VirtualDevice::getAppId, appId)
                .eq(VirtualDevice::getId, id)
                .update();
    }

    /// 商户调整使用门店(空=未分配, 不参与门店级匹配)
    public void changeStore(Long id, String storeNo) {
        lambdaUpdate()
                .set(VirtualDevice::getStoreNo, storeNo)
                .eq(VirtualDevice::getId, id)
                .update();
    }

    /// 批量绑定门店(运营端门店工作台快捷绑定, 单事务一次更新)
    public void changeStoreBatch(Collection<Long> ids, String storeNo) {
        lambdaUpdate()
                .set(VirtualDevice::getStoreNo, storeNo)
                .in(VirtualDevice::getId, ids)
                .update();
    }

    /// 商户启用/停用设备
    public void changeEnable(Long id, boolean enable) {
        lambdaUpdate()
                .set(VirtualDevice::getEnable, enable)
                .eq(VirtualDevice::getId, id)
                .update();
    }

    /// 轮换播报密钥(激活/重复激活: 旧密文与旧回执签名即时作废, 即换机语义; 同时刷新激活时间)
    public void rotateSecret(Long id, String secret) {
        lambdaUpdate()
                .set(VirtualDevice::getSecret, secret)
                .set(VirtualDevice::getActivatedTime, OffsetDateTime.now(ZoneOffset.UTC))
                .eq(VirtualDevice::getId, id)
                .update();
    }

    /// 清空播报凭证(解绑: 旧 APP 下一次心跳即 revoked, 且设备回到可删除状态; 激活时间一并清空回到未激活态)
    public void clearSecret(Long id) {
        lambdaUpdate()
                .set(VirtualDevice::getSecret, null)
                .set(VirtualDevice::getActivatedTime, null)
                .eq(VirtualDevice::getId, id)
                .update();
    }

    /// 回写最近一次收款播报回执(仅 cmd=pay.success 的 ack)
    public void updateLastPayAck(Long id, String msgId, Boolean ok) {
        lambdaUpdate()
                .set(VirtualDevice::getLastPayAckTime, OffsetDateTime.now(ZoneOffset.UTC))
                .set(VirtualDevice::getLastPayAckMsgId, msgId)
                .set(VirtualDevice::getLastPayAckOk, ok)
                .eq(VirtualDevice::getId, id)
                .update();
    }

    /// 回写最后在线时间(ack 心跳节流落库, 30 分钟精度, 非实时; 实时判定走 Redis)
    public void updateLastOnlineTime(Long id) {
        lambdaUpdate()
                .set(VirtualDevice::getLastOnlineTime, OffsetDateTime.now(ZoneOffset.UTC))
                .eq(VirtualDevice::getId, id)
                .update();
    }
}
