package dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(builderMethodName = "blankFieldPageBuilder")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BlankFieldPage extends BasePage<BlankField>{
    public BlankFieldPage(Integer code, Meta meta, BlankField[] data) {
        super(code, meta, data);
    }
}
