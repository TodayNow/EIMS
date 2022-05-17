package com.oniokey.eims;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author iokey
 */
public class OperateMySQL
{
    private Connection connection = null;//数据库链接
    private PreparedStatement statement = null;//链接状态
    private ResultSet result = null;//返回结果

    private Connection CreateConnection() throws Exception//链接数据库
    {
        //数据库用户名
        String username = "eims";
        //密码
        String passwd = "Jrz0720*";
        //MySQL数据库链接地址
        String url = "jdbc:mysql://oniokeys.mysql.rds.aliyuncs.com/eims";
        //数据库驱动
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,passwd);//创建连接并返回该连接
    }

    public ResultSet getResult()//返回结果集
    {
        return result;
    }

    public void setResult(ResultSet result)//结果集
    {
        this.result = result;
    }

    public void CloseResult()//关闭结果集
    {
        try
        {
            result.close();
        }
        catch (SQLException e)
        {
            System.out.println("关闭结果集时发生错误！");
        }
    }

    public void CloseStatement()//关闭状态
    {
        try
        {
            statement.close();
        }
        catch (SQLException e)
        {
            System.out.println("关闭状态时发生错误！");
        }
    }

    public void CloseConnection()//关闭连接
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println("关闭连接时发生错误！");
        }
    }

    //新增
    void doInsert(String InsertID,String InsertName,String InsertGender,String InsertAddress,String InsertTel,
                  String InsertGroup) throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("insert into eims.information values(?,?,?,?,?,?)");
        statement.setString(1,InsertID);
        statement.setString(2,InsertName);
        statement.setString(3,InsertGender);
        statement.setString(4,InsertAddress);
        statement.setString(5,InsertTel);
        statement.setString(6,InsertGroup);
        statement.executeUpdate();
    }

    //新增核酸信息
    void doNucAcidInsert(String ID,String Date,String Condition) throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("insert into eims.`nuc-acid` value(?,?,?)");
        statement.setString(1,ID);
        statement.setString(2,Date);
        statement.setString(3,Condition);
        statement.executeUpdate();
    }

    //信息删除
    void doInfoDelete(String DeleteID) throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("delete from eims.information where ID = ?");
        statement.setString(1,DeleteID);
        statement.executeUpdate();
        statement = connection.prepareStatement("delete from eims.`nuc-acid` where ID = ?");
        statement.setString(1,DeleteID);
        statement.executeUpdate();
    }

    //更新
    void doInfoUpdate(String UpdateID,String UpdateItem,String UpdateContent) throws Exception
    {
        connection = CreateConnection();
        String sql = "update eims.information set "+UpdateItem+" = ? where ID = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1,UpdateContent);
        statement.setString(2,UpdateID);
        statement.executeUpdate();
    }

    //信息查找
    ResultSet doInfoQuery(String ID) throws Exception
    {
        String sql = "select * from eims.information where ID = ?";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        statement.setString(1,ID);
        result = statement.executeQuery();
        return result;
    }

    //查找核酸状态
    String[][] doNucAcidConditionDateQuery(@NotNull String ID) throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("select * from eims.`nuc-acid` where ID like ?");
        if (ID.equals("%")) statement.setString(1,"%");
        else statement.setString(1,"%"+ID+"%");
        result = statement.executeQuery();
        List<String> dateList = new ArrayList<>();
        while (result.next()) dateList.add(result.getString(2)+"-"+result.getString(3));
        String[] dateArray = new String[dateList.size()];
        for (int i = 0;i<dateList.size();++i)
            dateArray[i] = dateList.get(i);
        String[][] Array = new String[dateArray.length][4];
        for (int i = 0;i<dateArray.length;++i)
            Array[i] = dateArray[i].split("-");
        sort(Array,new int[]{0,1,2});
        return Array;
    }

    //条件查找
    ResultSet doQueryByCondition(@NotNull String ID,String Name,String Tel,String Group) throws Exception
    {
        String sql =
                "select * from eims.information where ID like ? and Name like ? and Tel like ? and `Group` like ? order by ID";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        if (ID.equals("%")) statement.setString(1,"%");
        else statement.setString(1,"%"+ID+"%");
        if (Name.equals("%")) statement.setString(2,"%");
        else statement.setString(2,"%"+Name+"%");
        if (Tel.equals("%")) statement.setString(3,"%");
        else statement.setString(3,"%"+Tel+"%");
        if (Group.equals("%")) statement.setString(4,"%");
        else statement.setString(4,"%"+Group+"%");
        result = statement.executeQuery();
        return result;
    }

    //注册
    void doRegister(String InsertID,String InsertName,String InsertPasswd,String School,String Major,String Class)
            throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("insert into eims.account values(?,?,?,?,?,?)");
        statement.setString(1,InsertID);
        statement.setString(2,InsertName);
        statement.setString(3,InsertPasswd);
        statement.setString(4,School);
        statement.setString(5,Major);
        statement.setString(6,Class);
        statement.executeUpdate();
    }

    //修改密码
    void doPasswdModify(String ID,String Passwd) throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("update eims.account set Passwd = ? where ID = ?");
        statement.setString(1,Passwd);
        statement.setString(2,ID);
        statement.executeUpdate();
    }

    //删除校验码
    void doValueCodeDelete(String DeleteValueCode) throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("delete from eims.valuecode where Code = ?");
        statement.setString(1,DeleteValueCode);
        statement.executeUpdate();
    }

    //账号查找
    ResultSet doAccountQuery(String ID) throws Exception
    {
        String sql = "select * from eims.account where ID = ?";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        statement.setString(1,ID);
        result = statement.executeQuery();
        return result;
    }

    //校验码查找
    ResultSet doValueCodeQuery(@NotNull String CodeOrAuthor) throws Exception
    {
        String sql;
        if (CodeOrAuthor.length() == 6) sql = "select * from eims.valuecode where Code = ? order by Code";
        else sql = "select * from eims.valuecode where Author = ? order by Code";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        statement.setString(1,CodeOrAuthor);
        result = statement.executeQuery();
        return result;
    }

    //权限更新
    void doAccountUpdate(String EnID,String School,String Major,String Class) throws Exception
    {
        connection = CreateConnection();
        String sql = "update eims.account set School = ? , Major = ? , Class = ? where ID = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1,School);
        statement.setString(2,Major);
        statement.setString(3,Class);
        statement.setString(4,EnID);
        statement.executeUpdate();
    }

    //生成校验码
    void doValueCodeInsert(String Code,String School,String Major,String Class,String Author) throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("insert into eims.valuecode values(?,?,?,?,?)");
        statement.setString(1,Code);
        statement.setString(2,School);
        statement.setString(3,Major);
        statement.setString(4,Class);
        statement.setString(5,Author);
        statement.executeUpdate();
    }

    //账号删除
    void doAccountDelete(String EnID) throws Exception
    {
        connection = CreateConnection();
        statement = connection.prepareStatement("delete from eims.account where ID = ?");
        statement.setString(1,EnID);
        statement.executeUpdate();
    }

    //级-组查询
    String[] doLGQuery() throws Exception
    {
        String sql = "select distinct School from eims.`level-group`";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        result = statement.executeQuery();
        List<String> list = new ArrayList<>();
        while (result.next()) list.add(result.getString(1));
        String[] arr = new String[list.size()];
        for (int i = 0;i<list.size();++i)
            arr[i] = list.get(i);
        return arr;
    }

    String[] doLGQuery(String School) throws Exception
    {
        String sql = "select distinct Major from eims.`level-group` where School = ?";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        statement.setString(1,School);
        result = statement.executeQuery();
        List<String> list = new ArrayList<>();
        while (result.next()) list.add(result.getString(1));
        String[] arr = new String[list.size()];
        for (int i = 0;i<list.size();++i)
            arr[i] = list.get(i);
        return arr;
    }

    String[] doLGQuery(String School,String Major) throws Exception
    {
        String sql = "select distinct Class from eims.`level-group` where School = ? and Major = ?";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        statement.setString(1,School);
        statement.setString(2,Major);
        result = statement.executeQuery();
        List<String> list = new ArrayList<>();
        while (result.next()) list.add(result.getString(1));
        String[] arr = new String[list.size()];
        for (int i = 0;i<list.size();++i)
            arr[i] = list.get(i);
        return arr;
    }

    //二维数组降序排列
    public static void sort(String[][] ob,final int[] order)
    {
        Arrays.sort(ob,(Comparator<Object>)(o1,o2) ->
        {
            String[] one = (String[])o1;
            String[] two = (String[])o2;
            for (int k: order)
            {
                if (Integer.parseInt(one[k])<Integer.parseInt(two[k]))
                    return 1;
                else if (Integer.parseInt(one[k])>Integer.parseInt(two[k]))
                    return -1;
            }
            return 0;
        });
    }

    //新增组
    void doAddGroup(String School,String Major,String Class) throws Exception
    {
        String sql = "insert into eims.`level-group` values(?,?,?)";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        statement.setString(1,School);
        statement.setString(2,Major);
        statement.setString(3,Class);
        statement.executeUpdate();
    }

    //删除组
    void doDelGroup(String School,String Major,String Class) throws Exception
    {
        String sql = "delete from eims.`level-group` where School = ? and Major = ? and Class = ?";
        connection = CreateConnection();
        statement = connection.prepareStatement(sql);
        statement.setString(1,School);
        statement.setString(2,Major);
        statement.setString(3,Class);
        statement.executeUpdate();
    }
}
