import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class AiModel implements ActionListener, MouseListener {
    //数据结构
    int ROW;
    int COL;
    int[][] space;
    JButton[][] button;
    int mineCode = -1;//雷标记
    int mineCount;//雷数
    int unopened = ROW * COL;//点的雷数
    int mineRight;
    int flagCnt = 0;//插的旗子
    int opened = 0;//打开的格子
    int secs = 0;//计时

    public void setHardCode(int hardCode) {
        this.hardCode = hardCode;
    }

    int hardCode = 0;//Ai难度

    boolean action = false;
    boolean playerWin = false;

    int[] startXY = {-1, -1};//首次点击

    JFrame frame = new JFrame();
    ImageIcon banner1 = new ImageIcon("D:\\save_2\\banner1.png");
    ImageIcon banner2 = new ImageIcon("D:\\save_2\\banner2.png");
    ImageIcon banner3 = new ImageIcon("D:\\save_2\\banner3.png");
    ImageIcon banner4 = new ImageIcon("D:\\save_2\\banner4.png");
    Font font = new Font("方正姚体", Font.PLAIN, 32);
    ImageIcon winFlagIcon = new ImageIcon("D:\\saver\\Imagepack\\win_flag.png");

    JButton bannerBtn = new JButton();
    JLabel label1 = new JLabel("剩余： " + unopened);
    JLabel label2 = new JLabel("已开： " + opened);
    JLabel label3 = new JLabel("用时： " + secs + "s");
    JLabel label4 = new JLabel("<html>你被晶哥用" + secs + "秒橄榄了！" + "<br>点流汗黄豆重开吧</html>");
    JLabel label5 = new JLabel("你用" + secs + "秒战胜了晶哥！");

    Timer timer = new Timer(1000, this);

//    public static void main(String[] args) {
//        new AiModel(16, 16, 40);
//    }

    public AiModel(int row, int col, int minecnt) {
        ROW = row;
        COL = col;
        mineCount = minecnt;
        space = new int[ROW][COL];    //每个格子雷的信息
        button = new JButton[ROW][COL];    //一个按钮代表一个块
        unopened = COL * ROW;


        frame.setTitle("扫雷");
        frame.setSize(COL * 40, ROW * 40 + 100);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        setHeader();

        //addMine();

        setButton();

        timer.start();
        frame.setVisible(true);

    }//总方法

    public void easyAi(boolean action) {
        Random rand = new Random();
        if (action) {
            for (int i = 0; i < 1; ) {
                int r = rand.nextInt(ROW);
                int c = rand.nextInt(COL);
                if (button[r][c].isEnabled()) {

                    if (space[r][c] != mineCode) {
                        Open(r, c);
                        i++;
                        judge();
                    }

                    if (space[r][c] == mineCode) {
                        button[r][c].setBackground(Color.MAGENTA);
                        label4.setFont(font);
                        label4.setText("<html>仅仅" + secs + "秒！" + "<br>MAGI出现了bug！</html>");
                        timer.stop();
                        JOptionPane.showMessageDialog(frame, label4, "啊这", JOptionPane.PLAIN_MESSAGE);
                        i++;
                    }
                }
            }
            this.action = false;
        }
    }

    public void mediumAi(boolean action) {
        Random rand = new Random();
        System.out.println("OK");
        if (action) {
            for (int i = 0; i < 1; ) {
                int r = rand.nextInt(ROW);
                int c = rand.nextInt(COL);
                if (button[r][c].isEnabled()) {

                    if (space[r][c] != mineCode) {
                        Open(r, c);
                        i++;
                        judge();
                    }
                }
            }
        }
        this.action = false;
    }


    public void hardAi(boolean action) {
        Random rand = new Random();
        if (action) {
            for (int i = 0; i < 3; ) {
                int r = rand.nextInt(ROW);
                int c = rand.nextInt(COL);
                if (button[r][c].isEnabled()) {

                    if (space[r][c] != mineCode) {
                        Open(r, c);
                        i++;
                        judge();
                    }
                }
            }
        }
        this.action = false;
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

        if (ROW==9 && COL == 9 && mineCount ==10) bannerBtn.setIcon(banner1);
        else if (ROW==16 && COL == 16 && mineCount ==40) bannerBtn.setIcon(banner2);
        else if (ROW==16 && COL == 30 && mineCount ==99) bannerBtn.setIcon(banner3);
        else  bannerBtn.setIcon(banner4);

        bannerBtn.addMouseListener(this);
        bannerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
                System.out.println(hardCode);
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

        GridBagConstraints c2 = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(label1, c2);
        GridBagConstraints c3 = new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(label2, c3);
        GridBagConstraints c4 = new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(label3, c4);

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

    private void end() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (button[i][j].isEnabled()) {
                    JButton btn = button[i][j];
                    if (space[i][j] == mineCode) {
                        button[i][j].setBackground(Color.MAGENTA);
                        /*button[i][j].setEnabled(false);
                        button[i][j].setIcon(mineIcon);
                        button[i][j].setDisabledIcon(mineIcon);*/
                    } else {
                        btn.setIcon(null);
                        btn.setEnabled(false);
                        btn.setOpaque(true);
                        btn.setBackground(Color.CYAN);
                        if (btn.getText().equals("0")) btn.setText("");
                        else btn.setText(space[i][j] + "");
                    }
                }
            }
        }
        action = false;

        label4.setFont(font);
        timer.stop();
        JOptionPane.showMessageDialog(frame, label4, "错误", JOptionPane.PLAIN_MESSAGE);
    }

    private void judge() {
        if (unopened + mineRight == mineCount ) {
            timer.stop();
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (button[i][j].isEnabled()) button[i][j].setIcon(winFlagIcon);
                }
            }
            if (!action){
            label5.setText("你用" + secs + "秒战胜了晶哥！");
            playerWin = true;
            }else label5.setText("晶哥用" + secs + "秒击败了你！");
            System.out.println(action);

            label5.setFont(font);
            JOptionPane.showMessageDialog(frame, label5, "赢得太多了！", JOptionPane.PLAIN_MESSAGE);
            System.out.println(action);
        }
    }

    private void restart() {

        unopened = COL * ROW;
        opened = 0;
        secs = 0;    //时间
        flagCnt = 0;
        startXY[0] = -1;
        startXY[1] = -1;

        action = false;


        label1.setText("剩余： " + unopened);
        label2.setText("已开： " + opened);
        label3.setText("用时： " + secs + "s");

        int Hdc = hardCode;
        System.out.println(COL + "&" + COL + "&" + mineCount);
        frame.dispose();
        AiModel m =new AiModel(ROW, COL, mineCount);
        m.setHardCode(Hdc);

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
        if (space[i][j] != 0) {
            btn.setText(space[i][j] + "");
            if (!action)
                btn.setBackground(Color.yellow);
            if (action)
                btn.setBackground(Color.WHITE);
        } else btn.setText("");


        updateCnt();

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

    public void setFlag(int i, int j, String text) {
        JButton btn = button[i][j];
        //rightClicked.add(String.format("%d,%d", i, j));
        if (Objects.equals(text, "F")) {
            if (space[i][j] == mineCode) mineRight--;
            btn.setBackground(null);
            btn.setEnabled(true);
            btn.setText("");
            flagCnt--;
        } else if (btn.isEnabled()) {
            if (flagCnt >= mineCount)
                JOptionPane.showMessageDialog(frame, "插旗数不能大于雷数！", "旗用完了...", JOptionPane.PLAIN_MESSAGE);
            else {
                if (space[i][j] == mineCode) mineRight++;
                btn.setBackground(new Color(255, 200, 200));
                btn.setEnabled(false);
                btn.setText("F");
                flagCnt++;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof Timer) {
            secs++;
            label3.setText("用时: " + secs + "s");
            label4.setText("<html>MAGI用" + secs + "结束了战斗！" + "<br>点击上方按钮再次模拟</html>");
            timer.start();
            return;
        }


        JButton btn = (JButton) e.getSource();

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (btn.equals(button[i][j])) {
                    if (opened == 0) {
                        startXY[0] = i;
                        startXY[1] = j;
                        addMine();
                        Open(i, j);
                    } else if (space[i][j] == mineCode) {
                        end();
                    } else {
                        Open(i, j);
                        judge();
                        if (!playerWin) {
                            action = true;
                            if (hardCode == 1) easyAi(action);
                            if (hardCode == 2) mediumAi(action);
                            if (hardCode == 3) hardAi(action);
                        }
                        return;
                    }
                }
            }
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
        JOptionPane.showMessageDialog(frame, label5, "注意", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton btn = (JButton) e.getSource();

        if (btn == bannerBtn && e.getButton() == MouseEvent.BUTTON3) {
            HackerMode();
        }
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (btn.equals(button[i][j])) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        setFlag(i, j, btn.getText());
                    }
                }
            }
        }

        if (mineRight == mineCount) {
            timer.stop();
            label5.setText("你用" + secs + "秒战胜了MAGI！");
            Font font = new Font("华文行楷", Font.PLAIN, 32);
            label5.setFont(font);
            JOptionPane.showMessageDialog(frame, label5, "恭喜！", JOptionPane.PLAIN_MESSAGE);
        }
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


}