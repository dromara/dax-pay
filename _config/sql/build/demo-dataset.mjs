/**
 * demo-dataset.mjs — 演示数据剧本（DECLARATIVE，唯一手编数据源）
 *
 * 定位：`export-sql.mjs` 生成 data-demo.sql 的 [4.3][4.4][4.5] 段内容。
 * 只描述"要什么"（谁是谁、开几个店、跑多少单），不写 INSERT 语句本身——
 * 列名/必填/引用完整性由 build/demo-gen.mjs 按 table.sql 的真实列契约兜底。
 *
 * 三段：
 *   [4.3] buildIdentity     商户 / 用户 / 应用 / 门店 / 码牌 / 路由
 *   [4.4] buildChannels     mock 全局与直连凭证 + 模拟服务商 + 微信应用 + 产品生效环境
 *   [4.5] buildTransactions 交易流水（订单/退款/资金流水/通知/回调）
 *
 * 开源版口径（相对 DaxPay Plus 商业版剧本裁剪，2026-11-03 阶段 B 移植）：
 *   1. 无代理商体系：不写 agent_* 表，也不再有 agent_no 列；
 *   2. 无进件体系：不写 pay_onb_* 表；
 *   3. 无费率/限额/风控配置：不写 pay_rate_config / mch_limit_rule / mch_risk_config /
 *      pay_trade_rate_snapshot / pay_trade_profit_detail（开源版无这些表）；
 *   4. pay_trade 无 method / capability / agent_no 列，只保留 provider / channel；
 *   5. 时间列全部为 timestamptz，时间表达式用 demo-gen.mjs 的 now()/ago()/dayAt()。
 *
 * 硬约束：
 *   1. 锚点编号只增不改，发布会/文档/测试脚本都引用它
 *   2. mock 通道商户号与微信 appId 必须与 mock 内置种子一致（MOCKM99999 / wx9999999999999999）
 *   3. 时间用相对表达式（ago / dayAt），装机即新鲜
 *   4. 全部行主键落在演示 ID 段，禁止与 dev 库真实数据撞号
 *   5. 演示数据面向 dev 环境（daxpay.platform.config.sandbox-enabled=true）：
 *      mock 通道商户 sandbox=true，必须配套 pay_md_product_config(mock_pay).active_env=sandbox，
 *      否则路由的环境一致性校验会拒绝（channelMchEnvMismatch）
 */

import { ago, dayAt, raw } from './demo-gen.mjs'

// ---------- ID 段 ----------

const ID_BASE = 1800000000000000000n
/** 身份与配置段：1800000000000000800 ~ 899 */
const idCfg = (n) => ID_BASE + BigInt(800 + n)
/** 交易流水段：1800000000000010000 ~ */
const idTx = (n) => ID_BASE + BigInt(10000 + n)

// ---------- 锚点（只增不改） ----------

export const ANCHORS = {
  // 主演示商户（剧本生成，不取 dev 库示例商户）
  mchMain: 'M1999999999999',
  // 预留：对比商户（本轮不铺，避免出现"半配置商户"告警；后续要演示多商户对比时启用）
  mchCompare: 'M1999999999998',
  // 主演示商户默认应用（default_app=true）
  appMain: 'A1999999999999999',
  // 预留：对比商户默认应用
  appCompare: 'A1999999999999998',
  // 门店：第一条为默认门店
  stores: ['S1999999999990001', 'S1999999999990002'],
  storeMain: 'S1999999999990001',
  // 码牌：前两张随机金额、第三张固定 1 元
  qrCodes: ['DEMOQR20261101001', 'DEMOQR20261101002', 'DEMOQR20261101003'],
  // mock 通道内置种子（不可改，改了 mock 侧归属校验失败）
  mockChannelMchNo: 'MOCKM99999',
  mockWxAppId: 'wx9999999999999999',
  mockWxAppSecret: '9'.repeat(32),
  // 演示模拟服务商编号（mock_isv 服务商管理页主数据，避免新环境空列表；2026-11-08 起）
  mockIsvNo: 'MISV1000000000001',
  // 演示账号（运营 admin / 商户 csqysh；2026-11-03 起与商业版统一为 admin）
  adminUser: 'admin',
  merchantUser: 'csqysh',
  // 预留：对比商户账号
  merchant2User: 'merchant2',
}

// ---------- 公共值 ----------

/** 开发默认密码 121212 的 BCrypt 哈希（与 redact-data.mjs / reset-bootx-password.sql 同源） */
const PWD_HASH = '$2a$10$HiIvaX7tbGWDeRVSciX/LuIAIYUgVJwasWtstsXsakpt0d9Sw.cKG'
/** 商户对接密钥（展示用固定值，检查器要求 publicKey + secretKey 均非空） */
const MCH_SECRET_KEY = 'DaxPayDemoSecretKey00000000000001'

