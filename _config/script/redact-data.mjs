#!/usr/bin/env node
/**
 * Data SQL 敏感数据清除脚本（白名单架构 + 内置演示账号/商户）
 *
 * 用途：生成干净安装包的数据种子，供他人导入。
 * 策略：保留系统种子表（整表）+ 内置演示账号与商户（行级），其余整表清除；
 *       演示账号密码统一重置为开发默认密码(见 DEMO_PASSWORD_HASH)。
 *
 * 四原则决策：
 *   ① 表 ∈ KEEP_TABLES（系统种子）              → 整表原样保留
 *   ② 表 ∈ DEMO_ROW_IDS 且首列 id 命中白名单     → 该行保留（同表其余行清除）；
 *      其中 iam_user_info 的密码列替换为开发默认密码哈希
 *      （开发库密码常被改动，原样保留会导致每次导出的 data.sql 登录密码漂移）
 *   ③ 内置超管 bootx(id=1) 的两张关联表          → 见 ②（id=1 在 DEMO_ROW_IDS 内）
 *      （iam_user_info 账号主表 + iam_user_expand_info 扩展信息，
 *       后者登录后拉用户信息必需，缺则抛 UserInfoNotExistsException）
 *   ④ 其余                                   → 整表跳过（写审计注释）
 *
 * 内置演示账号/商户（2026-09-11 定稿，用于对外演示）：
 *   - csadmin  运营端管理员（role 1 admin_admin）
 *   - csqysh   商户端管理员（role 2 merchant_admin），名下「示例商户」
 *   商户以「全新形态」交付：只保留商户本体 + 管理员账号 + 商户账号绑定
 *   + 1 个默认应用 + 1 个默认门店，不含任何通道/产品/路由/订单等已配置数据。
 *   账号另需 iam_user_password_security 行且 initial_password=false、
 *   password_expire_time 为极远未来——非超管账号缺行会被 PasswordStatusCheck
 *   判为初始密码并 40302 拦到改密页，直接进不去系统。
 *
 * 格式假设：pg_dump --inserts（INSERT INTO public.表名 VALUES (...)，无列名）。
 *           自动处理跨多行 INSERT（值内含换行，累积到行尾 ");" 视为语句结束）。
 * 兼容处理：自动跳过 \restrict / \unrestrict 元命令与 SET transaction_timeout——
 *          均为 pg_dump 18 客户端产物，前者在非 psql 环境报错、后者 PG17+ 才有
 *          （服务端 PG16 会报 unrecognized configuration parameter），故无需外层 sed 预处理。
 *
 * 用法：node redact-data.mjs <input.sql> <output.sql>
 * 注意：input 必须是 pg_dump 原始 dump，勿对本脚本产物重复处理
 *      （产物头部注释属非 INSERT 行会被透传，重复处理会叠加出双头部）。
 */

import { createReadStream, createWriteStream } from 'node:fs'
import { createInterface } from 'node:readline'
import { argv, exit } from 'node:process'

// ---------- 参数 ----------
const positional = argv.slice(2).filter((a) => !a.startsWith('--'))
if (positional.length < 2) {
  console.error('用法：node redact-data.mjs <input.sql> <output.sql>')
  exit(1)
}
const [inputPath, outputPath] = positional

// ---------- 白名单：系统种子表（整表保留） ----------
const KEEP_TABLES = new Set([
  // 权限/菜单/角色
  'iam_perm_code',
  'iam_perm_menu',
  'iam_role',
  'iam_role_code',
  'iam_role_menu',
  // 字典
  'system_dict',
  'system_dict_item',
  // 地区
  'base_area',
  'base_city',
  'base_city_adjacent',
  'base_province',
  // 支付元数据定义
  'pay_md_provider',
  'pay_md_channel',
  'pay_md_method',
  'pay_md_capability',
  'pay_md_provider_method',
  'pay_md_product',
  'pay_md_product_capability',
  // 敏感词库
  'system_sensitive_word',
])

