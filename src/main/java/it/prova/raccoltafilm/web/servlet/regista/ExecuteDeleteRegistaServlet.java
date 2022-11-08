package it.prova.raccoltafilm.web.servlet.regista;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import it.prova.raccoltafilm.exceptions.DeleteRegistaConFilms;
import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.service.FilmService;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;

/**
 * Servlet implementation class ExecuteDeleteRegistaServlet
 */
@WebServlet("/ExecuteDeleteRegistaServlet")
public class ExecuteDeleteRegistaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// injection del Service
	private RegistaService registaService;
	private FilmService filmService;

	public ExecuteDeleteRegistaServlet() {
			this.registaService = MyServiceFactory.getRegistaServiceInstance();
			this.filmService = MyServiceFactory.getFilmServiceInstance();
		}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idRegistaParam = request.getParameter("idRegista");

		if (!NumberUtils.isCreatable(idRegistaParam)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore!");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		try {
			
			Regista registaParam = registaService.caricaSingoloElemento(Long.parseLong(idRegistaParam));
			Film example = new Film(registaParam);

			List<Film> filmRegista = filmService.findByExample(example);

			if (filmRegista.size() != 0) {
				throw new DeleteRegistaConFilms("Impossibile rimuovere un regista con dei film associati.");
			}

			registaService.rimuovi(Long.parseLong(idRegistaParam));
			
		} catch (DeleteRegistaConFilms e) {
			request.setAttribute("errorMessage", "Impossibile rimuovere un regista con dei film associati.");
			request.getRequestDispatcher("home").forward(request, response);
			return;		
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore!!");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		response.sendRedirect("ExecuteListRegistaServlet?operationResult=SUCCESS");
	}

}
