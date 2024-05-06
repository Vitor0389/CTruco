package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SlayerBotTest {

    List<GameIntel.RoundResult> roundResults;
    TrucoCard vira;
    List<TrucoCard> openCards;
    List<TrucoCard> cards;
    GameIntel.StepBuilder stepBuilder;

    @Test
    @DisplayName("If first to play, should not play zap on first round")
    void shouldNotPlayZapOnFirstRound(){

        roundResults = List.of();
        vira = TrucoCard.of(FOUR, HEARTS);
        cards = List.of(
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(FIVE, HEARTS));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertFalse(chosenCard.isZap(stepBuilder.build().getVira()));
    }

}
