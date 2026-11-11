/**
 * demo-dataset.mjs — 演示数据剧本（DECLARATIVE，唯一手编数据源）
 *
 * 定位：`export-sql.mjs` 生成 data-demo.sql 的 [4.3][4.4][4.5] 段内容。
 * 只描述"要什么"（谁是谁、开几个店、跑多少单），不写 INSERT 语句本身——
 * 列名/必填/引用完整性由 build/demo-gen.mjs 按 table.sql 的真实列契约兜底。
 *
 * 分段（[4.4] 含三个配置域函数，段号与 data-demo.sql 装机断言 [4.6] 保持稳定）：
 *   [4.3] buildIdentity     商户 / 用户 / 应用 / 门店×5 / 码牌×9 / 路由（basic）/ 协议版本
 *   [4.4] buildChannels     mock 全局与直连凭证 + 模拟服务商 + 微信应用 + 产品生效环境
 *        buildTerminals    终端设备域（商户终端 / 通道终端台账 / 终端绑定 / 虚拟设备 / 播报打印配置）
 *        buildGateway      网关配置域（网关支付配置 / 客户端环境策略 / 收银台支付项）
 *   [4.5] buildTransactions 交易流水（订单/退款/资金流水/通知/回调，四维度打通门店/码牌/终端/method）
 *        buildGatewayOrders 网关支付订单（聚合/收银台 + trade_type=gateway 交易闭环）
 *        buildAbnormalOrders 异常订单（终态交易的人工处置台账，演示「确认成功/忽略」）
 *        buildNotices      通知全家桶（公告 6 态覆盖 / 已读记录三账号对比 / 个人消息 4 条，2026-11-10）
 *        buildSecurity     安全与平台（IP/地区黑名单 + 风险命中成对样本 + 域名校验文件 + 平台配置，2026-11-10）
 *        buildTransfers    转账静态终态单（wechat/alipay/douyin 三容器 + 转账资金凭证闭环，2026-11-10）
 *        buildP2Extras     P2 低价值项（审计三件套登录/操作/统一接口日志 + 敏感词命中，2026-11-10）
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

import { ago, dayAt, Json, json, raw, Raw } from './demo-gen.mjs'

// ---------- ID 段 ----------

const ID_BASE = 1800000000000000000n
/** 身份与配置段：1800000000000000800 起（身份 80x / 门店码牌 8xx 前段 / 设备与网关 8xx 后段~9xx） */
const idCfg = (n) => ID_BASE + BigInt(800 + n)
/** 用户协议版本演示段：1800000000000003000 ~（独立段位，避开 [2] 段导出的 2091 段协议行 id） */
const idProto = (n) => ID_BASE + BigInt(3000 + n)
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
  // 门店：第一条为默认门店；2026-11-10 扩至 5 家（新增商圈店/社区店/停用店各 1）
  stores: [
    'S1999999999990001', 'S1999999999990002', 'S1999999999990003',
    'S1999999999990004', 'S1999999999990005',
  ],
  storeMain: 'S1999999999990001',
  // 码牌：前两张随机金额、第三张固定 1 元；2026-11-10 扩至 8 张（新增 h5 两张 / 小程序高额固定 / 停用 / 商圈店专属）；
  // 2026-11-10 二次扩至 9 张（+社区店专属，保证四家启用门店各有专属码牌可承接 cashier_code 订单）
  qrCodes: [
    'DEMOQR20261101001', 'DEMOQR20261101002', 'DEMOQR20261101003', 'DEMOQR20261101004',
    'DEMOQR20261101005', 'DEMOQR20261101006', 'DEMOQR20261101007', 'DEMOQR20261101008',
    'DEMOQR20261101009',
  ],
  // 码牌第二批批次号（与首批 DEMOB20261101 构成 ≥2 批次覆盖）
  qrBatchSecond: 'DEMOB20261201',
  // 商户终端（2026-11-10 终端设备域）：每家启用门店 1 默认终端 + 总店备援 + 停用店停用终端
  terminals: [
    'TERM20261101001', 'TERM20261101002', 'TERM20261101003',
    'TERM20261101004', 'TERM20261101005', 'TERM20261101006',
  ],
  // 虚拟设备（收款播报）：已激活有回执 / 已激活无回执 / 未激活 / 停用
  virtualDevices: ['VD20261101001', 'VD20261101002', 'VD20261101003', 'VD20261101004'],
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

  // ===== 门店（第一家为默认门店，app_id 指向商户默认应用；5 家 = 启用×4 + 停用×1）=====
  // region_code 取 base_area 真实行政区划（东城/西城/朝阳/海淀/丰台）；每商户仅一条 default_store=true（部分唯一索引）
  const STORE_IDS = [idCfg(16), idCfg(17), idCfg(40), idCfg(41), idCfg(42)]
  const STORE_META = [
    { name: '演示商户总店', region: '110101', address: '北京市东城区演示路 1 号', lng: 116.4180, lat: 39.9170, status: 'enable' },
    { name: '演示商户分店', region: '110102', address: '北京市西城区演示路 22 号', lng: 116.3660, lat: 39.9120, status: 'enable' },
    { name: '演示商户商圈店', region: '110105', address: '北京市朝阳区三丰北里 2 号楼一层', lng: 116.4370, lat: 39.9230, status: 'enable' },
    { name: '演示商户社区店', region: '110108', address: '北京市海淀区成府路 45 号', lng: 116.3300, lat: 39.9920, status: 'enable' },
    { name: '演示商户停用店', region: '110106', address: '北京市丰台区蒲黄榆路 28 号', lng: 116.4300, lat: 39.8620, status: 'disabled' },
  ]
  const storeRows = ANCHORS.stores.map((storeNo, i) => {
    const m = STORE_META[i]
    gen.add('mch_store_info', {
      ...base(STORE_IDS[i]),
      mch_no: ANCHORS.mchMain,
      store_no: storeNo,
      store_name: m.name,
      app_id: ANCHORS.appMain,
      status: m.status,
      default_store: i === 0,
      region_code: m.region,
      address: m.address,
      longitude: m.lng,
      latitude: m.lat,
    })
    return { storeNo, name: m.name, status: m.status }
  })

  // ===== 码牌（9 张 = mini_app×6 + h5×3，随机/固定金额 × 启用/停用，≥2 批次；开源版 program_type 仅 h5/mini_app）=====
  // 注意：不加 allocation=true 分账码牌（已定稿），全部 allocation=false
  const QR_IDS = [idCfg(18), idCfg(19), idCfg(20), idCfg(43), idCfg(44), idCfg(45), idCfg(46), idCfg(47), idCfg(48)]
  const QR_META = [
    // 首批：两随机 + 一元快收
    { name: '演示商户-聚合收款码牌1', batch: 'DEMOB20261101', program: 'mini_app', amountType: 'random', fixed: null, status: 'enabled', storeIdx: 0 },
    { name: '演示商户-聚合收款码牌2', batch: 'DEMOB20261101', program: 'mini_app', amountType: 'random', fixed: null, status: 'enabled', storeIdx: 1 },
    { name: '演示商户-一元快收码牌', batch: 'DEMOB20261101', program: 'mini_app', amountType: 'fixed', fixed: 100, status: 'enabled', storeIdx: 1 },
    // 第二批：h5 自定义金额 / h5 固定 9.90 元 / 小程序固定 99 元 / 停用 / 商圈店专属 / 社区店专属
    { name: '演示商户-H5自定义金额码牌', batch: ANCHORS.qrBatchSecond, program: 'h5', amountType: 'random', fixed: null, status: 'enabled', storeIdx: 0 },
    { name: '演示商户-H5九块九码牌', batch: ANCHORS.qrBatchSecond, program: 'h5', amountType: 'fixed', fixed: 990, status: 'enabled', storeIdx: 0 },
    { name: '演示商户-小程序高额固定码牌', batch: ANCHORS.qrBatchSecond, program: 'mini_app', amountType: 'fixed', fixed: 9900, status: 'enabled', storeIdx: 1 },
    { name: '演示商户-已停用码牌', batch: ANCHORS.qrBatchSecond, program: 'h5', amountType: 'random', fixed: null, status: 'disabled', storeIdx: 0 },
    { name: '演示商户-商圈店专属码牌', batch: ANCHORS.qrBatchSecond, program: 'mini_app', amountType: 'random', fixed: null, status: 'enabled', storeIdx: 2 },
    { name: '演示商户-社区店专属码牌', batch: ANCHORS.qrBatchSecond, program: 'h5', amountType: 'random', fixed: null, status: 'enabled', storeIdx: 3 },
  ]
  const qrRows = ANCHORS.qrCodes.map((code, i) => {
    const m = QR_META[i]
    gen.add('device_qr_code', {
      ...base(QR_IDS[i]),
      code,
      name: m.name,
      batch_no: m.batch,
      mch_no: ANCHORS.mchMain,
      app_id: ANCHORS.appMain,
      program_type: m.program,
      amount_type: m.amountType,
      fixed_amount: m.fixed,
      status: m.status,
      store_no: storeRows[m.storeIdx].storeNo,
      allocation: false,
    })
    return { code, name: m.name, program: m.program, amountType: m.amountType, fixed: m.fixed, status: m.status, storeNo: storeRows[m.storeIdx].storeNo }
  })

  // ===== 路由策略（PayRouteChecker：每个启用应用必须有策略行）=====
  const strategyId = idCfg(21)
  gen.add('pay_route_strategy', {
    ...base(strategyId),
    app_id: ANCHORS.appMain,
    mch_no: ANCHORS.mchMain,
    mode: 'basic',
  })
  // 路由策略唯一且收敛为 basic 模式：双策略行会让运行时 PayRouteStrategyManager#findByAppId
  // 的 oneOpt 抛 TooManyResultsException（2026-11-10 撤除 scene 第二条策略行，idCfg(90~95) 段位就此空置）
  // basic 模式按「支付渠道 → 通道商户」命中；演示五个渠道全部指向 mock 通道商户
  ;['aggregate_pay', 'wechat', 'alipay', 'union_pay', 'douyin'].forEach((provider, i) => {
    gen.add('pay_route_basic_config', {
      ...base(idCfg(22 + i)),
      strategy_id: strategyId,
      provider,
      channel_mch_no: ANCHORS.mockChannelMchNo,
    })
  })

  // ===== 用户协议演示版本（挂 [2] 段既有协议行，勿新造协议；部分唯一索引只限 PUBLISHED，DRAFT/ARCHIVED 不受限）=====
  // id 用独立演示段 idProto，与 [2] 段导出的 2091 段协议行 id 天然错开
  gen.add('base_user_protocol_version', {
    ...base(idProto(1)),
    protocol_id: 2091000000000000001n, // [2] 段 WEB 用户协议行
    language: 'zh-CN',
    version_no: 2,
    version_label: 'v1.1',
    title: 'DaxPay 用户服务协议（修订草案）',
    content: '## 演示草稿\n\n这是演示环境的协议草稿版本，用于展示协议版本管理列表的 DRAFT 状态。正式发布前不会对用户生效。',
    status: 'DRAFT',
    effective_time: null,
    summary: '演示数据：下一版草稿',
  })
  gen.add('base_user_protocol_version', {
    ...base(idProto(2), ago('60 day')),
    protocol_id: 2091000000000000002n, // [2] 段 WEB 隐私政策行
    language: 'zh-CN',
    version_no: 1,
    version_label: 'v0.9',
    title: 'DaxPay 隐私政策（历史归档版）',
    content: '## 历史归档\n\n这是演示环境的协议归档版本，用于展示协议版本管理列表的 ARCHIVED 状态。',
    status: 'ARCHIVED',
    effective_time: ago('90 day'),
    summary: '演示数据：已归档的历史版本',
  })

  return {
    mchMain: {
      mchNo: ANCHORS.mchMain,
      appId: ANCHORS.appMain,
      storeRows,
      qrRows,
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
  gen.add('mock_gateway_credential', {
    ...base(1),
    merchant_private_key: MOCK_MERCHANT_PRIVATE_KEY,
    platform_public_key: MOCK_PLATFORM_PUBLIC_KEY,
  })
  // 直连产品按通道商户号取商户级凭证；gateway_base_url 留空（NULL）回落子应用 yml 配置，
  // 写死会把部署环境钉死在本机 127.0.0.1:10400（容器编排里该地址指向容器自身）。
  gen.add('mock_pay_credential', {
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

// ---------- [4.4] 终端设备域 ----------

/** 虚拟设备演示密钥（形态值，与 MCH_SECRET_KEY 同风格的固定演示串） */
const VD_SECRET = 'DemoBroadcastSecret0000000000000001'

/**
 * 终端设备域 5 表：商户终端 / 通道终端台账 / 终端绑定 / 虚拟设备（收款播报）/ 应用播报打印配置。
 *
 * 终端布局：4 家启用门店各 1 台默认终端（default_terminal 每门店部分唯一）+ 总店 1 台备援 + 停用店 1 台停用终端；
 * 通道终端全挂 mock 通道商户，type 覆盖 TerminalTypeEnum 四档（common/wechat/alipay/union），
 * status 主力 submit（通道侧已回传终端号）+ init/wait/error 各 1 条撑起状态筛选；
 * 绑定满足三条唯一约束并存：(system_terminal_no, channel_terminal_id) / channel_terminal_id 全局唯一 /
 * (system_terminal_no, channel_mch_no, type) —— 总店默认终端绑 3 个通道终端（类型互斥），其余一对一。
 */
export function buildTerminals(gen, ctx) {
  const mchNo = ctx.mchMain.mchNo
  const stores = ctx.mchMain.storeRows
  const enabledStores = stores.filter((s) => s.status === 'enable')
  const disabledStores = stores.filter((s) => s.status !== 'enable')

  // ===== 商户终端（device_terminal）=====
  const TERMINAL_META = [
    ...enabledStores.map((s) => ({ storeIdx: stores.indexOf(s), name: `${s.name}收银终端`, enable: true, isDefault: true })),
    { storeIdx: 0, name: `${stores[0].name}备援终端`, enable: true, isDefault: false },
    ...disabledStores.map((s) => ({ storeIdx: stores.indexOf(s), name: `${s.name}收银终端`, enable: false, isDefault: false })),
  ]
  const terminalRows = TERMINAL_META.map((m, i) => {
    const terminalNo = ANCHORS.terminals[i]
    gen.add('device_terminal', {
      ...base(idCfg(50 + i)),
      terminal_no: terminalNo,
      name: m.name,
      store_no: stores[m.storeIdx].storeNo,
      enable: m.enable,
      default_terminal: m.isDefault,
      mch_no: mchNo,
    })
    return { terminalNo, ...m }
  })

  // ===== 通道终端台账（device_channel_terminal）：channel/product 对齐 mock 通道商户 =====
  const CHANNEL_TERMINAL_META = [
    { type: 'common', status: 'submit', name: '总店通用终端', outNo: 'MOCKT20261101001' },
    { type: 'wechat', status: 'submit', name: '总店微信终端', outNo: 'MOCKT20261101002' },
    { type: 'alipay', status: 'submit', name: '总店支付宝终端', outNo: 'MOCKT20261101003' },
    { type: 'union', status: 'submit', name: '总店云闪付终端', outNo: 'MOCKT20261101004' },
    { type: 'common', status: 'submit', name: '分店通用终端', outNo: 'MOCKT20261101005' },
    { type: 'wechat', status: 'submit', name: '商圈店微信终端', outNo: 'MOCKT20261101006' },
    { type: 'alipay', status: 'submit', name: '社区店支付宝终端', outNo: 'MOCKT20261101007' },
    { type: 'common', status: 'init', name: '待报备终端', outNo: null },
    { type: 'wechat', status: 'wait', name: '待报送微信终端', outNo: null },
    { type: 'union', status: 'error', name: '报送失败终端', outNo: null },
  ]
  const channelTerminals = CHANNEL_TERMINAL_META.map((m, i) => {
    const id = idCfg(60 + i)
    gen.add('device_channel_terminal', {
      ...base(id),
      channel_mch_no: ANCHORS.mockChannelMchNo,
      product: 'mock_pay',
      channel: 'mock',
      type: m.type,
      name: m.name,
      out_terminal_no: m.outNo,
      status: m.status,
      error_msg: m.status === 'error' ? '演示数据：通道报送被拒绝（终端编号重复）' : null,
      mch_no: mchNo,
    })
    return { id, type: m.type, status: m.status, outNo: m.outNo }
  })

  // ===== 终端绑定（device_terminal_channel_bind）=====
  // terminalRows 下标：0=总店默认 1=分店默认 2=商圈默认 3=社区默认 4=总店备援 5=停用店
  // channelTerminals 下标：0~3=总店四类型 4=分店 5=商圈 6=社区 7~9=init/wait/error（不参与绑定）
  const BIND_PLAN = [
    { termIdx: 0, ctIdx: 0 }, { termIdx: 0, ctIdx: 1 }, { termIdx: 0, ctIdx: 2 },
    { termIdx: 4, ctIdx: 3 }, { termIdx: 1, ctIdx: 4 }, { termIdx: 2, ctIdx: 5 }, { termIdx: 3, ctIdx: 6 },
  ]
  BIND_PLAN.forEach(({ termIdx, ctIdx }, i) => {
    const term = terminalRows[termIdx]
    const ct = channelTerminals[ctIdx]
    gen.add('device_terminal_channel_bind', {
      ...base(idCfg(70 + i)),
      system_terminal_no: term.terminalNo,
      channel_terminal_id: ct.id,
      mch_no: mchNo,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      channel: 'mock',
      type: ct.type,
    })
  })

  // ===== 虚拟设备（device_virtual）：4 台覆盖 激活+播报回执 / 激活无回执 / 未激活 / 停用 =====
  // 总店音箱行对象保留引用（vdMainRow）：buildTransactions 生成真实交易后回填
  // last_pay_ack_msg_id = `${tradeNo}:${deviceNo}`（AudioBroadcastMessage 支付成功工厂的确定性去重格式）
  const vdMainRow = {
    ...base(idCfg(80), ago('40 day')),
    name: '总店播报音箱',
    device_no: ANCHORS.virtualDevices[0],
    batch_no: 'DEMOVD20261101',
    mch_no: mchNo,
    app_id: ctx.mchMain.appId,
    store_no: stores[0].storeNo,
    enable: true,
    secret: VD_SECRET,
    last_pay_ack_time: ago('30 minute'),
    last_pay_ack_msg_id: 'DEMOACK20261101001',
    last_pay_ack_ok: true,
    last_online_time: ago('5 minute'),
    activated_time: ago('40 day'),
  }
  gen.add('device_virtual', vdMainRow)
  gen.add('device_virtual', {
    ...base(idCfg(81), ago('38 day')),
    name: '总店备用播报音箱',
    device_no: ANCHORS.virtualDevices[1],
    batch_no: 'DEMOVD20261101',
    mch_no: mchNo,
    app_id: ctx.mchMain.appId,
    store_no: stores[0].storeNo,
    enable: true,
    secret: VD_SECRET,
    last_pay_ack_time: null,
    last_pay_ack_msg_id: null,
    last_pay_ack_ok: null,
    last_online_time: ago('2 hour'),
    activated_time: ago('38 day'),
  })
  gen.add('device_virtual', {
    ...base(idCfg(82), ago('10 day')),
    name: '分店播报音箱（未激活）',
    device_no: ANCHORS.virtualDevices[2],
    batch_no: 'DEMOVD20261101',
    mch_no: mchNo,
    app_id: ctx.mchMain.appId,
    store_no: stores[1].storeNo,
    enable: true,
    secret: null,
    last_pay_ack_time: null,
    last_pay_ack_msg_id: null,
    last_pay_ack_ok: null,
    last_online_time: null,
    activated_time: null,
  })
  gen.add('device_virtual', {
    ...base(idCfg(83), ago('35 day')),
    name: '停用店播报音箱',
    device_no: ANCHORS.virtualDevices[3],
    batch_no: 'DEMOVD20261101',
    mch_no: mchNo,
    app_id: ctx.mchMain.appId,
    store_no: stores[4].storeNo,
    enable: false,
    secret: VD_SECRET,
    last_pay_ack_time: null,
    last_pay_ack_msg_id: null,
    last_pay_ack_ok: null,
    last_online_time: null,
    activated_time: ago('35 day'),
  })

  // ===== 应用播报/打印配置（device_mch_app_config）：demo 应用一条，播报+打印全开 =====
  gen.add('device_mch_app_config', {
    ...base(idCfg(84)),
    mch_no: mchNo,
    app_id: ctx.mchMain.appId,
    broadcast_enable: true,
    print_enable: true,
    template_code: 'standard',
    store_strict_match: false,
  })

  // ===== 交易段四维度打通用的句柄（挂在 ctx.device，buildTransactions/buildGatewayOrders 消费）=====
  // terminalHandles: 每台商户终端 + 其绑定的通道终端（出号/类型/状态），供订单 terminal_no / out_terminal_no 解析
  ctx.device = {
    vdMainRow,
    terminalHandles: terminalRows.map((t) => ({
      terminalNo: t.terminalNo,
      storeNo: stores[t.storeIdx].storeNo,
      enable: t.enable,
      isDefault: t.isDefault,
      binds: BIND_PLAN.filter((p) => p.termIdx === terminalRows.indexOf(t))
        .map((p) => channelTerminals[p.ctIdx])
        .map((c) => ({ type: c.type, status: c.status, outNo: c.outNo })),
    })),
  }

  return { terminalRows, channelTerminals }
}

// ---------- [4.4] 网关配置域 ----------

/**
 * 网关配置域 3 表：网关支付配置主表 / 客户端环境策略 / 收银台支付项。
 *
 * level='auto' 跟随通道路由（与 basic 路由 5 渠道覆盖自洽，运行时按 ClientEnvEnum 默认推导不读子表）；
 * client_env 行的 method 与 defaultMethodCode 推导值一致，作为配置页展示/预填，切 METHOD 级别后语义即生效；
 * 收银台支付项 icon 取 PayProviderEnum code（与前端 CASHIER_ICON_OPTIONS 对齐），
 * METHOD 模式只填 method、DIRECT 模式只填 channel_mch_no+capability（与保存端归一化互斥规则一致）。
 */
export function buildGateway(gen, ctx) {
  const mchNo = ctx.mchMain.mchNo
  const appId = ctx.mchMain.appId

  // ===== 网关支付配置主表（pay_gateway_pay_config）：app_id 唯一 =====
  const gatewayConfigId = idCfg(100)
  gen.add('pay_gateway_pay_config', {
    ...base(gatewayConfigId),
    mch_no: mchNo,
    app_id: appId,
    level: 'auto',
    auto_launch: true,
  })

  // ===== 客户端环境策略（pay_gateway_pay_client_env）：client_env × pay_form =====
  // 唯一约束 (config_id, client_env, pay_form)；browser 仅收银台 H5 使用，聚合扫码配置不配 browser
  ;[
    { env: 'wechat', form: 'h5', method: 'wechat_jsapi' },
    { env: 'wechat', form: 'mini', method: 'wechat_mini' },
    { env: 'alipay', form: 'h5', method: 'alipay_qr' },
    { env: 'alipay', form: 'mini', method: 'alipay_jsapi' },
    { env: 'union_pay', form: 'h5', method: 'union_jsapi' },
    { env: 'douyin', form: 'h5', method: 'douyin_jsapi' },
  ].forEach((m, i) => {
    gen.add('pay_gateway_pay_client_env', {
      ...base(idCfg(101 + i)),
      mch_no: mchNo,
      config_id: gatewayConfigId,
      client_env: m.env,
      pay_form: m.form,
      method: m.method,
      channel_mch_no: null,
      capability: null,
    })
  })

  // ===== 收银台支付项（pay_gateway_cashier_item）：h5/web/mini 三台 × 典型客户端环境 =====
  // WEB 为扁平列表 client_env 置空；MINI 四档无 browser；H5 桶含 browser
  ;[
    { type: 'h5', env: 'browser', name: '微信扫码支付', icon: 'wechat', recommend: false, sort: 1, mode: 'method', method: 'wechat_qr' },
    { type: 'h5', env: 'browser', name: '支付宝扫码支付', icon: 'alipay', recommend: false, sort: 2, mode: 'method', method: 'alipay_qr' },
    { type: 'h5', env: 'wechat', name: '微信支付', icon: 'wechat', recommend: true, sort: 1, mode: 'method', method: 'wechat_jsapi' },
    { type: 'h5', env: 'alipay', name: '支付宝', icon: 'alipay', recommend: false, sort: 1, mode: 'method', method: 'alipay_jsapi' },
    { type: 'web', env: null, name: '微信扫码', icon: 'wechat', recommend: true, sort: 1, mode: 'method', method: 'wechat_qr' },
    { type: 'web', env: null, name: '支付宝扫码', icon: 'alipay', recommend: false, sort: 2, mode: 'method', method: 'alipay_qr' },
    { type: 'mini', env: 'wechat', name: '微信支付', icon: 'wechat', recommend: true, sort: 1, mode: 'direct', capability: 'wechat_mini' },
    { type: 'mini', env: 'alipay', name: '支付宝', icon: 'alipay', recommend: false, sort: 2, mode: 'direct', capability: 'alipay_jsapi' },
  ].forEach((m, i) => {
    gen.add('pay_gateway_cashier_item', {
      ...base(idCfg(110 + i)),
      app_id: appId,
      cashier_type: m.type,
      client_env: m.env,
      name: m.name,
      icon: m.icon,
      recommend: m.recommend,
      sort_no: m.sort,
      resolve_mode: m.mode,
      method: m.mode === 'method' ? m.method : null,
      channel_mch_no: m.mode === 'direct' ? ANCHORS.mockChannelMchNo : null,
      capability: m.mode === 'direct' ? m.capability : null,
      mch_no: mchNo,
    })
  })

  return { gatewayConfigId }
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

/** mch_api 直连支付方式（mock_pay 支持的方式子集，provider 与 PayMethodEnum 归属一致） */
const API_ROUTES = [
  { method: 'wechat_qr', capability: 'wechat_qr', provider: 'wechat' },
  { method: 'alipay_qr', capability: 'alipay_qr', provider: 'alipay' },
  { method: 'union_qr', capability: 'union_qr', provider: 'union_pay' },
  { method: 'douyin_qr', capability: 'douyin_qr', provider: 'douyin' },
  { method: 'wechat_jsapi', capability: 'wechat_jsapi', provider: 'wechat' },
]

/**
 * 码牌/聚合支付方式解析表 —— Java 侧 CodePayFormEnum#defaultMethodCode 的 JS 镜像
 * （码牌 program_type: mini_app→mini / h5→h5；与 GatewayPayConfigResolveService AUTO 档一致，
 *  改 Java 默认表时必须同步此处，勿凭记忆臆造）。
 */
const CODE_DEFAULT_METHOD = {
  mini: { wechat: 'wechat_mini', alipay: 'alipay_jsapi', union_pay: 'union_jsapi', douyin: 'douyin_jsapi' },
  h5: { wechat: 'wechat_jsapi', alipay: 'alipay_qr', union_pay: 'union_jsapi', douyin: 'douyin_jsapi' },
}

/** 聚合扫码客户端环境（browser 仅收银台 H5 使用，不进码牌解析） */
const AGGREGATE_CLIENT_ENVS = ['wechat', 'alipay', 'union_pay', 'douyin']

/** method → 支付渠道（PayMethodEnum 归属规则的镜像） */
function providerOfMethod(method) {
  if (method.startsWith('wechat')) return 'wechat'
  if (method.startsWith('alipay')) return 'alipay'
  if (method.startsWith('union')) return 'union_pay'
  return 'douyin'
}

/**
 * 订单状态 → 交易状态（PayFundStatusEnum code：成功 success / 失败 fail / 关闭 close / 处理中 processing；
 * 2026-11-10 修正历史脏值 closed/progress → close/processing，否则异常订单「确认成功」CAS 前置态校验必失败）
 */
const TRADE_STATUS_MAP = {
  paid: 'success',
  failed: 'fail',
  closed: 'close',
  expired: 'close',
  wait_pay: 'processing',
}

/** 启用门店加权抽取（顺序同 STORE_META：总店 45% / 分店 25% / 商圈店 18% / 社区店 12%；停用店永不参与） */
const STORE_WEIGHTS = [0.45, 0.25, 0.18, 0.12]

/** 门店区域示例 IP 池（RFC5737 文档段 + 少量内网段，按门店轮换；不再全量 127.0.0.1） */
const STORE_IP_POOLS = [
  ['203.0.113.10', '203.0.113.11', '192.168.1.35'],
  ['203.0.113.24', '198.51.100.7', '192.168.1.36'],
  ['198.51.100.23', '198.51.100.24'],
  ['203.0.113.99', '192.168.1.37'],
]

/** 在给定 timestamptz 表达式上加秒（虚拟设备回执 = 支付时间 + 5 秒等秒级偏移） */
const addSeconds = (time, seconds) => raw(`(${time.sql}) + interval '${seconds} second'`)

/** timestamptz 表达式 → 东八区墙钟字符串 SQL（通知快照时间字段，与 unipay 契约序列化同口径） */
const bjTime = (time) => raw(`to_char((${time.sql}) AT TIME ZONE 'Asia/Shanghai', 'YYYY-MM-DD HH24:MI:SS')`)

/**
 * 通知快照 content：真实订单字段 JSON 文本（camelCase 键，对齐 NormalPayOrderResult 序列化形态），
 * Raw 时间值用 SQL 字符串拼接渲染成装机相对表达式（快照时间随装机鲜活），并保留 demo 标记。
 */
function snapshotContent(pairs, { demoMark = true } = {}) {
  const q = (s) => String(s).replace(/'/g, "''")
  const parts = []
  let staticBuf = '{'
  let first = true
  for (const [k, v] of pairs) {
    const sep = first ? '' : ','
    first = false
    if (v instanceof Raw) {
      staticBuf += `${sep}"${k}":`
      parts.push(staticBuf)
      staticBuf = ''
      parts.push(v.sql)
    } else {
      // 值不在此处转义单引号——最终统一按 SQL 字面量转义（两处都转会叠成四引号）
      staticBuf += `${sep}"${k}":${JSON.stringify(v ?? null)}`
    }
  }
  if (demoMark) staticBuf += `${first ? '' : ','}"demo":true`
  staticBuf += '}'
  parts.push(staticBuf)
  return raw(parts.map((p, i) => (i % 2 === 0 ? `'${q(p)}'` : `(${p})`)).join(' || '))
}

/**
 * 交易流水：近 30 天历史 + 今天自然流量 + 3 笔现场可操作单
 * 一致性由 checkConsistency 保证：订单 ↔ 交易 ↔ 资金流水 ↔ 通知任务 逐层对齐
 * （断言在全部交易域 builder 执行完后由 export-sql 统一触发，本函数只造数）。
 *
 * 订单四维度打通（2026-11-10）：
 *   门店 —— STORE_WEIGHTS 加权抽取启用门店（停用店 0005 永不参与）；
 *   码牌 —— cashier_code 单从所选门店的启用码牌轮转抽取，回指 qr_code / 门店 / 标题；
 *   终端 —— 按门店默认终端写 terminal_no，经绑定链（submit 通道终端）解析 out_terminal_no；
 *   方式 —— 码牌单按 program_type → payForm → client_env 走 CODE_DEFAULT_METHOD，mch_api 才允许直连 qr 方式。
 *
 * 随机种子固定为 20261115：该种子下规模落在设计目标上（约 168 单 / 15 退款 / ≥100 通知任务），
 * 换种子只影响分布不影响正确性（一致性断言统一在构建末段自检）。
 *
 * 返回 tx 句柄：计数 + id/单号续号器 + 终态交易引用，供 buildGatewayOrders / buildAbnormalOrders 复用。
 */
export function buildTransactions(gen, ctx, seed = 20261115) {
  const mch = ctx.mchMain
  const rng = makeRng(seed)
  let seq = 0
  let noSeq = 0
  const nextId = () => idTx(seq++)
  const nextNo = (prefix) => `${prefix}${String(++noSeq).padStart(8, '0')}`

  const tx = {
    orders: 0, trades: 0, refunds: 0, notices: 0, records: 0, flows: 0,
    gwOrders: 0, gwTrades: 0, abnormals: 0,
    nextId, nextNo,
    // 终态交易引用（异常订单台账动态回指，禁止硬编码——随机序列变化会让旧号漂移）
    closedResults: [],
    failedResults: [],
    paidResults: [],
    successRefunds: [],
  }

  // ===== 四维度抽取器 =====
  const enabledIdx = mch.storeRows.map((s, i) => (s.status === 'enable' ? i : -1)).filter((i) => i >= 0)
  /** 加权抽取启用门店下标（STORE_WEIGHTS 顺序对应启用店；兜底最后一家） */
  function pickStoreIdx() {
    const r = rng()
    let acc = 0
    for (let i = 0; i < enabledIdx.length; i++) {
      acc += STORE_WEIGHTS[i] ?? 0
      if (r < acc) return enabledIdx[i]
    }
    return enabledIdx[enabledIdx.length - 1]
  }

  /** 各门店的启用码牌（轮转指针保证每张启用码牌都能分到交易） */
  const enabledQrsByStore = new Map()
  const qrCursor = new Map()
  for (const q of mch.qrRows) {
    if (q.status !== 'enabled') continue
    if (!enabledQrsByStore.has(q.storeNo)) enabledQrsByStore.set(q.storeNo, [])
    enabledQrsByStore.get(q.storeNo).push(q)
  }
  function pickQr(storeNo) {
    const list = enabledQrsByStore.get(storeNo)
    if (!list || !list.length) return null
    const i = (qrCursor.get(storeNo) ?? 0) % list.length
    qrCursor.set(storeNo, i + 1)
    return list[i]
  }

  /** 终端绑定链解析：优先 provider 对应类型（wechat/alipay/union），否则 common 型；无匹配绑定时不出号 */
  function pickBind(term, provider) {
    const submit = term.binds.filter((b) => b.status === 'submit' && b.outNo)
    const typeWant = provider === 'union_pay' ? 'union' : provider
    return submit.find((b) => b.type === typeWant) ?? submit.find((b) => b.type === 'common') ?? null
  }

  /** 门店区域示例 IP 轮换 */
  function pickClientIp(storeIdx) {
    const pool = STORE_IP_POOLS[storeIdx] ?? STORE_IP_POOLS[0]
    return pool[Math.floor(rng() * pool.length)]
  }

  /**
   * 商户通知任务 + 发送记录（三种形态）：
   *   success 一次送达 / retry 首次失败后重试成功 / failed 始终不可达（保留待重试）
   * content 为订单/退款真实快照（snapshotContent 产物），不再是占位 JSON。
   */
  function noticeTask({ id, event, bizId, bizNo, time, mode, content }) {
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
      content: content ?? snapshotContent({ bizNo, event }),
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
    tx.notices += 1
    tx.records += retry ? 2 : 1
  }

  /** 造一笔订单（含统一交易与派生产物）；agoMinutes 给定时用"N 分钟前"（保证落在过去） */
  function makeOrder({ dayOffset, hour, minute, status, amount, route, agoMinutes, titleOverride, sourceOverride, storeIdxOverride }) {
    const orderId = nextId()
    const tradeId = nextId()
    const orderNo = nextNo('DEMO')
    const tradeNo = nextNo('DEMOT')
    // 门店维度：加权抽取启用门店（停用店永不参与）
    const storeIdx = storeIdxOverride ?? pickStoreIdx()
    const storeNo = mch.storeRows[storeIdx].storeNo
    // 来源与码牌维度：cashier_code 单回指所选门店的启用码牌（轮转保证覆盖）
    let source = sourceOverride ?? (rng() < 0.6 ? 'cashier_code' : 'mch_api')
    let qr = null
    if (source === 'cashier_code') {
      qr = pickQr(storeNo)
      if (!qr) source = 'mch_api' // 该门店无启用码牌时兜底（四家启用店均有专属码牌，正常不触发）
    }
    // 方式维度：码牌单按 program_type → payForm → client_env 走默认方式表；mch_api 走直连方式
    let method, capability, provider
    if (qr) {
      const clientEnv = AGGREGATE_CLIENT_ENVS[Math.floor(rng() * AGGREGATE_CLIENT_ENVS.length)]
      const form = qr.program === 'mini_app' ? 'mini' : 'h5'
      method = CODE_DEFAULT_METHOD[form][clientEnv]
      capability = method
      provider = providerOfMethod(method)
    } else {
      const r = route ?? API_ROUTES[Math.floor(rng() * API_ROUTES.length)]
      method = r.method
      capability = r.capability
      provider = r.provider
    }
    // 固定金额码牌的订单金额取码牌面额（一元快收 / 九块九 / 小程序高额固定）
    if (qr && qr.fixed != null) amount = qr.fixed
    const title = titleOverride ?? (qr ? `${qr.name}收款: ${(amount / 100).toFixed(2)}元` : `演示订单-${provider}`)
    // 终端维度：mch_api 单约 25% 不带终端（贴近真实 API 调用），其余按门店默认终端 + 绑定链出号
    let terminalNo = null
    let outTerminalNo = null
    if (!(source === 'mch_api' && rng() < 0.25)) {
      const term = ctx.device.terminalHandles.find((t) => t.storeNo === storeNo && t.isDefault && t.enable)
      if (term) {
        terminalNo = term.terminalNo
        const bind = pickBind(term, provider)
        outTerminalNo = bind ? bind.outNo : null
      }
    }
    const clientIp = pickClientIp(storeIdx)
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
      store_no: storeNo,
      channel: 'mock',
      product: 'mock_pay',
      method,
      capability,
      provider,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      client_ip: clientIp,
      terminal_no: terminalNo,
      out_terminal_no: outTerminalNo,
      qr_code: qr ? qr.code : null,
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
      store_no: storeNo,
      out_order_no: `MOCK${tradeNo}`,
      app_id: mch.appId,
      mch_no: mch.mchNo,
      provider,
      channel: 'mock',
      title,
    }
    gen.add('pay_trade', tradeRow)

    tx.orders += 1
    tx.trades += 1

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
        provider,
        channel_mch_no: ANCHORS.mockChannelMchNo,
        out_order_no: `MOCK${tradeNo}`,
        finish_time: payTime,
      })
      tx.flows += 1
      noticeTask({
        id: nextId(), event: 'pay.success', bizId: orderId, bizNo: orderNo, time: payTime,
        mode: rng() < 0.1 ? 'retry' : 'success',
        // 支付成功通知快照（NormalPayOrderResult 关键字段 + 资金交易号）
        content: snapshotContent([
          ['mchNo', mch.mchNo],
          ['appId', mch.appId],
          ['orderNo', orderNo],
          ['bizOrderNo', `BIZ${orderNo}`],
          ['source', source],
          ['title', title],
          ['status', status],
          ['amount', amount],
          ['channel', 'mock'],
          ['method', method],
          ['provider', provider],
          ['storeNo', storeNo],
          ['terminalNo', terminalNo],
          ['outTerminalNo', outTerminalNo],
          ['qrCode', qr ? qr.code : null],
          ['tradeNo', tradeNo],
          ['outOrderNo', `MOCK${tradeNo}`],
          ['payTime', bjTime(payTime)],
        ]),
      })
    } else if (status === 'failed') {
      noticeTask({
        id: nextId(), event: 'pay.fail', bizId: orderId, bizNo: orderNo, time: createTime,
        mode: 'failed',
        content: snapshotContent([
          ['orderNo', orderNo], ['tradeNo', tradeNo], ['amount', amount],
          ['status', status], ['provider', provider], ['storeNo', storeNo], ['event', 'pay.fail'],
        ]),
      })
    } else if (status === 'closed') {
      noticeTask({
        id: nextId(), event: 'pay.close', bizId: orderId, bizNo: orderNo, time: closeTime,
        mode: 'success',
        content: snapshotContent([
          ['orderNo', orderNo], ['tradeNo', tradeNo], ['amount', amount],
          ['status', status], ['provider', provider], ['storeNo', storeNo], ['event', 'pay.close'],
        ]),
      })
    } else if (status === 'expired') {
      noticeTask({
        id: nextId(), event: 'pay.timeout', bizId: orderId, bizNo: orderNo, time: closeTime,
        mode: 'success',
        content: snapshotContent([
          ['orderNo', orderNo], ['tradeNo', tradeNo], ['amount', amount],
          ['status', status], ['provider', provider], ['storeNo', storeNo], ['event', 'pay.timeout'],
        ]),
      })
    }

    const result = { orderId, tradeId, orderNo, tradeNo, amount, status, payTime, tradeRow, storeNo, storeIdx, provider, method, source, clientIp }
    if (status === 'paid') tx.paidResults.push(result)
    if (status === 'closed' || status === 'expired') tx.closedResults.push(result)
    if (status === 'failed') tx.failedResults.push(result)
    return result
  }

  /** 退款单（含成功退款资金流水与通知）；退款成功时下调原交易可退余额；门店/IP 继承原订单 */
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
      out_order_no: `MOCK${o.tradeNo}`,
      out_refund_no: `MOCKR${refundNo}`,
      order_amount: o.amount,
      amount: refundAmount,
      reason: '演示数据：用户申请退款',
      status,
      channel: 'mock',
      product: 'mock_pay',
      channel_mch_no: ANCHORS.mockChannelMchNo,
      store_no: o.storeNo,
      mch_no: mch.mchNo,
      finish_time: finishTime,
      trade_type: 'normal',
      client_ip: o.clientIp,
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
      tx.flows += 1
      noticeTask({
        id: nextId(), event: 'refund.success', bizId: refundId, bizNo: refundNo, time: finishTime,
        mode: 'success',
        // 退款成功通知快照（RefundOrderResult 关键字段）
        content: snapshotContent([
          ['mchNo', mch.mchNo],
          ['appId', mch.appId],
          ['refundNo', refundNo],
          ['bizRefundNo', `BIZ${refundNo}`],
          ['tradeNo', o.tradeNo],
          ['bizOrderNo', o.orderNo],
          ['outRefundNo', `MOCKR${refundNo}`],
          ['title', `演示退款-${o.tradeNo}`],
          ['amount', refundAmount],
          ['status', 'success'],
          ['storeNo', o.storeNo],
          ['finishTime', bjTime(finishTime)],
        ]),
      })
    } else if (status === 'fail') {
      noticeTask({
        id: nextId(), event: 'refund.fail', bizId: refundId, bizNo: refundNo, time: createTime,
        mode: 'failed',
        content: snapshotContent([
          ['refundNo', refundNo], ['tradeNo', o.tradeNo], ['amount', refundAmount],
          ['status', status], ['storeNo', o.storeNo], ['event', 'refund.fail'],
        ]),
      })
    } else if (status === 'close') {
      noticeTask({
        id: nextId(), event: 'refund.close', bizId: refundId, bizNo: refundNo, time: createTime,
        mode: 'success',
        content: snapshotContent([
          ['refundNo', refundNo], ['tradeNo', o.tradeNo], ['amount', refundAmount],
          ['status', status], ['storeNo', o.storeNo], ['event', 'refund.close'],
        ]),
      })
    }
    // progress：退款中，不产生终态通知

    tx.refunds += 1
    const refundResult = { refundId, refundNo, refundAmount, status, createTime, finishTime, tradeNo: o.tradeNo }
    if (status === 'success') tx.successRefunds.push(refundResult)
    return refundResult
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
      // 金额 1~500 元（分）：80% 小额、15% 中额、5% 大额（固定金额码牌单会被码牌面额覆盖）
      const amount = a < 0.8
        ? 100 + Math.floor(rng() * 9900)
        : a < 0.95
          ? 10000 + Math.floor(rng() * 20000)
          : 30000 + Math.floor(rng() * 20000)
      const o = makeOrder({ dayOffset: d, hour, minute, status, amount })

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
    const o = makeOrder({ status, amount, agoMinutes: 55 + i * 65 })
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
      agoMinutes: minsAgo,
      sourceOverride: 'mch_api',
      titleOverride: '现场演示订单（可退款/可关单）',
    })
  })

  // ===== 虚拟设备 001 播报回执回填（B7）=====
  // msgId = `${tradeNo}:${deviceNo}`（AudioBroadcastMessage 支付成功工厂的确定性去重格式），
  // 回执时间 = 该笔总店交易的支付时间 + 5 秒；行对象在 buildTerminals 已登记，此处事后改写。
  const vdTrade = [...tx.paidResults].reverse().find((o) => o.storeIdx === 0) ?? tx.paidResults[tx.paidResults.length - 1]
  if (vdTrade) {
    ctx.device.vdMainRow.last_pay_ack_msg_id = `${vdTrade.tradeNo}:${ANCHORS.virtualDevices[0]}`
    ctx.device.vdMainRow.last_pay_ack_time = addSeconds(vdTrade.payTime, 5)
    ctx.device.vdMainRow.last_pay_ack_ok = true
  }

  // ===== 通道回调记录（B6：4 条回指真实交易/退款，验签失败与未找到保留虚构通道单号）=====
  // PayCallbackRecordService 语义：支付回调 trade_no=交易号、退款回调 trade_no=退款号；out_trade_no 为通道侧单号
  const recentPaid = [...tx.paidResults].reverse()
  const cbRefund = tx.successRefunds[tx.successRefunds.length - 1]
  const callbacks = [
    { status: 'success', type: 'pay', tip: '通道支付回调处理成功', trade: recentPaid[0] },
    { status: 'success', type: 'pay', tip: '通道支付回调处理成功', trade: recentPaid[1] },
    { status: 'fail', type: 'pay', tip: '验签失败：签名不匹配', trade: null },
    { status: 'exception', type: 'pay', tip: '解析异常：报文格式错误', trade: recentPaid[2] },
    { status: 'ignore', type: 'refund', tip: '重复回调已忽略', refund: cbRefund },
    { status: 'not_found', type: 'pay', tip: '未找到对应交易', trade: null },
  ]
  callbacks.forEach((c, i) => {
    const tradeNo = c.trade ? c.trade.tradeNo : c.refund ? c.refund.refundNo : null
    const outTradeNo = c.trade
      ? `MOCK${c.trade.tradeNo}`
      : c.refund ? `MOCKR${c.refund.refundNo}` : `MOCKCALLBACK${String(i + 1).padStart(4, '0')}`
    // 回调时间贴着所回指交易的发生时刻（虚构单保持"i 天前"）
    const time = c.trade
      ? addSeconds(c.trade.payTime, 10)
      : c.refund ? addSeconds(c.refund.finishTime, 10) : ago(`${i + 1} day`)
    gen.add('pay_callback_record', {
      ...base(nextId(), time),
      mch_no: mch.mchNo,
      app_id: mch.appId,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      trade_no: tradeNo,
      out_trade_no: outTradeNo,
      product: 'mock_pay',
      callback_type: c.type,
      notify_info: JSON.stringify({ demo: true, note: c.tip, seq: i + 1, outTradeNo }),
      status: c.status,
      error_msg: c.status === 'success' ? null : c.tip,
    })
  })

  return tx
}

