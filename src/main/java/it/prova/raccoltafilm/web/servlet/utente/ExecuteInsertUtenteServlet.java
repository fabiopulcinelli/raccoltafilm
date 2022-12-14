package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.model.Ruolo;
import it.prova.raccoltafilm.service.FilmService;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;
import it.prova.raccoltafilm.service.RuoloService;
import it.prova.raccoltafilm.service.UtenteService;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteInsertUtenteServlet
 */
@WebServlet("/ExecuteInsertUtenteServlet")
public class ExecuteInsertUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// injection del Service
	private UtenteService utenteService;
	private RuoloService ruoloService;

	public ExecuteInsertUtenteServlet() {
		this.utenteService = MyServiceFactory.getUtenteServiceInstance();
		this.ruoloService = MyServiceFactory.getRuoloServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// estraggo input
		String usernameParam = request.getParameter("username");
		String passwordParam = request.getParameter("password");
		String conferma_passwordParam = request.getParameter("conferma_password");
		String nomeParam = request.getParameter("nome");
		String cognomeParam = request.getParameter("cognome");
		Date dateCreazione = new Date();
		
		Utente utenteInstance = UtilityForm.createUtenteFromParams(usernameParam, passwordParam, nomeParam,
				cognomeParam, dateCreazione);
		
		try {
			Ruolo temp = new Ruolo();
			String[] ruoli = request.getParameterValues("checkruoli");
			Set<Ruolo> ruoliParam = new HashSet<>(0);
			
			if(ruoli!=null) {
				for(int i = 0; i < ruoli.length; i++) {
					temp.setId(Long.parseLong(ruoli[i]));
					ruoliParam.add(temp);
				}
				System.out.println(ruoliParam.size());
			}
			else {
				request.setAttribute("insert_utente_attr", utenteInstance);
				request.setAttribute("conferma_password", conferma_passwordParam);
				// questo mi serve per la select di registi in pagina
				request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
				request.setAttribute("errorMessage", "Inserire almeno un ruolo!");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}
			
			List<Ruolo> ruoliList = new ArrayList<>(ruoliParam);
			
			//setto i ruoli
			for (String stringItem : ruoli!=null?ruoli:new String[] {}) {
				utenteInstance.getRuoli().add(ruoloService.caricaSingoloElemento(Long.valueOf(stringItem)));
			}
			
			// se la validazione non risulta ok
			if (!UtilityForm.validateUtenteBean(utenteInstance)) {
				request.setAttribute("insert_utente_attr", utenteInstance);
				request.setAttribute("conferma_password", conferma_passwordParam);
				request.setAttribute("ruoliList", 
						UtilityForm.buildCheckedRolesForPages(ruoloService.listAll(), ruoli));
				// questo mi serve per la select di registi in pagina
				request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
				request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}
			if(!passwordParam.equals(conferma_passwordParam)) {
				request.setAttribute("insert_utente_attr", utenteInstance);
				request.setAttribute("conferma_password", conferma_passwordParam);
				request.setAttribute("ruoliList", ruoliList);
				// questo mi serve per la select di registi in pagina
				request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
				request.setAttribute("errorMessage", "Le password non combaciano!");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}

			// se sono qui i valori sono ok quindi posso creare l'oggetto da inserire
			// occupiamoci delle operazioni di business
			utenteService.inserisciNuovo(utenteInstance);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si ?? verificato un errore.");
			request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		// uso il sendRedirect con parametro per evitare il problema del double save on
		// refresh
		request.getRequestDispatcher("/utente/search.jsp").forward(request, response);

	}
}
