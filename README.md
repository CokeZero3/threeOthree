# 삼쩜삼 백엔드 엔지니어 채용 과제 Readme.md

# 요구사항
삼쩜삼 서비스에는 유저의 환급액을 계산해 주는 기능이 있습니다.
사용자가 삼쩜삼에 가입해야 합니다.
가입한 유저의 정보를 스크랩 하여 환급액이 있는지 조회합니다.
조회한 금액을 계산한 후 유저에게 실제 환급액을 알려줍니다.
UI를 제외하고 간소화된 REST API만 구현하시면 됩니다.

1. 회원가입
2. 로그인
3. 토큰발급확인
4. 로그인한 회원 정보 스크랩
5. 유저의 결정세액과 퇴직연금세액공제금액 계산
    
# 개발환경 
  -  IDE: intellij community
  -  JAVA 11 / STS 2.7.5 / GRADLE 8.5 / Swagger
  -  DB: H2 
  -  DEPENDENCIES: JPA, thymeleaf, Spring Web, devtools, validation, H2, LOMBOK, Spring Security

# 도메인 모델과 테이블 설계
  - 테이블은 Member(멤버), MemberInfo(멤버 정보), Salary(급여), IncomeDeduction(소득공제) 4개의 테이블로 구성되어 있습니다.
  - 회원가입과 로그인할 때 사용하는 멤버 테이블, 로그인한 개인의 정보를 담고있는 멤버정보, 급여, 소득공제 테이블로 나누어 놨고,
    멤버의 id 자동생성(Generated Value 설정)으로 외래키를 설정 했습니다. 
  

# 구현
  <<구현한 애플리케이션 설명>>
  1. 회원가입 할 때 Member에 데이터를 저장하면서 성공하면 추가로 이후에 사용할 데이터도 임의로 넣어주도록 설계를 했습니다.
    - 회원가입은 name, userId, password, regNo, 그리고 id(GenerateValue설정)으로 구성되어 있습니다.

A. 회원가입을 하면서 우선 validation 체크합니다. 순서는 허가된 이름과 주민번호 리스트에서 확인, 중복 회원, 그리고 중복아이디 확인 순으로 합니다.
 
B. 양방향 암복호화가 가능한 AesBytesEncryptor로 주민번호를 암호화해서 DB에 저장합니다. 

C. 비밀번호는 Spring Security PasswordEncoder으로 암호화 해서 데이터베이스에 저장합니다.
  
  3. 로그인 하면 토큰이 발급됩니다. 여기서 앞에 Bearer JwtToken + 토큰으로 출력되는데, 아래 토큰을 넣을 때 **Bearer JwtToken** 빼고 토큰만 넣어주셔야 정상 작동됩니다. 
    - 로그인은 userId로 DB 정보를 조회합니다. 조회된 데이터와 입력 데이터를 서비스에서 passwordEncoder.matches을 사용해 비밀번호가 일치하는지 확인합니다.
  4. 발급된 토큰으로 토큰 발급 확인 후 해당 회원의 정보를 읽어옵니다.
  5. 토큰으로 조회된 정보로 결정 세액과 퇴직연금세액공제금액 계산을 합니다.
