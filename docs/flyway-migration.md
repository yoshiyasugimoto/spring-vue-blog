# Flyway導入 - DataInitializer.javaからの移行

## 概要

データベースのスキーマ管理と初期データ投入を、Hibernateの自動DDLおよび `DataInitializer.java` から **Flyway** によるバージョン管理付きマイグレーションに移行した。

## 変更理由

| 移行前 | 問題点 |
|--------|--------|
| `ddl-auto: update` | スキーマ変更の履歴が残らず、本番環境で予期しない変更が起こりうる |
| `DataInitializer.java` | 毎回起動時に存在チェックが走り、SQLベースの管理と一貫性がない |

Flywayにより以下が実現される：
- SQLファイルによるスキーマ変更のバージョン管理
- 適用済みマイグレーションの履歴記録（`flyway_schema_history`テーブル）
- チーム開発でのDB変更の衝突防止

---

## 変更一覧

### 1. Flyway依存の追加

**ファイル:** `backend/build.gradle.kts`

```diff
+ implementation("org.flyway:flyway-core")
+ implementation("org.flyway:flyway-mysql")
```

Spring Boot 3.x + MySQL の組み合わせでは `flyway-core` と `flyway-mysql` の両方が必要。

---

### 2. スキーマ作成マイグレーション

**新規ファイル:** `backend/src/main/resources/db/migration/V1__create_schema.sql`

以下の5テーブルをCREATE TABLE文で定義：

| テーブル | 説明 |
|----------|------|
| `users` | ユーザー（管理者・エディター） |
| `categories` | 記事カテゴリ |
| `tags` | 記事タグ |
| `posts` | ブログ記事（users, categoriesへの外部キー） |
| `post_tags` | 記事とタグの中間テーブル（多対多） |

カラム定義はエンティティクラス（`User.java`, `Post.java`, `Category.java`, `Tag.java`）のJPAアノテーションに忠実に対応。

---

### 3. 初期データ投入マイグレーション

**新規ファイル:** `backend/src/main/resources/db/migration/V2__insert_default_admin.sql`

`DataInitializer.java` が行っていた管理者ユーザーの作成を、SQL INSERT文で置き換え。

| フィールド | 値 |
|------------|-----|
| username | `admin` |
| password | BCryptハッシュ済み（平文: `admin123`） |
| display_name | `Administrator` |
| role | `ADMIN` |

**注意:** パスワードはBCryptでハッシュ化した固定値をSQL内に記述している。`DataInitializer.java` では `PasswordEncoder.encode()` を毎回呼んでいたが、Flywayマイグレーションでは事前生成したハッシュ値を使用する。

---

### 4. application.yml の変更

**ファイル:** `backend/src/main/resources/application.yml`

```diff
  jpa:
    hibernate:
-     ddl-auto: update
+     ddl-auto: validate
```

```diff
+ flyway:
+   enabled: true
+   locations: classpath:db/migration
+   baseline-on-migrate: true
```

| 設定 | 説明 |
|------|------|
| `ddl-auto: validate` | Hibernateはスキーマの検証のみ行い、変更はしない |
| `flyway.enabled: true` | Flyway自動実行を有効化 |
| `flyway.locations` | マイグレーションファイルの配置場所 |
| `flyway.baseline-on-migrate: true` | 既存DBに対して初回実行時にベースラインを設定 |

---

### 5. DataInitializer.java の削除

**削除ファイル:** `backend/src/main/java/com/blog/config/DataInitializer.java`

初期データ投入の役割が `V2__insert_default_admin.sql` に移行されたため、不要となり削除。

---

## 今後のマイグレーション追加方法

新しいスキーマ変更が必要な場合は、以下の命名規則でSQLファイルを追加する：

```
db/migration/V{バージョン}__{説明}.sql
```

例：
- `V3__add_email_to_users.sql`
- `V4__create_comments_table.sql`

**重要:**
- 一度適用されたマイグレーションファイルは**絶対に変更しない**こと
- バージョン番号は常にインクリメントすること
- ファイル名のアンダースコアは2つ（`__`）

## 動作確認

1. MySQLが起動していることを確認
2. `./gradlew bootRun` でアプリケーションを起動
3. 起動ログに以下が表示されればマイグレーション成功：
   ```
   Successfully applied 2 migration(s) to schema `blog`
   ```
4. MySQLで `flyway_schema_history` テーブルを確認：
   ```sql
   SELECT version, description, success FROM flyway_schema_history;
   ```
