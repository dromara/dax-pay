/**
 * 将 @PermCode 字面量迁移为 PermCodes 嵌套 interface 常量引用。
 * 仅处理含 @PermCode 的 .java 文件；值不变，只换标识符。
 *
 * 用法: node dax-pay-open/_config/scripts/migrate-perm-codes.mjs
 */
import fs from 'node:fs';
import path from 'node:path';

const ROOT = path.resolve(import.meta.dirname, '../..');
const IMPORT_LINE = 'import cn.daxpay.open.platform.core.code.PermCodes;';

/** menuCode 字面量 → 常量路径 */
const MENU_MAP = {
  'channel:merchant': 'PermCodes.Channel.Merchant.MENU',
  'channel:alipay:app': 'PermCodes.Channel.AlipayApp.MENU',
  'channel:wechat:app': 'PermCodes.Channel.WechatApp.MENU',
  'channel:douyin:app': 'PermCodes.Channel.DouyinApp.MENU',
  'merchant:info': 'PermCodes.Merchant.Info.MENU',
  'merchant:credential': 'PermCodes.Merchant.Credential.MENU',
  'merchant:notify_config': 'PermCodes.Merchant.NotifyConfig.MENU',
  'merchant:app': 'PermCodes.Merchant.App.MENU',
  'merchant:app:route': 'PermCodes.Merchant.AppRoute.MENU',
  'merchant:gateway-aggregate': 'PermCodes.Merchant.GatewayAggregate.MENU',
  'merchant:gateway-cashier': 'PermCodes.Merchant.GatewayCashier.MENU',
  'merchant:store': 'PermCodes.Merchant.Store.MENU',
  'merchant:wx_verify': 'PermCodes.Merchant.WxDomainVerify.MENU',
  'payment:alipay:isv': 'PermCodes.Payment.AlipayIsv.MENU',
  'payment:wechat:isv': 'PermCodes.Payment.WechatIsv.MENU',
  'payment:lakala:isv': 'PermCodes.Payment.Lakala.MENU',
  'payment:hkrt:isv': 'PermCodes.Payment.Hkrt.MENU',
  'payment:dougong:isv': 'PermCodes.Payment.Dougong.MENU',
  'payment:vbill:isv': 'PermCodes.Payment.Vbill.MENU',
  'payment:fuyou:isv': 'PermCodes.Payment.Fuyou.MENU',
  'payment:leshua:isv': 'PermCodes.Payment.Leshua.MENU',
  'payment:hmpay:isv': 'PermCodes.Payment.Hmpay.MENU',
  'payment:platform:product': 'PermCodes.Payment.Platform.Product.MENU',
  'payment:platform:provider': 'PermCodes.Payment.Platform.Provider.MENU',
  'payment:platform:pay_channel': 'PermCodes.Payment.Platform.PayChannel.MENU',
  'payment:platform:capability': 'PermCodes.Payment.Platform.Capability.MENU',
  'payment:config:product_config': 'PermCodes.Payment.ProductConfig.MENU',
  'payment:config:wx_verify': 'PermCodes.Payment.Config.WxDomainVerify.MENU',
  'payment:order': 'PermCodes.Payment.Order.MENU',
  'payment:gateway-order': 'PermCodes.Payment.GatewayOrder.MENU',
  'payment:refund': 'PermCodes.Payment.Refund.MENU',
  'payment:trade': 'PermCodes.Payment.Trade.MENU',
  'iam:perm:menu': 'PermCodes.Iam.PermMenu.MENU',
  'iam:role': 'PermCodes.Iam.Role.MENU',
  'iam:user:manager': 'PermCodes.Iam.UserManager.MENU',
  'iam:online:user': 'PermCodes.Iam.OnlineUser.MENU',
  'iam:social:login-config': 'PermCodes.Iam.Social.MENU',
  'system:dict': 'PermCodes.System.Dict.MENU',
  'system:log:login': 'PermCodes.System.Log.Login.MENU',
  'system:log:operate': 'PermCodes.System.Log.Operate.MENU',
  'system:notify:notice': 'PermCodes.System.Notify.MENU',
  'system:notify:wechat-config': 'PermCodes.System.WechatNotify.MENU',
  'system:file:platform': 'PermCodes.System.FilePlatform.MENU',
  'system:platform_config': 'PermCodes.System.PlatformConfig.MENU',
  'system:oss_config': 'PermCodes.System.OssConfig.MENU',
  'system:security_config': 'PermCodes.System.SecurityConfig.MENU',
  'system:protocol': 'PermCodes.System.Protocol.MENU',
  'system:config:mobile_app': 'PermCodes.System.MobileApp.MENU',
  'develop:trade': 'PermCodes.Develop.Trade.MENU',
  'develop:sign': 'PermCodes.Develop.Sign.MENU',
  'develop:auth': 'PermCodes.Develop.Auth.MENU',
  'device:qrcode': 'PermCodes.Device.QrCode.MENU',
};

