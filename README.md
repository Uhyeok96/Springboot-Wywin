# [Spring Boot Project - Wywin]

[Spring Boot 프로젝트 3조]
<br><br><br>
## 프로젝트 소개
경매 및 해외 구매 대행 사이트를 구현하도록 하였습니다.
> 개발 취지 : 소비자가 원하는 상품이 국내 미 출시, 단종으로 구매하지 못하는 경우, 관세/할인 등으로 가격 차이가 많이 나는 경우 해외 거주자가 대신 구매하여 소비자의 구매 욕구를 충족시키고자 개발하게 됨.
> 개발 목표 : 구매 대행으로 일반적인 쇼핑몰의 B2C 판매 시스템의 부족함을 보완하여 소비자의 구매 만족도를 더 충족시키고 경매 시스템 도입으로 중고 물품을 보다 합리적인 가격으로 거래를 유도함.
<br>

## WYWIN 발표자료 다운로드

## 개발 기간
2024.10.28 ~ 2024.11.28 (약 4주)
<br><br>

## 멤버 구성
문지현(조장) - 전반적인 사이트 틀 작업, 결제 시스템
<br><br>
김우혁(조원) - 경매 관련 시스템 개발, 로고 제작 및 메인 페이지 작업
<br><br>
함시은(조원) - 회원 및 관리자 시스템
<br><br>
양승환(조원) - 구매 대행 및 판매 시스템
<br><br>
## 개발 환경
- **BE** : Java, Gradle, JPA, Querydsl
- **FE** : JavaScript, jQuery, Thymeleaf
- **IDE** : IntelliJ
- **Framework** : Spring Boot V3, Spring Security V6
- **DB** : Maria DB
- **VCS** : github
<br><br>

## 주요 기능
**회원**
- 로그인(Session에 저장된 data 이용하여 header 기능 구분)

- 회원가입(아이디 중복확인, 필수 사항 기입 체크)

- 마이페이지 및 활동내역
  > 회원정보 변경 기능 및 작성글, 후기, 신고 정보 확인

- 중고 거래 게시판 리스트
  > 인기 게시물(좋아요 수)과 최근 게시물(등록일) 중 선택하여 리스트 출력 가능
  > 판매중, 예약중, 판매완료 순으로 출력

- 게시물 등록 - 이미지 미리보기 및 파일 업로드 기능
  
- 게시물 상세보기, 수정, 삭제
  > 로그인 한 회원 판별하여 작성자일 경우 수정, 삭제 버튼 / 다른 회원일 경우 대화연결, 신고하기 버튼 활성화

- 좋아요, 관심(장바구니) 리스트

- 게시물 검색 기능 및 인기 검색어 순위(1~10위) 표시
  > 카테고리와 제목 각각 따로 검색 가능

- 판매자와 구매자간의 1:1 대화기능
  > 거래도 채팅방에서 버튼으로 이루어지며 마지막 거래완료 버튼 클릭 시
  > 설문 버튼 활성화 (설문 작성 시 작성한 회원의 포인트 적립)

- 판매자에 대한 후기(댓글) 기능과 답글(대댓글) CRUD 기능

- 판매자의 판매물품 리스트와 매너 칭찬(설문) 기능
  > 거래완료 시 설문을 통하여 기록을 남기고 판매자의 매너온도 상승 및 하락 기능

- 감자페이와 포인트 (충전, 환불, 포인트 전환)
  > 충전과 환불은 랜덤인증번호 생성하여 인증받아야 함, 포인트는 페이로 전환하여 사용가능

- QNA 게시판 - 하단 footer에 QNA게시판을 만들어 자주 묻는 질문 리스트 확인 가능

- 고객의 편지(개선사항) - 하단 footer에 개선사항 전달하는 Message 기능
  > 관리자가 고객의 편지 리스트에서 확인

- 오류 통합 관리(예외 발생 시 통합 오류 처리 기능)

- 그 외 성능 향상을 위한 부가기능
  > ULID, INDEX, 테이블 분리, Alias


**관리자**
- 관리자(운영자)의 회원 검색

- 회원 관리(회원가입일 및 최근 로그인 시간 정보)
  
- 회원 등급 수정(일반 회원, 블랙리스트 회원)
  > 블랙리스트 회원은 로그인 불가

- 신고내역 확인 / 신고회원 등급 수정

- 관리자 게시판 (공지 등록, 수정, 삭제 등)

<br><br>
## WYWIN 웹사이트 [클릭!](http://mbc-webcloud.iptime.org:8003/)시 홈페이지로 이동합니다.

[감자마켓 홈페이지 이동](http://mbc-webcloud.iptime.org:6302/home) <http://mbc-webcloud.iptime.org:6302/home>

**TEST 계정 로그인**

ID : test1

PW : q1






