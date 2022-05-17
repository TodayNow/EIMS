package com.oniokey.eims;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;
import java.util.Random;

public class AccountManagementUI extends JFrame implements ItemListener, ActionListener
{
    //数据库
    private final OperateMySQL database = new OperateMySQL();
    //按钮组
    private final ButtonGroup Choice = new ButtonGroup();
    private JTabbedPane Base;//选项卡
    private JPanel //嵌板
            Account,//添加账号
            Permission,//权限管理
            AddValueCode,//添加校验码
            QueryValueCode;//查询校验码
    private JButton//按钮
            AccountSubmit,//提交
            AccountSubmitReset,//清空页面
            PermissionSubmit,//提交
            PermissionSubmitReset,//清空
            PermissionQuery,//查询
            ValueCodeSubmit,//提交
            ValueCodeSubmitReset,//清空页面
            ValueCodeQuery,//查询
            ValueCodeReset;//清空页面
    private JRadioButton//单选按钮
            Add,//新增
            Delete;//删除
    private JLabel
            Loading1,
            Loading2,
            UserID,
            UserSchool,
            UserMajor,
            UserClass,
            Tips,//提示
            InsertPermissionID1,//身份证号
            InsertRegistered1,//已登记权限
            ItemModify1,//权限变更
            InsertPasswd1,//密码
            InsertID1,//身份证号
            InsertName1,//姓名
            InsertQuantity1;//数量
    private JTextField
            InsertPermissionID2,//身份证号
            InsertRegistered2,//已登记权限
            InsertID2,//身份证号
            InsertName2,//姓名
            InsertQuantity2;//数量
    private JPasswordField InsertPasswd2;//密码
    private JTextArea QueryValueCodeText;//查询校验码文本框
    private JScrollPane scroll = null;
    private final JComboBox<String>
            vcGroupItemSchool = new JComboBox<>(),//学院组选择
            vcGroupItemMajor = new JComboBox<>(),//专业组选择
            vcGroupItemClass = new JComboBox<>(),//班级组选择
            pGroupItemSchool = new JComboBox<>(),//学院组选择
            pGroupItemMajor = new JComboBox<>(),//专业组选择
            pGroupItemClass = new JComboBox<>();//班级组选择

