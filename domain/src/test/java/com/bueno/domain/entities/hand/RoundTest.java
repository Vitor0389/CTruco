/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.domain.entities.hand;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoundTest {

    @Mock
    private Player p1;
    @Mock
    private Player p2;

    @BeforeEach
     void setUp(){
        Logger.getLogger(Round.class.getName()).setLevel(Level.OFF);
    }

    @Test
    @DisplayName("Should the player of the highest card be the winner")
    void shouldThePlayerOfTheHighestCardBeTheWinner() {
        final Card card1 = Card.of(Rank.FOUR, Suit.SPADES);
        final Card card2 = Card.of(Rank.FOUR, Suit.CLUBS);
        final Card vira = Card.of(Rank.THREE, Suit.SPADES);
        var round = new Round(p1, card1, p2, card2, vira);
        round.play();
        assertEquals(p2, round.getWinner().orElseThrow());
    }

    @Test
    @DisplayName("Should the player of the lowest card be the loser")
    void shouldThePlayerOfTheLowestCardBeTheLoser() {
        final Card card1 = Card.of(Rank.ACE, Suit.SPADES);
        final Card card2 = Card.of(Rank.TWO, Suit.CLUBS);
        final Card vira = Card.of(Rank.THREE, Suit.SPADES);
        var round = new Round(p1, card1, p2, card2, vira);
        round.play();
        assertNotEquals(p1, round.getWinner().orElseThrow());
    }

    @Test
    @DisplayName("Should have no winner if players play cards of equal value")
    void shouldHaveNoWinnerIfPlayersPlayCardsOfEqualValue() {
        final Card card1 = Card.of(Rank.FOUR, Suit.SPADES);
        final Card card2 = Card.of(Rank.FOUR, Suit.CLUBS);
        final Card vira = Card.of(Rank.ACE, Suit.SPADES);
        var round = new Round(p1, card1, p2, card2, vira);
        round.play();
        assertTrue(round.getWinner().isEmpty());
    }


    @Test
    @DisplayName("Should throw if any constructor parameter is null")
    void shouldThrowIfConstructorParameterIsNull() {
        final Card card1 = Card.of(Rank.FOUR, Suit.SPADES);
        final Card card2 = Card.of(Rank.FOUR, Suit.CLUBS);
        final Card vira = Card.of(Rank.THREE, Suit.SPADES);
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new Round(null, card1, p2, card2, vira)),
                () -> assertThrows(NullPointerException.class, () -> new Round(p1, null, p2, card2, vira)),
                () -> assertThrows(NullPointerException.class, () -> new Round(p1, card1, null, card2, vira)),
                () -> assertThrows(NullPointerException.class, () -> new Round(p1, card1, p2, null, vira)),
                () -> assertThrows(NullPointerException.class, () -> new Round(p1, card1, p2, card2, null))
        );
    }

    @ParameterizedTest
    @CsvSource({"FOUR,FOUR,FIVE", "FOUR,FIVE,FOUR", "FIVE,FOUR,FOUR"})
    @DisplayName("Should throw if round has duplicated cards")
    void shouldThrowIfRoundHasDuplicatedCard(Rank card1Rank, Rank card2Rank, Rank viraRank) {
        final Card card1 = Card.of(card1Rank, Suit.SPADES);
        final Card card2 = Card.of(card2Rank, Suit.SPADES);
        final Card vira = Card.of(viraRank, Suit.SPADES);
        assertThrows(GameRuleViolationException.class, () -> new Round(p1,card1, p2, card2, vira));
    }

    @Test
    @DisplayName("Should correctly toString")
    void shouldCorrectlyToString() {
        when(p1.getUsername()).thenReturn("p1");
        when(p2.getUsername()).thenReturn("p2");
        final Card card1 = Card.of(Rank.FOUR, Suit.SPADES);
        final Card card2 = Card.of(Rank.FOUR, Suit.CLUBS);

        final Round roundA = new Round(p1, card1, p2, card2, Card.of(Rank.FOUR, Suit.DIAMONDS));
        roundA.play();

        final Round roundB = new Round(p1, card1, p2, card2, Card.of(Rank.THREE, Suit.DIAMONDS));
        roundB.play();

        final Round roundC = new Round(p1, card2, p2, card1, Card.of(Rank.THREE, Suit.DIAMONDS));
        roundC.play();

        final Round nonPlayedRound = new Round(p1, card1, p2, card2, Card.of(Rank.THREE, Suit.DIAMONDS));

        assertAll(
                () -> assertEquals("Round = [4\u2660] x [4\u2663] (Vira [4\u2666]) - Result: Draw (--)", roundA.toString()),
                () -> assertEquals("Round = [4\u2660] x [4\u2663] (Vira [3\u2666]) - Result: p2 wins ([4\u2663])", roundB.toString()),
                () -> assertEquals("Round = [4\u2663] x [4\u2660] (Vira [3\u2666]) - Result: p1 wins ([4\u2663])", roundC.toString()),
                () -> assertEquals("Round = [4\u2660] x [4\u2663] (Vira [3\u2666]) - Result: Draw (--)", nonPlayedRound.toString())
        );
    }

}