package cs305_2022;

import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.*;

import java.sql.*;

public class MainFile {
    //A hashmap storing a pair of paramtype and the query for a queryID
    static Map<String, ArrayList<String>> my_dict = new HashMap<>();

    public Connection conn=null;
    public Statement stmt=null;
    public ResultSet rs=null;   //resultset for select queries

    public void setup_connection(String url){
        try {
            // setting up connection for the database
            // getting url from unit tests
            conn =
                    DriverManager.getConnection(url);
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
/*
        try {
            stmt = conn.createStatement();
            return stmt;
            rs=stmt.executeQuery(str);
            if (stmt.execute(str)) {
                rs = stmt.getResultSet();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    }
                    System.out.println("");
                }
                return rs;
            }
            else{
                stmt.executeUpdate(str);
                System.out.println("Done inserting or deleting or updating.");
                return rs;
            }
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;*/
    }

    public static String edit_regex(String str, Object param, String queryId) {
        //checking if paramType matches with the type of queryParam class
        if (!(param.getClass().getSimpleName().equals(my_dict.get(queryId).get(0)) || param.getClass().getName().equals(my_dict.get(queryId).get(0)))){
            throw new RuntimeException("ParamType does not match.");
        }

        //regex to extract the variables of type ${...}
        String regex = "(\\$\\{[^}]+\\})";
        Pattern pattern = Pattern.compile(regex);
        StringBuilder buffer = new StringBuilder();
        Matcher matcher = pattern.matcher(str);
        System.out.println(param.getClass().getName());

        // if object is of primitive data type, all the variables are replaced with the same value as passed in the  queryParam
        if(param.getClass().getName().equals("java.lang.Integer") ||
                param.getClass().getName().equals("java.lang.Short") ||
                param.getClass().getName().equals("java.lang.Long") || param.getClass().getName().equals("java.lang.String") ||
                param.getClass().getName().equals("java.lang.Float") || param.getClass().getName().equals("java.lang.Double") ||
                param.getClass().getName().equals("java.lang.Byte") || param.getClass().getName().equals("java.lang.Boolean") ||
                param.getClass().getName().equals("java.sql.Date") || param.getClass().getName().equals("java.sql.Timestamp")){
            while(matcher.find()){
                matcher.appendReplacement(buffer, "\'"+param.toString()+"\'");
            }
        }

        // if object is an array type, all the variables are replaced with the same value as passed in the  queryParam
        else if(param.getClass().getName().equals("[I") || param.getClass().getName().equals("[Ljava.lang.Double") ||
                param.getClass().getName().equals("[Ljava.lang.Object") || param.getClass().getName().equals("[Ljava.lang.Short") ||
                param.getClass().getName().equals("[F") || param.getClass().getName().equals("[J") || param.getClass().getName().equals("[S") ||
                param.getClass().getName().equals("[D") || param.getClass().getName().equals("[C") || param.getClass().getName().equals("[B") ){
            while(matcher.find()) {
                String temp = "(";
                int len = Array.getLength(param);
                for (int i = 0; i < len - 1; i++) {
                    temp += Array.get(param, i);temp += ',';
                }
                temp += Array.get(param, len - 1);
                temp += ')';
                System.out.println(temp);
                matcher.appendReplacement(buffer,  temp);
            }
        }
        else { // when object is normal object
            while (matcher.find()) {
                String arg_name = matcher.group().substring(2, matcher.group().length() - 1);
                Class<?> c = param.getClass();
                Field para;
                String temp;
                try {
                    para = c.getDeclaredField(arg_name);
                    temp = para.get(param).toString();

                } catch (NoSuchFieldException | IllegalAccessException nsfe) {throw new RuntimeException(nsfe);
                }

                matcher.appendReplacement(buffer, "\'" + temp + "\'");
            }
        }
        //replacing it with the varibles of the query
        matcher.appendTail(buffer);
        System.out.println(buffer.toString());
        return buffer.toString();
    }

    public static void add_to_dict(Map<String, ArrayList<String>> my_dict, Document doc) {
        // Adding the queries and their paramTypes corresponding to that queryId.
        NodeList nList = doc.getElementsByTagName("sql");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String my_id = eElement.getAttribute("id");
                my_dict.put(my_id, new ArrayList<>());
                my_dict.get(my_id).add(eElement.getAttribute("paramType"));
                my_dict.get(my_id).add(doc.getElementsByTagName("sql").item(temp).getTextContent());
            }
        }

    }

    public void decide_type(Field para,String columnType,Object obj,ResultSet rs, Integer i) throws SQLException, IllegalAccessException {
//        if(Objects.equals(columnType, "java.lang.String")){
//            para.set(obj,rs.getString(i));
//        }
//        else if(Objects.equals(columnType, "java.lang.Integer")){
//            para.setInt(obj,rs.getInt(i));
//        }
//        else if(Objects.equals(columnType, "java.lang.Long")){
//            para.setLong(obj,rs.getLong(i));
//        }
//        else if(Objects.equals(columnType, "java.lang.Short")){
//            para.setShort(obj,rs.getShort(i));
//        }
//        else if(Objects.equals(columnType, "java.lang.Float")){
//            para.setFloat(obj,rs.getFloat(i));
//        }
//        else if(Objects.equals(columnType, "java.lang.Double")){
//            para.setDouble(obj,rs.getDouble(i));
//        }
//        else if(Objects.equals(columnType, "java.lang.Byte")){
//            para.setByte(obj,rs.getByte(i));
//        }
//        else if(Objects.equals(columnType, "java.lang.Boolean")){
//            para.setBoolean(obj,rs.getBoolean(i));
//        }
//        else if(Objects.equals(columnType, "java.sql.Timestamp")){
//            para.set(obj,rs.getTimestamp(i));
//        }
//        else if(Objects.equals(columnType, "java.sql.Date")){
//            para.set(obj,rs.getDate(i));
//        }
//        else{
            para.set(obj,rs.getObject(i));
        //}
    }


    public Object select_in_sql(String queryid, Object queryparam, Class<?> resultType) {
        Object obj=null;
        try {
            obj = resultType.getDeclaredConstructor().newInstance();
        }
        catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException n){};
        String str=edit_regex(String.valueOf(my_dict.get(queryid).get(1)),queryparam,queryid);
        //System.out.println(str);
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            if (stmt.execute(str)) {
                rs = stmt.getResultSet();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                rs.last();
                if(rs.getRow()==0){
                    System.out.println("No such row found");
                    return null;
                }
                if(rs.getRow()!=1){
                    System.out.println("Multiple rows found!");
                    return new Object();
                }
                rs.first();
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnName=rsmd.getColumnName(i);
                    //System.out.println(columnName);
                    String columnType= String.valueOf(rsmd.getColumnClassName(i));
                    //System.out.println(columnType);
                    Field para;
                    para=resultType.getDeclaredField(columnName);
                    decide_type(para,columnType,obj,rs,i);
                }
            }
            else{
                System.out.println("Not a select query.");
                throw new RuntimeException("Not a select query");
                //TODO add exception here....it is still running the command like inserting ...dont do that.
            }
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (NoSuchFieldException | IllegalAccessException e) {e.printStackTrace();
        }
        return  obj;
    }

    public List<?> many_in_sql(String queryid, Object queryparam, Class<?> resultType)  {

        List<Object> lis=new ArrayList<>();
        String str=edit_regex(String.valueOf(my_dict.get(queryid).get(1)),queryparam,queryid);
        //System.out.println(str);

        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs;
            if (stmt.execute(str)) {
                rs = stmt.getResultSet();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while(rs.next()) {
                    Object obj= resultType.getDeclaredConstructor().newInstance();
                    for (int i = 1; i <= columnsNumber; i++){
                        String columnName = rsmd.getColumnName(i);
                        String columnType = String.valueOf(rsmd.getColumnClassName(i));
                        Field para;
                        //System.out.println(columnName);
                        //System.out.println(rsmd.getColumnLabel(i));
                        para = resultType.getDeclaredField(columnName);
                        decide_type(para,columnType,obj,rs,i);

                    }
                    lis.add(obj);
                }

            }
            else{
                System.out.println("Not a select query.");
                throw new RuntimeException("Not a select query");
                //TODO add exception here
            }
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {e.printStackTrace();
        }
        return lis;
    }

    public  int insert_in_sql(String queryid, Object queryparam){
        String str=edit_regex(String.valueOf(my_dict.get(queryid).get(1)),queryparam,queryid);

        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(str);
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return 0;
    }

    public  int delete_in_sql(String queryid, Object queryparam){
        String str=edit_regex(String.valueOf(my_dict.get(queryid).get(1)),queryparam,queryid);

        Statement stmt;
        try {
            stmt = conn.createStatement();
            if (stmt.executeUpdate(str)==0) {
                System.out.println("Not an insert/delete query.");
                throw new RuntimeException("No such row found");
                //TODO add exception here
            }
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return 0;
    }



    public MainFile() throws ParserConfigurationException, IOException, SAXException {

            File input = new File("src/main/config.xml");
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = fact.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            doc.getDocumentElement().normalize();
            add_to_dict(my_dict, doc);


    }
}