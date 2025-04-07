package pe.com.graduate.insights.api.domain.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse <T>{

 @JsonProperty("data")
 private T data;

 @JsonProperty("paginate")
 private Paginate paginate;

 @JsonProperty("errors")
 private Object errors;

 @JsonProperty("code")
 private Integer code;

 @JsonProperty("message")
 private String message;

    public ApiResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ApiResponse(T data, Integer code, Paginate paginate, String message) {
        this.data = data;
        this.code = code;
        this.paginate = paginate;
        this.message = message;
    }

    public ApiResponse(T data, Paginate paginate, Object errors, Integer code, String message) {
        this.data = data;
        this.paginate = paginate;
        this.errors = errors;
        this.code = code;
        this.message = message;
    }

    // metodo para obtener una copia de objeto  la paginacion - evita que se pueda mofidicar
    // por otras clases

    public Paginate getPaginate(){
        return Paginate.copiar(paginate);
    }
}
