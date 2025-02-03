package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.authJdbcUrl();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            "VALUES (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);

        List<AuthorityEntity> authorities = user.getAuthorities();
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (? , ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authorities.get(i).getUser().getId());
                        ps.setString(2, authorities.get(i).getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authorities.size();
                    }
                });

        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        "SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired " +
                                "FROM \"user\" u join authority a on u.id = a.user_id WHERE u.id = ?",
                        rs -> {
                            AuthUserEntity user = null;
                            while (rs.next()) {
                                if (user == null) {
                                    user = new AuthUserEntity();
                                    user.setId(UUID.fromString(rs.getString("id")));
                                    try {
                                        user.setUsername(rs.getString("username"));
                                        user.setPassword(rs.getString("password"));
                                        user.setEnabled(rs.getBoolean("enabled"));
                                        user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                                        user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                                        user.setCredentialsNonExpired(rs.getBoolean(" credentials_non_expired"));
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                AuthorityEntity authority = new AuthorityEntity();
                                authority.setId(rs.getObject("authority_id", UUID.class));
                                authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                                user.addAuthorities(authority);
                            }
                            return user;
                        },
                        id
                )
        );
    }

    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        return jdbcTemplate.query(
                "SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired " +
                        "FROM \"user\" u join authority a on u.id = a.user_id",
                rs -> {
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
                    return userMap.values().stream().toList();
                }
        );
    }
}
