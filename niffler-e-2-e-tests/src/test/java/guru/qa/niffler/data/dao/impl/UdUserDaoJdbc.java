package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UdUserDaoJdbc implements UdUserDao {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.userdataJdbcUrl();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
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
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity update(UserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "UPDATE \"user\" SET " +
                        "currency = ?, " +
                        "firstname = ?, " +
                        "surname = ?," +
                        "photo = ?," +
                        "photo_small = ?," +
                        "full_name = ? " +
                        "WHERE id = ?");
             PreparedStatement friendshipPs = holder(url).connection().prepareStatement(
                     "INSERT INTO friendship (requester_id, addressee_id, status) " +
                     "VALUES (?, ?, ?) " +
                     "ON CONFLICT (requester_id, addressee_id)" +
                     "DO UPDATE SET status = ?")
        ) {
            ps.setString(1, user.getCurrency().name());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getSurname());
            ps.setBytes(4, user.getPhoto());
            ps.setBytes(5, user.getPhotoSmall());
            ps.setString(6, user.getFullname());
            ps.setObject(7, user.getId());
            ps.executeUpdate();

            for (FriendshipEntity fe : user.getFriendshipRequests()) {
                friendshipPs.setObject(1, user.getId());
                friendshipPs.setObject(2, fe.getAddressee().getId());
                friendshipPs.setString(3, fe.getStatus().name());
                friendshipPs.setString(4, fe.getStatus().name());
                friendshipPs.addBatch();
                friendshipPs.clearParameters();
            }
            friendshipPs.executeBatch();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("SELECT * FROM \"user\" WHERE id = ? ")) {
            ps.setObject(1, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            if (rs.next()) {
                return Optional.ofNullable(
                        UdUserEntityRowMapper.instance.mapRow(rs, rs.getRow())
                );
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement("SELECT * FROM \"user\" WHERE username = ? ")) {
            ps.setObject(1, username);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            if (rs.next()) {
                return Optional.ofNullable(
                        UdUserEntityRowMapper.instance.mapRow(rs, rs.getRow())
                );
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "SELECT * FROM \"user\"")) {
            ps.execute();
            List<UserEntity> result = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    result.add(UdUserEntityRowMapper.instance.mapRow(rs, rs.getRow()));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(UserEntity user) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "DELETE FROM \"user\" where id = ?")) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
