package dto;

import lombok.*;

@Data
@Builder(builderMethodName = "userPageBuilder")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPage extends BasePage<User> {
    public UserPage(Integer code, Meta meta, User[] data) {
        super(code, meta, data);
    }
}
