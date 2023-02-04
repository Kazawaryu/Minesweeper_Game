import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Reload implements ActionListener {

    JFrame saveLoading = new JFrame();

    JButton save_01 = new JButton();
    JButton save_02 = new JButton();
    JButton save_03 = new JButton();
    JButton save_04 = new JButton();

    JLabel s1Text = new JLabel();
    JLabel s2Text = new JLabel();
    JLabel s3Text = new JLabel();
    JLabel s4Text = new JLabel();

    int playerCase = 0;
    String path = "";

    String imagePath;

    public Reload(String imagePath){
        this.imagePath = imagePath;
    }
    public void flash(int playerCase) {
        if (playerCase == 1) {
            s1Text.setText("<html>模式： 单人模式<br>难度： " + getLevel(1, path) + "<br>存档时间：" + getTime(1, path));
            s2Text.setText("<html>模式： 单人模式<br>难度： " + getLevel(2, path) + "<br>存档时间：" + getTime(2, path));
            s3Text.setText("<html>模式： 单人模式<br>难度： " + getLevel(3, path) + "<br>存档时间：" + getTime(3, path));
            s4Text.setText("<html>模式： 单人模式<br>难度： " + getLevel(4, path) + "<br>存档时间：" + getTime(4, path));
        }
        if (playerCase == 2) {
            s1Text.setText("<html>模式： 双人模式<br>难度： " + getLevel(1, path) + "<br>存档时间：" + getTime(1, path));
            s2Text.setText("<html>模式： 双人模式<br>难度： " + getLevel(2, path) + "<br>存档时间：" + getTime(2, path));
            s3Text.setText("<html>模式： 双人模式<br>难度： " + getLevel(3, path) + "<br>存档时间：" + getTime(3, path));
            s4Text.setText("<html>模式： 双人模式<br>难度： " + getLevel(4, path) + "<br>存档时间：" + getTime(4, path));
        }
    }

    public static String getLevel(int saveNum, String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + saveNum + ".txt"));
            String basic = reader.readLine();
            if (basic != null) {
                String level[] = basic.trim().split(" ");
                int[] a = new int[level.length];
                for (int i = 0; i < level.length; i++) {
                    a[i] = Integer.parseInt(level[i]);
                }

                if (a[0] == 9 && a[1] == 9 && a[2]==10) return "简单难度";
                if (a[0] == 16 && a[1] == 16 && a[2]==40) return "中等难度";
                if (a[0] == 16 && a[1] == 30 && a[2]==99) return "困难难度";
                return "自定义模式";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "空存档";
    }

    public static String getTime(int saveNum, String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path + saveNum + ".txt"));
            String basic = reader.readLine();
            basic = reader.readLine();
            if (basic != null) {
                return basic.trim();
            } else return "空存档";

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Error";
    }

    private void setSave() {
        save_01.add(s1Text);
        save_01.setBackground(Color.WHITE);
        save_01.addActionListener(this);
        saveLoading.add(save_01);

        save_02.add(s2Text);
        save_02.setBackground(Color.LIGHT_GRAY);
        save_02.addActionListener(this);
        saveLoading.add(save_02);

        save_03.add(s3Text);
        save_03.setBackground(Color.WHITE);
        save_03.addActionListener(this);
        saveLoading.add(save_03);

        save_04.add(s4Text);
        save_04.setBackground(Color.LIGHT_GRAY);
        save_04.addActionListener(this);
        saveLoading.add(save_04);


    }

    public void setLoading() {
        LoadingItem(saveLoading);
        flash(playerCase);
        setSave();
    }

    static void LoadingItem(JFrame saveLoading) {
        saveLoading.setTitle("存档");
        saveLoading.setSize(300, 400);
        saveLoading.setResizable(false);
        saveLoading.setLayout(new GridLayout(4, 1, 0, 5));


        saveLoading.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        saveLoading.setLocationRelativeTo(null);
        saveLoading.setVisible(true);
    }

    private void reloadSG(int saveNum) {
        if (saveNum <= 4 && saveNum > 0) {
            File file = new File("D:\\MineSweeper_Save_1\\save_0" + saveNum + ".txt");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String basic = reader.readLine();
                if (basic != null) {
                    String RandC[] = basic.trim().split(" ");
                    int r = Integer.parseInt(RandC[0]);
                    int c = Integer.parseInt(RandC[1]);
                    int mine = Integer.parseInt(RandC[2]);
                    basic = reader.readLine();
                    basic = reader.readLine();

                    SinglePlayer New = new SinglePlayer(r, c, mine,imagePath);

                    int space[][] = new int[r][c];
                    RandC = basic.trim().split(" ");
                    for (int i = 0, k = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            New.space[i][j] = Integer.parseInt(RandC[k]);
                            k++;
                        }
                    }

                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    if (RandC.length > 1) for (int i = 0; i < RandC.length; i++) {
                        int R = Integer.parseInt(RandC[i].trim().split(",")[0]);
                        int C = Integer.parseInt(RandC[i].trim().split(",")[1]);
                        New.Open(R, C);
                    }

                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    if (RandC.length > 1)
                        for (int i = 0; i < RandC.length; i++) {
                            int R = Integer.parseInt(RandC[i].trim().split(",")[0]);
                            int C = Integer.parseInt(RandC[i].trim().split(",")[1]);
                            New.setFlag(R, C, "");
                        }


                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    New.unopened = Integer.parseInt(RandC[0]);
                    New.opened = Integer.parseInt(RandC[1]);
                    New.flagCnt = Integer.parseInt(RandC[2]);
                    New.secs = Integer.parseInt(RandC[3]);

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }

        }


    }


    private void reloadDG(int saveNum) {
        if (saveNum <= 4 && saveNum > 0) {
            File file = new File("D:\\MineSweeper_Save_2\\save_0" + saveNum + ".txt");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String basic = reader.readLine();
                if (basic != null) {
                    String RandC[] = basic.trim().split(" ");
                    int r = Integer.parseInt(RandC[0]);
                    int c = Integer.parseInt(RandC[1]);
                    int mine = Integer.parseInt(RandC[2]);
                    basic = reader.readLine();
                    basic = reader.readLine();

                    DoublePlayer New = new DoublePlayer(r, c, mine,imagePath);

                    int space[][] = new int[r][c];
                    RandC = basic.trim().split(" ");
                    for (int i = 0, k = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            New.space[i][j] = Integer.parseInt(RandC[k]);
                            k++;
                        }
                    }

                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    if (RandC.length > 1) for (int i = 0; i < RandC.length; i++) {
                        int R = Integer.parseInt(RandC[i].trim().split(",")[0]);
                        int C = Integer.parseInt(RandC[i].trim().split(",")[1]);
                        New.Open(R, C);
                    }

                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    if (RandC.length > 1)
                        for (int i = 0; i < RandC.length; i++) {
                            int R = Integer.parseInt(RandC[i].trim().split(",")[0]);
                            int C = Integer.parseInt(RandC[i].trim().split(",")[1]);
                            New.setFlag(R, C);
                        }


                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    New.unopened = Integer.parseInt(RandC[0]);
                    New.opened = Integer.parseInt(RandC[1]);
                    New.flagCnt = Integer.parseInt(RandC[2]);
                    New.secs = Integer.parseInt(RandC[3]);

                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    New.playerCode = RandC[0];
                    New.UNClick = Integer.parseInt(RandC[1]);

                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    New.player1Score = Integer.parseInt(RandC[0]);
                    New.player1False = Integer.parseInt(RandC[1]);
                    New.player1UN = Integer.parseInt(RandC[2]);

                    basic = reader.readLine();
                    RandC = basic.trim().split(" ");
                    New.player2Score = Integer.parseInt(RandC[0]);
                    New.player2False = Integer.parseInt(RandC[1]);
                    New.player2UN = Integer.parseInt(RandC[2]);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();

        if (btn.equals(save_01)) {
            if (playerCase == 1) reloadSG(1);
            if (playerCase == 2) reloadDG(1);
            flash(playerCase);
        }
        if (btn.equals(save_02)) {
            if (playerCase == 1) reloadSG(2);
            if (playerCase == 2) reloadDG(2);
            flash(playerCase);
        }
        if (btn.equals(save_03)) {
            if (playerCase == 1) reloadSG(3);
            if (playerCase == 2) reloadDG(3);
            flash(playerCase);
        }
        if (btn.equals(save_04)) {
            if (playerCase == 1) reloadSG(4);
            if (playerCase == 2) reloadDG(4);
            flash(playerCase);
        }

    }
}
