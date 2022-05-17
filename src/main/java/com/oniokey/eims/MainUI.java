package com.oniokey.eims;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.Objects;

public class MainUI extends JFrame implements ItemListener, ActionListener
{
    //数据库
    private final OperateMySQL database = new OperateMySQL();
    //下拉栏
    private final JComboBox<String> GenderItem = new JComboBox<>();//性别选择
    private final JComboBox<String> GroupItemSchool = new JComboBox<>();//学院组选择
    private final JComboBox<String> GroupItemMajor = new JComboBox<>();//专业组选择
    private final JComboBox<String> GroupItemClass = new JComboBox<>();//班级组选择
    private final JComboBox<String> UpdateItem = new JComboBox<>();//更新项目
    private String[] Date, Condition;
    private JTabbedPane Base;//选项卡
    private JPanel//嵌板
            Add,//添加记录
            Remove,//删除记录
            Update,//更新记录
            Search,//查询记录
            Mine;//我的
    private JButton//按钮
            Insert,//添加记录
            InsertReset,//清空页面
            AddNucAcid,//添加核酸(Add页面)
            AddSearchNucAcid,//添加核酸(Search页面)
            Delete,//删除记录
            DeleteReset,//清空页面
            Upgrade,//更新记录
            UpgradeReset,//清空页面
            Query,//查询记录
            QueryReset,//清空页面
            LGModify,//级-组 管理
            AccountManage,//账号管理
            AccountCancel,//账号注销
            Logout;//退出登录
    private JLabel//标签
            Loading,//加载提示
            InsertID1,//身份证号
            InsertName1,//姓名
            InsertGender1,//性别
            InsertNucAcid1,//核酸
            InsertAddress1,//家庭住址
            InsertTel1,//联系电话
            InsertGroup1,//组
            DeleteID1,//删除身份证号
            UpdateID1,//更新身份证号
            CurrentAccount,//当前账号
            CAContent,//内容
            UserGroup,//用户身份
            UGContent,//内容
            UserID,//鉴权
            UserSchool,//鉴权
            UserMajor,//鉴权
            UserClass;//鉴权
    private JTextField//文本框
            InsertID2,//身份证号
            InsertName2,//姓名
            InsertAddress2,//家庭住址
            InsertTel2,//联系电话
            DeleteID2,//删除身份证号
            UpdateID2,//更新身份证号
            UpdateContent,//更新内容
            IDCondition,//查询ID
            NameCondition,//查询姓名
            TelCondition,//查询联系电话
            GroupCondition;//查询组
    private JTextArea QueryRecordResult;//查询信息文本域
    private JCheckBox//查询选项
            ID,//身份证号
            Name,//姓名
            Tel,//电话
            Group;//组
    private JScrollPane scroll = null;