    AccountManagementUI(String UID,String US,String UM,String UC)
    {
        new Thread(() ->
        {
            try
            {
                setButton();//设置各按钮信息
                setLabel(UID);//设置各标签信息
                setTextField();//设置各文本框信息
                setPanel();//设置各面板信息
                setLayout();//设置布局信息
                setBase();//设置选项卡信息
                setThis(); //设置主窗口信息
                initial();//初始化按键监听
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
        new Thread(() ->
        {
            pGroupItemSchool.addActionListener(this);
            pGroupItemMajor.addActionListener(this);
            pGroupItemClass.addActionListener(this);
            vcGroupItemSchool.addActionListener(this);
            vcGroupItemMajor.addActionListener(this);
            vcGroupItemClass.addActionListener(this);
            try
            {
                setComboBox(US,UM,UC);//设置动态下拉栏联动
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Permission.add(pGroupItemSchool);
            Permission.add(pGroupItemMajor);
            Permission.add(pGroupItemClass);
            Permission.remove(Loading1);
            Permission.updateUI();
            AddValueCode.add(vcGroupItemSchool);
            AddValueCode.add(vcGroupItemMajor);
            AddValueCode.add(vcGroupItemClass);
            AddValueCode.remove(Loading2);
            AddValueCode.updateUI();
        }).start();
    }

    private void setButton()
    {
        //Account
        Add = new JRadioButton("新增");
        Add.setFont(new Font("等线",Font.PLAIN,20));
        Add.setBounds(300,40,120,50);
        Add.setMargin(new Insets(0,0,0,0));
        //
        Delete = new JRadioButton("删除");
        Delete.setFont(new Font("等线",Font.PLAIN,20));
        Delete.setBounds(460,40,120,50);
        Delete.setMargin(new Insets(0,0,0,0));
        //
        AccountSubmit = new JButton("提交");
        AccountSubmit.setFont(new Font("等线",Font.PLAIN,20));
        AccountSubmit.setBounds(270,360,100,45);
        AccountSubmit.setMargin(new Insets(0,0,0,0));//让按钮中的文本与按钮边缘贴齐
        //
        AccountSubmitReset = new JButton("重置");
        AccountSubmitReset.setFont(new Font("等线",Font.PLAIN,20));
        AccountSubmitReset.setBounds(420,360,100,45);
        AccountSubmitReset.setMargin(new Insets(0,0,0,0));
        //Permission
        PermissionSubmit = new JButton("提交");
        PermissionSubmit.setFont(new Font("等线",Font.PLAIN,20));
        PermissionSubmit.setBounds(270,410,100,45);
        PermissionSubmit.setMargin(new Insets(0,0,0,0));
        //
        PermissionSubmitReset = new JButton("重置");
        PermissionSubmitReset.setFont(new Font("等线",Font.PLAIN,20));
        PermissionSubmitReset.setBounds(420,410,100,45);
        PermissionSubmitReset.setMargin(new Insets(0,0,0,0));
        //
        PermissionQuery = new JButton("查询");
        PermissionQuery.setFont(new Font("等线",Font.PLAIN,20));
        PermissionQuery.setBounds(600,55,100,40);
        //AddValueCode
        ValueCodeSubmit = new JButton("添加");
        ValueCodeSubmit.setFont(new Font("等线",Font.PLAIN,20));
        ValueCodeSubmit.setBounds(270,360,100,45);
        ValueCodeSubmit.setMargin(new Insets(0,0,0,0));//让按钮中的文本与按钮边缘贴齐
        //
        ValueCodeSubmitReset = new JButton("重置");
        ValueCodeSubmitReset.setFont(new Font("等线",Font.PLAIN,20));
        ValueCodeSubmitReset.setBounds(420,360,100,45);
        ValueCodeSubmitReset.setMargin(new Insets(0,0,0,0));
        //QueryValueCode
        ValueCodeQuery = new JButton("查询");
        ValueCodeQuery.setFont(new Font("等线",Font.PLAIN,20));
        ValueCodeQuery.setBounds(520,390,80,45);
        ValueCodeReset = new JButton("重置");
        //
        ValueCodeReset.setFont(new Font("等线",Font.PLAIN,20));
        ValueCodeReset.setBounds(640,390,80,45);
        //绑定按钮到组
        Choice.add(Add);
        Choice.add(Delete);
    }

    private void setTextField()
    {
        //Account
        InsertID2 = new JTextField();
        InsertID2.setFont(new Font("等线",Font.PLAIN,20));
        InsertID2.setBounds(310,125,300,40);
        //
        InsertName2 = new JTextField();
        InsertName2.setFont(new Font("等线",Font.PLAIN,22));
        InsertName2.setBounds(310,215,300,40);
        //Permission
        InsertPermissionID2 = new JTextField("请输入身份证号");
        InsertPermissionID2.setFont(new Font("楷体",Font.PLAIN,20));
        InsertPermissionID2.setBounds(280,55,300,40);
        //
        InsertRegistered2 = new JTextField("点击\"查询\"以查询账号权限");
        InsertRegistered2.setFont(new Font("楷体",Font.PLAIN,20));
        InsertRegistered2.setBounds(280,125,300,40);
        InsertRegistered2.setEditable(false);
        //
        InsertPasswd2 = new JPasswordField();
        InsertPasswd2.setFont(new Font("楷体",Font.PLAIN,20));
        InsertPasswd2.setBounds(280,265,300,40);
        //
        pGroupItemSchool.setFont(new Font("等线",Font.PLAIN,15));
        pGroupItemSchool.setBounds(140,195,200,40);
        //
        pGroupItemMajor.setFont(new Font("等线",Font.PLAIN,15));
        pGroupItemMajor.setBounds(340,195,200,40);
        //
        pGroupItemClass.setFont(new Font("等线",Font.PLAIN,15));
        pGroupItemClass.setBounds(540,195,200,40);
        //AddValueCode
        InsertQuantity2 = new JTextField();
        InsertQuantity2.setFont(new Font("等线",Font.PLAIN,20));
        InsertQuantity2.setBounds(310,205,300,40);
        //
        vcGroupItemSchool.setFont(new Font("等线",Font.PLAIN,15));
        vcGroupItemSchool.setBounds(120,75,200,40);
        //
        vcGroupItemMajor.setFont(new Font("等线",Font.PLAIN,15));
        vcGroupItemMajor.setBounds(320,75,200,40);
        //
        vcGroupItemClass.setFont(new Font("等线",Font.PLAIN,15));
        vcGroupItemClass.setBounds(520,75,200,40);
        //QueryValueCode
        QueryValueCodeText = new JTextArea("查询结果：");//文本域
        QueryValueCodeText.setFont(new Font("等线",Font.PLAIN,20));
        QueryValueCodeText.setEditable(false);
        QueryValueCodeText.setLineWrap(true);// 当一行文字过多时自动换行
        scroll = new JScrollPane(QueryValueCodeText);// 添加滚动条
        scroll.setBounds(30,30,600,320);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);//当需要垂直滚动条时显示
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);//当需要水平滚动条时显示
    }

    private void setComboBox(String US,String UM,String UC) throws Exception
    {
        UserSchool = new JLabel(US);
        UserMajor = new JLabel(UM);
        UserClass = new JLabel(UC);
        if (Objects.equals(US,"All"))
        {
            String[] School = database.doLGQuery();
            for (String s: School) pGroupItemSchool.addItem(s);
            pGroupItemSchool.setSelectedIndex(0);
        }
        else if (!Objects.equals(US,"Personal"))
        {
            pGroupItemSchool.addItem(US);
            pGroupItemSchool.setSelectedIndex(0);
            pGroupItemSchool.setEnabled(false);
        }
        else
        {
            pGroupItemSchool.setEnabled(false);
            pGroupItemMajor.setEnabled(false);
            pGroupItemClass.setEnabled(false);
        }
        if (Objects.equals(UM,"Personal"))
        {
            pGroupItemMajor.setEnabled(false);
            pGroupItemClass.setEnabled(false);
        }
        else if (!Objects.equals(UM,"All"))
        {
            pGroupItemMajor.removeAllItems();
            pGroupItemMajor.addItem(UM);
            pGroupItemMajor.setSelectedIndex(0);
            pGroupItemMajor.setEnabled(false);
        }
        if (Objects.equals(UC,"Personal"))
            pGroupItemClass.setEnabled(false);
        else if (!Objects.equals(UC,"All"))
        {
            pGroupItemClass.removeAllItems();
            pGroupItemClass.addItem(UC);
            pGroupItemClass.setSelectedIndex(0);
            pGroupItemClass.setEnabled(false);
        }
        //
        if (Objects.equals(US,"All"))
        {
            String[] School = database.doLGQuery();
            for (String s: School) vcGroupItemSchool.addItem(s);
        }
        else if (!Objects.equals(US,"Personal"))
        {
            vcGroupItemSchool.addItem(US);
            vcGroupItemSchool.setSelectedIndex(0);
            vcGroupItemSchool.setEnabled(false);
        }
        else
        {
            vcGroupItemSchool.setEnabled(false);
            vcGroupItemMajor.setEnabled(false);
            vcGroupItemClass.setEnabled(false);
        }
        if (Objects.equals(UM,"Personal"))
        {
            vcGroupItemMajor.setEnabled(false);
            vcGroupItemClass.setEnabled(false);
        }
        else if (!Objects.equals(UM,"All"))
        {
            vcGroupItemMajor.removeAllItems();
            vcGroupItemMajor.addItem(UM);
            vcGroupItemMajor.setSelectedIndex(0);
            vcGroupItemMajor.setEnabled(false);
        }
        if (Objects.equals(UC,"Personal"))
            vcGroupItemClass.setEnabled(false);
        else if (!Objects.equals(UC,"All"))
        {
            vcGroupItemClass.removeAllItems();
            vcGroupItemClass.addItem(UC);
            vcGroupItemClass.setSelectedIndex(0);
            vcGroupItemClass.setEnabled(false);
        }
    }

    private void setLabel(String UID)
    {
        InsertID1 = new JLabel("身份证号：");
        InsertID1.setFont(new Font("等线",Font.PLAIN,22));
        InsertID1.setBounds(200,120,120,50);
        //
        InsertName1 = new JLabel("姓名：");
        InsertName1.setFont(new Font("等线",Font.PLAIN,22));
        InsertName1.setBounds(200,210,120,50);
        //
        Tips = new JLabel("因系统安全策略，在此处添加的账号默认为Personal权限，要修改权限请至\"权限管理\"处修改。");
        Tips.setFont(new Font("等线",Font.PLAIN,13));
        Tips.setBounds(140,270,800,50);
        //Permission
        InsertPermissionID1 = new JLabel("身份证号：");
        InsertPermissionID1.setFont(new Font("楷体",Font.PLAIN,22));
        InsertPermissionID1.setBounds(140,50,120,50);
        //
        InsertRegistered1 = new JLabel("已登记组：");
        InsertRegistered1.setFont(new Font("楷体",Font.PLAIN,22));
        InsertRegistered1.setBounds(140,120,150,50);
        //
        ItemModify1 = new JLabel("变更为：");
        ItemModify1.setFont(new Font("楷体",Font.PLAIN,22));
        ItemModify1.setBounds(60,190,150,50);
        //
        Loading1 = new JLabel("正在加载，请稍后……");
        Loading1.setFont(new Font("等线",Font.PLAIN,22));
        Loading1.setBounds(340,195,200,40);
        //
        InsertPasswd1 = new JLabel("验证密码：");
        InsertPasswd1.setFont(new Font("楷体",Font.PLAIN,22));
        InsertPasswd1.setBounds(140,260,150,50);
        //
        Loading2 = new JLabel("正在加载，请稍后……");
        Loading2.setFont(new Font("等线",Font.PLAIN,22));
        Loading2.setBounds(320,75,200,40);
        //
        InsertQuantity1 = new JLabel("数量：");
        InsertQuantity1.setFont(new Font("等线",Font.PLAIN,22));
        InsertQuantity1.setBounds(200,200,120,50);
        //
        UserID = new JLabel(UID);
    }

    private void setPanel()
    {
        Account = new JPanel();
        Permission = new JPanel();
        AddValueCode = new JPanel();
        QueryValueCode = new JPanel();
    }

    private void setLayout()
    {
        //Account
        Account.setLayout(null);
        Account.add(Add);
        Account.add(Delete);
        Account.add(InsertID1);
        Account.add(InsertName1);
        Account.add(Tips);
        Account.add(AccountSubmit);
        Account.add(AccountSubmitReset);
        Account.add(InsertID2);
        Account.add(InsertName2);
        //Permission
        Permission.setLayout(null);
        Permission.add(PermissionSubmit);
        Permission.add(PermissionSubmitReset);
        Permission.add(PermissionQuery);
        Permission.add(InsertPermissionID1);
        Permission.add(InsertRegistered1);
        Permission.add(ItemModify1);
        Permission.add(InsertPasswd1);
        Permission.add(InsertPermissionID2);
        Permission.add(InsertRegistered2);
        Permission.add(Loading1);
        Permission.add(InsertPasswd2);
        //AddValueCode
        AddValueCode.setLayout(null);
        AddValueCode.add(ValueCodeSubmit);
        AddValueCode.add(ValueCodeSubmitReset);
        AddValueCode.add(InsertQuantity2);
        AddValueCode.add(Loading2);
        AddValueCode.add(InsertQuantity1);
        //QueryValueCode
        QueryValueCode.setLayout(null);
        QueryValueCode.add(ValueCodeQuery);
        QueryValueCode.add(ValueCodeReset);
        QueryValueCode.add(scroll);
    }

    private void setBase()
    {
        Base = new JTabbedPane(JTabbedPane.TOP);
        Base.addTab("添加账号",Account);
        Base.addTab("权限管理 ",Permission);
        Base.addTab("添加校验码",AddValueCode);
        Base.addTab("查询校验码",QueryValueCode);
    }

    private void setThis()
    {
        this.add(Base);
        this.setTitle("账号管理");
        this.setSize(800,550);
        this.setLocationRelativeTo(null);//窗体居中显示
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    void initial()
    {
        //按钮
        Add.addItemListener(this);
        Delete.addItemListener(this);
        AccountSubmit.addActionListener(this);
        AccountSubmitReset.addActionListener(this);
        PermissionSubmit.addActionListener(this);
        PermissionSubmitReset.addActionListener(this);
        PermissionQuery.addActionListener(this);
        ValueCodeSubmit.addActionListener(this);
        ValueCodeSubmitReset.addActionListener(this);
        ValueCodeQuery.addActionListener(this);
        ValueCodeReset.addActionListener(this);
        //文本框
        InsertID2.addActionListener(this);
        InsertQuantity2.addActionListener(this);
        InsertPermissionID2.addActionListener(this);
        InsertPasswd2.addActionListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource().equals(Add))
        {
            AccountSubmit.setText("添加");
            InsertName2.setText("");
            InsertName2.setEditable(true);
        }
        else
        {
            AccountSubmit.setText("删除");
            InsertName2.setText("---无需填写---");
            InsertName2.setEditable(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //下拉栏
        if (e.getSource().equals(vcGroupItemSchool))
        {
            try
            {
                String getSchool = (String)vcGroupItemSchool.getSelectedItem();
                vcGroupItemMajor.removeAllItems();
                vcGroupItemClass.removeAllItems();
                if (Objects.equals(UserSchool.getText(),"All"))
                    vcGroupItemMajor.addItem("All");
                String[] Major = database.doLGQuery(getSchool);
                for (String s: Major) vcGroupItemMajor.addItem(s);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        if (e.getSource().equals(vcGroupItemMajor))
        {
            try
            {
                String getSchool = (String)vcGroupItemSchool.getSelectedItem();
                String getMajor = (String)vcGroupItemMajor.getSelectedItem();
                vcGroupItemClass.removeAllItems();
                if (Objects.equals(UserMajor.getText(),"All"))
                    vcGroupItemClass.addItem("All");
                String[] Class = database.doLGQuery(getSchool,getMajor);
                for (String s: Class) vcGroupItemClass.addItem(s);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        //下拉栏
        if (e.getSource().equals(pGroupItemSchool))
        {
            try
            {
                String getSchool = (String)pGroupItemSchool.getSelectedItem();
                pGroupItemMajor.removeAllItems();
                pGroupItemClass.removeAllItems();
                if (Objects.equals(UserSchool.getText(),"All"))
                    pGroupItemMajor.addItem("All");
                String[] Major = database.doLGQuery(getSchool);
                for (String s: Major) pGroupItemMajor.addItem(s);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        if (e.getSource().equals(pGroupItemMajor))
        {
            try
            {
                String getSchool = (String)pGroupItemSchool.getSelectedItem();
                String getMajor = (String)pGroupItemMajor.getSelectedItem();
                pGroupItemClass.removeAllItems();
                if (Objects.equals(UserMajor.getText(),"All"))
                    pGroupItemClass.addItem("All");
                String[] Class = database.doLGQuery(getSchool,getMajor);
                for (String s: Class) pGroupItemClass.addItem(s);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        //重置
        if (e.getSource().equals(AccountSubmitReset))
        {
            Choice.clearSelection();
            AccountSubmit.setText("提交");
            InsertID2.setText("");
            InsertName2.setText("");
            InsertName2.setEditable(true);
        }
        else if (e.getSource().equals(ValueCodeReset))
            QueryValueCodeText.setText("查询结果：");
        else if (e.getSource().equals(ValueCodeSubmitReset))
            InsertQuantity2.setText("");
        else if (e.getSource().equals(PermissionSubmitReset))
        {
            InsertPermissionID2.setText("");
            InsertRegistered2.setText("点击\"查询\"以查询账号权限");
            InsertPasswd2.setText("");
        }
        //添加账号
        if (e.getSource().equals(AccountSubmit))
        {
            String InsertID = InsertID2.getText();
            String InsertName = InsertName2.getText();
            String EnID = DigestUtils.md5Hex(InsertID).toUpperCase();
            if (InsertID.length() == 18 && !InsertName.isEmpty())
            {
                String EnPasswd = DigestUtils.md5Hex(InsertID.substring(12)+EnID).toUpperCase();
                if (AccountSubmit.getText().equals("添加"))
                {
                    try
                    {
                        database.setResult(database.doAccountQuery(EnID));
                        if (!database.getResult().next())
                        {
                            database.doRegister(EnID,InsertName,EnPasswd,"Personal","Personal","Personal");
                            JOptionPane.showOptionDialog(this,"添加成功","温馨提示",JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,null,null,null);
                        }
                        else
                            JOptionPane.showOptionDialog(this,"账号已存在","温馨提示",JOptionPane.DEFAULT_OPTION,
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
                else if (AccountSubmit.getText().equals("删除"))
                {
                    try
                    {
                        database.setResult(database.doAccountQuery(EnID));
                        if (database.getResult().next())
                        {
                            String getSchool = database.getResult().getString(4);
                            String getMajor = database.getResult().getString(5);
                            String getClass = database.getResult().getString(6);
                            if ((UserSchool.getText().equals("All") && !getSchool.equals("All")) ||
                                    (UserSchool.getText().equals(getSchool) && UserMajor.getText().equals("All") &&
                                            !getMajor.equals("All")) ||
                                    (UserSchool.getText().equals(getSchool) && UserMajor.getText().equals(getMajor) &&
                                            UserClass.getText().equals("All") && !getClass.equals("All")))
                            {
                                database.doAccountDelete(EnID);
                                JOptionPane.showOptionDialog(this,"删除成功","温馨提示",JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
                            }
                            else
                                JOptionPane.showOptionDialog(this,"权限不足","温馨提示",JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
                        }
                        else
                            JOptionPane.showOptionDialog(this,"账号不存在","温馨提示",JOptionPane.DEFAULT_OPTION,
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
                else
                    JOptionPane.showOptionDialog(this,"请选择操作","温馨提示",JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,null,null,null);
            }
            else
                JOptionPane.showOptionDialog(this,"身份证号格式错误或所填项为空","温馨提示",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
        }
        //查询权限
        if (e.getSource().equals(PermissionQuery) || e.getSource().equals(InsertPermissionID2))
        {
            String EnID = DigestUtils.md5Hex(InsertPermissionID2.getText()).toUpperCase();
            if (!InsertPermissionID2.getText().isEmpty())
                try
                {
                    database.setResult(database.doAccountQuery(EnID));
                    if (database.getResult().next())
                    {
                        String getSchool = database.getResult().getString(4);
                        String getMajor = database.getResult().getString(5);
                        String getClass = database.getResult().getString(6);
                        if (UserSchool.getText().equals("All") ||
                                (UserSchool.getText().equals(getSchool) && UserMajor.getText().equals("All")) ||
                                (UserSchool.getText().equals(getSchool) && UserMajor.getText().equals(getMajor) &&
                                        UserClass.getText().equals("All")) ||
                                (UserSchool.getText().equals(getSchool) && UserMajor.getText().equals(getMajor) &&
                                        UserClass.getText().equals(getClass)) || getSchool.equals("Personal"))
                            InsertRegistered2.setText(
                                    database.getResult().getString(4)+"-"+database.getResult().getString(5)+"-"+
                                            database.getResult().getString(6));
                        else
                            JOptionPane.showOptionDialog(this,"权限不足","查询失败",JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,null,null,null);
                    }
                    else
                        JOptionPane.showOptionDialog(this,"账号不存在","查询失败",JOptionPane.DEFAULT_OPTION,
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
            else
                JOptionPane.showOptionDialog(this,"请输入身份证号","查询失败",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
        }
        //变更权限
        if (e.getSource().equals(PermissionSubmit) || e.getSource().equals(InsertPasswd2))
        {
            String School = (String)pGroupItemSchool.getSelectedItem();
            String Major = (String)pGroupItemMajor.getSelectedItem();
            String Class = (String)pGroupItemClass.getSelectedItem();
            String EnID = DigestUtils.md5Hex(InsertPermissionID2.getText()).toUpperCase();
            String EnUserID = DigestUtils.md5Hex(UserID.getText()).toUpperCase();
            String EnPasswd = DigestUtils.md5Hex(String.valueOf(InsertPasswd2.getPassword())+EnUserID).toUpperCase();
            if (InsertPermissionID2.getText().length() == 18)
            {
                try
                {
                    database.setResult(database.doAccountQuery(EnUserID));
                    if (database.getResult().next())
                    {
                        String getSchool = database.getResult().getString(4);
                        String getMajor = database.getResult().getString(5);
                        String getClass = database.getResult().getString(6);
                        if (UserSchool.getText().equals("All") ||
                                (UserSchool.getText().equals(getSchool) && UserMajor.getText().equals("All")) ||
                                (UserSchool.getText().equals(getSchool) && UserMajor.getText().equals(getMajor) &&
                                        UserClass.getText().equals("All")) ||
                                (UserSchool.getText().equals(getSchool) && UserMajor.getText().equals(getMajor) &&
                                        UserClass.getText().equals(getClass)))
                        {
                            database.setResult(database.doAccountQuery(EnUserID));
                            database.getResult().next();
                            String getPasswd = database.getResult().getString(3);
                            if (EnPasswd.equals(getPasswd))
                            {
                                database.doAccountUpdate(EnID,School,Major,Class);
                                InsertPasswd2.setText("");//每次提交之后都清空密码
                                JOptionPane.showOptionDialog(this,"权限已变更","修改成功",JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
                            }
                            else
                                JOptionPane.showOptionDialog(this,"密码错误","修改失败",JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
                        }
                        else
                            JOptionPane.showOptionDialog(this,"权限不足","修改失败",JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,null,null,null);
                    }
                    else
                        JOptionPane.showOptionDialog(this,"账号不存在","修改失败",JOptionPane.DEFAULT_OPTION,
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
            else
                JOptionPane.showOptionDialog(this,"身份证号错误","修改失败",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
        }
        //添加校验码
        else if (e.getSource().equals(ValueCodeSubmit))
        {
            String School = (String)vcGroupItemSchool.getSelectedItem();
            String Major = (String)vcGroupItemMajor.getSelectedItem();
            String Class = (String)vcGroupItemClass.getSelectedItem();
            String Author = DigestUtils.md5Hex(UserID.getText()).toUpperCase();
            if (!Objects.equals(School,null) && !Objects.equals(Major,null) && !Objects.equals(Class,null))
            {
                int Quantity = Integer.parseInt(InsertQuantity2.getText());
                if (!InsertQuantity2.getText().isEmpty() && Quantity<=5 && Quantity>0)
                {
                    try
                    {
                        for (int a = 0;a<Quantity;a++)
                        {
                            Random random = new Random();
                            StringBuilder Code = new StringBuilder();
                            for (int i = 0;i<6;i++)//生成一个6位随机数，包含大小写字母和数字
                            {
                                int number = random.nextInt(3);
                                long result;
                                switch (number)
                                {
                                    case 0 ->
                                    {
                                        result = Math.round(Math.random()*25+65);
                                        Code.append((char)result);
                                    }
                                    case 1 ->
                                    {
                                        result = Math.round(Math.random()*25+97);
                                        Code.append((char)result);
                                    }
                                    case 2 -> Code.append(new Random().nextInt(10));
                                }
                            }
                            database.setResult(database.doValueCodeQuery(Code.toString()));
                            database.doValueCodeInsert(Code.toString(),School,Major,Class,Author);
                        }
                        JOptionPane.showOptionDialog(this,"添加成功","温馨提示",JOptionPane.DEFAULT_OPTION,
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
                else
                    JOptionPane.showOptionDialog(this,"数量应为1～5个","温馨提示",JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,null,null,null);
            }
            else JOptionPane.showOptionDialog(this,"权限不足","温馨提示",JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,null,null,null);
        }
        //查询校验码
        else if (e.getSource().equals(ValueCodeQuery))
        {
            String Author = DigestUtils.md5Hex(UserID.getText()).toUpperCase();
            try
            {
                //根据各选项检索关键字进行查询，并返回结果集
                database.setResult(database.doValueCodeQuery(Author));
                //定义结果集中记录条数
                int i = 0;
                QueryValueCodeText.setText("查询结果：");
                //输出结果集记录
                while (database.getResult().next())
                {
                    String getAuthor = database.getResult().getString(5);
                    if (Objects.equals(getAuthor,Author))
                    {
                        ++i;
                        QueryValueCodeText.append("\r\n"+"第"+i+"条记录："+"\r\n"
                                +"校验码："+database.getResult().getString(1)+"\r\n"
                                +"一级："+database.getResult().getString(2)+"\r\n"
                                +"二级："+database.getResult().getString(3)+"\r\n"
                                +"三级："+database.getResult().getString(4)+
                                ("\r\n---------------------------------------------"));
                    }
                }
                QueryValueCodeText.setText(QueryValueCodeText.getText()+"\r\n"+"共有"+i+"条信息记录");

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
