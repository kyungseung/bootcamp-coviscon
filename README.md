# bootcamp-coviscon
## ⭐ Coviscon
* ### 프로젝트 개요
  
  * PLAYDATA의 MSA 백엔드 개발자 양성 과정 부트 캠프를 수강하면서 같은 분야에 대해 공부하는 다양한 사람들을 마주하고, 함께 학습한 내용을 공부하면서 매일 조금씩 성장하는 모습을 발견하게 됨.
  * 시작점은 다르지만 같은 방향을 향해서 성장하는 개발자를 양성할 수 있는 교육 사이트를 개발하고자 함.

* ### 주요 기능
  * Discovery Service
    > a. 외부에서 다른 서비스들이 마이크로 서비스를 검색하기 위해 사용되는 개념으로, key / value 형태로 서비스 이름과 서버 IP를 저장한다.
  * Api Gateway Service
    > a. 사용자가 설정한 라우팅 설정에 따라 각각의 엔드포인트로 클라이언트를 대신해서 요청하고, 응답을 받으면 다시 클라이언트에게 전달해주는 프록시 역할을 한다.
    >
    > ➡️ 시스템의 내부 구조를 숨기고, 외부의 요청에 대해 적절한 형태로 가공해 응답할 수 있다.
  * Config Service
    > a. 각 서비스의 application.yml이 가지고 있는 설정을 스프링부트 내부가 아니라 외부에서 관리 <br>
    > b. 하나의 중앙화 된 저장소에 관리함으로써 환경설정 정보를 일괄적 관리, 분리된 환경에서 실행 가능하게 지원 <br>
    > c. 내부에 설정정보가 있다면, 설정 변동 시 어플리케이션을 재빌드 ➡️ 배포해야한다. <br>
    > d. 하지만 외부에 설정정보가 있다면 설정정보를 관리하는 서비스만 변경 후 갱신하면 된다. <br>
    >
    > ➡️ Spring Cloud Config 를 통해 각 서비스를 재빌드 하지 않고 설정 정보를 적용
    >
  * 그외 서비스
    
  ![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/def28d44-c2bf-4fae-b5fb-c84899f9ce22)

* ### 향후 계획
  * 학습 멘토링 서비스 : 다양한 학습 분야에 대해 여러 멘토들의 정보를 제공, 학생은 해당 정보를 통해 별도의 신청으로 매칭해주는 시스템 추가

    ➕ 1:1 채팅 기능으로 멘토와 대화

  * 인기/추천 강의 서비스 : 조회 수, 구매 건수, 좋아요 등의 일정 조건을 기준으로 인기 강의/추천 강의를 제공해 주는 시스템

    ➕ 강의 별 누적 수강생 수를 확인할 수 있는 시스템

## ⚒️ 사용된 도구
* 서버
  * Spring Boot 2.7.17, JPA, QueryDSL, Spring Cloud Eureka, Spring Cloud Config
    
* 인증 및 인가
  * Spring Security, OAuth2
    
* 세션 클러스터링
  * Redis
  
* DB 및 Storage
  * AWS RDS(Mysql), AWS S3
    
* 메세지 큐
  * RabbitMQ
    
* 화면
  * Thymeleaf
    
* CI / CD
  * Docker
  * Git
  * Jenkins
  * Ansible
  * AWS EC2
  
## 💻 기술 스택
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/72d2818d-fbf4-48c2-ab7c-d35c0dd62429)

## ⚙️ 아키텍처
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/278b17db-79bd-4796-8a92-2fe0654d48be)

## 🛠️ ERD 설계
* ### 👨‍👩‍👧‍👦 member-service
  
  ![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/6618e38c-3ca6-4d51-b0e0-a3e044345388)
  
* ### 🎁 item-service & ✉️ post-service
  
  ![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/186b3dfd-749e-4770-afbb-0a07a2b37718)
  
* ### 🧾 order-service
  
  ![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/0bd4aef9-0dff-4d97-9080-ada6c49e6061)

## 🔧 배포

![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/fbbf5956-55c3-4ce3-abf3-0222fe7a6682)

* Git ➡️ Jenkins ➡️ jar build ➡️ 도커 서버 내로 파일 복사
* IaC를 위한 Ansible 사용
  * 서버의 구성 정보를 변경하거나 관리하는 용도
  * 멱등성 : 같은 설정을 여러번 적용해도 결과가 달라지지 않고 한번만 적용
* CI
  > 1. Jenkins ➡️ Ansible 서버 내 playbook.yml 실행
  > 2. Docer 서버 내 jar 파일을 통한 Docker Image Build ➡️ Dockerfile 사용
  > 3. 생성한 이미지 Docker Hub로 push
  > 4. 생성한 이미지 삭제
* CD
  > 1. Jenkins ➡️ Ansible 서버 내 playbook.yml 실행
  > 2. 기존에 기동 중이던 컨테이너 stop
  > 3. 중지된 컨테이너 삭제
  > 4. docker run 명령어를 통해 Docker Hub에서 Image pull ➡️ 컨테이너 생성 ➡️ 컨테이너 실행
  > 5. 처음에 Docker 서버로 복사했던 jar 파일 삭제

## 🖥️ 서비스 소개

### 1. 메인 화면
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/904e68d0-5efc-4072-8b3f-0178deb19d98)

***

### 2. 회원가입
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/8edc1eed-11e2-4520-95e2-f681fec84493)

***

### 3. 로그인
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/5262f675-f4fe-4b86-9624-8f5a083e23dc)

***

### 4. 아이디 / 비밀번호 찾기
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/6f3d772f-4d53-4779-86f0-7a87786d01d8)

***

### 5. 강의 목록
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/a5557fc9-c2c5-46f1-9bc9-9af268c1bdb0)

***

### 6. 강의 상세
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/244ccc70-a6d1-42f8-8920-f44ca4f1538b)

***

### 7. 강의 질문 목록 / 질문 상세
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/efa999ad-091f-48f4-861b-e286912a89f0)

***

### 8. 도서 목록
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/d1c5c9fd-e2d4-4815-af01-6b1a21f2fce4)

***

### 9. 질문 목록 / 질문 상세
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/f5fc8dab-afb5-4362-892f-b6244d5951b6)

***

### 10. 장바구니 / 강의 결제
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/3d8f3c25-b654-487b-b853-2758d28f3f01)
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/5d27ad6c-4051-4332-afff-6ed86e83d3db)

***

### 11. 구매 내역
![image](https://github.com/hyoredm/bootcamp-coviscon/assets/102834723/e69833a0-630b-4b7d-82a2-38eb374d5f4b)

## 👩‍💻👨‍💻 역할 분담
* 이현수 - Member & 배포
* 임제정 - Order
* 김효경 - Member
* 문효주 - Item
* 이경승 - Post
