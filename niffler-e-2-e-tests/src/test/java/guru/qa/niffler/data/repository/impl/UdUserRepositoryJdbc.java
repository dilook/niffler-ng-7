package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UdUserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UdUserRepositoryJdbc implements UdUserRepository {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.userdataJdbcUrl();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            ps.executeUpdate();
            final UUID generatedUserId;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedUserId = rs.getObject("id", UUID.class);
                } else {
                    throw new IllegalStateException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedUserId);
            if (!user.getFriendshipAddressees().isEmpty()) {
                for (FriendshipEntity friendshipAddressee : user.getFriendshipAddressees()) {
                    addIncomeInvitation(user, friendshipAddressee.getAddressee());
                }
            }

            if (!user.getFriendshipRequests().isEmpty()) {
                for (FriendshipEntity friendshipRequest : user.getFriendshipRequests()) {
                    addOutcomeInvitation(user, friendshipRequest.getAddressee());
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "SELECT * FROM \"user\" u LEFT JOIN friendship f ON (u.id = f.addressee_id OR u.id = f.requester_id) WHERE u.id = ?")) {
            ps.setObject(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            UserEntity user = null;

            while (rs.next()) {
                if (user == null) {
                    user = UdUserEntityRowMapper.instance.mapRow(rs, 1);
                }
                if (rs.getString("status") != null) {
                    FriendshipEntity friendshipEntity = new FriendshipEntity();
                    UserEntity reqUser = new UserEntity();
                    reqUser.setId(rs.getObject("requester_id", UUID.class));
                    friendshipEntity.setRequester(reqUser);

                    UserEntity addresseeUser = new UserEntity();
                    addresseeUser.setId(rs.getObject("addressee_id", UUID.class));
                    friendshipEntity.setAddressee(addresseeUser);

                    friendshipEntity.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                    friendshipEntity.setCreatedDate(rs.getDate("created_date"));

                    if (rs.getObject("requester_id", UUID.class).equals(user.getId())) {
                        user.getFriendshipRequests().add(friendshipEntity);
                    } else {
                        user.getFriendshipAddressees().add(friendshipEntity);
                    }
                }
            }
            if (user == null) {
                return Optional.empty();
            } else {
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "SELECT * FROM \"user\" u LEFT JOIN friendship f ON (u.id = f.addressee_id OR u.id = f.requester_id)")) {
            ps.execute();
            ResultSet rs = ps.getResultSet();

            Map<UUID, UserEntity> users = new HashMap<>();
            UUID userId;

            while (rs.next()) {
                userId = rs.getObject("id", UUID.class);
                UserEntity user = users.computeIfAbsent(userId, id -> {
                    try {
                        return UdUserEntityRowMapper.instance.mapRow(rs, 1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (rs.getString("status") != null) {
                    FriendshipEntity friendshipEntity = new FriendshipEntity();
                    UserEntity reqUser = new UserEntity();
                    reqUser.setId(rs.getObject("requester_id", UUID.class));
                    friendshipEntity.setRequester(reqUser);

                    UserEntity addresseeUser = new UserEntity();
                    addresseeUser.setId(rs.getObject("addressee_id", UUID.class));
                    friendshipEntity.setAddressee(addresseeUser);

                    friendshipEntity.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                    friendshipEntity.setCreatedDate(rs.getDate("created_date"));

                    if (rs.getObject("requester_id", UUID.class).equals(userId)) {
                        user.getFriendshipRequests().add(friendshipEntity);
                    } else {
                        user.getFriendshipAddressees().add(friendshipEntity);
                    }
                }
            }
            return new ArrayList<>(users.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement friendshipPs = holder(url).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)")) {
            friendshipPs.setObject(1, addressee.getId());
            friendshipPs.setObject(2, requester.getId());
            friendshipPs.setString(3, FriendshipStatus.PENDING.name());
            friendshipPs.setDate(4, new Date(System.currentTimeMillis()));
            friendshipPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        addIncomeInvitation(addressee, requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement fReqPs = holder(url).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)");
             PreparedStatement fAddPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                             "VALUES (?, ?, ?, ?)")) {
            fReqPs.setObject(1, requester.getId());
            fReqPs.setObject(2, addressee.getId());
            fReqPs.setString(3, FriendshipStatus.ACCEPTED.name());
            fReqPs.setDate(4, new Date(System.currentTimeMillis()));
            fReqPs.executeUpdate();

            fAddPs.setString(1, addressee.getId().toString());
            fAddPs.setString(2, requester.getId().toString());
            fAddPs.setString(3, FriendshipStatus.ACCEPTED.name());
            fAddPs.setDate(4, new Date(System.currentTimeMillis()));
            fAddPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
