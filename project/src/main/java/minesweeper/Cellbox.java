package minesweeper;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * The class that represents the board of the game
 * @author Andreas Kalavas
 */
public class Cellbox {
    Rectangle box;
    Text num;
    GridPane grid = new GridPane();
    Rectangle[][] boxes = new Rectangle[16][16];
    Text[][] nums = new Text[16][16];
    int size;

    /**
     * Initialize the board with specific size (number of rows and columns)
     * @param s the size of the board
     */
    public void init(int s){
        size=s;
        grid.getChildren().clear();
        grid.setAlignment(Pos.CENTER);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Rectangle box = new Rectangle(0, 0, 432 / size, 432 / size);
                box.setFill(Color.BLUE);
                box.setStroke(Color.BLACK);
                box.setStrokeWidth(1);
                boxes[i][j] = box;
                Text num = new Text("#");
                num.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                nums[i][j] = num;

                StackPane temp = new StackPane(nums[i][j], boxes[i][j]);
                grid.add(temp, j, i);
            }
        }

    }
    /**
     * Get the String content of a specific cell
     * @param tx the row of the cell
     * @param ty the column of the cell
     * @return The content of the cell at row tx, and column ty
     */
    public String getcelltext(int tx,int ty){
        return nums[tx][ty].getText();
    }
    /**
     * Get the Color of a specific cell
     * @param tx the row of the cell
     * @param ty the column of the cell
     * @return The Color of the cell at row tx, and column ty
     */
    public Color getcellcolor(int tx,int ty){
        return (Color) boxes[tx][ty].getFill();
    }
    /**
     * Set a specific cell to be a mine
     * @param tx the row of the cell
     * @param ty the column of the cell
     */
    public void updatemine(int tx,int ty){
        nums[tx][ty].setText("*");
    }
    /**
     * Set a specific cell to be a supermine
     * @param tx the row of the cell
     * @param ty the column of the cell
     */
    public void updatesmine(int tx,int ty){
        nums[tx][ty].setText("@");
    }
    /**
     * Set the correct String content for each cell
     */
    public void updateall(){
        for(int k=0;k<size;k++){
            for(int l=0;l<size;l++){
                if(nums[k][l].getText()!="#")continue;
                int curnum=0;
                for(int m=-1;m<2;m++){
                    for(int n=-1;n<2;n++){
                        if(m==0 && n==0)continue;
                        if(k+m>=0 && k+m<size && l+n>=0 && l+n<size){
                            if(nums[k+m][l+n].getText()=="*")
                                curnum+=1;
                        }
                    }
                }
                String tem="";
                if(curnum>0)tem+=Integer.toString(curnum);
                nums[k][l].setText(tem);
            }
        }
    }
    /**
     * Show the content of a specific cell
     * @param i the row of the cell
     * @param j the column of the cell
     */
    public void revealcell(int i,int j){boxes[i][j].setFill(Color.TRANSPARENT);}
    /**
     * Mark a specific cell with a bomb flag
     * @param i the row of the cell
     * @param j the column of the cell
     */
    public void markcell(int i,int j){boxes[i][j].setFill(Color.RED);}
    /**
     * Unmark a specific cell
     * @param i the row of the cell
     * @param j the column of the cell
     */
    public void unmarkcell(int i,int j){boxes[i][j].setFill(Color.BLUE);}
    /**
     * Open a specific cell, and if it is empty open the neighboring ones recursively
     * @param i the row of the cell
     * @param j the column of the cell
     * @return The number of cells opened
     */
    public int openrecursive(int i,int j){
        int ans=1;
        revealcell(i,j);
        if(nums[i][j].getText()==""){
            for(int m=-1;m<2;m++){
                for(int n=-1;n<2;n++){
                    if(m==0 && n==0)continue;
                    if(i+m>=0 && i+m<size && j+n>=0 && j+n<size){
                        Color boxColor = (Color) boxes[i+m][j+n].getFill();
                        if(boxColor==Color.BLUE)ans+=openrecursive(i+m,j+n);
                    }
                }
            }
        }
        return ans;
    }
    /**
     * Open a specific cell
     * @param i the row of the cell
     * @param j the column of the cell
     * @return Info whether the marked cells and open cells change
     */
    public int open(int i,int j){
        int ans=0;
        Color boxColor = (Color) boxes[i][j].getFill();
        if(boxColor==Color.RED){
            ans--;          //already marked square
        }
        else if(boxColor!=Color.BLUE)return ans;
        if(nums[i][j].getText()=="*"){
            boxes[i][j].setFill(Color.BLACK);
            ans++;
        }
        else{
            revealcell(i,j);
            ans+=150;
        }
        return ans;
    }
}