    MainUI(String UID,String UN,String US,String UM,String UC)
    {
        new Thread(() ->
        {
            try
            {
                setButton();//设置各按钮信息
                setLabel(UID,UN,US,UM,UC);//设置各标签信息
                setTextField();//设置各文本框信息
                setPanel();//设置各面板信息
                setLayout(UID);//设置布局信息
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
            GroupItemSchool.addActionListener(this);
            GroupItemMajor.addActionListener(this);
            GroupItemClass.addActionListener(this);
            try
            {
                setComboBox(US,UM,UC);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Add.add(GroupItemSchool);
            Add.add(GroupItemMajor);
            Add.add(GroupItemClass);
            Add.remove(Loading);
            Add.updateUI();
        }).start();
    }

    private void setComboBox(String US,String UM,String UC) throws Exception
    {
        if (Objects.equals(US,"All"))
        {
            String[] School = database.doLGQuery();
            for (String s: School) GroupItemSchool.addItem(s);
            GroupItemSchool.setSelectedIndex(0);
        }
        else if (!Objects.equals(US,"Personal"))
        {
            GroupItemSchool.addItem(US);
            GroupItemSchool.setSelectedIndex(0);
            GroupItemSchool.setEnabled(false);
        }
        else
        {
            GroupItemSchool.setEnabled(false);
            GroupItemMajor.setEnabled(false);
            GroupItemClass.setEnabled(false);
        }
        if (Objects.equals(UM,"Personal"))
        {
            GroupItemMajor.setEnabled(false);
            GroupItemClass.setEnabled(false);
        }
        else if (!Objects.equals(UM,"All"))
        {
            GroupItemMajor.removeAllItems();
            GroupItemMajor.addItem(UM);
            GroupItemMajor.setSelectedIndex(0);
            GroupItemMajor.setEnabled(false);
        }
        if (Objects.equals(UC,"Personal"))
            GroupItemClass.setEnabled(false);
        else if (!Objects.equals(UC,"All"))
        {
            GroupItemClass.removeAllItems();
            GroupItemClass.addItem(UC);
            GroupItemClass.setSelectedIndex(0);
            GroupItemClass.setEnabled(false);
        }
    }

    private void setButton()//按钮信息的方法
    {
        //Add
        Insert = new JButton("添加");
        Insert.setFont(new Font("等线",Font.PLAIN,20));
        Insert.setBounds(270,480,100,45);
        Insert.setMargin(new Insets(0,0,0,0));//让按钮中的文本与按钮边缘贴齐
        //
        InsertReset = new JButton("重置");
        InsertReset.setFont(new Font("等线",Font.PLAIN,20));
        InsertReset.setBounds(420,480,100,45);
        InsertReset.setMargin(new Insets(0,0,0,0));
        //Remove
        Delete = new JButton("删除");
        Delete.setFont(new Font("等线",Font.PLAIN,20));
        Delete.setBounds(270,480,100,45);
        Delete.setMargin(new Insets(0,0,0,0));
        //
        DeleteReset = new JButton("重置");
        DeleteReset.setFont(new Font("等线",Font.PLAIN,20));
        DeleteReset.setBounds(420,480,100,45);
        DeleteReset.setMargin(new Insets(0,0,0,0));
        //Update
        Upgrade = new JButton("更新");
        Upgrade.setFont(new Font("等线",Font.PLAIN,20));
        Upgrade.setBounds(270,480,100,45);
        //
        UpgradeReset = new JButton("重置");
        UpgradeReset.setFont(new Font("等线",Font.PLAIN,20));
        UpgradeReset.setBounds(420,480,100,45);
        //Search
        ID = new JCheckBox("身份证号");
        ID.setFont(new Font("等线",Font.PLAIN,18));
        ID.setMargin(new Insets(0,0,0,0));
        ID.setBounds(30,320,120,30);
        //
        Name = new JCheckBox("姓名");
        Name.setFont(new Font("等线",Font.PLAIN,18));
        Name.setMargin(new Insets(0,0,0,0));
        Name.setBounds(30,370,120,30);
        //
        Tel = new JCheckBox("联系电话");
        Tel.setFont(new Font("等线",Font.PLAIN,18));
        Tel.setMargin(new Insets(0,0,0,0));
        Tel.setBounds(30,420,120,30);
        //
        Group = new JCheckBox("组");
        Group.setFont(new Font("等线",Font.PLAIN,18));
        Group.setMargin(new Insets(0,0,0,0));
        Group.setBounds(30,470,120,30);
        //
        AddSearchNucAcid = new JButton("添加核酸检测记录");
        AddSearchNucAcid.setFont(new Font("等线",Font.PLAIN,20));
        AddSearchNucAcid.setBounds(520,400,200,45);
        //
        Query = new JButton("查询");
        Query.setFont(new Font("等线",Font.PLAIN,20));
        Query.setBounds(520,480,80,45);
        //
        QueryReset = new JButton("重置");
        QueryReset.setFont(new Font("等线",Font.PLAIN,20));
        QueryReset.setBounds(640,480,80,45);
        //Management
        LGModify = new JButton("组管理");
        LGModify.setFont(new Font("等线",Font.PLAIN,20));
        LGModify.setBounds(190,250,160,45);
        //
        AccountManage = new JButton("账号管理");
        AccountManage.setFont(new Font("等线",Font.PLAIN,20));
        AccountManage.setBounds(430,250,160,45);
        //
        AccountCancel = new JButton("注销账号");
        AccountCancel.setFont(new Font("等线",Font.PLAIN,20));
        AccountCancel.setBounds(190,360,160,45);
        //
        Logout = new JButton("退出登录");
        Logout.setFont(new Font("等线",Font.PLAIN,20));
        Logout.setBounds(430,360,160,45);
    }

    private void setTextField()  //文本框信息的方法
    {
        //Add
        InsertID2 = new JTextField();
        InsertID2.setFont(new Font("等线",Font.PLAIN,20));
        InsertID2.setBounds(280,40,300,40);
        //
        InsertName2 = new JTextField();
        InsertName2.setFont(new Font("等线",Font.PLAIN,20));
        InsertName2.setBounds(280,100,300,40);
        //
        GenderItem.setFont(new Font("等线",Font.PLAIN,20));
        GenderItem.setBounds(280,160,300,38);
        GenderItem.addItem("男");
        GenderItem.addItem("女");
        GenderItem.setSelectedIndex(-1);
        //
        AddNucAcid = new JButton("添加核酸检测记录");
        AddNucAcid.setFont(new Font("等线",Font.PLAIN,20));
        AddNucAcid.setBounds(280,220,300,40);
        //
        InsertAddress2 = new JTextField();
        InsertAddress2.setFont(new Font("等线",Font.PLAIN,20));
        InsertAddress2.setBounds(280,280,300,40);
        //
        InsertTel2 = new JTextField();
        InsertTel2.setFont(new Font("等线",Font.PLAIN,20));
        InsertTel2.setBounds(280,340,300,40);
        //
        GroupItemSchool.setFont(new Font("等线",Font.PLAIN,15));
        GroupItemSchool.setBounds(110,400,200,40);
        //
        GroupItemMajor.setFont(new Font("等线",Font.PLAIN,15));
        GroupItemMajor.setBounds(310,400,200,40);
        //
        GroupItemClass.setFont(new Font("等线",Font.PLAIN,15));
        GroupItemClass.setBounds(510,400,200,40);
        //Remove
        DeleteID2 = new JTextField();
        DeleteID2.setFont(new Font("等线",Font.PLAIN,20));
        DeleteID2.setBounds(260,145,350,40);
        //Update
        UpdateID2 = new JTextField();
        UpdateID2.setFont(new Font("等线",Font.PLAIN,20));
        UpdateID2.setBounds(270,110,350,40);
        //
        UpdateContent = new JTextField("请输入更新后的内容");
        UpdateContent.setFont(new Font("等线",Font.PLAIN,20));
        UpdateContent.setBounds(270,245,350,40);
        //Search
        QueryRecordResult = new JTextArea("查询结果：");//文本域
        QueryRecordResult.setFont(new Font("等线",Font.PLAIN,20));
        QueryRecordResult.setEditable(false);
        QueryRecordResult.setLineWrap(true);// 当一行文字过多时自动换行
        scroll = new JScrollPane(QueryRecordResult);// 添加滚动条
        scroll.setBounds(30,30,570,270);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);//当需要垂直滚动条时显示
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);//当需要水平滚动条时显示
        //
        IDCondition = new JTextField();
        IDCondition.setFont(new Font("等线",Font.PLAIN,15));
        IDCondition.setBounds(140,320,200,30);
        //
        NameCondition = new JTextField();
        NameCondition.setFont(new Font("等线",Font.PLAIN,15));
        NameCondition.setBounds(140,370,200,30);
        //
        TelCondition = new JTextField();
        TelCondition.setFont(new Font("等线",Font.PLAIN,15));
        TelCondition.setBounds(140,420,200,30);
        //
        GroupCondition = new JTextField();
        GroupCondition.setFont(new Font("等线",Font.PLAIN,15));
        GroupCondition.setBounds(140,470,200,30);
        //
        IDCondition.setEditable(false);
        NameCondition.setEditable(false);
        TelCondition.setEditable(false);
        GroupCondition.setEditable(false);
    }

