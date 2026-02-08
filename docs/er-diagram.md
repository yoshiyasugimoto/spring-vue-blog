# ER図・クラス図

## ER図 (Entity Relationship Diagram)

```mermaid
erDiagram
    users {
        BIGINT id PK
        VARCHAR(50) username UK
        VARCHAR password
        VARCHAR(100) display_name
        VARCHAR(20) role "EDITOR | ADMIN"
        DATETIME created_at
        DATETIME updated_at
    }

    categories {
        BIGINT id PK
        VARCHAR(100) name UK
        VARCHAR(100) slug UK
        TEXT description
    }

    tags {
        BIGINT id PK
        VARCHAR(100) name UK
        VARCHAR(100) slug UK
    }

    posts {
        BIGINT id PK
        VARCHAR title
        VARCHAR slug UK
        LONGTEXT content
        TEXT excerpt
        VARCHAR(500) cover_image
        VARCHAR(20) status "DRAFT | PUBLISHED"
        BIGINT category_id FK
        BIGINT author_id FK
        DATETIME published_at
        DATETIME created_at
        DATETIME updated_at
    }

    post_tags {
        BIGINT post_id FK
        BIGINT tag_id FK
    }

    comments {
        BIGINT id PK
        BIGINT post_id FK
        VARCHAR(100) author_name
        VARCHAR(255) author_email
        TEXT content
        BOOLEAN approved
        DATETIME created_at
    }

    users ||--o{ posts : "author"
    categories ||--o{ posts : "belongs to"
    posts ||--o{ post_tags : ""
    tags ||--o{ post_tags : ""
    posts ||--o{ comments : ""
```

## クラス図 (Class Diagram)

```mermaid
classDiagram
    class Post {
        -Long id
        -String title
        -String slug
        -String content
        -String excerpt
        -String coverImage
        -PostStatus status
        -Category category
        -User author
        -Set~Tag~ tags
        -LocalDateTime publishedAt
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class User {
        -Long id
        -String username
        -String password
        -String displayName
        -UserRole role
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class Category {
        -Long id
        -String name
        -String slug
        -String description
    }

    class Tag {
        -Long id
        -String name
        -String slug
    }

    class PostStatus {
        <<enumeration>>
        DRAFT
        PUBLISHED
    }

    class UserRole {
        <<enumeration>>
        EDITOR
        ADMIN
    }

    class PostService {
        +findPublished(Pageable) Page~Post~
        +findPublishedBySlug(String) Post
        +findAll(Pageable) Page~Post~
        +findById(Long) Post
        +create(PostRequest, String) Post
        +update(Long, PostRequest) Post
        +delete(Long) void
    }

    class CategoryService {
        +findAll() List~Category~
        +findById(Long) Category
        +create(CategoryRequest) Category
        +update(Long, CategoryRequest) Category
        +delete(Long) void
    }

    class TagService {
        +findAll() List~Tag~
        +create(TagRequest) Tag
        +delete(Long) void
    }

    class ResourceNotFoundException {
        +ResourceNotFoundException(String, Object)
    }

    class GlobalExceptionHandler {
        +handleNotFound(ResourceNotFoundException) ResponseEntity
        +handleValidation(MethodArgumentNotValidException) ResponseEntity
        +handleBadRequest(IllegalArgumentException) ResponseEntity
        +handleGeneral(Exception) ResponseEntity
    }

    class SlugUtil {
        +generate(String, String, Predicate) String$
    }

    Post --> PostStatus
    Post --> Category
    Post --> User
    Post --> Tag
    User --> UserRole
    PostService --> Post
    CategoryService --> Category
    TagService --> Tag
    PostService ..> SlugUtil
    CategoryService ..> SlugUtil
    TagService ..> SlugUtil
    PostService ..> ResourceNotFoundException
    CategoryService ..> ResourceNotFoundException
```

## テーブル関係の説明

| 関係 | 説明 |
|---|---|
| `users` → `posts` | 1対多: 1ユーザーが複数記事を作成 |
| `categories` → `posts` | 1対多: 1カテゴリに複数記事 (nullable) |
| `posts` ↔ `tags` | 多対多: 中間テーブル `post_tags` |
| `posts` → `comments` | 1対多: 1記事に複数コメント (将来実装) |
