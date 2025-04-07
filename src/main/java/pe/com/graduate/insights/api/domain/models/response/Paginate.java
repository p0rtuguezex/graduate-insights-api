package pe.com.graduate.insights.api.domain.models.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Paginate {
    @JsonProperty("totalElements")
    private Long totalElements;

    @JsonProperty("totalPaginas")
    private Integer totalPaginas;

    @JsonProperty("paginaActual")
    private Integer paginaActual;


    public static Paginate copiar(Paginate others){
        if (others==null){
            System.out.println("Error paginas siguiente inexistente ");
            return null;
        }
        return Paginate.builder()
                .totalElements(others.getTotalElements())
                .totalPaginas(others.getTotalPaginas())
                .paginaActual(others.getPaginaActual())
                .build();
    }

}