    private void setLabel(String UID,String UN,String US,String UM,String UC)//标签信息的方法
    {
        //Add
        InsertID1 = new JLabel("身份证号：");
        InsertID1.setFont(new Font("等线",Font.PLAIN,22));
        InsertID1.setBounds(170,35,120,50);
        //
        InsertName1 = new JLabel("姓名：");
        InsertName1.setFont(new Font("等线",Font.PLAIN,22));
        InsertName1.setBounds(170,95,120,50);
        //
        InsertGender1 = new JLabel("性别：");
        InsertGender1.setFont(new Font("等线",Font.PLAIN,22));
        InsertGender1.setBounds(170,155,120,50);
        //
        InsertNucAcid1 = new JLabel("核酸状态：");
        InsertNucAcid1.setFont(new Font("等线",Font.PLAIN,20));
        InsertNucAcid1.setBounds(170,215,120,50);
        //
        InsertAddress1 = new JLabel("家庭住址：");
        InsertAddress1.setFont(new Font("等线",Font.PLAIN,22));
        InsertAddress1.setBounds(170,275,120,50);
        //
        InsertTel1 = new JLabel("联系电话：");
        InsertTel1.setFont(new Font("等线",Font.PLAIN,22));
        InsertTel1.setBounds(170,335,120,50);
        //
        InsertGroup1 = new JLabel("组：");
        InsertGroup1.setFont(new Font("等线",Font.PLAIN,22));
        InsertGroup1.setBounds(60,395,60,50);
        //
        Loading = new JLabel("正在加载，请稍后……");
        Loading.setFont(new Font("等线",Font.PLAIN,22));
        Loading.setBounds(310,400,200,40);
        //Remove
        DeleteID1 = new JLabel("身份证号：");
        DeleteID1.setFont(new Font("等线",Font.PLAIN,22));
        DeleteID1.setBounds(150,140,150,50);
        //Update
        UpdateID1 = new JLabel("身份证号：");
        UpdateID1.setFont(new Font("等线",Font.PLAIN,22));
        UpdateID1.setBounds(150,105,150,50);
        //
        UpdateItem.setFont(new Font("等线",Font.PLAIN,20));
        UpdateItem.setBounds(135,245,130,40);
        UpdateItem.addItem("姓名");
        UpdateItem.addItem("身份证号");
        UpdateItem.addItem("家庭住址");
        UpdateItem.addItem("联系电话");
        UpdateItem.setSelectedIndex(-1);
        //Management
        CurrentAccount = new JLabel("当前账号：");
        CurrentAccount.setFont(new Font("等线",Font.PLAIN,22));
        CurrentAccount.setBounds(150,60,150,50);
        //
        CAContent = new JLabel(UN+"("+UID+")");
        UserID = new JLabel(UID);
        CAContent.setFont(new Font("等线",Font.PLAIN,22));
        CAContent.setBounds(260,60,400,50);
        //
        UserGroup = new JLabel("用户组：");
        UserGroup.setFont(new Font("等线",Font.PLAIN,22));
        UserGroup.setBounds(150,150,150,50);
        //
        UGContent = new JLabel(US+"-"+UM+"-"+UC);
        UGContent.setFont(new Font("等线",Font.PLAIN,15));
        UGContent.setBounds(260,150,600,50);
        UserSchool = new JLabel(US);
        UserMajor = new JLabel(UM);
        UserClass = new JLabel(UC);
    }

