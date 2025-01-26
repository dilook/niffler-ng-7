package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbcWithError;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;


public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
    private final UdUserDao udUserDaoSpringJdbc = new UdUserDaoSpringJdbc();
    private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
    private final UdUserDao udUserDaoJdbc = new UdUserDaoJdbc();
    private final UdUserDao udUserDaoJdbcWithError = new UdUserDaoJdbcWithError();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @SuppressWarnings("deprecation")
    private final TransactionTemplate xaChainedTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            DataSources.testDataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            DataSources.testDataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    private static AuthorityEntity[] getAuthorityEntities(AuthUserEntity createdAuthUser) {
        return Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);
    }

    public UserJson createUserJdbcWithoutTx(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        AuthUserEntity createdAuthUser = authUserDaoJdbc.create(authUser);

        AuthorityEntity[] authorityEntities = getAuthorityEntities(createdAuthUser);
        authAuthorityDaoJdbc.create(authorityEntities);

        return UserJson.fromEntity(
                udUserDaoJdbc.create(UserEntity.fromJson(user)),
                null
        );
    }

    @SuppressWarnings("unchecked")
    public UserJson createUserJdbcWithXa(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDaoJdbc.create(authUser);
                    AuthorityEntity[] authorityEntities = getAuthorityEntities(createdAuthUser);

                    authAuthorityDaoJdbc.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDaoJdbc.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    @SuppressWarnings("unchecked")
    public UserJson createUserSpringJdbcWithXa(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

                    AuthorityEntity[] authorityEntities = getAuthorityEntities(createdAuthUser);

                    authAuthorityDaoSpringJdbc.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDaoSpringJdbc.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }


    public UserJson createUserSpringJdbcWithoutTx(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

        AuthorityEntity[] authorityEntities = getAuthorityEntities(createdAuthUser);

        authAuthorityDaoSpringJdbc.create(authorityEntities);
        return UserJson.fromEntity(
                udUserDaoSpringJdbc.create(UserEntity.fromJson(user)),
                null
        );
    }

    public UserJson createUserWithChained(UserJson user) {
        return xaChainedTemplate.execute(status -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

            AuthorityEntity[] authorityEntities = getAuthorityEntities(createdAuthUser);

            authAuthorityDaoSpringJdbc.create(authorityEntities);
            return UserJson.fromEntity(
                    udUserDaoSpringJdbc.create(UserEntity.fromJson(user)),
                    null
            );
        });
    }
}
