package cn.daxpay.open.platform.core.code;

/// # 权限编译期常量
///
/// 供 {@code @PermCode} 使用。完整码 = menuCode + ":" + code（见
/// {@link cn.daxpay.open.platform.core.util.PermCodeUtil}）。
///
/// ## 约定
///
/// | 要点 | 说明 |
/// |------|------|
/// | 形态 | 嵌套 interface，字段默认 `public static final`（对齐 {@link CommonCode}） |
/// | 叶子语义 | `MENU` = menuCode；`Action.*` / 资源专属 = 动作段 `code`；**不是**完整码 |
/// | 完整码 | 运行时由工具拼接；前端完整码见 `dax-pay-ui/.../constants/perm-codes.ts` |
/// | 域划分 | 对齐前端七域：merchant / channel / payment / develop / device / iam / system |
/// | name | 仅高复用资源抽中英文名（如跨通道 `Channel.Merchant`）；其余可在注解上写字面量 |
///
/// 前端对照：`dax-pay-ui/apps/daxpay-admin/src/constants/perm-codes.ts`（叶子为完整码 `menuCode:code`）。
public interface PermCodes {

    /// 通用动作码（对应 {@code @PermCode.code}）
    interface Action {
        /// 查看（列表/详情/下拉等只读）
        String VIEW = "view";
        /// 管理（增删改等写操作，标准粒度）
        String MANAGE = "manage";
        /// 发布（公告、协议等从草稿上线）
        String PUBLISH = "publish";
        /// 更新（仅更新配置、非完整 manage 场景）
        String UPDATE = "update";
        /// 状态变更（启用/禁用等）
        String STATUS = "status";
        /// 签名（开发调试签名等）
        String SIGN = "sign";
        /// 踢下线（在线用户）
        String KICKOUT = "kickout";
        /// 重置密码
        String RESET_PASSWORD = "reset_password";
        /// 分配角色
        String ASSIGN_ROLE = "assign_role";
        /// 重发（如微信消息通知失败重发）
        String RESEND = "resend";
        /// 测试（如通知配置测试发送）
        String TEST = "test";
    }

    /// 渠道域
    interface Channel {
        /// 通道商户 menuCode=channel:merchant（多通道 Controller 共用）
        interface Merchant {
            String MENU = "channel:merchant";
            String VIEW_NAME_CN = "通道商户查看";
            String VIEW_NAME_EN = "Channel Merchant View";
            String MANAGE_NAME_CN = "通道商户管理";
            String MANAGE_NAME_EN = "Channel Merchant Manage";
        }

        /// 支付宝直连应用
        interface AlipayApp {
            String MENU = "channel:alipay:app";
        }

        /// 微信直连应用
        interface WechatApp {
            String MENU = "channel:wechat:app";
        }

        /// 抖音直连应用
        interface DouyinApp {
            String MENU = "channel:douyin:app";
        }
    }

    /// 商户域
    interface Merchant {
        /// 商户主体
        interface Info {
            String MENU = "merchant:info";
        }

        /// 对接配置
        interface Credential {
            String MENU = "merchant:credential";
            /// 资源专属动作
            String CREDENTIAL_CONFIG_UPDATE = "credential_config_update";
        }

        /// 通知配置
        interface NotifyConfig {
            String MENU = "merchant:notify_config";
            /// 资源专属动作
            String NOTIFY_CONFIG_UPDATE = "notify_config_update";
        }

        /// 商户应用
        interface App {
            String MENU = "merchant:app";
        }

        /// 通道路由
        interface AppRoute {
            String MENU = "merchant:app:route";
        }

        /// 码牌和聚合支付配置
        interface GatewayAggregate {
            String MENU = "merchant:gateway-aggregate";
        }

        /// 收银台配置
        interface GatewayCashier {
            String MENU = "merchant:gateway-cashier";
        }

        /// 门店
        interface Store {
            String MENU = "merchant:store";
        }