    private void setPanel()//面板信息的方法
    {
        Add = new JPanel();
        Remove = new JPanel();
        Update = new JPanel();
        Search = new JPanel();
        Mine = new JPanel();
    }

    private void setLayout(String UID)//布局信息的方法
    {
        //Add
        Add.setLayout(null);
        Add.add(Insert);
        Add.add(InsertReset);
        Add.add(InsertID1);
        Add.add(InsertName1);
        Add.add(InsertGender1);
        Add.add(InsertNucAcid1);
        Add.add(InsertAddress1);
        Add.add(InsertTel1);
        Add.add(InsertGroup1);
        Add.add(InsertID2);
        Add.add(InsertName2);
        Add.add(GenderItem);
        Add.add(AddNucAcid);
        Add.add(InsertAddress2);
        Add.add(InsertTel2);
        Add.add(Loading);
        //Remove
        Remove.setLayout(null);
        Remove.add(DeleteID1);
        Remove.add(DeleteID2);
        Remove.add(Delete);
        Remove.add(DeleteReset);
        //Update
        Update.setLayout(null);
        Update.add(UpdateID1);
        Update.add(UpdateID2);
        Update.add(UpdateItem);
        Update.add(UpdateContent);
        Update.add(Upgrade);
        Update.add(UpgradeReset);
        //Search
        Search.setLayout(null);
        Search.add(scroll);
        Search.add(AddSearchNucAcid);
        Search.add(Query);
        Search.add(QueryReset);
        Search.add(ID);
        Search.add(Name);
        Search.add(Tel);
        Search.add(Group);
        Search.add(IDCondition);
        Search.add(NameCondition);
        Search.add(TelCondition);
        Search.add(GroupCondition);
        //Manage
        Mine.setLayout(null);
        Mine.add(CurrentAccount);
        Mine.add(CAContent);
        Mine.add(UserGroup);
        Mine.add(UGContent);
        Mine.add(LGModify);
        Mine.add(AccountManage);
        Mine.add(AccountCancel);
        Mine.add(Logout);
        //为非管理员用户屏蔽管理页面
        if (UserSchool.getText().equals("Personal"))
        {
            Mine.remove(LGModify);
            Mine.remove(AccountManage);
            Search.remove(Name);
            Search.remove(Tel);
            Search.remove(Group);
            Search.remove(NameCondition);
            Search.remove(TelCondition);
            Search.remove(GroupCondition);
            ID.setEnabled(false);
            IDCondition.setText(UID);
            IDCondition.setEditable(false);
        }
        //为班级管理员屏蔽高级管理页面
        if (!UserClass.getText().equals("All"))
        {
            Mine.remove(LGModify);
            Mine.remove(AccountManage);
        }
    }

