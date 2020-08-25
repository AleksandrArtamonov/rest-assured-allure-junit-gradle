package dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "commentPageBuilder")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentPage extends BasePage<Comment> {
    public CommentPage(Integer code, Meta meta, Comment[] data) {
        super(code, meta, data);
    }

}