/** 公开测试 RSA 公钥（与 SDK examples/keys 同源，无保密价值） */
const MCH_PUBLIC_KEY = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3JHJQOEY/9+3ESCRynjn
+O7xlQ99qHx/ueQBIaH8avAHZf4F5CSQhjbaIuN7qssgmQ7hVz3ltCRi5zQPX7a7
6O09o+UwOjPN22Fk9l3jR0c3QAmyj1r6Dj1eGcPoiq0FcSdRMedvxxA/2StQdU3G
ulR7a+gLzvOlaubru0yBRp6Gp9y+ozNJv4ohX9cvLDHjfgcY0/8YN/TT0OwZo7Vf
2kttJ7YmXt7bSZ8l2Iy82fxuJS2y/ll5vO8zejvmeJaXsh3R/qa2sTOYd4UL+dG+
bTi0D/mqjLseI3oyuBNIoOXL75lgQBJ++C3CWuzVAe9qQGgDet9vmQjXDUC79eVK
5QIDAQAB
-----END PUBLIC KEY-----
`

/**
 * mock 网关双向签名密钥（公开测试钥，明文 PEM 真实换行）。
 * 主应用 ↔ mock 子应用用；后端 SecureAesGcmEncryptor 对非 `v1:` 前缀原样透传，
 * RsaSignUtil 只剔除空白字符、不容忍 `\n` 字面，所以必须是真实换行。
 */
const MOCK_MERCHANT_PRIVATE_KEY = `-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7M9oWVEuHqbuJ
wVjZdhiuRDaV9lyS8hwyqhG/BHFpo86DbOT2C/8V/sQXup5Yx+JnY37UMFg9UA+O
jjvkeaeNyB269y4ZMrND2LIeRDZSNaB2CqNjYAsfecC40sCUwSDcien0VTrszi7f
/yaLAD54rrnFEV0jJc/ptfXn4VPJDnLQram8Iy9vIB4rI5M7oPTm/14slFKCkFrf
9Izj75CiirpHSKcnzVsrbStZecYGKfFFZK3Wm8pibib5utvgadDXQldVyBB1IYAw
g8VJ8tvrxGwGH1cvHoT1Tjd6CNlHS29TYsmPLjY6somr/1lIKbtzJctcgF8SG30M
Ob4MOor3AgMBAAECggEASSi+djODQQVoMTHKk1G/23r1Ci45h4Pu2S31uXz3BZ3q
uxn3cODHU8U4xT09XNzg8h7EFhC+q1d6fO/BIE0a2iH23HZ1IYva2wz4rzNhN0eV
6gvGRrPmJOrkElbqxDelrAqJz97vJFR/pTKF0ZTK1UcTepyKlY3VbzSZYYGByPSc
QCGWnV5eKpc7xXo1p8GBkRU870kJQeX+ji2LPsskMf1z0I4fiHTZg371gYU0O7IN
ojOEg355Dat77fw260kjjcWlFOsIGj869mkEYNKLjJeL23MS2cYMyqo3jaVe+Ada
rdsqd7xpkoWsxGx4lF1xCYZ3Fl/pBCul4EPp0/SuQQKBgQD2lVFMSlzuHU8atUqm
KEiwuJGUe+GcUy7J9wGknv6NLrDeQkYKEJE79hR/oR9s7sWUEi83HeCqDQ0DUVuC
MD1/t6uzR6p8fL8TilOdWbqlOLczgugcTzKPc58qCQzSI51SKk50/j4qgZXczqZ4
NwuyKj2UqadCGpSzxe3mmhQw6QKBgQDCWgIPVNuwk+50wpdyCJkkh8JJvrY5foLy
JIUawyNwoUsRfpLntcG4NpTAql9UATg2IsPZkK4MBYgQpuRHa/bp3NkFbIKZtvtX
M+wABMfc1IZmQttKQk58t07+z0BFDm1G+AEEIoVIYqPb5s90HA1PYdxAzjSObtLu
GSO4Lo5w3wKBgQDOQMOJzsHAol9idbYFGbkDqL6MtrpV6fY/FQ8Yxa0iJqXmowS2
snjo8MLgpsMEeQxm+Hbp4WHkm5oA5nJmiTdHtzg1c4KVMjsZeyDJgx5tyOzkd2Dz
HQOrngjKPlutAgE7kwuhLHCPWWkjEu+HqnNsCY0M5oNy9YzgenSt6L9u0QKBgQDB
V+OGtweaoX/ufgtFcVJQahIZZPVJOxcvpq8X0Zs3F+5jKAUjJ8jg5sOS66rMGi5+
hJVLokfTQ8l5r12Xw8suGft4DtvZ6ZQey1NzLU5a3L4bi1U2icIlBgM7MJiiWljR
6jSwlVr8iMy0HeixfJqtJxCDE+Bq9cs2/KFxQDeKBwKBgAvN44W2s0h81NYyAo1/
0Bb3BGasQrEhdIE4wNo5kX+NWe3Wh+WuhfoxsKysm5gegKRqZtkDQnNhmAeLw9Q+
uo3Lg0aWrPGUFxUpuJm0bkm/30zs/nTwVpRl45nrOrozeZ9kQDB55+t641ahV6A6
Wo6Syh0aCpzFCx0BQPrLQ2x1
-----END PRIVATE KEY-----
`

const MOCK_PLATFORM_PUBLIC_KEY = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwg2yI8WJV/USy+UOycEW
8SI3WmcDu+LtJG99GDX5QvUvBFBz+5b5+PFtOzBvbilT48WfYihBbO8GnnF1bmyW
2PHKwcVFWGdUo41ICa+7xnX9bXFZ+7PaFAsa94mU4aO0rvKyYFTF/4ecrR4QGuvu
/+q2A589v5mHtMs3zKvSmjzAyXJ2cVvndDGg+ZqM9m7DLn6MZN5tkKGxKO+GE20l
FJ12Rq6TJwww/YfFHjp6P7hPhShepJpxgFWvm3EdH7SnSsKflWp7THSrNc+EvdVw
LCpDi2GQKIGD6QsgfknJZVC4TCHdG2VVv2i/uuJn95LrUdiSagzo8VAoHNHcyqVU
LQIDAQAB
-----END PUBLIC KEY-----
`

/** 配置类数据的创建时间：装机时刻往前 45 天，先于所有交易 */
const CFG_TIME = ago('45 day')
/** 演示回调地址占位（部署时按环境修改） */
const NOTIFY_URL = 'https://demo.example.com/notify'

/** 基础公共列（所有业务表通用，避免每行重复抄） */
const base = (id, time = CFG_TIME) => ({
  id,
  creator: 1,
  create_time: time,
  last_modifier: 1,
  last_modified_time: time,
  version: 0,
  deleted: false,
})

/** 只有 creator/create_time 的关联表（mch_user） */
const baseLite = (id, time = CFG_TIME) => ({ id, creator: 1, create_time: time })

/** 只有主键的纯关系表（iam_user_role） */
const baseId = (id) => ({ id })

/** 未来的绝对时间表达式（密码过期时间等） */
const later = (interval) => raw(`now() + interval '${interval}'`)

/** 在给定 timestamptz 表达式上再加分钟（订单支付/关单/退款时间偏移） */
const addMinutes = (time, minutes) => raw(`(${time.sql}) + interval '${minutes} minute'`)

// ---------- [4.3] 身份与配置 ----------

