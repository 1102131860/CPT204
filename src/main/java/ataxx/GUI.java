package ataxx;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import ataxx.Game;
// Optional Task: The GUI for the Ataxx Game

class GUI extends JFrame implements View, CommandSource, Reporter{
    private BoardComponent boardComponent;

    /** Constructor */
    GUI(String ataxx) {
        // create BoardComponent object
        boardComponent = new BoardComponent();

        // set frame attributes
        setTitle("Ataxx Game");
        setSize(500, 500);
        setMinimumSize(new Dimension(600, 600)); // set Max and Min size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // add BoardComponent into frame
        add(boardComponent);

        // show the frame
        setVisible(true);
    }

    /** Update is called outside to update the GUI*/
    @Override
    public void update(Board board) {
        boardComponent.setBoard(board);
    }

    /** Get the command from the input dialog plan
     * @param prompt used to prompt what should input in the dialog plane
     * @return command that is a request to the game*/
    @Override
    public String getCommand(String prompt) {
        String command = JOptionPane.showInputDialog(null, prompt);
        return command;
    }

    /** Announce the winner at the end of game.
     * @Param state that indicates the result of the game */
    @Override
    public void announceWinner(PieceState state) {
        if (state == PieceState.EMPTY) {
            JOptionPane.showMessageDialog(null, "It's a tie!" );
        } else {
            JOptionPane.showMessageDialog(null, "Player " + state.name() + " wins!");
        }
    }


    /** Announce very move with the name of player
     * @param move contains the move information
     * @param player who took the action */
    @Override
    public void announceMove(Move move, PieceState player) {
        //JOptionPane.showMessageDialog(null, "Player " + player.name() + " moves " + move.toString());
    }


    /** Format the output */
    @Override
    public void message(String format, Object... args) {
        JOptionPane.showConfirmDialog(null, String.format(format, args));
    }


    /** Send out the error message through plane */
    @Override
    public void error(String format, Object... args) {
        JOptionPane.showMessageDialog(null, String.format(format, args),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Inheritance of setting visible*/
    public void setVisible(boolean b) {
        super.setVisible(b);

    }

    /** Inheritance of pack */
    public void pack() {
        super.pack();
    }

}
