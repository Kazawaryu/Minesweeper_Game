import java.io.*;
import java.util.Objects;

public class checkSaves {
    private static String path;

    public static void setPath(String path) {
        checkSaves.path = path;
    }

    public void startSave() throws IOException {
        //判断存档文件是否完整
        File file = new File(path);
        String[] files = file.list();
        if (files == null) {
            file.mkdir();
            files = file.list();
            String sets = "attrib +H \"" + file.getAbsolutePath() + "\"";
            System.out.println(sets);
            Runtime.getRuntime().exec(sets);
        }

        for (int i = 1; i <= 4; i++) {
            if (!findSave(i)) {
                createSave(i);
            } else {
                //检查存档是否符合规范
            }
        }


    }

    private boolean findSave(int num) {
        File fSave = new File(path + "\\save_0" + num + ".txt");
        return fSave.exists();
    }


    private boolean createSave(int num) throws IOException {
        boolean flag = false;
        String name = "save_0" + num;
        String filenameTemp = path + name + ".txt";
        File filename = new File(filenameTemp);
        if (!filename.exists()) {
            filename.createNewFile();
            flag = true;
        }
        return flag;
    }

//    public static void main(String[] args) throws IOException {
//        new checkSaves().startSave();
//    }


}