export function buildIdentity(gen) {
  const adminUserId = idCfg(1)
  const merchantUserId = idCfg(2)

  // ===== 运营端演示账号（admin 终端，role 1 admin_admin）=====
  userOne(gen, {
    userId: adminUserId,
    name: '演示运营管理员',
    account: ANCHORS.adminUser,
    clientCode: 'admin',
    roleId: 1,
    roleRowId: idCfg(32),
  })

  // ===== 商户端演示账号（merchant 终端，role 2 merchant_admin）=====
  userOne(gen, {
    userId: merchantUserId,
    name: '演示商户管理员',
    account: ANCHORS.merchantUser,
    clientCode: 'merchant',
    roleId: 2,
    roleRowId: idCfg(33),
  })

  // ===== 主演示商户 =====
  gen.add('mch_info', {
    ...base(idCfg(10)),
    mch_no: ANCHORS.mchMain,
    mch_name: '演示商户',
    mch_short_name: '演示商户',
    admin_user_id: merchantUserId,
    status: 'enable',
    subject_type: 'enterprise',
  })
  gen.add('mch_user', {
    ...baseLite(idCfg(11)),
    user_id: merchantUserId,
    mch_no: ANCHORS.mchMain,
    administrator: true,
  })

  // ===== 默认应用 + 回调配置 + 对接凭证 =====
  gen.add('mch_app_info', {
    ...base(idCfg(12)),
    mch_no: ANCHORS.mchMain,
    app_id: ANCHORS.appMain,
    app_name: '演示商户默认应用',
    status: 'enable',
    default_app: true,
  })
  gen.add('mch_credential', {
    ...base(idCfg(14)),
    mch_no: ANCHORS.mchMain,
    public_key: MCH_PUBLIC_KEY,
    secret_key: MCH_SECRET_KEY,
  })

  // ===== mock 通道商户（唯一可支付的通道商户）=====
  gen.add('mch_channel_merchant', {
    ...base(idCfg(15)),
    mch_no: ANCHORS.mchMain,
    channel_mch_no: ANCHORS.mockChannelMchNo,
    channel_merchant_name: '模拟支付',
    product: 'mock_pay',
    enable: true,
    source: 'manual',
    sandbox: true,
  })

  // ===== 门店（第一家为默认门店，app_id 指向商户默认应用）=====
  const storeRows = ANCHORS.stores.map((storeNo, i) => {
    gen.add('mch_store_info', {
      ...base(idCfg(16 + i)),
      mch_no: ANCHORS.mchMain,
      store_no: storeNo,
      store_name: i === 0 ? '演示商户总店' : '演示商户分店',
      app_id: ANCHORS.appMain,
      status: 'enable',
      default_store: i === 0,
      region_code: '110101',
      address: '演示地址（占位）',
    })
    return { storeNo }
  })

  // ===== 码牌：前两张随机金额、第三张固定 1 元（开源版 program_type 仅 h5/mini_app）=====
  ANCHORS.qrCodes.forEach((code, i) => {
    const fixed = i === ANCHORS.qrCodes.length - 1
    gen.add('device_qr_code', {
      ...base(idCfg(18 + i)),
      code,
      name: fixed ? '演示商户-一元快收码牌' : `演示商户-聚合收款码牌${i + 1}`,
      batch_no: 'DEMOB20261101',
      mch_no: ANCHORS.mchMain,
      app_id: ANCHORS.appMain,
      program_type: 'mini_app',
      amount_type: fixed ? 'fixed' : 'random',
      fixed_amount: fixed ? 100 : null,
      status: 'enabled',
      store_no: storeRows[Math.min(i, storeRows.length - 1)].storeNo,
      allocation: false,
    })
  })

  // ===== 路由策略（PayRouteChecker：每个启用应用必须有策略行）=====
  const strategyId = idCfg(21)
  gen.add('pay_route_strategy', {
    ...base(strategyId),
    app_id: ANCHORS.appMain,
    mch_no: ANCHORS.mchMain,
    mode: 'basic',
  })
  // basic 模式按「支付渠道 → 通道商户」命中；演示五个渠道全部指向 mock 通道商户
  ;['aggregate_pay', 'wechat', 'alipay', 'union_pay', 'douyin'].forEach((provider, i) => {
    gen.add('pay_route_basic_config', {
      ...base(idCfg(22 + i)),
      strategy_id: strategyId,
      provider,
      channel_mch_no: ANCHORS.mockChannelMchNo,
    })
  })

  return {
    mchMain: {
      mchNo: ANCHORS.mchMain,
      appId: ANCHORS.appMain,
      storeRows,
    },
    adminUserId,
    merchantUserId,
  }
}

/** 账号三件套 + 角色绑定：iam_user_info / expand / password_security / role */
function userOne(gen, { userId, name, account, clientCode, roleId, roleRowId }) {
  gen.add('iam_user_info', {
    ...base(userId),
    name,
    client_code: clientCode,
    account,
    password: PWD_HASH,
    status: 'normal',
    // 非内置超管：允许运营端/商户端正常编辑、禁用、删除（内置超管为 bootx id=1）
    administrator: false,
  })
  gen.add('iam_user_expand_info', {
    ...base(userId),
    register_source: 'demo',
  })
  // 密码状态行主键 = 用户 id（UserPasswordSecurityManager.findByUserId 走 findById）：
  // initial_password=false + 远期过期 + 已改密时间 + 未锁定，否则登录后被 40302 拦到改密页
  gen.add('iam_user_password_security', {
    ...base(userId),
    password_error_count: 0,
    lock_time: null,
    password_expire_time: later('3650 day'),
    last_change_password_time: CFG_TIME,
    initial_password: false,
  })
  gen.add('iam_user_role', {
    ...baseId(roleRowId),
    user_id: userId,
    role_id: roleId,
  })
}

// ---------- [4.4] 通道配置 ----------

export function buildChannels(gen, ctx) {
  const mchNo = ctx.mchMain.mchNo

  // ===== mock 产品生效环境：必须与演示通道商户 sandbox=true 一致 =====
  // 缺这行时 PayProductConfigManager.isSandboxActive('mock_pay') 回落 prod，
  // 路由会以 channelMchEnvMismatch 拒绝所有演示支付。
  gen.add('pay_md_product_config', {
    ...base(idCfg(27)),
    product: 'mock_pay',
    channel: 'mock',
    active_env: 'sandbox',
    remark: '演示环境：mock 产品固定沙箱生效，与演示通道商户 sandbox=true 匹配',
  })

  // ===== mock 直连：微信应用（appId/secret 必须与 mock 内置种子一致）=====
  const wxMchAppId = idCfg(28)
  gen.add('wx_mch_app', {
    ...base(wxMchAppId),
    mch_no: mchNo,
    app_name: '演示商户微信应用（mock 直连）',
    app_type: 'official_account',
    wx_app_id: ANCHORS.mockWxAppId,
    app_secret: ANCHORS.mockWxAppSecret,
  })
  ;['wechat_jsapi', 'wechat_mini'].forEach((capability, i) => {
    gen.add('wx_channel_app_capability', {
      ...base(idCfg(29 + i)),
      mch_no: mchNo,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      capability,
      app_scope: 'merchant',
      wx_app_ref_id: wxMchAppId,
    })
  })

  // ===== mock 网关双向签名凭证 =====
  // 没有这两行，装完扫码会在签名阶段失败；明文 PEM 由 TypeHandler 透传，运行时仍可正常读。
  // 全局单行主键固定 1（MockChannelCredentialManager.SINGLETON_ID），不能落演示 ID 段。
  gen.add('mock_channel_credential', {
    ...base(1),
    merchant_private_key: MOCK_MERCHANT_PRIVATE_KEY,
    platform_public_key: MOCK_PLATFORM_PUBLIC_KEY,
  })
  // 直连产品按通道商户号取商户级凭证；gateway_base_url 留空（NULL）回落子应用 yml 配置，
  // 写死会把部署环境钉死在本机 127.0.0.1:10400（容器编排里该地址指向容器自身）。
  gen.add('mock_direct_credential', {
    ...base(idCfg(31)),
    channel_mch_no: ANCHORS.mockChannelMchNo,
    merchant_private_key: MOCK_MERCHANT_PRIVATE_KEY,
    platform_public_key: MOCK_PLATFORM_PUBLIC_KEY,
    gateway_base_url: null,
  })

  // ===== mock 服务商主数据 =====
  // 模拟服务商管理页的演示数据（仅主数据，不挂服务商通道商户与服务商密钥，演示时按需在页面建档）
  gen.add('mock_isv_info', {
    ...base(idCfg(34)),
    isv_no: ANCHORS.mockIsvNo,
    isv_name: '演示模拟服务商',
    enable: true,
    pay_enable: true,
  })

  return { wxMchAppId }
}

