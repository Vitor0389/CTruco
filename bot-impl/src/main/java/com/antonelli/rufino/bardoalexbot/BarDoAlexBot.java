package com.antonelli.rufino.bardoalexbot;

import com.bueno.spi.model.*;
import java.util.List;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class BarDoAlexBot {

    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        long manilhasCount = cards.stream().filter(card -> card.isManilha(vira)).count();
        return manilhasCount >= 3;
    }

}