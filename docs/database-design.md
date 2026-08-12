# データベース設計書（MySQL）

> 本設計書は [要件定義書](./requirements.md) の内容を元にした初期案です。
> 未確定事項（勝利条件、投票公開ルール、コスト詳細など）に関わるテーブル・カラムは、
> 仕様確定後に変更される可能性があります。

## 1. 前提

- DBMS: MySQL
- 命名規則：テーブル名・カラム名はスネークケース（例: `game_players`）
- 主キーは基本的に `id`（AUTO_INCREMENT）

---

## 2. テーブル一覧

| テーブル名 | 概要 |
|---|---|
| users | プレイヤーアカウント |
| ai_characters | AIキャラクターのマスタ（難易度パラメータ・性格） |
| roles | 役職マスタ |
| games | ゲーム（1プレイ単位） |
| game_players | ゲームに参加するプレイヤー（人間 or AI） |
| cards | カードマスタ |
| game_player_hands | プレイヤーの手札状態 |
| card_usage_logs | カード使用履歴 |
| night_actions | 夜フェーズの行動（襲撃・護衛・占い） |
| votes | 投票履歴 |
| suspicion_points | 被疑心ポイントの履歴 |

---

## 3. テーブル定義

### 3.1 users（プレイヤーアカウント）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| username | VARCHAR(50) | UNIQUE, NOT NULL | |
| password_hash | VARCHAR(255) | NOT NULL | |
| email | VARCHAR(255) | UNIQUE | |
| created_at | DATETIME | NOT NULL DEFAULT CURRENT_TIMESTAMP | |

### 3.2 ai_characters（AIキャラクターマスタ）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| name | VARCHAR(50) | NOT NULL | キャラクター名 |
| personality | TEXT | | 口調・性格の設定文 |
| optimality | TINYINT | NOT NULL | 判断の最適性パラメータ（例: 0-100） |
| consistency | TINYINT | NOT NULL | 一貫性チェックの強さ |
| deception_skill | TINYINT | NOT NULL | 欺瞞の巧妙さ |

### 3.3 roles（役職マスタ）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| name | VARCHAR(20) | NOT NULL, UNIQUE | villager / werewolf / seer / hunter / madman など |
| faction | VARCHAR(20) | NOT NULL | village / werewolf / third（将来の第三陣営用） |
| description | TEXT | | |

> MVP時点では村人・人狼・占い師・狩人・狂人の5レコードを想定。第三陣営追加時はここに追加する。

### 3.4 games（ゲーム）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| status | VARCHAR(20) | NOT NULL | waiting / in_progress / finished |
| current_phase | VARCHAR(20) | | morning / day / voting / night |
| day_number | INT | NOT NULL DEFAULT 1 | 現在の日数 |
| winner_faction | VARCHAR(20) | NULL | ゲーム終了後に確定 |
| is_single_player | BOOLEAN | NOT NULL DEFAULT FALSE | 1人プレイかどうか |
| created_at | DATETIME | NOT NULL DEFAULT CURRENT_TIMESTAMP | |
| finished_at | DATETIME | NULL | |

### 3.5 game_players（ゲーム参加プレイヤー）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| game_id | INT | FK -> games.id, NOT NULL | |
| user_id | INT | FK -> users.id, NULL | AIの場合はNULL |
| ai_character_id | INT | FK -> ai_characters.id, NULL | 人間の場合はNULL |
| role_id | INT | FK -> roles.id, NOT NULL | |
| is_alive | BOOLEAN | NOT NULL DEFAULT TRUE | |
| seat_order | INT | NOT NULL | 席順・表示順 |

> `user_id` と `ai_character_id` はどちらか一方のみ値を持つ（CHECK制約またはアプリ側バリデーションで担保）。

