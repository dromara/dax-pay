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
/// | 叶子语义 | `MENU` = menuCode；`Action.*` = 动作段；**不是**完整码 |
/// | 命名 | menu 段与 action 一律 **kebab-case**，字符集 `a-z0-9:-`，禁止 `_` |
/// | 写权限 | 仅 `manage`（含配置保存）；无全局 `update` |
/// | 完整码 | 运行时拼接；前端见 `dax-pay-ui/.../constants/perm-codes.ts` |
/// | 域 | merchant / channel / payment / **trade** / develop / device / iam / system |
/// | 通道相关 | 通道商户 `channel:merchant`；直连应用 `channel:app`；服务商 ISV `payment:isv`（均不按通道拆角色） |
/// | name | **≥2 个 Controller 共用** → 放资源节点 `VIEW/MANAGE_NAME_CN/EN`；**仅 1 个 Controller 但方法重复多次** → 放该 Controller 内 `static final`；偶发字面量即可。禁止把「查看/View」等过泛文案收成全局常量 |
///
/// 前端对照：`dax-pay-ui/apps/daxpay-admin/src/constants/perm-codes.ts`（叶子为完整码 `menuCode:code`）。
public interface PermCodes {

    /// 通用动作码（对应 {@code @PermCode.code}）
    interface Action {
        /// 查看（列表/详情/下拉等只读）
        String VIEW = "view";
        /// 管理（增删改、配置保存等写操作）
        String MANAGE = "manage";
        /// 发布（公告、协议等从草稿上线）
        String PUBLISH = "publish";
        /// 状态变更（启用/禁用等）
        String STATUS = "status";
        /// 签名（开发调试签名等）
        String SIGN = "sign";
        /// 踢下线（在线用户）
        String KICKOUT = "kickout";
        /// 重置密码
        String RESET_PASSWORD = "reset-password";
        /// 分配角色
        String ASSIGN_ROLE = "assign-role";
        /// 重发（如微信消息通知失败重发）
        String RESEND = "resend";
        /// 测试（如通知配置测试发送）
        String TEST = "test";
    }

    /// 渠道域（导航挂商户下；通道商户 C1 统一码）
    interface Channel {
        /// 通道商户 menuCode=channel:merchant（多通道 Controller 共用，不按 wechat/alipay 拆）
        interface Merchant {
            /// 菜单编码
            String MENU = "channel:merchant";
            /// 查看权限中文名（高复用，跨通道 Controller 共用）
            String VIEW_NAME_CN = "通道商户查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Channel Merchant View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "通道商户管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Channel Merchant Manage";
        }

        /// 通道应用（直连应用 + 通道侧子商户应用等，全通道共用）menuCode=channel:app
        interface App {
            /// 菜单编码
            String MENU = "channel:app";
            /// 查看权限中文名
            String VIEW_NAME_CN = "通道应用查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Channel App View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "通道应用管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Channel App Manage";
        }
    }

    /// 商户域
    interface Merchant {
        /// 商户主体 menuCode=merchant:info
        interface Info {
            /// 菜单编码
            String MENU = "merchant:info";
            /// 查看权限中文名（admin + app-admin 共用）
            String VIEW_NAME_CN = "商户查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Merchant View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "商户管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Merchant Manage";
        }

        /// 对接配置 menuCode=merchant:credential
        interface Credential {
            /// 菜单编码
            String MENU = "merchant:credential";
            /// 查看权限中文名
            String VIEW_NAME_CN = "对接配置查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Credential Config View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "对接配置管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Credential Config Manage";
        }

        /// 通知配置 menuCode=merchant:notify-config
        interface NotifyConfig {
            /// 菜单编码
            String MENU = "merchant:notify-config";
            /// 查看权限中文名
            String VIEW_NAME_CN = "通知配置查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Notify Config View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "通知配置管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Notify Config Manage";
        }

        /// 商户应用 menuCode=merchant:app
        interface App {
            /// 菜单编码
            String MENU = "merchant:app";
            /// 查看权限中文名
            String VIEW_NAME_CN = "应用查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "App View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "应用管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "App Manage";
        }

        /// 通道路由 menuCode=merchant:app:route
        interface AppRoute {
            /// 菜单编码
            String MENU = "merchant:app:route";
        }

        /// 应用配置工作台子页 menuCode=merchant:app:workbench（与应用列表消歧）
        interface AppWorkbench {
            /// 菜单编码
            String MENU = "merchant:app:workbench";
        }

