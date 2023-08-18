package minesweeper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The main class of the application
 * @author Andreas Kalavas
 */
public class Minesweeper extends Application {
    private int checkok(String st1,String st2, String st3, String st4){
        int t1=0,t2=0,t3=0,t4=0;
        try{
            t1 = Integer.parseInt(st1);
            t2 = Integer.parseInt(st2);
            t3 = Integer.parseInt(st3);
            t4 = Integer.parseInt(st4);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
        if(t1!=1 && t1!=2)return 0;
        if((t1==1 && t3!=0)|| (t3!=0 && t3!=1))return 0;
        if(t1==1 && (t4<120|| t4>180))return 0;
        if(t1==2 && (t4<240|| t4>360))return 0;
        if(t1==1 && (t2<9|| t2>11))return 0;
        if(t1==2 && (t2<35|| t2>45))return 0;
        return 1;
    }

    private MenuBar SetMenuBar(Stage stage, Game playing, Game loading){
        MenuBar menubar=new MenuBar();
        Menu menapl=new Menu("Application");
        Menu mendet=new Menu("Details");
        MenuItem create=new MenuItem("Create");
        MenuItem load=new MenuItem("Load");
        MenuItem start=new MenuItem("Start");
        MenuItem exit=new MenuItem("Exit");
        MenuItem rounds=new MenuItem("Rounds");
        MenuItem solution=new MenuItem("Solution");
        menapl.getItems().add(create);
        menapl.getItems().add(load);
        menapl.getItems().add(start);
        menapl.getItems().add(exit);
        mendet.getItems().add(rounds);
        mendet.getItems().add(solution);
        menubar.getMenus().add(menapl);
        menubar.getMenus().add(mendet);

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.close();
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Stage loadstage=new Stage();
                loadstage.setTitle("Load Scenario");
                loadstage.resizableProperty().setValue(Boolean.FALSE);
                GridPane loadgrid = new GridPane();
                loadgrid.setAlignment(Pos.CENTER);
                loadgrid.setHgap(10);
                loadgrid.setVgap(10);
                loadgrid.setPadding(new Insets(25, 25, 25, 25));
                Scene scene2 = new Scene(loadgrid, 350, 150);
                loadstage.setScene(scene2);

                Text lscenetitle = new Text("Specify Scenario File");
                lscenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                loadgrid.add(lscenetitle, 0, 0);
                Text tlfile = new Text("Name of File:");
                loadgrid.add(tlfile, 0, 1);
                TextField inputlname = new TextField();
                loadgrid.add(inputlname, 1, 1);

                Text lincorrect=new Text("File name must be\n3 characters or more");
                lincorrect.setFill(Color.TRANSPARENT);
                loadgrid.add(lincorrect,0,2);
                Button lbtok = new Button("OK");
                loadgrid.add(lbtok, 1, 2);

                loadstage.show();

                lbtok.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        String st1=inputlname.getText();
                        if(st1.length()<3)
                            lincorrect.setFill(Color.RED);
                        else{
                            try{
                                st1="medialab/"+st1+".txt";
                                FileInputStream fin=new FileInputStream(st1);
                                int i=0;
                                String[] temp=new String[4];
                                temp[0]=temp[1]=temp[2]=temp[3]="";
                                int j=0;
                                while((i=fin.read())!=-1) {
                                    if ((char) i == '\n') {
                                        j++;
                                        continue;
                                    }
                                    temp[j] += (char) i;
                                }
                                if(j!=3) {
                                    fin.close();
                                    throw new InvalidDescriptionException("Description");
                                }
                                if(checkok(temp[0],temp[1],temp[3],temp[2])==1){
                                    //System.out.println("correct input");
                                    loading.update(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[3]),Integer.parseInt(temp[2]));
                                }
                                else {
                                    fin.close();
                                    throw new InvalidValueException("Value");
                                }

                                fin.close();
                                loadstage.close();
                            }catch(Exception e){
                                String typee=e.getMessage();
                                String messageprompt="";
                                if(typee=="Value"){
                                    messageprompt="Invalid Values";
                                }
                                else if(typee=="Description"){
                                    messageprompt="Invalid Description";
                                }
                                else{
                                    messageprompt="File Not Found";
                                }
                                Stage exstage=new Stage();
                                exstage.setTitle("ERROR!!!");
                                exstage.resizableProperty().setValue(Boolean.FALSE);
                                GridPane exgrid = new GridPane();
                                exgrid.setAlignment(Pos.CENTER);
                                exgrid.setHgap(10);
                                exgrid.setVgap(10);
                                exgrid.setPadding(new Insets(25, 25, 25, 25));
                                Scene scene2 = new Scene(exgrid, 250, 100);
                                exstage.setScene(scene2);
                                Text tmes=new Text(messageprompt);
                                tmes.setFill(Color.RED);
                                exgrid.add(tmes,0,0);
                                Button exbt=new Button("OK");
                                exgrid.add(exbt,0,1);
                                exstage.show();
                                exbt.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent t) {
                                        loadstage.close();
                                        exstage.close();
                                    }
                                });
                            }
                        }
                    }
                });

            }
        });

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                if(loading.level!=-1){
                    playing.update(loading.level, loading.mines, loading.supermine, loading.time);
                }
                else{
                    Stage exstage=new Stage();
                    exstage.setTitle("ERROR!!!");
                    exstage.resizableProperty().setValue(Boolean.FALSE);
                    GridPane exgrid = new GridPane();
                    exgrid.setAlignment(Pos.CENTER);
                    exgrid.setHgap(10);
                    exgrid.setVgap(10);
                    exgrid.setPadding(new Insets(25, 25, 25, 25));
                    Scene scene2 = new Scene(exgrid, 250, 100);
                    exstage.setScene(scene2);
                    Text tmes=new Text("You have not loaded a Description,\nso a game of level 1 is created");
                    tmes.setFill(Color.RED);
                    exgrid.add(tmes,0,0);
                    Button exbt=new Button("OK");
                    exgrid.add(exbt,0,1);
                    exstage.show();
                    exbt.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            exstage.close();
                        }
                    });
                }
                playing.init();
            }
        });

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Stage stage2=new Stage();
                stage2.setTitle("Create Scenario");
                stage2.resizableProperty().setValue(Boolean.FALSE);
                GridPane grid1 = new GridPane();
                grid1.setAlignment(Pos.CENTER);
                grid1.setHgap(10);
                grid1.setVgap(10);
                grid1.setPadding(new Insets(25, 25, 25, 25));
                Scene scene2 = new Scene(grid1, 400, 400);
                stage2.setScene(scene2);

                Text scenetitle = new Text("Fill game details");
                scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid1.add(scenetitle, 0, 0);

                Text tfile = new Text("Name of File:");
                grid1.add(tfile, 0, 1);

                Text tlevel = new Text("Level:");
                grid1.add(tlevel, 0, 2);

                Text tmines = new Text("No of Mines:");
                grid1.add(tmines, 0, 3);

                Text tsmine = new Text("Super Mine:");
                grid1.add(tsmine, 0, 4);

                Text ttime = new Text("Time(sec):");
                grid1.add(ttime, 0, 5);

                TextField inputname = new TextField();
                grid1.add(inputname, 1, 1);

                TextField inputlevel = new TextField();
                grid1.add(inputlevel, 1, 2);

                TextField inputmines = new TextField();
                grid1.add(inputmines, 1, 3);

                TextField inputsmine = new TextField();
                grid1.add(inputsmine, 1, 4);

                TextField inputtime = new TextField();
                grid1.add(inputtime, 1, 5);

                Button btok = new Button("OK");
                grid1.add(btok, 1, 6);

                String incorrectt="Incorrect details:\nName of File: 3 characters or more\n                      Not 'mines' or 'rounds'\nLevel: 1 or 2\nNo of Mines: between 9 and 11 for level 1\n";
                incorrectt+="                      between 35 and 45 for level 2\nSuper Mine: 0 for level 1\n                    0 or 1 for level 2\n";
                incorrectt+="Time: between 120 and 180 for level 1\n          between 240 and 360 for level 2";

                Text incorrect=new Text(incorrectt);
                incorrect.setFill(Color.TRANSPARENT);
                grid1.add(incorrect,0,6);

                btok.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        String st1=inputlevel.getText();
                        String st2=inputmines.getText();
                        String st3=inputsmine.getText();
                        String st4=inputtime.getText();
                        String st5=inputname.getText();

                        if(checkok(st1,st2,st3,st4)==1 && st5.length()>2 && !st5.equals("mines") && !st5.equals("rounds")) {
                            try{
                                st5="medialab/"+st5+".txt";
                                FileOutputStream fout=new FileOutputStream(st5);
                                String printfile=st1+"\n"+st2+"\n"+st4+"\n"+st3;
                                byte b[]=printfile.getBytes();
                                fout.write(b);
                                fout.close();
                            }catch(Exception e){System.out.println(e);}
                            stage2.close();
                        }
                        else{
                            incorrect.setFill(Color.RED);
                        }
                    }
                });

                stage2.show();
            }
        });

        rounds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Stage roundstage=new Stage();
                roundstage.setTitle("Rounds");
                roundstage.resizableProperty().setValue(Boolean.FALSE);
                GridPane grid1 = new GridPane();
                grid1.setAlignment(Pos.CENTER);
                grid1.setHgap(10);
                grid1.setVgap(10);
                grid1.setPadding(new Insets(25, 25, 25, 25));
                Scene scene2 = new Scene(grid1, 300, 250);
                roundstage.setScene(scene2);
                roundstage.show();

                Text minestitle = new Text("Mines");
                minestitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid1.add(minestitle, 0, 0);
                Text mines1 = new Text(playing.scoreboard.mines[0]);
                mines1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(mines1, 0, 1);
                Text mines2 = new Text(playing.scoreboard.mines[1]);
                mines2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(mines2, 0, 2);
                Text mines3 = new Text(playing.scoreboard.mines[2]);
                mines3.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(mines3, 0, 3);
                Text mines4 = new Text(playing.scoreboard.mines[3]);
                mines4.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(mines4, 0, 4);
                Text mines5 = new Text(playing.scoreboard.mines[4]);
                mines5.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(mines5, 0, 5);

                Text movestitle = new Text("Moves");
                movestitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid1.add(movestitle, 1, 0);
                Text moves1 = new Text(playing.scoreboard.moves[0]);
                moves1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(moves1, 1, 1);
                Text moves2 = new Text(playing.scoreboard.moves[1]);
                moves2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(moves2, 1, 2);
                Text moves3 = new Text(playing.scoreboard.moves[2]);
                moves3.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(moves3, 1, 3);
                Text moves4 = new Text(playing.scoreboard.moves[3]);
                moves4.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(moves4, 1, 4);
                Text moves5 = new Text(playing.scoreboard.moves[4]);
                moves5.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(moves5, 1, 5);

                Text timetitle = new Text("Time");
                timetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid1.add(timetitle, 2, 0);
                Text time1 = new Text(playing.scoreboard.time[0]);
                time1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(time1, 2, 1);
                Text time2 = new Text(playing.scoreboard.time[1]);
                time2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(time2, 2, 2);
                Text time3 = new Text(playing.scoreboard.time[2]);
                time3.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(time3, 2, 3);
                Text time4 = new Text(playing.scoreboard.time[3]);
                time4.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(time4, 2, 4);
                Text time5 = new Text(playing.scoreboard.time[4]);
                time5.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(time5, 2, 5);

                Text winnertitle = new Text("Winner");
                winnertitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid1.add(winnertitle, 3, 0);
                Text winner1 = new Text(playing.scoreboard.winner[0]);
                winner1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(winner1, 3, 1);
                Text winner2 = new Text(playing.scoreboard.winner[1]);
                winner2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(winner2, 3, 2);
                Text winner3 = new Text(playing.scoreboard.winner[2]);
                winner3.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(winner3, 3, 3);
                Text winner4 = new Text(playing.scoreboard.winner[3]);
                winner4.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(winner4, 3, 4);
                Text winner5 = new Text(playing.scoreboard.winner[4]);
                winner5.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
                grid1.add(winner5, 3, 5);

                Button exbt=new Button("OK");
                grid1.add(exbt,0,6);
                //stage.show();
                exbt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        roundstage.close();
                    }
                });
            }
        });

        solution.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if(playing.state==1) {
                    playing.showmines();
                    playing.finish(1);
                }
                else{
                    Stage exstage=new Stage();
                    exstage.setTitle("ERROR!!!");
                    exstage.resizableProperty().setValue(Boolean.FALSE);
                    GridPane exgrid = new GridPane();
                    exgrid.setAlignment(Pos.CENTER);
                    exgrid.setHgap(10);
                    exgrid.setVgap(10);
                    exgrid.setPadding(new Insets(25, 25, 25, 25));
                    Scene scene2 = new Scene(exgrid, 250, 100);
                    exstage.setScene(scene2);
                    Text tmes=new Text("You have to begin playing first\nin order to show the solution");
                    tmes.setFill(Color.RED);
                    exgrid.add(tmes,0,0);
                    Button exbt=new Button("OK");
                    exgrid.add(exbt,0,1);
                    exstage.show();
                    exbt.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            exstage.close();
                        }
                    });
                }
            }
        });

        return menubar;
    }

    private HBox SetInfo(Game playing){
        HBox info = new HBox();
        info.setAlignment(Pos.CENTER);
        Text mines=new Text("Mines:");
        mines.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Text marked=new Text(" Marked Boxes:");
        marked.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Text time=new Text(" Time Left:");
        time.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Rectangle minesbox=new Rectangle(0,0,50,25);
        minesbox.setStroke(Color.BLACK);
        minesbox.setFill(Color.YELLOW);
        StackPane minesval=new StackPane(minesbox,playing.minesno);

        Rectangle markedbox=new Rectangle(0,0,50,25);
        markedbox.setStroke(Color.BLACK);
        markedbox.setFill(Color.YELLOW);
        StackPane markedval=new StackPane(markedbox,playing.markedno);

        Rectangle timebox=new Rectangle(0,0,50,25);
        timebox.setStroke(Color.BLACK);
        timebox.setFill(Color.YELLOW);
        StackPane timeval=new StackPane(timebox,playing.timeno);

        info.getChildren().add(mines);
        info.getChildren().add(minesval);
        info.getChildren().add(marked);
        info.getChildren().add(markedval);
        info.getChildren().add(time);
        info.getChildren().add(timeval);

        return info;
    }

    private GridPane SetGrid(Game playing){
        GridPane grid = playing.board.grid;

        for(Node node : grid.getChildren()) {
            int row=GridPane.getRowIndex(node);
            int col=GridPane.getColumnIndex(node);
            node.setOnMouseClicked(event ->
            {
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    playing.leftclick(row,col);           //pointer to class
                } else if (event.getButton() == MouseButton.SECONDARY)
                {
                    playing.rightclick(row,col);          //pointer to class
                }
            });
        }
        return grid;
    }

    /**
     * The function that sets the game
     * @param stage the stage for the javafx appllication
     */
    @Override
    public void start(Stage stage) throws IOException {
        Game playing=new Game();
        playing.init();
        Game loading=new Game("load");

        stage.setTitle("MediaLab MineSweeper");
        VBox parts = new VBox();
        parts.setSpacing(10);

        parts.getChildren().add(SetMenuBar(stage,playing,loading));

        parts.getChildren().add(SetInfo(playing));

        parts.getChildren().add(SetGrid(playing));

        Scene scene = new Scene(parts, 460, 520);
        stage.setScene(scene);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();
    }
    /**
     * The main function, which launches the application
     * @param args the arguments of the execution
     */
    public static void main(String[] args) {
        launch();
    }
}
