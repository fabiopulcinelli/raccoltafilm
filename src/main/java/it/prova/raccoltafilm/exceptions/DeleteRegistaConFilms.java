package it.prova.raccoltafilm.exceptions;

public class DeleteRegistaConFilms extends RuntimeException {
private static final long serialVersionUID = 1L;
	
	public DeleteRegistaConFilms(String message) {
		super(message);
	}
}
