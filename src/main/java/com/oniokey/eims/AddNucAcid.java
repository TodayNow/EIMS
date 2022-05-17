package com.oniokey.eims;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddNucAcid extends JFrame implements ActionListener
{
    private final OperateMySQL database = new OperateMySQL();
    //检测结果单选按钮列表
    private final ArrayList<JRadioButton> Negative = new ArrayList<>();
    private final ArrayList<JRadioButton> Positive = new ArrayList<>();
    private final ArrayList<ButtonGroup> Result = new ArrayList<>();
    //标签列表
    private final ArrayList<JLabel> TimeLabel = new ArrayList<>();
    private final ArrayList<JLabel> ResultLabel = new ArrayList<>();
    //下拉栏列表
    private final ArrayList<JComboBox<String>> Year = new ArrayList<>();
    private final ArrayList<JComboBox<String>> Month = new ArrayList<>();
    private final ArrayList<JComboBox<String>> Day = new ArrayList<>();
    //
    private final String[] ComboYear = new String[5];
    private final String[] ComboMonth = new String[12];
    private final String[] ComboDay = new String[31];
    private final MainUI what;
    private final String ID;
    //日历类
    private final Calendar calendar = Calendar.getInstance();
    // 获取当前年
    int year = calendar.get(Calendar.YEAR);
    // 获取当前月
    int month = calendar.get(Calendar.MONTH) + 1;
    // 获取当前日
    int day = calendar.get(Calendar.DATE);
    private JPanel main, Details;
    private JButton Submit, Back, Add, Minus;
    private JScrollPane scroll;
    //控制量
    private int//下标
            NegativeIndex = 0,//阴性按钮
            PositiveIndex = 0,//阳性按钮
            ResultIndex = 0,//按钮组
            TimeLabelIndex = 0,//检测时间标签
            ResultLabelIndex = 0,//检测结果标签
            YearIndex = 0,//年
            MonthIndex = 0,//月
            DayIndex = 0;//日
    private int//计数
            TimeCount = 1,//时间
            ResultCount = 1;//结果
    private int//坐标
            ButtonY = 100,//按钮们
            LabelY = 35,//标签们
            TextFieldY = 40,//输入框们
            DetailsHeight = 145;//滚动板高度

    AddNucAcid(MainUI get,String getID)
    {
        setButton();//设置各按钮信息
        setLabel();//设置各标签信息
        setTextField();//设置各文本框信息
        setPanel();//设置各面板信息
        setLayout();//设置布局信息
        setThis(); //设置主窗口信息
        initial();//初始化按键监听
        what = get;
        ID = getID;
    }

    private void setButton()
    {
        Submit = new JButton("确定");
        Submit.setFont(new Font("等线",Font.PLAIN,20));
        Submit.setBounds(270,510,100,45);
        Submit.setMargin(new Insets(0,0,0,0));
        //
        Back = new JButton("返回");
        Back.setFont(new Font("等线",Font.PLAIN,20));
        Back.setBounds(420,510,100,45);
        Back.setMargin(new Insets(0,0,0,0));
        //
        Add = new JButton("+");
        Add.setFont(new Font("等线",Font.PLAIN,20));
        Add.setBounds(600,ButtonY,40,40);
        Add.setMargin(new Insets(0,0,0,0));
        //
        Minus = new JButton("-");
        Minus.setFont(new Font("等线",Font.PLAIN,20));
        Minus.setBounds(650,ButtonY,40,40);
        Minus.setMargin(new Insets(0,0,0,0));
    }

    private void setLabel()
    {
        TimeLabel.add(new JLabel("检测时间 " + TimeCount + "："));
        TimeLabel.get(TimeLabelIndex).setFont(new Font("等线",Font.PLAIN,18));
        TimeLabel.get(TimeLabelIndex).setBounds(160,LabelY,120,50);
        //
        ResultLabel.add(new JLabel("检测结果 " + ResultCount + "："));
        ResultLabel.get(ResultLabelIndex).setFont(new Font("等线",Font.PLAIN,18));
        ResultLabel.get(ResultLabelIndex).setBounds(160,LabelY + 60,120,50);
    }

    private void setTextField()
    {
        //
        for (int i = 0, j = - 2;i < 5;++ i,++ j) ComboYear[i] = year + j + "年";
        Year.add(new JComboBox<>(ComboYear));
        Year.get(YearIndex).setFont(new Font("等线",Font.PLAIN,15));
        Year.get(YearIndex).setBounds(290,TextFieldY,100,40);
        Year.get(YearIndex).setSelectedIndex(2);
        //
        for (int i = 0, month = 1;month <= 12;++ i,++ month) ComboMonth[i] = month + "月";
        Month.add(new JComboBox<>(ComboMonth));
        Month.get(MonthIndex).setFont(new Font("等线",Font.PLAIN,15));
        Month.get(MonthIndex).setBounds(390,TextFieldY,100,40);
        Month.get(MonthIndex).setSelectedIndex(month - 1);
        //
        for (int i = 0, day = 1;day <= 31;++ i,++ day) ComboDay[i] = day + "日";
        Day.add(new JComboBox<>(ComboDay));
        Day.get(DayIndex).setFont(new Font("等线",Font.PLAIN,15));
        Day.get(DayIndex).setBounds(490,TextFieldY,100,40);
        Day.get(DayIndex).setSelectedIndex(day - 1);
        //
        Negative.add(new JRadioButton("阴性   "));
        Negative.get(NegativeIndex).setFont(new Font("等线",Font.PLAIN,20));
        Negative.get(NegativeIndex).setBounds(340,TextFieldY + 60,100,40);
        Negative.get(NegativeIndex).setMargin(new Insets(0,0,0,0));
        //
        Positive.add(new JRadioButton("阳性"));
        Positive.get(PositiveIndex).setFont(new Font("等线",Font.PLAIN,20));
        Positive.get(PositiveIndex).setBounds(460,TextFieldY + 60,100,40);
        Positive.get(PositiveIndex).setMargin(new Insets(0,0,0,0));
        //
        Result.add(new ButtonGroup());
        Result.get(ResultIndex).add(Negative.get(NegativeIndex));
        Result.get(ResultIndex).add(Positive.get(PositiveIndex));
    }

    private void setPanel()
    {
        main = new JPanel();
        Details = new JPanel();
        Details.setPreferredSize(new Dimension(800,DetailsHeight));
        scroll = new JScrollPane(Details);// 添加滚动条
        main.add(scroll);
        scroll.setBounds(0,0,800,480);
        scroll.setOpaque(false);//背景透明
        scroll.setBorder(null);//无边框
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);//当需要垂直滚动条时显示
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);//从不显示水平滚动条
    }

    private void setLayout()
    {
        main.setLayout(null);
        main.add(Submit);
        main.add(Back);
        Details.setLayout(null);
        Details.add(Add);
        Details.add(Minus);
        Details.add(TimeLabel.get(0));
        Details.add(ResultLabel.get(0));
        Details.add(Negative.get(0));
        Details.add(Positive.get(0));
        Details.add(Year.get(0));
        Details.add(Month.get(0));
        Details.add(Day.get(0));
    }

    private void setThis()
    {
        this.add(main);
        this.setTitle("添加核酸检测结果");
        this.setSize(800,620);
        this.setLocationRelativeTo(null);//窗体居中显示
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void initial()
    {
        //按钮
        Submit.addActionListener(this);
        Back.addActionListener(this);
        Add.addActionListener(this);
        Minus.addActionListener(this);
        //下拉栏
        Month.get(0).addActionListener(this);
    }

    public String[] getDate()
    {
        String[] arrYear = new String[Year.size()];
        String[] arrMonth = new String[Month.size()];
        String[] arrDay = new String[Day.size()];
        String[] arrDate = new String[arrYear.length];
        for (int i = 0;i < Year.size();++ i)
        {
            String str = (String)Year.get(i).getSelectedItem();
            assert str != null;
            arrYear[i] = str.substring(0,str.length() - 1);
        }
        for (int i = 0;i < Month.size();++ i)
        {
            String str = (String)Month.get(i).getSelectedItem();
            assert str != null;
            arrMonth[i] = str.substring(0,str.length() - 1);
        }
        for (int i = 0;i < Day.size();++ i)
        {
            String str = (String)Day.get(i).getSelectedItem();
            assert str != null;
            arrDay[i] = str.substring(0,str.length() - 1);
        }
        for (int i = 0;i < arrYear.length;++ i)
            arrDate[i] = arrYear[i] + "-" + arrMonth[i] + "-" + arrDay[i];
        return arrDate;
    }

    public String[] getCondition()
    {
        String[] arrCondition = new String[Result.size()];
        for (int i = 0;i < Result.size();++ i)
            if (Negative.get(i).isSelected()) arrCondition[i] = "阴性";
            else arrCondition[i] = "阳性";
        return arrCondition;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Add
        if (e.getSource().equals(Add))
        {
            //创建新提示标签
            TimeLabelIndex++;
            ResultLabelIndex++;
            ResultCount++;
            TimeCount++;
            LabelY = LabelY + 145;
            TextFieldY = TextFieldY + 145;
            setLabel();
            Details.add(TimeLabel.get(TimeLabelIndex));
            Details.add(ResultLabel.get(ResultLabelIndex));
            //创建新日期下拉栏和结果单选按钮
            NegativeIndex++;
            PositiveIndex++;
            YearIndex++;
            MonthIndex++;
            DayIndex++;
            ResultIndex++;
            setTextField();
            Details.add(Negative.get(NegativeIndex));
            Details.add(Positive.get(PositiveIndex));
            Details.add(Year.get(YearIndex));
            Details.add(Month.get(MonthIndex));
            Details.add(Day.get(DayIndex));
            Month.get(MonthIndex).addActionListener(this);
            //移动按钮"+"和"-"到新数据组后面
            ButtonY = ButtonY + 145;
            Add.setBounds(600,ButtonY,40,40);
            Minus.setBounds(650,ButtonY,40,40);
            //扩展嵌板长度
            DetailsHeight = DetailsHeight + 145;
            Details.setPreferredSize(new Dimension(800,DetailsHeight));
            Details.updateUI();
            //将滚动条移动到最底部
            scroll.getViewport().setViewPosition(new Point(600,ButtonY));
            //创建监听
            Month.get(MonthIndex).addActionListener(this);
        }
        //Minus
        if (e.getSource().equals(Minus))
        {
            if (TimeLabelIndex > 0)
            {
                //删除最后的提示标签
                Details.remove(TimeLabel.get(TimeLabelIndex));
                Details.remove(ResultLabel.get(ResultLabelIndex));
                TimeLabelIndex--;
                ResultLabelIndex--;
                ResultCount--;
                TimeCount--;
                LabelY = LabelY - 145;
                TextFieldY = TextFieldY - 145;
                //删除最后的日期下拉栏和结果单选按钮
                Details.remove(Negative.get(NegativeIndex));
                Details.remove(Positive.get(PositiveIndex));
                Details.remove(Year.get(YearIndex));
                Details.remove(Month.get(MonthIndex));
                Details.remove(Day.get(DayIndex));
                Month.get(MonthIndex).addActionListener(this);
                NegativeIndex--;
                PositiveIndex--;
                YearIndex--;
                MonthIndex--;
                DayIndex--;
                ResultIndex--;
                //移动按钮"+"和"-"到新数据组后面
                ButtonY = ButtonY - 145;
                Add.setBounds(600,ButtonY,40,40);
                Minus.setBounds(650,ButtonY,40,40);
                //缩减嵌板长度
                DetailsHeight = DetailsHeight - 145;
                Details.setPreferredSize(new Dimension(800,DetailsHeight));
                Details.updateUI();
                //将滚动条移动到最底部
                scroll.getViewport().setViewPosition(new Point(600,ButtonY));
            }
            else JOptionPane.showOptionDialog(this,"请至少填写一组相关信息","温馨提示",JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,null,null,null);
        }
        if (e.getSource().equals(Submit))
        {
            what.pullArray(getDate(),getCondition());
            String[] Date = getDate();
            String[] Condition = getCondition();
            for (int i = 0;i < Date.length;++ i)
            {
                try
                {
                    database.doNucAcidInsert(ID,Date[i],Condition[i]);
                }
                catch (Exception ex)
                {
                    throw new RuntimeException(ex);
                }
            }
            JOptionPane.showOptionDialog(this,"添加成功","温馨提示",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,
                    null,null,null);
            this.dispose();
        }
        //返回
        if (e.getSource().equals(Back))
        {
            JOptionPane.showOptionDialog(this,"数据未添加，您确定要退出吗","温馨提示",JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,null,null,null);
            this.dispose();
        }
    }
}