        /// 码牌和聚合支付配置 menuCode=merchant:gateway-aggregate
        interface GatewayAggregate {
            /// 菜单编码
            String MENU = "merchant:gateway-aggregate";
            /// 查看权限中文名（与收银台配置共用文案）
            String VIEW_NAME_CN = "网关配置查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Gateway Config View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "网关配置管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Gateway Config Manage";
        }

        /// 收银台配置 menuCode=merchant:gateway-cashier
        interface GatewayCashier {
            /// 菜单编码
            String MENU = "merchant:gateway-cashier";
            /// 查看权限中文名
            String VIEW_NAME_CN = "网关配置查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Gateway Config View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "网关配置管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Gateway Config Manage";
        }

        /// 门店 menuCode=merchant:store
        interface Store {
            /// 菜单编码
            String MENU = "merchant:store";
            /// 查看权限中文名
            String VIEW_NAME_CN = "门店查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Store View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "门店管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Store Manage";
        }

        /// 微信域名验证文件（商户侧）menuCode=merchant:wx-verify
        interface WxDomainVerify {
            /// 菜单编码
            String MENU = "merchant:wx-verify";
            /// 查看权限中文名
            String VIEW_NAME_CN = "微信域名验证查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "WeChat Domain Verify View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "微信域名验证管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "WeChat Domain Verify Manage";
        }
    }

    /// 支付平台能力（主数据 + 配置 + ISV；不含交易单据）
    interface Payment {
        /// 服务商 ISV 配置（全通道共用）menuCode=payment:isv
        interface Isv {
            /// 菜单编码
            String MENU = "payment:isv";
            /// 查看权限中文名
            String VIEW_NAME_CN = "服务商配置查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "ISV Config View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "服务商配置管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "ISV Config Manage";
        }

        /// 支付主数据 menuCode=payment:platform:*
        interface Platform {
            /// 支付产品 menuCode=payment:platform:product
            interface Product {
                /// 菜单编码
                String MENU = "payment:platform:product";
                /// 查看权限中文名
                String VIEW_NAME_CN = "产品查看";
                /// 查看权限英文名
                String VIEW_NAME_EN = "Product View";
                /// 管理权限中文名
                String MANAGE_NAME_CN = "产品管理";
                /// 管理权限英文名
                String MANAGE_NAME_EN = "Product Manage";
            }

            /// 支付方式 menuCode=payment:platform:pay-method
            interface PayMethod {
                /// 菜单编码
                String MENU = "payment:platform:pay-method";
            }

            /// 支付渠道 menuCode=payment:platform:provider
            interface Provider {
                /// 菜单编码
                String MENU = "payment:platform:provider";
            }

            /// 支付通道 menuCode=payment:platform:pay-channel
            interface PayChannel {
                /// 菜单编码
                String MENU = "payment:platform:pay-channel";
            }

            /// 支付能力 menuCode=payment:platform:capability
            interface Capability {
                /// 菜单编码
                String MENU = "payment:platform:capability";
            }
        }

        /// 支付产品配置 menuCode=payment:config:product-config
        interface ProductConfig {
            /// 菜单编码
            String MENU = "payment:config:product-config";
            /// 查看权限中文名
            String VIEW_NAME_CN = "产品配置查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Product Config View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "产品配置管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Product Config Manage";
        }

        /// 支付配置子域
        interface Config {
            /// 微信域名验证文件（平台侧）menuCode=payment:config:wx-verify
            interface WxDomainVerify {
                /// 菜单编码
                String MENU = "payment:config:wx-verify";
            }
        }
    }

    /// 交易单据域（从 payment 拆出，对齐侧栏「交易管理」）
    interface Trade {
        /// 普通支付业务订单 menuCode=trade:order
        interface Order {
            /// 菜单编码
            String MENU = "trade:order";
            /// 查看权限中文名（与网关订单 Controller 共用文案）
            String VIEW_NAME_CN = "订单查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Order View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "订单管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Order Manage";
        }

        /// 网关支付订单 menuCode=trade:gateway-order
        interface GatewayOrder {
            /// 菜单编码
            String MENU = "trade:gateway-order";
            /// 查看权限中文名
            String VIEW_NAME_CN = "订单查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Order View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "订单管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Order Manage";
        }

        /// 退款订单 menuCode=trade:refund
        interface Refund {
            /// 菜单编码
            String MENU = "trade:refund";
        }

        /// 资金交易凭证 menuCode=trade:fund
        interface Fund {
            /// 菜单编码
            String MENU = "trade:fund";
        }
    }

