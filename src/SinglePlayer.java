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

public class SinglePlayer implements ActionListener, MouseListener {
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
    int hackerCode;//0不允许作弊，1允许作弊

    ArrayList<String> leftClicked = new ArrayList();
    ArrayList<String> rightClicked = new ArrayList();


    int[] startXY = {-1, -1};//首次点击

    JFrame frame = new JFrame();
    ImageIcon banner1 ;
    ImageIcon banner2 ;
    ImageIcon banner3 ;
    ImageIcon banner4 ;
    Font font = new Font("方正姚体", Font.PLAIN, 32);


    JButton bannerBtn = new JButton();
    JLabel label1 = new JLabel("剩余： " + unopened);
    JLabel label2 = new JLabel("已开： " + opened);
    JLabel label3 = new JLabel("用时： " + secs + "s");
    JLabel label4 = new JLabel("<html>你被晶哥用" + secs + "秒橄榄了！" + "<br>点流汗黄豆重开吧</html>");
    JLabel label5 = new JLabel("你用" + secs + "秒战胜了晶哥！");

    Timer timer = new Timer(1000, this);

    String imagePath;
//    public static void main(String[] args) {
//        new SinglePlayer(16, 16, 10);
//    }

    public SinglePlayer(int row, int col, int minecnt,String imagePath) {
        ROW = row;
        COL = col;
        mineCount = minecnt;
        space = new int[ROW][COL];    //每个格子雷的信息
        button = new JButton[ROW][COL];    //一个按钮代表一个块
        unopened = COL * ROW;
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

        if (ROW==9 && COL == 9 ) bannerBtn.setIcon(banner1);
        else if (ROW==16 && COL == 16 ) bannerBtn.setIcon(banner2);
        else if (ROW==16 && COL == 30 ) bannerBtn.setIcon(banner3);
        else  bannerBtn.setIcon(banner4);

        bannerBtn.addMouseListener(this);
        bannerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hackerCode == 1) {
                    restart();
                    setHackerCode();
                } else restart();
                System.out.println(hackerCode);
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

        label4.setFont(font);
        timer.stop();
        JOptionPane.showMessageDialog(frame, label4, "错误", JOptionPane.PLAIN_MESSAGE);
    }

    private void judge() {
        if (unopened + mineRight == mineCount) {
            timer.stop();
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (button[i][j].isEnabled()){
//                        button[i][j].setIcon(winFlagIcon);
                        continue;
                    }
                }
            }
            label5.setText("你用" + secs + "秒战胜了模拟数据！");

            label5.setFont(font);
            JOptionPane.showMessageDialog(frame, label5, "测试成功！", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void restart() {

        unopened = COL * ROW;
        opened = 0;
        secs = 0;    //时间
        flagCnt = 0;
        startXY[0] = -1;
        startXY[1] = -1;


        label1.setText("剩余： " + unopened);
        label2.setText("已开： " + opened);
        label3.setText("用时： " + secs + "s");

        System.out.println(COL + "&" + COL + "&" + mineCount);
        frame.dispose();
        new SinglePlayer(ROW, COL, mineCount, imagePath);
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

    public void setHackerCode() {
        hackerCode = 1;
    }

    private void updateLandR(){
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
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof Timer) {
            secs++;
            label3.setText("用时: " + secs + "s");
            label4.setText("<html>模拟数据用" + secs + "秒突破了防线！" + "<br>点击上方按钮再次模拟</html>");
            timer.start();
            return;
        }


        JButton btn = (JButton) e.getSource();

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (btn.equals(button[i][j])) {
                    //leftClicked.add(String.format("%d,%d", i + 1, j + 1));
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
                    }
                    return;
                }
            }
        }

        if (save_01.equals(btn)) {
            updateLandR();
            Save sv = new Save();
            sv.saveNum = 1;
            sv.singleSave(this);
            saveLoading.dispose();
            flash();
        }
        if (save_02.equals(btn)) {
            updateLandR();
            Save sv = new Save();
            sv.saveNum = 2;
            sv.singleSave(this);
            saveLoading.dispose();
            flash();
        }
        if (save_03.equals(btn)) {
            updateLandR();
            Save sv = new Save();
            sv.saveNum = 3;
            sv.singleSave(this);
            saveLoading.dispose();
            flash();
        }
        if (save_04.equals(btn)) {
            updateLandR();
            Save sv = new Save();
            sv.saveNum = 4;
            sv.singleSave(this);
            saveLoading.dispose();
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
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton btn = (JButton) e.getSource();

        if (btn == bannerBtn && e.getButton() == MouseEvent.BUTTON3) {
            if (hackerCode == 1) HackerMode();
            if (hackerCode == 0) setSaving();
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
            label5.setText("你用" + secs + "秒战胜了模拟使徒！");

            label5.setFont(font);
            JOptionPane.showMessageDialog(frame, label5, "测试通过！", JOptionPane.PLAIN_MESSAGE);
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
        s1Text.setText("<html>模式： 单人模式<br>难度： " + getLevel(1) + "<br>存档时间：" + getTime(1));
        s2Text.setText("<html>模式： 单人模式<br>难度： " + getLevel(2) + "<br>存档时间：" + getTime(2));
        s3Text.setText("<html>模式： 单人模式<br>难度： " + getLevel(3) + "<br>存档时间：" + getTime(3));
        s4Text.setText("<html>模式： 单人模式<br>难度： " + getLevel(4) + "<br>存档时间：" + getTime(4));
    }

    public static String getLevel(int saveNum) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\MineSweeper_Save_1\\save_0" + saveNum + ".txt"));
            String basic = reader.readLine();
            if (basic != null) {
                String level[] = basic.trim().split(" ");
                int[] a = new int[level.length];
                for (int i = 0; i < level.length; i++) {
                    a[i] = Integer.parseInt(level[i]);
                }

                if (a[0] == 9 && a[1] == 9 && a[2] == 10) return "简单难度";
                if (a[0] == 16 && a[1] == 16 && a[2] == 40) return "中等难度";
                if (a[0] == 16 && a[1] == 30 && a[2] == 99) return "困难难度";
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
            BufferedReader reader = new BufferedReader(new FileReader("D:\\MineSweeper_Save_1\\save_0" + saveNum + ".txt"));
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

        return "空存档";
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

    public void setSaving() {
        Reload.LoadingItem(saveLoading);

        flash();
        setSave();
    }

}