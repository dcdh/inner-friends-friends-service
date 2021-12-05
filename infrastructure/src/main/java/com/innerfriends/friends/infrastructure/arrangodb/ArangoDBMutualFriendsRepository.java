package com.innerfriends.friends.infrastructure.arrangodb;

import com.arangodb.*;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.BaseEdgeDocument;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.StreamTransactionEntity;
import com.arangodb.model.EdgeCreateOptions;
import com.arangodb.model.StreamTransactionOptions;
import com.arangodb.model.VertexCreateOptions;
import com.arangodb.model.VertexUpdateOptions;
import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.infrastructure.opentelemetry.NewSpan;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.*;
import java.util.stream.Collectors;

//===========================================================================
// /!\ do not use Velocypack it does not seems to work in native mode !
//===========================================================================
@ApplicationScoped
public class ArangoDBMutualFriendsRepository implements MutualFriendsRepository {

    private static final Logger LOG = Logger.getLogger(ArangoDBMutualFriendsRepository.class);

    public static final String FRIENDS = "FRIENDS";
    public static final String IN_FRIENDSHIP_WITH = "IN_FRIENDSHIP_WITH";

    private final ArangoDB arangoDB;
    private final String dbName;
    private ArangoDatabase arangoDatabase;

    public ArangoDBMutualFriendsRepository(final ArangoDB arangoDB,
                                           @ConfigProperty(name = "arangodb.dbName") final String dbName) {
        this.arangoDB = Objects.requireNonNull(arangoDB);
        this.dbName = Objects.requireNonNull(dbName);
    }

    /** un-secure : relationship not checked
     * LET friendInFriendshipsWith = (FOR v, e, p IN 2..2 OUTBOUND 'FRIENDS/Mario' GRAPH 'FRIENDS' RETURN v._key)
     * LET inFriendshipWithFriends = (FOR v, e, p IN 1..1 OUTBOUND 'FRIENDS/DonkeyKong' GRAPH 'FRIENDS' RETURN v._key)
     * LET mutualFriends = INTERSECTION(friendInFriendshipsWith,inFriendshipWithFriends)
     * FOR mutualFriend IN mutualFriends RETURN DISTINCT mutualFriend
     */

    /** secure
     * LET friendInFriendshipsWith = (FOR v IN 1..1 OUTBOUND 'FRIENDS/Mario' GRAPH 'FRIENDS'
     * 		FILTER v._id == 'FRIENDS/DonkeyKong'
     * 		FOR vInFriendshipWith IN 1..1 OUTBOUND v GRAPH 'FRIENDS' RETURN vInFriendshipWith._key)
     * LET inFriendshipWithFriends = (FOR v IN 1..1 OUTBOUND 'FRIENDS/Mario' GRAPH 'FRIENDS'
     * 		FILTER v._id != 'FRIENDS/DonkeyKong'
     * 		RETURN v._key)
     * LET mutualFriends = INTERSECTION(friendInFriendshipsWith,inFriendshipWithFriends)
     * FOR mutualFriend IN mutualFriends RETURN DISTINCT mutualFriend
     */

