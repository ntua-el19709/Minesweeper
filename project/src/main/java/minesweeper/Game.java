package minesweeper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The class that represents a minesweeper game session
 * @author Andreas Kalavas
 */
public class Game {
    int level, mines, supermine, time, size;
    result scoreboard=new result();
    int[][] minecoors=new int[45][2];
    int smineno;
    int marked, timer, state,openboxes,curtime,moves;//state=0,1,2 for initialized,playing,finished
    Rectangle[][] boxes = new Rectangle[16][16];
    Text[][] nums = new Text[16][16];
    Text minesno = new Text();
    Text markedno = new Text();
    Text timeno = new Text();
    GridPane grid = new GridPane();
    Cellbox board=new Cellbox();

    /**
     * The default constructor, it sets the description for the easiest game possible
     */
    public Game() {
        level = 1;
        mines = 9;
        supermine = 0;
        time = 180;
        size = 9;
    }

    /**
     * A constructor that does not set a game description
     * @param t a random string
     */
    public Game(String t) {
        level = mines = supermine = time = size = -1;
    }

    /**
     * Set new game description
     * @param t1 the level of the game (1 or 2)
     * @param t2 the number of mines of the game
     * @param t3 the existence of super mine (0 or 1)
     * @param t4 the time available for the game
     */
    public void update(int t1, int t2, int t3, int t4) {
        level = t1;
        mines = t2;
        supermine = t3;
        time = t4;
        if (level == 1)
            size = 9;
        else
            size = 16;
    }

    /**
     * Create a new game, according to the game description
     */
    public void init() {
        marked = 0;
        moves=0;
        curtime=0;
        openboxes=0;
        timer = time;
        state = 0;
        minesno.setText(Integer.toString(mines));
        minesno.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        markedno.setText("0");
        markedno.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        timeno.setText(Integer.toString(time));
        timeno.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        board.init(size);

        for(Node node : board.grid.getChildren()) {
            int row=GridPane.getRowIndex(node);
            int col=GridPane.getColumnIndex(node);
            node.setOnMouseClicked(event ->
            {
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    leftclick(row,col);
                } else if (event.getButton() == MouseButton.SECONDARY)
                {
                    rightclick(row,col);
                }
            });
        }
    }
    private void starttimer() {
        Timer countdown = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(state!=1){
                    countdown.cancel();
                    countdown.purge();
                    return;
                }
                timer -= 1;
                timeno.setText(Integer.toString(timer));
                if(timer==0){
                    state=2;
                    countdown.cancel();
                    countdown.purge();
                    Platform.runLater(() -> {
                        finish(1);
                    });
                }
            }
        };
        countdown.schedule(task, 1000,1000);
    }
    private void start(int i,int j){
        state=1;
        Random rn=new Random();
        int count=0;
        while(count<mines){
            int tx,ty;
            tx=rn.nextInt(size);
            ty=rn.nextInt(size);
            if(tx-i>1 || i-tx>1 ||ty-j>1 || j-ty>1){ // to proto kouti pou pato einai panta keno
                if(board.getcelltext(tx,ty)=="#"){
                    board.updatemine(tx,ty);
                    minecoors[count][0]=tx;
                    minecoors[count][1]=ty;
                    count+=1;
                }
            }
        }
        board.updateall();
        smineno=-1;
        if(supermine==1){
            smineno=rn.nextInt(mines);
            board.updatesmine(minecoors[smineno][0],minecoors[smineno][1]);
        }
        try{
            FileOutputStream fout=new FileOutputStream("medialab/mines.txt");
            for(int k=0;k<mines;k++) {
                String tem="";
                if(k==smineno)tem+="1";
                else tem+="0";
                if(k!=mines-1)tem+="\n";
                String printline=Integer.toString(minecoors[k][0])+","+Integer.toString(minecoors[k][1])+","+tem;
                byte b[] = printline.getBytes();
                fout.write(b);
            }
            fout.close();
            this.starttimer();
        }catch(Exception e){System.out.println(e);}
    }

    /**
     * Terminating the game run, after an outcome has been reached
     * @param ans 1 (game lost) or 2 (game won)
     */
    public void finish(int ans){
        state=2;
        Stage finstage=new Stage();
        finstage.setTitle("Game Finished");
        GridPane exgrid = new GridPane();
        exgrid.setAlignment(Pos.CENTER);
        exgrid.setHgap(10);
        exgrid.setVgap(10);
        exgrid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene2 = new Scene(exgrid, 250, 100);
        finstage.setScene(scene2);
        String message="";
        if(ans==2)message+="You WON!!!";
        else {message+="You LOST!!!";}
        Text tmes=new Text(message);
        tmes.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        exgrid.add(tmes,0,0);
        Button exbt=new Button("OK");
        exgrid.add(exbt,0,1);
        finstage.show();

        exbt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                finstage.close();
                String winner="";
                if(ans==1)winner+="Computer";
                else winner+="Player";
                scoreboard.update(mines,moves,time-timer,winner);
            }
        });
    }
    private void openrecursive(int i,int j){
        openboxes+=board.openrecursive(i,j);
    }
    private void open(int i,int j){
        int temp=board.open(i,j);
        if(temp>100)openboxes++;
        if (temp==149 || temp==-1) {
            marked--;
        } else if (temp==151 || temp==1) {
            marked++;
        }
        markedno.setText(Integer.toString(marked));
    }

    /**
     * Make changes after the square in the ith row and jth collumn is right clicked
     * @param i the row of the square
     * @param j the column of the square
     */
    public void rightclick(int i, int j) {
        if(state==2)return;
        Color boxColor = board.getcellcolor(i,j);
        if(boxColor==Color.BLUE && marked<mines) {
            marked += 1;
            markedno.setText(Integer.toString(marked));
            board.markcell(i,j);
            if(board.getcelltext(i,j)=="@" && moves<5){
                for(int k=0;k<size;k++){
                    if(k!=i)open(k,j);
                    if(k!=j)open(i,k);
                }
            }
        }
        else if(boxColor==Color.RED){
            marked -= 1;
            markedno.setText(Integer.toString(marked));
            board.unmarkcell(i,j);
        }
    }

    /**
     * Make changes after the square in the ith row and jth collumn is left clicked
     * @param i the row of the square
     * @param j the collumn of the square
     */
    public void leftclick(int i, int j) {
        int ans=0;
        Color boxColor = board.getcellcolor(i,j);
        if(boxColor==Color.TRANSPARENT ||boxColor==Color.BLACK|| boxColor==Color.RED||state==2)return;
        if(state==0)this.start(i,j);
        moves+=1;
        openrecursive(i,j);
        if(board.getcelltext(i,j)=="*" || board.getcelltext(i,j)=="@"){
            ans=1; //game lost
        }
        if(openboxes==size*size-mines)ans=2;
        if(ans!=0)this.finish(ans);
    }

    /**
     * Show the positions of the mines
     */
    public void showmines(){
        for(int i=0;i<mines;i++){
            board.revealcell(minecoors[i][0],minecoors[i][1]);
        }
    }
}
