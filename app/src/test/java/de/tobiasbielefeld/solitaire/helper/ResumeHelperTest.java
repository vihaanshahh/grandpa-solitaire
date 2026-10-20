package de.tobiasbielefeld.solitaire.helper;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResumeHelperTest {

    private static final int NO_GAME = -1;
    private static final int GAME_COUNT = 20;
    private static final int KLONDIKE = 8;
    private static final int FREECELL = 4;

    private static ResumeHelper.InProgressCheck inProgress(Integer... games) {
        Set<Integer> set = new HashSet<>(Arrays.asList(games));
        return set::contains;
    }

    @Test
    public void isInProgress() {
        assertTrue(ResumeHelper.isInProgress(false, false));
        assertFalse(ResumeHelper.isInProgress(true, false));   // never started
        assertFalse(ResumeHelper.isInProgress(false, true));   // already won
    }

    @Test
    public void launchReopensGameThatWasOpen() {
        assertEquals(FREECELL, ResumeHelper.chooseGameOnLaunch(false, FREECELL, KLONDIKE, GAME_COUNT, NO_GAME, inProgress()));
    }

    @Test
    public void launchResumesLastPlayedGameAfterReturningToMenu() {
        assertEquals(KLONDIKE, ResumeHelper.chooseGameOnLaunch(false, NO_GAME, KLONDIKE, GAME_COUNT, NO_GAME, inProgress(KLONDIKE)));
    }

    @Test
    public void launchShowsMenuWhenLastGameIsFinished() {
        assertEquals(NO_GAME, ResumeHelper.chooseGameOnLaunch(false, NO_GAME, KLONDIKE, GAME_COUNT, NO_GAME, inProgress()));
    }

    @Test
    public void launchShowsMenuWhenNothingPlayedYet() {
        assertEquals(NO_GAME, ResumeHelper.chooseGameOnLaunch(false, NO_GAME, NO_GAME, GAME_COUNT, NO_GAME, inProgress(KLONDIKE)));
    }

    @Test
    public void startWithMenuSettingIsRespected() {
        assertEquals(NO_GAME, ResumeHelper.chooseGameOnLaunch(true, FREECELL, KLONDIKE, GAME_COUNT, NO_GAME, inProgress(KLONDIKE)));
    }

    @Test
    public void invalidIndexesAreIgnored() {
        assertEquals(NO_GAME, ResumeHelper.chooseGameOnLaunch(false, 99, 42, GAME_COUNT, NO_GAME, inProgress(42, 99)));
        assertEquals(NO_GAME, ResumeHelper.chooseGameToContinue(-5, GAME_COUNT, NO_GAME, inProgress(-5)));
    }

    @Test
    public void continueButton() {
        assertEquals(KLONDIKE, ResumeHelper.chooseGameToContinue(KLONDIKE, GAME_COUNT, NO_GAME, inProgress(KLONDIKE)));
        assertEquals(NO_GAME, ResumeHelper.chooseGameToContinue(KLONDIKE, GAME_COUNT, NO_GAME, inProgress(FREECELL)));
    }
}
