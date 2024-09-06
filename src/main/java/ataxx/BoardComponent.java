package ataxx;

import javax.swing.*;
import java.awt.*;


public class BoardComponent extends JComponent {
    private final int gridSize = 7;
    private static final int SQUARE_SIZE = 70;
    private int tileSize;
    private Board board;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

       /** Set the board size */
        int BOARD_SIZE = 490;

        /** Calculate grid/tile size */
        tileSize = BOARD_SIZE / gridSize;

        /** Draw background */
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, BOARD_SIZE, BOARD_SIZE);

        /** Draw 8 x 2 lines */
        g.setColor(Color.BLACK);
        for (int i = 0; i < gridSize + 1; i++) {
            int x = i * tileSize;
            g.drawLine(x, 0, x, BOARD_SIZE);
            g.drawLine(0, x, BOARD_SIZE, x);
        }

        /** Draw chess */
        for (char row = '1'; row <= '7'; row++) {
            for (char col = 'a'; col <= 'g'; col++) {
                PieceState state = board.getContent(col,row);
                int x = (col - 'a') * tileSize;
                int y = (7 - (row -'0')) * tileSize;
                drawPiece(g,x, y, state);
            }
        }

        /** Draw position string */
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int x = i * tileSize + tileSize / 2;
                int y = j * tileSize + tileSize / 2;
                String coord = "" + (char) ('a' + i) + (gridSize - j);
                int coordWidth = fm.stringWidth(coord);
                int coordHeight = fm.getHeight();
                g.drawString(coord, x - coordWidth / 2, y + coordHeight / 2);
            }
        }
    }
    /** Draw piece of a Rectangular for BLOCKED grid or Oval for BLUE & GRD grid */
    public void drawPiece(Graphics g, int x, int y, PieceState state) {
        if (state == PieceState.EMPTY) {
            return;
        } else if (state == PieceState.BLOCKED) {
            g.setColor(Color.BLACK);
            g.fillRect(x + tileSize / 2 - 15, y + tileSize / 2 - 15, 30, 30);
        } else {
            Color color = state == PieceState.RED ? Color.RED : Color.BLUE;
            g.setColor(color);
            g.fillOval(x + tileSize / 2 - 15, y + tileSize / 2 - 15, 30, 30);
        }
    }


    /** Update the Board */
    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }
}
