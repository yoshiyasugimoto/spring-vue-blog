# バックエンド改善ドキュメント

## 概要

Spring Boot バックエンドに対して以下の改善を実施しました。

---

## 1. 機密情報の .env 移行

### 問題点

データベースパスワードが `application.yml` と `compose.yml` にハードコードされていました。

### 変更内容

**新規作成: `.env`**
```
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_PASSWORD=blogpassword
SPRING_DATASOURCE_PASSWORD=blogpassword
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

**新規作成: `.env.example`** (テンプレート)

**新規作成: `.gitignore`** (プロジェクトルート)
```
.env
```

**`compose.yml` の変更:**
```yaml
# 変更前
environment:
  MYSQL_ROOT_PASSWORD: rootpassword
  MYSQL_PASSWORD: blogpassword

# 変更後
env_file: .env
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
  MYSQL_PASSWORD: ${MYSQL_PASSWORD}
```

**`application.yml` の変更:**
```yaml
# 変更前
password: blogpassword

# 変更後 (環境変数 or デフォルト値)
password: ${SPRING_DATASOURCE_PASSWORD:blogpassword}
```

---

## 2. グローバル例外ハンドラ導入

### 問題点

各サービスが `RuntimeException` をスローしており、クライアントに適切な HTTP ステータスコードが返されていませんでした。

### 変更内容

**新規作成: `exception/ResourceNotFoundException.java`**
```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " not found: " + id);
    }
}
```

**新規作成: `exception/GlobalExceptionHandler.java`**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)  // → 404
    @ExceptionHandler(MethodArgumentNotValidException.class)  // → 400 + フィールドエラー
    @ExceptionHandler(IllegalArgumentException.class)  // → 400
    @ExceptionHandler(Exception.class)  // → 500
}
```

**レスポンス形式の統一:**
```json
{
    "status": 404,
    "message": "Post not found: 123",
    "timestamp": "2025-01-15T10:30:00"
}
```

**サービスの変更 (PostService, CategoryService):**
```java
// 変更前
throw new RuntimeException("Post not found: " + id);

// 変更後
throw new ResourceNotFoundException("Post", id);
```

---

## 3. Post.status / User.role の Enum 化

### 問題点

`Post.status` と `User.role` が文字列 (`String`) で管理されており、タイプセーフでない比較が行われていました。

```java
// 変更前: 文字列比較（タイポのリスク）
if ("PUBLISHED".equals(request.status())) { ... }
```

### 変更内容

**新規作成: `entity/PostStatus.java`**
```java
public enum PostStatus {
    DRAFT,
    PUBLISHED
}
```

**新規作成: `entity/UserRole.java`**
```java
public enum UserRole {
    EDITOR,
    ADMIN
}
```

**エンティティの変更:**
```java
// Post.java
@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 20)
private PostStatus status = PostStatus.DRAFT;

// User.java
@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 20)
private UserRole role = UserRole.EDITOR;
```

**DTO の変更:**
```java
// PostRequest.java - status フィールドを PostStatus 型に
public record PostRequest(
    @NotBlank String title,
    // ...
    PostStatus status,  // String → PostStatus
    // ...
) {
    public PostRequest {
        if (status == null) status = PostStatus.DRAFT;
    }
}
```

**サービスの比較:**
```java
// 変更前
if ("PUBLISHED".equals(request.status()))

// 変更後 (タイプセーフ)
if (request.status() == PostStatus.PUBLISHED)
```

**DB互換性:** `@Enumerated(EnumType.STRING)` により、DB には引き続き `"DRAFT"` / `"PUBLISHED"` 文字列が格納されます。マイグレーション不要です。

---

## 4. Slug 生成ロジックの共通化

### 問題点

同一のスラッグ生成ロジックが PostService, CategoryService, TagService に重複していました。

### 変更内容

**新規作成: `util/SlugUtil.java`**
```java
public final class SlugUtil {
    public static String generate(String source, String providedSlug, Predicate<String> existsCheck) {
        String slug = providedSlug;
        if (slug == null || slug.isBlank()) {
            slug = source.toLowerCase()
                    .replaceAll("[^a-z0-9\\u3040-\\u9faf]+", "-")
                    .replaceAll("^-|-$", "");
        }
        if (existsCheck.test(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }
        return slug;
    }
}
```

**使用箇所:**
```java
// PostService
post.setSlug(SlugUtil.generate(request.title(), request.slug(), postRepository::existsBySlug));

// CategoryService
category.setSlug(SlugUtil.generate(request.name(), request.slug(), categoryRepository::existsBySlug));

// TagService
tag.setSlug(SlugUtil.generate(request.name(), request.slug(), s -> tagRepository.findBySlug(s).isPresent()));
```

**ポイント:** `Predicate<String>` を引数にとることで、各リポジトリの重複チェック方法の差異を吸収しています。

---

## 5. JPA 設定の改善

### 問題点

