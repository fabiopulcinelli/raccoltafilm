package it.prova.raccoltafilm.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;
import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.model.Sesso;
import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.model.Ruolo;

public class UtilityForm {

	public static Regista createRegistaFromParams(String nomeInputParam, String cognomeInputParam,
			String nickNameInputParam, String dataDiNascitaStringParam, String sessoParam) {

		Regista result = new Regista(nomeInputParam, cognomeInputParam, nickNameInputParam);
		result.setSesso(StringUtils.isBlank(sessoParam)?null:Sesso.valueOf(sessoParam));
		result.setDataDiNascita(parseDateArrivoFromString(dataDiNascitaStringParam));
		return result;
	}

	public static boolean validateRegistaBean(Regista registaToBeValidated) {
		// prima controlliamo che non siano vuoti i parametri
		if (StringUtils.isBlank(registaToBeValidated.getNome())
				|| StringUtils.isBlank(registaToBeValidated.getCognome())
				|| StringUtils.isBlank(registaToBeValidated.getNickName()) 
				|| registaToBeValidated.getSesso() == null
				|| registaToBeValidated.getDataDiNascita() == null) {
			return false;
		}
		return true;
	}
	
	public static Film createFilmFromParams(String titoloInputParam, String genereInputParam,
			String minutiDurataInputParam, String dataPubblicazioneStringParam, String registaIdStringParam) {

		Film result = new Film(titoloInputParam, genereInputParam);
		if (NumberUtils.isCreatable(minutiDurataInputParam)) {
			result.setMinutiDurata(Integer.parseInt(minutiDurataInputParam));
		}
		result.setDataPubblicazione(parseDateArrivoFromString(dataPubblicazioneStringParam));
		if (NumberUtils.isCreatable(registaIdStringParam)) {
			result.setRegista(new Regista(Long.parseLong(registaIdStringParam)));
		}
		return result;
	}

	public static Utente createUtenteFromParams(String usernameInputParam, String passwordInputParam,
			String nomeInputParam, String cognomeParam, Date dateCreatedStringParam) {

		Utente result = new Utente(usernameInputParam, passwordInputParam, nomeInputParam, cognomeParam, dateCreatedStringParam);
		return result;
	}
	
	public static boolean validateFilmBean(Film filmToBeValidated) {
		// prima controlliamo che non siano vuoti i parametri
		if (StringUtils.isBlank(filmToBeValidated.getTitolo())
				|| StringUtils.isBlank(filmToBeValidated.getGenere())
				|| filmToBeValidated.getMinutiDurata() == null 
				|| filmToBeValidated.getMinutiDurata() < 1
				|| filmToBeValidated.getDataPubblicazione() == null
				|| filmToBeValidated.getRegista() == null
				|| filmToBeValidated.getRegista().getId() == null 
				|| filmToBeValidated.getRegista().getId() < 1) {
			return false;
		}
		return true;
	}
	
	public static boolean validateUtenteBean(Utente utenteToBeValidated) {
		// prima controlliamo che non siano vuoti i parametri
		if (StringUtils.isBlank(utenteToBeValidated.getUsername())
				|| StringUtils.isBlank(utenteToBeValidated.getPassword())
				|| StringUtils.isBlank(utenteToBeValidated.getNome())
				|| StringUtils.isBlank(utenteToBeValidated.getCognome())
				|| utenteToBeValidated.getDateCreated() == null) {
			return false;
		}
		return true;
	}

	public static Date parseDateArrivoFromString(String dataDiNascitaStringParam) {
		if (StringUtils.isBlank(dataDiNascitaStringParam))
			return null;

		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(dataDiNascitaStringParam);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Map<Ruolo, Boolean> buildCheckedRolesForPages(List<Ruolo> listaTotaleRuoli,
			String[] ruoliFromParams) {
		Map<Ruolo, Boolean> result = new HashMap<>();

		// converto array di string in List di Long
		List<Long> ruoliIdConvertiti = new ArrayList<>();
		for (String stringItem : ruoliFromParams != null ? ruoliFromParams : new String[] {}) {
			ruoliIdConvertiti.add(Long.valueOf(stringItem));
		}

		for (Ruolo ruoloItem : listaTotaleRuoli) {
			result.put(ruoloItem, ruoliIdConvertiti.contains(ruoloItem.getId()));
		}

		return result;
	}
}