// ---------- 行级白名单：内置演示账号与商户（按主键 id 精确保留） ----------
// id 取自 dev 库现网值，固化于此保证产物确定性（可 diff、重跑不变）。
// 变更演示数据（重建账号/商户）后必须同步更新本清单，否则演示数据会整段丢失
// （脚本结尾对演示表做保留数校验，为 0 时非零退出）。
// 详见 _doc/design/内置演示账号与全新商户种子方案-2026-09-11.md
const DEMO_USER_BOOTX = '1' // 内置超管（administrator=true，跳过密码状态检查）
const DEMO_USER_CSADMIN = '2098300538905104384' // 运营端演示管理员
const DEMO_USER_CSQYSH = '2078739664805732352' // 商户端演示管理员（示例商户）

const DEMO_ROW_IDS = new Map([
  // 账号三件套：账号主表 + 扩展信息（登录后拉用户信息必需）+ 密码安全
  ['iam_user_info', new Set([DEMO_USER_BOOTX, DEMO_USER_CSADMIN, DEMO_USER_CSQYSH])],
  ['iam_user_expand_info', new Set([DEMO_USER_BOOTX, DEMO_USER_CSADMIN, DEMO_USER_CSQYSH])],
  // 密码安全行：非超管账号缺行会被判为初始密码而拦在改密页，故必须随包交付
  ['iam_user_password_security', new Set([DEMO_USER_CSADMIN, DEMO_USER_CSQYSH])],
  // 角色绑定：csadmin→role 1(admin_admin)、csqysh→role 2(merchant_admin)
  ['iam_user_role', new Set(['2098300598552301568', '2098238898364952576'])],
  // 商户本体
  ['mch_info', new Set(['2078739677237649408'])],
  // 商户 ↔ 账号绑定（登录后解析 mchNo 必需，见 MchContextLocalFilter）
  ['mch_user', new Set(['2078739677074071552'])],
  // 默认应用（default_app=true；同商户其余应用属配置，不保留）
  ['mch_app_info', new Set(['2078739677862600704'])],
  // 默认门店（default_store=true；同商户其余门店属配置，不保留）
  ['mch_store_info', new Set(['2078739677954875392'])],
])

// ---------- 演示账号密码重置：开发/演示默认密码 ----------
// 明文为 121212，与 _config/sql/reset-bootx-password.sql 急救脚本共用同一哈希，改动时两处同步。
// 用项目同款 hutool（cn.hutool.crypto.digest.BCrypt#hashpw，$2a$10$ 前缀）生成并 checkpw 自验通过，
// 登录侧 AbstractPasswordLoginHandler 用 BCrypt.checkpw 校验，标准算法跨实现互通。
// 固定哈希而非每次动态生成：BCrypt 带盐每次结果不同，固定值保证 data.sql 产物确定性（可 diff、重跑不变）。
// 安全语义：data.sql 面向干净安装包/演示数据，默认密码可接受，但生产部署后必须立即修改（README 有标注）。
const DEMO_PASSWORD_PLAIN = '121212'
const DEMO_PASSWORD_HASH = '$2a$10$HiIvaX7tbGWDeRVSciX/LuIAIYUgVJwasWtstsXsakpt0d9Sw.cKG'
// 需要重置密码的账号 id（长 id 在前，避免正则交替被短 id 抢先匹配）
const DEMO_PASSWORD_USER_IDS = [DEMO_USER_CSADMIN, DEMO_USER_CSQYSH, DEMO_USER_BOOTX]
// iam_user_info 无列名 INSERT，password 是第 5 个值（前 4 个值 id/名称/client_code/账号均为不含单引号的简单字面量，
// 且 BCrypt 哈希本身不含单引号，锚定前四列后替换第 5 列的引号内内容是安全的）
const DEMO_PASSWORD_RE = new RegExp(
  `^(INSERT INTO (?:public\\.)?iam_user_info\\s+VALUES\\s*\\(\\s*(?:${DEMO_PASSWORD_USER_IDS.join('|')}),\\s*'[^']*',\\s*'[^']*',\\s*'[^']*',\\s*')[^']*(')`,
)

