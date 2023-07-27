# ToDoListWeb
![Java_17](https://img.shields.io/badge/java-v17-red?logo=java)
![Spring_Boot](https://img.shields.io/badge/Spring_Boot-v3.1.0-green.svg?logo=spring)
![SQLite](https://img.shields.io/badge/SQLite-blue?logo=SQLite)

SQLite를 사용하는 To-do List 웹 어플리케이션 서버(Web Application Server, WAS) 프로젝트

### Endpoints

| Method | Url | Decription |
| ------ | --- | ---------- |
| POST   |/todos     | '할 일 추가' 기능 |
| GET    |/todos     | '할 일 목록 조회' 기능 |
| PATCH  |/todos/{id}| '할 일 수정' 기능 |
| DELETE |/todos/{id}| '할 일 삭제' 기능 |
| POST   |/accounts     | '계정 추가' 기능 |
| GET    |/accounts     | '전체 계정 조회' 기능 |

### Database
#### '할 일' 테이블(todolist)
``` sql
CREATE TABLE todolist (
    id INTEGER PRIMARY KEY AUTOINCREMENT,                // ID (PK)
    title TEXT NOT NULL,                                 // 제목
    content TEXT NOT NULL,                               // 내용
    done BOOLEAN NOT NULL,                               // 완료 여부
    account_id INTEGER,                                  // 계정 ID (FK)
    FOREIGN KEY(account_id) REFERENCES accounts(pk)
);
```
#### 계정 테이블(accounts)
``` sql
CREATE TABLE accounts (
    pk INTEGER PRIMARY KEY AUTOINCREMENT,    // (PK)
    id TEXT UNIQUE,                          // ID
    password TEXT NOT NULL                   // 비밀번호
);
```

## 사용하는 기술 스택
- Java 17
- SpringBoot 3.x (Gradle - Groovy)
- SQLite
- Intellij Community
- AWS EC2

## 프로젝트 실행
- git clone 후 제공하는 우선 Gradle Build 합니다.
- toDoListWeb.sqlite 파일이 존재하는지 확인하고 존재하지 않는 경우 추가하고 프로젝트를 실행합니다.
