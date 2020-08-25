package dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "postPageBuilder")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostPage extends BasePage<Post> {
    public PostPage(Integer code, Meta meta, Post[] data) {
        super(code, meta, data);
    }
}
