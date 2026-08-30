#!/usr/bin/env node
/**
 * Data SQL 敏感数据清除脚本（白名单架构）
 *
 * 用途：生成干净安装包/演示数据，供他人导入。
 * 策略：保留 19 张系统种子表 + bootx 内置超管(id=1)，其余整表清除。
 *
 * 三原则决策：
 *   ① 表 ∈ KEEP_TABLES（系统种子）              → 整表原样保留
 *   ② 内置超管关联表且 id=1 (bootx)              → 该行保留，同表其余行清除
 *      （iam_user_info 账号主表 + iam_user_expand_info 扩展信息，
 *       后者登录后拉用户信息必需，缺则抛 UserInfoNotExistsException）
 *   ③ 其余                                   → 整表跳过（写审计注释）
 *
 * 格式假设：pg_dump --inserts（INSERT INTO public.表名 VALUES (...)，无列名）。
 *           自动处理跨多行 INSERT（值内含换行，累积到行尾 ");" 视为语句结束）。
 *
 * 用法：node redact-data.mjs <input.sql> <output.sql>
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

// ---------- 解析规则 ----------
const INSERT_RE = /^INSERT INTO (?:public\.)?(\w+)\s+VALUES\b/i
const STMT_END_RE = /\);\s*$/

// ---------- 统计 ----------
const stats = new Map() // table -> { total, kept }
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
  output.end(() => printReport())
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
  lines.push(`[清除] ${clearedTables.length} 张表，丢弃 INSERT ${sumCleared} 条`)
  lines.push('---- 清除明细 ----')
  for (const [t, s] of clearedTables.sort((a, b) => a[0].localeCompare(b[0]))) {
    lines.push(`  ${t.padEnd(38)} ${s.total}`)
  }
  lines.push('=============================================')
  console.log(lines.join('\n'))
}
