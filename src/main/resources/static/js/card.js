function updateDate() {
	const dateElement = document.getElementById("current-date");
	const now = new Date();
	const options = {
		weekday: 'long',
		year: 'numeric',
		month: 'long',
		day: 'numeric'
	};
	// Génération de la date au format local
	const formattedDate = now.toLocaleDateString('fr-FR', options);
	console.log("Date générée :", formattedDate);
	dateElement.textContent = formattedDate;
}
// Mettre à jour la date au chargement de la page
$(document).ready(function(){
	updateDate();
	// Optionnel : Actualiser la date chaque minute
	setInterval(updateDate, 60000);
})
