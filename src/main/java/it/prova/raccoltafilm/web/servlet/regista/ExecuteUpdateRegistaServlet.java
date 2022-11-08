package it.prova.raccoltafilm.web.servlet.regista;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.model.Sesso;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteUpdateRegistaServlet
 */
@WebServlet("/ExecuteUpdateRegistaServlet")
public class ExecuteUpdateRegistaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String nomeParam = request.getParameter("nome");
		String cognomeParam = request.getParameter("cognome");
		String nickNameParam = request.getParameter("nickName");
		String dataDiNascitaParam = request.getParameter("dataDiNascita");
		String sessoParam = request.getParameter("sesso");

		Sesso sessoParsed = StringUtils.isNotBlank(sessoParam) ? Sesso.valueOf(sessoParam) : null;
		Regista example = new Regista(nomeParam, cognomeParam, nickNameParam,
				UtilityForm.parseDateArrivoFromString(dataDiNascitaParam), sessoParsed);
		
		if (!NumberUtils.isCreatable(request.getParameter("id"))) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		
		example.setId(Long.parseLong(request.getParameter("id")));

		// se sono qui i valori sono ok quindi posso creare l'oggetto da inserire
		// occupiamoci delle operazioni di business
		try {
			MyServiceFactory.getRegistaServiceInstance().aggiorna(example);
			request.setAttribute("listaRegistiAttribute", MyServiceFactory.getRegistaServiceInstance().listAllElements());
			request.setAttribute("successMessage", "Operazione effettuata con successo");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		request.getRequestDispatcher("/regista/search.jsp").forward(request, response);
	}

}