- `show-sql: true` が本番環境でもSQLを出力するリスク
- `JOIN FETCH` + `Pageable` で Hibernate 警告 (HHH000104) が発生

### 変更内容

**`application.yml`:**
```yaml
# 変更前
show-sql: true

# 変更後
show-sql: false
```

**`PostRepository.java` — countQuery の分離:**
```java
// 変更前 (countQuery なし → Hibernate が JOIN FETCH を含むクエリで COUNT しようとする)
@Query("SELECT p FROM Post p LEFT JOIN FETCH p.category ... WHERE p.status = 'PUBLISHED' ORDER BY ...")
Page<Post> findPublished(Pageable pageable);

// 変更後 (countQuery を分離して警告を解消)
@Query(value = "SELECT p FROM Post p LEFT JOIN FETCH p.category ... WHERE p.status = 'PUBLISHED' ORDER BY ...",
       countQuery = "SELECT COUNT(p) FROM Post p WHERE p.status = 'PUBLISHED'")
Page<Post> findPublished(Pageable pageable);
```

---

## 6. ヘルスチェック API 導入

### 変更内容

**`build.gradle.kts` — Actuator 依存追加:**
```kotlin
implementation("org.springframework.boot:spring-boot-starter-actuator")
```

**`application.yml` — Actuator 設定:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health
  endpoint:
    health:
      show-details: never
```

**`SecurityConfig.java` — 認証なしでアクセス許可:**
```java
.requestMatchers("/actuator/health").permitAll()
```

**エンドポイント:** `GET /actuator/health` → `{"status": "UP"}`

---

## 7. ログ設定

### 変更内容

**新規作成: `logback-spring.xml`**

| プロファイル | com.blog | Hibernate SQL | root |
|---|---|---|---|
| default (本番) | INFO | WARN | WARN |
| dev | DEBUG | DEBUG | INFO |

**設計ポイント:**
- 本番では SQL ログを出力しない (セキュリティ)
- `dev` プロファイルでのみ詳細ログを有効化
- Spring Security は WARN レベル (ノイズ低減)

---

## 8. セキュリティヘッダー追加

### 変更内容

**`SecurityConfig.java`:**
```java
.headers(headers -> headers
    .frameOptions(frame -> frame.deny())           // X-Frame-Options: DENY
    .contentTypeOptions(content -> {})              // X-Content-Type-Options: nosniff
    .httpStrictTransportSecurity(hsts -> hsts       // Strict-Transport-Security
        .includeSubDomains(true)
        .maxAgeInSeconds(31536000)
    )
)
```

| ヘッダー | 値 | 効果 |
|---|---|---|
| X-Frame-Options | DENY | クリックジャッキング防止 |
| X-Content-Type-Options | nosniff | MIME スニッフィング防止 |
| Strict-Transport-Security | max-age=31536000 | HTTPS 強制 (1年) |

**CORS の改善:**
```java
// 変更前
configuration.setAllowedOrigins(List.of("http://localhost:5173"));
configuration.setAllowedHeaders(List.of("*"));  // ワイルドカード

// 変更後
@Value("${cors.allowed-origins:http://localhost:5173}")
private String corsAllowedOrigins;

configuration.setAllowedOrigins(List.of(corsAllowedOrigins.split(",")));
configuration.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));  // 必要最小限
```

**ログアウト時のセッション無効化:**
```java
.logout(logout -> logout
    .logoutUrl("/api/auth/logout")
    .invalidateHttpSession(true)       // 追加: セッション無効化
    .deleteCookies("JSESSIONID")       // 追加: Cookie削除
    // ...
)
```

---

## 9. POST /api/admin/posts/{id} エンドポイント追加

### 変更内容

**`PostController.java`:**
```java
@GetMapping("/api/admin/posts/{id}")
public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
    Post post = postService.findById(id);
    return ResponseEntity.ok(PostResponse.from(post));
}
```

フロントエンドの `PostEditView.vue` が全件取得 (`getAll(0, 1000)`) の代わりに ID 指定で1件取得できるようになりました。

---

## 10. テストコード追加

### 追加ファイル

| ファイル | テスト内容 |
|---|---|
| `SlugUtilTest.java` | Slug 生成: タイトルから生成、日本語対応、カスタムスラッグ、重複時の timestamp 付与 |
| `PostServiceTest.java` | Service 層: findById, 存在しない場合の例外、PUBLISHED 時の publishedAt 設定、DRAFT 時の null |
| `PostControllerTest.java` | Controller 層: GET /api/admin/posts/{id} のレスポンス検証、未認証時の 401 検証 |

---

## 確認済み (対応不要) のタスク

| タスク | 状況 |
|---|---|
| **DTO record 化** | 全 DTO が既に record で実装済み |
| **Flyway 導入** | 設定済み、V1〜V3 マイグレーション実装済み |
| **ddl-auto** | `validate` に設定済み (安全) |
| **オープンリダイレクト** | リダイレクト処理なし (リスクなし) |
