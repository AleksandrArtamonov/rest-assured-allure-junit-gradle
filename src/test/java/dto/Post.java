package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Builder (toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post {
    private Integer id;
    private @JsonProperty("user_id") Integer userId;
    private String title;
    private String body;
    private @JsonProperty("created_at") Date createdAt;
    private @JsonProperty("updated_at") Date updatedAt;
}
