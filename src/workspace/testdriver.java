package workspace;

import java.util.*;

public class testdriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try{
            Connector con= new Connector();
            Order order= new Order();
			
			String result=order.getOrders("login", "user1", con.stmt);
			System.out.println(result);
			con.closeConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
	}

}