// ---------- [4.5] 网关支付订单（B4） ----------

/**
 * 网关支付订单（pay_gateway_order）：聚合扫码一码多付 + 收银台点选支付，约 10 笔覆盖六态
 * （paid×4 / paying×1 / wait_pay×2（过期时间在未来，可现场演示关单）/ expired / failed / closed）。
 *
 * 闭环口径（与 GatewayPayOrder 实体注释一致）：
 *   预下单创建容器 → paid/paying/failed 建有 pay_trade(trade_type='gateway')，wait_pay/closed/expired 无交易；
 *   paid 单配 trade(success) + 等额 pay_fund_flow + 商户通知，与 normal 交易共用金额守恒/流水唯一性断言。
 * 枚举取值来源：GatewayPayTypeEnum(cashier/aggregate)、GatewayOrderStatusEnum、TradeSourceEnum(cashier/aggress_pay)、
 * ClientEnvEnum、CodePayFormEnum（聚合扫码为 H5 运行形态）；id/单号续用 buildTransactions 的 tx 序列防撞号。
 */
export function buildGatewayOrders(gen, ctx, tx, seed = 20261116) {
  const mch = ctx.mchMain
  const rng = makeRng(seed)
  const nextId = tx.nextId
  const nextNo = tx.nextNo
  const enabledIdx = mch.storeRows.map((s, i) => (s.status === 'enable' ? i : -1)).filter((i) => i >= 0)
  const pickStoreIdx = () => {
    const r = rng()
    let acc = 0
    for (let i = 0; i < enabledIdx.length; i++) {
      acc += STORE_WEIGHTS[i] ?? 0
      if (r < acc) return enabledIdx[i]
    }
    return enabledIdx[enabledIdx.length - 1]
  }
  const pickClientIp = (storeIdx) => {
    const pool = STORE_IP_POOLS[storeIdx] ?? STORE_IP_POOLS[0]
    return pool[Math.floor(rng() * pool.length)]
  }

  /**
   * 商户通知任务（网关版）：bizId/bizNo 取网关订单，快照为网关订单字段（GatewayPayOrderResult 形态）。
   */
  function gwNotice({ event, orderId, orderNo, time, mode, snapshot }) {
    const retry = mode === 'retry'
    const failed = mode === 'failed'
    const id = nextId()
    gen.add('mch_notice_task', {
      ...base(id, time),
      mch_no: mch.mchNo,
      app_id: mch.appId,
      biz_id: orderId,
      biz_no: orderNo,
      event,
      source: 'order',
      content_mode: 'snapshot',
      content: snapshot,
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
        request_digest: `demo-${orderNo}-${reqCount}`,
      })
    }
    if (retry) {
      record(time, 1, false)
      record(addMinutes(time, 1), 2, true)
    } else {
      record(time, 1, !failed)
    }
    tx.notices += 1
    tx.records += retry ? 2 : 1
  }

  /** 网关订单计划：type(cashier/aggregate) × 状态 × 客户端环境；env 为空时按类型推导 */
  const GW_PLAN = [
    { type: 'aggregate', env: 'wechat', day: 9, hour: 10, status: 'paid' },
    { type: 'aggregate', env: 'alipay', day: 7, hour: 14, status: 'paid' },
    { type: 'aggregate', env: 'union_pay', day: 4, hour: 16, status: 'paid' },
    { type: 'cashier', day: 6, hour: 20, status: 'paid' },
    { type: 'aggregate', env: 'wechat', day: 1, hour: 12, status: 'paying' },
    // 今天的两笔待支付单：过期时间在未来（now()+30min），现场可演示关单/继续支付
    { type: 'cashier', agoMinutes: 12, status: 'wait_pay' },
    { type: 'aggregate', env: 'alipay', agoMinutes: 25, status: 'wait_pay' },
    { type: 'aggregate', env: 'wechat', day: 3, hour: 9, status: 'expired' },
    { type: 'cashier', day: 5, hour: 19, status: 'failed' },
    { type: 'aggregate', env: 'union_pay', day: 8, hour: 13, status: 'closed' },
  ]

  GW_PLAN.forEach((p, idx) => {
    const orderId = nextId()
    const orderNo = nextNo('DEMOGW')
    const storeIdx = pickStoreIdx()
    const storeNo = mch.storeRows[storeIdx].storeNo
    const clientIp = pickClientIp(storeIdx)
    // 网关类型与来源对应（GatewayPayOrder#source：与 gatewayType 对应 cashier/aggress_pay）
    const gatewayType = p.type
    const source = p.type === 'cashier' ? 'cashier' : 'aggress_pay'
    // 客户端环境/设备/方式：收银台=浏览器 PC 点选扫码方式；聚合=移动端宿主环境走默认方式表
    const clientEnv = p.type === 'cashier' ? 'browser' : p.env
    const device = p.type === 'cashier' ? 'pc' : 'mobile'
    const method = p.type === 'cashier'
      ? (rng() < 0.5 ? 'wechat_qr' : 'alipay_qr')
      : CODE_DEFAULT_METHOD.h5[p.env]
    const capability = method
    const provider = providerOfMethod(method)
    const amount = 1000 + Math.floor(rng() * 49000)
    const createTime = p.agoMinutes != null ? ago(`${p.agoMinutes} minute`) : dayAt(p.day, p.hour, 30)
    const payTime = p.status === 'paid' ? addMinutes(createTime, 1 + Math.floor(rng() * 3)) : null
    const closeTime = ['closed', 'expired', 'failed'].includes(p.status) ? addMinutes(createTime, 30) : null
    // wait_pay 过期时间在未来（现场可演示关单）；expired 单的过期时间落在过去
    const expiredTime = p.status === 'wait_pay' ? later('30 minute') : p.status === 'expired' ? addMinutes(createTime, 30) : null
    // 已支付/支付中/失败单在支付发起后即有通道路由信息；未支付单保留预下单形态
    const routed = ['paid', 'paying', 'failed'].includes(p.status)
    const openid = /jsapi|mini/.test(method) ? `oDemoGateway${String(idx + 1).padStart(10, '0')}` : null
    const title = p.type === 'cashier' ? `收银台收款 #${idx + 1}` : `聚合扫码收款 #${idx + 1}`

    gen.add('pay_gateway_order', {
      ...base(orderId, createTime),
      order_no: orderNo,
      biz_order_no: `BIZ${orderNo}`,
      gateway_type: gatewayType,
      source,
      title,
      description: p.type === 'cashier' ? '演示数据：收银台 H5 点选支付下单' : '演示数据：聚合码牌扫码预下单',
      status: p.status,
      notify_url: NOTIFY_URL,
      return_url: p.type === 'cashier' ? 'https://demo.example.com/cashier/return' : null,
      attach: idx === 0 ? '演示附加参数' : null,
      expired_time: expiredTime,
      amount,
      channel: routed ? 'mock' : null,
      method: routed ? method : null,
      product: routed ? 'mock_pay' : null,
      openid,
      client_env: clientEnv,
      device,
      pay_time: payTime,
      close_time: closeTime,
      channel_mch_no: routed ? ANCHORS.mockChannelMchNo : null,
      capability: routed ? capability : null,
      channel_app_id: routed && provider === 'wechat' ? ANCHORS.mockWxAppId : null,
      provider: p.status === 'paid' ? provider : null,
      buyer_id: p.status === 'paid' ? (provider === 'alipay' ? `2088000${String(idx + 1).padStart(10, '0')}` : `oDemoBuyer${String(idx + 1).padStart(10, '0')}`) : null,
      trade_product: p.status === 'paid' ? 'mock_pay' : null,
      trade_way: p.status === 'paid' ? method : null,
      bank_type: p.status === 'paid' && provider === 'wechat' ? 'CFT' : null,
      pay_body: p.status === 'paid' && /jsapi|mini/.test(method) ? `wx-demo-prepay-${orderNo}` : null,
      pay_body_type: p.status === 'paid' && /jsapi|mini/.test(method) ? 'jsapi' : null,
      trans_order_no: null,
      relation_order_no: routed ? orderNo : null,
      extra_param: null,
      app_id: mch.appId,
      goods_detail: idx === 3 ? json([{ goodsId: 'DEMO-G001', goodsName: '演示商品A', quantity: 1, unitPrice: amount, category: 'demo' }]) : null,
      client_ip: clientIp,
      store_no: storeNo,
      error_msg: p.status === 'failed' ? '演示数据：通道返回支付失败' : null,
      mch_no: mch.mchNo,
      allocation: false,
    })
    tx.gwOrders += 1

    // ===== 资金交易闭环（trade_type='gateway'）：paid/paying/failed 建交易，wait_pay/closed/expired 不建 =====
    if (routed) {
      const tradeId = nextId()
      const tradeNo = nextNo('DEMOT')
      const tradeStatus = p.status === 'paid' ? 'success' : p.status === 'paying' ? 'processing' : 'fail'
      const posted = p.status === 'paid' ? amount : 0
      gen.add('pay_trade', {
        ...base(tradeId, p.status === 'paid' ? payTime : createTime),
        trade_no: tradeNo,
        trade_type: 'gateway',
        container_id: orderId,
        amount,
        posted_amount: posted,
        refundable_balance: posted,
        status: tradeStatus,
        pay_time: payTime,
        close_time: closeTime,
        source,
        channel_mch_no: ANCHORS.mockChannelMchNo,
        store_no: storeNo,
        out_order_no: `MOCK${tradeNo}`,
        app_id: mch.appId,
        mch_no: mch.mchNo,
        provider,
        channel: 'mock',
        title,
      })
      tx.trades += 1
      tx.gwTrades += 1

      if (p.status === 'paid') {
        // paid 网关单闭环：等额 pay 流水（biz_order_no 取容器 bizOrderNo，FundFlowService 同口径）+ 商户通知
        gen.add('pay_fund_flow', {
          ...base(nextId(), payTime),
          mch_no: mch.mchNo,
          app_id: mch.appId,
          flow_type: 'pay',
          trade_no: tradeNo,
          biz_order_no: `BIZ${orderNo}`,
          title,
          amount,
          currency: 'cny',
          channel: 'mock',
          provider,
          channel_mch_no: ANCHORS.mockChannelMchNo,
          out_order_no: `MOCK${tradeNo}`,
          finish_time: payTime,
        })
        tx.flows += 1
        gwNotice({
          event: 'pay.success', orderId, orderNo, time: payTime,
          mode: rng() < 0.2 ? 'retry' : 'success',
          snapshot: snapshotContent([
            ['mchNo', mch.mchNo],
            ['appId', mch.appId],
            ['orderNo', orderNo],
            ['bizOrderNo', `BIZ${orderNo}`],
            ['gatewayType', gatewayType],
            ['source', source],
            ['title', title],
            ['status', p.status],
            ['amount', amount],
            ['channel', 'mock'],
            ['method', method],
            ['provider', provider],
            ['clientEnv', clientEnv],
            ['storeNo', storeNo],
            ['tradeNo', tradeNo],
            ['outOrderNo', `MOCK${tradeNo}`],
            ['payTime', bjTime(payTime)],
          ]),
        })
      } else if (p.status === 'failed') {
        gwNotice({
          event: 'pay.fail', orderId, orderNo, time: closeTime,
          mode: 'failed',
          snapshot: snapshotContent([
            ['orderNo', orderNo], ['tradeNo', tradeNo], ['amount', amount],
            ['status', p.status], ['provider', provider], ['storeNo', storeNo], ['event', 'pay.fail'],
          ]),
        })
      }
      // paying：通道处理中，无终态通知
    } else if (p.status === 'expired') {
      gwNotice({
        event: 'pay.timeout', orderId, orderNo, time: closeTime,
        mode: 'success',
        snapshot: snapshotContent([
          ['orderNo', orderNo], ['amount', amount], ['status', p.status],
          ['storeNo', storeNo], ['event', 'pay.timeout'],
        ]),
      })
    } else if (p.status === 'closed') {
      gwNotice({
        event: 'pay.close', orderId, orderNo, time: closeTime,
        mode: 'success',
        snapshot: snapshotContent([
          ['orderNo', orderNo], ['amount', amount], ['status', p.status],
          ['storeNo', storeNo], ['event', 'pay.close'],
        ]),
      })
    }
    // wait_pay：未支付不产生通知
  })
}

