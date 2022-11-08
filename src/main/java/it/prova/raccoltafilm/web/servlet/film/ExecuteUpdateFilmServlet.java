package it.prova.raccoltafilm.web.servlet.film;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.model.Sesso;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteUpdateFilmServlet
 */
@WebServlet("/ExecuteUpdateFilmServlet")
public class ExecuteUpdateFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 	String titoloParam = request.getParameter("titolo");
		String genereParam = request.getParameter("genere");
		String dataPubblicazioneParam = request.getParameter("dataPubblicazione");
		String minutiDurataParam = request.getParameter("minutiDurata");
		String registaIdParam = request.getParameter("regista.id");

		
		// preparo un bean (che mi serve sia per tornare in pagina
		// che per inserire) e faccio il binding dei parametri
		Film example = UtilityForm.createFilmFromParams(titoloParam, genereParam, minutiDurataParam,
				dataPubblicazioneParam, registaIdParam);
		
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
			MyServiceFactory.getFilmServiceInstance().aggiorna(example);
			request.setAttribute("listaFilmAttribute", MyServiceFactory.getFilmServiceInstance().listAllElements());
			request.setAttribute("successMessage", "Operazione effettuata con successo");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		request.getRequestDispatcher("/film/search.jsp").forward(request, response);
	}

}
