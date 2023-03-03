package service;

import domain.PlayerGameResult;
import domain.card.Deck;
import domain.participant.Participant;
import domain.participant.Participants;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlackjackService {

    private static final String HIT = "y";
    private static final String STAND = "n";
    public static final String ERROR_HIT_OPERATION = "[ERROR] y 또는 n만 입력할 수 있습니다";

    private final Deck deck;
    private final Participants participants;

    private BlackjackService(Deck deck, Participants participants) {
        this.deck = deck;
        this.participants = participants;
    }

    public static BlackjackService of(List<String> playersName) {
        return new BlackjackService(Deck.create(), Participants.of(playersName));
    }

    public void start() {
        participants.initHand(deck);
    }

    public boolean existNextPlayerTurn() {
        return participants.getNextTurnPlayer().isPresent();
    }

    public Participant getNextPlayer() {
        return participants.getNextTurnPlayer().get();
    }

    public void nextTurn(String hit) {
        Participant nextPlayer = participants.getNextTurnPlayer().get();

        if (hit.equals(STAND)) {
            nextPlayer.stand();
            return;
        }

        if (hit.equals(HIT)) {
            nextPlayer.addCard(deck.pollAvailableCard());
        }

        throw new IllegalArgumentException(ERROR_HIT_OPERATION);
    }

    public void dealerTurn() {
        participants.playDealerTurn(deck);
    }

    public boolean isDealerStand() {
        return participants.isDealerStand();
    }

    public List<String> getPlayersName() {
        return participants.getPlayers().stream()
                .map(Participant::getName)
                .collect(Collectors.toList());
    }

    public Map<String, PlayerGameResult> getResult() {
        return participants.getResult();
    }

    public Participant getDealer() {
        return participants.getDealer();
    }

    public List<Participant> getPlayers() {
        return participants.getPlayers();
    }
}
