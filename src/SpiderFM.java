import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderFM {

public static void main(String[] args) throws SQLException {
	
	
	String USER = "system";
    String PASSWORD = "Qwe123rty456";
	String DRVIER = "oracle.jdbc.OracleDriver";
    String URL = "jdbc:oracle:thin:@127.0.0.1:1521:sqlclass";
    Connection connection = null;
    // 创建预编译语句对象，一般都是用这个而不用Statement
    PreparedStatement pstm = null;
    // 创建一个结果集对象
    ResultSet rs = null;//数据库

    try {
		Class.forName(DRVIER);
	} catch (ClassNotFoundException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
		JOptionPane.showMessageDialog(null, "出错", "未启动驱动", JOptionPane.ERROR_MESSAGE);
	}
	try {
		
		connection = DriverManager.getConnection(URL, USER, PASSWORD);
	} catch (SQLException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
		JOptionPane.showMessageDialog(null, "出错", "未连接上数据库", JOptionPane.ERROR_MESSAGE);
	}
    
    //爬虫
	String timenow="";
		String url="http://www.yypt.com/home/finprod/";//
		try {
			Document doc=Jsoup.connect(url).get();
			Elements item=doc.getElementsByClass("prod-table");//wrapper924 clearfix
			int num=item.size();
			String []name=new String[num];
			String []style=new String[num];
			String []danger=new String[num];
			String []dateb=new String[num];
			String []datee=new String[num];
			String []interest=new String[num];
			String []least=new String[num];
			String []time=new String[num];
			String []owner=new String[num];
			String sql;
			for(int i=0;i<num;i++)
			{
			Element body=item.get(i);
			Elements td=body.select("a");
			Elements td2=body.select("td");
			Element styletd=td2.get(1);
			Element interesttd=td2.get(2);
			Element timetd=td2.get(3);
			Element leasttd=td2.get(5);
			Element dangertd=td2.get(6);
			Element datetd=td2.get(10);
			Element ownertd=td2.get(11);
			name[i]=td.text();
			style[i]=styletd.text();
			interest[i]=interesttd.text();
			time[i]=timetd.text().substring(0,timetd.text().length()-1);
			
			
			String regEx="[^0-9]";  
			Pattern p = Pattern.compile(regEx);  
			Matcher m = p.matcher(leasttd.text()); 
			least[i]=m.replaceAll("").trim();
			danger[i]=dangertd.text().substring(5);
			dateb[i]=datetd.text().substring(5,11);
			datee[i]=datetd.text().substring(12);
			owner[i]=ownertd.text().substring(5);
			System.out.println(name[i]);
			System.out.println(style[i]);
			System.out.println(interest[i]);
			System.out.println(time[i]);
			System.out.println(least[i]);
			System.out.println(danger[i]);
			System.out.println(dateb[i]);
			System.out.println(datee[i]);
			Date dd=new Date();
			//格式化
			SimpleDateFormat sim=new SimpleDateFormat("yyyyMMddHHmmss");
			timenow=sim.format(dd)+i;

			sql ="select COUNT(*) from PRODUCTIOF where name ='"+name[i]+"'";
			pstm = connection.prepareStatement(sql);
	         rs = pstm.executeQuery();
	         
	         if(rs.next())
	         {	 if(rs.getInt(1)==0)
	         {
	        	 sql = "INSERT INTO PRODUCTIOF VALUES('"+timenow+"','"+name[i]+"','"+style[i]+"','"+danger[i]+"','"+ time[i]+"','"+interest[i]+"','"+least[i]+"','"+dateb[i]+"','"+datee[i]+"','"+owner[i]+"')";
	         	pstm = connection.prepareStatement(sql);
	 	         rs = pstm.executeQuery();
	         }
	         }
	         
	         
			/*sql = "INSERT INTO PRODUCTIOF VALUES('"+timenow+"','"+name[i]+"','"+style[i]+"','"+danger[i]+"','"+ time[i]+"','"+interest[i]+"','"+least[i]+"','"+dateb[i]+"','"+datee[i]+"')";
        	pstm = connection.prepareStatement(sql);
	         rs = pstm.executeQuery();
			*/
			}
			
			
			
			
			
			
			
		    //Elements flag=body.getElementsByTag("foreach:OwnProducts");

			/*
			
			for(int i=0;i<num;i++)
			{
				Element body=item.get(i);
				
			}
			*/
			
			/*
			System.out.println(styletd.text());
			System.out.println(interesttd.text());
			System.out.println(timetd.text());
			System.out.println(leasttd.text());
			System.out.println(dangertd.text());
			System.out.println(datetd.text());
			*/
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
}
