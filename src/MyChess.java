import ChessUtils.ChessUtils;
import Pieces.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import ChessUtils.Board;

public class MyChess {
    public static LinkedList<Piece> piecesOnBoard = new LinkedList<>();
    public static Piece selectedPiece = null;

    public static void main(String[] args) throws IOException {
        final int SQUARE_WIDTH = 80; // in pixels
        final int PADDING = 50; // padding left, right, and above the board (not including the grey border)

        // getting images
        Image[] imgs = new Image[12];
        BufferedImage all= ImageIO.read(new File("/Users/Eddie/Downloads/MyChess/chess.png"));
        int id = 0;
        for(int y=0; y<400; y+=200){
            for(int x=0; x<1200; x+=200){
                imgs[id] = all.getSubimage(x, y, 200, 200).getScaledInstance(80, 80, BufferedImage.SCALE_SMOOTH);
                id++;
            }
        }

        // setting up the chessboard (starting squares of each piece)
        Pawn whitePawn1 = new Pawn(0, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn2 = new Pawn(1, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn3 = new Pawn(2, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn4 = new Pawn(3, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn5 = new Pawn(4, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn6 = new Pawn(5, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn7 = new Pawn(6, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn8 = new Pawn(7, 6, Alliance.WHITE, piecesOnBoard);
        Rook whiteRook1 = new Rook(0, 7, Alliance.WHITE, piecesOnBoard);
        Rook whiteRook2 = new Rook(7, 7, Alliance.WHITE, piecesOnBoard);
        Knight whiteKnight1 = new Knight(1, 7, Alliance.WHITE, piecesOnBoard);
        Knight whiteKnight2 = new Knight(6, 7, Alliance.WHITE, piecesOnBoard);
        Bishop whiteBishop1 = new Bishop(2, 7, Alliance.WHITE, piecesOnBoard);
        Bishop whiteBishop2 = new Bishop(5, 7, Alliance.WHITE, piecesOnBoard);
        King whiteKing = new King(4, 7, Alliance.WHITE, piecesOnBoard);
        Queen whiteQueen = new Queen(3, 7, Alliance.WHITE, piecesOnBoard);

        Pawn blackPawn1 = new Pawn(0, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn2 = new Pawn(1, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn3 = new Pawn(2, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn4 = new Pawn(3, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn5 = new Pawn(4, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn6 = new Pawn(5, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn7 = new Pawn(6, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn8 = new Pawn(7, 1, Alliance.BLACK, piecesOnBoard);
        Rook blackRook1 = new Rook(0, 0, Alliance.BLACK, piecesOnBoard);
        Rook blackRook2 = new Rook(7, 0, Alliance.BLACK, piecesOnBoard);
        Knight blackKnight1 = new Knight(1, 0, Alliance.BLACK, piecesOnBoard);
        Knight blackKnight2 = new Knight(6, 0, Alliance.BLACK, piecesOnBoard);
        Bishop blackBishop1 = new Bishop(2, 0, Alliance.BLACK, piecesOnBoard);
        Bishop blackBishop2 = new Bishop(5, 0, Alliance.BLACK, piecesOnBoard);
        King blackKing = new King(4, 0, Alliance.BLACK, piecesOnBoard);
        Queen blackQueen = new Queen(3, 0, Alliance.BLACK, piecesOnBoard);

        for(Piece p : piecesOnBoard)
            p.calculateLegalMoves();

        Board chessBoard = new Board();

        JFrame window = new JFrame();
        window.setResizable(false);

        JLabel l = new JLabel();
        l.setText("Two Player Chess");
        l.setForeground(new Color(105, 105, 105));
        l.setFont(new Font("Sans-Serif", Font.BOLD, 24));
        l.setBounds(280, 735, 400, 30);
        l.setHorizontalAlignment( SwingConstants.RIGHT );
        System.out.println("GAME BEGINS NOW");

        JButton resetBtn = new JButton("Reset");
        resetBtn.setFont(new Font("Sans-Serif", Font.BOLD, 15));
        resetBtn.setForeground(new Color(255, 255,255));
        resetBtn.setBorderPainted(false);
        resetBtn.setFocusPainted(false);
        resetBtn.setContentAreaFilled(false);
        resetBtn.setOpaque(true);
        resetBtn.setBackground(new Color(	65, 105, 225));
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
                l.setForeground(new Color(90, 90, 90));
                l.setText("New Game!");
                window.repaint();
            }
        });
        resetBtn.setBounds(50,730,100,40);

        window.add(resetBtn);
        window.setBounds(50, 50, SQUARE_WIDTH*8 + 15 + PADDING*2, SQUARE_WIDTH*8 + 85 + PADDING*2);
        window.add(l);
        JPanel boardDisplay = new JPanel() {
            @Override
            public void paint(Graphics g) {

                // draw border around chess board
                g.setColor(new Color(205,133,50));
                g.fillRect(PADDING-20, PADDING-20, SQUARE_WIDTH*8 + 20*2, SQUARE_WIDTH*8 + 20*2);

                // draw tiles
                boolean isWhite = ChessUtils.isWhiteToMove();
                for(int i=0; i<8; i++){
                    for(int j=0; j<8; j++){
                        if(isWhite)
                        g.setColor(new Color(238, 238, 210));
                        else
                            g.setColor(new Color(118, 150, 86));
                        g.fillRect(i*SQUARE_WIDTH + 50, j*SQUARE_WIDTH + 50, SQUARE_WIDTH, SQUARE_WIDTH);
                        isWhite = !isWhite;
                    }
                    isWhite = !isWhite;
                }

                // draw pieces
                for(Piece piece : piecesOnBoard) {
                    int id = 0;
                    if(piece.getClass().equals(King.class))
                        id = 0;
                    if(piece.getClass().equals(Queen.class))
                        id = 1;
                    if(piece.getClass().equals(Bishop.class))
                        id = 2;
                    if(piece.getClass().equals(Knight.class))
                        id = 3;
                    if(piece.getClass().equals(Rook.class))
                        id = 4;
                    if(piece.getClass().equals(Pawn.class))
                        id = 5;
                    if(piece.getAlliance() == Alliance.BLACK)
                        id += 6;
                    g.drawImage(imgs[id], piece.getX(), piece.getY(), null);
                }
            }
        };
        window.add(boardDisplay);
        window.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(selectedPiece != null) {
                    selectedPiece.setX(e.getX()-40);
                    selectedPiece.setY(e.getY()-75);
                }
                window.repaint();
            }
            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        window.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int xpos = (e.getX() - 50) / 80;
                int ypos = e.getY() / 80 - 1;
                selectedPiece = ChessUtils.getPiece(xpos, ypos, piecesOnBoard);
                System.out.println("selected " + selectedPiece + "at " + xpos + "," + ypos);
                System.out.println("alliance: " + selectedPiece.getAlliance());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int xpos = (e.getX()-PADDING) / SQUARE_WIDTH;
                int ypos = (e.getY()-PADDING - 30)/ SQUARE_WIDTH;
                boolean isLegalMove = selectedPiece.isLegalMove(xpos, ypos, selectedPiece.getAlliance());
                boolean isCorrectColor = selectedPiece.isCorrectColor();

                // if check occurs, check must be blocked - checks legality inside handlesCheck()
                if(isCorrectColor && whiteKing.isChecked() && selectedPiece != whiteKing && selectedPiece.handlesCheck(xpos, ypos)) {
                    selectedPiece.move(xpos, ypos, SQUARE_WIDTH);
                    ChessUtils.changeTurns();
                }
                // or king must move out of check
                else if (whiteKing.isChecked() && selectedPiece == whiteKing && isLegalMove){
                    selectedPiece.move(xpos, ypos, SQUARE_WIDTH);
                    whiteKing.isChecked = false;
                    ChessUtils.changeTurns();
                }
                // if not checked, dont need to check if it handles check
                 else if(!whiteKing.isChecked() && selectedPiece != null && isCorrectColor && isLegalMove) {
                    selectedPiece.move(xpos, ypos, SQUARE_WIDTH);
                    ChessUtils.changeTurns();
                }

                //    BLACK

                // if check occurs, check must be blocked
                else if(isCorrectColor && blackKing.isChecked() && selectedPiece != blackKing && selectedPiece.handlesCheck(xpos, ypos)) {
                    selectedPiece.move(xpos, ypos, SQUARE_WIDTH);
                    ChessUtils.changeTurns();
                }
                // or king must move out of check
                else if (blackKing.isChecked() && selectedPiece == blackKing && isLegalMove){
                    selectedPiece.move(xpos, ypos, SQUARE_WIDTH);
                    blackKing.isChecked = false;
                    ChessUtils.changeTurns();
                }
                // if not checked, dont need to check if it handles check
                else if(!blackKing.isChecked() && selectedPiece != null && isCorrectColor && isLegalMove) {
                    selectedPiece.move(xpos, ypos, SQUARE_WIDTH);
                    ChessUtils.changeTurns();
                }

                // if the move is invalid, return it to its original square
                else if (selectedPiece != null){
                    selectedPiece.setX(selectedPiece.getXpos() * 80 + PADDING);
                    selectedPiece.setY(selectedPiece.getYpos() * 80 + PADDING);
                }

                window.repaint();

                // decides what to do after checkmate
                if(whiteKing.isChecked && whiteKing.isCheckmate()){
                    l.setForeground(new Color(39, 139, 34));
                    l.setText("Checkmate by Black!");
                    System.out.println("CHECKMATE BLACK WINS");
                }
                else if(blackKing.isChecked && blackKing.isCheckmate()) {
                    l.setForeground(new Color(39, 139, 34));
                    l.setText("Checkmate by White!");
                    System.out.println("CHECKMATE WHITE WINS");
                } else if(whiteKing.isChecked || blackKing.isChecked) {
                    l.setForeground(new Color(255, 0, 0));
                    l.setText("Check!");
                }  else if(ChessUtils.whiteToMove) {
                    l.setForeground(new Color(90, 90, 90));
                    l.setText("White to move");
                } else {
                    l.setForeground(new Color(90, 90, 90));
                    l.setText("Black to move");
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        window.setDefaultCloseOperation(3);
        window.setVisible(true);
    }

    public static void resetBoard() {
        piecesOnBoard.removeIf(piece -> piece.getClass() != King.class);
        for(Piece p : piecesOnBoard) {
            if(p.getAlliance() == Alliance.WHITE) {
                p.setXpos(4);
                p.setYpos(7);
                p.setX(4*80+50);
                p.setY(7*80+50);
                ((King) p).isChecked = false;
                p.isFirstMove = true;
            } else {
                p.setXpos(4);
                p.setYpos(0);
                p.setX(4*80+50);
                p.setY(0*80+50);
                ((King) p).isChecked = false;
                p.isFirstMove = true;
            }
        }

        // setting up the chessboard (starting squares of each piece) except kings
        Pawn whitePawn1 = new Pawn(0, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn2 = new Pawn(1, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn3 = new Pawn(2, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn4 = new Pawn(3, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn5 = new Pawn(4, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn6 = new Pawn(5, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn7 = new Pawn(6, 6, Alliance.WHITE, piecesOnBoard);
        Pawn whitePawn8 = new Pawn(7, 6, Alliance.WHITE, piecesOnBoard);
        Rook whiteRook1 = new Rook(0, 7, Alliance.WHITE, piecesOnBoard);
        Rook whiteRook2 = new Rook(7, 7, Alliance.WHITE, piecesOnBoard);
        Knight whiteKnight1 = new Knight(1, 7, Alliance.WHITE, piecesOnBoard);
        Knight whiteKnight2 = new Knight(6, 7, Alliance.WHITE, piecesOnBoard);
        Bishop whiteBishop1 = new Bishop(2, 7, Alliance.WHITE, piecesOnBoard);
        Bishop whiteBishop2 = new Bishop(5, 7, Alliance.WHITE, piecesOnBoard);
        Queen whiteQueen = new Queen(3, 7, Alliance.WHITE, piecesOnBoard);

        Pawn blackPawn1 = new Pawn(0, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn2 = new Pawn(1, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn3 = new Pawn(2, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn4 = new Pawn(3, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn5 = new Pawn(4, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn6 = new Pawn(5, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn7 = new Pawn(6, 1, Alliance.BLACK, piecesOnBoard);
        Pawn blackPawn8 = new Pawn(7, 1, Alliance.BLACK, piecesOnBoard);
        Rook blackRook1 = new Rook(0, 0, Alliance.BLACK, piecesOnBoard);
        Rook blackRook2 = new Rook(7, 0, Alliance.BLACK, piecesOnBoard);
        Knight blackKnight1 = new Knight(1, 0, Alliance.BLACK, piecesOnBoard);
        Knight blackKnight2 = new Knight(6, 0, Alliance.BLACK, piecesOnBoard);
        Bishop blackBishop1 = new Bishop(2, 0, Alliance.BLACK, piecesOnBoard);
        Bishop blackBishop2 = new Bishop(5, 0, Alliance.BLACK, piecesOnBoard);
        Queen blackQueen = new Queen(3, 0, Alliance.BLACK, piecesOnBoard);

        ChessUtils.whiteToMove = true;
    }
}