    @Override
    // https://www.arangodb.com/docs/stable/aql/graphs-traversals.html
    @NewSpan
    public List<MutualFriendId> getMutualFriends(final FriendId friendId, final InFriendshipWithId inFriendshipWithId) {
        try {
//            final String query = "LET friendInFriendshipsWith = (FOR v, e, p IN 2..2 OUTBOUND @friendId GRAPH @graph RETURN v._key) " +
//                    "LET inFriendshipWithFriends = (FOR v, e, p IN 2..2 OUTBOUND @inFriendshipWithId GRAPH @graph RETURN v._key) " +
//                    "LET mutualFriends = INTERSECTION(friendInFriendshipsWith,inFriendshipWithFriends) " +
//                    "FOR mutualFriend IN mutualFriends RETURN DISTINCT mutualFriend";// DISTINCT: optimization. Complied with domain
            final String query =
                    "LET friendInFriendshipsWith = (FOR v IN 1..1 OUTBOUND @friendId GRAPH @graph\n" +
                    "   FILTER v._id == @inFriendshipWithId\n" +
                    "   FOR vInFriendshipWith IN 1..1 OUTBOUND v GRAPH @graph RETURN vInFriendshipWith._key)\n" +
                    "LET inFriendshipWithFriends = (FOR v IN 1..1 OUTBOUND @friendId GRAPH @graph\n" +
                    "   FILTER v._id != @inFriendshipWithId\n" +
                    "   RETURN v._key)\n" +
                    "LET mutualFriends = INTERSECTION(friendInFriendshipsWith,inFriendshipWithFriends)\n" +
                    "FOR mutualFriend IN mutualFriends RETURN DISTINCT mutualFriend";// DISTINCT: optimization. Complied with domain
            final Map<String, Object> bindVars = Map.of(
                    "friendId", String.format("%s/%s", FRIENDS, friendId.pseudo()),
                    "inFriendshipWithId", String.format("%s/%s", FRIENDS, inFriendshipWithId.pseudo()),
                    "graph", FRIENDS);
            final ArangoCursor<String> cursor = arangoDB.db(dbName).query(query, bindVars, null, String.class);
            return cursor.stream()
                    .map(MutualFriendId::new)
                    .collect(Collectors.toList());
        } catch (final ArangoDBException arangoDBException) {
            LOG.error(arangoDBException);
            throw arangoDBException;
        }
    }

    @Override
    @NewSpan
    public List<MutualFriendId> getMutualFriends(final FriendId friendId,
                                                 final InFriendshipWithId inFriendshipWithId,
                                                 final FriendOfFriendId friendOfFriendId) {
        try {
            final String query =
                    "LET friendsOfFriend = (FOR v IN 1..1 OUTBOUND @friendId GRAPH @graph\n" +
                    "   FILTER v._id == @inFriendshipWithId\n" +
                    "   FOR vInFriendshipWith IN 1..1 OUTBOUND v GRAPH @graph\n" +
                    "       FILTER vInFriendshipWith._id == @friendOfFriendId\n" +
                    "       FOR vFriendsOfFriend IN 1..1 OUTBOUND vInFriendshipWith GRAPH @graph RETURN vFriendsOfFriend._key)\n" +
                    "LET inFriendshipWithFriends = (FOR v IN 1..1 OUTBOUND @friendId GRAPH @graph\n" +
                    "   FILTER v._id != @inFriendshipWithId\n" +
                    "   RETURN v._key)\n" +
                    "LET mutualFriends = INTERSECTION(friendsOfFriend,inFriendshipWithFriends)\n" +
                    "FOR mutualFriend IN mutualFriends RETURN DISTINCT mutualFriend";// DISTINCT: optimization. Complied with domain
            final Map<String, Object> bindVars = Map.of(
                    "friendId", String.format("%s/%s", FRIENDS, friendId.pseudo()),
                    "inFriendshipWithId", String.format("%s/%s", FRIENDS, inFriendshipWithId.pseudo()),
                    "friendOfFriendId", String.format("%s/%s", FRIENDS, friendOfFriendId.pseudo()),
                    "graph", FRIENDS);
            final ArangoCursor<String> cursor = arangoDB.db(dbName).query(query, bindVars, null, String.class);
            return cursor.stream()
                    .map(MutualFriendId::new)
                    .collect(Collectors.toList());
        } catch (final ArangoDBException arangoDBException) {
            LOG.error(arangoDBException);
            throw arangoDBException;
        }
    }