### 3.6 cards（カードマスタ）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| name | VARCHAR(50) | NOT NULL | 例:「疑う」「占い」「偽占い」 |
| card_type | VARCHAR(20) | NOT NULL | role_card（役職専用）/ basic_card（基本） |
| required_role_id | INT | FK -> roles.id, NULL | role_cardの場合、使用可能な役職 |
| cost | TINYINT | NOT NULL | 共通コストプールでの消費量 |
| is_mandatory | BOOLEAN | NOT NULL DEFAULT FALSE | 強制使用カードか（例: 占い師の占いカード） |
| is_rare | BOOLEAN | NOT NULL DEFAULT FALSE | 1ゲーム1回限定などの希少カードか |
| value | TINYINT | NULL | 「疑い5」のような数値（ある場合） |
| is_usage_public | BOOLEAN | NOT NULL DEFAULT TRUE | カードの種類（何を使ったか）が公開されるか |
| description | TEXT | | |

### 3.7 game_player_hands（手札状態）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| game_player_id | INT | FK -> game_players.id, NOT NULL | |
| card_id | INT | FK -> cards.id, NOT NULL | |
| status | VARCHAR(20) | NOT NULL | in_hand / used / discarded / held（希少カードの保留状態） |
| acquired_at_day | INT | NOT NULL | 何日目に手札に加わったか |

### 3.8 card_usage_logs（カード使用履歴）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| game_id | INT | FK -> games.id, NOT NULL | |
| day_number | INT | NOT NULL | |
| phase | VARCHAR(20) | NOT NULL | day / night |
| game_player_id | INT | FK -> game_players.id, NOT NULL | 使用者 |
| card_id | INT | FK -> cards.id, NOT NULL | |
| target_game_player_id | INT | FK -> game_players.id, NULL | 対象がいる場合 |
| declared_result | VARCHAR(10) | NULL | 占い/偽占いの白黒申告など |
| used_at | DATETIME | NOT NULL DEFAULT CURRENT_TIMESTAMP | |

### 3.9 night_actions（夜フェーズの行動）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| game_id | INT | FK -> games.id, NOT NULL | |
| day_number | INT | NOT NULL | |
| actor_game_player_id | INT | FK -> game_players.id, NOT NULL | |
| action_type | VARCHAR(20) | NOT NULL | attack / guard / divine |
| target_game_player_id | INT | FK -> game_players.id, NOT NULL | |
| is_successful | BOOLEAN | NULL | 護衛によって襲撃が防がれたかなど、朝の結果判定後に確定 |

> 護衛は襲撃より優先処理する要件（3.1参照）をアプリ側のロジックで担保する。

### 3.10 votes（投票履歴）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| game_id | INT | FK -> games.id, NOT NULL | |
| day_number | INT | NOT NULL | |
| round | TINYINT | NOT NULL DEFAULT 1 | 同票再投票の場合は2以上 |
| voter_game_player_id | INT | FK -> game_players.id, NOT NULL | |
| target_game_player_id | INT | FK -> game_players.id, NOT NULL | |

### 3.11 suspicion_points（被疑心ポイント履歴）
| カラム名 | 型 | 制約 | 説明 |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | |
| game_id | INT | FK -> games.id, NOT NULL | |
| game_player_id | INT | FK -> game_players.id, NOT NULL | ポイントが加算される対象 |
| day_number | INT | NOT NULL | |
| points_delta | INT | NOT NULL | 加算/減算の量 |
| reason | VARCHAR(50) | NULL | 「疑うカードを受けた」など |
| created_at | DATETIME | NOT NULL DEFAULT CURRENT_TIMESTAMP | |

---

## 4. 今後の検討事項

- 投票の公開範囲ルール（得票数のみ公開/一部非公開）が確定した際、`votes` テーブルの公開範囲を制御するカラムの追加を検討する
- 被疑心ポイントの活用方針（カード使用制限 / 強制イベント）が確定した際、`suspicion_points` の集計ロジックと連動するテーブル・カラムを追加する
- 第三陣営追加時は `roles.faction` の値を拡張し、勝利条件判定ロジック側で対応する
- 村人専用カードの効果が確定次第、`cards` テーブルへ追加する
