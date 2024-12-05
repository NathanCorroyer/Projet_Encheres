$(document).ready(function() {
	$('.toggle-status').click(function(e) {
		e.preventDefault();  // Empêche la navigation vers une autre page
		var userId = $(this).data('id');
		var rowElement = $('#userRow_' + userId);

		$.ajax({
			type: 'POST',
			url: '/admin/users/activation/' + userId,
			success: function(response) {
				var linkElement = rowElement.find('.toggle-status');

				if (response.actif) {
					linkElement.text(messages.desactiverUser);  // Utilise le message injecté
					linkElement.attr('data-actif', true);
				} else {
					linkElement.text(messages.activerUser);
					linkElement.attr('data-actif', false);
				}
				alert(messages.updateSuccess);  // Affiche un message de succès
			},
			error: function() {
				alert(messages.updateError);  // Affiche un message d'erreur
			}
		});
	});


	$('.delete-user').click(function(e) {
		e.preventDefault();  // Empêche la navigation vers une autre page
		var userId = $(this).data('id');
		var rowElement = $('#userRow_' + userId);

		$.ajax({
			type: 'POST',
			url: '/admin/users/delete/' + userId,
			success: function(response) {
				var linkElement = rowElement.find('.toggle-status');
				rowElement.remove();
				alert(messages.updateSuccess);  // Affiche un message de succès
			},
			error: function() {
				alert(messages.updateError);  // Affiche un message d'erreur
			}
		});
	});

	$('.delete-categorie').click(function(e) {
		e.preventDefault();  // Empêche la navigation vers une autre page
		var categorieId = $(this).data('id');
		var rowElement = $('#categorieRow_' + categorieId);

		$.ajax({
			type: 'POST',
			url: '/categories/delete/' + categorieId,
			success: function(response) {
				var linkElement = rowElement.find('.delete-categorie');
				rowElement.remove();
				alert(response.message);  // Affiche un message de succès
			},
			error: function(xhr, status, error) {
				// Si une erreur se produit
				var errorMessage = xhr.responseJSON ? xhr.responseJSON.message : "Erreur inconnue";
				alert(errorMessage);  // Affiche le message d'erreur
			}
		});
	});
});

// Method to reset the URL in the fragment-encheres-actives
function resetForm() {
	document.getElementById("filterForm").reset();
	document.getElementById("filterForm").submit();
	window.history.pushState({}, document.title, window.location.pathname);
}