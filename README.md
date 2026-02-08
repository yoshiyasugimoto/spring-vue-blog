# Spring Boot × Vueでブログ機能を作るサンプル

## frontend

### 特徴

- CDD(https://www.chromatic.com/blog/component-driven-development/)を意識してStorybookを使うこと
- Ant Desigh Vueのコンポーネント利用とデザインをTailiwind CSSに寄せること
- Storybookを使ってVRT(ビジュアルリグレッションテスト)をすること

## backend

### テスト実行

Docker Compose経由でbackendのユニットテストを実行できます（ローカルにJava 21が不要）。

```bash
# 全テスト実行
docker compose run --rm backend-test

# 特定テストクラスのみ実行
docker compose run --rm backend-test gradle test --tests "com.blog.service.PostServiceTest" --no-daemon
```
