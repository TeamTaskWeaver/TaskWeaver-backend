package taskweaver.taskweaver_backend.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    // Refresh Token 쿠키 생성
    public void createRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite("Lax") // HTTP 환경을 위해 Lax로 고정
                //.secure(true) // HTTP 환경에서는 이 속성을 절대 사용하면 안 됩니다.
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void createAccessTokenCookie(HttpServletResponse response, String accessToken, long maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(false) // ★★★ JS에서 읽어야 하므로 false로 설정!
                .path("/")
                .maxAge(maxAgeSeconds) // AccessToken의 유효기간과 동일하게 설정
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // 쿠키에서 Refresh Token 가져오기
    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return getCookieValue(request, "refreshToken");
    }

    // 쿠키 삭제 (로그아웃 시)
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}