// ---------- [4.5] 异常订单（B5） ----------

/**
 * 异常订单（pay_abnormal_order）：终态交易收到通道收款证据后的人工处置台账，3 条
 * （pending×2 + ignored×1；source 覆盖 callback/sync；channel_status='success'）。
 *
 * trade_no 动态引用 buildTransactions 生成结果中最近的 closed/fail 交易（金额/币种/商户/应用一致，
 * 禁止硬编码——四维度加入后随机序列变化会让旧号漂移）；演示「确认成功」时 AbnormalOrderService
 * 走 confirmPaySuccess 把交易从 close/fail CAS 翻转为 success 并补流水/通知，预置行即可直接演示。
 */
export function buildAbnormalOrders(gen, ctx, tx) {
  const mch = ctx.mchMain
  // 取最近的终态交易：closed 2 笔 + failed 1 笔（closed 含 expired 单，数量充足）
  const closedDesc = [...tx.closedResults].reverse()
  const failedDesc = [...tx.failedResults].reverse()
  const plan = [
    { o: closedDesc[0], handleStatus: 'pending', source: 'callback', withNotify: true },
    { o: failedDesc[0], handleStatus: 'pending', source: 'sync', withNotify: false },
    { o: closedDesc[1], handleStatus: 'ignored', source: 'callback', withNotify: true },
  ]
  plan.forEach((p, i) => {
    if (!p.o) return
    const { o } = p
    const abnormalType = o.tradeRow.status === 'fail' ? 'fail_paid' : 'close_paid'
    // 回调报文快照（脱敏形态：金额/openid/签名打码，保留通道单号与状态）
    const notifyInfo = p.withNotify
      ? JSON.stringify({
        demo: true,
        outTradeNo: `MOCK${o.tradeNo}`,
        tradeStatus: 'success',
        amount: '***',
        openid: 'o***',
        sign: '***REDACTED***',
        note: '演示数据：终态单收到通道收款回调（敏感字段已脱敏）',
      })
      : null
    const handled = p.handleStatus === 'ignored'
    gen.add('pay_abnormal_order', {
      ...base(tx.nextId(), o.tradeRow.close_time ?? o.payTime),
      mch_no: mch.mchNo,
      app_id: mch.appId,
      trade_no: o.tradeNo,
      biz_order_no: `BIZ${o.orderNo}`,
      trade_type: 'normal',
      title: o.tradeRow.title,
      amount: o.tradeRow.amount,
      currency: 'CNY',
      trade_status: o.tradeRow.status,
      abnormal_type: abnormalType,
      source: p.source,
      channel: 'mock',
      provider: o.tradeRow.provider,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      out_order_no: `MOCK${o.tradeNo}`,
      channel_status: 'success',
      callback_notify_info: notifyInfo,
      handle_status: p.handleStatus,
      handle_action: handled ? 'ignore' : null,
      handler: handled ? ANCHORS.adminUser : null,
      handle_time: handled ? addMinutes(o.tradeRow.close_time, 60) : null,
      handle_remark: handled ? '演示数据：核实通道侧已原路退回，无需入账' : null,
    })
    tx.abnormals += 1
  })
}

