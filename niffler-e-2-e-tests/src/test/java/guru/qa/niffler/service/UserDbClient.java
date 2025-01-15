package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();

    public UserdataUserEntity createUser(AuthUserEntity authUserEntity, UserdataUserEntity userdataUserEntity) {
        return (UserdataUserEntity) xaTransaction(new XaFunction<>(
                        connection -> {
                            Optional<AuthUserEntity> userEntity = new AuthUserDaoJdbc(connection).findByUsername(authUserEntity.getUsername());
                            AuthUserEntity result = userEntity.orElseGet(() -> new AuthUserDaoJdbc(connection).createUser(authUserEntity));
                            new AuthAuthorityDaoJdbc(connection).create(new AuthorityEntity(null, Authority.read, authUserEntity));
                            new AuthAuthorityDaoJdbc(connection).create(new AuthorityEntity(null, Authority.write, authUserEntity));
                            return result;
                        },
                        CFG.authJdbcUrl()),
                new XaFunction<>(
                        connection -> new UserDaoJdbc(connection).createUser(userdataUserEntity),
                        CFG.userdataJdbcUrl()
                )
        );
    }
}
