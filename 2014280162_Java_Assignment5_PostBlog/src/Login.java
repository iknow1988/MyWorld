import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");

		System.out.println(request.getParameter("id"));
		System.out.println(request.getParameter("pass"));

		response.getWriter().println(id + pass);
		ArrayList<User> users = new ArrayList<User>();
		try {
			String filename = new String("/WEB-INF/Users.xml");
			ServletContext context = getServletContext();
			users = Parser.ParseUsers(context.getRealPath(filename));
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean result = false;
		for (User user : users) {
			if (user.getUserName().equals(request.getParameter("id"))
					&& (user.getPassword().equals(request.getParameter("pass")))) {
				result = true;
				break;
			}
		}
		response.getWriter().println(result);
		if (result)
			response.getWriter().write("{isSuccess: true}");
		else
			response.getWriter().write("{isSuccess: false}");
	}

}
