/**
 * 打包教程https://blog.csdn.net/G_SGold/article/details/128258303
 * 1.配置 File->Project Structure->Artifacts
 * 2.打包 Build->Build Artifac->Clean->Build
 * 3.jar文件在out->artifacts->signature_jar
 *
 * built-tools下载https://androidsdkmanager.azurewebsites.net/Buildtools
 *
 * 执行命令时 文件路径及名称不可有空格
 */
public class SignFrame {

    public static void main(String args[]) {
        new Sign();
    }
}