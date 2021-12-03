package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.Bio;
import com.innerfriends.friends.domain.ExecutedBy;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class WriteBioCommand implements UseCaseCommand<FriendId> {

    private final FriendId friendId;
    private final Bio bio;
    private final ExecutedBy executedBy;

    public WriteBioCommand(final FriendId friendId, final Bio bio, final ExecutedBy executedBy) {
        this.friendId = Objects.requireNonNull(friendId);
        this.bio = Objects.requireNonNull(bio);
        this.executedBy = Objects.requireNonNull(executedBy);
    }

    @Override
    public FriendId identifier() {
        return friendId;
    }

    public FriendId friendId() {
        return friendId;
    }

    public Bio bio() {
        return bio;
    }

    public ExecutedBy executedBy() {
        return executedBy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof WriteBioCommand)) return false;
        final WriteBioCommand that = (WriteBioCommand) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(executedBy, that.executedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, bio, executedBy);
    }

    @Override
    public String toString() {
        return "WriteBioCommand{" +
                "friendId=" + friendId +
                ", bio=" + bio +
                ", executedBy=" + executedBy +
                '}';
    }
}
