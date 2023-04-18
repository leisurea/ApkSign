import com.alibaba.fastjson2.JSON;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.JTextComponent;

public class Sign extends JFrame {

    final JFileChooser fc = new JFileChooser();
    private List<SignBean> signList = null;
    private JTextField apksignerT;
    private JTextField unSignApkT;
    private JTextField keyStorePas;
    private JTextField alikeys;
    private JTextField alikeysPas;
    private JTextArea jtLogcat;
    private String profitFileName = "sign.json";
    private File directory = new File(".");
    //    private String output = "output" + File.separator;
    private String output = "";//不设置单独输出文件夹

    /**
     * 读取本地配置文件
     *
     * @return
     */
    private List<SignBean> readProfitFile() {
        List<SignBean> signList = null;
        try {
            String signStr = Tools.readJsonFile(directory.getCanonicalPath() + File.separator + profitFileName);
            if (!Objects.isNull(signStr)) {
                signList = JSON.parseArray(signStr, SignBean.class);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            signList = new ArrayList<>();
        }
        if (null == signList) {
            signList = new ArrayList<>();
        }
        return signList;
    }

    public Sign() {

//        System.out.println(directory.getCanonicalPath());
//        System.out.println(directory.getAbsolutePath());

        signList = readProfitFile();

        GridBagLayout gbaglayout = new GridBagLayout(); //创建GridBagLayout布局管理器
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(3, 3, 3, 3);
        this.setLayout(gbaglayout); //使用GridBagLayout布局管理器
        constraints.fill = GridBagConstraints.BOTH; //组件填充显示区域
//        constraints.anchor = GridBagConstraints.CENTER;
//        constraints.weightx = 0.0; //恢复默认值
//        constraints.weightx = 0.5; // 指定组件的分配区域
//        constraints.weighty = 0.5;
//        constraints.gridwidth = 1;
//        constraints.gridx = 1;
//        constraints.gridy = 1;
        constraints.weightx = 0.0; //恢复默认值
        JLabel tf1 = new JLabel("apksigner路径：");
        gbaglayout.setConstraints(tf1, constraints);
        this.add(tf1);
        constraints.weightx = 1; //layout_weight缩放权重
        apksignerT = new JTextField("");
        apksignerT.setText("/Users/leisure/Library/Android/sdk/build-tools/30.0.2/");
        gbaglayout.setConstraints(apksignerT, constraints);
        this.add(apksignerT);
        constraints.weightx = 0.0; //恢复默认值
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
        JButton signerB = new JButton("选择");
        signerB.addActionListener(e -> {
//            fc.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
            fc.setCurrentDirectory(new File("."));//默认为当前文件夹
            fc.setDialogTitle("选择任一apksigner所在目录");
//            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//            fc.setFileHidingEnabled(true);//显示隐藏
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            fc.setFileSystemView(FileSystemView.getFileSystemView());
            //打开文件选择器，现成阻塞，直到选择框被关闭
            if (fc.showOpenDialog(signerB) == JFileChooser.APPROVE_OPTION) {
//                JOptionPane.showMessageDialog(null, fc.getSelectedFile().getAbsolutePath());
                String csv = fc.getSelectedFile().getAbsolutePath();
                apksignerT.setText(csv);
            }
        });
        gbaglayout.setConstraints(signerB, constraints);
        this.add(signerB);

        constraints.gridwidth = 1; //重新设置gridwidth的值
        constraints.weightx = 0.0; //恢复默认值
        JLabel tf2 = new JLabel("未签名apk：");
        gbaglayout.setConstraints(tf2, constraints);
        this.add(tf2);
        constraints.weightx = 1; //layout_weight缩放权重
        unSignApkT = new JTextField("");
        gbaglayout.setConstraints(unSignApkT, constraints);
        this.add(unSignApkT);
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
        constraints.weightx = 0.0; //恢复默认值
        JButton unSignApkB = new JButton("选择");
        unSignApkB.addActionListener(e -> {
//            fc.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
            fc.setCurrentDirectory(new File("."));//默认为当前文件夹
            fc.setDialogTitle("选择未签名apk");
//            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".apk") || f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            if (fc.showOpenDialog(unSignApkB) == JFileChooser.APPROVE_OPTION) {
                String csv = fc.getSelectedFile().getAbsolutePath();
                unSignApkT.setText(csv);
            }
        });
        unSignApkB.addActionListener(e -> {

        });
        gbaglayout.setConstraints(unSignApkB, constraints);
        this.add(unSignApkB);

        constraints.gridwidth = 1; //重新设置gridwidth的值
        constraints.weightx = 0.0; //恢复默认值
        JLabel tf3 = new JLabel("签名后apk路径：");
        gbaglayout.setConstraints(tf3, constraints);
        this.add(tf3);
        constraints.weightx = 1; //layout_weight缩放权重
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
        JComboBox signApkPath = new JComboBox(signList.stream().map(a -> a.signApk).toArray());
        signApkPath.setEditable(true);
        signApkPath.setSelectedIndex(-1);//默认不选
        gbaglayout.setConstraints(signApkPath, constraints);
        this.add(signApkPath);

        constraints.gridwidth = 1; //重新设置gridwidth的值
        constraints.weightx = 0.0; //恢复默认值
        JLabel tf4 = new JLabel("jks路径：");
        gbaglayout.setConstraints(tf4, constraints);
        this.add(tf4);
        constraints.weightx = 1; //layout_weight缩放权重
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
        JComboBox jksPath = new JComboBox(signList.stream().map(a -> a.jks).toArray());
        jksPath.setEditable(true);
        jksPath.setSelectedIndex(-1);
        gbaglayout.setConstraints(jksPath, constraints);
        jksPath.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                switch (event.getStateChange()) {
                    case ItemEvent.SELECTED:
                        Optional<SignBean> first = signList.stream().filter(a -> event.getItem().equals(a.jks)).findFirst();
                        if (first.isPresent()) {
                            SignBean signBean = first.get();
                            keyStorePas.setText(signBean.keyStorePas);
                            alikeys.setText(signBean.alikeys);
                            alikeysPas.setText(signBean.alikeysPas);
                        }

                        break;
//                    case ItemEvent.DESELECTED://取消选中
//                        break;
                }
            }
        });
        this.add(jksPath);

        constraints.gridwidth = 1; //重新设置gridwidth的值
        constraints.weightx = 0.0; //恢复默认值
        JLabel tf5 = new JLabel("密钥密码：");
        gbaglayout.setConstraints(tf5, constraints);
        this.add(tf5);
        constraints.weightx = 1; //layout_weight缩放权重
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
        keyStorePas = new JTextField("");
        gbaglayout.setConstraints(keyStorePas, constraints);
        this.add(keyStorePas);

        constraints.gridwidth = 1; //重新设置gridwidth的值
        constraints.weightx = 0.0; //恢复默认值
        JLabel tf6 = new JLabel("别名：");
        gbaglayout.setConstraints(tf6, constraints);
        this.add(tf6);
        constraints.weightx = 1; //layout_weight缩放权重
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
        alikeys = new JTextField("");
        gbaglayout.setConstraints(alikeys, constraints);
        this.add(alikeys);

        constraints.gridwidth = 1; //重新设置gridwidth的值
        constraints.weightx = 0.0; //恢复默认值
        JLabel tf7 = new JLabel("别名密码：");
        gbaglayout.setConstraints(tf7, constraints);
        this.add(tf7);
        constraints.weightx = 1; //layout_weight缩放权重
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
        alikeysPas = new JTextField("");
        gbaglayout.setConstraints(alikeysPas, constraints);
        this.add(alikeysPas);

        constraints.weightx = 0; //恢复默认值
        constraints.gridwidth = 1; //重新设置gridwidth的值
        JButton jbSave = new JButton("保存配置");
        gbaglayout.setConstraints(jbSave, constraints);
        jbSave.addActionListener(e -> {
//            String apksignerPath = apksignerT.getText().trim();
//            String unSignApkPath = unSignApkT.getText().trim();
            String signedApkPath = ((JTextComponent) signApkPath.getEditor().getEditorComponent()).getText().trim();
            String jks = ((JTextComponent) jksPath.getEditor().getEditorComponent()).getText().trim();
            String alikey = alikeys.getText().trim();
            String keyPas = keyStorePas.getText().trim();
            String alikeyPas = alikeysPas.getText().trim();
            SignBean signBean = new SignBean();
            signBean.signApk = signedApkPath;
            signBean.jks = jks;
            signBean.alikeys = alikey;
            signBean.keyStorePas = keyPas;
            signBean.alikeysPas = alikeyPas;
            signList.add(signBean);
            try {
                String result = Tools.writeFile(directory.getCanonicalPath() + File.separator + profitFileName, JSON.toJSONString(signList));
                if (Tools.isNotEmpty(result)) {
                    jtLogcat.append(result);
                } else {
                    //更新数据源
                    jksPath.setModel(new DefaultComboBoxModel(signList.stream().map(a -> a.jks).toArray()));
                    jtLogcat.append("保存成功：" + profitFileName);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                jtLogcat.append(exception.getMessage());
            }
        });
        this.add(jbSave);

        JButton jbSign = new JButton("签名");
        jbSign.addActionListener(e -> {

            String apksignerPath = apksignerT.getText().trim();
            if (Tools.isEmpty(apksignerPath)) {
                JOptionPane.showMessageDialog(null, "apksigner路径为空");
                return;
            }
            if (!apksignerPath.endsWith("\\") && !apksignerPath.endsWith("/")) {
                apksignerPath += File.separator;
            }
            String unSignApkPath = unSignApkT.getText().trim();
            if (Tools.isEmpty(unSignApkPath)) {
                JOptionPane.showMessageDialog(null, "未签名apk路径为空");
                return;
            }
            String preFix = Tools.getPrefixName(unSignApkPath);
            String sufFix = Tools.getSuffixName(unSignApkPath);
            if (Tools.isEmpty(preFix) || Tools.isEmpty(sufFix)) {
                JOptionPane.showMessageDialog(null, "未签名apk异常");
                return;
            }

            String signedApkPath = ((JTextComponent) signApkPath.getEditor().getEditorComponent()).getText().trim();
            if (Tools.isEmpty(signedApkPath)) {//JComboBox
                JOptionPane.showMessageDialog(null, "签名apk输出路径为空");
                return;
            }
            String jks = ((JTextComponent) jksPath.getEditor().getEditorComponent()).getText().trim();
            if (Tools.isEmpty(jks)) {//JComboBox
                JOptionPane.showMessageDialog(null, "jks签名文件路径为空");
                return;
            }
            String keyPas = keyStorePas.getText().trim();
            if (Tools.isEmpty(keyPas)) {//JComboBox
                JOptionPane.showMessageDialog(null, "签名密钥密码为空");
                return;
            }
            String alikey = alikeys.getText().trim();
            if (Tools.isEmpty(alikey)) {//JComboBox
                JOptionPane.showMessageDialog(null, "别名为空");
                return;
            }
            String alikeyPas = alikeysPas.getText().trim();
            if (Tools.isEmpty(alikeyPas)) {//JComboBox
                JOptionPane.showMessageDialog(null, "别名密码为空");
                return;
            }
            String output_dir = signedApkPath + (signedApkPath.endsWith("/") || signedApkPath.endsWith("\\") ? "" : File.separator) + output;

            String target_apk = signedApkPath + File.separator + sufFix;
            String zipalign_apk = output_dir + preFix + "_zipalign" + sufFix;
            String signer_apk = output_dir + preFix + "_signer" + sufFix;

//            System.getProperty("os.name")
            String cmd_zipalign = "";
            String cmd_signer = "";
//            # 拼装签名命令-如果您使用的是 apksigner，则必须在为 APK 文件签名之前使用 zipalign。如果您在使用 apksigner 为 APK 签名之后对 APK 做出了进一步更改，签名便会失效。
            cmd_zipalign = apksignerPath + "zipalign -p -f -v 4 " + unSignApkPath + " " + zipalign_apk;
//            #确认对齐结果命令，按需使用(操作或验证成功后会看到 Verification succesful)
//            # cmd += apksignerPath + 'zipalign -c -v 4 '+zipalign_apk +'\n '
            cmd_signer = apksignerPath + "apksigner sign --ks " + jks + " --ks-pass pass:" + alikeyPas + " --ks-key-alias " + alikey + " --key-pass pass:" + keyPas + " --v2-signing-enabled true -v --out " + signer_apk + " " + zipalign_apk;
            //校验签名
            String cmd_verify = apksignerPath + "apksigner verify -v --print-certs " + signer_apk;
//            #不推荐jarsigner

//            jtLogcat.append(Tools.exec(cmd_zipalign, cmd_signer));


            //MacOS可以同步执行，win执行卡死
//            onExecCmd(cmd_zipalign, cmd_signer, cmd_verify, zipalign_apk, signer_apk);
            onExecCmdAsync(cmd_zipalign, cmd_signer, cmd_verify, zipalign_apk, signer_apk);

//            ExecResult result = Tools.runShell(cmd_zipalign);
//            if (0 == result.state) {
//                jtLogcat.append("zipalign 对齐成功 \n");
//            } else {
//                jtLogcat.append(result.logcat + "\n");
//                return;
//            }
//            result = Tools.runShell(cmd_signer);
//            if (0 == result.state) {
//                jtLogcat.append("apksigner sign成功 \n");
//            } else {
//                jtLogcat.append(result.logcat + "\n");
//                return;
//            }
//            result = Tools.runShell(cmd_verify);
//            if (0 == result.state) {
//                jtLogcat.append("apksigner verify成功 \n");
//                jtLogcat.append(result.logcat + "\n");
//            } else {
//                jtLogcat.append(result.logcat + "\n");
//                return;
//            }
//            //删除对齐文件
//            Tools.deleteFile(zipalign_apk);
//            //删除V3文件
//            Tools.deleteFile(signer_apk + ".idsig");

        });
        gbaglayout.setConstraints(jbSign, constraints);
        this.add(jbSign);

        constraints.weightx = 0; //恢复默认值
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
        JButton jbClear = new JButton("清空日志");
        gbaglayout.setConstraints(jbClear, constraints);
        jbClear.addActionListener(e -> {
            jtLogcat.setText(null);
        });
        this.add(jbClear);


        constraints.weightx = 1; //layout_weight缩放权重
        constraints.weighty = 1.0; //恢复默认值
        constraints.gridwidth = GridBagConstraints.REMAINDER; //结束行
//        constraints.gridheight = GridBagConstraints.BASELINE;
        jtLogcat = new JTextArea();
//        jtLogcat.setRows(20);
        jtLogcat.setWrapStyleWord(true);
        jtLogcat.setLineWrap(true);    //设置多行文本框自动换行

//        JScrollPane jspane = new JScrollPane(jtLogcat,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane jspane = new JScrollPane(jtLogcat);
        gbaglayout.setConstraints(jspane, constraints);
        this.add(jspane);
        constraints.gridwidth = 1;

        this.setTitle("apk resign");
        this.setSize(400, 600);
//        this.pack();
//        this.setBounds(100, 100, 400, 600); //设置容器大小
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * 同步执行
     *
     * @param cmd_zipalign
     * @param cmd_signer
     * @param cmd_verify
     * @param zipalign_apk
     * @param signer_apk
     */
    private void onExecCmd(String cmd_zipalign, String cmd_signer, String cmd_verify, String zipalign_apk, String signer_apk) {
        ExecResult result = Tools.runShell(cmd_zipalign);
        if (0 == result.code) {
            jtLogcat.append("zipalign 对齐成功 \n");
        } else {
            jtLogcat.append(result.logcat + "\n");
            return;
        }
        result = Tools.runShell(cmd_signer);
        if (0 == result.code) {
            jtLogcat.append("apksigner sign成功 \n");
        } else {
            jtLogcat.append(result.logcat + "\n");
            return;
        }
        result = Tools.runShell(cmd_verify);
        if (0 == result.code) {
            jtLogcat.append("apksigner verify成功 \n");
            jtLogcat.append(result.logcat + "\n");
        } else {
            jtLogcat.append(result.logcat + "\n");
            return;
        }
        //删除对齐文件
        Tools.deleteFile(zipalign_apk);
        //删除V3文件
        Tools.deleteFile(signer_apk + ".idsig");
    }

    /**
     * 异步执行
     *
     * @param cmd_zipalign
     * @param cmd_signer
     * @param cmd_verify
     * @param zipalign_apk
     * @param signer_apk
     */
    private void onExecCmdAsync(String cmd_zipalign, String cmd_signer, String cmd_verify, String zipalign_apk, String signer_apk) {
        Tools.runShellAsync(cmd_zipalign, result1 -> {
            if (result1.state) {
                jtLogcat.append("zipalign 对齐成功 \n");
                Tools.runShellAsync(cmd_signer, result2 -> {
                    if (result2.state) {
                        jtLogcat.append("apksigner sign成功 \n");
                        Tools.runShellAsync(cmd_verify, result3 -> {
                            if (result3.state) {
                                jtLogcat.append("apksigner verify成功 \n");
                                jtLogcat.append(result3.logcat + "\n");
                                //删除对齐文件
                                Tools.deleteFile(zipalign_apk);
                                //删除V3文件
                                Tools.deleteFile(signer_apk + ".idsig");
                            } else {
                                jtLogcat.append(result3.logcat + "\n");
                            }
                        });
                    } else {
                        jtLogcat.append(result2.logcat + "\n");
                    }
                });
            } else {
                jtLogcat.append(result1.logcat + "\n");
            }
        });
    }

}
