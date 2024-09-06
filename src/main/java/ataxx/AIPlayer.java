package ataxx;

import java.util.ArrayList;

import static ataxx.PieceState.BLUE;
import static ataxx.PieceState.RED;

// Final Project Part A.2 Ataxx AI Player (A group project)

/** A Player that computes its own moves. */
class AIPlayer extends Player {
    private PieceState myColor;
    private Move lastFoundMove;


    /** A new AIPlayer for GAME that will play MYCOLOR.
     *  SEED is used to initialize a random-number generator,
     *  increase the value of SEED would make the AIPlayer move automatically.
     *  Identical seeds produce identical behaviour.
     */
    AIPlayer(Game game, PieceState myColor, long seed) {
        super(game, myColor);
        this.myColor = myColor;;
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getAtaxxMove() {
        Move move = findMove();
        getAtaxxGame().reportMove(move, getMyState());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there is a move. */
    public Move findMove() {
        Board b = new Board(getAtaxxBoard());
        lastFoundMove = null;

        // Get all possible moves
        ArrayList<Move> listOfMoves = possibleMoves(b, b.nextMove());

        // Evaluate each possible move and select the best one using alpha-beta pruning
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        int maxDepth = 1;
        long startTime = System.currentTimeMillis();

        // Firstly explores the depth of our research tree in first 0.5 s
        while (System.currentTimeMillis() - startTime < 500) {
            double bestScore = Double.NEGATIVE_INFINITY;
            Move bestMove = null;

            for (Move move : listOfMoves) {
                Board newBoard = new Board(b);
                newBoard.createMove(move);
                double score = alphaBetaMinimax(newBoard, alpha, beta, maxDepth-1, false);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }

            lastFoundMove = bestMove;
            maxDepth++;         // Increase depth by 1

            // Check if time is up
            if (System.currentTimeMillis() - startTime >= 2500) {
                break;
            }
        }

        // If it still can move, it cannot pass
        if (lastFoundMove == null && b.couldMove(b.nextMove())){
            int index = (int)(Math.random()*listOfMoves.size());
            lastFoundMove = listOfMoves.get(index);
        }

        // Please do not change the codes below
        if (lastFoundMove == null) {
            lastFoundMove = Move.pass();
        }
        return lastFoundMove;

    }


    /** MinMax algorithm with alpha beta pruning method
     * @params b is the current board with PieceState nextMove
     * @param alpha is the initial/known minimum value
     * @param beta is the initial/known maximum value
     * @param depth is the depth of research tree
     * @param isMax is true if this move seeks max value,
     *             otherwise it seeks for min value
     * @return value that is max or min score that move can get */
    private double alphaBetaMinimax(Board b, double alpha, double beta, int depth, boolean isMax) {
        if ((depth == 0) || b.getWinner()!=null) {
            return evaluateBoard(b);
        }

        // This state seeks for max value
        if (isMax) {
            double maxScore = Double.NEGATIVE_INFINITY;
            for (Move move : possibleMoves(b, b.nextMove())) {
                Board newBoard = new Board(b);
                newBoard.createMove(move);
                maxScore = Math.max(maxScore, alphaBetaMinimax(newBoard, alpha, beta, depth - 1, false));
                // Update the current max value of its parent move
                alpha = Math.max(alpha, maxScore);
                // If min is larger than max, stop researching
                if (beta <= alpha) {
                    break;
                }
            }
            // After iterating, return the max value
            return maxScore;
        }
        // This state seeks for min value
        else {
            double minScore = Double.POSITIVE_INFINITY;
            for (Move move : possibleMoves(b, b.nextMove())) {
                Board newBoard = new Board(b);
                newBoard.createMove(move);
                minScore = Math.min(minScore, alphaBetaMinimax(newBoard, alpha, beta, depth-1, true));
                // Update the current min value of its parent move
                beta = Math.min(beta, minScore);
                // If min is larger than max, stop researching
                if (beta <= alpha) {
                    break;
                }
            }
            // After iterating, retun the min value
            return minScore;
        }
    }

    /** Evaluate the total score of AI player by counting the
     * number of piece state of the same color with AI player.
     * The basic score is based on the difference of its color
     * and its opponent's color.
     * The extra score is the possible highest score it can
     * get from the remaining board.
     * @param b the current board needed evaluating
     * @return the totalScore that equals the basic score plus extra score */
    private double evaluateBoard(Board b) {
        int numMyPieces = b.getColorNums(myColor);
        int numOpponentPieces = b.getColorNums(myColor.opposite());

        // Basic score
        double score = numMyPieces - numOpponentPieces;

        // Get all possible moves
        ArrayList<Move> listOfMoves = possibleMoves(b, b.nextMove());

        // If there are no moves available, return the basic score
        if (listOfMoves.isEmpty()) {
            return score;
        }

        // Evaluate each move and select the one with the highest score
        // and highest score is the extra score
        double bestMoveScore = Double.NEGATIVE_INFINITY;
        for (Move move : listOfMoves) {
            Board newBoard = new Board(b);
            newBoard.createMove(move);

            int numMyPiecesAfterMove = newBoard.getColorNums(myColor);
            int numOpponentPiecesAfterMove = newBoard.getColorNums(myColor.opposite());
            double moveScore = numMyPiecesAfterMove - numOpponentPiecesAfterMove;

            if (moveScore > bestMoveScore) {
                bestMoveScore = moveScore;
            }
        }

        // Add the score of the best move to get the total score
        score += bestMoveScore;

        // Return the total score
        return score;

    }


    /** The move found by the last call to the findMove method above. */

    /** Return all possible moves for a color.
     * @param board the current board.
     * @param myColor the specified color.
     * @return an ArrayList of all possible moves for the specified color. */
    private ArrayList<Move> possibleMoves(Board board, PieceState myColor) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (char row = '7'; row >= '1'; row--) {
            for (char col = 'a'; col <= 'g'; col++) {
                int index = Board.index(col, row);
                if (board.getContent(index) == myColor) {
                    ArrayList<Move> addMoves
                            = assistPossibleMoves(board, row, col);
                    possibleMoves.addAll(addMoves);
                }
            }
        }
        return possibleMoves;
    }

    /** Returns an Arraylist of legal moves.
     * @param board the board for testing
     * @param row the row coordinate of the center
     * @param col the col coordinate of the center */
    private ArrayList<Move>
    assistPossibleMoves(Board board, char row, char col) {
        ArrayList<Move> assistPossibleMoves = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    Move currMove = Move.move(col, row, col2, row2);
                    if (board.moveLegal(currMove)) {
                        assistPossibleMoves.add(currMove);
                    }
                }
            }
        }
        return assistPossibleMoves;
    }
}
