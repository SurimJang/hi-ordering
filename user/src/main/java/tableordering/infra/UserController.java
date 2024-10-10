package tableordering.infra;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tableordering.domain.User;
import tableordering.domain.UserRepository;

@RestController
@Transactional  // TODO: 트랜잭션 필요성 재고
@RequestMapping("/users") // API의 기본 경로를 /users로 설정
public class UserController {

    @Autowired
    UserRepository userRepository;

    // Mock 데이터
    // TODO: keycloack 정상화 시 변경
    private final String mockUsername = "test";
    private final String mockPassword = "1234";
    private final String storeName = "이루다제면소 일산점 ";

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // 사용자 인증 로직 (Mock 데이터 사용)
        if (mockUsername.equals(username) && mockPassword.equals(password)) {
            Map<String, String> response = new HashMap<>();
            response.put("store_name", storeName);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
