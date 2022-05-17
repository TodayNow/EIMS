package com.oniokey.eims;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

/**
 * @author iokey
 */
public class LGModifyUI extends JFrame implements ItemListener, ActionListener
{
    //数据库
    private final OperateMySQL database = new OperateMySQL();
    //下拉栏
    private final JComboBox<String> GroupItemSchool = new JComboBox<>();//学院组选择
    private final JComboBox<String> GroupItemMajor = new JComboBox<>();//专业组选择
    private final JComboBox<String> GroupItemClass = new JComboBox<>();//班级组选择
    //嵌板
    private final JPanel main = new JPanel();
    private JButton Submit, Cancel;
    private JRadioButton Add, Minus;
    private JLabel Group, Loading;

    LGModifyUI(String US,String UM,String UC)
    {
        new Thread(() ->
        {
            setButton();//设置各按钮信息
            setLabel();//设置各标签信息
            setTextField();//设置各文本框信息
            setLayout();//设置布局信息
            setThis(); //设置主窗口信息
            initial();//初始化按键监听
        }).start();

        new Thread(() ->
        {
            GroupItemSchool.addActionListener(this);
            GroupItemMajor.addActionListener(this);
            GroupItemClass.addActionListener(this);
            try
            {
                setComboBox(US,UM,UC);//设置动态下拉栏联动
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            main.add(GroupItemSchool);
            main.add(GroupItemMajor);
            main.add(GroupItemClass);
            main.remove(Loading);
            main.updateUI();
        }).start();
    }

    private void setButton()
    {
        //
        Submit = new JButton("提交");
        Submit.setFont(new Font("等线",Font.PLAIN,20));
        Submit.setBounds(270,360,100,45);
        Submit.setMargin(new Insets(0,0,0,0));
        //
        Cancel = new JButton("取消");
        Cancel.setFont(new Font("等线",Font.PLAIN,20));
        Cancel.setBounds(420,360,100,45);
        Cancel.setMargin(new Insets(0,0,0,0));
        //
        Add = new JRadioButton("新增");
        Add.setFont(new Font("等线",Font.PLAIN,20));
        Add.setBounds(300,60,120,50);
        Add.setMargin(new Insets(0,0,0,0));
        //
        Minus = new JRadioButton("删除");
        Minus.setFont(new Font("等线",Font.PLAIN,20));
        Minus.setBounds(460,60,120,50);
        Minus.setMargin(new Insets(0,0,0,0));
        //
        //按钮组
        ButtonGroup choice = new ButtonGroup();
        choice.add(Add);
        choice.add(Minus);
    }

    private void setLabel()
    {
        Group = new JLabel("组：");
        Group.setFont(new Font("等线",Font.PLAIN,22));
        Group.setBounds(100,195,200,40);
        //
        Loading = new JLabel("正在加载，请稍后……");
        Loading.setFont(new Font("等线",Font.PLAIN,22));
        Loading.setBounds(340,195,200,40);
    }

    private void setTextField()
    {
        //
        GroupItemSchool.setFont(new Font("等线",Font.PLAIN,15));
        GroupItemSchool.setBounds(140,195,200,40);
        //
        GroupItemMajor.setFont(new Font("等线",Font.PLAIN,15));
        GroupItemMajor.setBounds(340,195,200,40);
        //
        GroupItemClass.setFont(new Font("等线",Font.PLAIN,15));
        GroupItemClass.setBounds(540,195,200,40);
    }

    private void setLayout()
    {
        main.setLayout(null);
        main.add(Submit);
        main.add(Cancel);
        main.add(Add);
        main.add(Minus);
        main.add(Group);
        main.add(Loading);
    }

    private void setThis()
    {
        this.add(main);
        this.setTitle("组管理");
        this.setSize(800,550);
        this.setLocationRelativeTo(null);//窗体居中显示
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void initial()
    {
        //单选按钮
        Add.addItemListener(this);
        Minus.addItemListener(this);
        //按钮
        Submit.addActionListener(this);
        Cancel.addActionListener(this);
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
        if (Objects.equals(UC,"Personal")) GroupItemClass.setEnabled(false);
        else if (!Objects.equals(UC,"All"))
        {
            GroupItemClass.removeAllItems();
            GroupItemClass.addItem(UC);
            GroupItemClass.setSelectedIndex(0);
            GroupItemClass.setEnabled(false);
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
        //提交
        if (e.getSource().equals(Submit))
        {
            String School = (String)GroupItemSchool.getSelectedItem();
            String Major = (String)GroupItemMajor.getSelectedItem();
            String Class = (String)GroupItemClass.getSelectedItem();
            if (Submit.getText().equals("新增"))
            {
                try
                {
                    database.doAddGroup(School,Major,Class);
                    JOptionPane.showOptionDialog(this,"添加成功","温馨提示",JOptionPane.DEFAULT_OPTION,
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
            if (Submit.getText().equals("删除"))
            {
                try
                {
                    database.doDelGroup(School,Major,Class);
                    JOptionPane.showOptionDialog(this,"删除成功","温馨提示",JOptionPane.DEFAULT_OPTION,
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
        }
        //返回
        if (e.getSource().equals(Cancel))
            this.dispose();
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource().equals(Add))
        {
            Submit.setText("新增");
            GroupItemSchool.setEditable(true);
            GroupItemMajor.setEditable(true);
            GroupItemClass.setEditable(true);
        }
        if (e.getSource().equals(Minus))
        {
            Submit.setText("删除");
            GroupItemSchool.setEditable(false);
            GroupItemMajor.setEditable(false);
            GroupItemClass.setEditable(false);
        }
    }
}
