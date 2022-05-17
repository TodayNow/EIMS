package com.oniokey.eims;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * @author iokey
 */
public class RegistrationUI extends JFrame implements ItemListener, ActionListener
{
    //数据库
    private final OperateMySQL database = new OperateMySQL();
    private JPanel main;
    /**
     * 嵌板
     */
    private JButton//按钮
            Submit,//提交
            SubmitReset,//清空页面
            Cancel;//取消
    private JLabel//标签
            InsertID1,//身份证号
            InsertName1,//姓名
            InsertPasswd1,//密码
            InsertValueCode1;//校验码
    private JTextField//文本框
            InsertID2,//身份证号
            InsertName2,//姓名
            InsertValueCode2;//校验码
    private JPasswordField//密码框
            InsertPasswd2;//密码

    RegistrationUI()
    {
        setButton();//设置各按钮信息
        setLabel();//设置各标签信息
        setTextField();//设置各文本框信息
        setPanel();//设置各面板信息
        setLayout();//设置布局信息
        setThis(); //设置主窗口信息
        initial();//初始化按键监听
    }

    private void setButton()//按钮
    {
        Submit = new JButton("注册");
        Submit.setFont(new Font("等线",Font.PLAIN,20));
        Submit.setBounds(200,400,100,45);
        Submit.setMargin(new Insets(0,0,0,0));//让按钮中的文本与按钮边缘贴齐
        //
        SubmitReset = new JButton("重置");
        SubmitReset.setFont(new Font("等线",Font.PLAIN,20));
        SubmitReset.setBounds(350,400,100,45);
        SubmitReset.setMargin(new Insets(0,0,0,0));
        //
        Cancel = new JButton("退出");
        Cancel.setFont(new Font("等线",Font.PLAIN,20));
        Cancel.setBounds(500,400,100,45);
        Cancel.setMargin(new Insets(0,0,0,0));
    }

    private void setLabel()//标签
    {
        //
        InsertID1 = new JLabel("身份证号：");
        InsertID1.setFont(new Font("楷体",Font.PLAIN,22));
        InsertID1.setBounds(200,40,120,50);
        //
        InsertName1 = new JLabel("姓名：");
        InsertName1.setFont(new Font("楷体",Font.PLAIN,22));
        InsertName1.setBounds(200,120,120,50);
        //
        InsertPasswd1 = new JLabel("密码：");
        InsertPasswd1.setFont(new Font("楷体",Font.PLAIN,22));
        InsertPasswd1.setBounds(200,200,120,50);
        //
        InsertValueCode1 = new JLabel("校验码：");
        InsertValueCode1.setFont(new Font("楷体",Font.PLAIN,22));
        InsertValueCode1.setBounds(200,280,120,50);
    }

    private void setTextField() //文本框
    {
        InsertID2 = new JTextField();
        InsertID2.setFont(new Font("等线",Font.PLAIN,20));
        InsertID2.setBounds(310,45,300,40);
        //
        InsertName2 = new JTextField();
        InsertName2.setFont(new Font("等线",Font.PLAIN,20));
        InsertName2.setBounds(310,125,300,40);
        //
        InsertPasswd2 = new JPasswordField();
        InsertPasswd2.setFont(new Font("等线",Font.PLAIN,20));
        InsertPasswd2.setBounds(310,205,300,40);
        //
        InsertValueCode2 = new JTextField();
        InsertValueCode2.setFont(new Font("等线",Font.PLAIN,20));
        InsertValueCode2.setBounds(310,285,300,40);
    }

    private void setPanel()//面板
    {
        main = new JPanel();
    }

    private void setLayout()
    {
        main.setLayout(null);
        main.add(Submit);
        main.add(SubmitReset);
        main.add(Cancel);
        main.add(InsertID1);
        main.add(InsertName1);
        main.add(InsertPasswd1);
        main.add(InsertValueCode1);
        main.add(InsertID2);
        main.add(InsertName2);
        main.add(InsertPasswd2);
        main.add(InsertValueCode2);
    }

    private void setThis()
    {
        this.add(main);
        this.setTitle("注册");
        this.setSize(800,500);
        this.setLocationRelativeTo(null);//窗体居中显示
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    void initial()//初始化
    {
        //按钮
        Submit.addActionListener(this);
        SubmitReset.addActionListener(this);
        Cancel.addActionListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //各个按钮功能
        //重置
        if (e.getSource().equals(SubmitReset))
        {
            InsertID2.setText("");
            InsertName2.setText("");
            InsertPasswd2.setText("");
        }
        //取消
        if (e.getSource().equals(Cancel))
        {
            this.dispose();
            new LoginUI();
        }
        //注册
        if (e.getSource().equals(Submit))
        {
            String InsertID = InsertID2.getText();
            String EnID = DigestUtils.md5Hex(InsertID).toUpperCase();
            String InsertName = InsertName2.getText();
            String InsertPasswd = String.valueOf(InsertPasswd2.getPassword());
            String EnPasswd = DigestUtils.md5Hex(InsertPasswd+EnID).toUpperCase();
            String InsertValueCode = InsertValueCode2.getText();
            if (InsertID.length() != 18) JOptionPane.showOptionDialog(this,"身份证错误","温馨提示",JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,null,null,null);
            else if (InsertPasswd.length()<8 || InsertPasswd.length()>18)
                JOptionPane.showOptionDialog(this,"密码为8～18位","温馨提示",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
            else
            {
                try
                {
                    database.setResult(database.doValueCodeQuery(InsertValueCode));
                    if (database.getResult().next())
                    {
                        String School = database.getResult().getString(2);
                        String Major = database.getResult().getString(3);
                        String Class = database.getResult().getString(4);
                        database.doValueCodeDelete(InsertValueCode);
                        database.setResult(database.doAccountQuery(EnID));
                        if (!database.getResult().next())
                        {

                            database.doRegister(EnID,InsertName,EnPasswd,School,Major,Class);
                            JOptionPane.showOptionDialog(this,"注册成功","温馨提示",JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,null,null,null);
                        }
                        else JOptionPane.showOptionDialog(this,"账号已存在，该校验码已失效","温馨提示",JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,null,null,null);
                    }
                    else JOptionPane.showOptionDialog(this,"校验码错误","温馨提示",JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,null,null,null);
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                finally
                {
                    database.CloseResult();
                    database.CloseStatement();
                    database.CloseConnection();
                }
            }
        }
    }
}