// ---------- [4.5] 通知全家桶（2026-11-10） ----------

/**
 * 通知域 3 表：notify_notice 公告 6 条 / notify_notice_read 已读记录 6 条 / notify_message 个人消息 4 条。
 *
 * 公告状态覆盖 NotifyStatusEnum（draft/published/offline）与 NotifySeverityEnum（normal/important）：
 *   published×4（is_top normal + important 升级通告 + 普通 + 已过期「管理端可见/用户端不可见」）+ draft + offline；
 *   用户端可见条件 = published 且 effective_time<=now 且（expire_time 为空或 >now）
 *   （NotifyNoticeMapper#listVisibleSummaries / #countVisibleUnread 同一判定）。
 * 已读记录按三账号铺对比：bootx 全读（含 1 条 is_ignored 演示「忽略后铃铛不展示」）/
 *   admin 留 1 条未读 / csqysh 仅读置顶 1 条、留 2 条未读撑角标。
 * 个人消息 source 为预留字符串字段（NotifyMessage 无枚举，写 system/trade 两档）；
 * link 是内部路由（通知组件 navigateTo：http(s) 外链或站内 path），指向异常单 /trade/abnormal-order、
 * 网关单 /trade/pay-order/gateway（与 iam_perm_menu 种子 path 一致）。
 * notify_notice_read 无 version/deleted 列（仅 creator/create_time），注意别套 base()。
 */
export function buildNotices(gen, ctx) {
  const { adminUserId, merchantUserId } = ctx
  const bootxUserId = 1n // [2] 段纯净种子的内置超管，已读/审计要引用但不在本剧本 gen 行内

  // ===== 公告 6 条（正文 Markdown：标题/列表/加粗/链接）=====
  const NOTICE_PLAN = [
    {
      id: idCfg(118), title: 'DaxPay 演示环境使用指南', severity: 'normal', isTop: true,
      status: 'published', effective: ago('30 day'), expire: null, time: ago('30 day'),
      content: '## 演示环境使用指南\n\n欢迎使用 **DaxPay 开源版演示环境**，本环境内置：\n\n- 演示商户与默认应用、门店/码牌/终端全套配置\n- 模拟支付通道，可体验扫码收款 / 退款 / 对账全流程\n- 近 30 天交易流水与统计报表\n\n详细文档见 [DaxPay 官网文档站](https://daxpay.cn)',
    },
    {
      id: idCfg(119), title: '系统升级维护通知', severity: 'important', isTop: false,
      status: 'published', effective: ago('7 day'), expire: null, time: ago('7 day'),
      content: '## 升级维护通知\n\n平台将于 **本周日 02:00 - 06:00** 进行版本升级，期间服务可能出现短暂中断：\n\n- 影响范围：收款 / 退款 / 转账接口\n- 预计时长：约 30 分钟\n\n给您带来不便敬请谅解。',
    },
    {
      id: idCfg(120), title: '新功能上线：码牌支持批量管理', severity: 'normal', isTop: false,
      status: 'published', effective: ago('2 day'), expire: null, time: ago('2 day'),
      content: '## 新功能上线\n\n码牌管理支持 **批量生成与批量绑定门店**：\n\n- 一次最多生成 500 张码牌\n- 支持按批次导出与停用\n\n可在「设备管理 - 码牌管理」中体验。',
    },
    {
      id: idCfg(121), title: '演示草稿公告（未发布）', severity: 'normal', isTop: false,
      status: 'draft', effective: null, expire: null, time: ago('1 day'),
      content: '## 草稿公告\n\n这是演示环境的 **草稿状态** 公告，发布前对用户端不可见。',
    },
    {
      id: idCfg(122), title: '演示历史公告（已下线）', severity: 'normal', isTop: false,
      status: 'offline', effective: ago('90 day'), expire: null, time: ago('90 day'),
      content: '## 历史公告\n\n这是演示环境的 **已下线** 公告，仅管理端列表可见。',
    },
    {
      id: idCfg(123), title: '演示已过期公告（用户端不可见）', severity: 'normal', isTop: false,
      status: 'published', effective: ago('60 day'), expire: ago('3 day'), time: ago('60 day'),
      content: '## 已过期公告\n\n这是 status=published 但 **expire_time 已过** 的公告：用户端铃铛不可见、管理端可见，用于演示过期筛选。',
    },
  ]
  for (const n of NOTICE_PLAN) {
    gen.add('notify_notice', {
      ...base(n.id, n.time),
      title: n.title,
      content: n.content,
      severity: n.severity,
      is_top: n.isTop,
      effective_time: n.effective,
      expire_time: n.expire,
      status: n.status,
    })
  }
  const noticeTop = NOTICE_PLAN[0]      // 置顶（人人已读）
  const noticeImportant = NOTICE_PLAN[1]
  const noticeNormal = NOTICE_PLAN[2]

  // ===== 已读记录 6 条：(user_id, notice_id) 唯一（uk_notify_notice_read）=====
  const READ_PLAN = [
    { user: bootxUserId, notice: noticeTop, read: ago('29 day'), ignored: false },
    { user: bootxUserId, notice: noticeImportant, read: ago('6 day'), ignored: false },
    { user: bootxUserId, notice: noticeNormal, read: ago('1 day'), ignored: true }, // 忽略：视同已读且铃铛不展示
    { user: adminUserId, notice: noticeTop, read: ago('25 day'), ignored: false },
    { user: adminUserId, notice: noticeNormal, read: ago('30 hour'), ignored: false },
    { user: merchantUserId, notice: noticeTop, read: ago('20 day'), ignored: false }, // csqysh 留 2 条未读撑角标
  ]
  for (const r of READ_PLAN) {
    gen.add('notify_notice_read', {
      id: idCfg(124 + READ_PLAN.indexOf(r)),
      user_id: r.user,
      notice_id: r.notice.id,
      read_time: r.read,
      is_ignored: r.ignored,
      creator: r.user,
      create_time: r.read,
    })
  }

  // ===== 个人消息 4 条：admin / csqysh 各 1 未读 + 1 已读 =====
  const MESSAGE_PLAN = [
    {
      id: idCfg(130), userId: adminUserId, title: '检测到 2 笔异常订单待处理',
      content: '存在待处理异常订单（终态交易收到通道收款证据），建议尽快核实并处置（确认成功 / 忽略）。',
      source: 'system', link: '/trade/abnormal-order', isRead: false, time: ago('30 minute'),
    },
    {
      id: idCfg(131), userId: adminUserId, title: '今日网关收款已全部入账',
      content: '今日聚合扫码与收银台收款已全部入账，可在网关订单页查看明细。',
      source: 'trade', link: '/trade/pay-order/gateway', isRead: true, time: ago('6 hour'),
    },
    {
      id: idCfg(132), userId: merchantUserId, title: '您有一笔退款已成功',
      content: '退款单已原路退回买家支付账户，预计 1-3 个工作日到账。',
      source: 'trade', link: null, isRead: false, time: ago('2 hour'),
    },
    {
      id: idCfg(133), userId: merchantUserId, title: '欢迎使用 DaxPay 商户端',
      content: '演示商户账号已开通，可在商户端管理门店 / 码牌 / 订单。',
      source: 'system', link: null, isRead: true, time: ago('29 day'),
    },
  ]
  for (const m of MESSAGE_PLAN) {
    gen.add('notify_message', {
      ...base(m.id, m.time),
      user_id: m.userId,
      title: m.title,
      content: m.content,
      source: m.source,
      link: m.link,
      extra: null,
      is_read: m.isRead,
    })
  }

  return { noticeIds: NOTICE_PLAN.map((n) => n.id), visibleIds: [noticeTop.id, noticeImportant.id, noticeNormal.id] }
}

// ---------- [4.5] 安全与平台（2026-11-10） ----------

/**
 * 安全与平台 4 表：pay_blacklist 5 条 / pay_risk_hit 4 条 / mch_wx_domain_verify 4 条 / system_platform_config 2 行。
 *
 * 黑名单红线（CRITICAL，构建期断言 #23 防退化）：绝不拉黑 127.0.0.1、192.168.*、10.* 内网段、
 * 北京地区码（110000/11）以及订单 IP 轮换池（STORE_IP_POOLS）中除 203.0.113.10 外的任何 IP——
 * 否则本机演示扫码会被 IP 黑名单直接拦死。开源版 DefaultPayRiskChecker 只检查 IP + 省市地区，
 * openid/alipay_user 类型不种（用户标识黑名单为商业版独占）。
 * 省市名单 value 存行政区划编码（省 "44" / 市 "4403"，见 DefaultPayRiskChecker#checkRegionBlacklist 注释）。
 *
 * 风险命中与黑名单成对：2 条 blacklist_id 回指 IP/市名单（phase=before_pay、trade_no 动态引用 demo 失败交易）、
 * 1 条海外 IP 命中（blacklist_id 为空，hit_type=overseas_ip，地域策略非黑名单来源）、1 条事后命中（after_pay）。
 *
 * 域名校验文件名规则（WxDomainVerifyService）：微信 MP_verify_+字母数字.txt（verify_code 取随机段）/
 * 平台级通用形态 [A-Za-z0-9_]{1,60}.(txt|html)（支付宝关联普通二维码为 32 位 hex .html，verify_code 取文件名主体）；
 * 商户级仅允许 MP_verify_*.txt；verify_code 全局唯一（uk_mch_wx_domain_verify_code）；
 * 平台行 mch_no 固定 '0'（WxDomainVerifyService#PLATFORM_MCH_NO）。
 *
 * 平台配置只手写 WEBSITE/URL 两行（config_type 唯一）；安全类（SECURITY_ 前缀族与 API_SECURITY 等）不手写——
 * 运行时 getOrCreateConfig 自动生成默认值，手写会把生成器默认值钉死在演示口径上。
 * WEBSITE 不填 logo/logoDark（无 S3 对象必裂图），字段结构以 PlatformWebsiteConfig/PlatformUrlConfig 为准。
 */
export function buildSecurity(gen, ctx, tx) {
  const mch = ctx.mchMain

  // ===== 黑名单 5 条：IP + 省 + 市（enable）/ 1 条 disable / 1 条已过期 =====
  const blacklistRows = [
    { id: idCfg(140), type: 'ip', value: '203.0.113.10', status: 'enable', expire: null, time: ago('20 day'), reason: '演示数据：高频异常下单 IP（订单 IP 池中指定可拉黑的文档段地址）' },
    { id: idCfg(141), type: 'province', value: '44', status: 'enable', expire: null, time: ago('15 day'), reason: '演示数据：广东省地区限制' },
    { id: idCfg(142), type: 'city', value: '4403', status: 'enable', expire: null, time: ago('15 day'), reason: '演示数据：深圳市地区限制' },
    { id: idCfg(143), type: 'ip', value: '198.51.100.77', status: 'disable', expire: null, time: ago('10 day'), reason: '演示数据：误报已解除' },
    { id: idCfg(144), type: 'ip', value: '203.0.113.66', status: 'enable', expire: ago('2 day'), time: ago('12 day'), reason: '演示数据：临时封禁（已过期自动失效）' },
  ]
  for (const b of blacklistRows) {
    gen.add('pay_blacklist', {
      ...base(b.id, b.time),
      type: b.type,
      value: b.value,
      wx_app_id: null,
      status: b.status,
      reason: b.reason,
      expire_time: b.expire,
      remark: '演示数据',
    })
  }
  const blIp = blacklistRows[0]
  const blCity = blacklistRows[2]

  // ===== 风险命中 4 条：成对黑名单 2 + 海外 1 + 事后 1 =====
  // 拦截时刻 = 所引交易下单前 30 秒（before_pay 检查先于落单）；after_pay 取已支付交易支付后 10 秒
  const failedDesc = [...tx.failedResults].reverse()
  const paidDesc = [...tx.paidResults].reverse()
  const beforeTime = (o) => raw(`(${o.tradeRow.create_time.sql}) - interval '30 second'`)
  const afterTime = (o) => addSeconds(o.payTime, 10)
  const ipTrade = failedDesc[0]
  const cityTrade = failedDesc[1]
  const afterTrade = paidDesc[0]
  if (ipTrade) {
    gen.add('pay_risk_hit', {
      ...base(idCfg(146), beforeTime(ipTrade)),
      phase: 'before_pay',
      hit_type: 'ip',
      hit_value: blIp.value,
      blacklist_id: blIp.id,
      mch_no: mch.mchNo,
      app_id: mch.appId,
      trade_no: ipTrade.tradeNo,
      order_no: ipTrade.orderNo,
      biz_order_no: `BIZ${ipTrade.orderNo}`,
      trade_type: 'normal',
      method: ipTrade.method,
      product: 'mock_pay',
      channel: 'mock',
      client_ip: blIp.value,
      scene: 'api',
      remark: '演示数据：支付前 IP 黑名单命中',
      client_city: '深圳市',
    })
  }
  if (cityTrade) {
    gen.add('pay_risk_hit', {
      ...base(idCfg(147), beforeTime(cityTrade)),
      phase: 'before_pay',
      hit_type: 'city',
      hit_value: blCity.value,
      blacklist_id: blCity.id,
      mch_no: mch.mchNo,
      app_id: mch.appId,
      trade_no: cityTrade.tradeNo,
      order_no: cityTrade.orderNo,
      biz_order_no: `BIZ${cityTrade.orderNo}`,
      trade_type: 'normal',
      method: cityTrade.method,
      product: 'mock_pay',
      channel: 'mock',
      client_ip: cityTrade.clientIp,
      scene: cityTrade.source === 'cashier_code' ? 'code' : 'api',
      remark: '演示数据：支付前市级地区黑名单命中',
      client_city: '深圳市',
    })
  }
  // 海外 IP 命中：地域策略拦截，非黑名单来源（blacklist_id 为空），发生在网关预下单阶段
  gen.add('pay_risk_hit', {
    ...base(idCfg(148), ago('4 day')),
    phase: 'before_pay',
    hit_type: 'overseas_ip',
    hit_value: '45.62.120.18',
    blacklist_id: null,
    mch_no: mch.mchNo,
    app_id: mch.appId,
    trade_no: null,
    order_no: null,
    biz_order_no: null,
    trade_type: null,
    method: null,
    product: 'mock_pay',
    channel: 'mock',
    client_ip: '45.62.120.18',
    scene: 'gateway',
    remark: '演示数据：海外 IP 地域策略命中（非黑名单来源）',
    client_city: null,
  })
  if (afterTrade) {
    gen.add('pay_risk_hit', {
      ...base(idCfg(149), afterTime(afterTrade)),
      phase: 'after_pay',
      hit_type: 'ip',
      hit_value: blIp.value,
      blacklist_id: blIp.id,
      mch_no: mch.mchNo,
      app_id: mch.appId,
      trade_no: afterTrade.tradeNo,
      order_no: afterTrade.orderNo,
      biz_order_no: `BIZ${afterTrade.orderNo}`,
      trade_type: 'normal',
      method: afterTrade.method,
      product: 'mock_pay',
      channel: 'mock',
      client_ip: blIp.value,
      scene: 'api',
      remark: '演示数据：支付后补录命中（after_pay 样本）',
      client_city: '深圳市',
    })
  }

  // ===== 域名校验文件 4 条：平台 2（微信 + 支付宝 html）/ 商户 2（MP_verify）=====
  const WX_VERIFY_PLAN = [
    { id: idCfg(152), platform: true, code: 'a1b2c3d4e5f60718', ext: '.txt', remark: '演示数据：平台级微信域名校验文件' },
    { id: idCfg(153), platform: true, code: '9f8e7d6c5b4a39281706f5e4d3c2b1a0', ext: '.html', remark: '演示数据：平台级支付宝关联普通二维码校验（32 位 hex .html）' },
    { id: idCfg(154), platform: false, code: 'b2c3d4e5f6071829', ext: '.txt', remark: '演示数据：商户级微信域名校验文件' },
    { id: idCfg(155), platform: false, code: 'c3d4e5f60718293a', ext: '.txt', remark: '演示数据：商户级微信域名校验文件（第二枚）' },
  ]
  for (const v of WX_VERIFY_PLAN) {
    const fileName = v.platform && v.ext === '.html' ? `${v.code}.html` : `MP_verify_${v.code}.txt`
    gen.add('mch_wx_domain_verify', {
      ...base(v.id, ago('25 day')),
      mch_no: v.platform ? '0' : mch.mchNo,
      platform: v.platform,
      file_name: fileName,
      verify_code: v.code,
      file_content: v.code,
      remark: v.remark,
    })
  }

  // ===== 平台配置 2 行：WEBSITE（不填 logo，无 S3 对象必裂图）/ URL（本地演示地址）=====
  gen.add('system_platform_config', {
    ...base(idCfg(158), ago('45 day')),
    config_type: 'website',
    config_name: '站点显示内容配置',
    config_data: json({
      systemName: 'DaxPay 演示环境',
      companyName: 'DaxPay 开源版演示公司',
      companyPhone: '400-000-0000',
      companyEmail: 'demo@daxpay.cn',
      companyWechat: 'daxpay-demo',
      icpInfo: '京ICP备00000000号-1',
      icpLink: 'https://beian.miit.gov.cn',
      copyright: 'Copyright © 2026 DaxPay',
    }),
    description: '演示数据：站点显示内容（未配置 logo，避免无对象裂图）',
    enabled: true,
  })
  gen.add('system_platform_config', {
    ...base(idCfg(159), ago('45 day')),
    config_type: 'url',
    config_name: '系统访问地址配置',
    config_data: json({
      adminBaseUrl: 'http://localhost:6999',
      merchantBaseUrl: 'http://localhost:7999',
      paymentGatewayBaseUrl: 'http://localhost:9500',
      backendBaseUrl: 'http://localhost:9999',
    }),
    description: '演示数据：各端访问地址（本地调试形态，部署时按环境修改）',
    enabled: true,
  })

  return { blacklistRows }
}

// ---------- [4.5] 转账静态终态单（2026-11-10） ----------

/**
 * 转账域 4 表：pay_transfer_order_wechat/alipay/douyin 容器各 1 + pay_transfer_trade 资金凭证。
 *
 * 只种终态（success×2 + fail×1）：mock 通道无 transfer 接口，种非终态会让页面「同步」按钮调用失败。
 * 闭环口径（TransferAssistService#buildTrade）：trade.trade_no = 容器 transfer_no（同一单号）、
 * relation_no = transferNo（实际上送通道的商户转账号）、container_id/container_channel 成对回指容器主键与通道。
 * 收款方形态与通道对齐：微信 payee_openid（必填，wx_app_id 挂 mock 微信应用）、
 * 支付宝 payee_type=user_id + transfer_scene=场景名（AlipayTransferSceneEnum 枚举值）、
 * 抖音 payee_type=openid + transfer_scene=场景 ID（DouyinTransferSceneEnum 1001-1007）。
 * 单号续用 tx 序列（idTx 段 + nextNo 计数器），与交易域天然错开。
 */
