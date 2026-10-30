package cn.daxpay.open.device.linkage.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户应用设备联动配置(极简版)
///
/// 应用级设备联动开关: 播报/打印/门店严格匹配三个布尔 + 打印版式代码, 一应用一行(行不存在=默认全开,
/// 存量商户行为零变化)。开源版不移植 plus 的联动规则表/线上静音托管/默认模板 ID
/// (拍板 3: 规则全砍, 线上单静音在消费侧按来源枚举一行判断, 见
/// [cn.daxpay.open.device.audio.consumer.DevicePaySuccessConsumer]; 拍板 2: 无模板表,
/// template_code 为内置版式常量代码 [cn.daxpay.open.device.print.template.PrintTemplateConstants])。
/// 播报开关关闭 = 该应用支付成功整条静默(不播不印); 打印开关关闭 = 消息不携带 printData(只播不印);
/// 门店严格匹配开启 = 门店订单只推绑定该门店的设备(未绑门店设备不再参与)。
///
/// 继承 [MchBaseEntity] 参与 mch_no 行级隔离(商户端免显式过滤); **运营端 insert 必须显式
/// setMchNo 单独赋值**(运营端不装载商户上下文, MetaObjectFill 无值会抛错), 消费线程读取走
/// [cn.daxpay.open.device.linkage.dao.MchAppDeviceConfigManager#findByAppIdNotTenant]。
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_mch_app_device_config")
public class MchAppDeviceConfig extends MchBaseEntity {

    /// 应用号(全局唯一, 一应用一行)
    private String appId;

    /// 是否播报(false=该应用支付成功不触发任何设备联动)
    private Boolean broadcastEnable;

    /// 是否允许打印(false=虚拟设备消息不附带 printData, 只播不印)
    private Boolean printEnable;

    /// 门店订单严格匹配设备(true=门店订单只推绑定该门店的设备, 未绑门店设备不再参与;
    /// false/空=未绑门店设备视为商户通用照常推送, 存量行为不变)
    private Boolean storeStrictMatch;

    /// 打印版式代码(内置版式常量, 首期 standard 标准小票; 空值消费侧回落 standard)
    private String templateCode;
}
