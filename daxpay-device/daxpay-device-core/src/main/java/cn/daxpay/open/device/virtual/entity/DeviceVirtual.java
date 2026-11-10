package cn.daxpay.open.device.virtual.entity;

import cn.daxpay.open.device.print.PrintTerminal;
import cn.daxpay.open.device.virtual.convert.VirtualDeviceConvert;
import cn.daxpay.open.device.virtual.result.VirtualDeviceResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

/// # 虚拟设备
///
/// 播报打印一体机(DaxPay 收款播报 APP): 台账/生成/激活/推送链路全自持于虚拟设备域。
/// 设备号(SN)为平台自造号段(VD+日期+随机段), 激活凭证为每设备 secret(AES-256-GCM)。
/// 归属模型(开源版无代理商域): 生成时入平台库存(mchNo 空), 绑定商户写 mchNo。
/// 实现 [PrintTerminal] 使小票渲染管线(内置版式渲染 ESC/POS)与设备台账共用同一契约。
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("device_virtual")
public class DeviceVirtual extends MpBaseEntity implements ToResult<VirtualDeviceResult>, PrintTerminal {

    /// 设备名称
    private String name;

    /// 设备号(平台自造 SN: VD+日期+随机段, 创建后不可改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String deviceNo;

    /// 批次号(生成时写入)
    private String batchNo;

    /// 所属商户号(绑定后写入, 空=未绑定)
    private String mchNo;

    /// 关联应用号(绑定应用后有值, 空=全部应用)
    private String appId;

    /// 使用门店号(空=未分配, 不参与门店级播报/打印匹配; 对应 MchStoreInfo#storeNo)
    private String storeNo;

    /// 是否启用
    private Boolean enable;

    /// 播报加密密钥(AES-256-GCM 32 字符, 激活时生成并轮换; 解绑时清空即回到未激活态)
    private String secret;

    /// 最近一次成功激活时间(激活轮换 secret 时写入, 解绑清空即回到未激活态; 未成功激活过为空)
    private OffsetDateTime activatedTime;

    /// 最近一次收款播报回执时间(仅 cmd=pay.success 的 ack 回写, 心跳不覆盖)
    private OffsetDateTime lastPayAckTime;

    /// 最近一次收款播报回执消息ID(与推送 msgId 对应, 排障定位用)
    private String lastPayAckMsgId;

    /// 最近一次收款播报回执结果(true=设备已完成播报)
    private Boolean lastPayAckOk;

    /// 设备最后在线时间(心跳 30 分钟节流落库, 非实时; 实时在线判定走 Redis, 见 VirtualOnlineTracker)
    private OffsetDateTime lastOnlineTime;

    /// 小票纸宽固定 58mm(虚拟设备不维护纸宽, 平台按 58mm 渲染主流 + 80mm 备流)
    @Override
    public Integer getPaperWidth() {
        return 58;
    }

    @Override
    public VirtualDeviceResult toResult() {
        return VirtualDeviceConvert.CONVERT.toResult(this);
    }
}