export function buildTransfers(gen, ctx, tx) {
  const mch = ctx.mchMain
  const PLAN = [
    { channel: 'wechat', status: 'success', day: 5, hour: 10, amount: 5200, title: '演示转账-微信零钱佣金发放' },
    { channel: 'alipay', status: 'success', day: 3, hour: 15, amount: 12800, title: '演示转账-支付宝货款结算' },
    { channel: 'douyin', status: 'fail', day: 1, hour: 11, amount: 8800, title: '演示转账-抖音零钱活动奖励（失败样本）' },
  ]
  for (const p of PLAN) {
    const containerId = tx.nextId()
    const transferNo = tx.nextNo('DEMOTRF')
    const createTime = dayAt(p.day, p.hour, 20)
    const finishTime = addMinutes(createTime, 2)
    const common = {
      ...base(containerId, createTime),
      transfer_no: transferNo,
      biz_transfer_no: `BIZ${transferNo}`,
      channel_mch_no: ANCHORS.mockChannelMchNo,
      out_transfer_no: `MOCK${transferNo}`,
      mch_no: mch.mchNo,
      amount: p.amount,
      currency: 'CNY',
      title: p.title,
      reason: '演示数据：静态终态转账样本',
      status: p.status,
      finish_time: finishTime,
      notify_url: NOTIFY_URL,
      attach: null,
      req_time: createTime,
      error_msg: p.status === 'fail' ? '演示数据：通道返回转账失败（收款方账户异常）' : null,
      app_id: mch.appId,
    }
    if (p.channel === 'wechat') {
      gen.add('pay_transfer_order_wechat', {
        ...common,
        payee_openid: 'oDemoTransferWx00000001',
        transfer_scene: null,
        transfer_body: '演示转账备注：佣金报酬',
        user_name: null,
        wx_app_id: ANCHORS.mockWxAppId,
      })
    } else if (p.channel === 'alipay') {
      gen.add('pay_transfer_order_alipay', {
        ...common,
        payee_type: 'user_id',
        payee_account: '2088001100002233',
        payee_name: '演示收款人',
        transfer_scene: '佣金报酬',
      })
    } else {
      gen.add('pay_transfer_order_douyin', {
        ...common,
        payee_type: 'openid',
        payee_account: 'demo-douyin-openid-0001',
        payee_name: '演示收款人',
        transfer_scene: '1003',
      })
    }
    gen.add('pay_transfer_trade', {
      ...base(tx.nextId(), createTime),
      trade_no: transferNo,
      biz_transfer_no: `BIZ${transferNo}`,
      container_id: containerId,
      container_channel: p.channel,
      channel: 'mock',
      provider: p.channel,
      amount: p.amount,
      currency: 'CNY',
      status: p.status,
      out_transfer_no: `MOCK${transferNo}`,
      relation_no: transferNo,
      finish_time: finishTime,
      title: p.title,
      mch_no: mch.mchNo,
      app_id: mch.appId,
    })
    tx.transfers = (tx.transfers ?? 0) + 1
  }
}

// ---------- [4.5] P2 低价值项（2026-11-10） ----------

/**
 * P2 审计与内容安全 4 表：登录日志 8 条 / 操作日志 5 条 / 统一支付接口日志 6 条 / 敏感词命中 3 条。
 *
 * 登录日志：三账号（bootx/admin/csqysh）历史登录，成功 + 密码失败样本；
 * client 为身份域（admin/merchant）、terminal 为端形态（web/app）、login_type=password、
 * login_location 留空（本机无 ip2region xdb 属预期，运行时不回填历史行）。
 * 操作日志 business_type 取 OperateLogType 枚举 code（other/add/update/...小写），
 * operate_url/method 用真实控制器路径（/admin/merchant/page、/admin/device/qrcode/create-batch 等）。
 * unipay 日志表无 base 列（仅 id + 业务列），api_path/api_title 与 PaymentVerifyAspect#resolveApiTitle 映射一致，
 * 成功 code=0（CommonCode.SUCCESS_CODE）、失败样本 20052（PayErrorCode.VERIFY_SIGN_FAILED）。
 * 敏感词命中 word_id 指向 [4.2] 段旁路种子 id 2077569027575820288（system_sensitive_word 测试词，纯净版已排除），
 * scene/source 取 SensitiveWordSceneEnum / SensitiveWordSourceEnum。
 */
