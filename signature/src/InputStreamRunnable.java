import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class InputStreamRunnable extends Thread {
    BufferedReader bReader = null;
    String type = null;
    StringBuilder stringBuilder;
    ReadCallback readingExec;

    public InputStreamRunnable(InputStream is, String _type) {
        try {
            bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), "UTF-8"));
            type = _type;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public InputStreamRunnable(InputStream is, String _type, StringBuilder sb, ReadCallback reading) {
        try {
            bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), "UTF-8"));
            type = _type;
            stringBuilder = sb;
            readingExec = reading;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        String line;
        int lineNum = 0;
        try {
            while ((line = bReader.readLine()) != null) {
                lineNum++;
                System.out.println(type + ":" + line);
                if (null != stringBuilder) {
                    stringBuilder.append(line).append("\n");
                }
            }
            bReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (null != readingExec) {
            readingExec.onReadFinish(stringBuilder.toString());
        }
    }

}