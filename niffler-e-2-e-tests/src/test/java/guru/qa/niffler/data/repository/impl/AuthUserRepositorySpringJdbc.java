package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityListExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.authJdbcUrl();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        AuthUserEntity authUserEntity = authUserDao.create(user);
        authAuthorityDao.create(user.getAuthorities().toArray(AuthorityEntity[]::new));
        return authUserEntity;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        user.getAuthorities().forEach(user::removeAuthority);
        authAuthorityDao.create(user.getAuthorities().toArray(AuthorityEntity[]::new));
        return authUserDao.update(user);
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        List<AuthUserEntity> result = jdbcTemplate.query(
                "SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired " +
                        "FROM \"user\" u join authority a on u.id = a.user_id WHERE u.id = ?",
                AuthUserEntityListExtractor.INSTANCE,
                id
        );
        return Optional.ofNullable(result.isEmpty() ? null : result.getFirst());
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        List<AuthUserEntity> result = jdbcTemplate.query(
                "SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired " +
                        "FROM \"user\" u join authority a on u.id = a.user_id WHERE u.username = ?",
                AuthUserEntityListExtractor.INSTANCE,
                username
        );
        return Optional.ofNullable(result.isEmpty() ? null : result.getFirst());
    }

    @Override
    public void remove(AuthUserEntity user) {
        authAuthorityDao.remove(user.getAuthorities().toArray(AuthorityEntity[]::new));
        authUserDao.remove(user);
    }

    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        return jdbcTemplate.query(
                "SELECT a.id as authority_id, authority, user_id as id, u.username, u.password, u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired " +
                        "FROM \"user\" u join authority a on u.id = a.user_id",
                AuthUserEntityListExtractor.INSTANCE
        );
    }
}
