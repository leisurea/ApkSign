import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

public class Tools {

    /**
     * 读取json文件
     *
     * @param fileName
     * @return
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            if (!jsonFile.exists()) {
                return null;
            }
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存文件
     *
     * @param path
     * @param json
     * @return
     */
    public static String writeFile(String path, String json) {
        try {
            File newTextFile = new File(path);
            FileWriter fw = new FileWriter(newTextFile);
            fw.write(json);
            fw.close();
            return null;
        } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
            return iox.getMessage();
        }
    }

    public static void deleteFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            boolean d = f.delete();
        }
    }

    public static String exec(String... shell) {
        StringBuilder sb = new StringBuilder();
        for (String s : shell) {
            sb.append(runShell(s).logcat);
        }
        return sb.toString();
    }

    public static ExecResult runShell(String shell) {
        ExecResult result = new ExecResult();

        StringBuilder sb = new StringBuilder();
        sb.append(shell).append("\n");

        try {
            Process p = Runtime.getRuntime().exec(shell);
            int exitValue = p.waitFor();
            result.code = exitValue;
            sb.append("===result state:").append(exitValue).append("\n");
            System.out.println(shell + " Process exitValue:" + exitValue);

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                System.out.println(line);
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            sb.append(e.getMessage()).append("\n");
            e.printStackTrace();
        } catch (InterruptedException exception) {
            sb.append(exception.getMessage()).append("\n");
            exception.printStackTrace();
        }
        result.logcat = sb.toString();
        return result;
    }

    public static void runShellAsync(String shell, ExecCallback callback) {
        ExecResult result = new ExecResult();
        result.state = false;

        StringBuilder sb = new StringBuilder();
        sb.append(shell).append("\n");

        try {
            Process process = Runtime.getRuntime().exec(shell);
//            new InputStreamRunnable(process.getErrorStream(), "Error",sb).start();
            new InputStreamRunnable(process.getInputStream(), "Info", sb, call -> {
                try {
                    boolean exitValue = process.waitFor(30, TimeUnit.MINUTES);

                    result.state = exitValue;
                    sb.append("===result state:").append(exitValue).append("\n");
                    System.out.println(shell + " Process exitValue:" + exitValue);
                    process.destroy();

                } catch (InterruptedException exception) {
                    result.state = false;
                    sb.append(exception.getMessage()).append("\n");
                    exception.printStackTrace();
                }
                if (null != callback) {
                    result.logcat = sb.toString();
                    callback.onExecFinish(result);
                }
            }).start();

        } catch (IOException e) {
            sb.append(e.getMessage()).append("\n");
            e.printStackTrace();
            if (null != callback) {
                result.logcat = sb.toString();
                callback.onExecFinish(result);
            }
        }
    }

    public static boolean isEmpty(String text) {
        return null == text || 0 == text.length();
    }

    public static boolean isNotEmpty(String text) {
        return null != text && text.length() > 0;
    }

    public static String getPrefixName(String fileName) {
        if (Tools.isEmpty(fileName)) {
            return "";
        }
        if (!fileName.contains(".")) {
            return "";
        }
        File file = new File(fileName);
        fileName = file.getName();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String getSuffixName(String fileName) {
        if (Tools.isEmpty(fileName)) {
            return "";
        }
        if (!fileName.contains(".")) {
            return "";
        }
        File file = new File(fileName);
        fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }


    /**
     * windows下需要.bat后缀，但命令行里不用
     *
     * @return
     */
    public static String getApksigner() {
        return isWindows() ? "apksigner.bat" : "apksigner";
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }
}
