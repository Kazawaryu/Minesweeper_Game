import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class InitialFrame implements KeyListener {
    ImageIcon background;
    ImageIcon backPlayer1;
    ImageIcon backPlayer2;
    JFrame Initial = new JFrame();
    JFrame frame = new JFrame();
    JLabel label1;
    JLabel label2 = new JLabel();

    JButton singlePlayer = new JButton("模擬訓練");
    JButton doublePlayer = new JButton("使徒襲来");
    JButton AiPlayer = new JButton("対戦MAGI");

    JButton easyModel = new JButton("初級");
    JButton mediumModel = new JButton("庸常");
    JButton hardModel = new JButton("極上");
    JButton playerSetModel = new JButton("自定义模式");
    JButton Load = new JButton("LCL存儲");
    JButton HackerJunit = new JButton();

    public int playerNum;
    public int levelCode;
    public int hackerCode = 0;//0不允许作弊，1允许作弊

    String imagePath;

    public InitialFrame() {
        loadImage();
        label1 = new JLabel(background);
        Initial.setTitle("扫雷");
        Initial.setSize(600, 700);
        Initial.setResizable(false);
        Initial.setLayout(new BorderLayout());
        Initial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Initial.setLocationRelativeTo(null);
        Initial.setVisible(true);
        selectPlayer();
        checkSave();
    }

    public String getImagePath() {
        String classPath = String.valueOf(this.getClass().getResource("/"));
        String[] singlePath = classPath.split("/");
        StringBuilder realPath = new StringBuilder();
        for (int i = 1; i < singlePath.length - 3; i++) {
            realPath.append(singlePath[i]);
            realPath.append("/");
        }
        realPath.append("MineSweeper/Images/");
        return String.valueOf(realPath);
    }

    private void loadImage() {
        String realPath = getImagePath();
        imagePath = String.valueOf(realPath);
        background = new ImageIcon(realPath + "初始界面.png/");
        backPlayer1 = new ImageIcon(realPath + "单人界面.png/");
        backPlayer2 = new ImageIcon(realPath + "双人界面.png/");


    }


    private void checkSave() {
        checkSaves ck = new checkSaves();
        ck.setPath("D:\\MineSweeper_Save_1\\");
        try {
            ck.startSave();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ck.setPath("D:\\MineSweeper_Save_2\\");
        try {
            ck.startSave();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void selectPlayer() {

        label1.add(singlePlayer);
        label1.add(doublePlayer);
        label1.add(HackerJunit);
        label1.add(AiPlayer);
        singlePlayer.setBounds(100, 380, 180, 100);
        doublePlayer.setBounds(320, 380, 180, 100);
        AiPlayer.setBounds(100, 500, 400, 60);
        HackerJunit.setBounds(0, 0, 50, 50);
        HackerJunit.setOpaque(false);
        HackerJunit.setContentAreaFilled(false);
        HackerJunit.setBorderPainted(false);
        HackerJunit.setBounds(0, 0, 50, 50);
        HackerJunit.setOpaque(false);
        HackerJunit.setContentAreaFilled(false);
        HackerJunit.setBorderPainted(false);


        singlePlayer.setContentAreaFilled(false);
        doublePlayer.setContentAreaFilled(false);
        AiPlayer.setContentAreaFilled(false);
        Font font = new Font("FC平成極太明朝体", Font.BOLD, 32);
        singlePlayer.setForeground(Color.WHITE);
        doublePlayer.setForeground(Color.WHITE);
        AiPlayer.setForeground(Color.WHITE);
        singlePlayer.setFont(font);
        doublePlayer.setFont(font);
        AiPlayer.setFont(font);

        AiPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AiModel();

                Initial.dispose();
                frame.setTitle("扫雷");
                frame.setSize(600, 700);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.add(label2);
            }
        });
        singlePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerNum = 1;

                selectDifficulty();
                Initial.dispose();
                frame.setTitle("扫雷");
                frame.setSize(600, 700);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.add(label2);
            }
        });
        doublePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerNum = 2;

                selectDifficulty();

                Initial.dispose();
                frame.setTitle("扫雷");
                frame.setSize(600, 700);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.add(label2);
            }
        });
        HackerJunit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "作弊开关已开启！此模式下无法存档！", "注意", JOptionPane.PLAIN_MESSAGE);
                hackerCode = 1;
            }
        });
        label1.setBackground(Color.white);

        Initial.add(label1);

    }

    private void AiModel() {
        label2.setIcon(background);
        label2.setBackground(Color.gray);
        label2.add(easyModel);
        label2.add(mediumModel);
        label2.add(hardModel);
        easyModel.setText("MELCHIOR");
        mediumModel.setText("BALTHASAR");
        hardModel.setText("CASPER");

        easyModel.setBounds(80, 450, 140, 60);
        mediumModel.setBounds(230, 450, 140, 60);
        hardModel.setBounds(380, 450, 140, 60);

        Font font = new Font("FC平成極太明朝体", Font.BOLD, 16);

        easyModel.setFont(font);
        mediumModel.setFont(font);
        hardModel.setFont(font);

        easyModel.setForeground(Color.WHITE);
        mediumModel.setForeground(Color.WHITE);
        hardModel.setForeground(Color.WHITE);

        easyModel.setContentAreaFilled(false);
        mediumModel.setContentAreaFilled(false);
        hardModel.setContentAreaFilled(false);


        easyModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AiModel m = new AiModel(9, 9, 10);
                m.setHardCode(1);
            }
        });

        mediumModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AiModel m = new AiModel(16, 16, 40);
                m.setHardCode(2);
            }
        });

        hardModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AiModel m = new AiModel(16, 30, 99);
                m.setHardCode(3);
            }
        });
    }

    private void selectDifficulty() {
        if (playerNum == 1) label2.setIcon(backPlayer1);
        if (playerNum == 2) label2.setIcon(backPlayer2);
        label2.setBackground(Color.gray);
        label2.add(easyModel);
        label2.add(mediumModel);
        label2.add(hardModel);
        label2.add(playerSetModel);
        label2.add(Load);
        easyModel.setBounds(100, 400, 80, 60);
        mediumModel.setBounds(260, 400, 80, 60);
        hardModel.setBounds(420, 400, 80, 60);
        playerSetModel.setBounds(100, 500, 180, 60);
        Load.setBounds(320, 500, 180, 60);

        Font font = new Font("FC平成極太明朝体", Font.BOLD, 16);

        easyModel.setFont(font);
        mediumModel.setFont(font);
        hardModel.setFont(font);
        playerSetModel.setFont(font);
        Load.setFont(font);

        easyModel.setForeground(Color.WHITE);
        mediumModel.setForeground(Color.WHITE);
        hardModel.setForeground(Color.WHITE);
        playerSetModel.setForeground(Color.WHITE);
        Load.setForeground(Color.white);

        easyModel.setContentAreaFilled(false);
        mediumModel.setContentAreaFilled(false);
        hardModel.setContentAreaFilled(false);
        playerSetModel.setContentAreaFilled(false);
        Load.setContentAreaFilled(false);


        easyModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (playerNum == 1) {
                    SinglePlayer n = new SinglePlayer(9, 9, 10, imagePath);
                    if (hackerCode == 1) n.setHackerCode();
                }
                if (playerNum == 2) {
                    DoublePlayer n = new DoublePlayer(9, 9, 10, imagePath);
                    if (hackerCode == 1) n.setHackerCode();
                }
                levelCode = 1;
                System.out.println("Player:" + playerNum + "\nLevel:" + levelCode);
            }
        });

        mediumModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (playerNum == 1) {
                    SinglePlayer n = new SinglePlayer(16, 16, 40, imagePath);
                    if (hackerCode == 1) n.setHackerCode();
                }
                if (playerNum == 2) {
                    DoublePlayer n = new DoublePlayer(16, 16, 40, imagePath);
                    if (hackerCode == 1) n.setHackerCode();
                }
                levelCode = 2;
                System.out.println("Player:" + playerNum + "\nLevel:" + levelCode);
            }
        });

        hardModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (playerNum == 1) {
                    SinglePlayer n = new SinglePlayer(16, 30, 99, imagePath);
                    if (hackerCode == 1) n.setHackerCode();
                }
                if (playerNum == 2) {
                    DoublePlayer n = new DoublePlayer(16, 30, 99, imagePath);
                    if (hackerCode == 1) n.setHackerCode();
                }
                levelCode = 3;
                System.out.println("Player:" + playerNum + "\nLevel:" + levelCode);
            }
        });

        playerSetModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlayerSetModel();
                levelCode = 4;
                System.out.println("Player:" + playerNum + "\nLevel:" + levelCode);

            }
        });

        Load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Loading");
                Loading();
            }
        });
    }

    private void PlayerSetModel() {
        JButton sure = new JButton();
        JTextField textField = new JTextField(); // 创建一个单行输入框
        textField.setEditable(true); // 设置输入框允许编辑
        textField.setColumns(11); // 设置输入框的长度为11个字符
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        JFrame frame2 = new JFrame();

        sure.setText("确定");
        sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                if (textField.getText() == null) {
                    JOptionPane.showMessageDialog(frame2, "还什么都没输入呢", "？？？", JOptionPane.PLAIN_MESSAGE);
                } else {
                    String[] ss = s.split(",");
                    if (ss.length != 3) {
                        JOptionPane.showMessageDialog(frame2, "依此输入行数，列数和雷数；并用','符号隔开", "非法输入", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        int r = Integer.parseInt(ss[0]);
                        int c = Integer.parseInt(ss[1]);
                        int mine = Integer.parseInt(ss[2]);

                        if (r <= 24 && c <= 30 && mine <= 0.5 * r * c) {
                            frame2.dispose();
                            frame.dispose();

                            if (playerNum == 1) {
                                SinglePlayer n = new SinglePlayer(r, c, mine, imagePath);
                                if (hackerCode == 1) n.setHackerCode();
                            }
                            if (playerNum == 2) {
                                DoublePlayer n = new DoublePlayer(r, c, mine, imagePath);
                                if (hackerCode == 1) n.setHackerCode();
                            }

                        } else {
                            JOptionPane.showMessageDialog(frame2, "行数须小于24，列数须小于30，雷数不能超过行列数积的一半", "注意", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
            }
        });

        label.add(textField); // 在面板上添加单行输入框
        label.add(sure);
        textField.setBounds(40, 0, 100, 25);
        sure.setBounds(60, 25, 60, 25);

        frame2.setTitle("请输入");
        frame2.setSize(200, 100);
        frame2.setResizable(false);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setLayout(new BorderLayout());
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        frame2.add(label);
    }

    private void Loading() {
        if (playerNum == 1) {
            Reload sg = new Reload(imagePath);
            sg.path = "D:\\MineSweeper_Save_1\\save_0";
            sg.playerCase = 1;
            sg.setLoading();
        }
        if (playerNum == 2) {
            Reload dg = new Reload(imagePath);
            dg.path = "D:\\MineSweeper_Save_2\\save_0";
            dg.playerCase = 2;
            dg.setLoading();
        }


    }

    public static void main(String[] args) {
        new InitialFrame();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
