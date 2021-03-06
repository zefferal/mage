/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.zendikar;

import java.util.UUID;

import mage.constants.CardType;
import mage.constants.Rarity;
import mage.abilities.Ability;
import mage.abilities.condition.Condition;
import mage.abilities.costs.AlternativeCostSourceAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.GainLifeEffect;
import mage.abilities.effects.common.LoseLifeTargetEffect;
import mage.cards.CardImpl;
import mage.game.Game;
import mage.target.TargetPlayer;
import mage.watchers.common.PlayerGainedLifeWatcher;

/**
 *
 * @author LevelX2
 */
public class NeedlebiteTrap extends CardImpl {

    public NeedlebiteTrap(UUID ownerId) {
        super(ownerId, 105, "Needlebite Trap", Rarity.UNCOMMON, new CardType[]{CardType.INSTANT}, "{5}{B}{B}");
        this.expansionSetCode = "ZEN";
        this.subtype.add("Trap");

        // If an opponent gained life this turn, you may pay {B} rather than pay Needlebite Trap's mana cost.
        this.addAbility(new AlternativeCostSourceAbility(new ManaCostsImpl("{B}"), NeedlebiteTrapCondition.getInstance()), new PlayerGainedLifeWatcher());

        // Target player loses 5 life and you gain 5 life.
        this.getSpellAbility().addEffect(new LoseLifeTargetEffect(5));
        this.getSpellAbility().addEffect(new GainLifeEffect(5));
        this.getSpellAbility().addTarget(new TargetPlayer());
    }

    public NeedlebiteTrap(final NeedlebiteTrap card) {
        super(card);
    }

    @Override
    public NeedlebiteTrap copy() {
        return new NeedlebiteTrap(this);
    }
}

class NeedlebiteTrapCondition implements Condition {

    private static final NeedlebiteTrapCondition fInstance = new NeedlebiteTrapCondition();

    public static Condition getInstance() {
        return fInstance;
    }

    @Override
    public boolean apply(Game game, Ability source) {
        PlayerGainedLifeWatcher watcher = (PlayerGainedLifeWatcher) game.getState().getWatchers().get("PlayerGainedLifeWatcher");
        if (watcher != null) {
            for (UUID opponentId : game.getOpponents(source.getControllerId())) {
                if (watcher.getLiveGained(opponentId) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "If an opponent gained life this turn";
    }
}
