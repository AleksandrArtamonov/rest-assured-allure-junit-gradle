package dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "todoPageBuilder")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TodoPage extends BasePage<Todo>{
    public TodoPage(Integer code, Meta meta, Todo[] data) {
        super(code, meta, data);
    }
}
