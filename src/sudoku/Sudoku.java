//Matthew Chiarella | CS - 431

/* TERMINOLOGY DEFINITIONS ----------------------------------------------------
FRAME       :The visible portion of the project
BOARD       :The collection of the VALUES
CELL(S)     :The place where a value can go (Either on the FRAME or the BOARD)
VALUE(S)    :The integer value from {1,2,3,4,5,6,7,8,9}
LEGAL       :Valid placement of a VALUE
WINDOW      :Where the frame can be seen (self explanitory)
HOLE(S)     :A blank place that must be solved for
LABEL(S)    :A visible square of the FRAME on the WINDOW
ROW(S)      :Horizontal collection of CELLS 
COLUMN(S)   :Vertical collection of CELLS
------------------------------------------------------------------------------*/
package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Sudoku extends JFrame {
    //Below are the global variables
    public static JTextField[][] txt = new JTextField[9][9];
    public static int[][] temp = new int[9][9]; 
    public static final int BOARD_X = 9;
    public static final int BOARD_Y = 9;
    int[][]board;

    public Sudoku(){
        Font font = new Font("Times New Roman", Font.BOLD,14); //sets font of VALUES in FRAME
        int x = 0;
        int y = 0;

        for (int i = 0; i < 9; i++)
        {
            if((i % 3) == 0)//makes spaces along every third COLUMN
            {
                y = y + 33;
                x = 0;
            }
            else//if not past the 3rd LABEL do not make a space
            {
                y = y + 30;
                x = 0;
            }
            for (int j = 0; j < 9; j++)
            {
                if ((j % 3) == 0)//makes spaces along every thrid ROW
                {
                    x = x + 33;
                }
                else{
                x = x + 30;//if not past the 3rd LABEL do not make a space
                }
                //Below here makes the FRAME itself
                txt[i][j] = new JTextField();
                txt[i][j].setVisible(true);
                txt[i][j].setBounds(x,y,30,30);
                txt[i][j].setHorizontalAlignment(JTextField.CENTER);
                this.add(txt[i][j]);
                txt[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                txt[i][j].setBackground(Color.white);
                txt[i][j].setOpaque(true);
                txt[i][j].setFont(font.deriveFont(Font.BOLD));
            }
        }
        //Below sets the FORMAT of the WINDOW and stops the program once the WINDOW is closed
        setLayout(null);
        setSize(400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void makeHoles(double holesToMake)//makes HOLES and gives them a VALUE of 0
	{
            double remainingSquares = 81; //number of squares you start with
            double remainingHoles = holesToMake; //number of HOLES left to be created

            for(int i=0;i<9;i++)
            {
                for(int j=0;j<9;j++)
                {
                    double holeChance = remainingHoles/remainingSquares; //this gives a probability to the holes
                    if(Math.random() <= holeChance)//gives a 0 to the HOLE if the random is = to the holeChance
                    {
                        board[i][j] = 0;//sets the HOLE to 0
                        remainingHoles--;//obviously need to decrease the remaining HOLES
                    }
                    remainingSquares--;//you also need to decrease the remainingSquares
                    }
            }
	}
    public int[][] nextBoard(int difficulty)//allows you to set how DIFFICULTY
	{
            board = new int[BOARD_X][BOARD_Y];
            nextCell(0,0); //sets the CELL of the BOARD to the first CELL (upper left)
            makeHoles(difficulty);
            return board;

	}
        private boolean legalMove(int a, int b, int current)//checks if each VALUE in the BOARD created is LEGAL
        {
            for(int i=0;i<9;i++) //Goes down the CELLS in the y-axis and checks if any VALUES are the same as the VALUE within the current CELL
            {
                if(current == board[a][i])
                    return false;
            }
            for(int i=0;i<9;i++) //Goes down the CELLS in the x-axis and checks if any are the same as the current CELL
            {
                    if(current == board[i][b])
                        return false;
            }
            int cornerX = 0;
            int cornerY = 0;
            if(a > 2) //Gives the CLUSTERS on the x-axis
                if(a > 5)
                        cornerX = 6;
                else
                        cornerX = 3;
            if(b > 2)//Gives the CLUSTERS on the y-axis
                if(b > 5)
                        cornerY = 6;
                else
                        cornerY = 3;
            for(int i=cornerX;i<10 && i<cornerX+3;i++)//Checks if the any of the VALUES are repeated within the CLUSTERS
                for(int j=cornerY;j<10 && j<cornerY+3;j++)//Checks if the any of the VALUES are repeated within the CLUSTERS
                    if(current == board[i][j])
                        return false;
            return true;//Once everything is okay, then you are good :)
        }
        public boolean nextCell(int a, int b)//Tries to place numbers in the CELLS of the BOARD LEGALLY
        {
            int nextX = a;
            int nextY = b;
            int[] toChk = {1,2,3,4,5,6,7,8,9};//All possible values that are allowed in the CELL
            Random r = new Random();
            int temp = 0;
            int current = 0;
            int top = toChk.length;
            
            for(int i=top-1;i>0;i--){
                current = r.nextInt(i);//gives a random integer to current
                temp = toChk[current];
                toChk[current] = toChk[i];
                toChk[i] = temp;
            }
            
            for(int i=0;i<top;i++)//goes through the VALUES in the toChk array
            {
                if(legalMove(a, b, toChk[i]))//Checks if the VALUE placed in the CELL is allowed in the CLUSTERS, ROWS, and COLUMNS of the BOARD
                {
                    board[a][b] = toChk[i];
                        if(a == 8)
                        {
                            if(b == 8)//Goes through all the ROWS of the BOARD
                                return true; //Done placing all VALUES in the CELLS in the whole BOARD
                            else
                            {
                                nextX = 0; //resets the nextX back to 0 to go to the next COLUMN
                                nextY = b + 1;//increases the nextY in the next COLUMN
                            }
                        }
                        else
                        {
                            nextX = a + 1;//increases the nextX to the next COLUMN
                        }
                        if(nextCell(nextX, nextY))//Recursion of the nextCell method
                            return true;
                }
            }
            board[a][b] = 0;//Gives the VALUE of the first CELL a 0 if the VALUE of the first CELL does is not LEGAL
            return false;//If the the values placed are not LEGAL return false
        }
        public void print()//Prints the values of the BOARD into the FRAME
	{
            for(int i=0;i<9;i++)//cycles throught the COLUMNS
            {

                for(int j=0;j<9;j++)//cycles throught the ROWS
                {
                    if (board[i][j] == 0)
                    {
                        txt[i][j].setText("");
                    }
                    else
                    {
                        txt[i][j].setText(Integer.toString(board[i][j]));//sets the VALUE of the current CELL in the board into the CELL of the FRAME 
                    }
                    System.out.print(board[i][j] + "  ");//This is just what is printed out in the output
            }
		System.out.println();//Separates ROWS by one line
            }
        }
    public static void main(String[] args) {
        Sudoku sk = new Sudoku();
        sk.nextBoard(35);
        sk.print();
        sk.setVisible(true);
    }
}