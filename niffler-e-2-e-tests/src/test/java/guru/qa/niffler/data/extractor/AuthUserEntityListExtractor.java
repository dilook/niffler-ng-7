package guru.qa.niffler.data.extractor;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AuthUserEntityListExtractor implements ResultSetExtractor<List<AuthUserEntity>> {
    public final static AuthUserEntityListExtractor INSTANCE = new AuthUserEntityListExtractor();

    private AuthUserEntityListExtractor() {
    }

    @Override
    public @Nonnull List<AuthUserEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new HashMap<>();
        UUID userId;
        while (rs.next()) {
            userId = UUID.fromString(rs.getString("id"));

            AuthUserEntity user = userMap.computeIfAbsent(userId, id -> {
                AuthUserEntity aue = new AuthUserEntity();
                try {
                    aue.setId(UUID.fromString(rs.getString("id")));
                    aue.setUsername(rs.getString("username"));
                    aue.setPassword(rs.getString("password"));
                    aue.setEnabled(rs.getBoolean("enabled"));
                    aue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    aue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    aue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return aue;
            });
            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(rs.getObject("authority_id", UUID.class));
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
            user.addAuthorities(authority);
        }
        return new ArrayList<>(userMap.values());
    }
}
