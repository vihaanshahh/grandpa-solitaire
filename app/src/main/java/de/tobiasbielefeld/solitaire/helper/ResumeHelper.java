/*
 * Copyright (C) 2026 Vihaan Shah (grandpa-solitaire changes)
 *
 * This file is part of Simple Solitaire.
 *
 * Simple Solitaire is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Simple Solitaire is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.tobiasbielefeld.solitaire.helper;

/**
 * Pure (Android-free) decision logic for resuming games, so it can be unit tested.
 */
public final class ResumeHelper {

    public interface InProgressCheck {
        boolean isInProgress(int gameIndex);
    }

    private ResumeHelper() {
    }

    /**
     * A game has a resumable save if it has been started at least once (not first run)
     * and the saved game is not already won.
     */
    public static boolean isInProgress(boolean firstRun, boolean won) {
        return !firstRun && !won;
    }

    static boolean isValidGame(int gameIndex, int gameCount) {
        return gameIndex >= 0 && gameIndex < gameCount;
    }

    /**
     * Decide which game to open directly when the app is launched.
     *
     * @param startWithMenu    user setting "always start with the main menu"
     * @param savedCurrentGame game that was open when the app was closed (upstream behaviour)
     * @param lastPlayedGame   last game the user played, kept even after returning to the menu
     * @param gameCount        number of games
     * @param noGame           value meaning "show the menu"
     * @param check            tells whether a game has an in-progress save
     * @return the game index to open, or noGame to show the menu
     */
    public static int chooseGameOnLaunch(boolean startWithMenu, int savedCurrentGame, int lastPlayedGame,
                                         int gameCount, int noGame, InProgressCheck check) {
        if (startWithMenu) {
            return noGame;
        }

        if (savedCurrentGame != noGame && isValidGame(savedCurrentGame, gameCount)) {
            return savedCurrentGame;
        }

        return chooseGameToContinue(lastPlayedGame, gameCount, noGame, check);
    }

    /**
     * Decide which game the "Continue" button on the main menu should open.
     *
     * @return the game index, or noGame if there is nothing to continue
     */
    public static int chooseGameToContinue(int lastPlayedGame, int gameCount, int noGame, InProgressCheck check) {
        if (lastPlayedGame != noGame && isValidGame(lastPlayedGame, gameCount) && check.isInProgress(lastPlayedGame)) {
            return lastPlayedGame;
        }

        return noGame;
    }
}
