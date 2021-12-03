package com.innerfriends.friends.infrastructure.postgres;

import com.innerfriends.friends.domain.Version;
import com.innerfriends.friends.domain.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"friendId", "version"}),
        name = "T_FRIEND"
)
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class FriendEntity {

    @Id
    @NotNull
    private String friendId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<String> friends;

    @NotNull
    private String bio;

    @NotNull
    private Integer nbOfFriends;

    @NotNull
    private Long version;

    public FriendEntity() {}

    public FriendEntity(final Friend friend) {
        this(friend.friendId().pseudo(),
                friend.inFriendshipsWith().stream()
                        .map(InFriendshipWithId::pseudo)
                        .collect(Collectors.toList()),
                friend.bio().content(),
                friend.nbOfFriends(),
                friend.version().value());
    }

    public FriendEntity(final String friendId,
                        final List<String> friends,
                        final String bio,
                        final Integer nbOfFriends,
                        final Long version) {
        this.friendId = Objects.requireNonNull(friendId);
        this.friends = Objects.requireNonNull(friends);
        this.bio = Objects.requireNonNull(bio);
        this.nbOfFriends = Objects.requireNonNull(nbOfFriends);
        this.version = Objects.requireNonNull(version);
    }

    public Friend toFriend() {
        return new Friend(
                new FriendId(friendId),
                friends.stream().map(InFriendshipWithId::new).collect(Collectors.toList()),
                new Bio(bio),
                new Version(version));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendEntity)) return false;
        final FriendEntity that = (FriendEntity) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(friends, that.friends) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(nbOfFriends, that.nbOfFriends) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, friends, bio, nbOfFriends, version);
    }
}
