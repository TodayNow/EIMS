package com.oniokey.eims;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author iokey
 */
public class LoginUI implements ActionListener
{
    private final OperateMySQL database = new OperateMySQL();
    private final JFrame main;//主窗口
    private final JTextField ID;
    private final JPasswordField Passwd;//用户名及密码
    private final JButton Login;
    private final JButton Cancel;
    private final JButton Register;//登录、退注册的按钮

    LoginUI()
    {
        main = new JFrame("登录");
        //提示标签
        JLabel inputUserName = new JLabel(" 账号： ");
        JLabel inputPasswd = new JLabel(" 密码： ");
        ID = new JTextField();
        Passwd = new JPasswordField();
        //各组件
        Login = new JButton("登录");
        Cancel = new JButton("取消");
        Register = new JButton("注册");
        //登陆窗口参数
        main.setSize(400,150);
        main.setLocationRelativeTo(null);//窗体居中显示
        //账号密码框大小
        ID.setPreferredSize(new Dimension(300,30));
        Passwd.setPreferredSize(new Dimension(300,30));
        //各组件
        main.getContentPane().add(inputUserName);
        main.getContentPane().add(ID);
        main.getContentPane().add(inputPasswd);
        main.getContentPane().add(Passwd);
        main.getContentPane().add(Login);
        main.getContentPane().add(Register);
        main.getContentPane().add(Cancel);
        //登陆窗口参数
        main.setLayout(new FlowLayout());
        main.setResizable(false);
        main.setVisible(true);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //监听事件
        Login.addActionListener(this);
        Register.addActionListener(this);
        Cancel.addActionListener(this);//按钮
        ID.addActionListener(this);
        Passwd.addActionListener(this);//文本框
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(Cancel)) System.exit(0);
        if (e.getSource().equals(Login) || e.getSource().equals(Passwd) || e.getSource().equals(ID))
        {
            String EnID = DigestUtils.md5Hex(ID.getText()).toUpperCase();
            String EnPasswd = DigestUtils.md5Hex(String.valueOf(Passwd.getPassword())+EnID).toUpperCase();
            if (!ID.getText().isEmpty() && !String.valueOf(Passwd.getPassword()).isEmpty())
            {
                try
                {
                    database.setResult(database.doAccountQuery(EnID));
                    if (database.getResult().next())
                    {
                        String getID = database.getResult().getString(1);
                        String getPasswd = database.getResult().getString(3);
                        if (EnID.equals(getID) && EnPasswd.equals(getPasswd))
                        {
                            String getUserName = database.getResult().getString(2);
                            String getSchool = database.getResult().getString(4);
                            String getMajor = database.getResult().getString(5);
                            String getClass = database.getResult().getString(6);
                            new MainUI(ID.getText(),getUserName,getSchool,getMajor,getClass);
                            main.dispose();
                        }
                        else
                            JOptionPane.showOptionDialog(main,"账号或密码错误","登陆失败",JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.ERROR_MESSAGE,null,null,null);
                    }
                    else
                        JOptionPane.showOptionDialog(main,"账号或密码错误","登陆失败",JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE,null,null,null);
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
            else
                JOptionPane.showOptionDialog(main,"账号或密码不能为空","登陆失败",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,null,null,null);
        }
        if (e.getSource().equals(Register))
        {
            try
            {
                new RegistrationUI();
                main.dispose();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