// ---------- [4.5] 交易流水 ----------

/** 确定性伪随机（固定种子 → 同版本重跑字节一致） */
function makeRng(seed) {
  let s = seed >>> 0
  return () => {
    s = (Math.imul(s, 1664525) + 1013904223) >>> 0
    return s / 4294967296
  }
}

/** 支付方式与渠道维度（mock_pay 支持的方式子集，provider 与 PayMethodEnum 归属一致） */
const PAY_ROUTES = [
  { method: 'wechat_qr', capability: 'wechat_qr', provider: 'wechat' },
  { method: 'alipay_qr', capability: 'alipay_qr', provider: 'alipay' },
  { method: 'union_qr', capability: 'union_qr', provider: 'union_pay' },
  { method: 'douyin_qr', capability: 'douyin_qr', provider: 'douyin' },
  { method: 'wechat_jsapi', capability: 'wechat_jsapi', provider: 'wechat' },
]

/** 订单状态 → 交易状态（开源版交易态无 init，待支付 = progress） */
const TRADE_STATUS_MAP = {
  paid: 'success',
  failed: 'fail',
  closed: 'closed',
  expired: 'closed',
  wait_pay: 'progress',
}

/**
 * 交易流水：近 30 天历史 + 今天自然流量 + 3 笔现场可操作单
 * 一致性由本函数保证：订单 ↔ 交易 ↔ 资金流水 ↔ 通知任务 逐层对齐
 *
 * 随机种子固定为 20261115：该种子下规模落在设计目标上（约 168 单 / 15 退款 / ≥100 通知任务），
 * 换种子只影响分布不影响正确性（一致性断言在函数末尾自检）。
 */