    /// IAM 身份与访问（导航挂系统下，码域独立）
    interface Iam {
        /// 菜单管理 menuCode=iam:menu（原 iam:perm:menu）
        interface Menu {
            /// 菜单编码
            String MENU = "iam:menu";
            /// 查看权限中文名（菜单 + 权限码 Controller 共用）
            String VIEW_NAME_CN = "菜单查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Menu View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "菜单管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Menu Manage";
        }

        /// 角色管理 menuCode=iam:role
        interface Role {
            /// 菜单编码
            String MENU = "iam:role";
        }

        /// 用户管理 menuCode=iam:user（原 iam:user:manager）
        interface User {
            /// 菜单编码
            String MENU = "iam:user";
            /// 查看权限中文名（用户/角色分配/社交绑定等 Controller 共用）
            String VIEW_NAME_CN = "用户查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "User View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "用户管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "User Manage";
        }

        /// 在线用户 menuCode=iam:online（原 iam:online:user）
        interface Online {
            /// 菜单编码
            String MENU = "iam:online";
        }

        /// 三方社交登录配置 menuCode=iam:social（原 iam:social:login-config）
        interface Social {
            /// 菜单编码
            String MENU = "iam:social";
            /// 查看权限中文名（多端登录配置 Controller 共用）
            String VIEW_NAME_CN = "社交登录配置查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Social Login Config View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "社交登录配置管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Social Login Config Manage";
        }
    }

    /// 系统域
    interface System {
        /// 字典（含字典项）menuCode=system:dict
        interface Dict {
            /// 菜单编码
            String MENU = "system:dict";
            /// 查看权限中文名（字典 + 字典项 Controller 共用）
            String VIEW_NAME_CN = "字典查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Dict View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "字典管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Dict Manage";
        }

        /// 日志 menuCode=system:log:*
        interface Log {
            /// 登录日志 menuCode=system:log:login
            interface Login {
                /// 菜单编码
                String MENU = "system:log:login";
            }

            /// 操作日志 menuCode=system:log:operate
            interface Operate {
                /// 菜单编码
                String MENU = "system:log:operate";
            }
        }

        /// 公告通知 menuCode=system:notify:notice
        interface Notify {
            /// 菜单编码
            String MENU = "system:notify:notice";
        }

        /// 微信消息通知 menuCode=system:notify:wechat-config（配置 + 发送记录）
        interface WechatNotify {
            /// 菜单编码
            String MENU = "system:notify:wechat-config";
        }

        /// 存储文件 menuCode=system:file（原 system:file:platform）
        interface File {
            /// 菜单编码
            String MENU = "system:file";
        }

        /// 平台配置 menuCode=system:platform-config
        interface PlatformConfig {
            /// 菜单编码
            String MENU = "system:platform-config";
        }

        /// OSS 配置 menuCode=system:oss-config
        interface OssConfig {
            /// 菜单编码
            String MENU = "system:oss-config";
        }

        /// 安全配置 menuCode=system:security-config
        interface SecurityConfig {
            /// 菜单编码
            String MENU = "system:security-config";
        }

        /// 用户协议 menuCode=system:protocol
        interface Protocol {
            /// 菜单编码
            String MENU = "system:protocol";
            /// 查看权限中文名
            String VIEW_NAME_CN = "协议查看";
            /// 查看权限英文名
            String VIEW_NAME_EN = "Protocol View";
            /// 管理权限中文名
            String MANAGE_NAME_CN = "协议管理";
            /// 管理权限英文名
            String MANAGE_NAME_EN = "Protocol Manage";
        }

        /// 移动端应用管理 menuCode=system:config:mobile-app
        interface MobileApp {
            /// 菜单编码
            String MENU = "system:config:mobile-app";
        }
    }

    /// 开发调试
    interface Develop {
        /// 支付调试 menuCode=develop:trade
        interface Trade {
            /// 菜单编码
            String MENU = "develop:trade";
        }

        /// 签名调试 menuCode=develop:sign
        interface Sign {
            /// 菜单编码
            String MENU = "develop:sign";
        }

        /// 认证调试 menuCode=develop:auth
        interface Auth {
            /// 菜单编码
            String MENU = "develop:auth";
        }
    }

    /// 设备管理（开源仅码牌）
    interface Device {
        /// 码牌 menuCode=device:qrcode
        interface QrCode {
            /// 菜单编码
            String MENU = "device:qrcode";
        }
    }
}
