package ChessUtils;

import Pieces.Piece;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Board {
    public void switchBoard(LinkedList<Piece> piecesOnBoard) {
        for(Piece p : piecesOnBoard) {
            int xpos = p.getXpos();
            int ypos = p.getYpos();
            // change sides
            p.setXpos(xpos);
            p.setYpos(7-ypos);
            // update graphics coordinate
            p.setY(p.getYpos()*80 + 50);
            p.setX(p.getXpos()*80 + 50);
        }
    }
}