    @NewSpan
    public void registerNewFriendIntoThePlatform(final FriendId friendId,
                                                 final List<InFriendshipWithId> inFriendshipsWithId,
                                                 final Version version) {
        final StreamTransactionEntity tx = arangoDatabase.beginStreamTransaction(new StreamTransactionOptions()
                .writeCollections(FRIENDS, IN_FRIENDSHIP_WITH));
        try {
            final ArangoGraph arangoGraph = arangoDatabase.graph(FRIENDS);
            final BaseDocument newFriend = new BaseDocument(friendId.pseudo());
            newFriend.addAttribute("bio", "");
            newFriend.addAttribute("version", version.value());
            arangoGraph
                    .vertexCollection(FRIENDS)
                    .insertVertex(newFriend, new VertexCreateOptions().streamTransactionId(tx.getId()).waitForSync(true));
            inFriendshipsWithId.stream()
                    .forEach(inFriendshipWithId -> {
                        final String key = String.format("%s_%s", friendId.pseudo(), inFriendshipWithId.pseudo());
                        final String from = String.format("%s/%s", FRIENDS, friendId.pseudo());
                        final String to = String.format("%s/%s", FRIENDS, inFriendshipWithId.pseudo());
                        arangoGraph.edgeCollection(IN_FRIENDSHIP_WITH)
                                .insertEdge(
                                        new BaseEdgeDocument(key, from, to),
                                        new EdgeCreateOptions().streamTransactionId(tx.getId()).waitForSync(true));
                    });

            arangoDatabase.commitStreamTransaction(tx.getId());
        } catch (final ArangoDBException arangoDBException) {
            arangoDatabase.abortStreamTransaction(tx.getId());
            if (Integer.valueOf(1210).equals(arangoDBException.getErrorNum())) {
                LOG.warn(String.format("ERROR_ARANGO_UNIQUE_CONSTRAINT_VIOLATED has been raised for friendId '%s' and version '%d'." +
                                "It may happen if a message is replayed following a failure after registering this user.",
                        friendId.pseudo(), version));
            } else {
                throw arangoDBException;
            }
        }
    }

    @NewSpan
    public void writeBio(final FriendId friendId, final Bio bio, final Version version) {
        arangoDatabase.graph(FRIENDS)
                .vertexCollection(FRIENDS)
                .updateVertex(friendId.pseudo(), String.format("{ \"bio\": \"%s\", \"version\": %d }", bio.content(), version.value()),
                        new VertexUpdateOptions().waitForSync(true));
    }

    @NewSpan
    public void establishFriendshipWith(final FriendId friendId,
                                        final EstablishedFriendshipWith establishedFriendshipWith,
                                        final Version version) {
        final String key = String.format("%s_%s", friendId.pseudo(), establishedFriendshipWith.inFriendshipWithId().pseudo());
        final String from = String.format("%s/%s", FRIENDS, friendId.pseudo());
        final String to = String.format("%s/%s", FRIENDS, establishedFriendshipWith.inFriendshipWithId().pseudo());
        final StreamTransactionEntity tx = arangoDatabase.beginStreamTransaction(new StreamTransactionOptions()
                .writeCollections(FRIENDS, IN_FRIENDSHIP_WITH));
        try {
            final ArangoGraph arangoGraph = arangoDatabase.graph(FRIENDS);
            arangoGraph.vertexCollection(FRIENDS)
                    .updateVertex(friendId.pseudo(), String.format("{ \"version\": %d }", version.value()),
                            new VertexUpdateOptions().streamTransactionId(tx.getId()).waitForSync(true));
            arangoGraph.edgeCollection(IN_FRIENDSHIP_WITH)
                    .insertEdge(
                            new BaseEdgeDocument(key, from, to),
                            new EdgeCreateOptions().streamTransactionId(tx.getId()).waitForSync(true));
            arangoDatabase.commitStreamTransaction(tx.getId());
        } catch (final ArangoDBException arangoDBException) {
            arangoDatabase.abortStreamTransaction(tx.getId());
            if (Integer.valueOf(1210).equals(arangoDBException.getErrorNum())) {
                LOG.warn(String.format("ERROR_ARANGO_UNIQUE_CONSTRAINT_VIOLATED has been raised for friendId '%s' and version '%d'." +
                                "It may happen if a message is replayed following a failure after registering this user.",
                        friendId.pseudo(), version.value()));
            } else {
                throw arangoDBException;
            }
        }
    }

    public void onStart(@Observes final StartupEvent startupEvent) {
        if (!arangoDB.db(dbName).exists()) {
            arangoDB.createDatabase(dbName);
        }
        final ArangoDatabase arangoDatabase = arangoDB.db(dbName);
        if (!arangoDatabase.graph(FRIENDS).exists()) {
            final Collection<EdgeDefinition> edgeDefinitions = new ArrayList<>();
            final EdgeDefinition edgeDefinition = new EdgeDefinition()
                    .collection(IN_FRIENDSHIP_WITH)
                    .from(FRIENDS)
                    .to(FRIENDS);
            edgeDefinitions.add(edgeDefinition);
            arangoDatabase.createGraph(FRIENDS, edgeDefinitions, null);
        }
        this.arangoDatabase = arangoDB.db(dbName);
    }
}
