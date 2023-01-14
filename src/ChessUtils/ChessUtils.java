package ChessUtils;
import Classes.Square;
import Pieces.Piece;
import java.util.LinkedList;

public class ChessUtils {
    public static boolean whiteToMove = true;
    // get piece using two coords
    public static Piece getPiece(int xpos, int ypos, LinkedList<Piece> piecesOnBoard) {
        for(Piece piece : piecesOnBoard) {
            if(piece.getXpos() == xpos && piece.getYpos() == ypos) {
                return piece;
            }
        }
        return null; // no piece on that square
    }
    // get piece by passing in a square as an arg
    public static Piece getPiece(Square s, LinkedList<Piece> piecesOnBoard) {
        int xpos = s.getXcoord();
        int ypos = s.getYcoord();

        for(Piece piece : piecesOnBoard) {
            if(piece.getXpos() == xpos && piece.getYpos() == ypos) {
                return piece;
            }
        }
        return null; // no piece on that square
    }
    public static void changeTurns() {
        whiteToMove = !whiteToMove;
        System.out.println("\nwhite to move: " + whiteToMove + "\n");
    }
    public static boolean isWhiteToMove() {
        return whiteToMove;
    }
}