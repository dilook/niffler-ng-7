package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UdUserRepository {

    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    List<UserEntity> findAll();

    void addIncomeInvitation(UserEntity requester, UserEntity addressee);

    void addOutcomeInvitation(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);
}