// ---------- 解析规则 ----------
const INSERT_RE = /^INSERT INTO (?:public\.)?(\w+)\s+VALUES\b/i
// 取 INSERT 首个值（主键 id，bigint 无引号），用于行级白名单比对
const INSERT_FIRST_VALUE_RE = /^INSERT INTO (?:public\.)?\w+\s+VALUES\s*\(\s*(\d+)/
const STMT_END_RE = /\);\s*$/
// 需跳过的整行（pg_dump 18 客户端产物，见文件头"兼容处理"）
const SKIP_LINE_RES = [/^\\restrict/, /^\\unrestrict/, /^SET transaction_timeout\s*=/]

// ---------- 统计 ----------
const stats = new Map() // table -> { total, kept }
let passwordResetFailed = false // 密码列替换失败标记（失败时非零退出，防真实密码哈希泄露）
let passwordResetCount = 0 // 密码重置成功次数（正常等于 DEMO_PASSWORD_USER_IDS.length）
const demoRowMissTables = new Set() // 行级白名单表一条都没命中（有则非零退出）
function bump(table, kept) {
  const s = stats.get(table) ?? { total: 0, kept: 0 }
  s.total += 1
  if (kept) s.kept += 1
  stats.set(table, s)
}

// ---------- 主流程 ----------
const input = createReadStream(inputPath, { encoding: 'utf8' })
const output = createWriteStream(outputPath, { encoding: 'utf8' })

output.write(`-- ============================================================\n`)
output.write(`-- 敏感数据已清除(白名单模式) | 工具 redact-data.mjs | 时间 ${new Date().toISOString()}\n`)
output.write(`-- 策略：保留系统种子表 + 内置演示账号/商户(行级)，其余整表清除\n`)
output.write(`-- 内置账号：bootx(超管) / csadmin(运营端) / csqysh(商户端)\n`)
output.write(`-- 演示账号密码已重置为开发默认密码(${DEMO_PASSWORD_PLAIN})，生产部署后必须立即修改\n`)
output.write(`-- 演示商户：示例商户，以全新形态交付（仅商户本体+账号+默认应用+默认门店）\n`)
output.write(`-- 用途：干净安装包/演示数据\n`)
output.write(`-- ============================================================\n\n`)

const rl = createInterface({ input, crlfDelay: Infinity })
const redactedMarked = new Set() // 每张被清除的表仅写一次审计注释
let inInsert = false // 是否在累积跨行 INSERT
let buf = [] // 跨行 INSERT 累积缓冲
let bufTable = '' // 当前累积的表名

function writeRedactedMark(table) {
  if (redactedMarked.has(table)) return
  output.write(`-- REDACTED: ${table} (整表清除)\n`)
  redactedMarked.add(table)
}

/** 决策一条完整 INSERT（lines 为单行或跨行合并后的行数组） */
function handleInsert(table, lines) {
  // ① 系统种子表：整表保留
  if (KEEP_TABLES.has(table)) {
    bump(table, true)
    for (const ln of lines) output.write(ln + '\n')
    return
  }
  // ② 演示账号/商户关联表：按主键 id 行级保留
  const demoIds = DEMO_ROW_IDS.get(table)
  if (demoIds) {
    const m = lines[0].match(INSERT_FIRST_VALUE_RE)
    const id = m ? m[1] : null
    if (id !== null && demoIds.has(id)) {
      // iam_user_info：密码列重置为开发默认密码（其余表无密码列，原样保留）
      if (table === 'iam_user_info') {
        // 先单独判正则是否命中：不能比对替换前后字符串是否相等——
        // 开发库该账号密码若本就是这个固定哈希，替换后与原文一致会被误判为"结构不符"
        if (!DEMO_PASSWORD_RE.test(lines[0])) {
          // 结构与预期不符（列变化/格式漂移），替换未生效 → 中止退出，
          // 防止开发库当时的真实密码哈希随产物流出
          console.error('[错误] 演示账号密码列替换失败，iam_user_info 行结构与预期不符：')
          console.error(lines[0].slice(0, 160))
          passwordResetFailed = true
        } else {
          // 替换必须用函数形式：哈希含大量 '$'，字符串替换会将其解析为捕获组引用
          lines[0] = lines[0].replace(DEMO_PASSWORD_RE, (mt, p1, p2) => p1 + DEMO_PASSWORD_HASH + p2)
          passwordResetCount += 1
        }
      }
      bump(table, true)
      for (const ln of lines) output.write(ln + '\n')
      return
    }
    // 同表其他行：属商户已配置数据，清除
    bump(table, false)
    if (id === null) {
      // 首列结构异常，无法参与白名单比对 → 视为演示数据缺失，结尾非零退出
      demoRowMissTables.add(`${table}(结构异常)`)
    }
    return
  }
  // ③ 其余：整表清除
  bump(table, false)
  writeRedactedMark(table)
}