export function buildP2Extras(gen, ctx) {
  const { adminUserId, merchantUserId } = ctx
  const mch = ctx.mchMain
  const bootxUserId = 1n

  // ===== 登录日志 8 条（成功 5 + 密码失败 3；ip 与订单同款示例段轮换）=====
  const LOGIN_PLAN = [
    { userId: bootxUserId, account: 'bootx', client: 'admin', ok: true, ip: '203.0.113.10', browser: 'Chrome 131', os: 'Windows 11', terminal: 'web', time: ago('2 hour') },
    { userId: bootxUserId, account: 'bootx', client: 'admin', ok: false, ip: '203.0.113.11', browser: 'Chrome 131', os: 'Windows 11', terminal: 'web', time: ago('9 day') },
    { userId: adminUserId, account: ANCHORS.adminUser, client: 'admin', ok: true, ip: '203.0.113.24', browser: 'Edge 130', os: 'Windows 11', terminal: 'web', time: ago('1 day') },
    { userId: adminUserId, account: ANCHORS.adminUser, client: 'admin', ok: false, ip: '198.51.100.7', browser: 'Edge 130', os: 'Windows 11', terminal: 'web', time: ago('6 day') },
    { userId: adminUserId, account: ANCHORS.adminUser, client: 'admin', ok: true, ip: '198.51.100.23', browser: 'Chrome 130', os: 'macOS 15', terminal: 'web', time: ago('3 day') },
    { userId: merchantUserId, account: ANCHORS.merchantUser, client: 'merchant', ok: true, ip: '203.0.113.99', browser: 'Chrome 131', os: 'Windows 10', terminal: 'web', time: ago('5 hour') },
    { userId: merchantUserId, account: ANCHORS.merchantUser, client: 'merchant', ok: false, ip: '192.168.1.36', browser: 'Safari 18', os: 'iOS 18', terminal: 'app', time: ago('4 day') },
    { userId: merchantUserId, account: ANCHORS.merchantUser, client: 'merchant', ok: true, ip: '192.168.1.35', browser: 'Chrome Mobile 131', os: 'Android 15', terminal: 'app', time: ago('12 day') },
  ]
  for (const l of LOGIN_PLAN) {
    gen.add('starter_audit_login_log', {
      ...base(idCfg(162 + LOGIN_PLAN.indexOf(l)), l.time),
      user_id: l.userId,
      account: l.account,
      login: l.ok,
      client: l.client,
      login_type: 'password',
      ip: l.ip,
      login_location: null,
      browser: l.browser,
      os: l.os,
      msg: l.ok ? '登录成功' : '账号或密码错误',
      login_time: l.time,
      terminal: l.terminal,
    })
  }

  // ===== 操作日志 5 条（典型管理操作，操作人与账号对应；businessType=OperateLogType code 小写）=====
  const OPERATE_PLAN = [
    { account: 'bootx', operateId: bootxUserId, client: 'admin', title: '商户管理', biz: 'other', reqMethod: 'GET', url: '/admin/merchant/page', method: 'cn.daxpay.open.payment.admin.controller.merchant.info.MerchantAdminController.page', ip: '203.0.113.10', time: ago('3 hour'), param: null },
    { account: ANCHORS.adminUser, operateId: adminUserId, client: 'admin', title: '批量创建空白码牌', biz: 'add', reqMethod: 'POST', url: '/admin/device/qrcode/create-batch', method: 'cn.daxpay.open.payment.admin.controller.device.DeviceQrCodeAdminController.createBatch', ip: '203.0.113.24', time: ago('30 day'), param: json({ count: 9, batchNo: 'DEMOB20261101' }) },
    { account: ANCHORS.adminUser, operateId: adminUserId, client: 'admin', title: '导出普通支付业务单', biz: 'export', reqMethod: 'POST', url: '/admin/order/normal-pay/export', method: 'cn.daxpay.open.payment.admin.controller.trade.NormalPayOrderAdminController.export', ip: '203.0.113.24', time: ago('2 day'), param: null },
    { account: ANCHORS.merchantUser, operateId: merchantUserId, client: 'merchant', title: '新增门店', biz: 'add', reqMethod: 'POST', url: '/mch/store/add', method: 'cn.daxpay.open.payment.merchant.controller.store.MchStoreInfoController.add', ip: '203.0.113.99', time: ago('28 day'), param: json({ storeName: '演示商户社区店' }) },
    { account: ANCHORS.merchantUser, operateId: merchantUserId, client: 'merchant', title: '导出普通支付业务单', biz: 'export', reqMethod: 'POST', url: '/mch/order/normal-pay/export', method: 'cn.daxpay.open.payment.merchant.controller.trade.MchNormalPayOrderController.export', ip: '203.0.113.99', time: ago('1 day'), param: null },
  ]
  for (const o of OPERATE_PLAN) {
    gen.add('starter_audit_operate_log', {
      ...base(idCfg(172 + OPERATE_PLAN.indexOf(o)), o.time),
      title: o.title,
      operate_id: o.operateId,
      account: o.account,
      client: o.client,
      browser: 'Chrome 131',
      os: 'Windows 11',
      business_type: o.biz,
      method: o.method,
      request_method: o.reqMethod,
      operate_url: o.url,
      operate_ip: o.ip,
      operate_location: null,
      operate_param: o.param,
      operate_return: null,
      success: true,
      error_msg: null,
      operate_time: o.time,
    })
  }

  // ===== 统一支付接口审计日志 6 条（无 base 列：仅 id + 业务列 + operate_time/req_id）=====
  const UNIPAY_PLAN = [
    { path: '/unipay/pay', title: '支付下单', success: true, errorCode: 0, errorMsg: null, duration: 145, ip: '203.0.113.24', time: ago('1 hour') },
    { path: '/unipay/query/pay-order', title: '支付订单查询', success: true, errorCode: 0, errorMsg: null, duration: 38, ip: '203.0.113.24', time: ago('55 minute') },
    { path: '/unipay/pay', title: '支付下单', success: false, errorCode: 20052, errorMsg: '签名校验失败', duration: 12, ip: '198.51.100.7', time: ago('2 day') },
    { path: '/unipay/refund', title: '退款', success: true, errorCode: 0, errorMsg: null, duration: 210, ip: '203.0.113.99', time: ago('3 day') },
    { path: '/unipay/sync/order/pay', title: '支付订单同步', success: true, errorCode: 0, errorMsg: null, duration: 96, ip: '203.0.113.99', time: ago('5 day') },
    { path: '/unipay/gateway/pre-pay', title: '网关预下单', success: true, errorCode: 0, errorMsg: null, duration: 58, ip: '198.51.100.23', time: ago('20 hour') },
  ]
  UNIPAY_PLAN.forEach((u, i) => {
    gen.add('starter_audit_unipay_log', {
      id: idCfg(180 + i),
      mch_no: mch.mchNo,
      api_path: u.path,
      api_title: u.title,
      request_method: 'POST',
      client_ip: u.ip,
      request_ip: u.ip,
      request_location: null,
      success: u.success,
      error_code: u.errorCode,
      error_msg: u.errorMsg,
      duration_ms: u.duration,
      trace_id: `DEMO TRACE ${String(i + 1).padStart(6, '0')}`,
      req_param: i === 0 ? json({ mchNo: mch.mchNo, appId: mch.appId, bizOrderNo: 'BIZDEMO00000001', amount: 6800, demo: true }) : null,
      res_body: i === 0 ? json({ code: 0, msg: 'success', demo: true }) : null,
      operate_time: u.time,
      req_id: `DEMOREQ${String(i + 1).padStart(10, '0')}`,
    })
  })

  // ===== 敏感词命中 3 条（word_id 指向 [4.2] 段旁路测试敏感词 2077569027575820288）=====
  const SENSITIVE_PLAN = [
    { scene: 'pay_title', source: 'unipay', operator: null, mch: true, ip: '203.0.113.99', path: '/unipay/pay', preview: '测试敏感词-演示下单标题', time: ago('4 day') },
    { scene: 'notice', source: 'admin', operator: adminUserId, mch: false, ip: '203.0.113.10', path: '/notify/notice/add', preview: '公告标题包含「测试敏感词」的演示样本', time: ago('2 day') },
    { scene: 'pay_title', source: 'merchant', operator: merchantUserId, mch: true, ip: '192.168.1.35', path: '/unipay/pay', preview: '商户直连下单标题命中测试敏感词', time: ago('1 day') },
  ]
  for (const s of SENSITIVE_PLAN) {
    gen.add('system_sensitive_word_hit', {
      ...base(idCfg(188 + SENSITIVE_PLAN.indexOf(s)), s.time),
      word_id: 2077569027575820288n,
      hit_word: '测试敏感词',
      content_preview: s.preview,
      scene: s.scene,
      source: s.source,
      mch_no: s.mch ? mch.mchNo : null,
      app_id: s.mch ? mch.appId : null,
      operator_id: s.operator,
      client_ip: s.ip,
      request_path: s.path,
      remark: '演示数据：敏感词命中样本（拒绝级）',
    })
  }
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
  // 交易按容器形态分型（normal/gateway 两套容器，container_id 分属不同表）
  const normalTrades = trades.filter((t) => (t.trade_type ?? 'normal') !== 'gateway')
  const gatewayTrades = trades.filter((t) => (t.trade_type ?? 'normal') === 'gateway')
  const gatewayOrders = gen.all('pay_gateway_order')
  const refunds = gen.all('pay_refund_order')
  const flows = gen.all('pay_fund_flow')
  const tasks = gen.all('mch_notice_task')
  const records = gen.all('mch_notice_record')
  const basics = gen.all('pay_route_basic_config')

  // 1a. 普通订单 ↔ normal 交易 1:1
  const tradeByContainer = countBy(normalTrades, 'container_id')
  for (const o of orders) {
    if ((tradeByContainer.get(o.id) ?? 0) !== 1) {
      fails.push(`订单 ${o.order_no} 对应交易数 ${tradeByContainer.get(o.id) ?? 0} ≠ 1`)
    }
  }
  for (const t of normalTrades) {
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

  // 1b. 网关订单 ↔ gateway 交易：paid/paying/failed 恰好 1 笔，wait_pay/closed/expired 不建交易；
  //     金额与状态映射对齐（paid→success / paying→processing / failed→fail）
  const gwOrderById = new Map(gatewayOrders.map((g) => [g.id, g]))
  const gwTradeByContainer = countBy(gatewayTrades, 'container_id')
  const GW_TRADE_STATUS = { paid: 'success', paying: 'processing', failed: 'fail' }
  for (const g of gatewayOrders) {
    const want = GW_TRADE_STATUS[g.status] != null ? 1 : 0
    if ((gwTradeByContainer.get(g.id) ?? 0) !== want) {
      fails.push(`网关订单 ${g.order_no}（${g.status}）交易数 ${gwTradeByContainer.get(g.id) ?? 0} ≠ ${want}`)
    }
  }
  for (const t of gatewayTrades) {
    const g = gwOrderById.get(t.container_id)
    if (!g) {
      fails.push(`gateway 交易 ${t.trade_no} 的 container_id=${t.container_id} 找不到网关订单`)
      continue
    }
    if (g.amount !== t.amount) fails.push(`网关订单 ${g.order_no} 金额 ${g.amount} ≠ 交易 ${t.trade_no} 金额 ${t.amount}`)
    if (GW_TRADE_STATUS[g.status] !== t.status) {
      fails.push(`网关订单 ${g.order_no} 状态 ${g.status} 与交易状态 ${t.status} 不匹配`)
    }
    if (t.status === 'success') {
      if (t.posted_amount !== t.amount) fails.push(`成功 gateway 交易 ${t.trade_no} 入账额 ${t.posted_amount} ≠ 交易额 ${t.amount}`)
      if (t.pay_time == null) fails.push(`成功 gateway 交易 ${t.trade_no} 缺 pay_time`)
    } else if (t.posted_amount !== 0 || t.refundable_balance !== 0 || t.pay_time != null) {
      fails.push(`非成功 gateway 交易 ${t.trade_no} 不应有入账额/可退余额/支付时间`)
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

  // 5. 通知任务：唯一键不冲突、业务单号可回溯（订单/退款/网关订单）、记录数与 send_count 对齐
  const taskKey = (t) => [t.mch_no, t.app_id, t.event, t.biz_no, t.transport, t.format, t.source].join('|')
  const seenTask = new Set()
  const bizNos = new Set([...orders.map((o) => o.order_no), ...refunds.map((r) => r.refund_no), ...gatewayOrders.map((g) => g.order_no)])
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

  // 6. 路由：策略唯一且为 basic（双策略行会让运行时 oneOpt 抛 TooManyResultsException）、
  //    五个支付渠道全覆盖且全部指向 mock 通道商户
  const strategies = gen.all('pay_route_strategy')
  if (strategies.length !== 1 || strategies[0].mode !== 'basic') {
    fails.push(`路由策略应唯一且 mode='basic'（当前 ${strategies.length} 行）`)
  }
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

  // 8. 门店：5 家、store_no 唯一、恰好 1 家默认、存在停用店（状态筛选覆盖）
  const stores = gen.all('mch_store_info')
  if (stores.length < 5) fails.push(`门店数 ${stores.length} < 5`)
  const defaultStores = stores.filter((s) => s.default_store)
  if (defaultStores.length !== 1) fails.push(`默认门店数 ${defaultStores.length} ≠ 1`)
  if (!stores.some((s) => s.status === 'disabled')) fails.push('门店缺少 disabled 状态行')
  const storeNoSeen = new Set()
  for (const s of stores) {
    if (storeNoSeen.has(s.store_no)) fails.push(`门店号重复 ${s.store_no}`)
    storeNoSeen.add(s.store_no)
  }

  // 9. 商户终端：terminal_no 唯一、每启用门店恰好 1 台默认终端、存在停用终端
  const terminals = gen.all('device_terminal')
  if (terminals.length < 5) fails.push(`商户终端数 ${terminals.length} < 5`)
  if (!terminals.some((t) => t.enable === false)) fails.push('商户终端缺少 enable=false 行')
  const termNoSeen = new Set()
  for (const t of terminals) {
    if (termNoSeen.has(t.terminal_no)) fails.push(`商户终端编码重复 ${t.terminal_no}`)
    termNoSeen.add(t.terminal_no)
  }
  const enabledStoreNos = new Set(stores.filter((s) => s.status === 'enable').map((s) => s.store_no))
  for (const storeNo of enabledStoreNos) {
    const defaults = terminals.filter((t) => t.store_no === storeNo && t.default_terminal)
    if (defaults.length !== 1) fails.push(`门店 ${storeNo} 默认终端数 ${defaults.length} ≠ 1`)
  }

  // 10. 通道终端与绑定：type 四档全覆盖、status 覆盖四态、三条唯一约束不冲突
  const channelTerminals = gen.all('device_channel_terminal')
  const types = new Set(channelTerminals.map((c) => c.type))
  for (const t of ['common', 'wechat', 'alipay', 'union']) {
    if (!types.has(t)) fails.push(`通道终端缺少报送类型 ${t}`)
  }
  const ctStatuses = new Set(channelTerminals.map((c) => c.status))
  for (const st of ['submit', 'init', 'wait', 'error']) {
    if (!ctStatuses.has(st)) fails.push(`通道终端缺少状态 ${st}`)
  }
  const binds = gen.all('device_terminal_channel_bind')
  const boundCtIds = new Set()
  const bindKeySeen = new Set()
  for (const b of binds) {
    if (boundCtIds.has(String(b.channel_terminal_id))) fails.push(`通道终端 ${b.channel_terminal_id} 被重复绑定`)
    boundCtIds.add(String(b.channel_terminal_id))
    const key = [b.system_terminal_no, b.channel_mch_no, b.type].join('|')
    if (bindKeySeen.has(key)) fails.push(`终端绑定 (终端,通道商户,类型) 重复: ${key}`)
    bindKeySeen.add(key)
  }

  // 11. 虚拟设备：device_no 唯一、覆盖未激活/停用、播报回执行时间字段齐全
  const virtuals = gen.all('device_virtual')
  if (virtuals.length < 4) fails.push(`虚拟设备数 ${virtuals.length} < 4`)
  if (!virtuals.some((v) => v.activated_time == null)) fails.push('虚拟设备缺少未激活行（activated_time 为空）')
  if (!virtuals.some((v) => v.enable === false)) fails.push('虚拟设备缺少停用行（enable=false）')
  const deviceNoSeen = new Set()
  for (const v of virtuals) {
    if (deviceNoSeen.has(v.device_no)) fails.push(`虚拟设备号重复 ${v.device_no}`)
    deviceNoSeen.add(v.device_no)
    if (v.last_pay_ack_ok === true) {
      if (v.last_pay_ack_time == null || v.last_pay_ack_msg_id == null || v.last_online_time == null || v.activated_time == null) {
        fails.push(`虚拟设备 ${v.device_no} 播报回执行缺时间字段`)
      }
    }
  }

  // 12. 码牌：8 张、code 唯一、h5/mini_app 双形态、存在停用、≥2 批次
  const qrCodes = gen.all('device_qr_code')
  if (qrCodes.length < 8) fails.push(`码牌数 ${qrCodes.length} < 8`)
  const codeSeen = new Set()
  for (const q of qrCodes) {
    if (codeSeen.has(q.code)) fails.push(`码牌编码重复 ${q.code}`)
    codeSeen.add(q.code)
  }
  if (!qrCodes.some((q) => q.program_type === 'h5')) fails.push('码牌缺少 h5 落地形态')
  if (!qrCodes.some((q) => q.program_type === 'mini_app')) fails.push('码牌缺少 mini_app 落地形态')
  if (!qrCodes.some((q) => q.status === 'disabled')) fails.push('码牌缺少停用行（status=disabled）')
  if (new Set(qrCodes.map((q) => q.batch_no)).size < 2) fails.push('码牌批次少于 2 个')

  // 13. 网关配置：level=auto 恰好 1 行、(config,client_env,pay_form) 唯一、收银项 METHOD/DIRECT 字段互斥
  const gatewayConfigs = gen.all('pay_gateway_pay_config')
  if (gatewayConfigs.filter((c) => c.level === 'auto').length !== 1) fails.push('网关支付配置应恰好 1 行 level=auto')
  const clientEnvs = gen.all('pay_gateway_pay_client_env')
  const envKeySeen = new Set()
  for (const e of clientEnvs) {
    const key = [String(e.config_id), e.client_env, e.pay_form].join('|')
    if (envKeySeen.has(key)) fails.push(`客户端环境策略 (配置,环境,形态) 重复: ${key}`)
    envKeySeen.add(key)
  }
  const cashierItems = gen.all('pay_gateway_cashier_item')
  if (cashierItems.length < 3) fails.push(`收银台支付项 ${cashierItems.length} 条 < 3`)
  for (const it of cashierItems) {
    if (it.resolve_mode === 'method' && (it.method == null || it.channel_mch_no != null || it.capability != null)) {
      fails.push(`收银项 ${it.name} METHOD 模式应只填 method`)
    }
    if (it.resolve_mode === 'direct' && (it.method != null || it.channel_mch_no == null || it.capability == null)) {
      fails.push(`收银项 ${it.name} DIRECT 模式应只填通道商户+能力`)
    }
    if (it.cashier_type === 'web' && it.client_env != null) fails.push(`收银项 ${it.name} WEB 台 client_env 应为空`)
    if (it.cashier_type !== 'web' && it.client_env == null) fails.push(`收银项 ${it.name} 非 WEB 台 client_env 必填`)
  }

  // 14. 协议版本：DRAFT 与 ARCHIVED 各恰好 1 行（挂靠 [2] 段协议行，挂靠完整性由装机断言兜底）
  const protoVersions = gen.all('base_user_protocol_version')
  if (protoVersions.filter((v) => v.status === 'DRAFT').length !== 1) fails.push('协议版本应恰好 1 行 DRAFT')
  if (protoVersions.filter((v) => v.status === 'ARCHIVED').length !== 1) fails.push('协议版本应恰好 1 行 ARCHIVED')

  // ===== 四维度打通与跨域闭环断言（2026-11-10，防退化）=====

  // 15. 门店维度：每家启用门店 ≥1 笔已支付订单；停用店零订单（普通单 + 网关单）
  const allStores = gen.all('mch_store_info')
  const paidByStore = countBy(orders.filter((o) => o.status === 'paid'), 'store_no')
  for (const s of allStores) {
    if (s.status === 'enable') {
      if ((paidByStore.get(s.store_no) ?? 0) < 1) fails.push(`启用门店 ${s.store_no} 无已支付订单`)
    } else {
      if (orders.some((o) => o.store_no === s.store_no)) fails.push(`停用门店 ${s.store_no} 存在普通订单`)
      if (gatewayOrders.some((g) => g.store_no === s.store_no)) fails.push(`停用门店 ${s.store_no} 存在网关订单`)
    }
  }

  // 16. 码牌维度：cashier_code 单 qr_code 非空且指向启用/同门店码牌；每张启用码牌 ≥1 笔回指；停用码牌零回指
  const allQrCodes = gen.all('device_qr_code')
  const qrByCode = new Map(allQrCodes.map((q) => [q.code, q]))
  const codeOrders = orders.filter((o) => o.source === 'cashier_code')
  for (const o of codeOrders) {
    const q = o.qr_code ? qrByCode.get(o.qr_code) : null
    if (!q) {
      fails.push(`码牌订单 ${o.order_no} qr_code=${o.qr_code} 找不到/未回指码牌`)
      continue
    }
    if (q.status !== 'enabled') fails.push(`码牌订单 ${o.order_no} 回指了停用码牌 ${q.code}`)
    if (q.store_no !== o.store_no) fails.push(`码牌订单 ${o.order_no} 门店 ${o.store_no} 与码牌 ${q.code} 门店 ${q.store_no} 不一致`)
  }
  const refQrCodes = new Set(codeOrders.map((o) => o.qr_code))
  for (const q of allQrCodes) {
    if (q.status === 'enabled' && !refQrCodes.has(q.code)) fails.push(`启用码牌 ${q.code} 无任何订单回指`)
    if (q.status !== 'enabled' && refQrCodes.has(q.code)) fails.push(`停用码牌 ${q.code} 被订单回指`)
  }
  // mch_api 单不得带码牌回指
  for (const o of orders) {
    if (o.source !== 'cashier_code' && o.qr_code != null) fails.push(`非码牌订单 ${o.order_no} 残留 qr_code=${o.qr_code}`)
  }

  // 17. 终端维度：terminal_no（非空时）存在/启用/属同门店；out_terminal_no 可在绑定链解析（submit 通道终端）
  const allTerminals = gen.all('device_terminal')
  const termByNo = new Map(allTerminals.map((t) => [t.terminal_no, t]))
  const allChannelTerminals = gen.all('device_channel_terminal')
  const ctById = new Map(allChannelTerminals.map((c) => [String(c.id), c]))
  const bindRows = gen.all('device_terminal_channel_bind')
  const resolvableOutNos = new Set(
    bindRows
      .map((b) => ctById.get(String(b.channel_terminal_id)))
      .filter((c) => c && c.status === 'submit' && c.out_terminal_no)
      .map((c) => c.out_terminal_no),
  )
  for (const o of orders) {
    if (o.terminal_no == null) continue
    const t = termByNo.get(o.terminal_no)
    if (!t) {
      fails.push(`订单 ${o.order_no} terminal_no=${o.terminal_no} 找不到商户终端`)
      continue
    }
    if (t.enable !== true) fails.push(`订单 ${o.order_no} 指向停用终端 ${t.terminal_no}`)
    if (t.store_no !== o.store_no) fails.push(`订单 ${o.order_no} 门店 ${o.store_no} 与终端 ${t.terminal_no} 门店 ${t.store_no} 不一致`)
    if (o.out_terminal_no != null && !resolvableOutNos.has(o.out_terminal_no)) {
      fails.push(`订单 ${o.order_no} out_terminal_no=${o.out_terminal_no} 无法在绑定链解析`)
    }
  }

  // 18. 虚拟设备 001 回执：msgId 按 `${tradeNo}:${deviceNo}` 可命中成功交易
  const allVirtuals = gen.all('device_virtual')
  const vdMain = allVirtuals.find((v) => v.device_no === ANCHORS.virtualDevices[0])
  if (vdMain && vdMain.last_pay_ack_ok === true) {
    const msgId = String(vdMain.last_pay_ack_msg_id ?? '')
    const tradeNo = msgId.split(':')[0]
    if (!msgId.endsWith(`:${ANCHORS.virtualDevices[0]}`)) {
      fails.push(`虚拟设备 ${ANCHORS.virtualDevices[0]} 回执 msgId=${msgId} 未按 \`\${tradeNo}:\${deviceNo}\` 格式`)
    }
    if (!trades.some((t) => t.trade_no === tradeNo && t.status === 'success')) {
      fails.push(`虚拟设备 ${ANCHORS.virtualDevices[0]} 回执 msgId=${msgId} 无法命中成功交易`)
    }
  }

  // 19. 网关订单：六态全覆盖、method 与类型自洽（cashier=browser 点选 qr 方式；aggregate=默认方式表）、门店落在启用店
  const gwEnabledStoreNos = new Set(allStores.filter((s) => s.status === 'enable').map((s) => s.store_no))
  for (const st of ['paid', 'paying', 'wait_pay', 'failed', 'closed', 'expired']) {
    if (!gatewayOrders.some((g) => g.status === st)) fails.push(`网关订单缺少状态 ${st}`)
  }
  const aggregateMethods = new Set(Object.values(CODE_DEFAULT_METHOD.h5))
  for (const g of gatewayOrders) {
    // 支付发起后的单（paid/paying/failed）才校验 method；wait_pay/closed/expired 为纯预下单形态 method 应为空
    const routed = ['paid', 'paying', 'failed'].includes(g.status)
    if (g.gateway_type === 'cashier') {
      if (routed && !['wechat_qr', 'alipay_qr'].includes(g.method)) fails.push(`收银台网关单 ${g.order_no} method=${g.method} 应为点选扫码方式`)
      if (!routed && g.method != null) fails.push(`未支付收银台单 ${g.order_no} 不应携带 method`)
      if (g.client_env !== 'browser') fails.push(`收银台网关单 ${g.order_no} client_env=${g.client_env} 应为 browser`)
    } else if (g.gateway_type === 'aggregate') {
      if (routed && !aggregateMethods.has(g.method)) fails.push(`聚合网关单 ${g.order_no} method=${g.method} 不在默认方式表内`)
      if (!routed && g.method != null) fails.push(`未支付聚合单 ${g.order_no} 不应携带 method`)
      if (!AGGREGATE_CLIENT_ENVS.includes(g.client_env)) fails.push(`聚合网关单 ${g.order_no} client_env=${g.client_env} 非法`)
    } else {
      fails.push(`网关单 ${g.order_no} gateway_type=${g.gateway_type} 非法`)
    }
    if (!gwEnabledStoreNos.has(g.store_no)) fails.push(`网关单 ${g.order_no} 门店 ${g.store_no} 非启用门店`)
  }
  // wait_pay 单过期时间必须在未来（现场可演示关单）
  for (const g of gatewayOrders.filter((x) => x.status === 'wait_pay')) {
    if (!String(g.expired_time?.sql ?? '').includes("now() + interval")) {
      fails.push(`待支付网关单 ${g.order_no} 过期时间未落在未来`)
    }
  }

  // 20. 异常订单：trade_no 指向存在的终态交易且金额/商户/应用一致；同一交易至多 1 条待处理
  const abnormals = gen.all('pay_abnormal_order')
  const tradeByNo = new Map(trades.map((t) => [t.trade_no, t]))
  const pendingAbnormal = new Set()
  for (const a of abnormals) {
    const t = tradeByNo.get(a.trade_no)
    if (!t) {
      fails.push(`异常订单 trade_no=${a.trade_no} 指向不存在的交易`)
      continue
    }
    if (a.amount !== t.amount) fails.push(`异常订单 trade_no=${a.trade_no} 金额 ${a.amount} ≠ 交易金额 ${t.amount}`)
    if (a.mch_no !== t.mch_no || a.app_id !== t.app_id) fails.push(`异常订单 trade_no=${a.trade_no} 商户/应用与交易不一致`)
    if (!['close', 'fail', 'cancel'].includes(t.status)) {
      fails.push(`异常订单 trade_no=${a.trade_no} 引用的交易非终态(${t.status})，无法演示确认成功`)
    }
    if (a.channel_status !== 'success') fails.push(`异常订单 trade_no=${a.trade_no} channel_status 应为 success`)
    if (a.handle_status === 'pending') {
      if (pendingAbnormal.has(a.trade_no)) fails.push(`交易 ${a.trade_no} 存在多条待处理异常单`)
      pendingAbnormal.add(a.trade_no)
    }
  }

  // ===== 通知全家桶 / 安全与平台 / 转账 / P2 审计断言（2026-11-10，防退化）=====

  // 允许引用的用户：bootx(id=1, [2] 段种子) + 剧本生成的 admin/csqysh
  const knownUserIds = new Set([1n, ...gen.all('iam_user_info').map((u) => u.id)])
  const knownUserAccounts = new Map([[1n, 'bootx'], ...gen.all('iam_user_info').map((u) => [u.id, u.account])])

  // 21. 通知域：公告状态覆盖、已读记录不悬空、商户账号留未读、消息用户存在且 link 形态合法
  const notices = gen.all('notify_notice')
  if (notices.length !== 6) fails.push(`公告数 ${notices.length} ≠ 6`)
  if (notices.filter((n) => n.status === 'published').length !== 4) fails.push('公告 published 应恰好 4 条（含 1 条已过期）')
  if (notices.filter((n) => n.status === 'draft').length !== 1) fails.push('公告 draft 应恰好 1 条')
  if (notices.filter((n) => n.status === 'offline').length !== 1) fails.push('公告 offline 应恰好 1 条')
  if (!notices.some((n) => n.severity === 'important' && n.status === 'published')) fails.push('公告缺 important 已发布样本')
  if (notices.filter((n) => n.is_top).length !== 1) fails.push('公告置顶应恰好 1 条')
  // 已过期样本：published 且 expire_time 为过去表达式（ago 产出 now() - interval）
  if (!notices.some((n) => n.status === 'published' && String(n.expire_time?.sql ?? '').includes('now() - interval'))) {
    fails.push('公告缺「published 但已过期」样本')
  }
  const noticeIds = new Set(notices.map((n) => n.id))
  const reads = gen.all('notify_notice_read')
  const readKeySeen = new Set()
  for (const r of reads) {
    if (!noticeIds.has(r.notice_id)) fails.push(`已读记录 notice_id=${r.notice_id} 指向不存在的公告`)
    if (!knownUserIds.has(r.user_id)) fails.push(`已读记录 user_id=${r.user_id} 指向不存在的用户`)
    const key = `${r.user_id}:${r.notice_id}`
    if (readKeySeen.has(key)) fails.push(`已读记录 (user,notice) 重复: ${key}`)
    readKeySeen.add(key)
    if (r.is_ignored !== true && r.read_time == null) fails.push(`已读记录 ${key} 非忽略却缺 read_time`)
  }
  // 商户账号（csqysh）至少 1 条未过期 published 公告未读（撑角标）
  const merchantUser = gen.all('iam_user_info').find((u) => u.client_code === 'merchant')
  if (merchantUser) {
    const readByMch = new Set(reads.filter((r) => r.user_id === merchantUser.id).map((r) => r.notice_id))
    const unreadVisible = notices.filter(
      (n) => n.status === 'published' && n.expire_time == null && !readByMch.has(n.id),
    )
    if (unreadVisible.length < 1) fails.push('商户账号无未读公告（角标演示缺样本）')
  }
  const messages = gen.all('notify_message')
  if (messages.length !== 4) fails.push(`个人消息数 ${messages.length} ≠ 4`)
  for (const m of messages) {
    if (!knownUserIds.has(m.user_id)) fails.push(`个人消息「${m.title}」user_id=${m.user_id} 指向不存在的用户`)
    if (m.link != null && !m.link.startsWith('/')) fails.push(`个人消息「${m.title}」link=${m.link} 应为站内路由（/ 开头）或空`)
  }
  const msgByUser = countBy(messages, 'user_id')
  for (const [uid, cnt] of msgByUser) {
    if (cnt !== 2) fails.push(`个人消息用户 ${uid} 应恰好 2 条（1 已读 + 1 未读），实际 ${cnt} 条`)
  }
  for (const uid of msgByUser.keys()) {
    const mine = messages.filter((m) => m.user_id === uid)
    if (!mine.some((m) => m.is_read) || !mine.some((m) => !m.is_read)) fails.push(`个人消息用户 ${uid} 应覆盖已读/未读两态`)
  }

  // 22. 黑名单红线（CRITICAL）：内网/回环/北京/订单 IP 池外地址一律禁止；类型仅 ip/province/city；唯一键不冲突
  const blacklists = gen.all('pay_blacklist')
  const bannedIpPrefixes = ['127.', '192.168.', '10.', '172.16.', '172.17.', '172.18.', '172.19.', '172.2', '172.30.', '172.31.', '0.', '::1', 'localhost']
  const poolIps = [...new Set(STORE_IP_POOLS.flat())]
  const blKeySeen = new Set()
  for (const b of blacklists) {
    if (!['ip', 'province', 'city'].includes(b.type)) fails.push(`黑名单 ${b.type}=${b.value} 类型越界（开源版仅 ip/province/city，openid/alipay_user 为商业版独占）`)
    const key = `${b.type}:${b.value}`
    if (blKeySeen.has(key)) fails.push(`黑名单 (type,value) 重复: ${key}`)
    blKeySeen.add(key)
    if (b.type === 'ip') {
      const v = String(b.value)
      if (bannedIpPrefixes.some((p) => v.startsWith(p))) fails.push(`黑名单拉黑了内网/回环地址 ${v}（本机演示扫码会被直接拦死）`)
      if (poolIps.includes(v) && v !== '203.0.113.10') fails.push(`黑名单拉黑了订单 IP 池地址 ${v}（池内仅 203.0.113.10 允许拉黑）`)
      if (!/^\d{1,3}(\.\d{1,3}){3}$/.test(v)) fails.push(`黑名单 IP 值 ${v} 非 IPv4 字面量`)
    } else {
      const v = String(b.value)
      if (v === '11' || v === '110000' || v.startsWith('11')) fails.push(`黑名单拉黑了北京地区码 ${v}（演示门店全部在北京）`)
      if (!/^\d{2,6}$/.test(v)) fails.push(`黑名单地区码 ${v} 非行政区划编码形态`)
    }
  }
  if (!blacklists.some((b) => b.status === 'disable')) fails.push('黑名单缺 disable 样本')
  if (!blacklists.some((b) => b.status === 'enable' && String(b.expire_time?.sql ?? '').includes('now() - interval'))) {
    fails.push('黑名单缺「已过期」样本（enable 但 expire_time 在过去）')
  }
  if (!blacklists.some((b) => b.type === 'province' && b.status === 'enable')) fails.push('黑名单缺省级 enable 样本')
  if (!blacklists.some((b) => b.type === 'city' && b.status === 'enable')) fails.push('黑名单缺市级 enable 样本')

  // 23. 风险命中与黑名单成对：blacklist_id 悬空 0、类型匹配、trade_no 可命中交易、phase/scene 枚举合法
  const hits = gen.all('pay_risk_hit')
  const blById = new Map(blacklists.map((b) => [String(b.id), b]))
  const tradeNos = new Set(trades.map((t) => t.trade_no))
  for (const h of hits) {
    if (!['before_pay', 'after_pay'].includes(h.phase)) fails.push(`风险命中 ${h.hit_value} phase=${h.phase} 非法`)
    if (!['api', 'gateway', 'code', 'manual', 'unknown'].includes(h.scene)) fails.push(`风险命中 ${h.hit_value} scene=${h.scene} 非法`)
    if (h.blacklist_id != null) {
      const bl = blById.get(String(h.blacklist_id))
      if (!bl) { fails.push(`风险命中 ${h.hit_value} blacklist_id=${h.blacklist_id} 悬空`); continue }
      if (bl.type !== h.hit_type) fails.push(`风险命中 ${h.hit_value}(${h.hit_type}) 关联到 ${bl.type} 名单（类型不匹配）`)
      if (bl.value !== h.hit_value) fails.push(`风险命中 hit_value=${h.hit_value} 与名单值 ${bl.value} 不一致`)
    } else if (h.hit_type !== 'overseas_ip') {
      fails.push(`风险命中 ${h.hit_type}:${h.hit_value} 无名单关联但非 overseas_ip（地域策略）`)
    }
    if (h.trade_no != null && !tradeNos.has(h.trade_no)) fails.push(`风险命中 ${h.hit_value} trade_no=${h.trade_no} 找不到交易`)
  }
  if (!hits.some((h) => h.blacklist_id != null && h.hit_type === 'ip')) fails.push('风险命中缺 IP 名单成对样本')
  if (!hits.some((h) => h.blacklist_id != null && h.hit_type === 'city')) fails.push('风险命中缺市级名单成对样本')
  if (!hits.some((h) => h.hit_type === 'overseas_ip' && h.blacklist_id == null)) fails.push('风险命中缺海外 IP 样本（blacklist_id 空）')
  if (!hits.some((h) => h.phase === 'after_pay')) fails.push('风险命中缺 after_pay 样本')

  // 24. 域名校验文件：verify_code 唯一、文件名匹配 Java 两种格式、平台/商户行归属自洽
  const verifies = gen.all('mch_wx_domain_verify')
  const WX_FILE_NAME_RE = /^MP_verify_([a-zA-Z0-9]+)\.txt$/
  const GENERIC_FILE_NAME_RE = /^([a-zA-Z0-9_]{1,60})\.(txt|html)$/
  const verifyCodeSeen = new Set()
  const fileNameSeen = new Set()
  for (const v of verifies) {
    const wxMatch = String(v.file_name).match(WX_FILE_NAME_RE)
    const genericMatch = String(v.file_name).match(GENERIC_FILE_NAME_RE)
    if (!wxMatch && !genericMatch) fails.push(`域名校验文件名 ${v.file_name} 不匹配网关路由/保存校验格式`)
    if (verifyCodeSeen.has(v.verify_code)) fails.push(`域名校验 verify_code 重复 ${v.verify_code}`)
    verifyCodeSeen.add(v.verify_code)
    if (fileNameSeen.has(v.file_name)) fails.push(`域名校验文件名重复 ${v.file_name}`)
    fileNameSeen.add(v.file_name)
    // verify_code 取值规则：微信形态取随机段，通用形态取文件名主体
    const expectCode = wxMatch ? wxMatch[1] : genericMatch[1]
    if (v.verify_code !== expectCode) fails.push(`域名校验 ${v.file_name} verify_code=${v.verify_code} 与文件名主体 ${expectCode} 不一致`)
    if (v.file_content !== v.verify_code) fails.push(`域名校验 ${v.file_name} file_content 应等于验证码`)
    if (v.platform) {
      if (v.mch_no !== '0') fails.push(`平台级域名校验 ${v.file_name} mch_no 应为 '0'（实际 ${v.mch_no}）`)
      if (!wxMatch && !genericMatch) fails.push(`平台级域名校验 ${v.file_name} 格式非法`)
    } else {
      if (v.mch_no !== ANCHORS.mchMain) fails.push(`商户级域名校验 ${v.file_name} mch_no 应为演示商户（实际 ${v.mch_no}）`)
      if (!wxMatch) fails.push(`商户级域名校验 ${v.file_name} 仅允许 MP_verify_*.txt 形态`)
    }
  }
  if (!verifies.some((v) => v.platform && v.file_name.endsWith('.html'))) fails.push('域名校验缺平台级 .html（支付宝形态）样本')

  // 25. 转账静态终态单：trade_no=容器 transfer_no、container_id/channel 成对、金额/状态一致、仅终态
  const transferTrades = gen.all('pay_transfer_trade')
  const CONTAINER_TABLES = {
    wechat: 'pay_transfer_order_wechat',
    alipay: 'pay_transfer_order_alipay',
    douyin: 'pay_transfer_order_douyin',
  }
  const containersByChannel = {
    wechat: gen.all('pay_transfer_order_wechat'),
    alipay: gen.all('pay_transfer_order_alipay'),
    douyin: gen.all('pay_transfer_order_douyin'),
  }
  const containerById = new Map(
    Object.entries(containersByChannel).flatMap(([ch, rows]) => rows.map((r) => [`${ch}:${r.id}`, { ch, r }])),
  )
  const containerKeySeen = new Set()
  for (const t of transferTrades) {
    const key = `${t.container_channel}:${t.container_id}`
    const c = containerById.get(key)
    if (!c) { fails.push(`转账凭证 ${t.trade_no} 容器 ${key} 不存在（container_id/channel 未成对闭环）`); continue }
    containerKeySeen.add(key)
    if (t.trade_no !== c.r.transfer_no) fails.push(`转账凭证 trade_no=${t.trade_no} ≠ 容器 transfer_no=${c.r.transfer_no}`)
    if (t.amount !== c.r.amount) fails.push(`转账凭证 ${t.trade_no} 金额 ${t.amount} ≠ 容器 ${c.r.amount}`)
    if (t.status !== c.r.status) fails.push(`转账凭证 ${t.trade_no} 状态 ${t.status} ≠ 容器 ${c.r.status}`)
    if (t.biz_transfer_no !== c.r.biz_transfer_no) fails.push(`转账凭证 ${t.trade_no} 商户转账号与容器不一致`)
    if (t.relation_no !== t.trade_no) fails.push(`转账凭证 ${t.trade_no} relation_no 应等于 trade_no（上送通道的商户转账号）`)
    if (!['success', 'fail'].includes(t.status)) fails.push(`转账凭证 ${t.trade_no} 非终态（mock 无 transfer 接口，禁止种非终态）`)
    if (t.status === 'success' && t.finish_time == null) fails.push(`成功转账 ${t.trade_no} 缺 finish_time`)
    if (c.r.status === 'fail' && c.r.error_msg == null) fails.push(`失败转账容器 ${c.r.transfer_no} 缺 error_msg`)
    if (t.mch_no !== ANCHORS.mchMain || t.app_id !== ANCHORS.appMain) fails.push(`转账凭证 ${t.trade_no} 商户/应用未指向演示商户`)
  }
  const totalContainers = Object.values(containersByChannel).reduce((a, l) => a + l.length, 0)
  if (containerKeySeen.size !== totalContainers) fails.push(`转账容器 ${totalContainers} 个，仅 ${containerKeySeen.size} 个有凭证回指`)
  for (const ch of Object.keys(containersByChannel)) {
    if (containersByChannel[ch].length !== 1) fails.push(`转账容器 ${CONTAINER_TABLES[ch]} 应恰好 1 条（实际 ${containersByChannel[ch].length}）`)
  }
  // 微信容器收款人必填、支付宝/抖音容器收款人类型/账号必填（NOT NULL 由 demo-gen 兜底，此处校验语义形态）
  for (const w of containersByChannel.wechat) {
    if (!w.payee_openid || w.payee_openid.length < 10) fails.push(`微信转账 ${w.transfer_no} payee_openid 形态异常`)
  }
  for (const a of containersByChannel.alipay) {
    if (a.payee_type !== 'user_id' && a.payee_type !== 'login_name') fails.push(`支付宝转账 ${a.transfer_no} payee_type=${a.payee_type} 非法`)
  }
  for (const d of containersByChannel.douyin) {
    if (!/^\d{4}$/.test(String(d.transfer_scene))) fails.push(`抖音转账 ${d.transfer_no} transfer_scene=${d.transfer_scene} 应为主数据场景 ID（1001-1007）`)
  }

  // 26. 平台配置：仅 website/url 两行且各 1 条、website 无 logo 字段、url 四地址齐全
  const platformConfigs = gen.all('system_platform_config')
  const configTypes = countBy(platformConfigs, 'config_type')
  if ((configTypes.get('website') ?? 0) !== 1) fails.push('平台配置 website 应恰好 1 行')
  if ((configTypes.get('url') ?? 0) !== 1) fails.push('平台配置 url 应恰好 1 行')
  for (const [t, cnt] of configTypes) {
    if (!['website', 'url'].includes(t)) fails.push(`平台配置混入手写 ${t} 行（安全类配置运行时 getOrCreateConfig 自动生成，禁止预置）`)
    if (cnt > 1) fails.push(`平台配置 ${t} 出现 ${cnt} 行（config_type 唯一）`)
  }
  const websiteCfg = platformConfigs.find((c) => c.config_type === 'website')
  if (websiteCfg && websiteCfg.config_data instanceof Json) {
    const keys = Object.keys(websiteCfg.config_data.value)
    if (keys.some((k) => k === 'logo' || k === 'logoDark')) fails.push('website 配置不得填 logo/logoDark（无 S3 对象必裂图）')
    if (!keys.includes('systemName')) fails.push('website 配置缺 systemName')
  }
  const urlCfg = platformConfigs.find((c) => c.config_type === 'url')
  if (urlCfg && urlCfg.config_data instanceof Json) {
    for (const k of ['adminBaseUrl', 'merchantBaseUrl', 'paymentGatewayBaseUrl', 'backendBaseUrl']) {
      if (!(k in urlCfg.config_data.value)) fails.push(`url 配置缺 ${k}`)
    }
  }

  // 27. P2 审计与敏感词：登录/操作日志账号在册且匹配、unipay 日志路径在册、敏感词命中指向种子词库
  const loginLogs = gen.all('starter_audit_login_log')
  if (loginLogs.length < 6) fails.push(`登录日志 ${loginLogs.length} 条 < 6`)
  if (!loginLogs.some((l) => l.login === false)) fails.push('登录日志缺密码失败样本')
  for (const l of loginLogs) {
    if (!knownUserIds.has(l.user_id)) fails.push(`登录日志 user_id=${l.user_id} 指向不存在的用户`)
    if (knownUserAccounts.get(l.user_id) !== l.account) fails.push(`登录日志 user_id=${l.user_id} 与账号 ${l.account} 不匹配`)
    if (l.login_location != null) fails.push(`登录日志 ${l.account} login_location 应留空（ip2region 缺失属预期）`)
  }
  const operateLogs = gen.all('starter_audit_operate_log')
  if (operateLogs.length < 4) fails.push(`操作日志 ${operateLogs.length} 条 < 4`)
  for (const o of operateLogs) {
    if (!knownUserIds.has(o.operate_id)) fails.push(`操作日志「${o.title}」operate_id=${o.operate_id} 指向不存在的用户`)
    if (knownUserAccounts.get(o.operate_id) !== o.account) fails.push(`操作日志「${o.title}」操作人与账号不匹配`)
  }
  const unipayLogs = gen.all('starter_audit_unipay_log')
  const KNOWN_API_PATHS = ['/unipay/pay', '/unipay/close', '/unipay/refund', '/unipay/query/pay-order', '/unipay/query/refund-order', '/unipay/sync/order/pay', '/unipay/sync/order/refund', '/unipay/gateway/pre-pay', '/unipay/gateway/query', '/unipay/ping', '/unipay/transfer']
  for (const u of unipayLogs) {
    if (!KNOWN_API_PATHS.includes(u.api_path)) fails.push(`unipay 日志 api_path=${u.api_path} 不在真实端点集内`)
    if (u.mch_no !== ANCHORS.mchMain) fails.push(`unipay 日志 ${u.api_path} mch_no 未指向演示商户`)
    if (u.success === true && u.error_code !== 0) fails.push(`unipay 日志 ${u.api_path} 成功行 error_code 应为 0`)
  }
  const sensitiveHits = gen.all('system_sensitive_word_hit')
  for (const s of sensitiveHits) {
    if (s.word_id !== 2077569027575820288n) fails.push(`敏感词命中 word_id=${s.word_id} 未指向种子测试词 2077569027575820288`)
    if (!['pay_title', 'pay_description', 'refund_reason', 'goods_name', 'mch_name', 'app_name', 'store_name', 'qr_name', 'notice', 'protocol', 'general'].includes(s.scene)) fails.push(`敏感词命中 scene=${s.scene} 非法`)
    if (!['admin', 'merchant', 'unipay', 'app_admin', 'unknown'].includes(s.source)) fails.push(`敏感词命中 source=${s.source} 非法`)
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

    -- 2. 订单 ↔ 统一交易 1:1（normal/gateway 两套容器分型回指）
    SELECT count(*) INTO n FROM public.pay_normal_order o
     WHERE o.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_trade t WHERE t.container_id = o.id AND t.trade_type = 'normal' AND t.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔普通订单缺统一交易记录', n; END IF;
    SELECT count(*) INTO n FROM public.pay_trade t
     WHERE t.deleted = false AND t.trade_type = 'normal'
       AND NOT EXISTS (SELECT 1 FROM public.pay_normal_order o WHERE o.id = t.container_id AND o.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔 normal 交易找不到普通订单', n; END IF;
    -- 网关侧：gateway 交易回指网关订单；paid/paying/failed 网关单恰好 1 笔交易，wait_pay/closed/expired 不建交易
    SELECT count(*) INTO n FROM public.pay_trade t
     WHERE t.deleted = false AND t.trade_type = 'gateway'
       AND NOT EXISTS (SELECT 1 FROM public.pay_gateway_order g WHERE g.id = t.container_id AND g.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔 gateway 交易找不到网关订单', n; END IF;
    SELECT count(*) INTO n FROM public.pay_gateway_order g
     WHERE g.deleted = false AND g.status IN ('paid', 'paying', 'failed')
       AND (SELECT count(*) FROM public.pay_trade t
             WHERE t.container_id = g.id AND t.trade_type = 'gateway' AND t.deleted = false) <> 1;
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔网关订单（paid/paying/failed）交易数不为 1', n; END IF;
    SELECT count(*) INTO n FROM public.pay_gateway_order g
     WHERE g.deleted = false AND g.status IN ('wait_pay', 'closed', 'expired')
       AND EXISTS (SELECT 1 FROM public.pay_trade t WHERE t.container_id = g.id AND t.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔未支付网关订单残留交易', n; END IF;

    -- 3. 金额守恒：订单金额 = 交易金额 = 入账金额（normal/gateway 分容器）；非成功交易不得有入账
    SELECT count(*) INTO n FROM public.pay_trade t
      JOIN public.pay_normal_order o ON o.id = t.container_id
     WHERE t.status = 'success' AND t.trade_type = 'normal'
       AND (o.amount <> t.amount OR t.posted_amount <> t.amount);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔交易金额与订单不一致', n; END IF;
    SELECT count(*) INTO n FROM public.pay_trade t
      JOIN public.pay_gateway_order g ON g.id = t.container_id
     WHERE t.status = 'success' AND t.trade_type = 'gateway'
       AND (g.amount <> t.amount OR t.posted_amount <> t.amount);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔网关交易金额与网关订单不一致', n; END IF;
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

    -- 7. 路由策略收敛为唯一 basic 行：覆盖 5 个支付渠道且全部指向 mock 通道商户
    --    （双策略行会让运行时 oneOpt 抛 TooManyResultsException，2026-11-10 起演示环境不铺 scene 策略）
    SELECT count(*) INTO n FROM public.pay_route_strategy
     WHERE app_id = '${ANCHORS.appMain}' AND mode = 'basic' AND deleted = false;
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] 路由策略 basic 行数 %（预期恰好 1）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_route_strategy
     WHERE app_id = '${ANCHORS.appMain}' AND deleted = false AND mode <> 'basic';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] 存在 % 条非 basic 路由策略（双策略行触发运行时 oneOpt 异常）', n; END IF;
    SELECT count(DISTINCT b.provider) INTO n
      FROM public.pay_route_basic_config b
      JOIN public.pay_route_strategy s ON s.id = b.strategy_id
     WHERE s.app_id = '${ANCHORS.appMain}' AND s.mode = 'basic' AND s.deleted = false AND b.deleted = false;
    IF n < 5 THEN RAISE EXCEPTION '[演示数据校验] 路由渠道覆盖不足: %（预期 aggregate_pay/wechat/alipay/union_pay/douyin 共 5 个）', n; END IF;
    SELECT count(*) INTO n
      FROM public.pay_route_basic_config b
      JOIN public.pay_route_strategy s ON s.id = b.strategy_id
     WHERE s.app_id = '${ANCHORS.appMain}' AND s.mode = 'basic' AND s.deleted = false AND b.deleted = false
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
    SELECT count(*) INTO n FROM public.mock_gateway_credential
     WHERE id = 1 AND deleted = false
       AND COALESCE(merchant_private_key, '') <> '' AND COALESCE(platform_public_key, '') <> '';
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] mock 全局凭证（id=1）缺失或 PEM 为空'; END IF;

    -- 11. mock 直连凭证两段 PEM 非空且网关地址留空（回落子应用 yml，避免钉死本机地址）
    SELECT count(*) INTO n FROM public.mock_pay_credential
     WHERE channel_mch_no = '${ANCHORS.mockChannelMchNo}' AND deleted = false
       AND COALESCE(merchant_private_key, '') <> '' AND COALESCE(platform_public_key, '') <> '';
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] mock 直连凭证 ${ANCHORS.mockChannelMchNo} 缺失或 PEM 为空'; END IF;
    SELECT count(*) INTO n FROM public.mock_pay_credential
     WHERE channel_mch_no = '${ANCHORS.mockChannelMchNo}' AND deleted = false
       AND gateway_base_url IS NOT NULL;
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] mock 直连凭证 gateway_base_url 应留空（当前非空）'; END IF;

    -- 12. mock 产品生效环境为沙箱（与演示通道商户 sandbox=true 匹配，否则路由报环境不一致）
    SELECT count(*) INTO n FROM public.pay_md_product_config
     WHERE product = 'mock_pay' AND active_env = 'sandbox' AND deleted = false;
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] pay_md_product_config 缺 mock_pay 沙箱生效配置'; END IF;

    -- 13. 门店规模与形态（≥5 家，含停用店；默认门店恰好 1 家）
    SELECT count(*) INTO n FROM public.mch_store_info
     WHERE mch_no = '${ANCHORS.mchMain}' AND deleted = false;
    IF n < 5 THEN RAISE EXCEPTION '[演示数据校验] 演示门店数 % 家（预期 ≥ 5）', n; END IF;
    SELECT count(*) INTO n FROM public.mch_store_info
     WHERE mch_no = '${ANCHORS.mchMain}' AND status = 'disabled' AND deleted = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺停用门店（status=disabled）'; END IF;
    SELECT count(*) INTO n FROM public.mch_store_info
     WHERE mch_no = '${ANCHORS.mchMain}' AND default_store = true AND deleted = false;
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] 默认门店数 %（预期恰好 1）', n; END IF;

    -- 14. 商户终端规模与停用形态（≥5 台，含 enable=false；每门店默认终端唯一由部分唯一索引保证）
    SELECT count(*) INTO n FROM public.device_terminal
     WHERE mch_no = '${ANCHORS.mchMain}' AND deleted = false;
    IF n < 5 THEN RAISE EXCEPTION '[演示数据校验] 商户终端数 %（预期 ≥ 5）', n; END IF;
    SELECT count(*) INTO n FROM public.device_terminal
     WHERE mch_no = '${ANCHORS.mchMain}' AND enable = false AND deleted = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺停用商户终端（enable=false）'; END IF;

    -- 15. 通道终端状态覆盖（≥3 种登记状态，撑起状态筛选）
    SELECT count(DISTINCT status) INTO n FROM public.device_channel_terminal
     WHERE channel_mch_no = '${ANCHORS.mockChannelMchNo}' AND deleted = false;
    IF n < 3 THEN RAISE EXCEPTION '[演示数据校验] 通道终端状态覆盖 % 种（预期 ≥ 3）', n; END IF;

    -- 16. 终端绑定关系存在且双向引用完整（防悬空）
    SELECT count(*) INTO n FROM public.device_terminal_channel_bind b
     WHERE b.deleted = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺终端绑定关系'; END IF;
    SELECT count(*) INTO n FROM public.device_terminal_channel_bind b
     WHERE b.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.device_terminal t
                        WHERE t.terminal_no = b.system_terminal_no AND t.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条终端绑定指向不存在的商户终端', n; END IF;
    SELECT count(*) INTO n FROM public.device_terminal_channel_bind b
     WHERE b.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.device_channel_terminal c
                        WHERE c.id = b.channel_terminal_id AND c.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条终端绑定指向不存在的通道终端', n; END IF;

    -- 17. 码牌规模与形态（≥8 张，h5 与停用各 ≥1，批次 ≥2）
    SELECT count(*) INTO n FROM public.device_qr_code
     WHERE mch_no = '${ANCHORS.mchMain}' AND deleted = false;
    IF n < 8 THEN RAISE EXCEPTION '[演示数据校验] 演示码牌数 %（预期 ≥ 8）', n; END IF;
    SELECT count(*) INTO n FROM public.device_qr_code
     WHERE mch_no = '${ANCHORS.mchMain}' AND program_type = 'h5' AND deleted = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺 h5 落地码牌'; END IF;
    SELECT count(*) INTO n FROM public.device_qr_code
     WHERE mch_no = '${ANCHORS.mchMain}' AND status = 'disabled' AND deleted = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺停用码牌（status=disabled）'; END IF;
    SELECT count(DISTINCT batch_no) INTO n FROM public.device_qr_code
     WHERE mch_no = '${ANCHORS.mchMain}' AND deleted = false;
    IF n < 2 THEN RAISE EXCEPTION '[演示数据校验] 码牌批次 % 个（预期 ≥ 2）', n; END IF;

    -- 18. 网关支付配置（level=auto 主行存在；客户端环境策略不悬空）
    SELECT count(*) INTO n FROM public.pay_gateway_pay_config
     WHERE app_id = '${ANCHORS.appMain}' AND level = 'auto' AND deleted = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺网关支付配置（level=auto）'; END IF;
    SELECT count(*) INTO n FROM public.pay_gateway_pay_client_env e
     WHERE e.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_gateway_pay_config c
                        WHERE c.id = e.config_id AND c.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条客户端环境策略悬空（config_id 无主行）', n; END IF;

    -- 19. 收银台支付项规模（h5/web/mini 至少各撑起一档）
    SELECT count(*) INTO n FROM public.pay_gateway_cashier_item
     WHERE app_id = '${ANCHORS.appMain}' AND deleted = false;
    IF n < 3 THEN RAISE EXCEPTION '[演示数据校验] 收银台支付项 % 条（预期 ≥ 3）', n; END IF;

    -- 20. 用户协议版本形态（DRAFT 草稿与 ARCHIVED 归档各 ≥1，且挂靠真实协议行）
    SELECT count(*) INTO n FROM public.base_user_protocol_version v
     JOIN public.base_user_protocol p ON p.id = v.protocol_id AND p.deleted = false
     WHERE v.status = 'DRAFT' AND v.deleted = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺协议草稿版本（status=DRAFT）'; END IF;
    SELECT count(*) INTO n FROM public.base_user_protocol_version v
     JOIN public.base_user_protocol p ON p.id = v.protocol_id AND p.deleted = false
     WHERE v.status = 'ARCHIVED' AND v.deleted = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺协议归档版本（status=ARCHIVED）'; END IF;

    -- ===== 四维度打通与跨域闭环校验（2026-11-10，与 checkConsistency 15~20 对应）=====

    -- 21. 门店维度：每家启用门店 ≥1 笔已支付订单；停用店零订单（普通单 + 网关单）
    SELECT count(*) INTO n FROM public.mch_store_info s
     WHERE s.mch_no = '${ANCHORS.mchMain}' AND s.status = 'enable' AND s.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_normal_order o
                        WHERE o.store_no = s.store_no AND o.status = 'paid' AND o.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 家启用门店无已支付订单', n; END IF;
    SELECT count(*) INTO n FROM public.pay_normal_order o
     JOIN public.mch_store_info s ON s.store_no = o.store_no AND s.deleted = false
     WHERE o.mch_no = '${ANCHORS.mchMain}' AND o.deleted = false AND s.status <> 'enable';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔普通订单落在停用门店', n; END IF;
    SELECT count(*) INTO n FROM public.pay_gateway_order g
     JOIN public.mch_store_info s ON s.store_no = g.store_no AND s.deleted = false
     WHERE g.mch_no = '${ANCHORS.mchMain}' AND g.deleted = false AND s.status <> 'enable';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔网关订单落在停用门店', n; END IF;

    -- 22. 码牌维度：cashier_code 单回指启用且同门店的码牌；每张启用码牌 ≥1 笔回指；停用码牌零回指
    SELECT count(*) INTO n FROM public.pay_normal_order o
     WHERE o.source = 'cashier_code' AND o.deleted = false
       AND (o.qr_code IS NULL OR o.store_no IS NULL
            OR NOT EXISTS (SELECT 1 FROM public.device_qr_code q
                            WHERE q.code = o.qr_code AND q.status = 'enabled'
                              AND q.store_no = o.store_no AND q.deleted = false));
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔码牌订单 qr_code 悬空/指向停用码牌/门店不一致', n; END IF;
    SELECT count(*) INTO n FROM public.device_qr_code q
     WHERE q.mch_no = '${ANCHORS.mchMain}' AND q.status = 'enabled' AND q.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_normal_order o
                        WHERE o.qr_code = q.code AND o.source = 'cashier_code' AND o.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 张启用码牌无收款订单回指', n; END IF;
    SELECT count(*) INTO n FROM public.pay_normal_order o
     JOIN public.device_qr_code q ON q.code = o.qr_code AND q.deleted = false
     WHERE o.deleted = false AND q.status <> 'enabled';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔订单回指停用码牌', n; END IF;

    -- 23. 终端维度：terminal_no 存在/启用/属同门店；out_terminal_no 可在绑定链解析（submit 通道终端）
    SELECT count(*) INTO n FROM public.pay_normal_order o
     WHERE o.terminal_no IS NOT NULL AND o.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.device_terminal t
                        WHERE t.terminal_no = o.terminal_no AND t.enable = true
                          AND t.store_no = o.store_no AND t.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔订单终端号悬空/停用/门店不一致', n; END IF;
    SELECT count(*) INTO n FROM public.pay_normal_order o
     WHERE o.out_terminal_no IS NOT NULL AND o.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.device_channel_terminal c
                        WHERE c.out_terminal_no = o.out_terminal_no AND c.status = 'submit' AND c.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔订单通道终端号无法在绑定链解析', n; END IF;

    -- 24. 虚拟设备 001 播报回执：msgId 按「交易号:设备号」格式可命中成功交易
    SELECT count(*) INTO n FROM public.device_virtual v
     WHERE v.device_no = '${ANCHORS.virtualDevices[0]}' AND v.deleted = false AND v.last_pay_ack_ok = true
       AND (v.last_pay_ack_msg_id IS NULL
            OR position(':' || v.device_no IN v.last_pay_ack_msg_id) <> length(v.last_pay_ack_msg_id) - length(v.device_no)
            OR NOT EXISTS (SELECT 1 FROM public.pay_trade t
                            WHERE t.trade_no = split_part(v.last_pay_ack_msg_id, ':', 1)
                              AND t.status = 'success' AND t.deleted = false));
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] 虚拟设备 ${ANCHORS.virtualDevices[0]} 播报回执 msgId 无法命中成功交易（应为 交易号:设备号 格式）'; END IF;

    -- 25. 网关订单：六态全覆盖、wait_pay 过期时间在未来、paid 单金额与 gateway 交易一致
    SELECT count(DISTINCT status) INTO n FROM public.pay_gateway_order
     WHERE mch_no = '${ANCHORS.mchMain}' AND deleted = false;
    IF n < 6 THEN RAISE EXCEPTION '[演示数据校验] 网关订单状态覆盖 % 种（预期 6：paid/paying/wait_pay/failed/closed/expired）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_gateway_order
     WHERE status = 'wait_pay' AND deleted = false AND expired_time IS NOT NULL AND expired_time <= now();
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔待支付网关订单过期时间不在未来（无法现场演示关单）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_gateway_order g
     JOIN public.pay_trade t ON t.container_id = g.id AND t.trade_type = 'gateway' AND t.deleted = false
     WHERE g.status = 'paid' AND g.deleted = false AND (t.status <> 'success' OR t.amount <> g.amount);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 笔已支付网关订单与 gateway 交易状态/金额不一致', n; END IF;

    -- 26. 异常订单：trade_no 指向存在交易且金额/商户一致、channel_status=success、pending 单引用终态交易
    SELECT count(*) INTO n FROM public.pay_abnormal_order a
     WHERE a.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_trade t WHERE t.trade_no = a.trade_no AND t.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条异常订单指向不存在的交易', n; END IF;
    SELECT count(*) INTO n FROM public.pay_abnormal_order a
     JOIN public.pay_trade t ON t.trade_no = a.trade_no AND t.deleted = false
     WHERE a.deleted = false AND (a.amount <> t.amount OR a.mch_no <> t.mch_no OR a.app_id <> t.app_id);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条异常订单与交易金额/商户/应用不一致', n; END IF;
    SELECT count(*) INTO n FROM public.pay_abnormal_order a
     JOIN public.pay_trade t ON t.trade_no = a.trade_no AND t.deleted = false
     WHERE a.deleted = false AND a.channel_status <> 'success';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条异常订单 channel_status 不为 success', n; END IF;
    SELECT count(*) INTO n FROM public.pay_abnormal_order a
     JOIN public.pay_trade t ON t.trade_no = a.trade_no AND t.deleted = false
     WHERE a.deleted = false AND a.handle_status = 'pending' AND t.status NOT IN ('close', 'fail', 'cancel');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条待处理异常订单引用非终态交易（确认成功 CAS 会失败）', n; END IF;

    -- ===== 通知全家桶 / 安全与平台 / 转账 / P2 审计校验（2026-11-10，与 checkConsistency 21~27 对应）=====

    -- 27. 公告状态覆盖（published×4 含 1 条已过期 / draft / offline / important / 置顶 1）+ 已读记录不悬空
    SELECT count(*) INTO n FROM public.notify_notice WHERE deleted = false;
    IF n <> 6 THEN RAISE EXCEPTION '[演示数据校验] 公告数 %（预期 6）', n; END IF;
    SELECT count(*) INTO n FROM public.notify_notice
     WHERE deleted = false AND status = 'published';
    IF n <> 4 THEN RAISE EXCEPTION '[演示数据校验] 已发布公告 %（预期 4，含 1 条已过期）', n; END IF;
    SELECT count(*) INTO n FROM public.notify_notice
     WHERE deleted = false AND status = 'published' AND expire_time <= now();
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺「已发布但已过期」公告（用户端不可见/管理端可见）'; END IF;
    SELECT count(*) INTO n FROM public.notify_notice
     WHERE deleted = false AND severity = 'important' AND status = 'published';
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺 important 级已发布公告'; END IF;
    SELECT count(*) INTO n FROM public.notify_notice WHERE deleted = false AND is_top = true;
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] 置顶公告 %（预期恰好 1）', n; END IF;
    SELECT count(*) INTO n FROM public.notify_notice_read r
     WHERE NOT EXISTS (SELECT 1 FROM public.notify_notice x WHERE x.id = r.notice_id AND x.deleted = false)
        OR NOT EXISTS (SELECT 1 FROM public.iam_user_info u WHERE u.id = r.user_id AND u.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条公告已读记录悬空（公告/用户不存在）', n; END IF;
    SELECT count(*) INTO n FROM public.notify_message m
     WHERE NOT EXISTS (SELECT 1 FROM public.iam_user_info u WHERE u.id = m.user_id AND u.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条个人消息指向不存在用户', n; END IF;
    SELECT count(*) INTO n FROM public.notify_message
     WHERE deleted = false AND link IS NOT NULL AND link NOT LIKE '/%';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条个人消息 link 非「/」开头站内路由', n; END IF;

    -- 28. 黑名单红线（CRITICAL）：内网/回环/北京/订单 IP 池外地址一律不得拉黑；类型仅 ip/province/city
    SELECT count(*) INTO n FROM public.pay_blacklist
     WHERE deleted = false AND type = 'ip'
       AND (value LIKE '127.%' OR value LIKE '192.168.%' OR value LIKE '10.%'
            OR value LIKE '172.16.%' OR value LIKE '172.17.%' OR value LIKE '172.18.%'
            OR value LIKE '172.19.%' OR value LIKE '172.2%' OR value LIKE '172.30.%' OR value LIKE '172.31.%'
            OR value LIKE '0.%' OR value = '::1' OR value = 'localhost');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条黑名单拉黑内网/回环段（本机演示扫码会被拦死）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_blacklist
     WHERE deleted = false AND type = 'ip' AND value <> '203.0.113.10'
       AND value IN ('203.0.113.10','203.0.113.11','192.168.1.35','203.0.113.24','198.51.100.7','192.168.1.36',
                     '198.51.100.23','198.51.100.24','203.0.113.99','192.168.1.37');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条黑名单拉黑订单 IP 池地址（池内仅 203.0.113.10 允许）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_blacklist
     WHERE deleted = false AND type IN ('province', 'city') AND (value LIKE '11%');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条黑名单拉黑北京地区码（演示门店全部在北京）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_blacklist
     WHERE deleted = false AND type NOT IN ('ip', 'province', 'city');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条黑名单类型越界（开源版仅 ip/province/city）', n; END IF;

    -- 29. 风险命中与黑名单成对：blacklist_id 悬空 0、类型/取值匹配、trade_no 可命中交易
    SELECT count(*) INTO n FROM public.pay_risk_hit h
     WHERE h.deleted = false AND h.blacklist_id IS NOT NULL
       AND NOT EXISTS (SELECT 1 FROM public.pay_blacklist b WHERE b.id = h.blacklist_id AND b.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条风险命中 blacklist_id 悬空', n; END IF;
    SELECT count(*) INTO n FROM public.pay_risk_hit h
     JOIN public.pay_blacklist b ON b.id = h.blacklist_id AND b.deleted = false
     WHERE h.deleted = false AND (h.hit_type <> b.type OR h.hit_value <> b.value);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条风险命中与名单类型/取值不匹配', n; END IF;
    SELECT count(*) INTO n FROM public.pay_risk_hit h
     WHERE h.deleted = false AND h.blacklist_id IS NULL AND h.hit_type <> 'overseas_ip';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条无名单关联的非海外命中（blacklist_id 空仅限 overseas_ip）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_risk_hit h
     WHERE h.deleted = false AND h.trade_no IS NOT NULL
       AND NOT EXISTS (SELECT 1 FROM public.pay_trade t WHERE t.trade_no = h.trade_no AND t.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条风险命中 trade_no 悬空', n; END IF;
    SELECT count(*) INTO n FROM public.pay_risk_hit
     WHERE deleted = false AND phase = 'before_pay' AND blacklist_id IS NOT NULL;
    IF n < 2 THEN RAISE EXCEPTION '[演示数据校验] 黑名单成对命中样本 % 条（before_pay 预期 ≥ 2）', n; END IF;

    -- 30. 域名校验：文件名格式、verify_code 唯一且等于文件名主体、平台/商户行归属
    SELECT count(*) INTO n FROM public.mch_wx_domain_verify
     WHERE deleted = false
       AND file_name !~ '^MP_verify_[a-zA-Z0-9]+\.txt$'
       AND file_name !~ '^[a-zA-Z0-9_]{1,60}\.(txt|html)$';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条域名校验文件名不匹配网关格式', n; END IF;
    SELECT count(*) INTO n FROM public.mch_wx_domain_verify
     WHERE deleted = false AND (
         (file_name ~ '^MP_verify_' AND verify_code <> regexp_replace(file_name, '^MP_verify_([a-zA-Z0-9]+)\.txt$', '\\1'))
      OR (file_name !~ '^MP_verify_' AND verify_code <> split_part(file_name, '.', 1))
      OR file_content <> verify_code);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条域名校验 verify_code/file_content 与文件名主体不一致', n; END IF;
    SELECT count(*) INTO n FROM public.mch_wx_domain_verify
     WHERE deleted = false AND platform = true AND mch_no <> '0';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条平台级域名校验 mch_no ≠ ''0''', n; END IF;
    SELECT count(*) INTO n FROM public.mch_wx_domain_verify
     WHERE deleted = false AND platform = false AND mch_no <> '${ANCHORS.mchMain}';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条商户级域名校验未指向演示商户', n; END IF;
    SELECT count(*) INTO n FROM public.mch_wx_domain_verify
     WHERE deleted = false AND file_name LIKE '%.html';
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 缺支付宝 .html 形态域名校验样本'; END IF;

    -- 31. 转账静态终态单：trade_no=容器 transfer_no、容器/凭证金额状态一致、仅终态、容器全部有凭证
    SELECT count(*) INTO n FROM public.pay_transfer_trade t
     JOIN public.pay_transfer_order_wechat c ON c.id = t.container_id
     WHERE t.deleted = false AND t.container_channel = 'wechat'
       AND (t.trade_no <> c.transfer_no OR t.amount <> c.amount OR t.status <> c.status OR t.biz_transfer_no <> c.biz_transfer_no);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条微信转账凭证与容器不一致', n; END IF;
    SELECT count(*) INTO n FROM public.pay_transfer_trade t
     JOIN public.pay_transfer_order_alipay c ON c.id = t.container_id
     WHERE t.deleted = false AND t.container_channel = 'alipay'
       AND (t.trade_no <> c.transfer_no OR t.amount <> c.amount OR t.status <> c.status OR t.biz_transfer_no <> c.biz_transfer_no);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条支付宝转账凭证与容器不一致', n; END IF;
    SELECT count(*) INTO n FROM public.pay_transfer_trade t
     JOIN public.pay_transfer_order_douyin c ON c.id = t.container_id
     WHERE t.deleted = false AND t.container_channel = 'douyin'
       AND (t.trade_no <> c.transfer_no OR t.amount <> c.amount OR t.status <> c.status OR t.biz_transfer_no <> c.biz_transfer_no);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条抖音转账凭证与容器不一致', n; END IF;
    SELECT count(*) INTO n FROM public.pay_transfer_trade
     WHERE deleted = false AND status NOT IN ('success', 'fail');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条转账凭证非终态（mock 无 transfer 接口，同步按钮会调用失败）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_transfer_trade t
     WHERE t.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.pay_transfer_order_wechat c WHERE c.id = t.container_id AND c.deleted = false
                        AND t.container_channel = 'wechat')
       AND NOT EXISTS (SELECT 1 FROM public.pay_transfer_order_alipay c WHERE c.id = t.container_id AND c.deleted = false
                        AND t.container_channel = 'alipay')
       AND NOT EXISTS (SELECT 1 FROM public.pay_transfer_order_douyin c WHERE c.id = t.container_id AND c.deleted = false
                        AND t.container_channel = 'douyin');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条转账凭证容器悬空（container_id/channel 未成对）', n; END IF;
    SELECT count(*) INTO n FROM public.pay_transfer_order_wechat c
     WHERE c.deleted = false AND c.status = 'fail' AND COALESCE(c.error_msg, '') = '';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条失败微信转账容器缺 error_msg', n; END IF;

    -- 32. 平台配置：仅 website/url 各 1 行（安全类运行时自动生成，禁止手写预置）
    SELECT count(*) INTO n FROM public.system_platform_config
     WHERE deleted = false AND config_type NOT IN ('website', 'url');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 行平台配置越界（安全类配置由 getOrCreateConfig 运行时生成，禁止预置）', n; END IF;
    SELECT count(*) INTO n FROM public.system_platform_config
     WHERE deleted = false AND config_type = 'website';
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] website 配置 % 行（预期恰好 1）', n; END IF;
    SELECT count(*) INTO n FROM public.system_platform_config
     WHERE deleted = false AND config_type = 'url';
    IF n <> 1 THEN RAISE EXCEPTION '[演示数据校验] url 配置 % 行（预期恰好 1）', n; END IF;
    SELECT count(*) INTO n FROM public.system_platform_config
     WHERE deleted = false AND config_type = 'website'
       AND (config_data ? 'logo' OR config_data ? 'logoDark');
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 行 website 配置含 logo 字段（无 S3 对象必裂图）', n; END IF;

    -- 33. 审计三件套规模与账号匹配（登录/操作/unipay）
    SELECT count(*) INTO n FROM public.starter_audit_login_log
     WHERE deleted = false AND login = false;
    IF n < 1 THEN RAISE EXCEPTION '[演示数据校验] 登录日志缺失败样本'; END IF;
    SELECT count(*) INTO n FROM public.starter_audit_login_log l
     WHERE l.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.iam_user_info u WHERE u.id = l.user_id AND u.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条登录日志指向不存在用户', n; END IF;
    SELECT count(*) INTO n FROM public.starter_audit_operate_log
     WHERE deleted = false;
    IF n < 4 THEN RAISE EXCEPTION '[演示数据校验] 操作日志 % 条（预期 ≥ 4）', n; END IF;
    SELECT count(*) INTO n FROM public.starter_audit_unipay_log
     WHERE mch_no <> '${ANCHORS.mchMain}';
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条 unipay 日志未指向演示商户', n; END IF;

    -- 34. 敏感词命中指向种子词库（system_sensitive_word 来自 [4.2] 段旁路种子）
    SELECT count(*) INTO n FROM public.system_sensitive_word_hit h
     WHERE h.deleted = false
       AND NOT EXISTS (SELECT 1 FROM public.system_sensitive_word w WHERE w.id = h.word_id AND w.deleted = false);
    IF n > 0 THEN RAISE EXCEPTION '[演示数据校验] % 条敏感词命中指向不存在的词库词条', n; END IF;
    SELECT count(*) INTO n FROM public.system_sensitive_word_hit
     WHERE deleted = false;
    IF n < 2 THEN RAISE EXCEPTION '[演示数据校验] 敏感词命中 % 条（预期 ≥ 2）', n; END IF;
END $$;`
}
