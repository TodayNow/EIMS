package com.oniokey.eims;

import javax.swing.*;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.codec.digest.DigestUtils;

public class AccountCancellationUI extends JFrame implements ActionListener
{
    //数据库
    private final OperateMySQL database = new OperateMySQL();
    private JPanel main;
    private JButton Submit, SubmitReset;
    private JLabel Tips1, Tips2, InsertPasswd1, CurrentAccount;
    private JPasswordField InsertPasswd2;

    AccountCancellationUI(String UID)
    {
        setButton();//设置各按钮信息
        setLabel(UID);//设置各标签信息
        setPasswdField();//设置各文本框信息
        setPanel();//设置各面板信息
        setLayout();//设置布局信息
        setThis(); //设置主窗口信息
        initial();//初始化按键监听
    }

    private void setButton()
    {
        Submit = new JButton("确认");
        Submit.setFont(new Font("等线",Font.PLAIN,20));
        Submit.setBounds(140,200,90,40);
        Submit.setMargin(new Insets(0,0,0,0));
        //
        SubmitReset = new JButton("取消");
        SubmitReset.setFont(new Font("等线",Font.PLAIN,20));
        SubmitReset.setBounds(270,200,90,40);
        SubmitReset.setMargin(new Insets(0,0,0,0));
    }

    private void setLabel(String UID)
    {
        Tips1 = new JLabel("您正在执行账号注销操作，该操作一旦完成将不可逆转");
        Tips1.setFont(new Font("等线",Font.PLAIN,18));
        Tips1.setBounds(35,30,500,40);
        //
        Tips2 = new JLabel("确需注销请输入密码，并点击确认键以注销");
        Tips2.setFont(new Font("等线",Font.PLAIN,18));
        Tips2.setBounds(80,70,500,40);
        //
        InsertPasswd1 = new JLabel("密码：");
        InsertPasswd1.setFont(new Font("等线",Font.PLAIN,20));
        InsertPasswd1.setBounds(60,120,100,40);
        //
        CurrentAccount = new JLabel(UID);

    }

    private void setPasswdField()
    {
        InsertPasswd2 = new JPasswordField();
        InsertPasswd2.setFont(new Font("等线",Font.PLAIN,20));
        InsertPasswd2.setBounds(130,125,300,40);
    }

    private void setPanel()
    {
        main = new JPanel();
    }

    private void setLayout()
    {
        main.setLayout(null);
        main.add(Submit);
        main.add(SubmitReset);
        main.add(Tips1);
        main.add(Tips2);
        main.add(InsertPasswd1);
        main.add(InsertPasswd2);

    }

    private void setThis()
    {
        this.add(main);
        this.setTitle("账号注销");
        this.setSize(500,300);
        this.setLocationRelativeTo(null);//窗体居中显示
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    void initial()
    {
        InsertPasswd2.addActionListener(this);
        Submit.addActionListener(this);
        SubmitReset.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(SubmitReset))
            this.dispose();
        if (e.getSource().equals(Submit))
        {
            String EnID = DigestUtils.md5Hex(CurrentAccount.getText()).toUpperCase();
            String EnPasswd = DigestUtils.md5Hex(String.valueOf(InsertPasswd2.getPassword())+EnID).toUpperCase();
            try
            {
                database.setResult(database.doAccountQuery(EnID));
                if (database.getResult().next() && !String.valueOf(InsertPasswd2.getPassword()).isEmpty())
                {
                    String getPasswd = database.getResult().getString(3);
                    if (EnPasswd.equals(getPasswd))
                    {
                        database.doAccountDelete(EnID);
                        JOptionPane.showOptionDialog(this,"注销成功","温馨提示",JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,null,null,null);
                        System.exit(0);
                    }
                    else
                        JOptionPane.showOptionDialog(this,"密码错误，注销失败","温馨提示",JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,null,null,null);
                }
                else
                    JOptionPane.showOptionDialog(this,"密码不能为空，注销失败","温馨提示",JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,null,null,null);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
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
