# Backend - Spring Boot Blog API

## 技術スタック

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Security
- Flyway (DBマイグレーション)
- MySQL 8.0

## 環境起動

### DB のみ起動

```bash
docker compose up -d db
```

DB のヘルスチェックが通るまで待つ:

```bash
docker compose ps
# STATUS が "healthy" になっていることを確認
```

### backend 起動（マイグレーション自動実行）

```bash
docker compose up -d backend
```

### 全サービス起動

```bash
docker compose up -d
```

### 再ビルドして起動

```bash
docker compose up -d --build backend
```

### 全停止・データ削除

```bash
docker compose down -v
```

## Flyway マイグレーション

### 仕組み

- Spring Boot 起動時に Flyway が自動でマイグレーションを実行する
- SQLファイルは `src/main/resources/db/migration/` に配置
- `application.yml` の `ddl-auto: validate` により、Hibernate はスキーマ検証のみ行う（テーブル作成・変更はしない）

### マイグレーションファイルの命名規則

```
V{番号}__{説明}.sql
```

- `V` の後に連番（V1, V2, V3...）
- アンダースコアを **2つ** 続ける
- 説明はスネークケース

例:

```
V1__create_schema.sql
V2__add_comments_table.sql
V3__add_index_to_posts.sql
```

### 新しいマイグレーション追加手順

1. `src/main/resources/db/migration/` に次の番号のSQLファイルを作成
2. backend を再起動（または再ビルド）

```bash
docker compose up -d --build backend
```

3. 適用結果を確認

```bash
docker compose logs backend | grep -i flyway
```

### マイグレーション確認コマンド

```bash
# MySQL に接続
docker compose exec db mysql -ublog -pblogpassword blog

# テーブル一覧
SHOW TABLES;

# Flyway 適用履歴
SELECT installed_rank, version, description, success FROM flyway_schema_history;

# テーブル定義確認
DESCRIBE comments;
```

### 注意事項

- **適用済みのSQLファイルは絶対に編集しない**（チェックサムエラーになる）
- 既存テーブルへの変更は新しい V ファイルで `ALTER TABLE` する
- やり直したい場合は `docker compose down -v` でDB初期化してから再起動

## DB 接続情報

| 項目       | 値                                       |
| ---------- | ---------------------------------------- |
| ホスト     | localhost (ホストから) / db (コンテナ間) |
| ポート     | 3306                                     |
| DB名       | blog                                     |
| ユーザー   | blog                                     |
| パスワード | blogpassword                             |