export function buildTransactions(gen, ctx, seed = 20261115) {
  const mch = ctx.mchMain
  const rng = makeRng(seed)
  let seq = 0
  const nextId = () => idTx(seq++)
  let noSeq = 0
  const nextNo = (prefix) => `${prefix}${String(++noSeq).padStart(8, '0')}`

  const stats = { orders: 0, trades: 0, refunds: 0, notices: 0, records: 0, flows: 0 }

  /** 订单标题：码牌收款带码牌编号与实际金额，API 单用通用文案 */
  function orderTitle(route, source, amount) {
    return source === 'cashier_code'
      ? `${ANCHORS.qrCodes[0]} 码牌收款: ${(amount / 100).toFixed(2)}元`
      : `演示订单-${route.provider}`
  }

  /**
   * 商户通知任务 + 发送记录（三种形态）：
   *   success 一次送达 / retry 首次失败后重试成功 / failed 始终不可达（保留待重试）
   */
  function noticeTask({ id, event, bizId, bizNo, time, mode }) {
    const retry = mode === 'retry'
    const failed = mode === 'failed'
    gen.add('mch_notice_task', {
      ...base(id, time),
      mch_no: mch.mchNo,
      app_id: mch.appId,
      biz_id: bizId,
      biz_no: bizNo,
      event,
      // 应用级订阅来源(notice_source=app)已随异步通知配置下线移除, 演示任务统一按订单级回调生成
      source: 'order',
      content_mode: 'snapshot',
      content: JSON.stringify({ demo: true, bizNo, event }),
      url: NOTIFY_URL,
      success: !failed,
      send_count: retry ? 2 : 1,
      delay_count: retry ? 1 : 0,
      next_time: failed ? addMinutes(time, 5) : null,
      latest_time: retry ? addMinutes(time, 1) : time,
      error_msg: failed ? '演示数据：回调地址不可达（占位域名）' : null,
      transport: 'http',
      format: 'system',
    })
    const record = (at, reqCount, ok) => {
      gen.add('mch_notice_record', {
        ...base(nextId(), at),
        mch_no: mch.mchNo,
        task_id: id,
        req_count: reqCount,
        send_type: 'auto',
        success: ok,
        http_status: ok ? 200 : 500,
        error_msg: ok ? null : '演示数据：连接超时',
        request_digest: `demo-${bizNo}-${reqCount}`,
      })
    }
    if (retry) {
      record(time, 1, false)
      record(addMinutes(time, 1), 2, true)
    } else {
      record(time, 1, !failed)
    }
    stats.notices += 1
    stats.records += retry ? 2 : 1
  }

  /** 造一笔订单（含统一交易与派生产物）；agoMinutes 给定时用"N 分钟前"（保证落在过去） */
  function makeOrder({ dayOffset, hour, minute, status, amount, route, agoMinutes, titleOverride }) {
    const orderId = nextId()
    const tradeId = nextId()
    const orderNo = nextNo('DEMO')
    const tradeNo = nextNo('DEMOT')
    // 开源版交易来源枚举无 api：码牌走 cashier_code，接口单走 mch_api
    const source = rng() < 0.6 ? 'cashier_code' : 'mch_api'
    const title = titleOverride ?? orderTitle(route, source, amount)
    const createTime = agoMinutes != null ? ago(`${agoMinutes} minute`) : dayAt(dayOffset, hour, minute)
    const payTime = status === 'paid'
      ? (agoMinutes != null ? ago(`${Math.max(agoMinutes - 1, 0)} minute`) : addMinutes(createTime, 1))
      : null
    const closeTime = status === 'closed' || status === 'expired'
      ? (agoMinutes != null ? ago(`${Math.max(agoMinutes - 5, 0)} minute`) : addMinutes(createTime, 5))
      : null

    gen.add('pay_normal_order', {
      ...base(orderId, createTime),
      order_no: orderNo,
      biz_order_no: `BIZ${orderNo}`,
      title,
      source,
      status,
      amount,
      app_id: mch.appId,
      mch_no: mch.mchNo,
      store_no: mch.storeRows[0].storeNo,
      channel: 'mock',
      product: 'mock_pay',
      method: route.method,
      capability: route.capability,
      provider: route.provider,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      client_ip: '127.0.0.1',
      pay_time: payTime,
      close_time: closeTime,
      expired_time: status === 'wait_pay' ? addMinutes(createTime, 30) : null,
      error_msg: status === 'failed' ? '演示数据：通道返回支付失败' : null,
    })

    const tradeStatus = TRADE_STATUS_MAP[status]
    const posted = status === 'paid' ? amount : 0
    const tradeRow = {
      ...base(tradeId, createTime),
      trade_no: tradeNo,
      trade_type: 'normal',
      container_id: orderId,
      amount,
      posted_amount: posted,
      refundable_balance: posted,
      status: tradeStatus,
      pay_time: payTime,
      close_time: closeTime,
      source,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      store_no: mch.storeRows[0].storeNo,
      out_order_no: `MOCK${tradeNo}`,
      app_id: mch.appId,
      mch_no: mch.mchNo,
      provider: route.provider,
      channel: 'mock',
      title,
    }
    gen.add('pay_trade', tradeRow)

    stats.orders += 1
    stats.trades += 1

    if (status === 'paid') {
      gen.add('pay_fund_flow', {
        ...base(nextId(), payTime),
        mch_no: mch.mchNo,
        app_id: mch.appId,
        flow_type: 'pay',
        trade_no: tradeNo,
        biz_order_no: orderNo,
        title,
        amount,
        currency: 'cny',
        channel: 'mock',
        provider: route.provider,
        channel_mch_no: ANCHORS.mockChannelMchNo,
        out_order_no: `MOCK${tradeNo}`,
        finish_time: payTime,
      })
      stats.flows += 1
      noticeTask({
        id: nextId(), event: 'pay.success', bizId: orderId, bizNo: orderNo, time: payTime,
        mode: rng() < 0.1 ? 'retry' : 'success',
      })
    } else if (status === 'failed') {
      noticeTask({
        id: nextId(), event: 'pay.fail', bizId: orderId, bizNo: orderNo, time: createTime,
        mode: 'failed',
      })
    } else if (status === 'closed') {
      noticeTask({
        id: nextId(), event: 'pay.close', bizId: orderId, bizNo: orderNo, time: closeTime,
        mode: 'success',
      })
    } else if (status === 'expired') {
      noticeTask({
        id: nextId(), event: 'pay.timeout', bizId: orderId, bizNo: orderNo, time: closeTime,
        mode: 'success',
      })
    }

    return { orderId, tradeId, orderNo, tradeNo, amount, status, payTime, tradeRow }
  }

  /** 退款单（含成功退款资金流水与通知）；退款成功时下调原交易可退余额 */
  let refundSeq = 0
  // 前四笔退款固定覆盖全部状态（演示退款列表的状态筛选），其余按权重随机
  const FORCED_REFUND_STATUS = ['success', 'progress', 'fail', 'close']
  function makeRefund(o) {
    const refundId = nextId()
    const refundNo = nextNo('DEMOR')
    // 70% 全额、30% 部分（保证单笔退款不超过原交易额）
    const refundAmount = rng() < 0.7 ? o.amount : Math.max(1, Math.round(o.amount * (0.2 + rng() * 0.5)))
    const r = rng()
    const status = refundSeq < FORCED_REFUND_STATUS.length
      ? FORCED_REFUND_STATUS[refundSeq]
      : r < 0.72 ? 'success' : r < 0.88 ? 'progress' : r < 0.95 ? 'fail' : 'close'
    refundSeq += 1
    const createTime = addMinutes(o.payTime, 5 + Math.floor(rng() * 20))
    const finishTime = status === 'success' ? addMinutes(createTime, 3) : null

    gen.add('pay_refund_order', {
      ...base(refundId, createTime),
      app_id: mch.appId,
      refund_no: refundNo,
      biz_refund_no: `BIZ${refundNo}`,
      title: `演示退款-${o.tradeNo}`,
      trade_no: o.tradeNo,
      biz_order_no: o.orderNo,
      order_amount: o.amount,
      amount: refundAmount,
      reason: '演示数据：用户申请退款',
      status,
      channel: 'mock',
      product: 'mock_pay',
      channel_mch_no: ANCHORS.mockChannelMchNo,
      store_no: mch.storeRows[0].storeNo,
      mch_no: mch.mchNo,
      finish_time: finishTime,
      trade_type: 'normal',
      client_ip: '127.0.0.1',
    })

    if (status === 'success') {
      // 退款成功 → 下调原交易可退余额（保证 trade.refundable_balance 与退款合计对得上）
      o.tradeRow.refundable_balance -= refundAmount
      gen.add('pay_fund_flow', {
        ...base(nextId(), finishTime),
        mch_no: mch.mchNo,
        app_id: mch.appId,
        flow_type: 'refund',
        trade_no: o.tradeNo,
        refund_no: refundNo,
        biz_order_no: o.orderNo,
        title: `演示退款-${o.tradeNo}`,
        amount: refundAmount,
        currency: 'cny',
        channel: 'mock',
        provider: o.tradeRow.provider,
        channel_mch_no: ANCHORS.mockChannelMchNo,
        out_order_no: `MOCKR${refundNo}`,
        finish_time: finishTime,
      })
      stats.flows += 1
      noticeTask({
        id: nextId(), event: 'refund.success', bizId: refundId, bizNo: refundNo, time: finishTime,
        mode: 'success',
      })
    } else if (status === 'fail') {
      noticeTask({
        id: nextId(), event: 'refund.fail', bizId: refundId, bizNo: refundNo, time: createTime,
        mode: 'failed',
      })
    } else if (status === 'close') {
      noticeTask({
        id: nextId(), event: 'refund.close', bizId: refundId, bizNo: refundNo, time: createTime,
        mode: 'success',
      })
    }
    // progress：退款中，不产生终态通知

    stats.refunds += 1
    return { refundId, refundNo, refundAmount, status }
  }

  // ===== 近 30 天历史（跳过今天，今天单独造现场可操作单）=====
  for (let d = 29; d >= 1; d--) {
    const count = 4 + Math.floor(rng() * 4) // 4~7 笔/天
    for (let i = 0; i < count; i++) {
      const hour = 9 + Math.floor(rng() * 12) // 09:00 ~ 20:59
      const minute = Math.floor(rng() * 60)
      const s = rng()
      const status = s < 0.8 ? 'paid' : s < 0.84 ? 'wait_pay' : s < 0.92 ? 'closed' : s < 0.98 ? 'expired' : 'failed'
      const a = rng()
      // 金额 1~500 元（分）：80% 小额、15% 中额、5% 大额
      const amount = a < 0.8
        ? 100 + Math.floor(rng() * 9900)
        : a < 0.95
          ? 10000 + Math.floor(rng() * 20000)
          : 30000 + Math.floor(rng() * 20000)
      const route = PAY_ROUTES[Math.floor(rng() * PAY_ROUTES.length)]
      const o = makeOrder({ dayOffset: d, hour, minute, status, amount, route })

      // 约 12% 的已支付订单挂退款单（全部/部分/处理中/失败/关闭）
      if (status === 'paid' && rng() < 0.12) {
        makeRefund(o)
      }
    }
  }

  // ===== 今天的自然流量（用"N 分钟前"，保证不会出现未来时间）=====
  for (let i = 0; i < 5; i++) {
    const s = rng()
    const status = s < 0.8 ? 'paid' : s < 0.9 ? 'wait_pay' : 'closed'
    const amount = 200 + Math.floor(rng() * 8000)
    const route = PAY_ROUTES[Math.floor(rng() * PAY_ROUTES.length)]
    const o = makeOrder({ status, amount, route, agoMinutes: 55 + i * 65 })
    if (status === 'paid' && rng() < 0.15) {
      makeRefund(o)
    }
  }

  // ===== 今天的 3 笔现场可操作单（刚支付成功、未退款，供现场演示退款/关单）=====
  const walkIn = [5, 20, 40]
  walkIn.forEach((minsAgo, i) => {
    makeOrder({
      status: 'paid',
      amount: [1280, 6800, 19900][i],
      route: PAY_ROUTES[i % PAY_ROUTES.length],
      agoMinutes: minsAgo,
      titleOverride: '现场演示订单（可退款/可关单）',
    })
  })

  // ===== 通道回调记录（成功/失败/异常/忽略/未找到，撑起回调记录页）=====
  const callbacks = [
    { status: 'success', type: 'pay', tip: '通道支付回调处理成功' },
    { status: 'success', type: 'pay', tip: '通道支付回调处理成功' },
    { status: 'fail', type: 'pay', tip: '验签失败：签名不匹配' },
    { status: 'exception', type: 'pay', tip: '解析异常：报文格式错误' },
    { status: 'ignore', type: 'refund', tip: '重复回调已忽略' },
    { status: 'not_found', type: 'pay', tip: '未找到对应交易' },
  ]
  callbacks.forEach((c, i) => {
    gen.add('pay_callback_record', {
      ...base(nextId(), ago(`${i + 1} day`)),
      mch_no: mch.mchNo,
      app_id: mch.appId,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      trade_no: null,
      out_trade_no: `MOCKCALLBACK${String(i + 1).padStart(4, '0')}`,
      product: 'mock_pay',
      callback_type: c.type,
      notify_info: JSON.stringify({ demo: true, note: c.tip, seq: i + 1 }),
      status: c.status,
      error_msg: c.status === 'success' ? null : c.tip,
    })
  })

  assertConsistency(gen)
  return stats
}