        /// 微信域名验证文件（商户侧）
        interface WxDomainVerify {
            String MENU = "merchant:wx_verify";
        }
    }

    /// 支付核心域
    interface Payment {
        /// 支付宝服务商
        interface AlipayIsv {
            String MENU = "payment:alipay:isv";
        }

        /// 微信服务商
        interface WechatIsv {
            String MENU = "payment:wechat:isv";
        }

        /// 拉卡拉服务商
        interface Lakala {
            String MENU = "payment:lakala:isv";
        }

        /// 海科融通服务商
        interface Hkrt {
            String MENU = "payment:hkrt:isv";
        }

        /// 斗拱服务商
        interface Dougong {
            String MENU = "payment:dougong:isv";
        }

        /// 随行付服务商
        interface Vbill {
            String MENU = "payment:vbill:isv";
        }

        /// 富友服务商
        interface Fuyou {
            String MENU = "payment:fuyou:isv";
        }

        /// 乐刷服务商
        interface Leshua {
            String MENU = "payment:leshua:isv";
        }

        /// 河马付服务商
        interface Hmpay {
            String MENU = "payment:hmpay:isv";
        }

        /// 支付主数据
        interface Platform {
            interface Product {
                String MENU = "payment:platform:product";
            }

            interface Provider {
                String MENU = "payment:platform:provider";
            }

            interface PayChannel {
                String MENU = "payment:platform:pay_channel";
            }

            interface Capability {
                String MENU = "payment:platform:capability";
            }
        }

        /// 支付产品配置
        interface ProductConfig {
            String MENU = "payment:config:product_config";
        }

        /// 支付配置子域
        interface Config {
            /// 微信域名验证文件（平台侧）
            interface WxDomainVerify {
                String MENU = "payment:config:wx_verify";
            }
        }

        /// 普通支付业务订单
        interface Order {
            String MENU = "payment:order";
        }

        /// 网关支付订单
        interface GatewayOrder {
            String MENU = "payment:gateway-order";
        }

        /// 退款订单
        interface Refund {
            String MENU = "payment:refund";
        }

        /// 资金交易凭证
        interface Trade {
            String MENU = "payment:trade";
        }
    }

    /// IAM 身份与访问
    interface Iam {
        interface PermMenu {
            String MENU = "iam:perm:menu";
        }

        interface Role {
            String MENU = "iam:role";
        }

        interface UserManager {
            String MENU = "iam:user:manager";
        }

        interface OnlineUser {
            String MENU = "iam:online:user";
        }

        interface Social {
            String MENU = "iam:social:login-config";
        }
    }

    /// 系统域
    interface System {
        interface Dict {
            String MENU = "system:dict";
        }

        interface Log {
            interface Login {
                String MENU = "system:log:login";
            }

            interface Operate {
                String MENU = "system:log:operate";
            }
        }

        /// 公告通知
        interface Notify {
            String MENU = "system:notify:notice";
        }

        /// 微信消息通知
        interface WechatNotify {
            String MENU = "system:notify:wechat-config";
        }

        interface FilePlatform {
            String MENU = "system:file:platform";
        }

        interface PlatformConfig {
            String MENU = "system:platform_config";
        }

        interface OssConfig {
            String MENU = "system:oss_config";
        }

        interface SecurityConfig {
            String MENU = "system:security_config";
        }

        interface Protocol {
            String MENU = "system:protocol";
        }

        interface MobileApp {
            String MENU = "system:config:mobile_app";
        }
    }

    /// 开发调试
    interface Develop {
        interface Trade {
            String MENU = "develop:trade";
        }

        interface Sign {
            String MENU = "develop:sign";
        }

        interface Auth {
            String MENU = "develop:auth";
        }
    }

    /// 设备管理
    interface Device {
        interface QrCode {
            String MENU = "device:qrcode";
        }
    }
}
