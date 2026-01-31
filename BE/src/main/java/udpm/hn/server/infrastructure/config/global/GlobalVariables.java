package udpm.hn.server.infrastructure.config.global;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalVariables {

    private static final ThreadLocal<Map<String, Object>> threadLocalVariables = 
        ThreadLocal.withInitial(HashMap::new);

    public void setGlobalVariable(String key, Object value) {
        threadLocalVariables.get().put(key, value);
    }

    public Object getGlobalVariable(String key) {
        return threadLocalVariables.get().get(key);
    }

    public void clear() {
        threadLocalVariables.get().clear();
    }
}