/** 通用 / 资源专属动作 */
const ACTION_MAP = {
  view: 'PermCodes.Action.VIEW',
  manage: 'PermCodes.Action.MANAGE',
  publish: 'PermCodes.Action.PUBLISH',
  update: 'PermCodes.Action.UPDATE',
  status: 'PermCodes.Action.STATUS',
  sign: 'PermCodes.Action.SIGN',
  kickout: 'PermCodes.Action.KICKOUT',
  reset_password: 'PermCodes.Action.RESET_PASSWORD',
  assign_role: 'PermCodes.Action.ASSIGN_ROLE',
  resend: 'PermCodes.Action.RESEND',
  test: 'PermCodes.Action.TEST',
  credential_config_update: 'PermCodes.Merchant.Credential.CREDENTIAL_CONFIG_UPDATE',
  notify_config_update: 'PermCodes.Merchant.NotifyConfig.NOTIFY_CONFIG_UPDATE',
};

/** 高复用 name（通道商户） */
const CHANNEL_MERCHANT_NAMES = [
  [/nameCn\s*=\s*"通道商户查看"/g, 'nameCn = PermCodes.Channel.Merchant.VIEW_NAME_CN'],
  [/nameEn\s*=\s*"Channel Merchant View"/g, 'nameEn = PermCodes.Channel.Merchant.VIEW_NAME_EN'],
  [/nameCn\s*=\s*"通道商户管理"/g, 'nameCn = PermCodes.Channel.Merchant.MANAGE_NAME_CN'],
  [/nameEn\s*=\s*"Channel Merchant Manage"/g, 'nameEn = PermCodes.Channel.Merchant.MANAGE_NAME_EN'],
];

function walk(dir, out = []) {
  for (const ent of fs.readdirSync(dir, { withFileTypes: true })) {
    const p = path.join(dir, ent.name);
    if (ent.isDirectory()) {
      if (ent.name === 'target' || ent.name === 'node_modules' || ent.name === '.git') continue;
      walk(p, out);
    } else if (ent.isFile() && ent.name.endsWith('.java')) {
      out.push(p);
    }
  }
  return out;
}

function ensureImport(content) {
  if (content.includes(IMPORT_LINE)) return content;
  if (!content.includes('PermCodes.')) return content;

  // 插在 package 后第一个 import 块前/中：放在其他 platform.core  import 附近
  const coreImport = /import cn\.daxpay\.open\.platform\.core\.[^\n]+;\n/;
  if (coreImport.test(content)) {
    return content.replace(coreImport, (m) => m + IMPORT_LINE + '\n');
  }
  // 插在 package 声明后
  return content.replace(
    /(package [^;]+;\s*\n)/,
    `$1\n${IMPORT_LINE}\n`,
  );
}

function transform(content) {
  let next = content;
  let changed = false;

  // menuCode
  for (const [literal, constRef] of Object.entries(MENU_MAP)) {
    const re = new RegExp(`menuCode\\s*=\\s*"${literal.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}"`, 'g');
    const replaced = next.replace(re, `menuCode = ${constRef}`);
    if (replaced !== next) {
      next = replaced;
      changed = true;
    }
  }

  // code = "xxx" only inside typical @PermCode usage (attribute name is unique enough)
  for (const [literal, constRef] of Object.entries(ACTION_MAP)) {
    const re = new RegExp(`code\\s*=\\s*"${literal.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}"`, 'g');
    const replaced = next.replace(re, `code = ${constRef}`);
    if (replaced !== next) {
      next = replaced;
      changed = true;
    }
  }

  // channel merchant names
  for (const [re, repl] of CHANNEL_MERCHANT_NAMES) {
    const replaced = next.replace(re, repl);
    if (replaced !== next) {
      next = replaced;
      changed = true;
    }
  }

  if (!changed) return null;

  next = ensureImport(next);
  return next;
}

const files = walk(ROOT);
let modified = 0;
const report = [];

for (const file of files) {
  // 跳过常量定义自身与无关路径
  if (file.endsWith(`${path.sep}PermCodes.java`)) continue;

  const raw = fs.readFileSync(file, 'utf8');
  if (!raw.includes('@PermCode') && !raw.includes('PermCode')) continue;

  const next = transform(raw);
  if (!next) continue;

  fs.writeFileSync(file, next, 'utf8');
  modified++;
  report.push(path.relative(ROOT, file));
}

console.log(`Modified ${modified} files:`);
for (const f of report.sort()) console.log('  ' + f);