    private void setBase()//选项卡信息的方法
    {
        Base = new JTabbedPane(JTabbedPane.TOP);
        Base.addTab("新增基础信息",Add);
        Base.addTab("信息删除",Remove);
        Base.addTab("信息更新",Update);
        Base.addTab("信息查询",Search);
        Base.addTab("我的",Mine);
        //为非管理员用户屏蔽管理页面
        if (UserSchool.getText().equals("Personal"))
        {
            Base.remove(Add);
            Base.remove(Remove);
            Base.remove(Update);
        }
    }

    private void setThis()//主窗口信息的方法
    {
        this.add(Base);
        this.setTitle("疫情信息管理系统");
        this.setSize(800,620);
        this.setLocationRelativeTo(null);//窗体居中显示
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    void initial()//初始化
    {
        //按钮
        Insert.addActionListener(this);
        InsertReset.addActionListener(this);
        AddNucAcid.addActionListener(this);
        Delete.addActionListener(this);
        DeleteReset.addActionListener(this);
        Upgrade.addActionListener(this);
        UpgradeReset.addActionListener(this);
        AddSearchNucAcid.addActionListener(this);
        Query.addActionListener(this);
        QueryReset.addActionListener(this);
        LGModify.addActionListener(this);
        AccountManage.addActionListener(this);
        AccountCancel.addActionListener(this);
        Logout.addActionListener(this);
        //文本框
        ID.addItemListener(this);
        Name.addItemListener(this);
        Tel.addItemListener(this);
        Group.addItemListener(this);
        //复选框
        IDCondition.addActionListener(this);
        NameCondition.addActionListener(this);
        TelCondition.addActionListener(this);
        GroupCondition.addActionListener(this);
    }

    public void pullArray(String[] pullDate,String[] pullCondition)
    {
        Date = pullDate;
        Condition = pullCondition;
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        Object source = e.getSource();
        //Search复选框
        if (ID.equals(source))
        {
            IDCondition.setEditable(ID.isSelected());
            if (!ID.isSelected())
                IDCondition.setText("");
        }
        else if (Name.equals(source))
        {
            NameCondition.setEditable(Name.isSelected());
            if (!Name.isSelected())
                NameCondition.setText("");
        }
        else if (Tel.equals(source))
        {
            TelCondition.setEditable(Tel.isSelected());
            if (!Tel.isSelected())
                TelCondition.setText("");
        }
        else if (Group.equals(source))
        {
            GroupCondition.setEditable(Group.isSelected());
            if (!Group.isSelected())
                GroupCondition.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //下拉栏
        if (e.getSource().equals(GroupItemSchool))
        {
            try
            {
                String getSchool = (String)GroupItemSchool.getSelectedItem();
                GroupItemMajor.removeAllItems();
                GroupItemClass.removeAllItems();
                String[] Major = database.doLGQuery(getSchool);
                for (String s: Major) GroupItemMajor.addItem(s);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        if (e.getSource().equals(GroupItemMajor))
        {
            try
            {
                String getSchool = (String)GroupItemSchool.getSelectedItem();
                String getMajor = (String)GroupItemMajor.getSelectedItem();
                GroupItemClass.removeAllItems();
                String[] Class = database.doLGQuery(getSchool,getMajor);
                for (String s: Class) GroupItemClass.addItem(s);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        //重置
        if (e.getSource().equals(InsertReset))//添加
        {
            InsertID2.setText("");
            InsertName2.setText("");
            GenderItem.setSelectedIndex(-1);
            InsertAddress2.setText("");
            InsertTel2.setText("");
        }
        else if (e.getSource().equals(DeleteReset))//删除
            DeleteID2.setText("");
        else if (e.getSource().equals(UpgradeReset))//更新
        {
            UpdateItem.setSelectedIndex(-1);
            UpdateID2.setText("");
        }
        else if (e.getSource().equals(QueryReset))//查询
        {
            ID.setSelected(false);
            Name.setSelected(false);
            Tel.setSelected(false);
            Group.setSelected(false);
            IDCondition.setText("");
            NameCondition.setText("");
            TelCondition.setText("");
            GroupCondition.setText("");
            QueryRecordResult.setText("查询结果：");
        }
        if (e.getSource().equals(AddNucAcid))
        {
            if (!InsertID2.getText().isEmpty())
                new AddNucAcid(this,InsertID2.getText());
            else
                JOptionPane.showOptionDialog(this,"请先输入身份证号","温馨提示",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
        }
        if (e.getSource().equals(AddSearchNucAcid))
        {
            if (!IDCondition.getText().isEmpty())
                new AddNucAcid(this,IDCondition.getText());
            else
                JOptionPane.showOptionDialog(this,"请先输入身份证号","温馨提示",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
        }

        //功能
        if (e.getSource().equals(Insert))//添加
        {
            if (Date != null && Condition != null)
            {
                String InsertID = InsertID2.getText();
                String InsertName = InsertName2.getText();
                String InsertAddress = InsertAddress2.getText();
                String InsertTel = InsertTel2.getText();
                String InsertGenderItem = (String)GenderItem.getSelectedItem();
                String InsertGroupSchool = (String)GroupItemSchool.getSelectedItem();
                String InsertGroupMajor = (String)GroupItemMajor.getSelectedItem();
                String InsertGroupClass = (String)GroupItemClass.getSelectedItem();
                String InsertGroup = InsertGroupSchool+"-"+InsertGroupMajor+"-"+InsertGroupClass;
                if (!Objects.equals(InsertGroupSchool,null) && !Objects.equals(InsertGroupMajor,null) &&
                        !Objects.equals(InsertGroupClass,null))
                    if (InsertID.length() == 18)
                    {
                        try
                        {
                            database.setResult(database.doInfoQuery(InsertID));
                            if (!database.getResult().next())
                            {
                                database.doInsert(InsertID,InsertName,InsertGenderItem,InsertAddress,
                                        InsertTel,InsertGroup);
                                String EnID = DigestUtils.md5Hex(InsertID).toUpperCase();
                                String EnPasswd = DigestUtils.md5Hex(InsertID.substring(12)+EnID).toUpperCase();
                                database.doRegister(EnID,InsertName,EnPasswd,"Personal","Personal","Personal");
                                JOptionPane.showOptionDialog(this,"信息添加成功","温馨提示",JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
                            }
                            else
                                JOptionPane.showOptionDialog(this,"该信息已存在","温馨提示",JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                        finally
                        {
                            database.CloseResult();
                            database.CloseStatement();
                            database.CloseConnection();
                        }
                    }
                    else
                        JOptionPane.showOptionDialog(this,"身份证号格式错误","温馨提示",JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,null,null,null);
                else
                    JOptionPane.showOptionDialog(this,"权限不足或组选择有误","温馨提示",JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,null,null,null);
            }
            else
                JOptionPane.showOptionDialog(this,"请添加核酸检测信息","温馨提示",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,null,null,null);
        }
        else if (e.getSource().equals(Delete))//删除
        {
            String DeleteID = DeleteID2.getText();
            try
            {
                database.setResult(database.doInfoQuery(DeleteID));
                database.getResult().next();
                String DeleteGroup = database.getResult().getString(6);
                String[] getGroup = DeleteGroup.split("-");
                if (UserSchool.getText().equals("All") ||
                        (UserSchool.getText().equals(getGroup[0]) && UserMajor.getText().equals("All")) ||
                        (UserSchool.getText().equals(getGroup[0]) && UserMajor.getText().equals(getGroup[1]) &&
                                UserClass.getText().equals("All")) ||
                        (UserSchool.getText().equals(getGroup[0]) && UserMajor.getText().equals(getGroup[1]) &&
                                UserClass.getText().equals(getGroup[2])))
                {
                    database.setResult(database.doInfoQuery(DeleteID));
                    if (database.getResult().next())
                    {
                        database.doInfoDelete(DeleteID);
                        JOptionPane.showOptionDialog(this,"删除成功！","温馨提示",
                                JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,null,null);
                    }
                    else
                        JOptionPane.showOptionDialog(this,"删除失败","温馨提示",
                                JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,null,null);
                }
                else
                    JOptionPane.showOptionDialog(this,"权限不足，删除失败","温馨提示",
                            JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,null,null);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
        else if (e.getSource().equals(Upgrade))//修改
        {
            String UpdateID = UpdateID2.getText();
            try
            {
                database.setResult(database.doInfoQuery(UpdateID));
                database.getResult().next();
                String DeleteGroup = database.getResult().getString(6);
                String[] getGroup = DeleteGroup.split("-");
                if (UserSchool.getText().equals("All") ||
                        (UserSchool.getText().equals(getGroup[0]) && UserMajor.getText().equals("All")) ||
                        (UserSchool.getText().equals(getGroup[0]) && UserMajor.getText().equals(getGroup[1]) &&
                                UserClass.getText().equals("All")) ||
                        (UserSchool.getText().equals(getGroup[0]) && UserMajor.getText().equals(getGroup[1]) &&
                                UserClass.getText().equals(getGroup[2])))
                {
                    database.setResult(database.doInfoQuery(UpdateID));
                    if (!database.getResult().next())
                        JOptionPane.showOptionDialog(this,"没有记录无法更新","温馨提示",JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,null,null,null);
                    else
                    {
                        String updateItem = null;
                        if (Objects.equals(UpdateItem.getSelectedItem(),"姓名"))
                            updateItem = "Name";
                        else if (Objects.equals(UpdateItem.getSelectedItem(),"身份证号"))
                            updateItem = "ID";
                        else if (Objects.equals(UpdateItem.getSelectedItem(),"家庭住址"))
                            updateItem = "Address";
                        else if (Objects.equals(UpdateItem.getSelectedItem(),"联系电话"))
                            updateItem = "Tel";
                        database.doInfoUpdate(UpdateID,updateItem,UpdateContent.getText());
                        JOptionPane.showOptionDialog(this,"更新成功！","温馨提示",JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,null,null,null);
                    }
                }
                else
                    JOptionPane.showOptionDialog(this," 权限不足，更新失败！","温馨提示",JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,null,null,null);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
            finally
            {
                database.CloseResult();
                database.CloseStatement();
                database.CloseConnection();
            }

        }
        else if (e.getSource().equals(Query) || e.getSource().equals(IDCondition) ||
                e.getSource().equals(NameCondition) || e.getSource().equals(TelCondition) ||
                e.getSource().equals(GroupCondition))//查询
            //管理员查询
            if (!UserClass.getText().equals("Personal"))
            {
                try
                {
                    // 默认设置各检索条件均为通配符
                    String ID = "%", Name = "%", Tel = "%", Group = "%";
                    if (this.ID.isSelected() && !IDCondition.getText().trim().isEmpty())
                        ID = IDCondition.getText();
                    if (this.Name.isSelected() && !NameCondition.getText().trim().isEmpty())
                        Name = NameCondition.getText();
                    if (this.Tel.isSelected() && !TelCondition.getText().trim().isEmpty())
                        Tel = TelCondition.getText();
                    if (this.Group.isSelected() && !GroupCondition.getText().trim().isEmpty())
                        Group = GroupCondition.getText();
                    //根据各选项检索关键字进行查询，并返回结果集
                    database.setResult(database.doQueryByCondition(ID,Name,Tel,Group));
                    ResultSet resultSet = database.getResult();
                    //定义结果集中记录条数
                    int i = 0;
                    QueryRecordResult.setText("查询结果：");
                    //输出前半结果集记录
                    while (resultSet.next())
                    {
                        String QueryGroup = resultSet.getString(6);
                        String[] getGroup = QueryGroup.split("-");
                        if (UserSchool.getText().equals("All") ||
                                (UserSchool.getText().equals(getGroup[0]) && UserMajor.getText().equals("All")) ||
                                (UserSchool.getText().equals(getGroup[0]) &&
                                        UserMajor.getText().equals(getGroup[1]) &&
                                        UserClass.getText().equals("All")) ||
                                (UserSchool.getText().equals(getGroup[0]) &&
                                        UserMajor.getText().equals(getGroup[1]) &&
                                        UserClass.getText().equals(getGroup[2])))
                        {
                            ++i;
                            QueryRecordResult.append("\r\n"+"第"+i+"条记录："+"\r\n"
                                    +"身份证号："+resultSet.getString(1)+"\r\n"
                                    +"姓名："+resultSet.getString(2)+"\r\n"
                                    +"性别："+resultSet.getString(3)+"\r\n");
                            String Address = "家庭住址："+resultSet.getString(4)+"\r\n"
                                    +"联系电话："+resultSet.getString(5)+"\r\n"
                                    +"组："+resultSet.getString(6)+
                                    ("\r\n---------------------------------------------");
                            String IDNow = resultSet.getString(1);
                            //输出核酸查询结果集和后半结果集
                            String[][] DateArray = database.doNucAcidConditionDateQuery(IDNow);
                            for (int j = 0;j<DateArray.length && j<2;j++)
                                QueryRecordResult.append("核酸检测 "+(j+1)+"："+"\r\n"
                                        +"日期："+DateArray[j][0]+"-"+DateArray[j][1]+"-"+DateArray[j][2]+"\r\n"
                                        +"结果："+DateArray[j][3]+"\r\n");
                            QueryRecordResult.append(Address);
                        }
                    }
                    QueryRecordResult.setText(QueryRecordResult.getText()+
                            "\r\n"+"共有"+i+"条信息记录");

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
            //非管理员查询
            else
            {
                try
                {
                    // 默认设置各检索条件均为通配符
                    String ID = "%";
                    if (this.ID.isSelected() && !IDCondition.getText().trim().isEmpty())
                        ID = IDCondition.getText();
                    //根据各选项检索关键字进行查询，并返回结果集
                    database.setResult(database.doQueryByCondition(ID,"%","%","%"));
                    ResultSet resultSet = database.getResult();
                    //定义结果集中记录条数
                    int i = 0;
                    QueryRecordResult.setText("查询结果：");
                    //输出前半结果集记录
                    while (resultSet.next())
                    {
                        if (resultSet.getString(1).equals(UserID.getText()))
                        {
                            ++i;
                            QueryRecordResult.append("\r\n"+"第"+i+"条记录："+"\r\n"
                                    +"身份证号："+resultSet.getString(1)+"\r\n"
                                    +"姓名："+resultSet.getString(2)+"\r\n"
                                    +"性别："+resultSet.getString(3)+"\r\n");
                            String Address = "家庭住址："+resultSet.getString(4)+"\r\n"
                                    +"联系电话："+resultSet.getString(5)+"\r\n"
                                    +"组："+resultSet.getString(6)+
                                    ("\r\n---------------------------------------------");
                            String IDNow = resultSet.getString(1);
                            //输出核酸查询结果集和后半结果集
                            String[][] DateArray = database.doNucAcidConditionDateQuery(IDNow);
                            for (int j = 0;j<DateArray.length && j<2;j++)
                                QueryRecordResult.append("核酸检测 "+(j+1)+"："+"\r\n"
                                        +"日期："+DateArray[j][0]+"-"+DateArray[j][1]+"-"+DateArray[j][2]+"\r\n"
                                        +"结果："+DateArray[j][3]+"\r\n");
                            QueryRecordResult.append(Address);
                        }
                    }
                    if (i == 0)
                        QueryRecordResult.append("\r\n"+"你还没有录入有效的个人信息，请使用手机端添加个人信息，或联系管理员添加个人信息！");
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

        //管理界面
        try
        {
            if (e.getSource().equals(LGModify))
                new LGModifyUI(UserSchool.getText(),UserMajor.getText(),UserClass.getText());
            else if (e.getSource().equals(AccountManage))
                new AccountManagementUI(UserID.getText(),UserSchool.getText(),UserMajor.getText(),
                        UserClass.getText());
            else if (e.getSource().equals(AccountCancel))
                new AccountCancellationUI(UserID.getText());
            else if (e.getSource().equals(Logout))
                System.exit(0);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

