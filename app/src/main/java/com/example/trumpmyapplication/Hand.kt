package com.example.trumpmyapplication

import android.util.Log

class Hand {
    private var handCards: MutableList<Card> = mutableListOf();
    private var handCardsFinal: List<Card> = listOf();
    private var handCardsMid: MutableList<Card> = mutableListOf();
    private var handCardsFinalMid: List<Card> = listOf();

    var deckInit = Deck();
    var hand: MutableList<Int> = mutableListOf();
    var handMid: MutableList<Int> = mutableListOf();
    private var role: String = "";
    private var roleMid: String = "";

    fun Kubaru(): MutableList<Int> {
        hand = DeckMgr(5);
        return hand;
    }

    fun Kubaru(isMid: Boolean): MutableList<Int> {
        handMid = DeckMgr(5);
        return handMid;
    }

    fun Koukan(koukanCards: MutableList<Int>): MutableList<Int> {
        var koukaned = DeckMgr(koukanCards.count());

        for(i in 0 until koukanCards.count()) {
            hand[koukanCards[i]] = koukaned[i];
        }

        return koukaned;
    }

    fun Kakutei(): String {
        handCards = mutableListOf();
        for(i in 0 until hand.count()) {
            var card = Card();
            var index = deckInit.cardImageIds.indexOf(hand[i]);

            card.mark = index / 13 + 1;
            card.num = index % 13 + 1;

            handCards.add(card);
        }
        handCardsFinal = listOf();
        handCardsFinal = handCards.toList();
        var roleMgrInit = RoleMgr(handCardsFinal);
        role = roleMgrInit.Role();
        return role;
    }

    fun Kakutei(isMid: Boolean): String {
        handCardsMid = mutableListOf();
        for(i in 0 until handMid.count()) {
            var card: Card = Card();
            var index: Int = deckInit.cardImageIds.indexOf(handMid[i]);

            card.mark = index / 13 + 1;
            card.num = index % 13 + 1;
            handCardsMid.add(card);
        }

        handCardsFinalMid = listOf();
        handCardsFinalMid = handCardsMid.toList();

        var roleMgrInit = RoleMgr(handCardsFinalMid);
        roleMid = roleMgrInit.Role();
        return roleMid;
    }

    fun DeckMgr(cardCount: Int): MutableList<Int> {

        if(deckInit.deck.count() < cardCount) {
            deckInit = Deck();
        }

        var deckToHand: MutableList<Int> = mutableListOf();

        for(i in 0 until cardCount) {
            deckToHand.add(deckInit.cardImageIds[deckInit.deck[0]]);
            //?????????????????????????????????????????????????????????0????????????????????????????????????0????????????
            deckInit.deck.removeAt(0);
        }

        return deckToHand;
    }

    fun Result(): String {
        val roleIndex: Int = deckInit.roleList.indexOf(role);
        val roleIndexMid: Int = deckInit.roleList.indexOf(roleMid);
        val winnerInTie = NumCheck(roleIndex, roleIndexMid);

        when {
            roleIndex > roleIndexMid || winnerInTie == 1 -> return "??????";
            roleIndex < roleIndexMid || winnerInTie == -1 -> return "??????";
            roleIndex == roleIndexMid && winnerInTie == 0 -> return "????????????";
        }

        return "??????????????????";
    }

    private fun NumCheck(roleIndex: Int, roleIndexMid: Int): Int {
        if(roleIndex != roleIndexMid || handCardsFinal.count() == 0 || handCardsFinalMid.count() == 0) {
            return 777;
        }
        var isWin = 0;//1 -> win; -1 -> lose; 0 -> tie
        var handNum: MutableList<Int> = mutableListOf();
        var handNumMid: MutableList<Int> = mutableListOf();

        for(i in 0 until 5) {
            handNum.add(handCardsFinal[i].num);
            handNumMid.add(handCardsFinalMid[i].num);
        }

        //ACE????????? 777???????????????
        for(i in 0 until handNum.count()) {
            if(handNum[i] == 1) {
                handNum[i] = 777;
            }
            if(handNumMid[i] == 1) {
                handNumMid[i] = 777;
            }
        }
        handNum.sort();
        handNumMid.sort();

        val role = deckInit.roleList[roleIndex];

        when(role) {
            "???????????????", "???????????????" -> {
                isWin = 0;
                for(i in 4 downTo 0) {
                    if(handNum[i] == handNumMid[i]) {
                        isWin = 0;
                    } else if(handNum[i] > handNumMid[i]) {
                        isWin = 1;
                        break;
                    } else if(handNum[i] < handNumMid[i]) {
                        isWin = -1;
                        break;
                    }
                }
            }

            "???????????????", "??????????????????????????????" -> {
                isWin = 0;
                if(handNum[4] > handNumMid[4]) {
                    isWin = 1;
                } else if(handNum[4] < handNumMid[4]) {
                    isWin = -1;
                } else if(handNum[4] == handNumMid[4]) {
                    isWin = 0;
                }
            }

            "????????????", "??????????????????", "??????????????????" -> {
                isWin = 0;
                var numCount = 1;
                var num = -1;
                for(i in 4 downTo 0) {
                    if(num == handNum[i]) {
                        numCount++;
                        continue;
                    }

                    if(numCount > 1) {
                        break;
                    }
                    num = handNum[i];
                }

                handNum.removeAll {
                    it == num;
                };
                handNum.add(num)

                num = -1;
                numCount = 1;
                for(i in 4 downTo 0) {
                    if(num == handNumMid[i]) {
                        numCount++;
                        continue;
                    }

                    if(numCount > 1) {
                        break;
                    }

                    num = handNumMid[i];
                }

                handNumMid.removeAll {
                    it == num;
                };
                handNumMid.add(num);

                handNumMid.forEach {
                    Log.d("-----", it.toString());
                }

                var numRepeat = handNum[handNum.count() - 1];
                var numRepeatMid = handNumMid[handNumMid.count() - 1];
                if(numRepeat > numRepeatMid) {
                    isWin = 1;
                    return isWin;
                } else if(numRepeat < numRepeatMid) {
                    isWin = -1;
                    return isWin;
                } else {
                    isWin = 0;
                }
                for(i in (handNum.count() - 2) downTo 0) {
                    if(handNum[i] == handNumMid[i]) {
                        isWin = 0;
                    } else if(handNum[i] > handNumMid[i]) {
                        isWin = 1;
                        break;
                    } else if(handNum[i] < handNumMid[i]) {
                        isWin = -1;
                        break;
                    }
                }
            }

            "????????????", "???????????????" -> {
                isWin = 0;
            }


            "???????????????????????????" -> {
                isWin = 0;
            }
        }

        return isWin;
    }


}