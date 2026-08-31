#!/usr/bin/env node
/**
 * Data SQL 敏感数据清除脚本（白名单架构）
 *
 * 用途：生成干净安装包/演示数据，供他人导入。
 * 策略：保留 19 张系统种子表 + bootx 内置超管(id=1)，其余整表清除；
 *       bootx 密码重置为开发默认密码(见 BOOTX_DEV_PASSWORD_HASH)。
 *
 * 三原则决策：
 *   ① 表 ∈ KEEP_TABLES（系统种子）              → 整表原样保留
 *   ② 内置超管关联表且 id=1 (bootx)              → 该行保留，同表其余行清除；
 *      其中 iam_user_info 的密码列替换为开发默认密码哈希
 *      （开发库密码常被改动，原样保留会导致每次导出的 data.sql 登录密码漂移）
 *      （iam_user_info 账号主表 + iam_user_expand_info 扩展信息，
 *       后者登录后拉用户信息必需，缺则抛 UserInfoNotExistsException）
 *   ③ 其余                                   → 整表跳过（写审计注释）
 *
 * 格式假设：pg_dump --inserts（INSERT INTO public.表名 VALUES (...)，无列名）。
 *           自动处理跨多行 INSERT（值内含换行，累积到行尾 ");" 视为语句结束）。
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

// ---------- 行级保留：内置超管 bootx(id=1) 的关联表 ----------
// iam_user_info: 账号主表
// iam_user_expand_info: 用户扩展信息（登录后拉用户信息必需，缺则 getLoginAfterUserInfo 抛 UserInfoNotExistsException）
const BOOTX_ADMIN_TABLES = new Set(['iam_user_info', 'iam_user_expand_info'])
const BOOTX_ADMIN_RE = /^INSERT INTO (?:public\.)?(?:iam_user_info|iam_user_expand_info)\s+VALUES\s*\(\s*1\s*,/

// ---------- bootx 密码重置：开发/演示默认密码 ----------
// 明文为 121212，与 _config/sql/reset-bootx-password.sql 急救脚本共用同一哈希，改动时两处同步。
// 用项目同款 hutool（cn.hutool.crypto.digest.BCrypt#hashpw，$2a$10$ 前缀）生成并 checkpw 自验通过，
// 登录侧 AbstractPasswordLoginHandler 用 BCrypt.checkpw 校验，标准算法跨实现互通。
// 固定哈希而非每次动态生成：BCrypt 带盐每次结果不同，固定值保证 data.sql 产物确定性（可 diff、重跑不变）。
// 安全语义：data.sql 面向干净安装包/演示数据，默认密码可接受，但生产部署后必须立即修改（README 有标注）。
const BOOTX_DEV_PASSWORD_PLAIN = '121212'
const BOOTX_DEV_PASSWORD_HASH = '$2a$10$HiIvaX7tbGWDeRVSciX/LuIAIYUgVJwasWtstsXsakpt0d9Sw.cKG'
// iam_user_info 无列名 INSERT，password 是第 5 个值（前 4 个值 id/名称/client_code/账号均为不含单引号的简单字面量，
// 且 BCrypt 哈希本身不含单引号，锚定前四列后替换第 5 列的引号内内容是安全的）
const BOOTX_PASSWORD_RE = /^(INSERT INTO (?:public\.)?iam_user_info\s+VALUES\s*\(\s*1,\s*'[^']*',\s*'[^']*',\s*'[^']*',\s*')[^']*(')/

// ---------- 解析规则 ----------
const INSERT_RE = /^INSERT INTO (?:public\.)?(\w+)\s+VALUES\b/i
const STMT_END_RE = /\);\s*$/

// ---------- 统计 ----------
const stats = new Map() // table -> { total, kept }
let passwordResetFailed = false // bootx 密码列替换失败标记（失败时非零退出，防真实密码哈希泄露）
let passwordResetCount = 0      // bootx 密码重置成功次数（正常恒为 1）
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
output.write(`-- 策略：保留 19 张系统种子表 + bootx 超管(id=1)，其余整表清除\n`)
output.write(`-- bootx 超管密码已重置为开发默认密码(121212)，生产部署后必须立即修改\n`)
output.write(`-- 用途：干净安装包/演示数据\n`)
output.write(`-- ============================================================\n\n`)

const rl = createInterface({ input, crlfDelay: Infinity })
const redactedMarked = new Set() // 每张被清除的表仅写一次审计注释
let inInsert = false // 是否在累积跨行 INSERT
let buf = []         // 跨行 INSERT 累积缓冲
let bufTable = ''    // 当前累积的表名

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
  // ② 内置超管关联表行级保留：bootx (id=1)
  if (BOOTX_ADMIN_TABLES.has(table) && BOOTX_ADMIN_RE.test(lines[0])) {
    // iam_user_info：密码列重置为开发默认密码（iam_user_expand_info 无密码列，原样保留）
    if (table === 'iam_user_info') {
      // 替换必须用函数形式：哈希含大量 '$'，字符串替换会将其解析为捕获组引用
      const replaced = lines[0].replace(BOOTX_PASSWORD_RE, (m, p1, p2) => p1 + BOOTX_DEV_PASSWORD_HASH + p2)
      if (replaced === lines[0]) {
        // 结构与预期不符（列变化/格式漂移），替换未生效 → 中止退出，
        // 防止开发库当时的真实密码哈希随产物流出
        console.error('[错误] bootx 密码列替换失败，iam_user_info id=1 行结构与预期不符：')
        console.error(lines[0].slice(0, 160))
        passwordResetFailed = true
      } else {
        lines[0] = replaced
        passwordResetCount += 1
      }
    }
    bump(table, true)
    for (const ln of lines) output.write(ln + '\n')
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
  // 非 INSERT 行（注释、空行等）原样输出
  output.write(line + '\n')
})

rl.on('close', () => {
  // 兜底：文件结束时若仍在累积（理论上不会发生）
  if (inInsert && buf.length) handleInsert(bufTable, buf)
  output.end(() => {
    printReport()
    // 密码替换失败时以非零码退出，提示产物不可用（防真实密码哈希随产物流出）
    if (passwordResetFailed) exit(1)
  })
})

function printReport() {
  const keptSeedTables = []
  const partialTables = [] // 行级保留表（内置超管关联表）
  const clearedTables = []
  for (const [t, s] of stats) {
    if (KEEP_TABLES.has(t)) keptSeedTables.push([t, s])
    else if (BOOTX_ADMIN_TABLES.has(t)) partialTables.push([t, s])
    else clearedTables.push([t, s])
  }

  const sumKept = keptSeedTables.reduce((a, [, s]) => a + s.kept, 0)
  const sumCleared = clearedTables.reduce((a, [, s]) => a + s.total, 0)
  const sumPartialCleared = partialTables.reduce((a, [, s]) => a + (s.total - s.kept), 0)

  const lines = []
  lines.push('')
  lines.push('========== 脱敏统计（白名单模式） ==========')
  lines.push(`[保留·种子表] ${keptSeedTables.length} 张表，INSERT ${sumKept} 条（全保留）`)
  const partialDetail = partialTables.map(([t, s]) => `${t}(${s.total}→${s.kept})`).join(', ') || '无'
  lines.push(`[保留·行级] 内置超管(id=1)关联表：${partialDetail}，清除 ${sumPartialCleared}`)
  lines.push(`[重置·密码] bootx 密码 → 开发默认密码 ${BOOTX_DEV_PASSWORD_PLAIN}（${passwordResetCount} 处）`)
  lines.push(`[清除] ${clearedTables.length} 张表，丢弃 INSERT ${sumCleared} 条`)
  lines.push('---- 清除明细 ----')
  for (const [t, s] of clearedTables.sort((a, b) => a[0].localeCompare(b[0]))) {
    lines.push(`  ${t.padEnd(38)} ${s.total}`)
  }
  lines.push('=============================================')
  console.log(lines.join('\n'))
}
