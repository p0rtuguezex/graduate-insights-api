package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseWrapper<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseWrapper<T> success(String message, T data) {
        return new ApiResponseWrapper<>(true, message, data);
    }

    public static <T> ApiResponseWrapper<T> error(String message) {
        return new ApiResponseWrapper<>(false, message, null);
    }
} 