package udpm.hn.server.infrastructure.security.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import udpm.hn.server.utils.SecurityUtil;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Slf4j
public class TokenUriResponse {

    private String accessToken;

    private String refreshToken;

    public String getTokenAuthorizationSimple() {
        try {
            // Use Jackson ObjectMapper for proper JSON serialization
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", accessToken);
            tokenMap.put("refreshToken", refreshToken);
            String tokenObject = objectMapper.writeValueAsString(tokenMap);
            return SecurityUtil.encodeBase64(tokenObject);
        } catch (JsonProcessingException e) {
            log.error("Error serializing token data to JSON", e);
            // Fallback to manual approach if Jackson fails
            String tokenObject = "{" + "\"accessToken\":\"" + accessToken + "\",\"refreshToken\":\"" + refreshToken + "\"}";
            return SecurityUtil.encodeBase64(tokenObject);
        }
    }

    public static String getState(
            String accessToken,
            String refreshToken
    ) {
        TokenUriResponse tokenUriResponse = new TokenUriResponse(accessToken, refreshToken);
        return tokenUriResponse.getTokenAuthorizationSimple();
    }

}
