package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        authUserDao.create(user);
        authAuthorityDao.create(user.getAuthorities().toArray(AuthorityEntity[]::new));
        return user;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        authUserDao.update(user);
        user.getAuthorities().forEach(authAuthorityDao::update);
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return findUser("select * from \"user\" u join authority a on u.id = a.user_id where u.id = ?", id);
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return findUser("select * from \"user\" u join authority a on u.id = a.user_id where u.username = ?", username);
    }

    @Override
    public List<AuthUserEntity> findAll() {
        return authUserDao.findAll();
    }

    @Override
    public void remove(AuthUserEntity user) {
        authUserDao.remove(user);
    }

    private Optional<AuthUserEntity> findUser(String query, Object param) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(query)) {
            ps.setObject(1, param);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthorityEntity> authorityEntities = new ArrayList<>();
                while (rs.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
                    }

                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(user);
                    ae.setId(rs.getObject("a.id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    authorityEntities.add(ae);
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorityEntities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
