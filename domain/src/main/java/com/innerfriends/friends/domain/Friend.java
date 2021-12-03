package com.innerfriends.friends.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Friend extends Aggregate {

    public static final InFriendshipWithId DAM_DAM_DEO = new InFriendshipWithId("DamDamDeo");

    private final FriendId friendId;
    private final List<InFriendshipWithId> friendshipsWith;
    private Bio bio;

    public Friend(final FriendId friendId) {
        this(friendId, new ArrayList<>(List.of(DAM_DAM_DEO)),
                new Bio(), new Version(0l));
    }

    public Friend(final FriendId friendId,
                  final List<InFriendshipWithId> friendshipsWith,
                  final Bio bio,
                  final Version version) {
        super(version);
        this.friendId = Objects.requireNonNull(friendId);
        this.friendshipsWith = Objects.requireNonNull(friendshipsWith);
        this.bio = Objects.requireNonNull(bio);
    }

    public Friend writeBio(final Bio bio, final ExecutedBy executedBy) {
        if (!executedBy.pseudoId().equals(friendId.pseudoId())) {
            throw new IllegalStateException("The one doing the action must be the friend");
        }
        this.apply(() -> this.bio = bio);
        return this;
    }

    public Friend establishAFriendship(final FriendId friendIdToEstablishAFriendshipWith, final ExecutedBy executedBy) {
        if (!executedBy.pseudoId().equals(friendId.pseudoId())) {
            throw new IllegalStateException("The one doing the action must be the friend");
        }
        this.establishAFriendship(friendIdToEstablishAFriendshipWith);
        return this;
    }

    public Friend establishAFriendship(final FriendId friendIdToEstablishAFriendshipWith) {
        if (friendId.pseudoId().equals(friendIdToEstablishAFriendshipWith.pseudoId())) {
            throw new IllegalStateException("You could not add yourself as a friend");
        }
        if (DAM_DAM_DEO.pseudoId().equals(friendIdToEstablishAFriendshipWith.pseudoId())) {
            return this;
        }
        if (friendshipsWith.contains(new InFriendshipWithId(friendIdToEstablishAFriendshipWith))) {
            throw new AlreadyAFriendException();
        }
        this.apply(() -> {
            friendshipsWith.add(new InFriendshipWithId(friendIdToEstablishAFriendshipWith));
            return null;
        });
        return this;
    }

    public FriendId friendId() {
        return friendId;
    }

    public List<InFriendshipWithId> inFriendshipsWith() {
        return Collections.unmodifiableList(friendshipsWith);
    }

    public Bio bio() {
        return bio;
    }

    public Integer nbOfFriends() {
        return friendshipsWith.size();
    }

    public boolean hasBio() {
        return bio.hasBio();
    }

    public boolean isInFriendshipWith(final InFriendshipWithId inFriendshipWithId) {
        return friendshipsWith.contains(inFriendshipWithId);
    }

    public InFriendshipWithId lastEstablishedFriendshipWith() {
        return friendshipsWith.stream().reduce((first, second) -> second).get();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Friend)) return false;
        if (!super.equals(o)) return false;
        final Friend friend = (Friend) o;
        return Objects.equals(friendId, friend.friendId) &&
                Objects.equals(friendshipsWith, friend.friendshipsWith) &&
                Objects.equals(bio, friend.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), friendId, friendshipsWith, bio);
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friendId=" + friendId +
                ", friendshipsWith=" + friendshipsWith +
                ", bio=" + bio +
                "} " + super.toString();
    }
}
