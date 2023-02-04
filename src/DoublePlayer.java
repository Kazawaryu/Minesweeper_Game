import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class DoublePlayer implements ActionListener, MouseListener {
    //数据结构
    int ROW;
    int COL;
    int[][] space;
    JButton[][] button;
    int mineCode = -1;//雷标记
    int mineCount;//雷数
    int unopened = ROW * COL;//点的雷数
    int mineRight;//正确标记的雷数
    int flagCnt = 0;//插的旗子
    int opened = 0;//打开的格子


    int secs = 0;//计时
    String playerCode;//玩家代码
    int saveNum = 0;//存档代号
    public int hackerCode = 0;//0不允许作弊，1允许作弊
    boolean Load = false;//是否载入
    ArrayList<String> leftClicked = new ArrayList();
    ArrayList<String> rightClicked = new ArrayList();

    int[] startXY = {-1, -1};//首次点击

    JFrame frame = new JFrame();
    ImageIcon banner1;
    ImageIcon banner2;
    ImageIcon banner3;
    ImageIcon banner4;
    Font font = new Font("方正姚体", Font.PLAIN, 32);
    JButton bannerBtn = new JButton();

    JLabel label1 = new JLabel("剩余： " + unopened);
    JLabel label2 = new JLabel("已开： " + opened);
    JLabel label3 = new JLabel("用时： " + secs + "s");
    JLabel label4 = new JLabel();
    JLabel label5 = new JLabel("你用" + secs + "秒战胜了晶哥！");

    JButton playerSwitch = new JButton();

    int player1Score = 0;
    int player2Score = 0;
    int player1False = 0;
    int player2False = 0;
    int UNClick = 0;
    int player1UN = 0;
    int player2UN = 0;

    JLabel player1 = new JLabel("初号机_得分 / 失误：" + player1Score + " / " + player1False);
    JLabel player2 = new JLabel("零号机_得分 / 失误：" + player2Score + " / " + player2False);

    JLabel unClick = new JLabel("余步：" + UNClick);

    Timer timer = new Timer(1000, this);

    String imagePath;

//    public static void main(String[] args) {
//        new DoublePlayer(16, 16, 20);
//    }

    public DoublePlayer(int row, int col, int minecnt, String imagePath) {
        ROW = row;
        COL = col;
        mineCount = minecnt;
        space = new int[ROW][COL];    //每个格子雷的信息
        button = new JButton[ROW][COL];    //一个按钮代表一个块
        unopened = COL * ROW;
        playerCode = "NotBegin";
        this.imagePath = imagePath;
        loadIamge(imagePath);

        frame.setTitle("EVA扫雷");
        frame.setSize(COL * 40, ROW * 40 + 100);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        setHeader();

        setButton();

        timer.start();
        frame.setVisible(true);

    }//总方法

    private void loadIamge(String path) {
        banner1 = new ImageIcon(path + "/banner1.png");
        banner2 = new ImageIcon(path + "/banner2.png");
        banner3 = new ImageIcon(path + "/banner3.png");
        banner4 = new ImageIcon(path + "/banner4.png");
    }

    private void setButton() {
        Container con = new Container();
        con.setLayout(new GridLayout(ROW, COL));

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                JButton btn = new JButton();
                btn.addActionListener(this);
                btn.setMargin(new Insets(0, 0, 0, 0));
                con.add(btn);
                btn.addMouseListener(this);
                button[i][j] = btn;

            }
        }

        frame.add(con, BorderLayout.CENTER);

    }

    private void setHeader() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c1 = new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(bannerBtn, c1);

        if (ROW == 9 && COL == 9) bannerBtn.setIcon(banner1);
        else if (ROW == 16 && COL == 16) bannerBtn.setIcon(banner2);
        else if (ROW == 16 && COL == 30) bannerBtn.setIcon(banner3);
        else bannerBtn.setIcon(banner4);


        bannerBtn.addMouseListener(this);
        bannerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });

        label1.setOpaque(true);
        label1.setBackground(Color.WHITE);
        label1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        label2.setOpaque(true);
        label2.setBackground(Color.WHITE);
        label2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        label3.setOpaque(true);
        label3.setBackground(Color.WHITE);
        label3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        GridBagConstraints c2 = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(label1, c2);
        GridBagConstraints c3 = new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(label2, c3);
        GridBagConstraints c4 = new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(label3, c4);

        player1.setOpaque(true);
        player1.setBackground(Color.WHITE);
        player1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        player2.setOpaque(true);
        player2.setBackground(Color.WHITE);
        player2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        unClick.setOpaque(true);
        unClick.setBackground(Color.WHITE);
        unClick.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        playerSwitch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print(playerCode + "-->");
                changePlayer();
            }
        });

        GridBagConstraints c5 = new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(player1, c5);
        GridBagConstraints c6 = new GridBagConstraints(2, 3, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(unClick, c6);
        GridBagConstraints c7 = new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(player2, c7);
        GridBagConstraints c8 = new GridBagConstraints(2, 4, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(playerSwitch, c8);


        frame.add(panel, BorderLayout.NORTH);

    }

    private void addMine() {
        //加雷
        Random rand = new Random();
        for (int i = 0; i < mineCount; ) {
            int r = rand.nextInt(ROW);    //产生0-20整数
            int c = rand.nextInt(COL);    //产生0-20整数

            if (space[r][c] != mineCode) {
                //第一个开的格子周围不能有雷,直接跳过
                if (r == startXY[0] - 1 && c >= startXY[1] - 1 && c <= startXY[1] + 1) continue;
                if (r == startXY[0] && c >= startXY[1] - 1 && c <= startXY[1] + 1) continue;
                if (r == startXY[0] + 1 && c >= startXY[1] - 1 && c <= startXY[1] + 1) continue;

                space[r][c] = mineCode;
                i++;
            }
        }

        //计算周围雷数量
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (space[i][j] == mineCode) continue;
                int tempCnt = 0;
                if (i > 0 && j > 0 && space[i - 1][j - 1] == mineCode) tempCnt++;
                if (i > 0 && space[i - 1][j] == mineCode) tempCnt++;
                if (i > 0 && j < COL - 1 && space[i - 1][j + 1] == mineCode) tempCnt++;
                if (j > 0 && space[i][j - 1] == mineCode) tempCnt++;
                if (j < COL - 1 && space[i][j + 1] == mineCode) tempCnt++;
                if (i < ROW - 1 && j > 0 && space[i + 1][j - 1] == mineCode) tempCnt++;
                if (i < ROW - 1 && space[i + 1][j] == mineCode) tempCnt++;
                if (i < ROW - 1 && j < COL - 1 && space[i + 1][j + 1] == mineCode) tempCnt++;
                if (tempCnt > 8) addMine();
                space[i][j] = tempCnt;
            }
        }
    }

    public void judge() {
        //a. 如果双方的分数差距大于游戏区中未揭晓的雷数，则直接判定优势方获胜。
        int deltaScore = Math.abs(player1Score - player2Score);
        if (deltaScore > mineCount - mineRight) {
            timer.stop();
            if (player1Score > player2Score)
                JOptionPane.showMessageDialog(frame, "初号机获胜！", "恭喜！", JOptionPane.PLAIN_MESSAGE);
            else
                JOptionPane.showMessageDialog(frame, "零号机获胜！", "恭喜！", JOptionPane.PLAIN_MESSAGE);
        }

        //b. 如果在游戏中所有雷都被揭晓时双方分数依然相同，则失误数少的一方（失误包含误触雷以及标记错误）获胜。
        if (deltaScore == 0 && player1False + player2False + mineRight == mineCount) {
            timer.stop();
            if (player1False + player1UN < player2False + player2UN)
                JOptionPane.showMessageDialog(frame, "初号机获胜！用时" + secs + "s", "恭喜！", JOptionPane.PLAIN_MESSAGE);
            if (player1False + player1UN > player2False + player2UN)
                JOptionPane.showMessageDialog(frame, "零号机获胜！用时" + secs + "s", "恭喜！", JOptionPane.PLAIN_MESSAGE);

            //c. 如果失误数依然相同，则双方平局。
            if (player1False + player1UN == player2False + player2UN)
                JOptionPane.showMessageDialog(frame, "平局了！用时" + secs + "s", "恭喜！", JOptionPane.PLAIN_MESSAGE);
        }


    }

    private void restart() {

        unopened = COL * ROW;
        opened = 0;
        secs = 0;
        flagCnt = 0;
        startXY[0] = -1;
        startXY[1] = -1;
        player1Score = 0;
        player2Score = 0;
        player1False = 0;
        player2False = 0;
        playerCode = "NotBegin";

        Load = false;

        label1.setText("剩余： " + unopened);
        label2.setText("已开： " + opened);
        label3.setText("用时： " + secs + "s");

        player1.setText("初号机_得分/失误：" + player1Score + " / " + player1False);
        player2.setText("零号机_得分/失误：" + player2Score + " / " + player2False);
        unClick.setText("余步：" + UNClick);

        System.out.println(COL + "&" + COL + "&" + mineCount);
        frame.dispose();
        new DoublePlayer(ROW, COL, mineCount, imagePath);
    }

    private void updateCnt() {
        opened++;
        unopened--;
        label1.setText("待开：" + unopened);
        label2.setText("已开：" + opened);
    }

    public void Open(int i, int j) {

        JButton btn = button[i][j];
        if (!btn.isEnabled()) return;

        btn.setIcon(null);
        btn.setEnabled(false);
        if (space[i][j] != 0)
            btn.setText(space[i][j] + "");
        else btn.setText("");

        updateCnt();

        updateScore();
        //是否为空格子且周围没有雷
        if (space[i][j] == 0) {
            if (i > 0 && j > 0) Open(i - 1, j - 1);
            if (i > 0) Open(i - 1, j);
            if (i > 0 && j < COL - 1) Open(i - 1, j + 1);
            if (j > 0) Open(i, j - 1);
            if (j < COL - 1) Open(i, j + 1);
            if (i < ROW - 1 && j > 0) Open(i + 1, j - 1);
            if (i < ROW - 1) Open(i + 1, j);
            if (i < ROW - 1 && j < COL - 1) Open(i + 1, j + 1);
        }
    }

    public void setFlag(int i, int j) {
        if (flagCnt >= mineCount)
            JOptionPane.showMessageDialog(frame, "插旗数不能大于雷数！", "旗用完了...", JOptionPane.PLAIN_MESSAGE);
        else {
            JButton btn = button[i][j];
            if (btn.isEnabled()) {

                if (space[i][j] == mineCode) {
                    if (playerCode.equals("P1")) player1Score++;
                    if (playerCode.equals("P2")) player2Score++;
                    mineRight++;

                } else {
                    if (playerCode.equals("P1")) player1UN++;
                    if (playerCode.equals("P2")) player2UN++;
                }
                btn.setBackground(new Color(255, 200, 200));
                btn.setEnabled(false);
                btn.setText("F");
                flagCnt++;
            }
            updateScore();
        }
    }

    public void changePlayer() {
        if (playerCode.equals("P1")) playerCode = "P2";
        else playerCode = "P1";
        System.out.print(playerCode + "\n");
        setClick();
    }

    public void updateScore() {
        player1.setText("初号机_得分/失误：" + player1Score + " / " + player1False);
        player2.setText("零号机_得分/失误：" + player2Score + " / " + player2False);
        unClick.setText("余步：" + UNClick);
    }

    private void setClick() {
        JButton sure = new JButton();
        JTextField textField = new JTextField(); // 创建一个单行输入框
        textField.setEditable(true); // 设置输入框允许编辑
        textField.setColumns(11); // 设置输入框的长度为11个字符
        JLabel label = new JLabel();
        JFrame frame2 = new JFrame();

        sure.setText(playerCode);
        sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int s = Integer.parseInt(textField.getText());
                if (s <= 5 && s >= 1) {
                    UNClick = s;
                    frame2.dispose();
                    updateScore();
                } else
                    JOptionPane.showMessageDialog(frame2, "你只能走1~5步！", "FBI WARNING", JOptionPane.PLAIN_MESSAGE);

            }
        });

        label.add(textField); // 在面板上添加单行输入框
        label.add(sure);
        textField.setBounds(40, 0, 100, 25);
        sure.setBounds(60, 25, 60, 25);

        frame2.setTitle("请输入步数");
        frame2.setSize(200, 100);
        frame2.setResizable(false);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setLayout(new BorderLayout());
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        frame2.add(label);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            secs++;
            label3.setText("用时: " + secs + "s");
            label4.setText("<html>晶哥用" + secs + "秒把你橄榄了！" + "<br>点流汗黄豆重开吧</html>");
            timer.start();
            return;
        }

        JButton btn = (JButton) e.getSource();

        if (playerCode.equals("NotBegin")) {
            JOptionPane.showMessageDialog(frame, "点击按钮并输入步数", "请注意", JOptionPane.PLAIN_MESSAGE);
        } else if (UNClick == 0) {
            JOptionPane.showMessageDialog(frame, "该换操作员了！", "不合法操作！", JOptionPane.PLAIN_MESSAGE);
        } else {
            UNClick--;
            updateScore();
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (btn.equals(button[i][j])) {
                        //leftClicked.add(String.format("%d,%d", i, j));
                        if (opened == 0) {
                            startXY[0] = i;
                            startXY[1] = j;
                            if (!Load)
                                addMine();
                            Open(i, j);

                        } else if (space[i][j] == mineCode) {
                            Open(i, j);

                            if (playerCode.equals("P1")) {
                                if (player1Score > 0) player1Score--;
                                player1False++;
                            }
                            if (playerCode.equals("P2")) {
                                if (player2Score > 0) player2Score--;
                                player2False++;
                            }
                            updateScore();
                        } else {
                            Open(i, j);
                            updateScore();
                        }
                        return;
                    }
                }
            }
        }
        judge();


        if (save_01.equals(btn)) {
            updateLandR();
            UNClick++;
            Save ds = new Save();
            ds.saveNum = 1;
            ds.doubleSave(this);
            saveLoading.dispose();
            unClick.setText("余步：" + UNClick);
            flash();
        }
        if (save_02.equals(btn)) {
            updateLandR();
            UNClick++;
            Save ds = new Save();
            ds.saveNum = 2;
            ds.doubleSave(this);
            saveLoading.dispose();
            unClick.setText("余步：" + UNClick);
            flash();
        }
        if (save_03.equals(btn)) {
            updateLandR();
            UNClick++;
            Save ds = new Save();
            ds.saveNum = 3;
            ds.doubleSave(this);
            saveLoading.dispose();
            unClick.setText("余步：" + UNClick);
            flash();
        }
        if (save_04.equals(btn)) {
            updateLandR();
            UNClick++;
            Save ds = new Save();
            ds.saveNum = 4;
            ds.doubleSave(this);
            saveLoading.dispose();
            unClick.setText("余步：" + UNClick);
            flash();
        }

    }

    public void HackerMode() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (space[i][j] == mineCode) {
                    button[i][j].setBackground(Color.orange);
                }
            }
        }
        label5.setText("超级瞄准已部署");
        label5.setFont(font);
        JOptionPane.showMessageDialog(frame, label5, "检测模式", JOptionPane.PLAIN_MESSAGE);
        setHackerCode();
    }

    public void setHackerCode() {
        hackerCode = 1;
    }

    private void updateLandR() {
        ArrayList left = new ArrayList();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if ((!button[i][j].isEnabled()) && (!button[i][j].getText().equals("F")))
                    left.add(String.format("%d,%d", i, j));
            }
        }
        leftClicked = left;

        ArrayList right = new ArrayList();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (button[i][j].getText().equals("F"))
                    right.add(String.format("%d,%d", i, j));
            }
        }

        rightClicked = right;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton btn = (JButton) e.getSource();

        if (btn == bannerBtn && e.getButton() == MouseEvent.BUTTON3) {
            if (hackerCode == 1) HackerMode();
            if (hackerCode == 0) setLoading();
        }
        if (btn == playerSwitch && e.getButton() == MouseEvent.BUTTON3) {
            // new FileSave().save(this);
        }
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (btn.equals(button[i][j])) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        if (playerCode.equals("NotBegin")) {
                            JOptionPane.showMessageDialog(frame, "点击按钮并输入步数", "请注意", JOptionPane.PLAIN_MESSAGE);
                        } else if (UNClick == 0) {
                            JOptionPane.showMessageDialog(frame, "该换人了！", "不合规操作！", JOptionPane.PLAIN_MESSAGE);
                        } else if (UNClick != 0) {
                            setFlag(i, j);
                            UNClick--;
                            //rightClicked.add(String.format("%d,%d", i + 1, j + 1));
                        }
                    }
                }
            }
        }
        updateScore();
        judge();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }


    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }


    JFrame saveLoading = new JFrame();

    JButton save_01 = new JButton();
    JButton save_02 = new JButton();
    JButton save_03 = new JButton();
    JButton save_04 = new JButton();

    JLabel s1Text = new JLabel();
    JLabel s2Text = new JLabel();
    JLabel s3Text = new JLabel();
    JLabel s4Text = new JLabel();

    public void flash() {
        s1Text.setText("<html>模式： 双人模式<br>难度： " + getLevel(1) + "<br>存档时间：" + getTime(1));
        s2Text.setText("<html>模式： 双人模式<br>难度： " + getLevel(2) + "<br>存档时间：" + getTime(2));
        s3Text.setText("<html>模式： 双人模式<br>难度： " + getLevel(3) + "<br>存档时间：" + getTime(3));
        s4Text.setText("<html>模式： 双人模式<br>难度： " + getLevel(4) + "<br>存档时间：" + getTime(4));
    }

    public static String getLevel(int saveNum) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\MineSweeper_Save_2\\save_0" + saveNum + ".txt"));
            String basic = reader.readLine();
            if (basic != null) {
                String level[] = basic.trim().split(" ");
                int[] a = new int[level.length];
                for (int i = 0; i < level.length; i++) {
                    a[i] = Integer.parseInt(level[i]);
                }

                if (a[0] == 9 && a[1] == 9) return "简单难度";
                if (a[0] == 16 && a[1] == 16) return "中等难度";
                if (a[0] == 16 && a[1] == 30) return "困难难度";
                return "自定义模式";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "空存档";
    }

    public static String getTime(int saveNum) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\MineSweeper_Save_2\\save_0" + saveNum + ".txt"));
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
        Reload.LoadingItem(saveLoading);

        flash();
        setSave();
    }
}