rl.on('line', (line) => {
  if (inInsert) {
    // 正在累积跨行 INSERT
    buf.push(line)
    if (STMT_END_RE.test(line)) {
      handleInsert(bufTable, buf)
      inInsert = false
      buf = []
      bufTable = ''
    }
    return
  }
  const m = line.match(INSERT_RE)
  if (m) {
    const table = m[1]
    if (STMT_END_RE.test(line)) {
      handleInsert(table, [line]) // 单行 INSERT
    } else {
      inInsert = true // 跨行 INSERT 起始
      bufTable = table
      buf = [line]
    }
    return
  }
  // 非 INSERT 行：跳过不兼容的元命令 / SET，其余（注释、空行等）原样输出
  if (SKIP_LINE_RES.some((re) => re.test(line))) return
  output.write(line + '\n')
})

rl.on('close', () => {
  // 兜底：文件结束时若仍在累积（理论上不会发生）
  if (inInsert && buf.length) handleInsert(bufTable, buf)
  output.end(() => {
    printReport()
    // 密码替换失败 / 演示行一条都没命中时以非零码退出，提示产物不可用
    if (passwordResetFailed || demoRowMissTables.size > 0) exit(1)
  })
})

function printReport() {
  const keptSeedTables = []
  const demoKept = [] // 行级保留表（演示账号/商户）
  const demoMissing = []
  const clearedTables = []
  for (const [t, s] of stats) {
    if (KEEP_TABLES.has(t)) keptSeedTables.push([t, s])
    else if (DEMO_ROW_IDS.has(t)) {
      if (s.kept > 0) demoKept.push([t, s])
      else demoMissing.push(t)
    } else clearedTables.push([t, s])
  }

  const sumKept = keptSeedTables.reduce((a, [, s]) => a + s.kept, 0)
  const sumCleared = clearedTables.reduce((a, [, s]) => a + s.total, 0)
  const sumDemoCleared = demoKept.reduce((a, [, s]) => a + (s.total - s.kept), 0)

  const lines = []
  lines.push('')
  lines.push('========== 脱敏统计（白名单模式） ==========')
  lines.push(`[保留·种子表] ${keptSeedTables.length} 张表，INSERT ${sumKept} 条（全保留）`)
  const demoDetail = demoKept.map(([t, s]) => `${t}(${s.total}→${s.kept})`).join(', ') || '无'
  lines.push(`[保留·演示行] ${demoKept.length} 张表：${demoDetail}`)
  lines.push(`[清除·演示表其余行] ${sumDemoCleared} 条（属商户已配置数据，按"全新商户"形态剔除）`)
  lines.push(
    `[重置·密码] 演示账号密码 → 开发默认密码 ${DEMO_PASSWORD_PLAIN}（${passwordResetCount}/${DEMO_PASSWORD_USER_IDS.length} 处）`,
  )
  lines.push(`[清除] ${clearedTables.length} 张表，丢弃 INSERT ${sumCleared} 条`)
  lines.push('---- 清除明细 ----')
  for (const [t, s] of clearedTables.sort((a, b) => a[0].localeCompare(b[0]))) {
    lines.push(`  ${t.padEnd(38)} ${s.total}`)
  }
  if (demoMissing.length > 0) {
    lines.push('---- ⚠️ 演示表未命中任何行 ----')
    for (const t of demoMissing) lines.push(`  ${t}`)
    lines.push('  演示数据不完整，产物不可用！请核对 DEMO_ROW_IDS 中的 id 是否与库中一致')
  }
  lines.push('=============================================')
  console.log(lines.join('\n'))
}
