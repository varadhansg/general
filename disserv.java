package dis;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.*;
import dis.BankRecord;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 public class DisplayServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String pageNumber = "";
	 String pageFunction = "";	 
	public DisplayServlet() {
		super();
	}   	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}  	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		pageFunction = request.getParameter("pageFunction");
		pageNumber = request.getParameter("pageNumber");		
		System.out.println("pageFunction : "+pageFunction);
		System.out.println("pageNumber   : "+pageNumber);		
		if(null==pageFunction)	{
		ArrayList list = getBankDetails();
		request.setAttribute("list", list);
		RequestDispatcher dis = request.getRequestDispatcher("result.jsp");
		dis.forward(request, response);
		}

		if(null != pageFunction && pageFunction.equalsIgnoreCase("previous"))	{
		System.out.println("at previous");
		int page = Integer.parseInt(pageNumber);
		page=page-1;
		ArrayList list = getBankDetails();
		request.setAttribute("pageNumber", (new Integer(page)).toString());
		request.setAttribute("list", list);
		RequestDispatcher dis = request.getRequestDispatcher("result.jsp");
		dis.forward(request, response);
		}

		if(null != pageFunction && pageFunction.equalsIgnoreCase("next"))	{
		System.out.println("at next");
		int page = Integer.parseInt(pageNumber);
		page=page+1;
		ArrayList list = getBankDetails();
		request.setAttribute("pageNumber", (new Integer(page)).toString());
		request.setAttribute("list", list);
		RequestDispatcher dis = request.getRequestDispatcher("result.jsp");
		dis.forward(request, response);
		}		
		if(null != pageFunction && pageFunction.equalsIgnoreCase("add")){
		System.out.println("at add");
		int page = Integer.parseInt(pageNumber);
		BankRecord br = new BankRecord();
		br.setBankName((String)request.getParameter("bank5name"));
		br.setBankAddress((String)request.getParameter("bank5address"));
		br.setBankCity((String)request.getParameter("bank5city"));
		br.setBankPinCode((String)request.getParameter("bank5pincode"));
		addBank(br);
		ArrayList list = getBankDetails();
		request.setAttribute("pageNumber", (new Integer(page)).toString());
		request.setAttribute("list", list);
		RequestDispatcher dis = request.getRequestDispatcher("result.jsp");
		dis.forward(request, response);
		}
		if(null != pageFunction && pageFunction.equalsIgnoreCase("update"))
		{
		System.out.println("at update");
		int page = Integer.parseInt(pageNumber);		
		String postalCodes[]=request.getParameterValues("cbox");
		for(int i=0; i<postalCodes.length; i++)
		{ 
			System.out.println(postalCodes[i]);
			java.util.StringTokenizer st = new java.util.StringTokenizer(postalCodes[i],",");
			String pincode = (String)st.nextElement();
			System.out.println("pincode : "+pincode);
			if(!pincode.equals("new"))
			{
				String count = (String)st.nextElement();
				System.out.println("count : "+count);
				BankRecord br = new BankRecord();
				br.setBankName((String)request.getParameter("bank"+count+"name"));
				br.setBankAddress((String)request.getParameter("bank"+count+"address"));
				br.setBankCity((String)request.getParameter("bank"+count+"city"));
				br.setBankPinCode(pincode);			
				updateBank(br);
			}	
		}
		ArrayList list = getBankDetails();
		request.setAttribute("pageNumber", (new Integer(page)).toString());
		request.setAttribute("list", list);
		RequestDispatcher dis = request.getRequestDispatcher("result.jsp");
		dis.forward(request, response);
		}
		if(null != pageFunction && pageFunction.equalsIgnoreCase("delete"))
		{
		System.out.println("at delete");
		int page = Integer.parseInt(pageNumber);
		
		String postalCodes[]=request.getParameterValues("cbox");
		
		for(int i=0; i<postalCodes.length; i++)
		{ 
			System.out.println(postalCodes[i]);
			java.util.StringTokenizer st = new java.util.StringTokenizer(postalCodes[i],",");
			String pincode = (String)st.nextElement();
			System.out.println("pincode : "+pincode);
			if(!pincode.equals("new"))
			{
				String count = (String)st.nextElement();
				System.out.println("count : "+count);
				BankRecord br = new BankRecord();
				br.setBankName((String)request.getParameter("bank"+count+"name"));
				br.setBankAddress((String)request.getParameter("bank"+count+"address"));
				br.setBankCity((String)request.getParameter("bank"+count+"city"));
				br.setBankPinCode(pincode);
				deleteBank(br);
			}	
		}
		ArrayList list = getBankDetails();
		request.setAttribute("pageNumber", (new Integer(page)).toString());
		request.setAttribute("list", list);
		RequestDispatcher dis = request.getRequestDispatcher("result.jsp");
		dis.forward(request, response);
		}
	  }catch(Exception e){ System.out.println(e.getMessage()); }
	}	
	public void addBank(BankRecord br) {		
						try{
							Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
							System.out.println("add bank loaded");
						}
						catch(Exception e){
						}			
						try{
							Connection conn = DriverManager.getConnection("jdbc:derby:C:\\BankDataBase");
							Statement st=conn.createStatement();
							String query = "insert into ADMIN.BANKADDRESS" +
									"(bankname,address,building,postalcode)" +
									" values('"+br.getBankName()+"','"+br.getBankAddress()+"','"
									+br.getBankCity()+"',"+br.getBankPinCode()+")";
							System.out.println("update query : "+query);
							int count = st.executeUpdate(query);
							System.out.println("update count : "+count);							
							if(st != null){st.close();}
							if(conn != null){conn.close();}							
							}
							catch(Exception e){
								System.out.println("Got Exception.... "+e.getMessage());
							}							
					}	
	
	public void updateBank(BankRecord br) {		
						try{
							Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
							System.out.println("update bank loaded");
						}
						catch(Exception e){
						}			
						try{
							Connection conn = DriverManager.getConnection("jdbc:derby:C:\\BankDataBase");
							Statement st=conn.createStatement();
							String query = "update ADMIN.BANKADDRESS set bankname='"+br.getBankName()+"',address='"+br.getBankAddress()+"',building='"+br.getBankCity()+"' where postalcode="+br.getBankPinCode();
							System.out.println("query : "+query);
							int count = st.executeUpdate(query);
							System.out.println("count : "+count);							
							if(st != null){st.close();}
							if(conn != null){conn.close();}															
							}
							catch(Exception e){
								System.out.println("Got Exception.... "+e.getMessage());
							}				
					}
	
	public void deleteBank(BankRecord br) {		
		try{Class.forName("org.apache.derby.jdbc.EmbeddedDriver");}
		catch(Exception e){	}			
		try{
			Connection conn = DriverManager.getConnection("jdbc:derby:C:\\BankDataBase");
			Statement st=conn.createStatement();
			String query = "delete from ADMIN.BANKADDRESS where postalcode="+br.getBankPinCode();
			System.out.println("query : "+query);
			int count = st.executeUpdate(query);
			System.out.println("count : "+count);			
			if(st != null){st.close();}
			if(conn != null){conn.close();}											
			}
			catch(Exception e){System.out.println("GotException."+e.getMessage());}			
	}	
	public ArrayList getBankDetails() {		
		//String bankAddress = "";	
		ArrayList alist = new ArrayList();
						try{
							Class.forName("org.apache.derby.jdbc.EmbeddedDriver");							
						}
						catch(Exception e){}			
						try{
							Connection conn = DriverManager.getConnection("jdbc:derby:C:\\BankDataBase");
							Statement st=conn.createStatement();			
							ResultSet rs=st.executeQuery("select * from ADMIN.BANKADDRESS order by bankname");// where bankname = '"+bankName.toUpperCase()+"'");
							BankRecord bankRecord = null;
							while(rs.next()){
								  bankRecord = new BankRecord();
								  bankRecord.setBankName(rs.getString("bankname"));
								  bankRecord.setBankAddress(rs.getString("address"));
								  bankRecord.setBankCity(rs.getString("building"));
								  //bankRecord.setBankPinCode("test");
								  bankRecord.setBankPinCode(new Integer(rs.getInt("postalcode")).toString());
								  //bankAddress = rs.getString("bankname")+","+rs.getString("address")+","+rs.getString("building")+","+new Integer(rs.getInt("postalcode")).toString();
								alist.add(bankRecord);
								}			
							if(rs != null){rs.close();}
							if(st != null){st.close();}
							if(conn != null){conn.close();}
							}
							catch(Exception e){
								System.out.println("Got Exception.... "+e.getMessage());
							}
							
							return(alist);
					}
	}
