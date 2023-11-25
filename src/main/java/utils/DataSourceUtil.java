package utils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSourceUtil {

    //定义一个数据源
    private static DataSource dataSource;
    private static final Logger logger = Logger.getLogger(DataSourceUtil.class.getName());

    //给datasource赋值
    static {
        try {
            //读取连接数据库的基本信息
            //定义输入流
            InputStream is= DataSourceUtil.class.getClassLoader().getResourceAsStream("db.properties");
            //System.out.println(is);
            //定义属性对象
            Properties p=new Properties();
            //加载流
            p.load(is);
            //System.out.println("p:"+p);
            //得到数据源
            dataSource = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(p);
            System.out.println("dataSource:"+dataSource);
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * 得到连接对象
     */
    public static Connection getConnection() {
        //定义一个连接对象
        Connection conn=null;
        try {
            conn= dataSource.getConnection();
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return conn;
    }

    /**
     * 执行所有查询
     * @param sql  sql语句
     * @param c  实体类对象
     * @param params  给占位符传值的参数
     */
    public <T> List<T> executeQueryList(String sql, Class<T> c, Object... params) {

         //定义连接对象
        Connection conn=null;
        //定义语句对象
        PreparedStatement pstmt=null;
        //定义结果集
        ResultSet rs;
        //定义一个list
        List<T> dataList=new ArrayList<>();

        try {
            //判断sql是否为空
            if(sql==null || sql.trim().isEmpty()) {
                //直接返回list
                return dataList;
            }

            //获取conn连接
            conn = DataSourceUtil.getConnection();
            //创建语句
            pstmt=conn.prepareStatement(sql);
            //给占位符赋值
            for(int i=0;params!=null&&i<params.length;i++) {

                pstmt.setObject(i+1,params[i]);
            }
            //执行查询
            rs=pstmt.executeQuery();

            //获取查询数据的字段个数
            ResultSetMetaData metaData = rs.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();

            //取出记录的次数
            while (rs.next()) {
                //定义一个对象，用来装一条记录
                //反射产生对象
                T t = c.newInstance();
                //循环放入
                for(int i=1;i<=columnCount;i++) {
                    //获取字段的名称
                    String columnName=metaData.getColumnName(i);
                    //获取字段的值
                    Object value=rs.getObject(columnName);
                    //取数据
                    Field field=c.getDeclaredField(columnName);
                    //获取属性可访问
                    field.setAccessible(true);
                    //设置属性值
                    field.set(t,value);
                    //把属性可访问权限设置为false
                    field.setAccessible(false);
                }
                //把得到数据放入集合
                dataList.add(t);
            }


        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }finally {

            //关闭数据源
            close(conn,pstmt,null);
        }

        return dataList;
    }

    /**
     * 查询单条
     * @param sql
     * @param c
     * @param params
     */
    public <T> T executeQueryOne(String sql,Class<T> c,Object... params) {
        return executeQueryList(sql, c, params).isEmpty() ?null:executeQueryList(sql,c,params).get(0);
    }

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (conn != null) {
                conn.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }


    /**
     * 修改:可以执行添加、删除、修改单条操作
     * @param sql
     * @param params
     * @return
     */
    public int executeUpdate(String sql,Object...params){
        Connection conn = null;
        PreparedStatement st = null;
        int i=0;
        try {
            //参数校验
            if(sql==null || sql.trim().isEmpty()){
                return 0;
            }
            //获取连接
            conn = DataSourceUtil.getConnection();
            //创建Statement对象
            st = conn.prepareStatement(sql);
            //给sql赋值参数
            for (int n=0;params!=null && params.length>=1 &&n<params.length;n++){
                st.setObject(n+1,params[n]);
            }
            //向mysql发送sql语句，接受结果
            i = st.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }finally {
            //释放资源
            DataSourceUtil.close(conn,st,null);
        }
        return i;
    }

    public int executeInsert(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement st = null;
        int generatedKey = 0; // 用于存储生成的主键值

        try {
            // 参数校验
            if (sql == null || sql.trim().isEmpty()) {
                return 0;
            }

            // 获取连接
            conn = DataSourceUtil.getConnection();

            // 创建Statement对象，并指定返回生成的主键
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // 给sql赋值参数
            for (int n = 0; params != null && params.length >= 1 && n < params.length; n++) {
                st.setObject(n + 1, params[n]);
            }

            // 向数据库发送SQL语句，执行插入操作
            int rowsAffected = st.executeUpdate();

            // 获取生成的主键值
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            // 释放资源
            DataSourceUtil.close(conn, st, null);
        }

        return generatedKey;
    }


    /*public static void main(String[] args) {
        Connection connection = getConnection();
        System.out.println(connection);

        DataSourceUtil ds=new DataSourceUtil();
        List<Stu> stuList=ds.executeQueryList("select * from stu", Stu.class);
        stuList.forEach(System.out::println);
    }*/
}
