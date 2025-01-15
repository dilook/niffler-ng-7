package guru.qa.niffler.data.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AuthorityEntity implements Serializable {
    private UUID id;
    private Authority authority;
    private AuthUserEntity user;
}
