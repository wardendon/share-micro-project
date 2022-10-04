package top.w2gd.user.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常处理
 * @author w2gd
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionErrorHandler {
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity error(SecurityException e) {
        log.warn("发生 SecurityException 异常",e );
        return new ResponseEntity<>(
          ErrorBody.builder()
                  .body(e.getMessage())
                  .status(HttpStatus.UNAUTHORIZED.value())
                  .build(),
                HttpStatus.UNAUTHORIZED
        );
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorBody {
    private String body;
    private int status;
}