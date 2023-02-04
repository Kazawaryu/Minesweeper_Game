import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Save {
    int saveNum = 0;

    public void singleSave(SinglePlayer SP)  {
        try {
            if (saveNum != 0) {
                File file = new File("D:\\MineSweeper_Save_1\\save_0" + saveNum + ".txt");
                System.out.println(saveNum);
                FileWriter fw = new FileWriter(file);
                FileWriter FW = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(FW);
                PrintWriter pw = new PrintWriter(bw);

                fw.write("");

                pw.print(SP.ROW + " " + SP.COL + " " + SP.mineCount );//行，列，雷
                pw.println("");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                pw.print(df.format(new Date()) );
                pw.println("");
                for (int i = 0; i < SP.ROW; i++) {
                    for (int j = 0; j < SP.COL; j++) {
                        pw.print(SP.space[i][j]+" ");
                    }
                }//雷数组
                pw.println("");
                for (int i = 0; i < SP.leftClicked.size(); i++) {
                    pw.print(SP.leftClicked.get(i) + " ");
                }//左键arraylist
                pw.println("");
                for (int i = 0; i < SP.rightClicked.size(); i++) {
                    pw.print(SP.rightClicked.get(i) + " ");
                }//右键arraylist
                pw.println("");
                pw.println(SP.unopened + " " + SP.opened + " " + SP.flagCnt + " " + SP.secs);//未开，已开，标旗，时间

                pw.close();
                FW.close();
            }
        }catch (IOException e){
            System.out.println("ERROR");
        }
    }

    public void doubleSave(DoublePlayer DP){
        try {
            if (saveNum != 0) {
                File file = new File("D:\\MineSweeper_Save_2\\save_0" + saveNum + ".txt");
                System.out.println(saveNum);
                FileWriter fw = new FileWriter(file);
                FileWriter FW = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(FW);
                PrintWriter pw = new PrintWriter(bw);

                fw.write("");

                pw.print(DP.ROW + " " + DP.COL + " " + DP.mineCount );//1.行，列，雷
                pw.println("");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                pw.print(df.format(new Date()) );//2.时间
                pw.println("");
                for (int i = 0; i < DP.ROW; i++) {
                    for (int j = 0; j < DP.COL; j++) {
                        pw.print(DP.space[i][j]+" ");
                    }
                }//3.雷数组
                pw.println("");
                for (int i = 0; i < DP.leftClicked.size(); i++) {
                    pw.print(DP.leftClicked.get(i) + " ");
                }//4.左键arraylist
                pw.println("");
                for (int i = 0; i < DP.rightClicked.size(); i++) {
                    pw.print(DP.rightClicked.get(i) + " ");
                }//5.右键arraylist
                pw.println("");
                pw.println(DP.unopened + " " + DP.opened + " " + DP.flagCnt + " " + DP.secs);//6.未开，已开，标旗，时间
                pw.print(DP.playerCode+" "+DP.UNClick);//7.目前玩家+未走步数
                pw.println("");
                pw.print(DP.player1Score+" "+DP.player1False+" "+DP.player1UN);//8.玩家1信息
                pw.println("");
                pw.print(DP.player2Score+" "+DP.player2False+" "+DP.player2UN);//9.玩家2信息


                pw.close();
                FW.close();
            }
        }catch (IOException e){
            System.out.println("ERROR");
        }


    }

}
