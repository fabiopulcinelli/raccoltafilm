<!doctype html>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="it" class="h-100" >
	 <head>
	 
	 	<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
	   
	   <title>Modifica elemento</title>
	 </head>
	   <body class="d-flex flex-column h-100">
	   
	   		<!-- Fixed navbar -->
	   		<jsp:include page="../navbar.jsp"></jsp:include>
	    
			
			<!-- Begin page content -->
			<main class="flex-shrink-0">
			  <div class="container">
			  
			  		<div class="alert alert-danger alert-dismissible fade show ${errorMessage==null?'d-none':'' }" role="alert">
					  ${errorMessage}
					  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
					</div>
					<div class="alert alert-danger alert-dismissible fade show d-none" role="alert">
					  Esempio di operazione fallita!
					  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
					</div>
					<div class="alert alert-info alert-dismissible fade show d-none" role="alert">
					  Aggiungere d-none nelle class per non far apparire
					   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
					</div>
			  
			  <div class='card'>
				    <div class='card-header'>
				        <h5>Modifica questo elemento</h5> 
				    </div>
				    <div class='card-body'>
		
							<form method="post" action="ExecuteUpdateFilmServlet" class="row g-3" novalidate="novalidate">
							
								<c:set var="filmInPagina" value = "${update_film_attr}"/>
							
								<div class="col-md-6">
									<label for="titolo" class="form-label">Titolo <span class="text-danger"></span></label>
									<input type="text" name="titolo" id="titolo" class="form-control" placeholder="Inserire il titolo" value="${update_film_attr.titolo }">
								</div>
								
								<div class="col-md-6">
									<label for="genere" class="form-label">Genere <span class="text-danger"></span></label>
									<input type="text" name="genere" id="genere" class="form-control" placeholder="Inserire il genere" value="${update_film_attr.genere }">
								</div>
							
								<fmt:formatDate pattern='yyyy-MM-dd' var="parsedDate" type='date' value='${update_film_attr.dataPubblicazione}' />
								<div class="col-md-6">
									<label for="dataPubblicazione" class="form-label">Data di Pubblicazione <span class="text-danger"></span></label>
	                        		<input class="form-control" id="dataPubblicazione" type="date" placeholder="dd/MM/yy" 
	                        				title="formato : gg/mm/aaaa"  name="dataPubblicazione" value="${parsedDate}" >
								</div>
								
								<div class="col-md-6">
									<label for="minutiDurata" class="form-label">Durata (minuti) <span class="text-danger"></span></label>
									<input type="number" class="form-control" name="minutiDurata" id="minutiDurata" placeholder="Inserire la durata" value="${update_film_attr.minutiDurata }">
								</div>
								
								
								<div class="col-md-6">
									<label for="regista.id">Regista <span class="text-danger"></span></label>
								    <select class="form-select" id="regista.id" name="regista.id">
								    	<option value="${update_film_attr.regista.id}" selected>Regista di questo film</option>
								      	<c:forEach items="${registi_list_attribute}" var="registaItem">
								      		<option value="${registaItem.id}" ${update_film_attr.regista.id == registaItem.id?'selected':''} >${registaItem.nome } ${registaItem.cognome }</option>
								      	</c:forEach>
								    </select>
								</div>
								
								<input type="hidden" name="id" value="${filmInPagina.id}">
								
							<div class="col-12">
								<button type="submit" name="submit" value="submit" id="submit" class="btn btn-warning">Conferma</button>
							</div>
		
						</form>
				    
					<!-- end card-body -->			   
				    </div>
				<!-- end card -->
				</div>		
					  
			    
			  <!-- end container -->  
			  </div>
			  
			</main>
			
			<!-- Footer -->
			<jsp:include page="../footer.jsp" />
</body>
</html>