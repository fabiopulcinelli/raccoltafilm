package it.prova.raccoltafilm.web.servlet.film;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.service.FilmService;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;
import it.prova.raccoltafilm.utility.UtilityForm;

@WebServlet("/ExecuteSearchFilmServlet")
public class ExecuteSearchFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// injection del Service
	private FilmService filmService;

	public ExecuteSearchFilmServlet() {
		this.filmService = MyServiceFactory.getFilmServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String titoloParam = request.getParameter("titolo");
		String genereParam = request.getParameter("genere");
		String dataDiPubblicazioneParam = request.getParameter("dataDiPubblicazione");
		String durataParam = request.getParameter("durata");
		String idRegistaParam = request.getParameter("regista.id");
		
		Integer durataParsed = StringUtils.isNotBlank(durataParam) ? Integer.parseInt(durataParam) : null;
		Long idParsed = StringUtils.isNotBlank(idRegistaParam) ? Long.parseLong(idRegistaParam) : null;
		
		RegistaService registaService = MyServiceFactory.getRegistaServiceInstance();
		Regista regista;
		// controllo se l'id del regista non e' nullo perche' senno lo setto direttamente Regista a null
		if(idParsed!=null) {
			try {
				regista = registaService.caricaSingoloElemento(idParsed);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
				request.getRequestDispatcher("/film/search.jsp").forward(request, response);
				return;
			}
		}
		else
			regista = null;

		Film example = new Film(titoloParam, genereParam,
				UtilityForm.parseDateArrivoFromString(dataDiPubblicazioneParam), durataParsed, regista);

		try {
			request.setAttribute("film_list_attribute", filmService.findByExample(example));
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/film/search.jsp").forward(request, response);
			return;
		}
		request.getRequestDispatcher("/film/list.jsp").forward(request, response);
	}

}
