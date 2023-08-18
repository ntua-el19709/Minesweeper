package minesweeper;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * The class that represents the results the last 5 rounds that were played
 * @author Andreas Kalavas
 */
public class result{
    String[] mines=new String[5];
    String[] moves=new String[5];
    String[] time=new String[5];
    String[] winner=new String[5];
    /**
     * The default constructor, it sets the rounds with null data
     */
    public result(){
        try{
            FileInputStream fin=new FileInputStream("medialab/rounds.txt");
            String[][] temp=new String[5][4];
            for(int i=0;i<5;i++){
                for(int j=0;j<4;j++){
                    temp[i][j]="";
                }
            }
            int i,j=0,k=0;
            while((i=fin.read())!=-1) {
                if ((char) i == '\n') {
                    j++;
                    k=0;
                    continue;
                }
                if ((char) i == ' ') {
                    k++;
                    continue;
                }
                temp[j][k] += (char) i;
            }
            for(i=0;i<5;i++){
                mines[i]=temp[i][0];
                moves[i]=temp[i][1];
                time[i]=temp[i][2];
                winner[i]=temp[i][3];
            }
            fin.close();
        }
        catch(Exception e){
            try {
                FileOutputStream fout = new FileOutputStream("medialab/rounds.txt");
                for (int i = 0; i < 5; i++) {
                    mines[i] = moves[i] = time[i] = winner[i] = "-";
                    String printfile = "- - - -\n";
                    byte b[] = printfile.getBytes();
                    fout.write(b);
                }
                fout.close();
            }
            catch(Exception ee){
                //nothing
            }
        }
    }
    /**
     * Insert a new round
     * @param t1 the number of mines
     * @param t2 the number of moves
     * @param t3 the time
     * @param t4 'Computer' or 'Player'
     */
    public void update(int t1,int t2,int t3,String t4){
        for(int i=4;i>0;i--){
            mines[i]=mines[i-1];
            moves[i]=moves[i-1];
            time[i]=time[i-1];
            winner[i]=winner[i-1];
        }
        mines[0]=Integer.toString(t1);
        moves[0]=Integer.toString(t2);
        time[0]=Integer.toString(t3);
        winner[0]=t4;
        try {
            FileOutputStream fout = new FileOutputStream("medialab/rounds.txt");
            for (int i = 0; i < 5; i++) {
                String printfile= mines[i]+ " "+ moves[i] +" "+ time[i] +" "+ winner[i]+"\n";
                byte b[] = printfile.getBytes();
                fout.write(b);
            }
            fout.close();
        }
        catch(Exception ee){
            //nothing
        }
    }
}
