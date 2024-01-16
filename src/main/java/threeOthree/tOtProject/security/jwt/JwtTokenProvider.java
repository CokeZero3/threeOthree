package threeOthree.tOtProject.security.jwt;

import com.nimbusds.jose.crypto.PasswordBasedDecrypter;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.security.config.SecurityConfig;
import threeOthree.tOtProject.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${security.jwt.secret-key}") String secretKey;
    // 토큰 유효시간 30분
    private long tokenValidTime = 30 * 60 * 1000L;
    private Key key;
    private final UserDetailsService userDetailsService;
    private MemberService memberService;
    // 객체 초기화, secretKey를 Base64로 인코딩
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
//    public String createToken(String userPK, Role roles) {
    public String createToken(Member member) {

        Claims claims = Jwts.claims().setSubject(member.getName()); // JWT payload에 저장되는 정보 단위
        claims.setId(member.getUserId());
        log.info("member.getRegNo() 복호화 전: "+ member.getRegNo());
        log.info("member.getUserId: "+member.getUserId() );

        claims.put("userId", member.getUserId());
        claims.put("regNo", member.getRegNo());

        //claims.put("roles", roles); // 정보 저장 (key-value)
        Date now = new Date();
        log.info("secretkey:: "+secretKey);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature에 들어갈 secret 값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPK(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public Map<String, Object> getUserPKList(String token) {
        Map<String, Object>  map = new HashMap<>();
        String name = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        String regNo = (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("regNo");
        String userId = (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userId");

        //log.info("regNo = " + regNo);
        map.put("regNo", regNo);
        map.put("name", name);
        map.put("userId", userId);
        return map;
    }
    public String getUserPK(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN": "TOKEN 값"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    /*public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }*/
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return true;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken2(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