/** 构建期一致性断言：失败直接抛出，导出脚本据此中止且不落盘 */
export function assertConsistency(gen) {
  const fails = checkConsistency(gen)
  if (fails.length) {
    const err = new Error(`[演示数据] 一致性检查不通过 ${fails.length} 项:\n  - ${fails.slice(0, 20).join('\n  - ')}`)
    err.fails = fails
    throw err
  }
}

/** 计数工具：按 key 聚合行数 */
function countBy(list, key) {
  return list.reduce((m, r) => m.set(r[key], (m.get(r[key]) ?? 0) + 1), new Map())
}

/**
 * 校验订单/交易/资金流水/退款/通知逐层对齐。
 * @returns {string[]} 失败明细（空数组表示通过）
 */
export function checkConsistency(gen) {
  const fails = []
  const orders = gen.all('pay_normal_order')
  const orderById = new Map(orders.map((o) => [o.id, o]))
  const orderIds = new Set(orders.map((o) => o.id))
  const trades = gen.all('pay_trade')
  const refunds = gen.all('pay_refund_order')
  const flows = gen.all('pay_fund_flow')
  const tasks = gen.all('mch_notice_task')
  const records = gen.all('mch_notice_record')
  const basics = gen.all('pay_route_basic_config')

  // 1. 订单 ↔ 交易 1:1
  const tradeByContainer = countBy(trades, 'container_id')
  for (const o of orders) {
    if ((tradeByContainer.get(o.id) ?? 0) !== 1) {
      fails.push(`订单 ${o.order_no} 对应交易数 ${tradeByContainer.get(o.id) ?? 0} ≠ 1`)
    }
  }
  for (const t of trades) {
    if (!orderIds.has(t.container_id)) {
      fails.push(`交易 ${t.trade_no} 的 container_id=${t.container_id} 找不到订单`)
      continue
    }
    const o = orderById.get(t.container_id)
    if (o.amount !== t.amount) fails.push(`订单 ${o.order_no} 金额 ${o.amount} ≠ 交易 ${t.trade_no} 金额 ${t.amount}`)
    if (TRADE_STATUS_MAP[o.status] !== t.status) {
      fails.push(`订单 ${o.order_no} 状态 ${o.status} 与交易状态 ${t.status} 不匹配`)
    }
    if (t.status === 'success') {
      if (t.posted_amount !== t.amount) fails.push(`成功交易 ${t.trade_no} 入账额 ${t.posted_amount} ≠ 交易额 ${t.amount}`)
      if (t.pay_time == null) fails.push(`成功交易 ${t.trade_no} 缺 pay_time`)
    } else if (t.posted_amount !== 0 || t.refundable_balance !== 0 || t.pay_time != null) {
      fails.push(`非成功交易 ${t.trade_no} 不应有入账额/可退余额/支付时间`)
    }
  }

  // 2. 退款：单号唯一、指向存在交易、成功退款不超原交易额
  const seenRefundNo = new Set()
  const refundedByTrade = new Map()
  for (const r of refunds) {
    if (seenRefundNo.has(r.refund_no)) fails.push(`退款单号重复 ${r.refund_no}`)
    seenRefundNo.add(r.refund_no)
    const trade = trades.find((t) => t.trade_no === r.trade_no)
    if (!trade) {
      fails.push(`退款 ${r.refund_no} 指向不存在的交易 ${r.trade_no}`)
      continue
    }
    if (r.status === 'success') {
      refundedByTrade.set(r.trade_no, (refundedByTrade.get(r.trade_no) ?? 0) + r.amount)
    }
  }
  for (const [tradeNo, sum] of refundedByTrade) {
    const trade = trades.find((t) => t.trade_no === tradeNo)
    if (sum > trade.amount) fails.push(`交易 ${tradeNo} 退款合计 ${sum} 超过交易额 ${trade.amount}`)
  }

  // 3. 可退余额 = 交易额 - 成功退款合计
  for (const t of trades) {
    if (t.status !== 'success') continue
    const want = t.amount - (refundedByTrade.get(t.trade_no) ?? 0)
    if (t.refundable_balance !== want) {
      fails.push(`交易 ${t.trade_no} 可退余额 ${t.refundable_balance} ≠ ${want}（退款合计对不上）`)
    }
  }

  // 4. 资金流水：成功交易恰好 1 条等额 pay 流水；成功退款恰好 1 条等额 refund 流水
  const payFlowByTrade = countBy(flows.filter((f) => f.flow_type === 'pay'), 'trade_no')
  for (const t of trades) {
    if (t.status !== 'success') continue
    if ((payFlowByTrade.get(t.trade_no) ?? 0) !== 1) {
      fails.push(`成功交易 ${t.trade_no} 支付流水数 ${payFlowByTrade.get(t.trade_no) ?? 0} ≠ 1`)
      continue
    }
    const f = flows.find((x) => x.flow_type === 'pay' && x.trade_no === t.trade_no)
    if (f.amount !== t.amount) fails.push(`交易 ${t.trade_no} 支付流水金额 ${f.amount} ≠ ${t.amount}`)
  }
  const refundFlowByNo = new Map(flows.filter((f) => f.flow_type === 'refund').map((f) => [f.refund_no, f]))
  for (const r of refunds.filter((x) => x.status === 'success')) {
    const f = refundFlowByNo.get(r.refund_no)
    if (!f) {
      fails.push(`成功退款 ${r.refund_no} 缺资金流水`)
      continue
    }
    if (f.amount !== r.amount) fails.push(`退款 ${r.refund_no} 流水金额 ${f.amount} ≠ 退款额 ${r.amount}`)
    if (f.trade_no !== r.trade_no) fails.push(`退款 ${r.refund_no} 流水交易号 ${f.trade_no} ≠ ${r.trade_no}`)
  }
  const refundIds = new Set(refunds.map((r) => r.refund_no))
  for (const f of flows.filter((x) => x.flow_type === 'refund')) {
    if (!refundIds.has(f.refund_no)) fails.push(`退款流水 ${f.refund_no} 找不到退款单`)
  }

  // 5. 通知任务：唯一键不冲突、业务单号可回溯、记录数与 send_count 对齐
  const taskKey = (t) => [t.mch_no, t.app_id, t.event, t.biz_no, t.transport, t.format, t.source].join('|')
  const seenTask = new Set()
  const bizNos = new Set([...orders.map((o) => o.order_no), ...refunds.map((r) => r.refund_no)])
  const recordByTask = countBy(records, 'task_id')
  for (const t of tasks) {
    const key = taskKey(t)
    if (seenTask.has(key)) fails.push(`通知任务唯一键重复: ${key}`)
    seenTask.add(key)
    if (!bizNos.has(t.biz_no)) fails.push(`通知任务 biz_no=${t.biz_no} 找不到订单/退款`)
    if ((recordByTask.get(t.id) ?? 0) !== t.send_count) {
      fails.push(`通知任务 ${t.biz_no}/${t.event} 记录数 ${recordByTask.get(t.id) ?? 0} ≠ send_count ${t.send_count}`)
    }
  }
  const taskIds = new Set(tasks.map((t) => t.id))
  for (const r of records) {
    if (!taskIds.has(r.task_id)) fails.push(`通知记录 task_id=${r.task_id} 找不到任务`)
  }

  // 6. 路由：五个支付渠道全覆盖且全部指向 mock 通道商户
  const providers = new Set(basics.map((b) => b.provider))
  for (const p of ['aggregate_pay', 'wechat', 'alipay', 'union_pay', 'douyin']) {
    if (!providers.has(p)) fails.push(`路由缺少支付渠道 ${p}`)
  }
  for (const b of basics) {
    if (b.channel_mch_no !== ANCHORS.mockChannelMchNo) {
      fails.push(`路由渠道 ${b.provider} 指向 ${b.channel_mch_no}，预期 ${ANCHORS.mockChannelMchNo}`)
    }
  }
  if (basics.length !== providers.size) fails.push(`路由渠道存在重复配置（${basics.length} 行 / ${providers.size} 个渠道）`)

  // 7. 状态覆盖（演示列表的状态筛选需要每种状态都有样本）
  for (const st of ['paid', 'wait_pay', 'closed', 'expired', 'failed']) {
    if (!orders.some((o) => o.status === st)) fails.push(`订单缺少状态 ${st}`)
  }
  for (const st of ['success', 'progress', 'fail', 'close']) {
    if (!refunds.some((r) => r.status === st)) fails.push(`退款缺少状态 ${st}`)
  }

  return fails
}

