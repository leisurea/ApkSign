/**
 * 打包教程https://blog.csdn.net/G_SGold/article/details/128258303
 * 1.配置 File->Project Structure->Artifacts
 * 2.打包 Build->Build Artifac->Clean->Build
 * 3.jar文件在out->artifacts->signature_jar
 */
public class SignFrame {

    public static void main(String args[]) {
        new Sign();
    }
}