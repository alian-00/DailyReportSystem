# DailyReportSystem

DailyReportSystem は、日報を作成・管理するための Spring Boot 製 Web アプリケーションです。ユーザー登録、ログイン、日報の作成・編集・削除、日報詳細へのコメント投稿に対応しています。管理者は管理画面からユーザー管理を行えます。

## 主な機能

- ユーザー登録とログイン
- 日報の一覧表示、詳細表示、作成、編集、削除
- 日報詳細画面でのコメント投稿
- 管理者専用のユーザー一覧、作成、削除
- Thymeleaf によるサーバーサイドレンダリング

## 使用技術

- Java 21
- Spring Boot 3.4
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Maven Wrapper

## ディレクトリ構成

```text
src/main/java/com/example/
  controller/       MVC コントローラー
  controller/admin/ 管理者用コントローラー
  entity/           JPA エンティティ
  repository/       Spring Data リポジトリ
  security/         セキュリティ設定
src/main/resources/
  templates/        Thymeleaf テンプレート
  static/css/       スタイルシート
  application.properties
  data.sql          初期データ
src/test/java/      テスト
```

## 動作環境

- Java 21
- MySQL 8 互換のデータベース

データベース接続設定は `src/main/resources/application.properties` にあります。

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/daily_report_system
spring.datasource.username=devuser
spring.datasource.password=password
```

起動前に `daily_report_system` データベースと接続ユーザーを作成するか、ローカル環境に合わせて設定を変更してください。

## ローカルでの起動

```bash
./mvnw spring-boot:run
```

Windows の場合:

```bat
mvnw.cmd spring-boot:run
```

起動後、ブラウザで以下にアクセスします。

```text
http://localhost:8080
```

## テスト

テストは以下のコマンドで実行します。

```bash
./mvnw test
```

現在は Spring Boot のコンテキスト起動テストが含まれています。

## 初期データ

`data.sql` で一般ユーザーと管理者ユーザーを 1 件ずつ登録します。パスワードは BCrypt ハッシュとして保存されています。`spring.jpa.hibernate.ddl-auto` を変更する場合は、テーブル作成と初期データ投入の挙動に注意してください。

## 開発メモ

- 静的ファイルは `src/main/resources/static` に配置します。
- Thymeleaf テンプレートは `src/main/resources/templates` 配下で機能ごとに管理します。
- `/admin/**` は `ROLE_ADMIN` のみアクセスできます。
- 日報の編集・削除は投稿者本人のみ実行できます。