// ---------- 装机期断言块（写入 data-demo.sql 末尾，失败整装回滚） ----------

/**
 * 生成 `DO $$ ... $$` 校验块。
 * 构建期断言（checkConsistency）保证"生成的东西对"，这段保证"库里落下来的东西对"——
 * 覆盖数据被手工改动、生成器与表结构错位、执行被截断等场景。
 * 配合 psql `-v ON_ERROR_STOP=1 --single-transaction`：断言失败 → 抛异常 → 整个安装事务回滚。
 */
export function buildAssertBlock({ minOrders = 150 } = {}) {
  return `DO $$
DECLARE n int;
BEGIN
    -- 1. 演示订单量（防止演示段未执行或执行被截断）
    SELECT count(*) INTO n FROM public.pay_normal_order WHERE order_no LIKE 'DEMO%' AND deleted = false;
    IF n < ${minOrders} THEN
        RAISE EXCEPTION '[演示数据校验] 演示订单数异常: %（预期 ≥ ${minOrders}）', n;
    END IF;

    -- 2. 订单 ↔ 统一交易 1:1（container_id 双向回指）
    SELECT count(*) INTO n FROM public.pay_normal_order o
     WHERE o.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_trade t WHERE t.container_id = o.id AND t.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔订单缺统一交易记录', n; END IF;
    SELECT count(*) INTO n FROM public.pay_trade t
     WHERE t.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_normal_order o WHERE o.id = t.container_id AND o.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔交易找不到订单', n; END IF;

    -- 3. 金额守恒：订单金额 = 交易金额 = 入账金额；非成功交易不得有入账
    SELECT count(*) INTO n FROM public.pay_trade t
      JOIN public.pay_normal_order o ON o.id = t.container_id
     WHERE t.status = 'success'
       AND (o.amount <> t.amount OR t.posted_amount <> t.amount);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔交易金额与订单不一致', n; END IF;
    SELECT count(*) INTO n FROM public.pay_trade t
     WHERE t.status <> 'success'
       AND (t.posted_amount <> 0 OR t.refundable_balance <> 0 OR t.pay_time IS NOT NULL);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔非成功交易残留入账额/可退余额/支付时间', n; END IF;

    -- 4. 资金流水唯一且等额（支付：每成功交易恰好 1 条；退款：每成功退款恰好 1 条）
    SELECT count(*) INTO n FROM public.pay_trade t
     WHERE t.status = 'success' AND t.deleted = false
       AND (SELECT count(*) FROM public.pay_fund_flow f
             WHERE f.trade_no = t.trade_no AND f.flow_type = 'pay' AND f.deleted = false) <> 1;
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔成功交易支付流水条数不为 1', n; END IF;
    SELECT count(*) INTO n FROM public.pay_trade t
     WHERE t.status = 'success' AND t.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_fund_flow f
                        WHERE f.trade_no = t.trade_no AND f.flow_type = 'pay'
                          AND f.amount = t.amount AND f.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔成功交易缺等额支付流水', n; END IF;
    SELECT count(*) INTO n FROM public.pay_refund_order r
     WHERE r.status = 'success' AND r.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_fund_flow f
                        WHERE f.refund_no = r.refund_no AND f.flow_type = 'refund'
                          AND f.amount = r.amount AND f.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔成功退款缺等额退款流水', n; END IF;

    -- 5. 可退余额与成功退款合计自洽
    SELECT count(*) INTO n FROM public.pay_trade t
     WHERE t.status = 'success' AND t.deleted = false
       AND t.refundable_balance <> t.amount - COALESCE(
           (SELECT sum(r.amount) FROM public.pay_refund_order r
             WHERE r.trade_no = t.trade_no AND r.status = 'success' AND r.deleted = false), 0);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔交易可退余额与退款合计不一致', n; END IF;

    -- 6. 演示商户必须有启用中的 mock 通道商户（否则扫码必然无法支付）
    SELECT count(*) INTO n FROM public.mch_channel_merchant
     WHERE mch_no = '${ANCHORS.mchMain}' AND enable = true AND deleted = false
       AND channel_mch_no = '${ANCHORS.mockChannelMchNo}';
    IF n = 0 THEN RAISE EXCEPTION '[演示数据校验] 演示商户无可用 mock 通道商户（通道商户号应为 ${ANCHORS.mockChannelMchNo}）'; END IF;

    -- 7. 路由覆盖 5 个支付渠道且全部指向 mock 通道商户
    SELECT count(DISTINCT b.provider) INTO n
      FROM public.pay_route_basic_config b
      JOIN public.pay_route_strategy s ON s.id = b.strategy_id
     WHERE s.app_id = '${ANCHORS.appMain}' AND s.deleted = false AND b.deleted = false;
    IF n < 5 THEN RAISE EXCEPTION '[演示数据校验] 路由渠道覆盖不足: %（预期 aggregate_pay/wechat/alipay/union_pay/douyin 共 5 个）', n; END IF;
    SELECT count(*) INTO n
      FROM public.pay_route_basic_config b
      JOIN public.pay_route_strategy s ON s.id = b.strategy_id
     WHERE s.app_id = '${ANCHORS.appMain}' AND s.deleted = false AND b.deleted = false
       AND b.channel_mch_no <> '${ANCHORS.mockChannelMchNo}';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条路由未指向 mock 通道商户 ${ANCHORS.mockChannelMchNo}', n; END IF;

    -- 8. 微信应用 appId 必须与 mock 内置种子一致（否则 mock 侧归属不匹配）
    SELECT count(*) INTO n FROM public.wx_mch_app
     WHERE wx_app_id = '${ANCHORS.mockWxAppId}' AND deleted = false;
    IF n = 0 THEN RAISE EXCEPTION '[演示数据校验] 缺 mock 内置微信应用 ${ANCHORS.mockWxAppId}'; END IF;
    SELECT count(*) INTO n FROM public.wx_channel_app_capability
     WHERE channel_mch_no = '${ANCHORS.mockChannelMchNo}' AND deleted = false;
    IF n < 2 THEN RAISE EXCEPTION '[演示数据校验] 微信通道应用能力绑不足: %（预期 wechat_jsapi/wechat_mini 两行）', n; END IF;

    -- 9. 商户通知任务量（撑起通知任务/记录页）
    SELECT count(*) INTO n FROM public.mch_notice_task WHERE biz_no LIKE 'DEMO%' AND deleted = false;
    IF n < 100 THEN RAISE EXCEPTION '[演示数据校验] 通知任务数异常: %（预期 ≥ 100）', n; END IF;

    -- 10. mock 全局凭证两段 PEM 非空（主键固定 1，缺行/空值扫码即签名失败）
    SELECT count(*) INTO n FROM public.mock_channel_credential
     WHERE id = 1 AND deleted = false
       AND COALESCE(merchant_private_key, '') <> '' AND COALESCE(platform_public_key, '') <> '';
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] mock 全局凭证（id=1）缺失或 PEM 为空'; END IF;

    -- 11. mock 直连凭证两段 PEM 非空且网关地址留空（回落子应用 yml，避免钉死本机地址）
    SELECT count(*) INTO n FROM public.mock_direct_credential
     WHERE channel_mch_no = '${ANCHORS.mockChannelMchNo}' AND deleted = false
       AND COALESCE(merchant_private_key, '') <> '' AND COALESCE(platform_public_key, '') <> '';
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] mock 直连凭证 ${ANCHORS.mockChannelMchNo} 缺失或 PEM 为空'; END IF;
    SELECT count(*) INTO n FROM public.mock_direct_credential
     WHERE channel_mch_no = '${ANCHORS.mockChannelMchNo}' AND deleted = false
       AND gateway_base_url IS NOT NULL;
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] mock 直连凭证 gateway_base_url 应留空（当前非空）'; END IF;

    -- 12. mock 产品生效环境为沙箱（与演示通道商户 sandbox=true 匹配，否则路由报环境不一致）
    SELECT count(*) INTO n FROM public.pay_md_product_config
     WHERE product = 'mock_pay' AND active_env = 'sandbox' AND deleted = false;
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] pay_md_product_config 缺 mock_pay 沙箱生效配置'; END IF;
END $$;`
}
