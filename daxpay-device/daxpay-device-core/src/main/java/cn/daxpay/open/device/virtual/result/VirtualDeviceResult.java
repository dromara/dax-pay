package cn.daxpay.open.device.virtual.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

/// # 虚拟设备
///
/// (开源版无代理商域: 无 agentNo/agentName; 激活日志与监控大屏不在范围)
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "虚拟设备")
public class VirtualDeviceResult extends MchBaseResult {

    @Schema(description = "设备名称")
    private String name;

    @Schema(description = "设备号(平台自造 SN)")
    private String deviceNo;

    /// 设备厂商标识(开源版仅 virtual 一条产品线, 由 Convert 常量填充; 前端按此区分运行态列渲染)
    @Schema(description = "设备厂商(virtual=虚拟设备)")
    private String vendor;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "关联应用号")
    private String appId;

    /// 应用名称(由 appId 翻译, 空=全部应用)
    @Trans(entity = MchAppInfo.class, source = Fields.appId, result = MchAppInfo.Fields.appName)
    @Schema(description = "应用名称(空=全部应用)")
    private String appName;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "使用门店号(空=未分配门店)")
    private String storeNo;

    /// 门店名称(由 storeNo 翻译, 未分配为空)
    @Trans(entity = MchStoreInfo.class, source = Fields.storeNo, result = MchStoreInfo.Fields.storeName)
    @Schema(description = "使用门店名称(空=未分配门店)")
    private String storeName;

    @Schema(description = "最近一次收款播报回执时间")
    private OffsetDateTime lastPayAckTime;

    @Schema(description = "最近一次收款播报回执消息ID")
    private String lastPayAckMsgId;

    @Schema(description = "最近一次收款播报回执结果")
    private Boolean lastPayAckOk;

    /// 以下四项来自 Redis 试播/试打回执快照(TTL 1 小时, 非台账, 仅供排障窗口内比对)
    @Schema(description = "最近一次试播/试打回执时间(Redis 快照, 1 小时内有效)")
    private OffsetDateTime lastTestAckTime;

    @Schema(description = "最近一次试播/试打回执消息ID")
    private String lastTestAckMsgId;

    @Schema(description = "最近一次试播/试打指令(audio.test/print.ticket)")
    private String lastTestAckCmd;

    @Schema(description = "最近一次试播/试打回执结果")
    private Boolean lastTestAckOk;

    @Schema(description = "最后在线时间(Redis 优先, DB 节流值兜底)")
    private OffsetDateTime lastOnlineTime;

    @Schema(description = "当前是否在线(在线 ZSET score 距今 15 分钟内)")
    private Boolean online;

    @Schema(description = "是否已激活(secret 非空即已激活)")
    private Boolean activated;

    @Schema(description = "最近一次成功激活时间(未成功激活过为空)")
    private OffsetDateTime activatedTime;
}
