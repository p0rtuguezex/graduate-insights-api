package pe.com.graduate.insights.api.domain.models.request;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@ToString
public class DirectorRequest extends UserRequest implements Serializable {}
